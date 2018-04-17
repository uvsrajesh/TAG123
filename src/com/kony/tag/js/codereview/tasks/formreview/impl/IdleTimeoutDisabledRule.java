package com.kony.tag.js.codereview.tasks.formreview.impl;

import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.TagToolException;
import com.kony.tag.config.WidgetVo;
import com.kony.tag.js.codereview.base.FormReviewMasterTask;

public class IdleTimeoutDisabledRule extends FormReviewMasterTask {
	
	public static final String PROPS_RULE_NAME = "rules.form.all.003.idle_timeout_rule";
	public static final String RULE_ID = "FRM-003";
	
	private static final String ERR_IDLE_TIMEOUT_DISABLED = "Check if Idle Timeout alert is needed for this form and set Idle Timeout Flag to true";
	private static final String ERR_SHORT_DESC = "Idle Timeout disabled for the Form";
	
	private String _formName = null;

	public IdleTimeoutDisabledRule(ProjectConfig codeReviewConfig) {
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
	
	protected void review(WidgetVo formWidgetsVo) throws TagToolException {
		
		if (getReviewFileDirectory() != null &&
				!((getReviewFileDirectory().startsWith(LOCATION_MOBILE_FORMS))
						|| (getReviewFileDirectory().startsWith(LOCATION_TABLET_FORMS))
						|| (getReviewFileDirectory().startsWith(LOCATION_DESKTOP_FORMS)))	
		) {
				// Only Mobile, Tablet, Desktop forms to be reviewed. Do not review pop ups
				return;
		}		
		
		if (formWidgetsVo.getWidgetType() != null && 
				formWidgetsVo.getWidgetType().equals(KONY_FORM)) {
			_formName = formWidgetsVo.getWidgetName();
			
			if (!formWidgetsVo.isIdleTimeOutEnabled()) {
				addError(ERR_IDLE_TIMEOUT_DISABLED, ERR_SHORT_DESC,formWidgetsVo.getWidgetType() + ":" + formWidgetsVo.getWidgetName(), _formName, 0, SEV_HIGH, RULE_ID);
			}
		}
		
	}
}
