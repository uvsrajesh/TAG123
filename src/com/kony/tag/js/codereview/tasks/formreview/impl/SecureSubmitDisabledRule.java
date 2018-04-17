package com.kony.tag.js.codereview.tasks.formreview.impl;

import java.util.ArrayList;
import java.util.Arrays;

import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.TagToolException;
import com.kony.tag.config.WidgetVo;
import com.kony.tag.js.codereview.base.FormReviewMasterTask;

public class SecureSubmitDisabledRule extends FormReviewMasterTask {
	
	public static final String PROPS_RULE_NAME = "rules.form.mobileweb.002.secure_submit_rule";
	public static final String RULE_ID = "FRM-002";
	private static final String ERR_SECURE_SUBMIT_DISABLED = "Secure Submit is disbaled for this form. Ensure that it is acceptable";
	private static final String ERR_SHORT_DESC = "Secure Submit disabled";
	private final ArrayList<String> applicableFolders = new ArrayList<String>(
			Arrays.asList(
					LOCATION_MOBILE_FORMS,
					LOCATION_MOBILE_FORMS_ADVPALM,
					LOCATION_MOBILE_FORMS_BJS,
					LOCATION_MOBILE_FORMS_NORMAL
					// Not applicable for pop ups
					/*
					,LOCATION_ALL_POPUPS,
					LOCATION_MOBILE_POPUPS_ADVPALM,
					LOCATION_MOBILE_POPUPS_BJS,
					LOCATION_MOBILE_POPUPS_NORMAL
					*/
					));

	private String _formName = null;

	public SecureSubmitDisabledRule(ProjectConfig codeReviewConfig) {
		super(codeReviewConfig);
		String[] ruleDescriptions = {ERR_SHORT_DESC};
		super.init(PROPS_RULE_NAME,RULE_ID,ruleDescriptions);

		rulesInit(null,null);
		
		// This rule is applicable for Mobile Web only
		// Turn off this rule if user is not reviewing for mobile web channel
		if (!isMobileWebReviewEnabled()) {
			setEnabled(false);
		}
		
		setMobileSpaReviewEnabled(false);
		setTabletSpaReviewEnabled(false);
		setDesktopWebReviewEnabled(false);
	}

	public void rulesInit(String reviewFileName, String reviewFileDirectoryName) {
		_formName = null;
		setReviewFileDirectory(reviewFileDirectoryName);
		setReviewFile(reviewFileName);
	}
	
	protected void review(WidgetVo formWidgetsVo) throws TagToolException {
		
		if (getReviewFileDirectory() != null &&
				!(applicableFolders.contains(getReviewFileDirectory()))) {
				// This folder need not be reviewed.
				return;
			}
		
		if (formWidgetsVo.getWidgetType() != null && 
				formWidgetsVo.getWidgetType().equals(KONY_FORM)) {
			_formName = formWidgetsVo.getWidgetName();
			
			if (!formWidgetsVo.isSecureSubmitEnabled()) {
				addError(ERR_SECURE_SUBMIT_DISABLED, ERR_SHORT_DESC, formWidgetsVo.getWidgetType() + ":" + formWidgetsVo.getWidgetName(), _formName, 0, SEV_MED, RULE_ID);
			}
		}
		
	}
	
}
