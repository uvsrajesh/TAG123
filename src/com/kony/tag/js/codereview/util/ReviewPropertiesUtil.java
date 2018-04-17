package com.kony.tag.js.codereview.util;

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

import com.kony.tag.config.ProjectConstants;
import com.kony.tag.config.CodeReviewStatus;
import com.kony.tag.util.eclipse.JSReviewUtil;

public class ReviewPropertiesUtil implements ProjectConstants {
	
	private static String HASH = "#";
	private static String KL_FILE = ".kl";
	private static String RETINA_IMAGE = "@2x";
	private static Properties defaultProperties = null;
	private static Properties configFileProperties = null;
	private static Map<String, List<String>> ignoreFilesMap = new HashMap<String, List<String>>();
	private static List<String> ignoreSkinsList = new ArrayList<String>();
	private static List<String> ignoreImagesList = new ArrayList<String>();
	private static List<String> ignoreFunctionsList = new ArrayList<String>();
	private static List<String> ignoreI18nkeysList = new ArrayList<String>();

	private static String NOT_DEFINED = "NotDefined";
	
	public static Properties getDefaultProperties() {
		return defaultProperties;
	}

	public static Properties getConfigFileProperties() {
		return configFileProperties;
	}
	
	public static List<String> getIgnoreSkinsList() {
		return ignoreSkinsList;
	}
	
	public static List<String> getIgnoreImagesList() {
		return ignoreImagesList;
	}
	
	public static List<String> getIgnoreFunctionsList() {
		return ignoreFunctionsList;
	}
	
	public static List<String> getIgnoreI18nkeysList() {
		return ignoreI18nkeysList;
	}
	
	public static Map<String, List<String>> getIgnoreFilesMap() {
		return ignoreFilesMap;
	}
	
	public static List<String> getFilesToIgnore(String ruleId) {
		return ignoreFilesMap.get(ruleId);
	}
	
	public static void init(String projectLocation) {
		// Read the properties file afresh. If certain properties are not found, default them to their default values
		defaultProperties = fetchDefaultProperties();
		configFileProperties = fetchReviewConfigFileProperties(projectLocation);
		
		initIgnoreFilesList(projectLocation);
		initIgnoreSkinsList(projectLocation);
		initIgnoreImagesList(projectLocation);
		initIgnoreI18KeysList(projectLocation);
		initIgnoreFunctionsList(projectLocation);
	}

	public static String getProperty(String propertyName) {
		String value = null;
		
		if (null != configFileProperties) {
			value = configFileProperties.getProperty(propertyName);
		} 
		
		if (value == null || value.trim().length()==0) {
			if (defaultProperties != null) {
				value = defaultProperties.getProperty(propertyName);
			}
		}

		if (value == null || value.trim().length()==0) {
			value = NOT_DEFINED;
			CodeReviewStatus.getInstance().addErrorMessage("Property Not Defined : " + propertyName);
		}
		
		return value;
	}
	
	private static Properties fetchReviewConfigFileProperties(String projectLocation) {
		Properties props = new Properties();

		try {
			File propsFile = new File(projectLocation+LOCATION_CODE_REVIEW_PROPS_FILE);
			if (propsFile.exists()) {
				JSReviewUtil.printToConsole("Code Review Properties file found. ");
				FileInputStream fos = new FileInputStream(propsFile);
				props.load(fos);
			}else {
				CodeReviewStatus.getInstance().addErrorMessage("Code Review Properties file not found . Using default values");
			}
		} catch (Exception fnfExcp) {
			CodeReviewStatus.getInstance().addErrorMessage(fnfExcp, "Error Reading Code Review Properties file: " + fnfExcp + " . Using default values");
			props = null;
		}
		
		return props;
		
	}
	
