package com.kony.tag.js.codereview.tasks.formreview.impl;

import java.util.ArrayList;
import java.util.List;

import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.TagToolException;
import com.kony.tag.config.WidgetVo;
import com.kony.tag.js.codereview.base.FormReviewMasterTask;

public class LastWidgetBrowserMapRule extends FormReviewMasterTask {
	
	public static final String PROPS_RULE_NAME = "rules.form.all.012.last_widget_browser_map_rule";
	public static final String RULE_ID = "FRM-012";
	
	private static final String ERR_LAST_WIDGET_MAP = "Map Widget on this form should be the last widget";
	private static final String ERR_LAST_WIDGET_BROWSER = "Browser Widget on this form should be the last widget";
	
	private static final String NOTES_LAST_WIDGET = "Last Widget found : ";
	private static final String ERR_SHORT_DESC_MAP = "Widgets found below Mapwidget";
	private static final String ERR_SHORT_DESC_BROWSER = "Widgets found below Browser widget";

	private String _formName = null;

	private List<String> mobileAppBrowserWidgets = null;
	private List<String> mobileAppMapWidgets = null;
	
	private List<String> tabletAppBrowserWidgets = null;
	private List<String> tabletAppMapWidgets = null;

	public LastWidgetBrowserMapRule(ProjectConfig codeReviewConfig) {
		super(codeReviewConfig);
		String[] ruleDescriptions = {ERR_SHORT_DESC_BROWSER,ERR_SHORT_DESC_MAP};
		super.init(PROPS_RULE_NAME,RULE_ID,ruleDescriptions);

		mobileAppBrowserWidgets = new ArrayList<String>();
		mobileAppMapWidgets = new ArrayList<String>();
		tabletAppBrowserWidgets = new ArrayList<String>();
		tabletAppMapWidgets = new ArrayList<String>();

		rulesInit(null,null);
	}

	public void rulesInit(String reviewFileName, String reviewFileDirectoryName) {
		_formName = null;
		
		setReviewFileDirectory(reviewFileDirectoryName);
		setReviewFile(reviewFileName);
		
	}
	
	protected void review(WidgetVo widgetVo) throws TagToolException {
		String widgetType = widgetVo.getWidgetType();
		WidgetVo lastWidget = null;
		
		mobileAppBrowserWidgets.clear();
		mobileAppMapWidgets.clear();
		tabletAppBrowserWidgets.clear();
		tabletAppMapWidgets.clear();
		
		if (widgetType != null && 
				widgetType.equals(KONY_FORM)) {
			_formName = widgetVo.getWidgetName();
			updateNestedWidgetCount(widgetVo);
			
			if (mobileAppBrowserWidgets.size()>0 || tabletAppBrowserWidgets.size()>0) {
				lastWidget = getLastWidget(widgetVo);
				if (lastWidget != null) {
					if(!lastWidget.getWidgetType().equals(KONY_BROWSER)) {
						addError(ERR_LAST_WIDGET_BROWSER, ERR_SHORT_DESC_BROWSER, NOTES_LAST_WIDGET + " :: " +  lastWidget.getWidgetType() + ":" + lastWidget.getWidgetName(), _formName, 0, SEV_HIGH, RULE_ID);
					}
				} 
			}
			
			if (mobileAppMapWidgets.size()>0 || tabletAppMapWidgets.size()>0) {
				
				if (null == lastWidget) {
					lastWidget = getLastWidget(widgetVo);
				}
				
				if (lastWidget != null) {
					if(!lastWidget.getWidgetType().equals(KONY_MAP)) {
						addError(ERR_LAST_WIDGET_MAP, ERR_SHORT_DESC_MAP, NOTES_LAST_WIDGET + " :: " +  lastWidget.getWidgetType() + ":" + lastWidget.getWidgetName(), _formName, 0, SEV_HIGH, RULE_ID);
					}
				} 
			}			
		}

	}
	
	private WidgetVo getLastWidget(WidgetVo widgetVo) {
		WidgetVo lastWidget = null;
		List<WidgetVo> childWidgets = null;
		int childWidgetCount = 0;
		
		if (widgetVo == null) {
			return null;
		}
		childWidgets = widgetVo.getChildWidgets();
		
		if (childWidgets != null) {
			childWidgetCount = childWidgets.size();
		}
		
		if (childWidgetCount ==0) {
			return widgetVo;
		} else {
			lastWidget = childWidgets.get(childWidgetCount-1);
			if (lastWidget.getChildWidgets() != null && lastWidget.getChildWidgets().size()>0) {
				return getLastWidget(lastWidget);
			} else {
				return lastWidget;
			}
		}
	}
	
	private boolean isRuleApplicable(String directory, String widgetType) {
		
		if (directory == null || widgetType == null) {
			return false;
		}
		
		return true;
	}
	
	private void updateNestedWidgetCount(WidgetVo widget) {
		
		List<WidgetVo> childWidgets = null;
		String reviewDirectory = getReviewFileDirectory();
		
		if(widget == null || !widget.isContainerWidget() || reviewDirectory ==null) {
			return;
		}
		
		childWidgets = widget.getChildWidgets();
		
		if (null == childWidgets) {
			return;
		}
		
		for (WidgetVo childWidget : childWidgets) {
			if (childWidget.isContainerWidget()) {
				updateNestedWidgetCount(childWidget); 
			} else if (childWidget.getWidgetType() != null) {
				if (childWidget.getWidgetType().equals(KONY_BROWSER)) {
					if (FORM_FOLDERS_MOBILE_APP_ONLY.contains(reviewDirectory)) { 
							if (!mobileAppBrowserWidgets.contains(childWidget.getWidgetName())) {
								mobileAppBrowserWidgets.add(childWidget.getWidgetName());
							} 
					} else if (FORM_FOLDERS_TAB_APP_ONLY.contains(reviewDirectory)) {
						if (!tabletAppBrowserWidgets.contains(childWidget.getWidgetName())) {
							tabletAppBrowserWidgets.add(childWidget.getWidgetName());
						} 
					}
				} else if (childWidget.getWidgetType().equals(KONY_MAP)) {
					if (FORM_FOLDERS_MOBILE_APP_ONLY.contains(reviewDirectory)) { 
						if (!mobileAppMapWidgets.contains(childWidget.getWidgetName())) {
							mobileAppMapWidgets.add(childWidget.getWidgetName());
						} 
				} else if (FORM_FOLDERS_TAB_APP_ONLY.contains(reviewDirectory)) {
					if (!tabletAppMapWidgets.contains(childWidget.getWidgetName())) {
						tabletAppMapWidgets.add(childWidget.getWidgetName());
					} 
				}
			}
		}
		}
		
	}
}