package weatherApp;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		boolean repeat = false;
		String answer;
		boolean valid = true;
		boolean us_measure;
		
		do {
		System.out.print("Please input the name of the city you want to check: ");
		String cityName = sc.nextLine();
		cityName = cityName.replaceAll(" ","+");
		
		//link to API
		String link = String.format("https://geocoding-api.open-meteo.com/v1/search?name=%s&count=10&language=en&format=json",cityName);
		String locationInfo = ConnectAPI(link); //if user types in an invalid place, len data will be short.
		
		if (locationInfo.length() < 50)
			valid = false;
		else
			valid = true;
		
		if (valid) //if the city they typed exists
		{
		locationInfo = locationInfo.substring(locationInfo.indexOf(","));
		
		String[] cities = locationInfo.split("\\{"); //removes extra { in JSON data, splits info up by location in case of multiple cities w/ same name.
		
		//using this to properly format strings, might not be needed
		for (int i = 0;i<cities.length;i++) {
			cities[i] = cities[i].replaceAll("\\{", " ");
			cities[i] = cities[i].replaceAll("}", " ");
		}
		
		//iterate over cities to make sure we're checking the right place before collecting weatherData
		for (int i = 0;i<cities.length;i++) {
				System.out.printf("%s, %s, %s? (yes/no): ",cityName.replaceAll("\\+", " "), WeatherData.parseData("admin1", cities[i]),WeatherData.parseData("admin2", cities[i]));
				answer = sc.nextLine();
				if (answer.toLowerCase().equals("yes")) { //if we got it, continue w/ program
					locationInfo = cities[i];
					break;
				}
				else { //else (if we run out of options), invalid city; prompt user to try again
					if (i == cities.length-1) {
						valid = false; 
						System.out.println("City not found.");
					}
				}
			}
		}
		else
			System.out.println("City not found.");
		
		//if we still have a city, keep going, else just reset loop
		if (valid) {
			System.out.print("Would you like to use US measurements or global? (input 'US' or 'GLOBAL') ");
			String measurements = sc.nextLine();			
			if (measurements.toUpperCase().equals("GLOBAL"))
				us_measure = false;
			else
				us_measure = true;
			WeatherData ex = new WeatherData(locationInfo,us_measure);
			System.out.println(cityName.replaceAll("\\+", " ") + ex);
		}
		
		System.out.print("Would you like to input another city? (yes/no): ");
		answer = sc.nextLine().toLowerCase();
		if (answer.equals("yes"))
			repeat = true;
		else
			repeat = false;
		//end of program, continues do-while loop
		}while(repeat);
		
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