	// Following properties will be  used if CodeReview.properties file is not found in the project folder
	private static Properties fetchDefaultProperties() {
		Properties props = new Properties();
		props.put("review.mobile.native", "yes");
		props.put("review.mobile.spa", "yes");
		props.put("review.mobile.mobileweb", "yes");
		props.put("review.tablet.native", "yes");
		props.put("review.tablet.spa", "yes");
		props.put("review.desktopweb", "yes");
		props.put("review.forkedforms","yes");
		
		props.put("rules.js.001.max_function_size_rule", "yes");
		props.put("rules.js.002.hardcoded_strings_rule","yes");
		props.put("rules.js.003.alert_display_rule","yes");
		props.put("rules.js.004.segment_update_in_loop_rule","yes");
		props.put("rules.js.005.multiple_returns_per_function_rule","yes");
		props.put("rules.js.006.network_api_wrapper_rule","yes");
		props.put("rules.js.007.function_level_comments_rule","yes");
		props.put("rules.js.008.multiple_timers_rule","yes");
		props.put("rules.js.009.global_variables_rule","yes");

		props.put("rules.form.all.001.blank_i18keys_rule","yes ");
		props.put("rules.form.mobileweb.002.secure_submit_rule","yes");
		props.put("rules.form.all.003.idle_timeout_rule","yes");
		props.put("rules.form.mobileweb.004.block_ui_skin_rule","yes");
		props.put("rules.form.all.005.focus_skin_rule","yes");
		props.put("rules.form.all.006.single_widget_in_container_rule","yes");
		props.put("rules.form.all.007.screen_level_widget_rule","yes");
		props.put("rules.form.all.008.segment_orientation_rule","yes");
		props.put("rules.form.all.009.nested_container_widgets_rule","yes");
		props.put("rules.form.all.010.single_browser_map_widget_rule","yes");
		props.put("rules.form.all.011.code_snippets_rule","yes");
		props.put("rules.form.all.012.last_widget_browser_map_rule","yes");
		props.put("rules.form.all.013.button_image_background_rule","yes");
		props.put("rules.form.all.014.max_widgets_rule","yes");
		props.put("rules.form.all.015.harcoded_i18keys_rule","yes");
		props.put("rules.form.all.016.button_progress_indicator_enabled_rule","yes");
		
		props.put("rules.misc.all.001.unused_js_functions_rule","yes");
		props.put("rules.misc.all.002.unused_skins_rule","yes");
		props.put("rules.misc.all.003.unused_images_rule","yes");
		props.put("rules.misc.all.004.unused_i18_rule","yes");
		props.put("rules.misc.all.005.invalid_js_module_names_rule","yes");
		
		props.put("rules.app.security.001.self_signed_cert_enabled","yes");

		// Configurable Review Params
		props.put("rules.js.001.max_function_size_without_comments","100");
		props.put("rules.js.001.max_function_size_with_comments","200");
		props.put("rules.js.005.max_returns_per_function","1");
		props.put("rules.js.009.global_variable_prefix","gbl");
		props.put("rules.form.all.009.redundant_nested_levels_allowed","1");
		props.put("rules.misc.all.002.project_ui_theme","default");

		props.put("rules.form.all.014.max_widgets_allowed_mobile","40");
		props.put("rules.form.all.014.max_widgets_allowed_tablet","60");
		props.put("rules.form.all.014.max_widgets_allowed_desktop","60");

		props.put("rules.form.all.014.max_widgets_allowed_dialog_mobile","10");
		props.put("rules.form.all.014.max_widgets_allowed_dialog_tablet","20");
		props.put("rules.form.all.014.max_widgets_allowed_dialog_desktop","20");

		props.put("rules.form.all.014.max_widgets_allowed_template_mobile","5");
		props.put("rules.form.all.014.max_widgets_allowed_template_tablet","10");
		props.put("rules.form.all.014.max_widgets_allowed_template_desktop","10");
		
		props.put("rules.ar.001.arabic_alignment_rule","yes");
		
		return props;
	}


	private final static void initIgnoreSkinsList(String projectLocation) {
		ignoreSkinsList = fetchIgnoreKeywordsList(projectLocation + LOCATION_SKINS_IGNORE_LIST);
		if (ignoreSkinsList != null && ignoreSkinsList.size()>0) {
			JSReviewUtil.printToConsole("One or more skins have been marked to be ignored, when reviewing Unused Skins");
		}
	}
	
	private final static void initIgnoreImagesList(String projectLocation) {
		ignoreImagesList = fetchIgnoreKeywordsList(projectLocation + LOCATION_IMAGES_IGNORE_LIST);
		if (ignoreImagesList != null && ignoreImagesList.size()>0) {
			JSReviewUtil.printToConsole("One or more images have been marked to be ignored, when reviewing Unused Images");
		}
	}

	private final static void initIgnoreI18KeysList(String projectLocation) {
		ignoreI18nkeysList = fetchIgnoreKeywordsList(projectLocation + LOCATION_I18NKEYS_IGNORE_LIST);
		if (ignoreI18nkeysList != null && ignoreI18nkeysList.size()>0) {
			JSReviewUtil.printToConsole("One or more I18Keys have been marked to be ignored, when reviewing Unused I18 Keys");
		}
	}

