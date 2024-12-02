package weatherApp;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherData {
	public int listIndex, weatherCode;
	public boolean multipleCities, us_measure, valid;
	public double temperature, windSpeed, latitude, longitude;
	public String url, userIn, tempUnits, speedUnits, time, condition, city;
	public String[] cities;
	
	/**
	 * constructor for objects of 'weatherdata' that holds geographic location and
	 * measure of weather
	 * 
	 * @param lo name of the location to be found w/ open-meteo's geocoding
	 * */
	public WeatherData(String lo) {
		valid = true;
		us_measure = false;
		userIn = lo;
		
		String link = String.format("https://geocoding-api.open-meteo.com/v1/search?name=%s&count=10&language=en&format=json",lo);
		String locationInfo = Main.ConnectAPI(link); 
		
		//if the city not found, length < 50
		if (locationInfo.length() < 50)
			valid = false;
				
		if (valid) {
			locationInfo = locationInfo.substring(locationInfo.indexOf(","));
			cities = locationInfo.split("\\{"); //parses cities w/ same name.
			multipleCities = (cities.length > 1) ? true : false;
			listIndex = 0;
			
			for (int i = 0;i<cities.length;i++) {
				cities[i] = cities[i].replaceAll("\\{", " ");
				cities[i] = cities[i].replaceAll("}", " ");
			}
			if(multipleCities){
				city = String.format("%s, %s",userIn.replaceAll("\\+", " "), WeatherData.parseData("admin1", cities[listIndex]));
				setWeatherData(cities[listIndex]);
			}else{
				city = String.format("%s, %s",userIn.replaceAll("\\+", " "), WeatherData.parseData("admin1", cities[0]));
				setWeatherData(cities[0]);
			}
		}
		else
			noData();		
	}
	
	
	/**
	 * method updates data for each measurement in weatherData object
	 * 
	 * @param data geographic data for city: contains latitude, longitude, and 
	 * 'admin' (country, state, province, etc.)
	 * */
	public void setWeatherData(String data) {
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
		numTime -= 5;
		
		if (us_measure) {
			numTime = (((numTime % 12)+12)%12);
			if (numTime == 0)
				numTime = 12;
			tempUnits = "°F";
			temperature = (temperature*1.8)+32;
			speedUnits = "mph";
			windSpeed = (windSpeed/1.609344);
		}
		else {
			tempUnits = "°C";
			speedUnits = "km/h";
		}
		time = numTime + time.substring(time.indexOf(":"));
	}
	
	/**
	 * defaults all displayable values in case city not found or doesn't exist
	 * */
	public void noData() {
		multipleCities = us_measure = false;
		temperature = windSpeed = latitude = longitude = 0.0;
		tempUnits = speedUnits = condition = city = time = "N/A";
	}
	
	/**
	 * reset weatherData object to next city w/ same name
	 * */
	public void nextCity() {
		if (!multipleCities)
			return;
		else {
			listIndex = (listIndex + 1) % cities.length;
			city = String.format("%s, %s",userIn.replaceAll("\\+", " "), WeatherData.parseData("admin1", cities[listIndex]));
			setWeatherData(cities[listIndex]);
		}
	}
	
	/**
	 * Set weather object to previous city
	 * */
	public void prevCity() {
		if(!multipleCities)
			return;
		else {
			listIndex--;
			if (listIndex < 0)
				listIndex = cities.length-1;
			city = String.format("%s, %s",userIn.replaceAll("\\+", " "), WeatherData.parseData("admin1", cities[listIndex]));
			setWeatherData(cities[listIndex]);
		}
	}
	
	/**
	 * method takes string we're searching for in string-formatted json data, finds index of value
	 * returns index to make parsing easier 
	 * 
	 * @param x is the data related to substring we're looking for
	 * @return index of 'x'
	 * */
	public static int findIndex(String x, String data) {
		int length = (x+"\":").length();
		int y = data.indexOf(x+"\":") + length;
		return y;
	}
	
	/**
	 * Separates pieces of string-formatted JSON data
	 * 
	 * @param var keyword being searched for
	 * @param src 'source', string JSON data
	 * @return data preceded by keyword as string. Can be parsed as int or double accordingly.
	 */
	public static String parseData(String var, String src) {
		int x = findIndex(var,src);
		String result;
		try {
		 result = (src.substring(x,src.indexOf(",",x)).replaceAll("\"", ""));
		}catch(Exception e) {
			result = src.substring(x,src.indexOf("}",x)).replaceAll("\"", "");
		}
		return result;
		
	}
	
	/** 
	 * Translates weather code to text
	 * 
	 * @param c is given weather code
	 * @return weather associated with each code
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
	
	/**
	 * Switches temperature units between Celsius and Fahrenheit
	 * 
	 * @param WeatherData 
	 * */
	public static void updateWeather(WeatherData obj) {
		if (obj.us_measure) {
			obj.tempUnits = "°F";
			obj.temperature = (obj.temperature*1.8)+32;
			obj.speedUnits = "mph";
			obj.windSpeed /= 1.609344;
		}
		else{
			obj.tempUnits = "°C";
			obj.temperature = (obj.temperature - 32)/1.8;
			obj.speedUnits = "km/h";
			obj.windSpeed *= 1.609344;
		}
	}
	
}
