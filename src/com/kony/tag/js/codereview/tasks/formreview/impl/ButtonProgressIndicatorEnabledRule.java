package com.kony.tag.js.codereview.tasks.formreview.impl;

import java.util.List;

import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.TagToolException;
import com.kony.tag.config.WidgetVo;
import com.kony.tag.js.codereview.base.FormReviewMasterTask;

public class ButtonProgressIndicatorEnabledRule extends FormReviewMasterTask {
	
	public static final String PROPS_RULE_NAME = "rules.form.all.016.button_progress_indicator_enabled_rule";
	public static final String RULE_ID = "FRM-016";
	
	private static final String ERR_BTN_PROGRESS_INDICATOR_RULE = "Button progress indicator has been set to true. Check if this is intentional.";
	
	private static final String ERR_SHORT_DESC = "Button Progress indicator set to true";
	
	private String _formName = null;
	

	public ButtonProgressIndicatorEnabledRule(ProjectConfig codeReviewConfig) {
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
		String widgetType = widgetVo.getWidgetType();
		
		if (widgetType != null && 
				widgetType.equals(KONY_FORM)) {
			_formName = widgetVo.getWidgetName();
		}
		
		widgetList = widgetVo.getChildWidgets();
		
		if (isRuleApplicable(getReviewFileDirectory(), widgetType)) {
			
			if (widgetVo.isBtnProgressIndicatorEnabled()) {
				addWarning(ERR_BTN_PROGRESS_INDICATOR_RULE,ERR_SHORT_DESC, widgetVo.getWidgetType() + ":" + widgetVo.getWidgetName(), _formName, 0, SEV_MED, RULE_ID);
			}
		}
		
		if (widgetList == null || widgetList.size()==0) {
			return;
		}

		// Recursively review all widgets
		for (WidgetVo widget: widgetList) {
				review(widget);
		} 
		
	}
	
	private boolean isRuleApplicable(String directory, String widgetType) {
		
		if (directory == null || widgetType == null) {
			return false;
		}
		
		if (widgetType.equals(KONY_BUTTON)) {
			return true;
		} else {
			return false;
		}
		
	}
}