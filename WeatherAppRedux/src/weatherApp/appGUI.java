package weatherApp;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.*;

public class appGUI extends JFrame {
	public appGUI() {
		super("WeatherApp");
		setSize(450,650);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(null);
		setResizable(false);
		
		addGUIComponents();
	}

	private void addGUIComponents() {
		//Text field for search
		JTextField search = new JTextField();
		search.setBounds(15,15,351,45); //x, y, width, height
		search.setFont(new Font("Dialog",Font.PLAIN,24)); //Font constructor (name, font 'value', font size)
		add(search);
		
		//weatherImage
		JLabel weatherImg = new JLabel(loadImage("src/Icons/clear.png"));
		weatherImg.setBounds(0,125,450,217);
		add(weatherImg);
		
		//tempText
		JLabel temperatureText = new JLabel("10°C");
		temperatureText.setBounds(0,350,450,54);
		temperatureText.setFont(new Font("Dialog",Font.PLAIN,24));
		temperatureText.setHorizontalAlignment(SwingConstants.CENTER);
		add(temperatureText);
		
		//city text
		JLabel cityText = new JLabel("CITY NOT FOUND");
		cityText.setBounds(0,400,450,54);
		cityText.setFont(new Font("Dialog",Font.PLAIN,30));
		cityText.setHorizontalAlignment(SwingConstants.CENTER);
		add(cityText);
		
		//humidity text
		JLabel humidityText = new JLabel("<html><b>Humidity<b> 100%<html>");
		humidityText.setFont(new Font("Dialog",Font.PLAIN,16));
		humidityText.setBounds(90,500,85,55);
		add(humidityText);
		
		//humidity picture
		JLabel humidityImage = new JLabel(loadImage("src/Icons/humidity.png"));
		humidityImage.setBounds(15,500,74,66);
		add(humidityImage);
		
		//windspeed pic
		JLabel windspeedImage = new JLabel(loadImage("src/Icons/windspeed.png"));
		windspeedImage.setBounds(220,500,74,66);
		add(windspeedImage);
		
		//windspeed text
		JLabel windText = new JLabel("<html><b>Windspeed<b> <html>");
		windText.setFont(new Font ("Dialog", Font.PLAIN,16));
		windText.setBounds(315,500,85,55);
		add(windText);
		
		//searchButton
		JButton searchButton = new JButton(loadImage("src/Icons/search.png"));
		searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		searchButton.setBounds(375,13,47,45);
		
		searchButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String input = search.getText();
					if (input.replaceAll("\\s","").length() <= 0)
						return;
					WeatherData weather = new WeatherData(input);
											
					if ((weather.condition).toLowerCase().contains("cloudy")){
						weatherImg.setIcon(loadImage("src/Icons/cloudy.png"));
					}
					else if ((weather.condition).toLowerCase().contains("rain")){
						weatherImg.setIcon(loadImage("src/Icons/rain.png"));
					}
					else if ((weather.condition).toLowerCase().contains("snow")){
						weatherImg.setIcon(loadImage("src/Icons/snow.png"));
					}
					else{
						weatherImg.setIcon(loadImage("src/Icons/clear.png"));
					}
					
					temperatureText.setText(weather.temperature + weather.tempUnits);
					windText.setText(weather.windSpeed + weather.speedUnits);
					/* TODO: Figure out how to represent missing cities' data w/o errors
					 * Add a relevant weather field to replace humidity
					 * Improve runtime
					 * */
				}
		});
		add(searchButton);
	}
	
	private ImageIcon loadImage(String path) {
		try {
			BufferedImage image = ImageIO.read(new File(path));
			return new ImageIcon(image);
		}catch(Exception e) {
			System.out.println("Resource Not Found");
			return null;
		}
	}
}
