package com.kony.tag.arabiclayout.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.ProjectConstants;

public class ArFormNameReplaceRule extends ArAlignmentBaseRule implements ProjectConstants{
	
	private ArFormNameReplaceRule() {
		
	}
	
	public ArFormNameReplaceRule(String formQualifier, TreeSet<String> blackListedWidgetList, ProjectConfig projectConfig, String buildChannel) {
		super(formQualifier,blackListedWidgetList,projectConfig,buildChannel);
	}
	

	private List<String> _matchingFunctions = new ArrayList<String>();

	public List<String> replaceFormName(List<String> formattedLines) {
		List<String> newLines = new ArrayList<String>();
		String formName = null;
		int index1 = -1;
		int index2 = -1;
		int index3 = -1;
		int index4 = -1;
		int index5 = -1;
		
		for(String line : formattedLines) {
        	// Always call this at the beginning
        	// updateCurrentWidgetName(line); -- NOT NEEDED FOR THIS RULE. EXCLUDE FLAG NOT APPLICABLE
        	// Actual Code Starts
			
			index1 = line.indexOf(KEYWORD_KONY_UI_FORM);
			index4 = line.indexOf(KEYWORD_ADD);
			
			if (index1<0) {
				index1 = line.indexOf(KEYWORD_KONY_UI_POPUP);
			}

			if (index1>0) {
				index2 = line.indexOf(CHAR_EQUALS);
				index3 = line.indexOf(KEYWORD_VAR);
	
				if (index2>0 && index1>index2 && index3<0) {
					formName = line.substring(0,index2).trim();
					ArLayoutManager.addForm(formName);
					line = line.replaceFirst(formName, formName+AR_SUFFIX);
				}
			}else if (index4>=0) {
				index5 = line.indexOf(getFormQualifier());
				
				if(line.matches("addWidgets"+getFormQualifier())){
					if (index5>=0 && index4>index5) {
						line = line.replaceFirst(getFormQualifier(), getFormQualifier()+AR_SUFFIX);
					}
				}
				
				
			}
			
			newLines.add(line);
			
		}
		
		return newLines;
	}
	
	private String getMatchingFunctionName(String line, List<String> functions) {
		String matchingFunction = null;
		_matchingFunctions.clear();
		int functionNameLength = 0;
		
		for(String functionName : functions) {
			if (line.indexOf(functionName) >=0) {
				_matchingFunctions.add(functionName); 
			}
		}
		
		// This is need to check if one function name is substring of another function name
		if (_matchingFunctions.size()==0) {
			return null;
		} else if (_matchingFunctions.size()==1) {
			return _matchingFunctions.get(0);
		} else {
			matchingFunction = _matchingFunctions.get(0);
			functionNameLength = matchingFunction.length();

			for (String fnName : _matchingFunctions) {
				if (fnName.length()>functionNameLength) {
					matchingFunction = fnName;
					functionNameLength = matchingFunction.length();
				}
			}
			
			return matchingFunction;
		}
	}
}
