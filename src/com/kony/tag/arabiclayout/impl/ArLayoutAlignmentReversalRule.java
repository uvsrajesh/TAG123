package com.kony.tag.arabiclayout.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.ProjectConstants;

public class ArLayoutAlignmentReversalRule extends ArAlignmentBaseRule implements ProjectConstants{
	private ArLayoutAlignmentReversalRule() {
		
	}

	public ArLayoutAlignmentReversalRule(String formQualifier, TreeSet<String> blackListedWidgetList, ProjectConfig projectConfig, String buildChannel) {
		super(formQualifier,blackListedWidgetList,projectConfig,buildChannel);
	}
	
	private static final String LAYOUT_ALIGN_FROM_RIGHT = "constants.BOX_LAYOUT_ALIGN_FROM_RIGHT";
	private static final String LAYOUT_ALIGN_FROM_LEFT = "constants.BOX_LAYOUT_ALIGN_FROM_LEFT";
	
	public List<String> reverseLayoutAlignment(List<String> formattedLines) {
		List<String> newLines = new ArrayList<String>();
		int index1 = 0;
		int index2=0;
		
		for (String line : formattedLines) {
	    	// Always call this at the beginning
	    	updateCurrentWidgetName(line);
	    	// Actual Code Starts
			
			index1 = line.indexOf(KEYWORD_LAYOUT_ALIGNMENT);
			index2 = line.indexOf(CHAR_COLON);
			
			if (index1>=0 && index2>index1) {
				if(line.indexOf(LAYOUT_ALIGN_FROM_RIGHT)>=0 && !isBlackListedWidget()) {
					line = line.replaceAll(LAYOUT_ALIGN_FROM_RIGHT, LAYOUT_ALIGN_FROM_LEFT);
				} else if(line.indexOf(LAYOUT_ALIGN_FROM_LEFT)>=0 && !isBlackListedWidget()) {
					line = line.replaceAll(LAYOUT_ALIGN_FROM_LEFT, LAYOUT_ALIGN_FROM_RIGHT);
				} 
			}
			
			newLines.add(line);
		}
		return newLines;
	}
	}
	
