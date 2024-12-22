# WeatherApp written in Java using open-meteo and geocoding API 

## Description:
The WeatherApp is a lightweight software application used to quickly check the weather conditions of in any city in the world, showing Temperature in degrees Celsius or Fahrenheit, time, and wind speed.

## Key Features:

1. City Data Management:
   - Takes user's input from the search text field and searches for that city's name via geolocation.
   - Updates weather information (e.g., windspeed, latitude and longitude, temperature) if found, display that the city isn't found otherwise.
   - Creates a list of cities' data if multiple locations have the same name.

2. API Connectivity:
   - Establishes a connection between the Java application and multiple APIs used to get information about cities.
   - Implements proper handling for invalid response codes, non-existent/unavailable cities, and other related issues.

3. Simple, User-friendly Interface:
   - Design is an intuitive user interface (UI) for users to interact with the system efficiently.
  
   
## Technologies Used:

- Java: The main language used for both logic and UI implementation.
- Javax.swing Library: Java library used to create and customize the interface.
- URLConnect Library: Java library used to access open-source API's used to get location and weather information.
