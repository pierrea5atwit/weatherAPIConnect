package weatherApp ;

/**
 * class WeatherData used to parse API-provided JSON data into useable data on location's weather, allows for translation on UI
 * 
 * @author Andrew Pierre
 * @version v2.4 2025-01-24 Javadoc Implementation
 */
public class WeatherData
    {

    public int listIndex, weatherCode ;
    public boolean multipleCities, us_measure, valid ;
    public double temperature, windSpeed, latitude, longitude ;
    public String url, userIn, tempUnits, speedUnits, time, condition, city ;
    public String[] cities ;

    /**
     * constructor for objects of 'weatherdata' that holds geographic location and
     * measure of weather
     * 
     * @param lo
     *     name of the location to be found w/ open-meteo's geocoding
     */
    public WeatherData( String lo )
        {
        this.valid = true ;
        this.us_measure = false ;
        this.userIn = lo ;

        String link = String.format( "https://geocoding-api.open-meteo.com/v1/search?name=%s&count=10&language=en&format=json",
                                     lo ) ;
        String locationInfo = Main.ConnectAPI( link ) ;

        // if the city not found, length < 50
        if ( locationInfo.length() < 50 )
            this.valid = false ;

        if ( this.valid )
            {
            locationInfo = locationInfo.substring( locationInfo.indexOf( "," ) ) ;
            this.cities = locationInfo.split( "\\{" ) ; // parses cities w/ same name.
            this.multipleCities = ( this.cities.length > 1 )
                ? true
                    : false ;
            this.listIndex = 0 ;

            for ( int i = 0 ; i < this.cities.length ; i++ )
                {
                this.cities[ i ] = this.cities[ i ].replaceAll( "\\{", " " ) ;
                this.cities[ i ] = this.cities[ i ].replaceAll( "}", " " ) ;

                }

            if ( this.multipleCities )
                {
                this.city = String.format( "%s, %s",
                                      this.userIn.replaceAll( "\\+", " " ),
                                      WeatherData.parseData( "admin1", this.cities[ this.listIndex ] ) ) ;
                setWeatherData( this.cities[ this.listIndex ] ) ;

                }
            else
                {
                this.city = String.format( "%s, %s",
                                      this.userIn.replaceAll( "\\+", " " ),
                                      WeatherData.parseData( "admin1", this.cities[ 0 ] ) ) ;
                setWeatherData( this.cities[ 0 ] ) ;

                }

            }
        else
            noData() ;

        }


    /**
     * method updates data for each measurement in weatherData object
     * 
     * @param data
     *     geographic data for city: contains latitude, longitude, and 'admin'
     *     (country, state, province, etc.)
     */
    public void setWeatherData( String data )
        {
        this.latitude = Double.parseDouble( parseData( "latitude", data ) ) ;
        this.longitude = Double.parseDouble( parseData( "longitude", data ) ) ;

        // changing the link, connecting to a weather API to pull this longitude's
        // data
        String url = String.format( "https://api.open-meteo.com/v1/forecast?latitude=%f&longitude=%f&current=temperature_2m,rain,weather_code,wind_speed_10m",
                                    this.latitude,
                                    this.longitude ) ;
        data = Main.ConnectAPI( url ) ;

        this.tempUnits = parseData( "temperature_2m", data ) ;
        this.speedUnits = parseData( "wind_speed_10m", data ) ;

        data = data.substring( findIndex( "current", data ) ) ;

        this.temperature = Double.parseDouble( parseData( "temperature_2m", data ) ) ;
        this.windSpeed = Double.parseDouble( parseData( "wind_speed_10m", data ) ) ;

        this.weatherCode = Integer.parseInt( parseData( "weather_code", data ) ) ;
        this.condition = weatherCode( this.weatherCode ) ;

        this.time = parseData( "time", data ) ;
        this.time = this.time.substring( this.time.indexOf( "T" ) + 1 ) ;

        int numTime = Integer.parseInt( this.time.substring( 0, this.time.indexOf( ":" ) ) ) ;
        numTime -= 5 ;

        if ( this.us_measure )
            {
            numTime = ( ( ( numTime % 12 ) + 12 ) % 12 ) ;
            if ( numTime == 0 )
                numTime = 12 ;
            this.tempUnits = "°F" ;
            this.temperature = ( this.temperature * 1.8 ) + 32 ;
            this.speedUnits = "mph" ;
            this.windSpeed = ( this.windSpeed / 1.609344 ) ;

            }
        else
            {
            this.tempUnits = "°C" ;
            this.speedUnits = "km/h" ;

            }

        this.time = numTime + this.time.substring( this.time.indexOf( ":" ) ) ;

        }


    /**
     * defaults all displayable values in case city not found or doesn't exist
     */
    public void noData()
        {
        this.multipleCities = this.us_measure = false ;
        this.temperature = this.windSpeed = this.latitude = this.longitude = 0.0 ;
        this.tempUnits = this.speedUnits = this.condition = this.city = this.time = "N/A" ;

        }


    /**
     * reset weatherData object to next city w/ same name
     */
    public void nextCity()
        {
        if ( !this.multipleCities )
            return ;
        else
            {
            this.listIndex = ( this.listIndex + 1 ) % this.cities.length ;
            this.city = String.format( "%s, %s",
                                  this.userIn.replaceAll( "\\+", " " ),
                                  WeatherData.parseData( "admin1", this.cities[ this.listIndex ] ) ) ;
            setWeatherData( this.cities[ this.listIndex ] ) ;

            }

        }


    /**
     * Set weather object to previous city
     */
    public void prevCity()
        {
        if ( !this.multipleCities )
            return ;
        else
            {
            this.listIndex-- ;
            if ( this.listIndex < 0 )
                this.listIndex = this.cities.length - 1 ;
            this.city = String.format( "%s, %s",
                                  this.userIn.replaceAll( "\\+", " " ),
                                  WeatherData.parseData( "admin1", this.cities[ this.listIndex ] ) ) ;
            setWeatherData( this.cities[ this.listIndex ] ) ;

            }

        }


    /**
     * method takes string we're searching for in string-formatted json data, finds
     * index of value returns index to make parsing easier
     * 
     * @param x
     *     is the data related to substring we're looking for
     * 
     * @return index of 'x'
     */
    public static int findIndex( String x,
                                 String data )
        {
        int length = ( x + "\":" ).length() ;
        int y = data.indexOf( x + "\":" ) + length ;
        return y ;

        }


    /**
     * Separates pieces of string-formatted JSON data
     * 
     * @param var
     *     keyword being searched for
     * @param src
     *     'source', string JSON data
     * 
     * @return data preceded by keyword as string. Can be parsed as int or double
     *     accordingly.
     */
    public static String parseData( String var,
                                    String src )
        {
        int x = findIndex( var, src ) ;
        String result ;
        try
            {
            result = ( src.substring( x, src.indexOf( ",", x ) ).replaceAll( "\"", "" ) ) ;

            }
        catch ( Exception e )
            {
            result = src.substring( x, src.indexOf( "}", x ) ).replaceAll( "\"", "" ) ;

            }

        return result ;

        }


    /**
     * Translates weather code to text
     * 
     * @param c
     *     is given weather code
     * 
     * @return weather associated with each code
     */
    public String weatherCode( int c )
        {
        String cond = "" ;
        if ( c == 0 )
            cond = "clear" ;
        else if ( c <= 3 )
            cond = "partly cloudy" ;
        else if ( c == 45 || c == 48 )
            cond = "fog" ;
        else if ( c >= 51 && c <= 57 )
            cond = "light rain/drizzle" ;
        else if ( c == 61 )
            cond = "slight rain" ;
        else if ( c == 63 )
            cond = "moderate rain" ;
        else if ( c >= 65 && c <= 67 )
            cond = "heavy rain" ;
        else if ( c == 71 )
            cond = "light snowfall" ;
        else if ( c == 73 )
            cond = "moderate snowfall" ;
        else if ( c == 75 )
            cond = "heavy snowfall" ;
        else if ( c == 80 )
            cond = "light rain showers" ;
        else if ( c == 81 )
            cond = "moderate rain showers" ;
        else if ( c == 85 )
            cond = "slight snow showers" ;
        else if ( c == 86 )
            cond = "heavy snow showers" ;
        else if ( c == 95 )
            cond = "thunderstorms" ;
        return cond ;

        }


    /**
     * Switches temperature units between Celsius and Fahrenheit
     * 
     * @param WeatherData
     */
    public static void updateWeather( WeatherData obj )
        {
        if ( obj.us_measure )
            {
            obj.tempUnits = "°F" ;
            obj.temperature = ( obj.temperature * 1.8 ) + 32 ;
            obj.speedUnits = "mph" ;
            obj.windSpeed /= 1.609344 ;

            }
        else
            {
            obj.tempUnits = "°C" ;
            obj.temperature = ( obj.temperature - 32 ) / 1.8 ;
            obj.speedUnits = "km/h" ;
            obj.windSpeed *= 1.609344 ;

            }

        }

    }
