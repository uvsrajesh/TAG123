package com.kony.tag.arabiclayout.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.ProjectConstants;

public class ArFunctionReplaceRule extends ArAlignmentBaseRule implements ProjectConstants{

	private List<String> _matchingFunctions = new ArrayList<String>();
	
	private ArFunctionReplaceRule() {
		
	}
	
	public ArFunctionReplaceRule(String formQualifier, TreeSet<String> blackListedWidgetList, ProjectConfig projectConfig, String buildChannel) {
		super(formQualifier,blackListedWidgetList,projectConfig,buildChannel);
	}

	public List<String> replaceFunctionNames(List<String> formattedLines,List<String> functions) {
        List<String> newLines = new ArrayList<String>();
        String matchingFunctionName = null;
        int index1 = -1;
        int index2 = -1;
        int index3 = -1;
        int index4 = -1;
        
        for(String line : formattedLines) {
        	// Always call this at the beginning
        	// updateCurrentWidgetName(line); -- NOT NEEDED FOR THIS RULE. EXCLUDE FLAG NOT APPLICABLE
        	// Actual Code Starts
        		
              index1 = line.indexOf(FUNCTION);
              index2 = line.indexOf(COLON);
              index4 = line.indexOf(KEYWORD_FUNCTION_CALL);//Execute this only for spa channel.
              
              if (index1>=0) {
                    matchingFunctionName = getMatchingFunctionName(line, functions);
                    if (matchingFunctionName !=null) {
                   		  line = line.replaceAll(matchingFunctionName, matchingFunctionName+AR_SUFFIX);
                    }
              } else if (index2>=0) {
                    // REplace only if it is a genuine function call back specified after a colon
                    matchingFunctionName = getMatchingFunctionName(line, functions);
                    if (matchingFunctionName !=null) {
                          index3 = line.indexOf(matchingFunctionName);
                          if (index3>index2 && (
                                      ((index2+1) == index3) || (line.substring((index2+1),index3).trim().length()==0))
                                ) {
                                      line = line.replaceAll(matchingFunctionName, matchingFunctionName+AR_SUFFIX);
                          }
                    }
              } else if(index4 >= 0){
            	  // SPA Fix
                    matchingFunctionName = getMatchingFunctionName(line, functions);
                    if (matchingFunctionName !=null && index4 > line.indexOf(matchingFunctionName)) {
                          line = line.replaceAll(matchingFunctionName, matchingFunctionName+AR_SUFFIX);
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
