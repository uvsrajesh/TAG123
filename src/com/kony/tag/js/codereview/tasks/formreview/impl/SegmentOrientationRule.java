package com.kony.tag.js.codereview.tasks.formreview.impl;

import java.util.List;

import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.TagToolException;
import com.kony.tag.config.WidgetVo;
import com.kony.tag.js.codereview.base.FormReviewMasterTask;

public class SegmentOrientationRule extends FormReviewMasterTask {
	
	public static final String PROPS_RULE_NAME = "rules.form.all.008.segment_orientation_rule";
	public static final String RULE_ID = "FRM-008";
	
	private static final String ERR_SEGMENT_ORIENTATION = "Redundant HBOX/VBOX used in the Segment. Consider using Segment Orientation Property to avoid the same.";
	private static final String ERR_SHORT_DESC = "Segment Orientation not used";
	
	private String _formName = null;
	
	public SegmentOrientationRule(ProjectConfig codeReviewConfig) {
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
		String reviewDirectory = getReviewFileDirectory();
		WidgetVo childWidget = null;
	
		if (widgetType != null && 
				widgetType.equals(KONY_FORM)) {
			_formName = widgetVo.getWidgetName();
		}
		
		widgetList = widgetVo.getChildWidgets();
		
		if (isRuleApplicable(reviewDirectory, widgetType)) {

			if (widgetList != null && widgetList.size()==1) {
				childWidget = widgetList.get(0);
				
				if (childWidget.getWidgetType().equals(KONY_HBOX) || 
						childWidget.getWidgetType().equals(KONY_VBOX)) {
					addError(ERR_SEGMENT_ORIENTATION,ERR_SHORT_DESC, widgetVo.getWidgetType() + ":" + widgetVo.getWidgetName(), _formName, 0, SEV_MED, RULE_ID);
				}
			}
		}

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
		
		if (widgetType.equals(KONY_SEGMENT)) {
			// All folders applicable. Screen level widget property supported for all paltforms for a browser widget
			isApplicable = true;
		}

		return isApplicable;
	}
}


