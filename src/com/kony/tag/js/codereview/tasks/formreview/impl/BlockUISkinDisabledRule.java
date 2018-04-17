package com.kony.tag.js.codereview.tasks.formreview.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.TagToolException;
import com.kony.tag.config.WidgetVo;
import com.kony.tag.js.codereview.base.FormReviewMasterTask;

public class BlockUISkinDisabledRule extends FormReviewMasterTask {
	
	public static final String PROPS_RULE_NAME = "rules.form.mobileweb.004.block_ui_skin_rule";
	public static final String RULE_ID = "FRM-004";
	
	private static final String ERR_TC_BLOCK_UISKIN_DISABLED = "Specify Block UI Skin for the Widget (for Mobile Web)";
	private static final String ERR_SPA_BLOCK_UISKIN_DISABLED = "Specify Block UI Skin for the Widget (for SPA)";
	
	private static final String ERR_SHORT_DESC = "Block UI Skin not specified";
	
	private String _formName = null;
	private static String SKIN_DEFAULTS = "Skin Defaults"; 
	
	private final ArrayList<String> applicableFolders = new ArrayList<String>(
			Arrays.asList(
					LOCATION_MOBILE_FORMS,
					LOCATION_MOBILE_FORMS_ADVPALM,
					LOCATION_MOBILE_FORMS_SPA_ANDROID,
					LOCATION_MOBILE_FORMS_SPA_IPHONE,
					LOCATION_MOBILE_FORMS_SPA_BLACKBERRY,
					LOCATION_MOBILE_FORMS_SPA_BLACKBERRY_NONTOUCH,
					LOCATION_MOBILE_FORMS_SPA_WINDOWS_NONTOUCH,					

					LOCATION_TABLET_FORMS,
					LOCATION_TABLET_FORMS_SPA_ANDROID,
					LOCATION_TABLET_FORMS_SPA_IPAD,

					LOCATION_MOBILE_POPUPS,
					LOCATION_MOBILE_POPUPS_ADVPALM,
					LOCATION_MOBILE_POPUPS_SPA_ANDROID,
					LOCATION_MOBILE_POPUPS_SPA_IPHONE,
					LOCATION_MOBILE_POPUPS_SPA_BLACKBERRY,
					LOCATION_MOBILE_POPUPS_SPA_BLACKBERRY_NONTOUCH,
					LOCATION_MOBILE_POPUPS_SPA_WINDOWS_NONTOUCH,
					
					LOCATION_TABLET_POPUPS,
					LOCATION_TABLET_POPUPS_SPA_ANDROID,
					LOCATION_TABLET_POPUPS_SPA_IPAD
					));

	public BlockUISkinDisabledRule(ProjectConfig codeReviewConfig) {
		super(codeReviewConfig);
		String[] ruleDescriptions = {ERR_SHORT_DESC};
		super.init(PROPS_RULE_NAME,RULE_ID,ruleDescriptions);

		rulesInit(null,null);
	}

	public void rulesInit(String reviewFileName, String reviewFileDirectoryName) {
		_formName = null;
		setReviewFileDirectory(reviewFileDirectoryName);
		setReviewFile(reviewFileName);
		
		// This rule is applicable for Mobile Web only (Server Side only) 
		if (isMobileWebReviewEnabled()) {
			setEnabled(true);
		} else {
			setEnabled(false);
		}
	}
	
	protected void review(WidgetVo widgetVo) throws TagToolException {
		List<WidgetVo> widgetList = null;
		String defaultSkin = null;
		String skinTC = null;
		String skinSPA = null;
		
		
		if (getReviewFileDirectory() != null &&
				!(applicableFolders.contains(getReviewFileDirectory()))) {
				// This folder need not be reviewed.
				//System.out.println("Invalid Folder: " + getReviewFileDirectory());
				return;
		}
		
		if (widgetVo.getWidgetType() != null && 
				widgetVo.getWidgetType().equals(KONY_FORM)) {
			_formName = widgetVo.getWidgetName();
		}
		
		if (widgetVo.isOnClickApplicable() && widgetVo.isBlockUISkinApplicable()) {
			// Check the blocked UI Skin
			defaultSkin = widgetVo.getBlockedUISkinDefault();
			skinTC = widgetVo.getBlockedUISkinTC();
			skinSPA = widgetVo.getBlockedUISkinSPA();
			
			if (defaultSkin == null || defaultSkin.trim().equalsIgnoreCase(SKIN_DEFAULTS) || defaultSkin.trim().equalsIgnoreCase(NONE)) {
				if (skinTC == null || skinTC.trim().equals(SKIN_DEFAULTS) || skinTC.trim().equals(NONE)) {
					addWarning(ERR_TC_BLOCK_UISKIN_DISABLED, ERR_SHORT_DESC,widgetVo.getWidgetType() + ":" + widgetVo.getWidgetName(), _formName, 0, SEV_MED, RULE_ID);
				}

				// KUMAR CHITTA: Jan 9th 2014: commented the following logic because block UI rule does not apply for SPA
				/*
				if (skinSPA == null || skinSPA.trim().equals(SKIN_DEFAULTS) || skinSPA.trim().equals(NONE)) {
					addWarning(ERR_SPA_BLOCK_UISKIN_DISABLED,ERR_SHORT_DESC, widgetVo.getWidgetType() + ":" + widgetVo.getWidgetName(), _formName, 0, SEV_MED, RULE_ID);
				}
				*/
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
		
}
