package weatherApp;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
//import org.json.JSONObject;
import java.util.Scanner;


//public class URLConnectionExample {
//    
//}

public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.print("Please input the name of the city you want to check: ");
		String cityName = sc.next();
		System.out.print("Would you like to use US measurements or global? (input 'US' or 'GLOBAL') ");
		String measurements = sc.next();
		boolean us_measure;
		
		if (measurements.toUpperCase().equals("GLOBAL"))
			us_measure = false;
		else
			us_measure = true;
		cityName = cityName.replaceAll(" ","+");
		
		String link = String.format("https://geocoding-api.open-meteo.com/v1/search?name=%s&count=10&language=en&format=json",cityName);
		String locationInfo = ConnectAPI(link);
		locationInfo = locationInfo.substring(locationInfo.indexOf(":")); //removes extra {
		//Find a way to parse location info so that each 'location', lat/long and country/admin, are split and 
		//packed into an array or arraylist, then find a way to ask user if they tb that specific place and iterate if no
		
		System.out.printf("%s's weather..%n", cityName);
		WeatherData ex = new WeatherData(locationInfo,us_measure);
		System.out.print(ex);
		sc.close();
		
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
