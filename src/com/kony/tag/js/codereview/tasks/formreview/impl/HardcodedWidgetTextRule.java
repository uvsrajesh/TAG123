package com.kony.tag.js.codereview.tasks.formreview.impl;

import java.util.List;

import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.TagToolException;
import com.kony.tag.config.WidgetVo;
import com.kony.tag.js.codereview.base.FormReviewMasterTask;

public class HardcodedWidgetTextRule extends FormReviewMasterTask {
	
	public static final String PROPS_RULE_NAME = "rules.form.all.015.harcoded_i18keys_rule";
	public static final String RULE_ID = "FRM-015";
	
	private static final String ERR_HARDCODED_TEXT = "i18 Key to be used for the Display Text instead of a hardcoded value";
	private static final String ERR_HARDCODED_MASTERDATA = "Master Data cannot be hard coded, unless the widget is in preview mode";
	
	private static final String ERR_SHORT_DESC = "Hardcoded data found for Display Text";
	
	private String _formName = null;

	public HardcodedWidgetTextRule(ProjectConfig codeReviewConfig) {
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
		List<String> widgetTextList = null;
		List<String> masterDataApplicableWidgetList = widgetVo.getMasterDataApplicableList();
		
		if (widgetVo.getWidgetType() != null && 
				widgetVo.getWidgetType().equals(KONY_FORM)) {
			_formName = widgetVo.getWidgetName();
		}
		
		// Perform Form Level Attributes Review Here
		// start reviewing individual widgets
		if (widgetVo.isWidgetTextApplicable()) {
			
			widgetTextList = widgetVo.getWidgetTextList();
			if(masterDataApplicableWidgetList.contains(widgetVo.getWidgetType())) {
				if(widgetVo.isMasterDataSetToWidget() && !widgetVo.isMasterDataSetToWidgetForPreview()) {
					addError(ERR_HARDCODED_MASTERDATA, ERR_SHORT_DESC, widgetVo.getWidgetType() + ":" + widgetVo.getWidgetName(), _formName, 0, SEV_HIGH, RULE_ID);
				}
			} else {
				if (widgetTextList != null && widgetTextList.size()>0) {
					for (String widgetText : widgetTextList) {
						if (widgetText != null
								&& !widgetText.trim().equalsIgnoreCase(NONE) && widgetText.trim().length() > 0) {
							addError(ERR_HARDCODED_TEXT, ERR_SHORT_DESC, widgetVo.getWidgetType() + ":" + widgetVo.getWidgetName() + " : " + widgetText, _formName, 0, SEV_HIGH, RULE_ID);
							break;
						}
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
