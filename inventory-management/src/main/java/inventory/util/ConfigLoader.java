package inventory.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
	
	private Properties properties = null;
	private static ConfigLoader instance = null;
	private String proFileName = "config.properties";
	
	private ConfigLoader() {
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(proFileName);
		if (inputStream != null) {
			properties = new Properties();
			try {
				properties.load(inputStream);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static ConfigLoader getInstance() {
		if (instance == null) {
			instance = new ConfigLoader();
		}
		return instance;
	}
	
	public String getValue(String key) {
		if (properties.containsKey(key)) {
			return properties.getProperty(key);
		}
		return null;
	}
}