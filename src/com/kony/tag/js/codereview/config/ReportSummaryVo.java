package com.kony.tag.js.codereview.config;

import com.kony.tag.config.ProjectConstants;

public class ReportSummaryVo implements Comparable<ReportSummaryVo>, ProjectConstants {

	private String ruleId;
	private String ruleDesc;
	private boolean isRuleEnabled;
	private String commentCategory;
	private int jsCommentsCount;
	private int mobileAppcommentsCount;
	private int forkedMobileAppCommentsCount; 
	private int tabletAppCommentsCount;
	private int forkedTabletAppCommentsCount;
	private int desktopAppCommentsCount;
	private int miscCommentsCount;
	
	public String getRuleId() {
		return ruleId;
	}

	public String getRuleDesc() {
		return ruleDesc;
	}

	public boolean isRuleEnabled() {
		return isRuleEnabled;
	}
	
	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	public void setRuleDesc(String ruleDesc) {
		this.ruleDesc = ruleDesc;
	}

	public void setRuleEnabled(boolean isRuleEnabled) {
		this.isRuleEnabled = isRuleEnabled;
	}
	

	public String getCommentCategory() {
		return commentCategory;
	}

	public int getJsCommentsCount() {
		return jsCommentsCount;
	}

	public int getMobileAppcommentsCount() {
		return mobileAppcommentsCount;
	}

	public int getForkedMobileAppCommentsCount() {
		return forkedMobileAppCommentsCount;
	}

	public int getTabletAppCommentsCount() {
		return tabletAppCommentsCount;
	}

	public int getForkedTabletAppCommentsCount() {
		return forkedTabletAppCommentsCount;
	}

	public int getDesktopAppCommentsCount() {
		return desktopAppCommentsCount;
	}

	public int getMiscCommentsCount() {
		return miscCommentsCount;
	}

	private ReportSummaryVo() {
		// Do not use this
	}
	
	public ReportSummaryVo(String ruleId, String ruleDesc, boolean isRuleEnabled) {
		this.ruleId = ruleId;
		this.ruleDesc = ruleDesc;
		this.isRuleEnabled = isRuleEnabled;
		
		this.jsCommentsCount = 0;
		this.mobileAppcommentsCount = 0;
		this.forkedMobileAppCommentsCount = 0;
		this.tabletAppCommentsCount = 0;
		this.forkedTabletAppCommentsCount = 0;
		this.desktopAppCommentsCount = 0;
		this.miscCommentsCount = 0;
	}
	
	@Override
	public boolean equals(Object obj) {
		ReportSummaryVo reportSummaryVo = null;
		
		if (obj == null) {
			return false;
		}
		
		if (obj instanceof ReportSummaryVo) {
			reportSummaryVo = (ReportSummaryVo) obj;
		} else {
			return false;
		}
		
		if ((this.ruleId != null && reportSummaryVo.ruleId != null && this.ruleId.equals(reportSummaryVo.ruleId))
				&& (this.ruleDesc != null && reportSummaryVo.ruleDesc != null && this.ruleDesc.equals(reportSummaryVo.ruleDesc))) {
			return true;
		} else {
			return false;
		}
	}
	
	public void updateCommentCount(String commentCategory) {
		
		this.commentCategory = commentCategory;
		
		if (commentCategory.equals(COMMENT_CATEGORY_JS)) {
			this.jsCommentsCount++;
		} else if (commentCategory.equals(COMMENT_CATEGORY_MOBILE_APP)) {
			this.mobileAppcommentsCount++;
		} else if (commentCategory.equals(COMMENT_CATEGORY_FORKED_MOBILE_APP )) {
			this.forkedMobileAppCommentsCount++;
		} else if (commentCategory.equals(COMMENT_CATEGORY_TABLET_APP)) {
			this.tabletAppCommentsCount++;
		} else if (commentCategory.equals(COMMENT_CATEGORY_FORKED_TABLET_APP )) {
			this.forkedTabletAppCommentsCount++;
		} else if (commentCategory.equals(COMMENT_CATEGORY_DESKTOP_APP)) {
			this.desktopAppCommentsCount++;
		} else {
			this.miscCommentsCount++;
		}
	}
	
	public int getCommentCount(String commentCategory) {
			int count = 0;
			
			if (commentCategory != null && commentCategory.trim().length()>0) {
				if (commentCategory.equals(COMMENT_CATEGORY_JS)) {
					count = this.jsCommentsCount;
				} else if (commentCategory.equals(COMMENT_CATEGORY_MOBILE_APP)) {
					count = this.mobileAppcommentsCount;
				} else if (commentCategory.equals(COMMENT_CATEGORY_FORKED_MOBILE_APP )) {
					count = this.forkedMobileAppCommentsCount;
				} else if (commentCategory.equals(COMMENT_CATEGORY_TABLET_APP)) {
					count = this.tabletAppCommentsCount;
				} else if (commentCategory.equals(COMMENT_CATEGORY_FORKED_TABLET_APP )) {
					count = this.forkedTabletAppCommentsCount;
				} else if (commentCategory.equals(COMMENT_CATEGORY_DESKTOP_APP)) {
					count = this.desktopAppCommentsCount;
				} else {
					count = this.miscCommentsCount;
				}		
			}
		
			return count;
	}
	
	@Override
	public int compareTo(ReportSummaryVo summaryVo) {
		int compareIndex = 0;
		
		if (null == summaryVo || this.getRuleId()==null || summaryVo.getRuleId()==null) {
			return 0;
		}
		
		compareIndex = this.getRuleId().compareTo(summaryVo.getRuleId());
		
		return compareIndex;
	}	
}
