package com.kony.tag.js.codereview.tasks.jsreview.impl;

import java.util.ArrayList;
import java.util.List;

import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.TagToolException;
import com.kony.tag.js.codereview.config.FunctionVo;
import com.kony.tag.js.codereview.config.ReviewComment;
import com.kony.tag.js.codereview.tasks.formreview.impl.UnusedItemsRule;
import com.kony.tag.js.codereview.util.ReviewPropertiesUtil;
import com.kony.tag.tasks.JsReviewMasterTask;

public class UnusedFunctionsRule extends JsReviewMasterTask {
	
	public static final String PROPS_RULE_NAME = UnusedItemsRule.PROPS_RULE_NAME_UNUSED_FUNCTIONS;
	public static final String RULE_ID = UnusedItemsRule.RULE_ID_UNUSED_FUNCTIONS;
	
	//public static final String ERR_UNUSED_FUNCTION = "Unused Function";
	//private final String NULL = "null"; 
	
	private static final String ERR_SHORT_DESC = UnusedItemsRule.ERR_SHORT_DESC_FUNCTIONS;
	
	private List<FunctionVo> _allUnusedFunctions = new ArrayList<FunctionVo>();
	
	private String _functionName = null;
	private List<FunctionVo> _tmpFunctionsList = null;
	private final char[] _acceptedPreChars = new char[]{CHAR_EQUALS,CHAR_COLON,CHAR_CURLY_CLOSE,CHAR_CURLY_OPEN, CHAR_SEMI_COLON,CHAR_COMMA,CHAR_DOT,CHAR_PARANTHESIS_OPEN,CHAR_PARANTHESIS_CLOSE,CHAR_DOUBLE_QUOTE,CHAR_SINGLE_QUOTE,CHAR_EXCLAMATION};
	private final char[] _acceptedPostChars = new char[]{CHAR_CURLY_CLOSE,CHAR_CURLY_OPEN, CHAR_SEMI_COLON,CHAR_COMMA,CHAR_DOT,CHAR_PARANTHESIS_OPEN,CHAR_PARANTHESIS_CLOSE,CHAR_DOUBLE_QUOTE,CHAR_SINGLE_QUOTE};
	
	
	private List<String> ignoreFunctionsList = ReviewPropertiesUtil.getIgnoreFunctionsList();
	
	@Override
	protected boolean isLineSplitNeeded(){return true;}

	public List<FunctionVo> getAllUnusedFunctions() {
		return _allUnusedFunctions;
	}
	
	public UnusedFunctionsRule(ProjectConfig codeReviewConfig) {
		super(codeReviewConfig,BLANK);
		//super.init(PROPS_RULE_NAME,2);
		String[] ruleDescriptions = {ERR_SHORT_DESC};
		super.init(PROPS_RULE_NAME,RULE_ID,ruleDescriptions,2);
		
		init();
	}
	
	public void init() {
		_functionName = null;
		
		if (null == _allUnusedFunctions) {
			_allUnusedFunctions = new ArrayList<FunctionVo>();
		}

		if (null == _tmpFunctionsList) {
			_tmpFunctionsList = new ArrayList<FunctionVo>();
		}
		
		ignoreFunctionsList = ReviewPropertiesUtil.getIgnoreFunctionsList();

	}
	
