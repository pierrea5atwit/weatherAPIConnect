
package weatherApp ;
import java.io.BufferedReader ;
import java.io.InputStreamReader ;
import java.net.HttpURLConnection ;
import java.net.URL ;
import javax.swing.SwingUtilities ;

/**
 * class Main establishes main loop for UI, and contains method for API connection
 * 
 * @author Andrew Pierre
 * @version v2.4 2025-01-24 Javadoc Implementation
 * */
public class Main
    {

    /**
     * Main method, runs application and attempts connection to API
     * 
     * @param args unused
     */
    public static void main( String[] args )
        {
        SwingUtilities.invokeLater( new Runnable()
            {

            @Override
            public void run()
                {
                new appGUI().setVisible( true ) ;

                }

            } ) ;

        }


    /**
     * Method that connects program to API
     * 
     * @param link URL as a String, passed to URLConnect as a 'URL' object
     * @return API content or empty string if connection fails
     */
    @SuppressWarnings( "deprecation" )
    public static String ConnectAPI( String link )
        {
        try
            {
            URL url = new URL( link ) ;
            HttpURLConnection connection = (HttpURLConnection) url.openConnection() ;
            connection.setRequestMethod( "GET" ) ;

            int responseCode = connection.getResponseCode() ;
            if ( responseCode != 200 )
                System.out.println( "Failed Connection!" ) ;

            BufferedReader in = new BufferedReader( new InputStreamReader( connection.getInputStream() ) ) ;
            String inputLine ;
            String content = "" ;
            while ( ( inputLine = in.readLine() ) != null )
                {
                content += inputLine ;

                }

            in.close() ;
            connection.disconnect() ;
            return content ;

            }
        catch ( Exception any)
            {
            any.printStackTrace() ;
            return "" ;

            }

        }

    }
