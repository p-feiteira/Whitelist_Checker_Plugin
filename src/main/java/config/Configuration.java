package config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;

public class Configuration{
	private static final String CONFIG_PATH = "F:"+File.separator+"Users"+File.separator+"Pedro Feiteira"+File.separator+"Documents"+File.separator+"GitHub"+File.separator+"Whitelist_Checker_Plugin"+File.separator+"src"+File.separator+"main"+File.separator+"java"+File.separator+"config" + File.separator + "configs.properties";
	private final HashMap<String, String> ipWhiteList;
	private boolean updated = false;
	private final Logger logger;

	public Configuration(Logger logger) throws IOException {
		this.logger = logger;
		ipWhiteList = new HashMap<>();
		load();
	}
	
	public String isInWhiteList(String username) {
		return ipWhiteList.get(username);
	}
	
	public void updateWhiteList(String username, String IP) {
		updated = true;
		ipWhiteList.put(username, IP);
	}
		
	private void load() throws IOException {
		InputStream inputStream = new FileInputStream(CONFIG_PATH);
        Properties properties = new Properties();
        properties.load(inputStream);
        
        for(Object key : properties.keySet()) {
        	String sKey = (String) key;
        	System.out.println(sKey);
        	String sValue = (String)properties.getProperty(sKey);
        	System.out.println(sValue);
        	ipWhiteList.put(sKey, sValue);
        }
	}
	
	public void store() throws IOException {
		
		if(updated) {
			Properties properties = new Properties();

			for (Map.Entry<String, String> entry : ipWhiteList.entrySet()) {
				properties.put(entry.getKey(), entry.getValue());
			}
			File file = new File(CONFIG_PATH);
			if (!file.exists()) {
				file.createNewFile();
			}
			properties.store(new FileOutputStream(file), null);
			logger.info("Saving state..");
		}else {
			logger.info("White list not updated");
		}
	}
}
