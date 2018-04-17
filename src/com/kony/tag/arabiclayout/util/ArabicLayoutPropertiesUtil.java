package com.kony.tag.arabiclayout.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

import com.kony.tag.config.CodeReviewStatus;
import com.kony.tag.config.ProjectConstants;
import com.kony.tag.util.eclipse.JSReviewUtil;

public class  ArabicLayoutPropertiesUtil implements ProjectConstants {
	
	
	private static Properties defaultProperties = null;
	private static Properties configFileProperties = null;
	private static Properties customMarginPaddingProperties = null;

	private static String channelName = null;
	private static String channelKey = null;
	
	public static String getChannelName(){
		return channelName;
	}
	
	public static String getChannelKey(){
		return channelKey;
	}

	public static Properties getDefaultProperties() {
		return defaultProperties;
	}

	public static Properties getConfigFileProperties() {
		return configFileProperties;
	}
	
	public static void init(String projectLocation) {
		// Read the properties file afresh. If certain properties are not found, default them to their default values
		defaultProperties = fetchDefaultProperties();
		configFileProperties = fetchReviewConfigFileProperties(projectLocation,AR_LAYOUT_PROPS_FILE);
		customMarginPaddingProperties = fetchReviewConfigFileProperties(projectLocation,AR_MARGIN_PADDING_PROPS_FILE);
	}
	
	public static String setChannelName(String text){
		channelKey = text;
		if(text.equals("IPhone")){
			channelName = "iphone";
		} else if(text.equals("Android")){
			channelName = "android";
		} else if(text.equals("Windows 8")){
			channelName = "winphone8";
		} else if(text.equals("Windows 7.5")){
			channelName = "winmobile";
		} else if(text.equals("Blackberry")){
			channelName = "bb";
		} else if(text.equals("Blackberry10")){
			channelName = "bb10";
		} else if(text.equals("IPad")){
			channelName = "ipad";
		} else if(text.equals("Android Tablet")){
			channelName = "tabrcandroid";
		} else if(text.equals("Windows Tablet")){
			channelName = "windows8";
		} else if(text.equals("SPA-IPhone")){
			channelName = "spaiphone";
		} else if(text.equals("SPA-Android")){
			channelName = "spaandroid";
		} else if(text.equals("SPA-Windows 8")){
			channelName = "spawinphone8";
		} else if(text.equals("SPA-Windows 7.5")){
			channelName = "spawindows";
		} else if(text.equals("SPA-Blackberry")){
			channelName = "spablackberry";
		} else if(text.equals("SPA-Blackberry Non Touch")){
			channelName = "spabbnth";
		} else if(text.equals("SPA-IPad")){
			channelName = "spaipad";
		} else if(text.equals("SPA-Android Tablet")){
			channelName = "spaandroidtablet";
		} else if(text.equals("SPA-Windows Tablet")){
			channelName = "spawindowstablet";
		} else if(text.equals("SPA Desktop Web")){
			channelName = "desktopweb";
		} else if(text.equals("Windows Native/Kiosk")){
			channelName = "kiosk";
		}
		return channelName;
	}

	public static String getProperty(String propertyName) {
		String value = null;
		
		if (null != configFileProperties) {
			value = configFileProperties.getProperty(propertyName);
		}
		
		if (value == null || value.trim().length()==0) {
			value = customMarginPaddingProperties.getProperty(propertyName);
		}
		
		if (value == null || value.trim().length()==0) {
			value = NOT_DEFINED;
			//CodeReviewStatus.getInstance().addErrorMessage("Property Not Defined : " + propertyName);
		}
		
		return value;
	}
	
	private static Properties fetchReviewConfigFileProperties(String projectLocation, String fileName) {
		Properties props = new Properties();

		try {
			File propsFile = new File(projectLocation+fileName);
			if (propsFile.exists()) {
				JSReviewUtil.printToConsole(fileName + " file found. ");
				FileInputStream fos = new FileInputStream(propsFile);
				props.load(fos);
			}else {
				CodeReviewStatus.getInstance().addErrorMessage(fileName+" file not found . Using default values");
			}
		} catch (Exception fnfExcp) {
			CodeReviewStatus.getInstance().addErrorMessage(fnfExcp, "Error Reading" + fileName + " file: " + fnfExcp + " . Using default values");
			props = null;
		}
		
		return props;
		
	}
	
	// Following properties will be  used if CodeReview.properties file is not found in the project folder
	private static Properties fetchDefaultProperties() {
		Properties props = new Properties();
		props.put("ar.layout.target.channel", "android");
		return props;
	}
		
}
