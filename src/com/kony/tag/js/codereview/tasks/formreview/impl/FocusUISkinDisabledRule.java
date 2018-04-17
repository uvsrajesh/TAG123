package com.kony.tag.js.codereview.tasks.formreview.impl;

import java.util.List;

import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.TagToolException;
import com.kony.tag.config.WidgetVo;
import com.kony.tag.js.codereview.base.FormReviewMasterTask;

public class FocusUISkinDisabledRule extends FormReviewMasterTask {
	
	public static final String PROPS_RULE_NAME = "rules.form.all.005.focus_skin_rule";
	public static final String RULE_ID = "FRM-005";
	
	private static final String ERR_SHORT_DESC = "Focus UI Skin not specified";
	
	private static final String ERR_FORM_MENU_FOCUS_SKIN = "Specify Menu Focus Skin for the Form";
	private static final String ERR_FOCUS_SKIN = "Specify Focus Skin for the widget";
	private static final String ERR_FORM_ROW_FOCUS_SKIN = "Specify Row Focus Skin for the widget";
	private static final String ERR_TABPANE_ACTIVE_FOCUS_SKIN = "Specify Active Focus Skin for the TabPane";
	
	private static final String NOTE_TEXT_AREA = "Ignore this warning for SPA(Android) platforms";
	private static final String NOTE_PICKER_VIEW = "Ignore this warning for iOS";
	
	private String _formName = null;
	private static String SKIN_DEFAULTS = "Skin Defaults"; 
	private static String NO_SKIN = "No Skin";
	
	
	public FocusUISkinDisabledRule(ProjectConfig codeReviewConfig) {
		super(codeReviewConfig);
		String[] ruleDescriptions = {ERR_SHORT_DESC};
		super.init(PROPS_RULE_NAME,RULE_ID,ruleDescriptions);

		rulesInit(null,null);
	}

	public void rulesInit(String reviewFileName, String reviewFileDirectoryName) {
		_formName = null;
		setReviewFileDirectory(reviewFileDirectoryName);
		setReviewFile(reviewFileName);
	}
	
	protected void review(WidgetVo widgetVo) throws TagToolException {
		List<WidgetVo> widgetList = null;
		String focusSkin = null;
		String widgetType = widgetVo.getWidgetType();
		String reviewDirectory = getReviewFileDirectory();
		String errMessage = null;
		String specialNote = null;
		
		if (widgetType != null && 
				widgetType.equals(KONY_FORM)) {
			_formName = widgetVo.getWidgetName();
		}
		
		if (widgetVo.isOnClickApplicable() && widgetVo.isFocusUISkinApplicable() && 
				isRuleApplicable(reviewDirectory, widgetType)) {
		
			// Check the blocked UI Skin
			focusSkin = widgetVo.getFocusSkin();
				
			if (focusSkin == null || focusSkin.trim().equals(SKIN_DEFAULTS) || focusSkin.trim().equals(NONE) || focusSkin.trim().equals(NO_SKIN)) {
				if (widgetType.equals(KONY_FORM)) {
					errMessage = ERR_FORM_MENU_FOCUS_SKIN;
				} else if (widgetType.equals(KONY_SEGMENT) || widgetType.equals(KONY_DATA_GRID)) {
					errMessage = ERR_FORM_ROW_FOCUS_SKIN;
				} else if (widgetType.equals(KONY_TAB_PANE)) {
					errMessage = ERR_TABPANE_ACTIVE_FOCUS_SKIN;
				} else {
					errMessage = ERR_FOCUS_SKIN;
				}
				
				if (widgetType.equals(KONY_TEXT_AREA)) {
					specialNote = NOTE_TEXT_AREA;
				} else if (widgetType.equals(KONY_PICKER_VIEW)) {
					specialNote = NOTE_PICKER_VIEW;
				} else {
					specialNote = BLANK;
				}
				addWarning(errMessage, ERR_SHORT_DESC, widgetVo.getWidgetType() + ":" + widgetVo.getWidgetName()  + " : " + specialNote, _formName, 0, SEV_MED, RULE_ID);
			}
		}

		widgetList = widgetVo.getChildWidgets();

		// Perform Form Level Attributes Review Here
		if (widgetList == null || widgetList.size()==0) {
			return;
		}

		// Recursively review all widgets
		for (WidgetVo widget: widgetList) {
				review(widget);
		} 
	}
	
	private boolean isRuleApplicable(String directory, String widgetType) {
		boolean isApplicable = false;
		
		if (directory == null || widgetType == null) {
			return false;
		}
		
		if (FORM_FOLDERS_ROOT_ALL_MOBILE_TAB.contains(directory)) {
			return true;
		}
		
		if (widgetType.equals(KONY_FORM) || widgetType.equals(KONY_OBJ_SELECTOR_3D)) {
			if (FORM_FOLDERS_NATIVE_ONLY.contains(directory)) {
				isApplicable = true;
			}
		} else {
			if (FORM_FOLDERS_NATIVE_ONLY.contains(directory) || 
					FORM_FOLDERS_SPA_ONLY.contains(directory)) {
				isApplicable = true;
			}
		}
		
		return isApplicable;
	}
}