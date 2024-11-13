package weatherApp;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
//import org.json.JSONObject;
import java.util.Scanner;

import javax.swing.SwingUtilities;

public class Main {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new appGUI().setVisible(true);
			}
		});
	}
	
	@SuppressWarnings("deprecation")
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
