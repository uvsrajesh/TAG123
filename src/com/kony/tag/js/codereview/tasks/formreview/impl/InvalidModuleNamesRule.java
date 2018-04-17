package com.kony.tag.js.codereview.tasks.formreview.impl;

import java.util.ArrayList;
import java.util.List;

import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.TagToolException;
import com.kony.tag.config.WidgetVo;
import com.kony.tag.js.codereview.base.FormReviewMasterTask;

public class InvalidModuleNamesRule extends FormReviewMasterTask {
	
	public static final String PROPS_RULE_NAME = "rules.misc.all.005.invalid_js_module_names_rule";
	public static final String RULE_ID = "MISC-005";
	
	private static final String ERR_MODULE_NAME = "Same file name has been used by a UI form as well. Rename the JS Module.";
	private static final String ERR_SHORT_DESC = "Invalid JS Module Name";

	private List<String> allModuleNames;
	private List<String> allFormNames;
	
	public List<String> getAllModuleNames() {
		return this.allModuleNames;
	}

	public void setAllModuleNames(List<String> allModuleNames) {
		this.allModuleNames = removeFileExtensions(allModuleNames);
	}

	public List<String> getAllFormNames() {
		return this.allFormNames;
	}

	public void setAllFormNames(List<String> allFormNames) {
		this.allFormNames = removeFileExtensions(allFormNames);
	}

	public InvalidModuleNamesRule(ProjectConfig codeReviewConfig) {
		super(codeReviewConfig);
		String[] ruleDescriptions = {ERR_SHORT_DESC};
		super.init(PROPS_RULE_NAME,RULE_ID,ruleDescriptions);

		rulesInit(null,null);
	}

	public void rulesInit(String reviewFileName, String reviewFileDirectoryName) {
		setReviewFileDirectory(reviewFileDirectoryName);
		setReviewFile(reviewFileName);
	}
	
	protected void review(WidgetVo widgetVo) throws TagToolException {
		// Nothing to review within a form
	}
	
	public void postReview() throws TagToolException {
		
		if (allFormNames != null && allModuleNames != null 
				&& allFormNames.size()>0 && allModuleNames.size()>0) {
			
			for (String formName : allFormNames) {
				
				if (allModuleNames.contains(formName)) {
					addError(ERR_MODULE_NAME, ERR_SHORT_DESC,formName , BLANK, 0, SEV_HIGH, RULE_ID);
				}
			}
		}
		
	}
	
	private List<String> removeFileExtensions(List<String> fileNamesList) {
		List<String> sanitizedFileNamesList = new ArrayList<String>();
		int index = 0;
		
		if (!isEnabled()) {
			return sanitizedFileNamesList;
		}
		
		for (String fileName : fileNamesList) {
			index = fileName.indexOf(CHAR_DOT);
			if (index>0) {
				sanitizedFileNamesList.add(fileName.substring(0, index));
				//System.out.println("ACTUAL NAME : " + fileName + " ## SANITIZED NAME : " + fileName.substring(0, index));
			}
		}

		return sanitizedFileNamesList;
	}
}