package com.kony.tag.js.codereview.tasks.formreview.impl;

import java.util.List;

import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.TagToolException;
import com.kony.tag.config.WidgetVo;
import com.kony.tag.js.codereview.base.FormReviewMasterTask;

public class NestedContainerWidgetRule extends FormReviewMasterTask {
	
	public static final String PROPS_RULE_NAME = "rules.form.all.009.nested_container_widgets_rule";
	public static final String RULE_ID = "FRM-009";
	public static final String PROPS_REDUNDANT_NESTED_LEVELS_ALLOWED = "rules.form.all.009.redundant_nested_levels_allowed"; 
	
	private static final String ERR_NESTED_CONTAINERS = "Redundant Nested Container Widgets Found. Re evaluate the UI Design";
	private static final String NOTE_NESTED_CONTAINERS = "# Redundant Nested containers : ";
	
	private static final String ERR_SHORT_DESC = "Redundant Nested Containers";
	
	private static int REDUNDANT_LEVELS_ALLOWED = 1;
	
	private String _formName = null;
	
	public NestedContainerWidgetRule(ProjectConfig codeReviewConfig) {
		super(codeReviewConfig);
		String[] ruleDescriptions = {ERR_SHORT_DESC};
		super.init(PROPS_RULE_NAME,RULE_ID,ruleDescriptions);

		REDUNDANT_LEVELS_ALLOWED = Integer.parseInt(
				getProjectConfig().getCodeReviewProperty(PROPS_REDUNDANT_NESTED_LEVELS_ALLOWED));
		
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
		int nestedContainerWidgetCount = 0;
	
		if (widgetType != null && 
				widgetType.equals(KONY_FORM)) {
			_formName = widgetVo.getWidgetName();
		}
		
		widgetList = widgetVo.getChildWidgets();
		
		if (isRuleApplicable(reviewDirectory, widgetType)) {
			nestedContainerWidgetCount = getNestedContainerWidgetCount(widgetVo);
			if (nestedContainerWidgetCount>REDUNDANT_LEVELS_ALLOWED) {
				addError(ERR_NESTED_CONTAINERS, ERR_SHORT_DESC, widgetVo.getWidgetType() + ":" + widgetVo.getWidgetName() + NOTE_NESTED_CONTAINERS + nestedContainerWidgetCount , _formName, 0, SEV_MED, RULE_ID);
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
		
		if (widgetType.equals(KONY_HBOX) || widgetType.equals(KONY_VBOX)) {
			// All folders applicable. 
			isApplicable = true;
		}

		return isApplicable;
	}
	
	private int getNestedContainerWidgetCount(WidgetVo widget) {
		
		int count=0;
		WidgetVo childWidget = null;
		String widgetType = null;
		
		if(widget == null) {
			return 0;
		}
		widgetType = widget.getWidgetType();
		if(widgetType == null) {
			return 0;
		}
		
		if(!(widgetType.equals(KONY_HBOX) || widgetType.equals(KONY_VBOX))) {
			return 0;
		}

		if (widget.getChildWidgets() != null && widget.getChildWidgets().size()==1) {
			childWidget = widget.getChildWidgets().get(0);
			if (childWidget.getWidgetType() !=null ) {
				if (childWidget.getWidgetType().equals(KONY_HBOX) || 
						childWidget.getWidgetType().equals(KONY_VBOX)) {
					count = 1 + getNestedContainerWidgetCount(childWidget);
				}
			}
		}
		
		return count;
		
	}	
}


