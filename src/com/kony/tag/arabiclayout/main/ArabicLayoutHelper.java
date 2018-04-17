package com.kony.tag.arabiclayout.main;


import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import com.kony.tag.arabiclayout.handlers.ExecutionModeHandler;
import com.kony.tag.arabiclayout.impl.ArLayoutManager;
import com.kony.tag.arabiclayout.impl.ArabicThemeCreationRule;
import com.kony.tag.arabiclayout.tasks.ArabicReviewEngine;
import com.kony.tag.arabiclayout.tasks.CleanupTask;
import com.kony.tag.arabiclayout.tasks.CopyModulesTask;
import com.kony.tag.arabiclayout.util.ArabicLayoutPropertiesUtil;
import com.kony.tag.arabiclayout.util.ArabicLayoutUtil;
import com.kony.tag.config.CodeReviewStatus;
import com.kony.tag.config.ConfigVO;
import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.ProjectConstants;
import com.kony.tag.config.ReviewModeConstants;
import com.kony.tag.config.TagToolException;
import com.kony.tag.js.codereview.util.ReviewUtil;
import com.kony.tag.util.eclipse.JSReviewUtil;


public class ArabicLayoutHelper implements ProjectConstants {
	
	protected ProjectConfig fetchReviewConfiguration(String channel) {
		JSReviewUtil.printDetailedLog("fetchReviewConfiguration START");
		
		ProjectConfig projectConfig = null;
		String projectLocation = null;
		String detailedLogFlag = null;
		
		projectLocation = JSReviewUtil.getProjectPath();
		
		// Read all this config information from projects..for now it is hard coded
		ConfigVO configVo = new ConfigVO();
		configVo.setProjectPath(projectLocation);
		configVo.setTempWorkAreaLocation(projectLocation + ARABICCODE_BACKUP_PATH);
		configVo.setCodeBackupPath(projectLocation + ARABICCODE_BACKUP_PATH + LOCATION_ORIGINAL_JS_MODULES);
		configVo.setReviewOutputPath(projectLocation + ARABICCODE_BACKUP_PATH + ARABICCODE_REVIEW_OUTPUT_PATH);
		ArabicLayoutPropertiesUtil.init(projectLocation);
		channel = channel == null ? ArabicLayoutPropertiesUtil.getChannelName() : channel;
		
		JSReviewUtil.printDetailedLog("channel selected : " + channel);
		
		// Create the config Object and keep it ready
		projectConfig = ProjectConfig.getCodeReviewConfig(configVo);
		
		detailedLogFlag = projectConfig.getArabicLayoutProperty(PROP_DETAILED_LOG);
		ReviewModeConstants.IS_DETAILED_LOGGING_ENABLED = false;
		
		if (null != detailedLogFlag && detailedLogFlag.trim().equalsIgnoreCase(TRUE)) {
			ReviewModeConstants.IS_DETAILED_LOGGING_ENABLED = true;
		}
		
		// Reset the review report
		ArabicLayoutUtil rUtil = ArabicLayoutUtil.initNewInstance(projectConfig);
 		CodeReviewStatus.reset();
 		ArLayoutManager.updateChannel(channel);
 		
 		JSReviewUtil.printDetailedLog("fetchReviewConfiguration END");
		return projectConfig;
	}
	
	public void validateCodeGen(String channel,ProjectConfig projectConfig) {
		
		JSReviewUtil.printDetailedLog("validateCodeGen START");
		
		if (!ExecutionModeHandler.isIncrementalCodeGenMode()) {
			return;
		}
		
		List<String> uiFormDirectories = new ArrayList<String>();
		String codeGenChannelDirectory = null;
		long lastUIModifiedTime = -1;
		long lastChannelCodeGenTime = -1;
		ReviewUtil reviewUtil = new ReviewUtil();
		
		uiFormDirectories.add(projectConfig.getProjectPath()+LOCATION_ALL_FORMS);
		uiFormDirectories.add(projectConfig.getProjectPath()+LOCATION_ALL_POPUPS);
		uiFormDirectories.add(projectConfig.getProjectPath()+LOCATION_ALL_TEMPLATES);
		codeGenChannelDirectory = projectConfig.getProjectPath() + LOCATION_JS_GEN_SRC_ROOT_FOLDER +FILE_DELIMITER + ArLayoutManager.getChannel() + LOCATION_JS_GEN_SRC_FOLDER;
		
		lastUIModifiedTime = reviewUtil.getLastModifiedTimeStamp(uiFormDirectories);
		lastChannelCodeGenTime = reviewUtil.getLastModifiedTimeStamp(codeGenChannelDirectory);
		
		if (lastUIModifiedTime > lastChannelCodeGenTime) {
			String warningMessage = "One or more forms/popups/templates may have been modified since last Build, for channel '" + ArabicLayoutPropertiesUtil.getChannelKey() +
			"'. Arabic Layout Code Gen may be working on outdated code. Please peform a fresh Incremental Build for : '" +ArabicLayoutPropertiesUtil.getChannelKey() + "' Channel, before running this tool";
			
			// No Build happened for this channel after modifying one or more forms/popups/templates for this channel
			JSReviewUtil.printDetailedLog(warningMessage);
			JSReviewUtil.setEndOfWorkWarningMessage(warningMessage);
			//ArLayoutManager.addWarning(warningMessage);
		}
		
		JSReviewUtil.printDetailedLog("validateCodeGen END");
		
	}
	
