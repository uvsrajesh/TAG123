package com.kony.tag.tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.TagToolException;
import com.kony.tag.js.codereview.config.FunctionVo;
import com.kony.tag.js.codereview.config.ReviewComment;
import com.kony.tag.tasks.base.CodeReviewMasterTask;

public abstract class JsReviewMasterTask extends CodeReviewMasterTask {

	private int passesNeeded;
	private int _openBracesCount = 0;
	private boolean _functionStarted = false;
	private int _functionBracesCount = 0;
	private int _innerFunctionBracesCount = 0;
	private String _functionName = null;
	boolean functionStartAdded = false;
	boolean functionEndAdded = false;
	boolean innerFunctionStarted = false;
	boolean innerFunctionEnded = false;
	
	private JsReviewMasterTask() {
		// Do not use
		super(null);
	}
	
	protected void initArabicTask(String ruleName,String ruleId, String[] ruleDescriptions,int maxPassesNeeded) {
		super.initArabicTask(ruleName,ruleId,ruleDescriptions);
		setPassesNeeded(maxPassesNeeded);
		_openBracesCount = 0;
		_functionStarted = false;
		_functionBracesCount = 0;
		_innerFunctionBracesCount = 0;
		_functionName = null;
		
	}
	
	protected void init(String ruleName,String ruleId, String[] ruleDescriptions,int maxPassesNeeded) {
		super.init(ruleName,ruleId,ruleDescriptions);
		setPassesNeeded(maxPassesNeeded);
		_openBracesCount = 0;
		_functionStarted = false;
		_functionBracesCount = 0;
		_innerFunctionBracesCount = 0;
		_functionName = null;
		
	}

	public JsReviewMasterTask(ProjectConfig codeReviewConfig) {
		super(codeReviewConfig);
		setReviewFileDirectory(LOCATION_JS_MODULES);
	}
	
	public JsReviewMasterTask(ProjectConfig codeReviewConfig, String reviewDirectory) {
		super(codeReviewConfig);
		setReviewFileDirectory(reviewDirectory);
	}	
	
	public final List<ReviewComment> reviewJSLine(String fileName, int lineNum, int lastLineNum, String line, int passNumber, int prevCommentedLinesCount) throws TagToolException {
		return null;
	}

