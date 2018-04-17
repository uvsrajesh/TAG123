package com.kony.tag.js.codereview.config;

import com.kony.tag.config.ProjectConstants;

public class ReviewComment implements Comparable<ReviewComment>, ProjectConstants{
	private final String BLANK = ""; 
	private int lineNumber;
	private int columnNumber;
	private String reviewComment;
	private String severity;
	private String fileName;
	private String ruleId;
	private boolean isWarning;
	private String referenceData;
	private String fileLocation;
	private String shortDescription;
	
	public ReviewComment(
		String reviewComment,
		String shortDescription,
		String referenceData,
		String fileName,
		String fileLocation,
		int lineNumber,
		String severity,
		String ruleId,
		boolean isWarning) {

		if (null != reviewComment) {
			this.reviewComment = reviewComment;
		} else {
			this.reviewComment = BLANK;
		}
		
		if (null != shortDescription) {
			this.shortDescription = shortDescription;
		} else {
			this.shortDescription = BLANK;
		}
		
		
		if (null != referenceData) {
			this.referenceData = referenceData;
		} else {
			this.referenceData = BLANK;
		}
		

		this.lineNumber = lineNumber;
		
		if (null != fileName) {
			this.fileName = fileName;
		} else {
			this.fileName = BLANK;
		}
		
		if (null != fileLocation) {
			this.fileLocation = fileLocation;
		} else {
			this.fileLocation = BLANK;
		}
		
		if (null != severity) {
			this.severity = severity;
		} else {
			this.severity = BLANK;
		}

		if (null != ruleId) {
			this.ruleId = ruleId;
		} else {
			this.ruleId = BLANK;
		}
		
		this.isWarning = isWarning;
	}
	
	@Override
	public boolean equals(Object obj) {

		ReviewComment comment = null;
		boolean isEqual = false;
		
		if (obj instanceof ReviewComment) {
			comment = (ReviewComment) obj;
		} else {
			return isEqual;
		}
		
		if (comment.getFileLocation().equals(this.fileLocation) &&
				comment.getFileName().equals(this.fileName) &&
				comment.getRuleId().equals(this.ruleId) &&
				comment.getLineNumber() == this.lineNumber &&
				comment.getReviewComment().equals(this.reviewComment) &&
				comment.getReferenceData().equals(this.referenceData) &&
				comment.getShortDescripton().equals(this.shortDescription)
				) {
			isEqual = true;
		}
		
		
		return isEqual;
	}
	
	public String getShortDescripton() {
		return shortDescription;
	}

	public void setShortDescripton(String shortDescripton) {
		this.shortDescription = shortDescripton;
	}
	
	public String getRuleId() {
		return ruleId;
	}
	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}
	
	public int getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	public int getColumnNumber() {
		return columnNumber;
	}
	public void setColumnNumber(int columnNumber) {
		this.columnNumber = columnNumber;
	}
	public String getReviewComment() {
		return reviewComment;
	}
	public void setReviewComment(String reviewComment) {
		this.reviewComment = reviewComment;
	}
	public String getSeverity() {
		return severity;
	}
	public void setSeverity(String severity) {
		this.severity = severity;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public boolean isWarning() {
		return isWarning;
	}

	public void setWarning(boolean isWarning) {
		this.isWarning = isWarning;
	}
	public String getReferenceData() {
		return referenceData;
	}

	public void setReferenceData(String referenceData) {
		this.referenceData = referenceData;
	}
	
	public String getFileLocation() {
		return fileLocation;
	}

	public void setFileLocation(String fileDirectory) {
		this.fileLocation = fileDirectory;
	}

	@Override
	public int compareTo(ReviewComment comment) {
		int compareIndex = 0;
		
		if (null == comment || this.getRuleId()==null || comment.getRuleId()==null) {
			return 1;
		}
		
		compareIndex = this.getRuleId().compareTo(comment.getRuleId());
		
		return compareIndex;
	}
	
	public String fetchCommentType() {
		if (isWarning) {
			return WARNING;
		} else {
			return ERROR;
		}
	}	
}
