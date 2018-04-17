package com.kony.tag.arabiclayout.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.ProjectConstants;
import com.kony.tag.util.eclipse.JSReviewUtil;

public class ArMarginPaddingReversalRule extends ArAlignmentBaseRule implements ProjectConstants{
	private ArMarginPaddingReversalRule() {
		
	}

	public ArMarginPaddingReversalRule(String formQualifier, TreeSet<String> blackListedWidgetList, ProjectConfig projectConfig, String buildChannel) {
		super(formQualifier,blackListedWidgetList,projectConfig,buildChannel);
	}
	
	public static final String _COMMA = ",";
	public static final String _PARANTHESIS_OPEN = "(";
	public static final String _PARANTHESIS_CLOSE = ")";
	public static final String _SQR_BRACKET_OPEN = "[";
	public static final String _SQR_BRACKET_CLOSE = "]";
	
	public List<String> reverseMarginsPaddings(List<String> formattedLines) {
		List<String> newLines = new ArrayList<String>();
		int index1 = 0;
		int index2 = 0;
		String containerName = null;
		String marginPaddingStr = null;
		String reverseMarginPaddingStr = null;
		int startIndex = 0;
		int endIndex = 0;
		String preConfiguredMarginPaddingStr = null;
		
		for (String line : formattedLines) {
        	// Always call this at the beginning
        	updateCurrentWidgetName(line);
        	// Actual Code Starts
			
        	preConfiguredMarginPaddingStr = null;
        	
			index1 = line.indexOf(KEYWORD_MARGIN);
			if (index1 <0) {
				index1 = line.indexOf(KEYWORD_PADDING);
				if (index1>=0) {
					preConfiguredMarginPaddingStr = getPreconfiguredPadding();
				}
			} else {
				preConfiguredMarginPaddingStr = getPreconfiguredMargin();
			}
			
			index2 = line.indexOf(CHAR_COLON);
			
			if (index1>=0 && index2>index1) {
				startIndex = line.indexOf(_SQR_BRACKET_OPEN,index1);
				endIndex = line.indexOf(_SQR_BRACKET_CLOSE,index1);
					if (startIndex>index2 && endIndex>(startIndex+1)) {
						
						// Is user specified specific margin padding for Arabic - honor it !
						marginPaddingStr = line.substring(startIndex+1,endIndex).trim();
						if (preConfiguredMarginPaddingStr != null && preConfiguredMarginPaddingStr.trim().length()>0) {
							JSReviewUtil.printDetailedLog("Using Pre configured Margin/Padding : " + preConfiguredMarginPaddingStr);
							reverseMarginPaddingStr = preConfiguredMarginPaddingStr;
						} else {
							reverseMarginPaddingStr = getReverseMarginPaddingStr(marginPaddingStr);
						}
						
						if (!isBlackListedWidget()) {
							line = line.replaceAll(marginPaddingStr, reverseMarginPaddingStr);
						}
					}
			}
			
			newLines.add(line);
		}
		
		return newLines;
		
	}
	
private String getReverseMarginPaddingStr(String marginPaddingStr) {
	StringBuffer reverseValues = new StringBuffer();
	String[] marginPaddingValues = marginPaddingStr.split(_COMMA);
	
	if (marginPaddingValues.length != 4) {
		JSReviewUtil.printDetailedLog("Strange Margin Padding Input : " + marginPaddingStr);
		return null;
	}
	
	String[] reverseMarginPaddingValues = {marginPaddingValues[2],marginPaddingValues[1],marginPaddingValues[0],marginPaddingValues[3]}; 
		
	for (int i=0; i<reverseMarginPaddingValues.length; i++) {
		reverseValues.append(reverseMarginPaddingValues[i]);
		if (i<reverseMarginPaddingValues.length-1) {
			reverseValues.append(_COMMA);
		}
	}
	return reverseValues.toString();
}


}
