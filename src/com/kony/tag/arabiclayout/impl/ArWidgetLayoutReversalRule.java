package com.kony.tag.arabiclayout.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.ProjectConstants;

public class ArWidgetLayoutReversalRule extends ArAlignmentBaseRule implements ProjectConstants{
	private ArWidgetLayoutReversalRule() {
		
	}

	public ArWidgetLayoutReversalRule(String formQualifier, TreeSet<String> blackListedWidgetList, ProjectConfig projectConfig, String buildChannel) {
		super(formQualifier,blackListedWidgetList,projectConfig,buildChannel);
	}

	public static final String _COMMA = ",";
	public static final String _PARANTHESIS_OPEN = "(";
	public static final String _PARANTHESIS_CLOSE = ")";
	
	public List<String> reverseWidgetLayout(List<String> formattedLines, List<String>horizontalConatiners) {
		List<String> newLines = new ArrayList<String>();
		int index1 = 0;
		String containerName = null;
		String widgetsStr = null;
		String reverseWidgetsStr = null;
		int startIndex = 0;
		int endIndex = 0;
		
		for (String line : formattedLines) {
        	// Always call this at the beginning
        	updateCurrentWidgetName(line);
        	// Actual Code Starts
			
			
			index1 = line.indexOf(KEYWORD_ADD);
			
			if (index1>0) {
				containerName = line.substring(0,index1).trim();
				if (horizontalConatiners.contains(containerName)) {
					startIndex = line.indexOf(_PARANTHESIS_OPEN,index1);
					endIndex = line.indexOf(_PARANTHESIS_CLOSE,index1);
					if (startIndex>index1 && endIndex>(startIndex+1)) {
						widgetsStr = line.substring(startIndex+1,endIndex).trim();
						reverseWidgetsStr = getReverseWidgetStr(widgetsStr);
						
						if (!isBlackListedWidget()) {
							line = line.replaceAll(widgetsStr, reverseWidgetsStr);
						}
					}
				}
			}
			
			newLines.add(line);
		}
		
		return newLines;
		
	}
	
private String getReverseWidgetStr(String widgetStr) {
	StringBuffer reverseWidgets = new StringBuffer();
	String[] widgets = widgetStr.split(_COMMA);
	
	for (int i=(widgets.length-1);i>=0;i--) {
		reverseWidgets.append(widgets[i]);
		if (i>0) {
			reverseWidgets.append(_COMMA);
		}
	}
	return reverseWidgets.toString();
}


}