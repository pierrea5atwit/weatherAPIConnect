package weatherApp;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherData {
	public double temperature;
	public double windSpeed;
	public String url;
	public double latitude;
	public double longitude;
	public String tempUnits;
	public String speedUnits;
	public String time;
	
	public WeatherData(String data,boolean US) {
		latitude = Double.parseDouble(parseData("latitude", data));
		longitude = Double.parseDouble(parseData("longitude", data));
		
		//changing the link, connecting to a weather API to pull this longitude's data
		String url = String.format("https://api.open-meteo.com/v1/forecast?latitude=%f&longitude=%f&current=temperature_2m,wind_speed_10m",latitude,longitude);
		data = ConnectAPI(url);
		
		tempUnits = parseData("temperature_2m",data);
		speedUnits = parseData("wind_speed_10m",data);
		
		data = data.substring(findIndex("current",data));

		temperature = Double.parseDouble(parseData("temperature_2m",data));
		windSpeed = Double.parseDouble(parseData("wind_speed_10m",data));

		time = parseData("time",data);	
		time = time.substring(time.indexOf("T")+1);
		
		int numTime = Integer.parseInt(time.substring(0,time.indexOf(":")));
		numTime -= 4;
		
		if (US) {
			numTime = (numTime % 12);
			if (numTime == 0)
				numTime = 12;
			tempUnits = "°F";
			temperature = (temperature*1.8)+32;
			speedUnits = "mph";
			windSpeed = windSpeed/1.609344;
		}
		time = numTime + time.substring(time.indexOf(":"));
	}
	
	/*
	 * method intakes the string we're looking for in our JSON data,finds the index of the result
	 * returns index
	 * purpose: make parsing easier 
	 * 
	 * */
	public int findIndex(String x,String data) {
		int length = (x+"\":").length();
		int y = data.indexOf(x+"\":") + length;
		return y;
	}
	
	/*
	 * method used to replace repetitive substring usage to get pieces of string-ified JSON data
	 * returns string, still needs to use parseInt/parseDouble for string returned
	 * 
	 * params var - keyword we're looking for
	 * param src - the 'source', in this case its our long string of data
	 * */
	public String parseData(String var,String src) {
		int x = findIndex(var,src);
		String result = "0.0";
		try {
		 result = (src.substring(x,src.indexOf(",",x)).replaceAll("\"", ""));
		}catch(Exception e) {
			result = src.substring(x,src.indexOf("}",x)).replaceAll("\"", "");
		}
		return result;
		
	}
	
	/* toString method for weatherData objects, prints relevant information */
	public String toString() {
		return String.format("Current weather as of %s is approximately %.1f%s, with windspeeds of %.1f%s",time,temperature,tempUnits,0.0,speedUnits);
	}
	
	public static String ConnectAPI(String link) {
	      try {
	          URL url = new URL(link);
	          HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	          connection.setRequestMethod("GET");
	
	          int responseCode = connection.getResponseCode();
	          if(responseCode != 200)
	        	  System.out.println("Failed Connection!");
	
	          BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	          String inputLine;
	          String content = "";
	          while ((inputLine = in.readLine()) != null) {
	              content += inputLine;
	          }
	          in.close();
	          connection.disconnect();
	          return content;
	      } catch (Exception e) {
	          e.printStackTrace();
	          return "";
	      }
	  }
}
