package com.kony.tag.arabiclayout.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.kony.tag.config.CodeReviewStatus;
import com.kony.tag.config.ProjectConstants;
import com.kony.tag.js.codereview.util.ReviewUtil;
import com.kony.tag.util.TAGToolsUtil;
import com.kony.tag.util.eclipse.JSReviewUtil;

public class BuildPropertiesUtil implements ProjectConstants{
	
	private static Map<String, String> buildPropertiesMap = new HashMap<String, String>();
	private static Properties globalProperties = new Properties();
	private static Properties buildProperties = new Properties();
	
	private static LinkedHashSet<String> selectedChannelList = new LinkedHashSet<String>();
	private static LinkedHashMap<String, String> selectedChannelPropertyMap = new LinkedHashMap<String, String>();
	
	public static String getProperty(String propertyName) {
		return buildPropertiesMap.get(propertyName);
	}
	
	public static Properties getBuildProperties(){
		return buildProperties;
	}
	
	public static LinkedHashSet<String> getSelectedChannelList(){
		return selectedChannelList;
	}
	
	public static void clear(){
		selectedChannelList.clear();
		buildPropertiesMap.clear();
		selectedChannelPropertyMap.clear();
	}
	
	public static void init(String projectPath) throws Exception {
		
		ReviewUtil reviewUtil = new ReviewUtil();
		reviewUtil.deleteDirectory(projectPath + ARABICCODE_BACKUP_PATH);
		reviewUtil.deleteSpecificFiles(projectPath +FILE_DELIMITER +LOCATION_JS_MODULES,AR_FILE_PREFIX);
		reviewUtil.createDirectory(projectPath + ARABICCODE_BACKUP_PATH);
		reviewUtil.createDirectory(projectPath + ARABICCODE_BACKUP_PATH + LOCATION_ORIGINAL_JS_MODULES);
		reviewUtil.createDirectory(projectPath + ARABICCODE_BACKUP_PATH + ARABICCODE_REVIEW_OUTPUT_PATH);
		
		buildPropertiesMap.clear();
		
		populateBuildPropertyMap();
		
		readBuildProperties(projectPath);
		
		globalProperties.clear();	
		
		populateGlobalProperties(projectPath);
		
		validateGlobalProperties();
	}
	
	private static void populateBuildPropertyMap(){
		buildPropertiesMap.put("iphone", "iphone");
		buildPropertiesMap.put("android", "android");
		buildPropertiesMap.put("windowsphone8", "winphone8");
		buildPropertiesMap.put("windows_mango", "winmobile");
		buildPropertiesMap.put("windows8", "windows8");
		buildPropertiesMap.put("windows8x86", "windows8");
		buildPropertiesMap.put("windows8arm", "windows8");
		buildPropertiesMap.put("windows8x64", "windows8");
		buildPropertiesMap.put("Blackberry.All", "bb");
		buildPropertiesMap.put("Blackberry.JDE7.0.Torch", "bb");
		buildPropertiesMap.put("Blackberry.JDE7.0.Bold", "bb");
		buildPropertiesMap.put("Blackberry.JDE7.0.Torch_TouchOnly", "bb");
		buildPropertiesMap.put("Blackberry.JDE7.0.Curve", "bb");
		buildPropertiesMap.put("Blackberry.JDE7.0.Storm", "bb");
		buildPropertiesMap.put("Blackberry.JDE6.0.Torch", "bb");
		buildPropertiesMap.put("Blackberry.JDE5.0.Storm", "bb");
		buildPropertiesMap.put("Blackberry.JDE5.0.Bold", "bb");
		buildPropertiesMap.put("Blackberry.JDE5.0.Pearl3G", "bb");
		buildPropertiesMap.put("Blackberry.JDE5.0.8800", "bb");
		buildPropertiesMap.put("Blackberry.JDE4.7.Storm", "bb");
		buildPropertiesMap.put("Blackberry.JDE4.5.Bold", "bb");
		buildPropertiesMap.put("Blackberry.JDE4.5.Pearl_8100", "bb");
		buildPropertiesMap.put("Blackberry.JDE4.5.8800", "bb");
		buildPropertiesMap.put("Blackberry.JDE4.2.1.Pearl_8100", "bb");
		buildPropertiesMap.put("Blackberry.JDE4.2.1.8800", "bb");
		buildPropertiesMap.put("Blackberry.Z10", "bb10");
		buildPropertiesMap.put("Blackberry.Q10", "bb10");
		buildPropertiesMap.put("Blackberry.A10", "bb10");
		buildPropertiesMap.put("ipad", "ipad");
		buildPropertiesMap.put("androidtablet", "tabrcandroid");
		buildPropertiesMap.put("desktop_kiosk", "kiosk");
		buildPropertiesMap.put("desktopweb", "desktopweb");
		//buildPropertiesMap.put("advanced.nontouch.palm", "advpalm");
		//buildPropertiesMap.put("basic_devices", "normal");
		//buildPropertiesMap.put("basic_js", "bjs");
		buildPropertiesMap.put("spa.iphone", "spaiphone");
		buildPropertiesMap.put("spa.android", "spaandroid");
		buildPropertiesMap.put("spa.blackberry", "spablackberry");
		buildPropertiesMap.put("spa.windows", "spawinphone8");
		buildPropertiesMap.put("spa.blackberrynth", "spabbnth");
		buildPropertiesMap.put("spa.winphone", "spawindows");
		buildPropertiesMap.put("spa.ipad", "spaipad");
		buildPropertiesMap.put("spa.androidtablet", "spaandroidtablet");
		buildPropertiesMap.put("spa.windowstablet", "spawindowstablet");
		//buildPropertiesMap.put("hybrid.iphone", "wrapper//iphone");
		//buildPropertiesMap.put("hybrid.android", "wrapper//android");
		//buildPropertiesMap.put("hybrid.ipad", "wrapper//ipad");
		//buildPropertiesMap.put("hybrid.androidtablet", "wrapper//androidtab");
	}
	