	protected void copyFilesForReview(ProjectConfig projectConfig) {
		JSReviewUtil.printDetailedLog("copyFilesForReview START");
		CopyModulesTask copyModulesTask = new CopyModulesTask(projectConfig);
		copyModulesTask.run();
		JSReviewUtil.printDetailedLog("copyFilesForReview END");
	}
	
	protected void executeArabicLayoutReversal(ProjectConfig projectConfig) {
		JSReviewUtil.printDetailedLog("executeJavaScriptReview START");
		// First Review All JS Files
		ReviewUtil reviewUtil = new ReviewUtil();
		File[] jsFiles = null;
		List<String> jsFileNames = new ArrayList<String>();
		String fileName = null;
		String excludeFlag = null;
		String duplicatedFormFlag = null;
		String duplicatedTemplateFlag = null;
		
		ArabicReviewEngine arabicReviewEngine = new ArabicReviewEngine(projectConfig);
		
		 // This is a JS project. Review All JS Files
			jsFiles = reviewUtil.fetchFilesWithoutFilter(projectConfig.getCodeBackupPath() + FILE_DELIMITER + ArLayoutManager.getChannel());
			
			for (int passNumber=1; passNumber<=1; passNumber++) {
				for(File reviewFile : jsFiles) {
					JSReviewUtil.printDetailedLog("Processing File : " + reviewFile.getName());
					if (!reviewFile.getName().toLowerCase().endsWith(".js")) {
						continue;
					}
					
					if (reviewFile.getName().toLowerCase().endsWith("globalsequences.js") || 
							reviewFile.getName().toLowerCase().equals("application.js") ||
							reviewFile.getName().toLowerCase().equals("appskins.js")) {
						JSReviewUtil.printToConsole("# Skipping Transformation of : " + reviewFile.getName());
						continue;
					}
					
					// Check if this file has been configured for being excluded from Alignment Swap
					fileName = reviewFile.getName().substring(0,reviewFile.getName().length()-3);
					excludeFlag = projectConfig.getArabicLayoutProperty(PROP_FORM_EXCLUDE+fileName);
					boolean excludeForm = excludeFlag != null && excludeFlag.toLowerCase().equals(TRUE);
					excludeFlag = projectConfig.getArabicLayoutProperty(PROP_TEMPLATE_EXCLUDE+fileName);
					boolean excludeTemplate = excludeFlag != null && excludeFlag.toLowerCase().equals(TRUE);
					duplicatedFormFlag = projectConfig.getArabicLayoutProperty(PROP_FORM_DUPLICATE+fileName);
					boolean duplicateForm = duplicatedFormFlag != null && duplicatedFormFlag.toLowerCase().equals(TRUE);
					
					boolean duplicateTemplate = isDuplicatedTemplate(fileName,projectConfig);
					
					if (excludeForm || excludeTemplate || duplicateForm || duplicateTemplate) {
						JSReviewUtil.printToConsole("# Skipping Transformation of : " + reviewFile.getName());
						continue;
					} else if (fileName.endsWith(AR_SUFFIX)){
						JSReviewUtil.printToConsole("# Skipping Transformation of : " + reviewFile.getName());
						continue;
					}
					
					if (passNumber == 1) {
						jsFileNames.add(reviewFile.getName());
					}
					
					// Review Each Java Script file for all JS Rules
					try {

					// Rule #JS-001 : No. of lines in a function should be within a threshold limit
					JSReviewUtil.printDetailedLog("jsReviewEngine.execute : File " + reviewFile.getName() + " Pass Number : " + passNumber);
					arabicReviewEngine.execute(reviewFile, passNumber);
					//comments = jsReviewEngine.execute(reviewFile);
					//consolidateComments(allComments, comments);
					
					} catch (TagToolException cdrExcp) {
						CodeReviewStatus.getInstance().addErrorMessage(cdrExcp,"JS Review Pass# " + passNumber + " : " + cdrExcp.getErrorMessage());
					} catch (Exception excp) {
						CodeReviewStatus.getInstance().addErrorMessage(excp,"JS Review Pass# " + passNumber + " : " + excp.toString());
					}					
				}
			}
			
			JSReviewUtil.printDetailedLog("executeJavaScriptReview END");
	}
	
	protected void prepareReviewWorkspace(ProjectConfig projectConfig) {
		// Clean up the necessary temp directories needed for review
		CleanupTask cleanupTask = new CleanupTask(projectConfig);
		cleanupTask.run();
	}

