package com.kony.tag.js.codereview.tasks.formreview.impl;

import java.util.ArrayList;
import java.util.List;

import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.TagToolException;
import com.kony.tag.config.WidgetVo;
import com.kony.tag.js.codereview.base.FormReviewMasterTask;

public class SingleBrowserMapWidgetsRule extends FormReviewMasterTask {
	
	public static final String PROPS_RULE_NAME = "rules.form.all.010.single_browser_map_widget_rule";
	public static final String RULE_ID = "FRM-010";
	
	private static final String ERR_MOBILE_APP_BROWSER_WIDGET = "Only a Single Browser Widget per app is recommended for the Mobile App";
	private static final String ERR_TABLET_APP_BROWSER_WIDGET = "Only a Single Browser Widget per app is recommended for the Tablet App";
	private static final String ERR_MOBILE_APP_MAP_WIDGET = "Only a Single Map Widget per app is recommended for the Mobile App";
	private static final String ERR_TABLET_APP_MAP_WIDGET = "Only a Single Map Widget per app is recommended for the Tablet App";
	
	private static final String ERR_SHORT_DESC = "Non Optimal usage of Heavy Widgets";

	private static final String NOTES_BROWSER_WIDGET = "# Browser Widgets Found : ";
	private static final String NOTES_MAP_WIDGET = "# Map Widgets Found : ";

	private String _formName = null;

	private List<String> mobileAppBrowserWidgets = null;
	private List<String> mobileAppMapWidgets = null;
	
	private List<String> tabletAppBrowserWidgets = null;
	private List<String> tabletAppMapWidgets = null;

	public SingleBrowserMapWidgetsRule(ProjectConfig codeReviewConfig) {
		super(codeReviewConfig);
		String[] ruleDescriptions = {ERR_SHORT_DESC};
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
		
		if (widgetType != null && 
				widgetType.equals(KONY_FORM)) {
			_formName = widgetVo.getWidgetName();
			
			updateNestedWidgetCount(widgetVo);
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
	
	public void postReview() throws TagToolException {
		setReviewFileDirectory(LOCATION_MOBILE_FORMS);
		
		if (mobileAppBrowserWidgets.size() >1 ) { 
			addError(ERR_MOBILE_APP_BROWSER_WIDGET,ERR_SHORT_DESC, NOTES_BROWSER_WIDGET  + ":" + mobileAppBrowserWidgets.size() + " :: " + mobileAppBrowserWidgets.toString(), _formName, 0, SEV_MED, RULE_ID);
		}
		
		if (mobileAppMapWidgets.size() >1 ) { 
			addError(ERR_MOBILE_APP_MAP_WIDGET,ERR_SHORT_DESC, NOTES_MAP_WIDGET  + ":" + mobileAppMapWidgets.size() + " :: " + mobileAppMapWidgets.toString(), _formName, 0, SEV_MED, RULE_ID);
		}

		setReviewFileDirectory(LOCATION_TABLET_FORMS);
		if (tabletAppBrowserWidgets.size() >1 ) { 
			addError(ERR_TABLET_APP_BROWSER_WIDGET,ERR_SHORT_DESC, NOTES_BROWSER_WIDGET  + ":" + tabletAppBrowserWidgets.size() + " :: " + tabletAppBrowserWidgets.toString(), _formName, 0, SEV_MED, RULE_ID);
		}

		if (tabletAppMapWidgets.size() >1 ) { 
			addError(ERR_TABLET_APP_MAP_WIDGET,ERR_SHORT_DESC, NOTES_MAP_WIDGET  + ":" + tabletAppMapWidgets.size() + " :: " + tabletAppMapWidgets.toString(), _formName, 0, SEV_MED, RULE_ID);
		}

	}
}