	private final static void initIgnoreFunctionsList(String projectLocation) {
		ignoreFunctionsList = fetchIgnoreKeywordsList(projectLocation + LOCATION_FUNCTIONS_IGNORE_LIST);
		if (ignoreFunctionsList != null && ignoreFunctionsList.size()>0) {
			JSReviewUtil.printToConsole("One or more JS Functions have been marked to be ignored, when reviewing Unused JS Functions");
		}
	}
	
	private final static List<String> fetchIgnoreKeywordsList(String fileLocation) {
		List<String> lines = fetchLinesOfFile(fileLocation);
		TreeSet<String> keywords = new TreeSet<String>();

		if (null == lines || lines.size()==0) {
			return new ArrayList<String>(keywords);
		}
		
		for (String line: lines) {
			
			line = line.trim().toLowerCase();
			
			// Names of retina images to be captured without @2x phrase in the file name  
			line = line.replaceAll(RETINA_IMAGE, BLANK);
			keywords.add(line);
		}
		
		return new ArrayList<String>(keywords);
		
	}
	
	private final static void initIgnoreFilesList(String projectLocation) {
		
		List<String> lines = fetchLinesOfFile(projectLocation + LOCATION_REVIEW_FILES_IGNORE_LIST);
		String ruleId = null;
		StringTokenizer strTokenizer = null;
		int indexVal = 0;
		List<String> files = null;
		String fileName = null;
		int index2=0;
		
		
		if (null != ignoreFilesMap) {
			ignoreFilesMap.clear();
		} else {
			ignoreFilesMap = new HashMap<String, List<String>>();
		}
		
		if (null == lines || lines.size()==0) {
			return;
		}
			
			for (String line: lines) {
				ruleId = BLANK;
				indexVal = line.indexOf(COLON);

				if (indexVal <= 0) {
					JSReviewUtil.printToConsole("Rule Id not specified : " + line + ". File : "+ projectLocation + LOCATION_REVIEW_FILES_IGNORE_LIST);
					continue;
				} else {
					ruleId = line.substring(0, indexVal); 
				}
				
				ruleId = ruleId.trim().toUpperCase();
				
				if (line.length()<=(indexVal+1) 
						|| line.substring(indexVal+1).trim().length()==0) {
					// Files to be ignored  not specified
					continue;
				}

				strTokenizer = new StringTokenizer(line.substring(indexVal+1), COMMA);
				
				files = ignoreFilesMap.get(ruleId);
				
				if (files == null) {
					files = new ArrayList<String>();
				}
				
				while(strTokenizer.hasMoreTokens()) {
					fileName = strTokenizer.nextToken().trim().toLowerCase();
					
					// For KL forms - just add form name and not the entire file name 
					index2 = fileName.indexOf(KL_FILE);
					if (index2>=0) {
						fileName = fileName.substring(0,(index2));
					}
					if (!files.contains(fileName)) {
						files.add(fileName);
					}
				}
				
				ignoreFilesMap.put(ruleId, files);
			}
			
			Set<String> keys = ignoreFilesMap.keySet();
			List<String> tmpFilesList = null;
			
			for (String key1 : keys) {
				tmpFilesList = ignoreFilesMap.get(key1);
				
				if (null != tmpFilesList && tmpFilesList.size()>0) {
					JSReviewUtil.printToConsole("One or more Files have been marked to be ignored, when reviewing Rule # " + key1);
					
					for (String tmpName : tmpFilesList) {
						System.out.println("IGNORED: " + tmpName);
					}
				}
			}
	}
	
	private final static List<String> fetchLinesOfFile(String fileLocation) {
		List<String> lines = new ArrayList<String>();
		LineNumberReader lineNumberReader = null;
		String line;
		
		try {
			
			File file = new File(fileLocation);
			lineNumberReader = new LineNumberReader(new FileReader(file));
			
			if (null != lineNumberReader) { 
			
				while(null != (line = lineNumberReader.readLine())) {
					
					if(line == null || line.trim().startsWith(HASH) || line.trim().length()<=0) {
						continue;
					}
					
					lines.add(line);
				}
			}
		} catch (FileNotFoundException excp) {
			CodeReviewStatus.getInstance().addErrorMessage(excp, fileLocation +" not found for Code Review!! ");
		} catch (IOException excp) {
			CodeReviewStatus.getInstance().addErrorMessage(excp, "Unable to read the file " + fileLocation);
		}
		
		return lines;
	}	
}