	public void printErrorsWarnings() {
		
		// Print any warnings encountered during review
		if (CodeReviewStatus.getInstance().getWarningMessages() != null && 
				CodeReviewStatus.getInstance().getWarningMessages().size()>0) {
			JSReviewUtil.printToConsole("\n >>>>>>  WARNINGS : <<<<<<");
			for (String message : CodeReviewStatus.getInstance().getWarningMessages()) {
				JSReviewUtil.printToConsole(":: " + message);
			}
			JSReviewUtil.printToConsole(" <<<<<<  Done with WARNINGS : <<<<<< \n");
		}
		
		if (CodeReviewStatus.getInstance().getErrorMessages() != null && 
				CodeReviewStatus.getInstance().getErrorMessages().size()>0) {
			JSReviewUtil.printToConsole("\n >>>>>>  ERRORS : <<<<<<");
			for (String message : CodeReviewStatus.getInstance().getErrorMessages()) {
				JSReviewUtil.printToConsole("## " + message);
			}
			JSReviewUtil.printToConsole(" <<<<<<  Done with ERRORS : <<<<<< \n");
			JSReviewUtil.printToConsole("REVIEW COMPLETED with one or more Errors. See the Error log printed above !!");
		} else {
			JSReviewUtil.printToConsole("SUCCESS !! Transformation of Arabic Modules is Completed.");
		}
		
	}
	
	/*public void animationFrameworkReversal(ProjectConfig projectConfig){
		try {
			ArAnimationFrameworkReversalRule arAnimationFrameworkRule = new ArAnimationFrameworkReversalRule(null, null, projectConfig, ArLayoutManager.getChannel());
			
			Properties props = ArabicLayoutPropertiesUtil.getConfigFileProperties();
			String arabicThemeRequired = props.getProperty(PROP_ARABIC_THEME);
			if(arabicThemeRequired != null && arabicThemeRequired.equals(TRUE)){
				arThemeRule.execute();
			}
		} catch (TagToolException e) {
			e.printStackTrace();
		}
	}*/
	
	public void createArabicTheme(ProjectConfig projectConfig){
		try {
			ArabicThemeCreationRule	arThemeRule = new ArabicThemeCreationRule(projectConfig);
			
			Properties props = ArabicLayoutPropertiesUtil.getConfigFileProperties();
			String arabicThemeRequired = props.getProperty(PROP_ARABIC_THEME);
			if(arabicThemeRequired != null && arabicThemeRequired.equals(TRUE)){
				arThemeRule.execute();
			}
		} catch (TagToolException e) {
			e.printStackTrace();
		}
	}
	
	public void provisionForDuplicateForms(ProjectConfig projectConfig) {
		//projectConfig.get
		Properties props = ArabicLayoutPropertiesUtil.getConfigFileProperties();
		Enumeration<Object> enumKeys =props.keys();
		String keyName = null;
		String formName = null;
		String templateName = null;
		String templateWidgetName = null;
		int index1 = -1;
		
		try {
		
		while (enumKeys.hasMoreElements()) {
			keyName = enumKeys.nextElement().toString().trim();
			JSReviewUtil.printDetailedLog("Trying to Prrovision for " + keyName);
			
			if (keyName.indexOf(PROP_FORM_DUPLICATE) >=0 && 
					props.get(keyName).toString().trim().equalsIgnoreCase(TRUE)) {
				formName = keyName.replace(PROP_FORM_DUPLICATE, BLANK).trim();
				JSReviewUtil.printToConsole("Provisioned for Form : " + formName);
				ArLayoutManager.addManuallyDuplicatedForm(formName);
			} else if (keyName.indexOf(PROP_TEMPLATE_DUPLICATE) >=0 && 
					props.get(keyName).toString().trim().equalsIgnoreCase(TRUE)) {
				formName = keyName.replace(PROP_TEMPLATE_DUPLICATE, BLANK).trim();
				index1 = formName.indexOf(CHAR_DOT);
				if (index1>=0 && formName.length() > (index1+1)) {
					JSReviewUtil.printToConsole("Provisioned for Template : " + formName);
					templateWidgetName = formName.substring(index1+1);
					templateName = formName.substring(0,index1);
					ArLayoutManager.addTemplate(templateWidgetName);
					ArLayoutManager.addTemplateInitializeMethod(KEYWORD_INITIALIZE_FUNCTION+templateName);
				}
			}
 
		}
		}catch (Exception e) {
			JSReviewUtil.printDetailedLog("ERROR when executing provisionForDuplicateForms  : " + e + " Stack Trace : " + e.getStackTrace());
		}
		
		
	}
	
	private boolean isDuplicatedTemplate(String fileName, ProjectConfig projectConfig) {
		//projectConfig.get
		Properties props = ArabicLayoutPropertiesUtil.getConfigFileProperties();
		Enumeration<Object> enumKeys =props.keys();
		String keyName = null;
		boolean flag = false;
		
		while (enumKeys.hasMoreElements()) {
			keyName = enumKeys.nextElement().toString().trim();
			
			if (keyName.indexOf(PROP_TEMPLATE_DUPLICATE+fileName) >=0 && 
					props.get(keyName).toString().trim().equalsIgnoreCase(TRUE)) {
					flag = true;
					break;
			}
		}
		return flag;
	}
}