	private static void populateGlobalProperties(String projectPath) throws Exception{
		FileInputStream in;
		try {
			in = new FileInputStream(projectPath + FILE_DELIMITER + GLOBAL_PROPERTIES_FILE);
			Properties props = new Properties();
			props.load(in);
			in.close();
			
			globalProperties = props;
			
		} catch (FileNotFoundException e) {
			CodeReviewStatus.getInstance().addErrorMessage(e,"File Not Found : " + GLOBAL_PROPERTIES_FILE + " :: " + e );
			e.printStackTrace();
			throw new FileNotFoundException(GLOBAL_PROPERTIES_FILE + " file not found. Please correct the same.");
		} catch (IOException e) {
			CodeReviewStatus.getInstance().addErrorMessage(e,"IO Exception reading the file : " + GLOBAL_PROPERTIES_FILE + " :: " + e );
			e.printStackTrace();
			throw new IOException("IO Exception reading the file : " + GLOBAL_PROPERTIES_FILE +". Please see the console logs for detailed information.");
		} catch (Exception excp) {
			CodeReviewStatus.getInstance().addErrorMessage(excp,"Error reading the file : " + GLOBAL_PROPERTIES_FILE + " :: " + excp );
			throw new Exception("IO Exception reading the file : " + BUILD_PROPERTIES_FILE +". Please see the console logs for detailed information.");
		}
	}
	
	private static void validateGlobalProperties() throws Exception{
		String errorString = " Please set the following properties in global.properties : \n";
		boolean propertiesMissing = false;
		if(globalProperties.get("eclipse.equinox.path") == null || ((String)globalProperties.get("eclipse.equinox.path")).trim().equals("")) {
			errorString += "eclipse.equinox.path \n";
			propertiesMissing = true;
		}
		if(globalProperties.get("imagemagic.home") == null || ((String)globalProperties.get("imagemagic.home")).trim().equals("")) {
			errorString += "imagemagic.home \n";
			propertiesMissing = true;
		}
		if(globalProperties.get("android.home") == null || ((String)globalProperties.get("android.home")).trim().equals("")) {
			errorString += "android.home \n";
			propertiesMissing = true;
		}
		if(propertiesMissing) {
			throw new Exception(errorString);
		}
	}
	
