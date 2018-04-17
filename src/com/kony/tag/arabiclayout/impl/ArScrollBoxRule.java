package com.kony.tag.arabiclayout.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.ProjectConstants;

public class ArScrollBoxRule  extends ArAlignmentBaseRule implements ProjectConstants {
	private ArScrollBoxRule() {
		
	}

	public ArScrollBoxRule(String formQualifier, TreeSet<String> blackListedWidgetList, ProjectConfig projectConfig, String buildChannel) {
		super(formQualifier,blackListedWidgetList,projectConfig,buildChannel);
	}
	
	private static final String SCROLL_END = ".scrollToEnd";
	private static final String SCROLL_BEGIN = ".scrollToBeginning";
	
	// This is temporary - needs more testing !!
	public List<String> applyScrollBoxRules(List<String> formattedLines) {
		List<String> newLines = new ArrayList<String>();
		int indexScrollEnd = -1;
		int indexScrollBegin = -1;
		

		for(String line : formattedLines) {
	    	// Always call this at the beginning
	    	updateCurrentWidgetName(line);
	    	// Actual Code Starts

			indexScrollEnd = line.indexOf(SCROLL_END);
			indexScrollBegin = line.indexOf(SCROLL_BEGIN);
			
			if (indexScrollEnd >=0 && !isBlackListedWidget()) {
				line = line.replaceFirst(SCROLL_END, SCROLL_BEGIN);
			}else if (indexScrollBegin >=0 && !isBlackListedWidget()) {
				line = line.replaceFirst(SCROLL_BEGIN, SCROLL_END);
			}
			
			newLines.add(line);
		}
		
		return newLines;
	}
}