	//code for ignoring comments - please write
	@Override
	protected List<ReviewComment> reviewSingleJSLine(String fileName, int lineNum, int lastLineNum, String line, int passNumber, String currFunctionName, boolean functionEndFlag ,int prevCommentedLinesCount) throws TagToolException {


		/*
		final String CHECK_STRING = "kony.os.isInteger(strMonth) || !kony.os.isInteger(strDay)";
		if (line.indexOf(CHECK_STRING)>=0 && passNumber == 2) {
			System.out.println("Checking");
			System.out.println(line);
		}
		*/
		
		String fName = null;
		FunctionVo fnVo = null;
		FunctionVo tmpFnVo = null;
		fnVo = getFunctionName(line);
		fName = fnVo.getFunctionName();
		
		//Util.printToConsole("fName: "+fName+" fnVo: "+fnVo);
		if (fName != null) {
			if (fName.equals(NULL)) {
				_functionName = null;
			} else {
				if (_functionName == null || !_functionName.equals(fName)) {
					_functionName = fName;
					
					tmpFnVo = new FunctionVo();
					tmpFnVo.setFunctionName(_functionName);
					tmpFnVo.setFileName(fileName);
					tmpFnVo.setLineNumber(lineNum);
					
					if (passNumber == 1) {
						//System.out.println("ADDING FUNCTION NAME " + _functionName);
						if (!_allUnusedFunctions.contains(tmpFnVo) 
								&& !ignoreFunctionsList.contains(tmpFnVo.getFunctionName().trim().toLowerCase())) {
							
							_allUnusedFunctions.add(tmpFnVo);
							//System.out.println("DEFINED FUNCTION : " + _functionName);
						} else {
							//System.out.println("DUPICATE FUNCTION : " + _functionName);
						}
					}
				}
			}
		}
		
		if (passNumber == 2) {
			
			// In Second Pass - filter out all the functions which are already being used in the code 
			String tmpReviewLine = null;
			//tmpReviewLine = truncateBetweenQuotes(line);
			
			_tmpFunctionsList.clear();
			fetchFunctionKeyWords(line,_allUnusedFunctions);
			
			if (_tmpFunctionsList.size()>0) {
				for (FunctionVo function : _tmpFunctionsList) {
					// Remove used Functions
					
					if (_allUnusedFunctions.contains(function)) {
						
						_allUnusedFunctions.remove(function);
						//System.out.println("REMOEVED FUNCTION : " + functionName);
						
					}
				}
			}

		}
		
		return getComments();
}

private void fetchFunctionKeyWords(String reviewLine, List<FunctionVo> functionsList) {
		int index = -1;
		String tmpReviewLine = null;
		int fnLength = 0; 

		if (reviewLine == null || reviewLine.length() ==0) {
			return;
		}
				
			for (FunctionVo function : functionsList) {
				
				index = getKeywordIndex(reviewLine,function.getFunctionName(),_acceptedPreChars,_acceptedPostChars,false);
				if (index>=0) {
					// Found that function is used.  
					if (_functionName != null && function.getFunctionName().equals(_functionName)) { 
						// Skip. Do not count if the current function is being counted in the same function definition
					} else {
						if (!_tmpFunctionsList.contains(function)) {
							_tmpFunctionsList.add(function);
						}
					}
					
					fnLength = function.getFunctionName().length();
					if ((reviewLine.length()>(index+(fnLength-1))) && (index+(fnLength-1)) >=0) {
						
						if (index >0) {
							tmpReviewLine = reviewLine.substring(0,index);
						} else {
							tmpReviewLine = BLANK;
						}
						tmpReviewLine += reviewLine.substring(index+(fnLength));
						fetchFunctionKeyWords(tmpReviewLine,functionsList);
					}
					break;
				}
			}
		
		return;
	}


/*
@Override
protected int getKeywordIndex(String reviewLine, String keyWord, char[] acceptedPreChars, char[] acceptedPostChars, boolean ignoreCase) {
	int index = 0;
	char prevChar;
	char postChar;
	boolean prevCharOk = false;
	boolean postCharOk = false;
	String line = reviewLine;
	int keyWordLength = 0;
	int keyWordCount = 0;
	
	if (line == null || line.length()==0) {
		return -1;
	}

	if (keyWord == null || keyWord.length()==0) {
		return -1;
	}
	
	keyWordLength = keyWord.length();
	if (ignoreCase) {
		index = line.toLowerCase().indexOf(keyWord.toLowerCase());
	}else {
		index = line.indexOf(keyWord);
	}
	
	if (index <0) {
		return -1;
	} 
	
	while (index >=0) {
		keyWordCount++;
		
		prevCharOk = false;
		postCharOk = false;
		
		if (index ==0) {
			prevCharOk = true;
		}
		
		if (index >0 && !prevCharOk) {
			prevChar = line.charAt(index-1);
			if (containsChar(acceptedPreChars, prevChar) || Character.isWhitespace(prevChar)) {
				prevCharOk = true;	
			}
		}
		
		if (line.length() > (index+keyWordLength)) {
			postChar = line.charAt(index+keyWordLength);
			if (containsChar(acceptedPostChars, postChar) || Character.isWhitespace(postChar)) {
				postCharOk = true;	
			}
		} else {
			postCharOk = true;
		}
		
		// Any prev char is accepted
		if (null == acceptedPreChars) {
			prevCharOk = true;
		}

		// Any post char is accepted
		if (null == acceptedPostChars) {
			postCharOk = true;
		}

		
		if (prevCharOk && postCharOk) {
			// Ignore all key words within quotes
			return index; 
		}  else {

			if (line.length()>(index+keyWordLength)) {
				
				if (ignoreCase) {
					index = line.toLowerCase().indexOf(keyWord.toLowerCase(),index+keyWordLength);
				} else {
					index = line.indexOf(keyWord,index+keyWordLength);
				}
			} else {
				index = -1;
			}
		}
	}

	
	return index;
}	

*/
}
