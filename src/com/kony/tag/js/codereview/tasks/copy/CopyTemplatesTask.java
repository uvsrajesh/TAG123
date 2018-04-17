package com.kony.tag.js.codereview.tasks.copy;

import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.ProjectConstants;
import com.kony.tag.config.TagToolException;
import com.kony.tag.js.codereview.util.ReviewUtil;


public class CopyTemplatesTask extends CopyMasterTask implements ProjectConstants{

	public CopyTemplatesTask(ProjectConfig codeReviewConfig) {
		super(codeReviewConfig);
	}
	
	public void execute() throws TagToolException {
		ReviewUtil reviewUtil = new ReviewUtil();
		
		// Copy Headers
		reviewUtil.copyAllFiles(
				(getProjectConfig().getProjectPath() + LOCATION_MOBILE_HEADERS) ,
				(getProjectConfig().getCodeBackupPath() + LOCATION_MOBILE_HEADERS)
				);
		
		reviewUtil.copyAllFiles(
				(getProjectConfig().getProjectPath() + LOCATION_TABLET_HEADERS) ,
				(getProjectConfig().getCodeBackupPath() + LOCATION_TABLET_HEADERS)
				);
		
		reviewUtil.copyAllFiles(
				(getProjectConfig().getProjectPath() + LOCATION_DESKTOP_HEADERS) ,
				(getProjectConfig().getCodeBackupPath() + LOCATION_DESKTOP_HEADERS)
				);
		
		// Copy Footers
		reviewUtil.copyAllFiles(
				(getProjectConfig().getProjectPath() + LOCATION_MOBILE_FOOTERS) ,
				(getProjectConfig().getCodeBackupPath() + LOCATION_MOBILE_FOOTERS)
				);
		
		reviewUtil.copyAllFiles(
				(getProjectConfig().getProjectPath() + LOCATION_TABLET_FOOTERS) ,
				(getProjectConfig().getCodeBackupPath() + LOCATION_TABLET_FOOTERS)
				);
		
		reviewUtil.copyAllFiles(
				(getProjectConfig().getProjectPath() + LOCATION_DESKTOP_FOOTERS) ,
				(getProjectConfig().getCodeBackupPath() + LOCATION_DESKTOP_FOOTERS)
				);
		
		// Copy Map Templates
		reviewUtil.copyAllFiles(
				(getProjectConfig().getProjectPath() + LOCATION_MOBILE_TEMPLATE_MAPS) ,
				(getProjectConfig().getCodeBackupPath() + LOCATION_MOBILE_TEMPLATE_MAPS)
				);
		
		reviewUtil.copyAllFiles(
				(getProjectConfig().getProjectPath() + LOCATION_TABLET_TEMPLATE_MAPS) ,
				(getProjectConfig().getCodeBackupPath() + LOCATION_TABLET_TEMPLATE_MAPS)
				);
		
		reviewUtil.copyAllFiles(
				(getProjectConfig().getProjectPath() + LOCATION_DESKTOP_TEMPLATE_MAPS) ,
				(getProjectConfig().getCodeBackupPath() + LOCATION_DESKTOP_TEMPLATE_MAPS)
				);
		
		// Copy Segment Templates
		reviewUtil.copyAllFiles(
				(getProjectConfig().getProjectPath() + LOCATION_MOBILE_TEMPLATE_SEGMENTS) ,
				(getProjectConfig().getCodeBackupPath() + LOCATION_MOBILE_TEMPLATE_SEGMENTS)
				);
		
		reviewUtil.copyAllFiles(
				(getProjectConfig().getProjectPath() + LOCATION_TABLET_TEMPLATE_SEGMENTS) ,
				(getProjectConfig().getCodeBackupPath() + LOCATION_TABLET_TEMPLATE_SEGMENTS)
				);
		
		reviewUtil.copyAllFiles(
				(getProjectConfig().getProjectPath() + LOCATION_DESKTOP_TEMPLATE_SEGMENTS) ,
				(getProjectConfig().getCodeBackupPath() + LOCATION_DESKTOP_TEMPLATE_SEGMENTS)
				);
		
		// Copy Desktop Specific Templates
		reviewUtil.copyAllFiles(
				(getProjectConfig().getProjectPath() + LOCATION_DESKTOP_TEMPLATE_GRIDS) ,
				(getProjectConfig().getCodeBackupPath() + LOCATION_DESKTOP_TEMPLATE_GRIDS)
				);
		reviewUtil.copyAllFiles(
				(getProjectConfig().getProjectPath() + LOCATION_DESKTOP_TEMPLATE_TABHEADERS) ,
				(getProjectConfig().getCodeBackupPath() + LOCATION_DESKTOP_TEMPLATE_TABHEADERS)
				);		
	}
}