package com.kony.tag.arabiclayout.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.ProjectConstants;

public class ArWidgetAlignmentReversalRule extends ArAlignmentBaseRule implements ProjectConstants{
	private ArWidgetAlignmentReversalRule() {
		
	}

	public ArWidgetAlignmentReversalRule(String formQualifier, TreeSet<String> blackListedWidgetList, ProjectConfig projectConfig, String buildChannel) {
		super(formQualifier,blackListedWidgetList,projectConfig,buildChannel);
	}
	
	private static final String WIDGET_ALIGN_TOP_LEFT = "constants.WIDGET_ALIGN_TOP_LEFT";
	private static final String WIDGET_ALIGN_TOP_RIGHT = "constants.WIDGET_ALIGN_TOP_RIGHT";
	private static final String WIDGET_ALIGN_MIDDLE_LEFT = "constants.WIDGET_ALIGN_MIDDLE_LEFT";
	private static final String WIDGET_ALIGN_MIDDLE_RIGHT = "constants.WIDGET_ALIGN_MIDDLE_RIGHT";
	private static final String WIDGET_ALIGN_BOTTOM_LEFT = "constants.WIDGET_ALIGN_BOTTOM_LEFT";
	private static final String WIDGET_ALIGN_BOTTOM_RIGHT = "constants.WIDGET_ALIGN_BOTTOM_RIGHT";
	
	public List<String> reverseWidgetAlignment(List<String> formattedLines) {
		List<String> newLines = new ArrayList<String>();
		int index1 = 0;
		int index2=0;
		
		for (String line : formattedLines) {
	    	// Always call this at the beginning
	    	updateCurrentWidgetName(line);
	    	// Actual Code Starts
			
			index1 = line.indexOf(KEYWORD_WIDGET_ALIGNMENT);
			index2 = line.indexOf(CHAR_COLON);
			
			if (index1>=0 && index2>index1) {
				if(line.indexOf(WIDGET_ALIGN_TOP_LEFT)>=0 && !isBlackListedWidget()) {
					line = line.replaceAll(WIDGET_ALIGN_TOP_LEFT, WIDGET_ALIGN_TOP_RIGHT);
				} else if(line.indexOf(WIDGET_ALIGN_TOP_RIGHT)>=0 && !isBlackListedWidget()) {
					line = line.replaceAll(WIDGET_ALIGN_TOP_RIGHT, WIDGET_ALIGN_TOP_LEFT);
				} else if(line.indexOf(WIDGET_ALIGN_MIDDLE_LEFT)>=0 && !isBlackListedWidget()) {
					line = line.replaceAll(WIDGET_ALIGN_MIDDLE_LEFT, WIDGET_ALIGN_MIDDLE_RIGHT);
				} else if(line.indexOf(WIDGET_ALIGN_MIDDLE_RIGHT)>=0 && !isBlackListedWidget()) {
					line = line.replaceAll(WIDGET_ALIGN_MIDDLE_RIGHT, WIDGET_ALIGN_MIDDLE_LEFT);
				} else if(line.indexOf(WIDGET_ALIGN_BOTTOM_LEFT)>=0 && !isBlackListedWidget()) {
					line = line.replaceAll(WIDGET_ALIGN_BOTTOM_LEFT, WIDGET_ALIGN_BOTTOM_RIGHT);
				} else if(line.indexOf(WIDGET_ALIGN_BOTTOM_RIGHT)>=0 && !isBlackListedWidget()) {
					line = line.replaceAll(WIDGET_ALIGN_BOTTOM_RIGHT, WIDGET_ALIGN_BOTTOM_LEFT);
				}
			}
			
			newLines.add(line);
		}
		return newLines;
	}
	}
	
