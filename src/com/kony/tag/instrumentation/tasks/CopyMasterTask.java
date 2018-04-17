package com.kony.tag.instrumentation.tasks;

import com.kony.tag.config.CodeReviewStatus;
import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.TagToolException;
import com.kony.tag.tasks.base.CodeReviewMasterTask;

public abstract class CopyMasterTask extends CodeReviewMasterTask {

	private CopyMasterTask() {
		// Do not use
		super(null);
	}

	public CopyMasterTask(ProjectConfig codeReviewConfig) {
		super(codeReviewConfig);
	}
	
	public final void run() {
		try {
			
			if (null != getProjectConfig()) {
				execute();
			} else {
				CodeReviewStatus.getInstance().addErrorMessage(
						"Code Review Configuration not initialized : " + this.getClass().getName());
			}
		} catch (TagToolException cdrExcp) {
			CodeReviewStatus.getInstance().addErrorMessage(cdrExcp, " Error copying files for code review :" + cdrExcp.getErrorMessage());
		} catch (Exception excp) {
			CodeReviewStatus.getInstance().addErrorMessage(excp, " Error copying files for code review :" + excp);
		}
	}
	
	public abstract void execute() throws TagToolException;
}
