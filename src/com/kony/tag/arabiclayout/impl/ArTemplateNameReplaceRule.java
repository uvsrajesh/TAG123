package com.kony.tag.arabiclayout.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.ProjectConstants;

public class ArTemplateNameReplaceRule extends ArAlignmentBaseRule implements ProjectConstants{
	
	private ArTemplateNameReplaceRule() {
		
	}

	public ArTemplateNameReplaceRule(String formQualifier, TreeSet<String> blackListedWidgetList, ProjectConfig projectConfig, String buildChannel) {
		super(formQualifier,blackListedWidgetList,projectConfig,buildChannel);
	}

	private List<String> _matchingFunctions = new ArrayList<String>();

	public List<String> replaceTemplateNames(List<String> formattedLines) {
		List<String> newLines = new ArrayList<String>();
		List<String> finalLines = new ArrayList<String>();
		int index0 = -1;
		int index1 = -1;
		int index2 = -1;
		int index3 = -1;
		
		int index4 = -1;
		int index5=-1;
		
		List<String> templateNames = new ArrayList<String>();
		String templateName = null;
		
		for(String line : formattedLines) {
        	// Always call this at the beginning
        	// updateCurrentWidgetName(line); -- NOT NEEDED FOR THIS RULE. EXCLUDE FLAG NOT APPLICABLE
        	// Actual Code Starts

			index0 = line.indexOf(KEYWORD_KONY_UI_BOX_TEMPLATE);
			index1 = line.indexOf(KEYWORD_KONY_UI_BOX);
			
			if (index0>0 || index1>0 ) {
				index2 = line.indexOf(CHAR_EQUALS);
				index3 = line.indexOf(KEYWORD_VAR);
	
				if (index2>0 && (index1>index2 || index0>index2) && index3<0) {
					templateName = line.substring(0,index2).trim();
					templateNames.add(templateName);
						line = line.replaceFirst(templateName, templateName+AR_SUFFIX);
					}
				}
			 
			
			newLines.add(line);
	}
	
		for(String line : newLines) {
			index4 = line.indexOf(KEYWORD_ADD);
			for (String name : templateNames) {
				index5 = line.indexOf(name);
				if (index5>=0 && index4>index5 && index4 == (name.length() + index5)) {
					line = line.replaceFirst(name, name+AR_SUFFIX);
				}
			}
			finalLines.add(line);
		}
		/*
		else if (index4>=0) {
			index5 = line.indexOf(templateName);
			if (index5>=0 && index4>index5) {
				line = line.replaceFirst(templateName, templateName+AR_SUFFIX);
			}
		}
		*/
		return finalLines;
	}
}