	public final List<ReviewComment> reviewJSLine(String fileName, int lineNum, int lastLineNum, String line, int passNumber, int prevCommentedLinesCount, ArrayList<String> linesOfCode) throws TagToolException {
		char[] lineChars = null;
		String tmpReviewLine = null;
		FunctionVo fnVo = null;
		String fName = null;
		int index = 0;
		int index2 = 0;
		boolean functionEndIndicator = false;
		boolean functionStartIndicator = false;
		
		
		char c;
		
		if (isLineSplitNeeded()) {
			// take of all semi colons within quotes
			
			List<String> lines = splitMultipleJSLines(line);

			
			for (String reviewLine : lines) {
				functionEndIndicator = false;
				functionStartIndicator = false;
				// The function is defined in a single line, so continue
				if(reviewLine.matches(".*function.*\\{.*\\}.*")){
					continue;
				}
				
				fnVo = getFunctionName(reviewLine);
				fName = fnVo.getFunctionName();
				index = fnVo.getStartIndex();
				
				if (fName != null && index >=0 && !_functionStarted) {
					tmpReviewLine = reviewLine.substring(index);
					_functionStarted = true;
					_functionName = fName;
				} else {
					tmpReviewLine = reviewLine;
				}
				
				if (null == tmpReviewLine || tmpReviewLine.trim().length()==0) {
					continue;
				}
				if(fName != null && !_functionName.equals(fName)) {
					innerFunctionStarted = true;
				}
				
				if(tmpReviewLine.contains("{") || tmpReviewLine.contains("}")) {
					lineChars = tmpReviewLine.toCharArray();
					
					for (int i=0; i<lineChars.length;i++) {
						c = lineChars[i];
						if (c == CHAR_CURLY_OPEN) {
							_openBracesCount++;
							if (_functionStarted) {
								functionStartAdded = false;
								if(_functionBracesCount == 0){
									functionStartIndicator = true;
								}
								_functionBracesCount++;
								if(innerFunctionStarted){
									_innerFunctionBracesCount++;
								}
							}
						}
						
						if (c == CHAR_CURLY_CLOSE) {
							_openBracesCount--;
							if (_functionStarted) {
								_functionBracesCount--;
							}
							if(innerFunctionStarted){
								_innerFunctionBracesCount--;
							}
							
						}
						if (c == CHAR_CURLY_CLOSE && _innerFunctionBracesCount == 0) {
							if(innerFunctionStarted) {
								innerFunctionEnded = true;
								innerFunctionStarted = false;
							}
						}
						
						if (c == CHAR_CURLY_CLOSE && _functionBracesCount==0) {
							if(_functionStarted) {
								functionEndIndicator = true;
							}
							functionEndAdded = false;
							_functionStarted = false;
							
							// check if another function is defined in the same line as the end of previous function.. currently that is not supported
							if(tmpReviewLine.length()>(i+1)) {
								index2 = getFunctionKeywordIndex(tmpReviewLine.substring(i+1));
								if (index2>=0) {
									throw new TagToolException(TagToolException.ERR_CD_SYNTAX_NOT_SUPPORTED,TagToolException.ERR_MSG_FUNCTION_NOT_ON_NEWLINE + fileName + " :: Line #" + lineNum + ". Cannot review rest of the file");
								}
							}
						}
					}
				}
				if(reviewLine.indexOf("return ") >= 0){
					boolean returnIndicator = false;
					String prevLine = getCompressedString(reviewLine);
					if(prevLine != null) {
						// return statement should be the first word of the line
						returnIndicator = prevLine.startsWith("return");
					}
					if(returnIndicator) {
						linesOfCode.remove(linesOfCode.size() - 1);
						linesOfCode.add("TAG.profiler.endFunction(\""+_functionName+"\", "+lineNum+", \""+fileName+"\");");
						linesOfCode.add(reviewLine);
					}
				}
				if((fName != null && fName.equals("networkCall")) || reviewLine.indexOf("function") >= 0){
					ArrayList<String> jsFileList = getProjectConfig().getJsFilesArray();
					String l = "";
					boolean fnFlag = true;
					if(reviewLine.indexOf("function") >= 0 && !reviewLine.matches(".*function.*\\{.*")){
						fnFlag = false;
						l = linesOfCode.remove(linesOfCode.size() - 1);
					}
					boolean formEventDetected = false;
					if((getProjectConfig().getInstrumentMode().equals("forms") && !jsFileList.contains(fileName)) 
							|| getProjectConfig().getInstrumentMode().equals("spa")) {
						boolean formEvent = (_functionName.matches(".*_seq(\\d+)"));
						System.out.println("_functionName: "+_functionName+" formEvent: "+formEvent);
						if(formEvent && functionStartIndicator) {
							if(!(_functionName.matches("frmEventInfo.*_seq(\\d+)") || _functionName.matches("frmFunctionInfo.*_seq(\\d+)"))) {
								linesOfCode.add("TAG.profiler.startEvent(\""+_functionName+"\");");
								functionStartAdded = true;
							}
							formEventDetected = true;
						}
					}
					if((getProjectConfig().getInstrumentMode().contains("modules") && jsFileList.contains(fileName)) 
							|| (getProjectConfig().getInstrumentMode().equals("spa") && !formEventDetected)){
						boolean formEvent = getProjectConfig().getInstrumentMode().equals("spa") &&
								((_functionName.matches("addWidgets.*") && _functionName.startsWith("addWidgets")) 
								|| (_functionName.matches(".*Globals") && _functionName.endsWith("Globals"))
								|| (_functionName.matches("initialize.*") && _functionName.startsWith("initialize"))
								|| (_functionName.matches("kony.*") && _functionName.startsWith("kony."))
								); 
						boolean excludedFunction = EXCLUDED_FUNCTION_NAMES.contains(_functionName) || formEvent;
						//System.out.println("excludedFunction: "+excludedFunction);
						if(reviewLine.contains(FUNCTION_TYPE5)) {
							l = linesOfCode.remove(linesOfCode.size() - 1);
							ArrayList<String> callBackArray  = getProjectConfig().getCallBackArray(); 
							boolean callBackFound = false;
							String serviceObject = null;
							int paramStartIndex = reviewLine.indexOf("(");
							int paramEndIndex = reviewLine.indexOf(")");
							if(paramStartIndex >= 0 && paramEndIndex >= 0 && paramEndIndex > paramStartIndex) {
								String functionParameters = reviewLine.substring(paramStartIndex,paramEndIndex);
								String[] paramArray = functionParameters.split(",");
								if(paramArray.length > 0){
									serviceObject = paramArray[1].trim();
								}
								/*if(paramArray.length > 1) {
									if(!callBackArray.contains(paramArray[2])){
										callBackArray.add(paramArray[2]);
										callBackFound = true;
									}
								}*/
							}
							/*if(!callBackFound && !callBackArray.contains("commonServiceCallBack")){
								callBackArray.add("commonServiceCallBack");
							}*/
							if(serviceObject != null) {
								linesOfCode.add("TAG.profiler.startNetworkCall("+serviceObject+"[\"serviceID\"], "+lineNum+", \""+fileName+"\");");
							} else {
								linesOfCode.add("TAG.profiler.startNetworkCall(\""+_functionName+"\", "+lineNum+", \""+fileName+"\");");
							}
							linesOfCode.add(l);
						} else if((functionStartIndicator || getProjectConfig().getCallBackArray().contains(fName)) && !excludedFunction){
							if(getProjectConfig().getCallBackArray().contains(_functionName) ||
									(fName != null && getProjectConfig().getCallBackArray().contains(fName))) {
								linesOfCode.add("if (status == 400 || status == 300) {");
								linesOfCode.add("TAG.profiler.endNetworkCall(\""+(fName != null ? fName : _functionName)+"\", "+lineNum+", \""+fileName+"\");");
								linesOfCode.add("TAG.profiler.startFunction(\""+(fName != null ? fName : _functionName)+"\", "+lineNum+", arguments, \""+fileName+"\");");
								linesOfCode.add("} else {");
								linesOfCode.add("return;");
								linesOfCode.add("}");
							} else {
								linesOfCode.add("TAG.profiler.startFunction(\""+_functionName+"\", "+lineNum+", arguments, \""+fileName+"\");");
							}
							functionStartAdded = true;
						}
					}
					if(!fnFlag) {
						linesOfCode.add(l);
					}
					
				} else if((functionEndIndicator && !functionEndAdded && _functionBracesCount == 0)){
					ArrayList<String> jsFileList = getProjectConfig().getJsFilesArray();
					String l = linesOfCode.remove(linesOfCode.size() - 1);
					boolean formEventDetected = false;
					if((getProjectConfig().getInstrumentMode().equals("forms") && !jsFileList.contains(fileName)) || 
							getProjectConfig().getInstrumentMode().equals("spa")){
						boolean onClick = (_functionName.matches(".*_seq(\\d+)"));
						if(onClick) {
							if(!(_functionName.matches("frmEventInfo.*_seq(\\d+)") || _functionName.matches("frmFunctionInfo.*_seq(\\d+)"))) {
								linesOfCode.add("TAG.profiler.endEvent(\""+_functionName+"\");");
								functionEndAdded = true;
							}
							formEventDetected = true;
						}
					}
					if((getProjectConfig().getInstrumentMode().contains("modules") && jsFileList.contains(fileName)) || 
							(getProjectConfig().getInstrumentMode().equals("spa") && !formEventDetected)){
						boolean formEvent = getProjectConfig().getInstrumentMode().equals("spa") &&
								((_functionName.matches("addWidgets.*") && _functionName.startsWith("addWidgets")) 
								|| (_functionName.matches(".*Globals") && _functionName.endsWith("Globals"))
								|| (_functionName.matches("initialize.*") && _functionName.startsWith("initialize"))
								|| (_functionName.matches("kony.*") && _functionName.startsWith("kony.")));
						boolean excludedFunction = EXCLUDED_FUNCTION_NAMES.contains(_functionName) || formEvent;
						if(!excludedFunction) {
							String prevLine = linesOfCode.get(linesOfCode.size() - 1);
							String returnLine = prevLine;
							boolean returnIndicator = prevLine.indexOf("return ") >= 0;
							prevLine = getCompressedString(prevLine);
							if(prevLine != null && returnIndicator) {
								// return statement should be the first word of the line
								returnIndicator = prevLine.startsWith("return");
							}
							if(returnIndicator) {
								/*linesOfCode.remove(linesOfCode.size() - 1);
								linesOfCode.add("TAG.profiler.endFunction(\""+_functionName+"\", "+lineNum+");");
								linesOfCode.add(returnLine);*/
							} else {
								linesOfCode.add("TAG.profiler.endFunction(\""+_functionName+"\", "+lineNum+", \""+fileName+"\");");
							}
							if(innerFunctionEnded) {
								innerFunctionEnded = false;
							} else {
								functionEndAdded = true;
							}
						}
					}
					linesOfCode.add(l);
					
				}
				reviewSingleJSLine(fileName, lineNum, lastLineNum, reviewLine, passNumber, _functionName, functionEndIndicator,prevCommentedLinesCount);
				
				if (functionEndIndicator) {
					_functionName = null;
					_functionBracesCount=0;
					_functionStarted = false;
				}
			}
		} else {
			reviewSingleJSLine(fileName, lineNum, lastLineNum, line, passNumber, BLANK, false,prevCommentedLinesCount);
		}

		return getComments();
	}
	
