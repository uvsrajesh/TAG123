package com.kony.tag.js.codereview.tasks.formreview.impl;

import java.util.List;

import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.TagToolException;
import com.kony.tag.config.WidgetVo;
import com.kony.tag.js.codereview.base.FormReviewMasterTask;

public class SingleWidgetRule extends FormReviewMasterTask {
	
	public static final String PROPS_RULE_NAME = "rules.form.all.006.single_widget_in_container_rule";
	public static final String RULE_ID = "FRM-006";
	
	private static final String ERR_SINGLE_WIDGET = "Redundant usage of HBox/VBox. Single UI widget found in the container widget (HBox/VBox). Consider changing the form design.";
	private static final String ERR_NO_WIDGET = "Redundant usage of HBox/VBox. Container widget is empty (HBox/VBox).  Consider changing the form design.";
	
	private static final String ERR_SHORT_DESC = "Redundant HboxVbox";
	
	private String _formName = null;
	
	public SingleWidgetRule(ProjectConfig codeReviewConfig) {
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
		boolean errFlag=false;
	
		if (widgetType != null && 
				widgetType.equals(KONY_FORM)) {
			_formName = widgetVo.getWidgetName();
		}
		
		widgetList = widgetVo.getChildWidgets();
		
		if (isRuleApplicable(reviewDirectory, widgetType)) {
			
			if (widgetList != null && widgetList.size()==0) {
				addError(ERR_NO_WIDGET, ERR_SHORT_DESC, widgetVo.getWidgetType() + ":" + widgetVo.getWidgetName(), _formName, 0, SEV_MED, RULE_ID);
			} else if (widgetList != null && widgetList.size()==1) {
				childWidget = widgetList.get(0);
				
				errFlag = false;
				
				if (childWidget.isContainerWidget()) {
					int count = getUIWidgetCount(childWidget);
					if (count==1) {
						errFlag = true;
					}
				} else {
					errFlag = true;
				}
					
				if (errFlag) {
					addError(ERR_SINGLE_WIDGET, ERR_SHORT_DESC, widgetVo.getWidgetType() + ":" + widgetVo.getWidgetName(), _formName, 0, SEV_MED, RULE_ID);
					return;
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
		
		if (widgetType.equals(KONY_HBOX) ||
				widgetType.equals(KONY_VBOX)) {
			isApplicable = true;
		}
		
		return isApplicable;
	}
		
	private int getUIWidgetCount(WidgetVo widget) {
		
		int count=0;
		List<WidgetVo> childWidgets = null;
		
		if(widget == null) {
			return 0;
		}
		childWidgets = widget.getChildWidgets();
		
		
		if (!widget.isContainerWidget()) {
			return 1;
		} else {
			if (childWidgets != null && childWidgets.size()>0) {
				for (WidgetVo widgetVo : childWidgets) {
					if (widgetVo.isContainerWidget()) {
						count += getUIWidgetCount(widgetVo);
					}else {
						count += 1;
					}
				}
			}
		}
		
		return count;
		
	}
}
