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
	public int weatherCode;
	public String condition;

	/* WeatherData constructor
 	* param data is info provided from geolocation api, boolean US determines units of measurement
  	*/
	public WeatherData(String data,boolean US) {
		latitude = Double.parseDouble(parseData("latitude", data));
		longitude = Double.parseDouble(parseData("longitude", data));
		
		//changing the link, connecting to a weather API to pull this longitude's data
		String url = String.format("https://api.open-meteo.com/v1/forecast?latitude=%f&longitude=%f&current=temperature_2m,rain,weather_code,wind_speed_10m",latitude,longitude);
		data = Main.ConnectAPI(url);
		
		tempUnits = parseData("temperature_2m",data);
		speedUnits = parseData("wind_speed_10m",data);
		
		data = data.substring(findIndex("current",data));

		temperature = Double.parseDouble(parseData("temperature_2m",data));
		windSpeed = Double.parseDouble(parseData("wind_speed_10m",data));
		
		weatherCode = Integer.parseInt(parseData("weather_code",data));
		condition = weatherCode(weatherCode);

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
			windSpeed = (windSpeed/1.609344);
		}
		time = numTime + time.substring(time.indexOf(":"));
	}
	
	/*
	 * method takes the string we're looking for in our string of data,finds the index of the value
	 * returns index
	 * purpose: make parsing easier 
	 * 
	 * */
	public static int findIndex(String x,String data) {
		int length = (x+"\":").length();
		int y = data.indexOf(x+"\":") + length;
		return y;
	}
	
	/*
	 * method used to replace repetitive substring usage to get pieces of string-ified JSON data
	 * returns string, still needs to use parseInt/parseDouble for string returned
	 * 
	 * param var - keyword we're looking for
	 * param src - the 'source', in this case its our long string of data
	 * */
	public static String parseData(String var,String src) {
		int x = findIndex(var,src);
		String result;
		try {
		 result = (src.substring(x,src.indexOf(",",x)).replaceAll("\"", ""));
		}catch(Exception e) {
			result = src.substring(x,src.indexOf("}",x)).replaceAll("\"", "");
		}
		return result;
		
	}

	/* Method that determines weather condition using U.S weathercodes, provided by API
  	*/
	public String weatherCode(int c) {
		String cond = "";
		if (c == 0)
			cond = "clear";
		else if (c <= 3)
			cond = "partly cloudy";
		else if (c == 45 || c == 48)
			cond = "fog";
		else if (c >= 51 && c <= 57)
			cond = "light rain/drizzle";
		else if (c == 61)
			cond = "slight rain";
		else if (c == 63)
			cond = "moderate rain";
		else if (c >= 65 && c <= 67)
			cond = "heavy rain";
		else if (c == 71)
			cond = "light snowfall";
		else if (c == 73)
			cond = "moderate snowfall";
		else if (c == 75)
			cond = "heavy snowfall";
		else if (c == 80)
			cond = "light rain showers";
		else if (c == 81)
			cond = "moderate rain showers";
		else if (c == 85)
			cond = "slight snow showers";
		else if (c == 86)
			cond = "heavy snow showers";
		else if (c == 95)
			cond = "thunderstorms";
		return cond;
	}
	
	/* toString method for weatherData objects, prints relevant information */
	public String toString() {
		return String.format("'s weather as of %s is %s at approximately %.1f%s, with windspeeds of %.1f%s.",time,condition,temperature,tempUnits,windSpeed,speedUnits);
	}
	
}