	private static void readBuildProperties(String projectPath) throws Exception{

		FileInputStream in;
		try {
			in = new FileInputStream(projectPath + FILE_DELIMITER + BUILD_PROPERTIES_FILE);
			Properties props = new Properties();
			props.load(in);
			buildProperties = props;
			in.close();
			
			Enumeration keyList = props.keys();
			
			while(keyList.hasMoreElements()){
				Object elementKey = keyList.nextElement();
				String elementValue = (String)props.get(elementKey);
				if(elementValue != null && elementValue.trim().equalsIgnoreCase("true")){
					Object channel = buildPropertiesMap.get(elementKey);
					if(channel != null) {
						selectedChannelList.add((String)channel);
						selectedChannelPropertyMap.put((String)channel, (String)elementKey);
					}
				}
			}
			
			if(selectedChannelList.size() > 1) {
				FileOutputStream out = new FileOutputStream(projectPath + BUILD_PROPERTIES_FILE);
				props = clearBuildProperties(props);
				props.store(out, null);
				out.close();
			}
			
		} catch (FileNotFoundException e) {
			CodeReviewStatus.getInstance().addErrorMessage(e,"File Not Found : " + BUILD_PROPERTIES_FILE + " :: " + e );
			e.printStackTrace();
			throw new FileNotFoundException(BUILD_PROPERTIES_FILE + " file not found. Please correct the same.");
		} catch (IOException e) {
			CodeReviewStatus.getInstance().addErrorMessage(e,"IO Exception reading the file : " + BUILD_PROPERTIES_FILE + " :: " + e );
			e.printStackTrace();
			throw new IOException("IO Exception reading the file : " + BUILD_PROPERTIES_FILE +". Please see the console logs for detailed information.");
		} catch (Exception excp) {
			CodeReviewStatus.getInstance().addErrorMessage(excp,"Error reading the file : " + BUILD_PROPERTIES_FILE + " :: " + excp );
			throw new Exception("IO Exception reading the file : " + BUILD_PROPERTIES_FILE +". Please see the console logs for detailed information.");
		}
	}
	
	private static Properties clearBuildProperties(Properties props) {
		Iterator<String> iterator = selectedChannelPropertyMap.values().iterator();
		while(iterator.hasNext()){
			String channelProperty = iterator.next();
			props.setProperty(channelProperty, "false");
		}
		return props;
	}
	
	public static void updateBuildProperties(String channel, String projectPath){
		FileInputStream in;
		try {
			in = new FileInputStream(projectPath + BUILD_PROPERTIES_FILE);
			Properties props = new Properties();
			props.load(in);
			in.close();
			
			FileOutputStream out = new FileOutputStream(projectPath + BUILD_PROPERTIES_FILE);
			Iterator<String> iterator = selectedChannelPropertyMap.values().iterator();
			String selectedChannelProperty = selectedChannelPropertyMap.get(channel);
			while(iterator.hasNext()){
				String channelProperty = iterator.next();
				if(selectedChannelProperty != null && selectedChannelProperty.equals(channelProperty)){
					props.setProperty(channelProperty, "true");
				} else {
					props.setProperty(channelProperty, "false");
				}
			}
			
			props.store(out, null);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public static int executeBuild(String projectPath){
		TAGToolsUtil.printToConsole("\n************** Executing ant script *************\n");
		Runtime rt = Runtime.getRuntime();
		Process process = null; 
		File fileDir = new File(projectPath);
		int result = -1;
		TAGToolsUtil.printToConsole("SEE-PERF-LOG: step - 2A-BUILD START" + (new java.util.Date()).toString());
		try{
			process = rt.exec("cmd /c ant" , null, fileDir );
			InputStream stdin = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(stdin);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			String pattern = "(.*)Build(.*)Failed(.*)";
			boolean buildFailure = false;
			Pattern r = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
			TAGToolsUtil.printToConsole("SEE-PERF-LOG: step - 2A-BUILD LOOP START" + (new java.util.Date()).toString());
			while ( (line = br.readLine()) != null){
				Matcher m = r.matcher(line);
				if(!buildFailure && m.find()){
					buildFailure = true;
				}
				TAGToolsUtil.printToConsole(line);
			}
			TAGToolsUtil.printToConsole("SEE-PERF-LOG: step - 2A-BUILD LOOP END" + (new java.util.Date()).toString());
			TAGToolsUtil.printToConsole("build completed: ");
			result = process.exitValue();
			if(result == 0 && buildFailure){
				result = 1;
			}
			
			TAGToolsUtil.printToConsole("SEE-PERF-LOG: step - 2A-BUILD END" + (new java.util.Date()).toString());
		}catch (IOException ioe) {
			ioe.printStackTrace();
			JSReviewUtil.printToConsole(ioe.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			JSReviewUtil.printToConsole(e.getMessage());
		}
		return result;
	}

}
