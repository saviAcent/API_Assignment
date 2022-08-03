package resources;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class Utils {
	
	public static String getGlobalProperty(String key) throws IOException {
		Properties prop = new Properties();
		FileInputStream file = new FileInputStream("C:\\Users\\AD-028\\APIAssignment\\API_Assignment\\src\\resources\\data.properties");
		prop.load(file);
		return prop.getProperty(key);
	}

	public static String getJsonPath(String response, String key) {
		 JsonPath js = new JsonPath(response);	
		 String jsn = js.getString(key);
		 return jsn;
	}
}
