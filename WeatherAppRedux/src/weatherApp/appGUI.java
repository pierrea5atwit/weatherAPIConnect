package weatherApp;

import java.awt.Color;
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
	WeatherData weather;
	
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
		
		//Temperature text
		JLabel temperatureText = new JLabel("XX°");
		temperatureText.setBounds(0,350,450,54);
		temperatureText.setFont(new Font("Dialog",Font.PLAIN,24));
		temperatureText.setHorizontalAlignment(SwingConstants.CENTER);
		add(temperatureText);
		
		//Windspeed text
		JLabel windText = new JLabel("<html><b>Windspeed<b> <html>");
		windText.setFont(new Font ("Dialog", Font.PLAIN,16));
		windText.setBounds(315,500,85,55);
		add(windText);
		
		//city text
		JLabel cityText = new JLabel("Enter City Above!");
		cityText.setBounds(0,400,450,54);
		cityText.setFont(new Font("Dialog",Font.PLAIN,30));
		cityText.setHorizontalAlignment(SwingConstants.CENTER);
		add(cityText);
		
		//TIME text
		JLabel timeText = new JLabel("<html><b>Time<b> XX:XX<html>");
		timeText.setFont(new Font("Dialog",Font.PLAIN,16));
		timeText.setBounds(90,490,85,55);
		add(timeText);
		
		//humidity picture
		JLabel clockImg = new JLabel(loadImage("src/Icons/clock.png"));
		clockImg.setBounds(15,500,85,55);
		add(clockImg);

		//weatherImage, 245x217 pixels
		JLabel weatherImg = new JLabel(loadImage("src/Icons/clear.png"));
		weatherImg.setBounds(0,125,450,217);
		add(weatherImg);
		
		//windspeed picture
		JLabel windspeedImg = new JLabel(loadImage("src/Icons/windspeed.png"));
		windspeedImg.setBounds(220,500,74,66);
		add(windspeedImg);
		
		//searchButton
		JButton searchButton = new JButton(loadImage("src/Icons/search.png"));
		searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		searchButton.setBounds(375,13,47,45);
		searchButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String input = search.getText().replaceAll(" ", "+");
					if (input.replaceAll("\\s","").length() <= 0)
						return;
					weather = new WeatherData(input);
					
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
					
					cityText.setText(weather.city);
					temperatureText.setText(String.format("<html>%.1f%s<html>",weather.temperature, weather.tempUnits));
					windText.setText(String.format("<html><b>Windspeed</b> %.1f<html>",weather.windSpeed) + weather.speedUnits);
					timeText.setText(String.format("<html><b>As of time:<b> %s<html>",weather.time));
					/* TODO: Figure out how to represent missing cities' data w/o errors
					 * */
				}
		});
		add(searchButton);
		
		//switch measurement button
		JButton measurement = new JButton("*F / *C");
		measurement.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		measurement.setBounds(15,65,175,45);
		measurement.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				weather.us_measure = !weather.us_measure;
				if(weather.us_measure) {
				}
				else {
				}
				//TODO: Create method to update weather object w/ measurements
				temperatureText.setText(String.format("<html>%.1f%s<html>",weather.temperature, weather.tempUnits));
				windText.setText(String.format("<html><b>Windspeed</b> %.1f<html>",weather.windSpeed) + weather.speedUnits);
			}
		});
		add(measurement);
		
		//Up Arrow Button (next city)
		JButton arrowUp = new JButton("↑");
		arrowUp.setFont(new Font("Dialog",Font.BOLD,20));
		arrowUp.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		arrowUp.setBounds(375,130,47,45);
		arrowUp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				weather.nextCity();
				cityText.setText(weather.city);
				temperatureText.setText(String.format("<html>%.1f%s<html>",weather.temperature, weather.tempUnits));
				windText.setText(String.format("<html><b>Windspeed</b> %.1f<html>",weather.windSpeed) + weather.speedUnits);
				timeText.setText(String.format("<html><b>As of time:<b> %s<html>",weather.time));
			}
		});
		add(arrowUp);
		
		//Down Arrow Button (previous city)
		JButton arrowDown = new JButton("↓");
		arrowDown.setFont(new Font("Dialog",Font.BOLD,20));
		arrowDown.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		arrowDown.setBounds(375,170,47,45);
		arrowDown.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				String input = search.getText().replaceAll(" ", "+");
//				WeatherData weather = new WeatherData(input);
//				
				weather.prevCity();
				cityText.setText(weather.city);
				temperatureText.setText(String.format("<html>%.1f%s<html>",weather.temperature, weather.tempUnits));
				windText.setText(String.format("<html><b>Windspeed</b> %.1f<html>",weather.windSpeed) + weather.speedUnits);
				timeText.setText(String.format("<html><b>As of time:<b> %s<html>",weather.time));
			}
		});
		add(arrowDown);
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