	protected abstract List<ReviewComment> reviewSingleJSLine(String fileName, int lineNum, int lastLineNum, String reviewLine, int passNumber, String currFunctionName, boolean functionEndFlag, int prevCommentedLinesCount) throws TagToolException;
	protected abstract boolean isLineSplitNeeded();
	
	public int getPassesNeeded() {
		return passesNeeded;
	}

	protected void setPassesNeeded(int passesNeeded) {
		this.passesNeeded = passesNeeded;
	}
	
	private List<String> splitMultipleJSLines(String line) {
		List<String> lines = new ArrayList<String>();
		StringBuffer splitLine = new StringBuffer();
		char[] mychars = null;
		int singleQuoteCount = 0;
		int doubleQuoteCount = 0;
		boolean isWithinDoubleQuotes = false;
		boolean isWithinSingleQuotes = false;
		boolean isWithinQuotes = false;
		
		
		if (line == null || line.trim().length()==0) {
			return lines;
		}
		
		mychars = line.toCharArray();
		
		for(char c: mychars) {
			if (c == CHAR_SINGLE_QUOTE && !isWithinDoubleQuotes) {
				singleQuoteCount++;
			}
			if (c == CHAR_DOUBLE_QUOTE && !isWithinSingleQuotes) {
				doubleQuoteCount++;
			}
			
			if ((singleQuoteCount%2)==1) {
				isWithinSingleQuotes = true;
			}else {
				isWithinSingleQuotes = false;
			}

			if ((doubleQuoteCount%2)==1) {
				isWithinDoubleQuotes = true;
			}else {
				isWithinDoubleQuotes = false;
			}
			
			if (isWithinSingleQuotes || isWithinDoubleQuotes) {
				isWithinQuotes = true;
			} else {
				isWithinQuotes = false;
			}
			
			splitLine.append(c);
			
			if (c == CHAR_SEMI_COLON && !isWithinQuotes) {
				if (splitLine.length() > 0) {
					lines.add(splitLine.toString());
				}
				splitLine = new StringBuffer();
			}
			
		}
		
		if (splitLine.length() > 0) {
			lines.add(splitLine.toString());
		}
		
		return lines;
	}
}
