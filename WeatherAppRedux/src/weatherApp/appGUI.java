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
	JTextField search;
	JLabel temperatureText,windText,cityText,timeText,clockImg,weatherImg,windspeedImg;
	JButton searchButton, arrowUp, arrowDown, measurement;
	
	public appGUI() {
		super("WeatherApp");
		setSize(450,650);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(null);
		setResizable(false);
		
		addGUIComponents();
	}

	/**
	 * Adds all buttons/UI components necessary for weather app
	 * */
	private void addGUIComponents() {
		//Text field for search
		search = new JTextField();
		search.setBounds(15,15,351,45); //x, y, width, height
		search.setFont(new Font("Dialog",Font.PLAIN,24)); //Font constructor (name, font 'value', font size)
		add(search);
		
		//Temperature text
		temperatureText = new JLabel("XX°");
		temperatureText.setBounds(0,350,450,54);
		temperatureText.setFont(new Font("Dialog",Font.PLAIN,24));
		temperatureText.setHorizontalAlignment(SwingConstants.CENTER);
		add(temperatureText);
		
		//Windspeed text
		windText = new JLabel("<html><b>Windspeed<b> XX<html>");
		windText.setFont(new Font ("Dialog", Font.PLAIN,16));
		windText.setBounds(315,500,85,55);
		add(windText);
		
		//city text
		cityText = new JLabel("Enter City Above!");
		cityText.setBounds(0,400,450,54);
		cityText.setFont(new Font("Dialog",Font.PLAIN,30));
		cityText.setHorizontalAlignment(SwingConstants.CENTER);
		add(cityText);
		
		//TIME text
		timeText = new JLabel("<html><b>Time<b> XX:XX<html>");
		timeText.setFont(new Font("Dialog",Font.PLAIN,16));
		timeText.setBounds(90,490,85,55);
		add(timeText);
		
		//humidity picture
		JLabel clockImg = new JLabel(loadImage("src/Icons/clock.png"));
		clockImg.setBounds(15,500,85,55);
		add(clockImg);

		//weatherImage, 245x217 pixels
		weatherImg = new JLabel(loadImage("src/Icons/clear.png"));
		weatherImg.setBounds(0,125,450,217);
		add(weatherImg);
		
		//windspeed picture
		windspeedImg = new JLabel(loadImage("src/Icons/windspeed.png"));
		windspeedImg.setBounds(220,500,74,66);
		add(windspeedImg);
		
		//searchButton
		searchButton = new JButton(loadImage("src/Icons/search.png"));
		searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		searchButton.setBounds(375,13,47,45);
		searchButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String input = search.getText().replaceAll(" ", "+");
					if (input.replaceAll("\\s","").length() <= 0)
						return;
					weather = new WeatherData(input);
					
					if(!weather.valid){
						weatherImg.setIcon(loadImage("src/Icons/ERROR.png"));
					}
					else if ((weather.condition).toLowerCase().contains("cloudy")){
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
					updateVisual(weather.valid);
				}
		});
		add(searchButton);
		
		//switch measurement button
		measurement = new JButton("*F / *C");
		measurement.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		measurement.setBounds(15,65,175,45);
		measurement.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				weather.us_measure = !weather.us_measure;
				updateMeasure();
			}
		});
		add(measurement);
		
		//Up Arrow Button (next city)
		arrowUp = new JButton("↑");
		arrowUp.setFont(new Font("Dialog",Font.BOLD,20));
		arrowUp.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		arrowUp.setBounds(375,130,47,45);
		arrowUp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				weather.nextCity();
				updateVisual(weather.valid);
			}
		});
		add(arrowUp);
		
		//Down Arrow Button (previous city)
		arrowDown = new JButton("↓");
		arrowDown.setFont(new Font("Dialog",Font.BOLD,20));
		arrowDown.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		arrowDown.setBounds(375,170,47,45);
		arrowDown.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {	
				weather.prevCity();
				updateVisual(weather.valid);
			}
		});
		add(arrowDown);
	}
	
	/**
	 * sets image/icon for buttons and other elements
	 * 
	 * @param path is the filepath where the img is loaded from
	 * */
	private ImageIcon loadImage(String path) {
		try {
			BufferedImage image = ImageIO.read(new File(path));
			return new ImageIcon(image);
		}catch(Exception e) {
			System.out.println("Resource Not Found");
			return null;
		}
	}
	
	/**
	 * Updates UI when units of measure are changed
	 * */
	public void updateMeasure() {
		WeatherData.updateWeather(weather);
		temperatureText.setText(String.format("<html>%.1f%s<html>",weather.temperature, weather.tempUnits));
		windText.setText(String.format("<html><b>Windspeed</b> %.1f<html>",weather.windSpeed) + weather.speedUnits);
		updateVisual(weather.valid);
	}
	
	/**
	 * Update UI to match weather data for given city
	 * 
	 * @param available suggests whether or not data is found; if not found, default data displayed
	 * */
	public void updateVisual(boolean available) {
		if (available) {
			cityText.setText(weather.city);
			temperatureText.setText(String.format("<html>%.1f%s<html>",weather.temperature, weather.tempUnits));
			windText.setText(String.format("<html><b>Windspeed</b> %.1f<html>",weather.windSpeed) + weather.speedUnits);
			timeText.setText(String.format("<html><b>As of time:<b> %s<html>",weather.time));
		}else {
			cityText.setText("City not found!");
			temperatureText.setText(String.format("N/A"));
			windText.setText(String.format("<html><b>Windspeed: XX</b><html>"));
			timeText.setText(String.format("<html><b>As of time: <b>XX:XX<html>"));
		}
	}
}
