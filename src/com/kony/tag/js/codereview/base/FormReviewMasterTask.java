package com.kony.tag.js.codereview.base;

import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.TagToolException;
import com.kony.tag.config.WidgetVo;
import com.kony.tag.tasks.base.CodeReviewMasterTask;

public abstract class FormReviewMasterTask extends CodeReviewMasterTask {
	
	private boolean isReviewCategoryEnabled;
	
	private boolean isMobileNativeReviewEnabled = false;
	private boolean isMobileSpaReviewEnabled = false;
	private boolean isMobileWebReviewEnabled = false;
	
	private boolean isTabletNativeReviewEnabled = false;
	private boolean isTabletSpaReviewEnabled = false;
	
	private boolean isDesktopWebReviewEnabled = false;
	
	private boolean isForkedFormsReviewEnabled = false;
	

	public boolean isForkedFormsReviewEnabled() {
		return isForkedFormsReviewEnabled;
	}

	public void setForkedFormsReviewEnabled(boolean isForkedFormsReviewEnabled) {
		this.isForkedFormsReviewEnabled = isForkedFormsReviewEnabled;
	}

	public void setMobileNativeReviewEnabled(boolean isMobileNativeReviewEnabled) {
		this.isMobileNativeReviewEnabled = isMobileNativeReviewEnabled;
	}

	public void setMobileSpaReviewEnabled(boolean isMobileSpaReviewEnabled) {
		this.isMobileSpaReviewEnabled = isMobileSpaReviewEnabled;
	}

	public void setMobileWebReviewEnabled(boolean isMobileWebReviewEnabled) {
		this.isMobileWebReviewEnabled = isMobileWebReviewEnabled;
	}

	public void setTabletNativeReviewEnabled(boolean isTabletNativeReviewEnabled) {
		this.isTabletNativeReviewEnabled = isTabletNativeReviewEnabled;
	}

	public void setTabletSpaReviewEnabled(boolean isTabletSpaReviewEnabled) {
		this.isTabletSpaReviewEnabled = isTabletSpaReviewEnabled;
	}

	public void setDesktopWebReviewEnabled(boolean isDesktopWebReviewEnabled) {
		this.isDesktopWebReviewEnabled = isDesktopWebReviewEnabled;
	}
	
	public boolean isMobileNativeReviewEnabled() {
		return isMobileNativeReviewEnabled;
	}

	public boolean isMobileSpaReviewEnabled() {
		return isMobileSpaReviewEnabled;
	}

	public boolean isMobileWebReviewEnabled() {
		return isMobileWebReviewEnabled;
	}

	public boolean isTabletNativeReviewEnabled() {
		return isTabletNativeReviewEnabled;
	}

	public boolean isTabletSpaReviewEnabled() {
		return isTabletSpaReviewEnabled;
	}

	public boolean isDesktopWebReviewEnabled() {
		return isDesktopWebReviewEnabled;
	}
	
	public boolean isReviewCategoryEnabled() {
		return isReviewCategoryEnabled;
	}

	public void setReviewCategoryEnabled(boolean isReviewCategoryEnabled) {
		this.isReviewCategoryEnabled = isReviewCategoryEnabled;
	}
	
	private FormReviewMasterTask() {
		// Do not use
		super(null);
	}
	
	public FormReviewMasterTask(ProjectConfig codeReviewConfig) {
		super(codeReviewConfig);
	}
	
	protected void init(String ruleName,String ruleId, String[] ruleDescriptions) {
		super.init(ruleName, ruleId, ruleDescriptions);
		
		isMobileNativeReviewEnabled = getBooleanValue(
				getProjectConfig().getCodeReviewProperty(PROP_MOBILE_NATIVE_APP_REVIEW_FLAG));
		
		isMobileSpaReviewEnabled = getBooleanValue(
				getProjectConfig().getCodeReviewProperty(PROP_MOBILE_SPA_APP_REVIEW_FLAG));

		isMobileWebReviewEnabled = getBooleanValue(
				getProjectConfig().getCodeReviewProperty(PROP_MOBILE_MOBILEWEB_APP_REVIEW_FLAG));

		isTabletNativeReviewEnabled = getBooleanValue(
				getProjectConfig().getCodeReviewProperty(PROP_TABLET_NATIVE_APP_REVIEW_FLAG));

		isTabletSpaReviewEnabled = getBooleanValue(
				getProjectConfig().getCodeReviewProperty(PROP_TABLET_SPA_APP_REVIEW_FLAG));

		isDesktopWebReviewEnabled = getBooleanValue(
				getProjectConfig().getCodeReviewProperty(PROP_DESKTOPWEB_APP_REVIEW_FLAG));

		isForkedFormsReviewEnabled = getBooleanValue(
				getProjectConfig().getCodeReviewProperty(PROP_FORKED_FORMS_REVIEW_FLAG));

		// By default Review Category is enabled
		setReviewCategoryEnabled(true);

	}
	
