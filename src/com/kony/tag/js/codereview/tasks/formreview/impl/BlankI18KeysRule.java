package com.kony.tag.js.codereview.tasks.formreview.impl;

import java.util.List;

import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.TagToolException;
import com.kony.tag.config.WidgetVo;
import com.kony.tag.js.codereview.base.FormReviewMasterTask;

public class BlankI18KeysRule extends FormReviewMasterTask {
	
	public static final String PROPS_RULE_NAME = "rules.form.all.001.blank_i18keys_rule";
	public static final String RULE_ID = "FRM-001";
	
	private static final String ERR_I18KEY_TITLE = "i18 Key to be used for the Display Text";
	private static final String ERR_I18KEY_PLACEHOLDER = "i18 Key to be used for the Place Holder Text";
	private static final String ERR_I18KEYS = "i18 Keys to be used for displaying text for all Display Choices of the Widget";
	
	private static final String ERR_SHORT_DESC = "I18 Key not assigned to a widget";
	
	private String _formName = null;

	public BlankI18KeysRule(ProjectConfig codeReviewConfig) {
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
		List<String> i18Keys = null;
		
		if (widgetVo.getWidgetType() != null && 
				widgetVo.getWidgetType().equals(KONY_FORM)) {
			_formName = widgetVo.getWidgetName();
		}
		
		// Perform Form Level Attributes Review Here
		// start reviewing individual widgets
		if (widgetVo.isI18TitleApplicable()) {
			if (widgetVo.getI18Title() == null 
					|| widgetVo.getI18Title().toLowerCase().trim().equalsIgnoreCase(NONE)) {
				addError(ERR_I18KEY_TITLE,ERR_SHORT_DESC, widgetVo.getWidgetType() + ":" + widgetVo.getWidgetName(),_formName, 0, SEV_HIGH, RULE_ID);
			}
		}
		
		if (widgetVo.isI18PlaceHolderApplicable()) {
			if (widgetVo.getI18PlaceHolder() == null 
					|| widgetVo.getI18PlaceHolder().toLowerCase().trim().equalsIgnoreCase(NONE)) {
				addError(ERR_I18KEY_PLACEHOLDER,ERR_SHORT_DESC, widgetVo.getWidgetType() + ":" + widgetVo.getWidgetName(), _formName, 0, SEV_HIGH, RULE_ID);
			}
			
		}

		if (widgetVo.isI18KeysApplicable()) {
			
			i18Keys = widgetVo.getI18Keys();
			
			if (i18Keys != null && i18Keys.size()>0) {
				for (String i18Key : i18Keys) {
					if (i18Key == null
							|| i18Key.toLowerCase().trim().length() == 0
							|| i18Key.toLowerCase().trim().equalsIgnoreCase(NONE)) {
						addError(ERR_I18KEYS,ERR_SHORT_DESC, widgetVo.getWidgetType() + ":" + widgetVo.getWidgetName(), _formName, 0, SEV_HIGH, RULE_ID);
						break;
					}
				}
			}
			
		}
		
		// end reviewing individual widgets		
		
		// Recursively Review all child widgets also
		widgetList = widgetVo.getChildWidgets();
		
		if (widgetList == null || widgetList.size()==0) {
			return;
		}
		
		for (WidgetVo widget: widgetList) {
			review(widget);
		}
	}
	
}
