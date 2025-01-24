package weatherApp ;

import java.awt.Cursor ;
import java.awt.Font ;
import java.awt.event.ActionEvent ;
import java.awt.event.ActionListener ;
import java.awt.image.BufferedImage ;
import java.io.File ;

import javax.imageio.ImageIO ;
import javax.swing.* ;

/**
 * class "appGUI" is written to translate any <b>WeatherData</b> object into displayed text and images,
 * with default displays and small implementations for error cases such as,
 * <ul>
 * <li> City not found / Does not exist
 * <li> Specific values not available
 * <li> No input given
 * </ul>
 * 
 * @author Andrew Pierre
 * @version v2.4 2025-01-24 Javadoc Implementation
 * */
public class appGUI extends JFrame
    {

    WeatherData weather ;
    JTextField search ;
    JLabel temperatureText, windText, cityText, timeText, clockImg, weatherImg, windspeedImg ;
    JButton searchButton, arrowUp, arrowDown, measurement ;

    /**
     * Method creates app window, sets location and close operation
     */
    public appGUI()
        {
        super( "WeatherApp" ) ;
        setSize( 450, 650 ) ;
        setDefaultCloseOperation( EXIT_ON_CLOSE ) ;
        setLocationRelativeTo( null ) ;
        setLayout( null ) ;
        setResizable( false ) ;

        addGUIComponents() ;

        }


    /**
     * Adds all buttons/UI components necessary for weather app
     */
    private void addGUIComponents()
        {
        // Text field for search
        this.search = new JTextField() ;
        this.search.setBounds( 15, 15, 351, 45 ) ; // x, y, width, height
        this.search.setFont( new Font( "Dialog", Font.PLAIN, 24 ) ) ; // Font constructor
                                                                 // (name, font
                                                                 // 'value', font
                                                                 // size)
        add( this.search ) ;

        // Temperature text
        this.temperatureText = new JLabel( "XX°" ) ;
        this.temperatureText.setBounds( 0, 350, 450, 54 ) ;
        this.temperatureText.setFont( new Font( "Dialog", Font.PLAIN, 24 ) ) ;
        this.temperatureText.setHorizontalAlignment( SwingConstants.CENTER ) ;
        add( this.temperatureText ) ;

        // Windspeed text
        this.windText = new JLabel( "<html><b>Windspeed<b> XX<html>" ) ;
        this.windText.setFont( new Font( "Dialog", Font.PLAIN, 16 ) ) ;
        this.windText.setBounds( 315, 500, 85, 55 ) ;
        add( this.windText ) ;

        // city text
        this.cityText = new JLabel( "Enter City Above!" ) ;
        this.cityText.setBounds( 0, 400, 450, 54 ) ;
        this.cityText.setFont( new Font( "Dialog", Font.PLAIN, 30 ) ) ;
        this.cityText.setHorizontalAlignment( SwingConstants.CENTER ) ;
        add( this.cityText ) ;

        // TIME text
        this.timeText = new JLabel( "<html><b>Time<b> XX:XX<html>" ) ;
        this.timeText.setFont( new Font( "Dialog", Font.PLAIN, 16 ) ) ;
        this.timeText.setBounds( 90, 490, 85, 55 ) ;
        add( this.timeText ) ;

        // humidity picture
        JLabel clockImg = new JLabel( loadImage( "src/Icons/clock.png" ) ) ;
        clockImg.setBounds( 15, 500, 85, 55 ) ;
        add( clockImg ) ;

        // weatherImage, 245x217 pixels
        this.weatherImg = new JLabel( loadImage( "src/Icons/clear.png" ) ) ;
        this.weatherImg.setBounds( 0, 125, 450, 217 ) ;
        add( this.weatherImg ) ;

        // windspeed picture
        this.windspeedImg = new JLabel( loadImage( "src/Icons/windspeed.png" ) ) ;
        this.windspeedImg.setBounds( 220, 500, 74, 66 ) ;
        add( this.windspeedImg ) ;

        // searchButton
        this.searchButton = new JButton( loadImage( "src/Icons/search.png" ) ) ;
        this.searchButton.setCursor( Cursor.getPredefinedCursor( Cursor.HAND_CURSOR ) ) ;
        this.searchButton.setBounds( 375, 13, 47, 45 ) ;
        this.searchButton.addActionListener( new ActionListener()
            {

            @Override
            public void actionPerformed( ActionEvent e )
                {
                String input = appGUI.this.search.getText().replaceAll( " ", "+" ) ;
                if ( input.replaceAll( "\\s", "" ).length() <= 0 )
                    return ;
                appGUI.this.weather = new WeatherData( input ) ;

                if ( !appGUI.this.weather.valid )
                    {
                    appGUI.this.weatherImg.setIcon( loadImage( "src/Icons/ERROR.png" ) ) ;

                    }
                else if ( ( appGUI.this.weather.condition ).toLowerCase().contains( "cloudy" ) )
                    {
                    appGUI.this.weatherImg.setIcon( loadImage( "src/Icons/cloudy.png" ) ) ;

                    }
                else if ( ( appGUI.this.weather.condition ).toLowerCase().contains( "rain" ) )
                    {
                    appGUI.this.weatherImg.setIcon( loadImage( "src/Icons/rain.png" ) ) ;

                    }
                else if ( ( appGUI.this.weather.condition ).toLowerCase().contains( "snow" ) )
                    {
                    appGUI.this.weatherImg.setIcon( loadImage( "src/Icons/snow.png" ) ) ;

                    }
                else
                    {
                    appGUI.this.weatherImg.setIcon( loadImage( "src/Icons/clear.png" ) ) ;

                    }

                updateVisual( appGUI.this.weather.valid ) ;

                }

            } ) ;
        add( this.searchButton ) ;

        // switch measurement button
        this.measurement = new JButton( "*F / *C" ) ;
        this.measurement.setCursor( Cursor.getPredefinedCursor( Cursor.HAND_CURSOR ) ) ;
        this.measurement.setBounds( 15, 65, 175, 45 ) ;
        this.measurement.addActionListener( new ActionListener()
            {

            @Override
            public void actionPerformed( ActionEvent e )
                {
                appGUI.this.weather.us_measure = !appGUI.this.weather.us_measure ;
                updateMeasure() ;

                }

            } ) ;
        add( this.measurement ) ;

        // Up Arrow Button (next city)
        this.arrowUp = new JButton( "↑" ) ;
        this.arrowUp.setFont( new Font( "Dialog", Font.BOLD, 20 ) ) ;
        this.arrowUp.setCursor( Cursor.getPredefinedCursor( Cursor.HAND_CURSOR ) ) ;
        this.arrowUp.setBounds( 375, 130, 47, 45 ) ;
        this.arrowUp.addActionListener( new ActionListener()
            {

            @Override
            public void actionPerformed( ActionEvent e )
                {
                appGUI.this.weather.nextCity() ;
                updateVisual( appGUI.this.weather.valid ) ;

                }

            } ) ;
        add( this.arrowUp ) ;

        // Down Arrow Button (previous city)
        this.arrowDown = new JButton( "↓" ) ;
        this.arrowDown.setFont( new Font( "Dialog", Font.BOLD, 20 ) ) ;
        this.arrowDown.setCursor( Cursor.getPredefinedCursor( Cursor.HAND_CURSOR ) ) ;
        this.arrowDown.setBounds( 375, 170, 47, 45 ) ;
        this.arrowDown.addActionListener( new ActionListener()
            {

            @Override
            public void actionPerformed( ActionEvent e )
                {
                appGUI.this.weather.prevCity() ;
                updateVisual( appGUI.this.weather.valid ) ;

                }

            } ) ;
        add( this.arrowDown ) ;

        }


    /**
     * sets image/icon for buttons and other elements
     * 
     * @param path filepath where the image is loaded from
     * @return ImageIcon picture to be displayed w/ weather
     */
    private static ImageIcon loadImage( String path )
        {
        try
            {
            BufferedImage image = ImageIO.read( new File( path ) ) ;
            return new ImageIcon( image ) ;

            }
        catch ( @SuppressWarnings( "unused" ) Exception any)
            {
            System.out.println( "Resource Not Found" ) ;
            return null ;

            }

        }


    /**
     * Method updates UI when units of measure are changed
     */
    public void updateMeasure()
        {
        WeatherData.updateWeather( this.weather ) ;
        this.temperatureText.setText( String.format( "<html>%.1f%s<html>",
                                                this.weather.temperature,
                                                this.weather.tempUnits ) ) ;
        this.windText.setText( String.format( "<html><b>Windspeed</b> %.1f<html>", this.weather.windSpeed ) +
                          this.weather.speedUnits ) ;
        updateVisual( this.weather.valid ) ;

        }


    /**
     * Update UI to match weather data for given city
     * 
     * @param available
     *     suggests whether or not data is found; if not found, default data
     *     displayed
     */
    public void updateVisual( boolean available )
        {
        if ( available )
            {
            this.cityText.setText( this.weather.city ) ;
            this.temperatureText.setText( String.format( "<html>%.1f%s<html>",
                                                    this.weather.temperature,
                                                    this.weather.tempUnits ) ) ;
            this.windText.setText( String.format( "<html><b>Windspeed</b> %.1f<html>",
                                             this.weather.windSpeed ) +
                              this.weather.speedUnits ) ;
            this.timeText.setText( String.format( "<html><b>As of time:<b> %s<html>", this.weather.time ) ) ;

            }
        else
            {
            this.cityText.setText( "City not found!" ) ;
            this.temperatureText.setText( String.format( "N/A" ) ) ;
            this.windText.setText( String.format( "<html><b>Windspeed: XX</b><html>" ) ) ;
            this.timeText.setText( String.format( "<html><b>As of time: <b>XX:XX<html>" ) ) ;

            }

        }

    }