	private boolean getBooleanValue(String value) {
		if (value == null) {
			return false;
		}
		value = value.trim();
		
		if (value.equalsIgnoreCase(YES) || value.equalsIgnoreCase(TRUE)) {
			return true;
		}else {
			return false;
		}
	}
	
	public void reviewForm(WidgetVo formWidgetsVo) throws TagToolException {
		review(formWidgetsVo);
	}
	
	protected abstract void review(WidgetVo formWidgetsVo) throws TagToolException;

	@Override
	public void setReviewFileDirectory(String reviewFileDirectory) {
		super.setReviewFileDirectory(reviewFileDirectory);
		
		String directory = getReviewFileDirectory();
		
		setReviewCategoryEnabled(false);
		
		// Check if review category is enabled for this rule based on the review file directory
		if (directory != null && directory.trim().length()>0) {
			
			if (directory.toLowerCase().indexOf(MOBILE)>=0 && 
					directory.toLowerCase().indexOf(SPA)<0 &&
					directory.toLowerCase().indexOf(ADVPALM)<0 &&
					directory.toLowerCase().indexOf(BJS)<0 &&
					directory.toLowerCase().indexOf(NORMAL)<0) {
				// This is Mobile Apps Native (not SPA or Mobile Web)
				
				
				if (directory.toLowerCase().endsWith(MOBILE_SEPARATOR)) {
					// This is not a forked form
					setReviewCategoryEnabled(isMobileNativeReviewEnabled());
				} else {
					// This is a forked form
					setReviewCategoryEnabled(isMobileNativeReviewEnabled() && isForkedFormsReviewEnabled());
				}
				
				
			} else if (directory.toLowerCase().indexOf(MOBILE)>=0 && 
					directory.toLowerCase().indexOf(SPA)>=0) {
				// This is Mobile SPA
				
				if (directory.toLowerCase().endsWith(MOBILE_SEPARATOR)) {
					// This is not a forked form
					setReviewCategoryEnabled(isMobileSpaReviewEnabled());
				} else {
					// This is a forked form
					setReviewCategoryEnabled(isMobileSpaReviewEnabled() && isForkedFormsReviewEnabled());
				}
				
			} else if (directory.toLowerCase().indexOf(MOBILE)>=0 && 
							(directory.toLowerCase().indexOf(ADVPALM)>=0 || 
							 directory.toLowerCase().indexOf(BJS)>=0 || 
							 directory.toLowerCase().indexOf(NORMAL)>=0)
							) {
				// This is Mobile Web

				if (directory.toLowerCase().endsWith(MOBILE_SEPARATOR)) {
					// This is not a forked form
					setReviewCategoryEnabled(isMobileWebReviewEnabled());
				} else {
					// This is a forked form
					setReviewCategoryEnabled(isMobileWebReviewEnabled() && isForkedFormsReviewEnabled());
				}
				
			} else if (directory.toLowerCase().indexOf(TABLET)>=0 && 
					directory.toLowerCase().indexOf(SPA)<0) {
				// This is Mobile Apps Native (not SPA or Mobile Web

				if (directory.toLowerCase().endsWith(TABLET_SEPARATOR)) {
					// This is not a forked form
					setReviewCategoryEnabled(isTabletNativeReviewEnabled());
				} else {
					// This is a forked form
					setReviewCategoryEnabled(isTabletNativeReviewEnabled() && isForkedFormsReviewEnabled());
				}
				
			} else if (directory.toLowerCase().indexOf(TABLET)>=0 && 
					directory.toLowerCase().indexOf(SPA)>=0) {
				// This is Mobile SPA
				
				if (directory.toLowerCase().endsWith(TABLET_SEPARATOR)) {
					// This is not a forked form
					setReviewCategoryEnabled(isTabletSpaReviewEnabled());
				} else {
					// This is a forked form
					setReviewCategoryEnabled(isTabletSpaReviewEnabled() && isForkedFormsReviewEnabled());
				}
				
			} else if (directory.toLowerCase().indexOf(DESKTOP)>=0) {
				// This is Mobile SPA
				setReviewCategoryEnabled(isDesktopWebReviewEnabled());
			} else {
				// By default Review Category is enabled
				setReviewCategoryEnabled(true);
			}
		}
		
		//System.out.println("CHECKCHECK : , " + directory + ", " + this.getClass().getName() +  ", " + isReviewCategoryEnabled);
	}

	public boolean isEnabledForCurrentFormCategory() {
		return (super.isEnabled() && isReviewCategoryEnabled) ;
	}	
}
