package com.kony.tag.arabiclayout.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.ProjectConstants;
import com.kony.tag.util.eclipse.JSReviewUtil;

public class ArScrollConfigReversalRule extends ArAlignmentBaseRule implements ProjectConstants{
	private ArScrollConfigReversalRule() {
		
	}

	public ArScrollConfigReversalRule(String formQualifier, TreeSet<String> blackListedWidgetList, ProjectConfig projectConfig, String buildChannel) {
		super(formQualifier,blackListedWidgetList,projectConfig,buildChannel);
	}

	public static final String _COMMA = ",";
	public static final String _SQR_BRACKET_OPEN = "[";
	public static final String _SQR_BRACKET_CLOSE = "]";
	
	public List<String> reverseScrollConfig(List<String> formattedLines) {
		List<String> newLines = new ArrayList<String>();
		int index1 = 0;
		int index2 = 0;
		String arrowConfigStr = null;
		String reverseArrowConfigStr = null;
		int startIndex = 0;
		int endIndex = 0;
		
		for (String line : formattedLines) {
	    	// Always call this at the beginning
	    	updateCurrentWidgetName(line);
	    	// Actual Code Starts

			index1 = line.indexOf(KEYWORD_SCROLL_ARROW_CONFIG);
			index2 = line.indexOf(CHAR_COLON);
			
			if (index1>=0 && index2>index1) {
				startIndex = line.indexOf(_SQR_BRACKET_OPEN,index1);
				endIndex = line.indexOf(_SQR_BRACKET_CLOSE,index1);
					if (startIndex>index2 && endIndex>(startIndex+1)) {
						arrowConfigStr = line.substring(startIndex+1,endIndex).trim();
						reverseArrowConfigStr = getReverseArrowConfigStr(arrowConfigStr);
						if (!isBlackListedWidget()) {
							line = line.replaceAll(arrowConfigStr, reverseArrowConfigStr);
						}
					}
			}
			
			newLines.add(line);
		}
		
		return newLines;
		
	}
	
private String getReverseArrowConfigStr(String configStr) {
	StringBuffer reverseValues = new StringBuffer();
	String[] configValues = configStr.split(_COMMA);
	
	if (configValues.length != 4) {
		JSReviewUtil.printDetailedLog("Strange Arrow Config : " + configStr);
		return null;
	}
	
	String[] reverseConfigValues = {configValues[2],configValues[1],configValues[0],configValues[3]}; 
		
	for (int i=0; i<reverseConfigValues.length; i++) {
		reverseValues.append(reverseConfigValues[i]);
		if (i<reverseConfigValues.length-1) {
			reverseValues.append(_COMMA);
		}
	}
	return reverseValues.toString();
}


}
