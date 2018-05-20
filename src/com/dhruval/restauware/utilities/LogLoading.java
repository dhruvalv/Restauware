package com.dhruval.restauware.utilities;

import java.io.FileInputStream;
import java.util.Properties;

import org.apache.log4j.Category;
import org.apache.log4j.PropertyConfigurator;

public class LogLoading {
	static final Category log = Category.getInstance(LogLoading.class);
	static String LOG_PROPERTIES_FILE = "";
	private RestauwareUtility utility = new RestauwareUtility();

	public LogLoading() {
		initializeLogger();
	}

	private void initializeLogger() {
		Properties logProperties = new Properties();
		try {
			LOG_PROPERTIES_FILE = utility.getLog4jPath() + "\\log4j.properties";
			logProperties.load(new FileInputStream(LOG_PROPERTIES_FILE));
			PropertyConfigurator.configure(logProperties);
			log.info("Logging initialized");
		} catch (Exception e) {
			utility.showAleart("Unable to load logging property due to : " + e.getMessage());
			log.error("Exception", e);
		}
	}

}
