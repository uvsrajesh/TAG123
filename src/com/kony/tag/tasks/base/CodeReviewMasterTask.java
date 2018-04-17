package com.kony.tag.tasks.base;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

import com.kony.tag.arabiclayout.util.ArabicLayoutUtil;
import com.kony.tag.config.CodeReviewStatus;
import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.ProjectConstants;
import com.kony.tag.config.TagToolException;
import com.kony.tag.js.codereview.config.FunctionVo;
import com.kony.tag.js.codereview.config.ReviewComment;
import com.kony.tag.js.codereview.util.ReportsUtil;

public abstract class CodeReviewMasterTask implements ProjectConstants {

	private boolean isEnabled;
	private String reviewFileDirectory;
	private String reviewFile;

	private ProjectConfig codeReviewConfig = null;
	
	private static final String ARG1="%1";
	private static final String ARG2="%2";
	private static final String ARG3="%3";
	
	public static String SEV_LOW = "Low";
	public static String SEV_MED = "Medium";
	public static String SEV_HIGH = "High";
	public static String SEV_CRITICAL = "Critical";
	
	public static String BLANK = "";
	public static String SEMI_COLON = ";";
	public static String CLOSING_BRACE = "}";
	public static String OPENING_BRACE = "{";
	public static String SINGLE_LINE_COMMENT = "//";
	
	public static String MULTI_LINE_COMMENT_START = "/*";
	public static String MULTI_LINE_COMMENT_END = "*/";
	
	public static final String FUNCTION_KEYWORD = "function";
	public static final String FUNCTION_TYPE2 = ":function(";
	public static final String FUNCTION_TYPE3 = "=function(";
	public static final String FUNCTION_TYPE4 = "(function(";
	public static final String FUNCTION_TYPE5 = "kony.net.invokeServiceAsync";
	public static final String RETURN = "return";
	
	private FunctionVo nullFunctionVo = null;
	
	private List<ReviewComment> comments = null;
	
	private CodeReviewMasterTask() {
		// do not use
	}

	public CodeReviewMasterTask(ProjectConfig codeReviewConfig) {
		this.codeReviewConfig = codeReviewConfig;
	}
	
	public ProjectConfig getProjectConfig() {
		return codeReviewConfig;
	}
	
	public void setProjectConfig(ProjectConfig codeReviewConfig) {
		this.codeReviewConfig = codeReviewConfig;
	}
	
	protected void initArabicTask(String ruleName, String ruleId, String[] ruleDescriptions) {
		
		String ruleEnabledFlag = null;
		comments = null;
		
		if (ruleName != null && ruleId != null) {
			ruleEnabledFlag = getProjectConfig().getArabicLayoutProperty(ruleName);
	
			// Check if this rule was configured to be executed !!
			if (null != ruleEnabledFlag && ruleEnabledFlag.trim().equalsIgnoreCase(YES)) {
				setEnabled(true);
			} else {
				setEnabled(false);
			}
		}
	}

	protected void init(String ruleName, String ruleId, String[] ruleDescriptions) {
		
		String ruleEnabledFlag = null;
		comments = new ArrayList<ReviewComment>();
		
		if (nullFunctionVo == null) {
			nullFunctionVo = new FunctionVo();
			nullFunctionVo.setFunctionName(null);
			nullFunctionVo.setStartIndex(-1);
		}
		
		if (ruleName != null && ruleId != null) {
			ruleEnabledFlag = getProjectConfig().getCodeReviewProperty(ruleName);
	
			// Check if this rule was configured to be executed !!
			if (null != ruleEnabledFlag && ruleEnabledFlag.trim().equalsIgnoreCase(YES)) {
				setEnabled(true);
			} else {
				setEnabled(false);
			}
			
			ReportsUtil.getInstance().initReportSummary(ruleId, ruleDescriptions, isEnabled);
		}
	}
	
	public void addError( 
			String reviewComment,
			String shortDesc,
			String referenceData,
			String fileName,
			int lineNumber,
			String severity,
			String ruleId) throws TagToolException {
		if(comments != null) {
			addComment(comments,reviewComment,shortDesc,referenceData,fileName,lineNumber,severity,ruleId,false);
		}
	}	
	
	public void addWarning( 
			String reviewComment,
			String shortDesc,
			String referenceData,
			String fileName,
			int lineNumber,
			String severity,
			String ruleId) throws TagToolException {
		if(comments != null) {
			addComment(comments,reviewComment,shortDesc,referenceData, fileName,lineNumber,severity,ruleId,true);
		}
	}
	
	public void addComment(List<ReviewComment> comments, 
			String reviewComment,
			String shortDesc,
			String referenceData,
			String fileName,
			int lineNumber,
			String severity,
			String ruleId,
			boolean isWarning) throws TagToolException {
		
		ReviewComment newComment = null;
		boolean isDuplicate = false;
		
		if (null == comments) {
			throw new TagToolException("Comments Master List not initialized ");
		}
		
		newComment = new ReviewComment(reviewComment, shortDesc, referenceData, fileName, getReviewFileDirectory(), lineNumber, severity, ruleId,isWarning);
		
		isDuplicate = false;
		// Add this comment only if it is not a duplicate
		for (ReviewComment comment : comments){
			
			if (comment.getLineNumber() == newComment.getLineNumber()) {
				if (comment.equals(newComment)) {
					isDuplicate = true;
					break;
				}
			}
		}
		
		if (!isDuplicate) {
			comments.add(newComment);
		}
	}
	
	protected final List<String> fetchLinesOfFile(File file) {
		List<String> lines = new ArrayList<String>();
		LineNumberReader lineNumberReader = null;
		String line;
		
		try {
			lineNumberReader = new LineNumberReader(new FileReader(file));
			
			if (null != lineNumberReader) { 
			
				while(null != (line = lineNumberReader.readLine())) {
					lines.add(line);
				}
			}
		} catch (FileNotFoundException excp) {
			CodeReviewStatus.getInstance().addErrorMessage(excp, file.getName()+" not found for Code Review!! ");
		} catch (IOException excp) {
			CodeReviewStatus.getInstance().addErrorMessage(excp, "Unable to read the file " + file.getName());
		}finally{
			if(lineNumberReader != null){
				try {
					lineNumberReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return lines;
	}	
	
	public void addStatusCommment(String statusComment) {
		CodeReviewStatus.getInstance().addErrorMessage(statusComment);
	}
	
	public List<ReviewComment> getComments() {
		return comments;
	}
	
	protected void setComments(List<ReviewComment> comments) {
		this.comments = comments;
	}
	
	public boolean isEnabled() {
		return isEnabled ;
	}

	protected void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	
	public String getReviewFileDirectory() {
		return reviewFileDirectory;
	}

	public void setReviewFileDirectory(String reviewFileDirectory) {
		if (null !=reviewFileDirectory && reviewFileDirectory.trim().length()>0) {
			String[] lines = reviewFileDirectory.split(CODE_BACKUP_PATH_DISPLAY_NAME);
			this.reviewFileDirectory = lines[(lines.length-1)];
		} else {
			this.reviewFileDirectory = reviewFileDirectory;
		}
	}
	
	public String getReviewFile() {
		return reviewFile;
	}

	public void setReviewFile(String reviewFile) {
		this.reviewFile = reviewFile;
	}
	
	protected final String getCompressedString(String inputStr) {
		
		char[] mychars = null;
		StringBuffer tmpStrBuff = null;

		// Compress the line with no white spaces
		if (inputStr == null || inputStr.trim().length() ==0) {
			return null;
		}
		
		mychars = inputStr.toCharArray();
		tmpStrBuff = new StringBuffer();
		for (char c : mychars) {
			if (!Character.isWhitespace(c)) {
				tmpStrBuff.append(c);
			}
		}
		
		if (tmpStrBuff.length() == 0) {
			return null;
		}
		
		return tmpStrBuff.toString();
	}

	protected final String stripQuotes(String data) {
		StringBuffer tmpStrBuff = null;
		char[] mychars = null;
		
		if (data == null || data.length()==0) {
			return null;
		}
		mychars = data.toCharArray();
		tmpStrBuff = new StringBuffer();
		
		for (char c : mychars) {
			if (c==CHAR_SINGLE_QUOTE || c == CHAR_DOUBLE_QUOTE) {
				continue;
			}
			tmpStrBuff.append(c);
		}
		
		return tmpStrBuff.toString();
	}
	
	protected final String truncateBetweenQuotes(String data) {
		StringBuffer tmpStrBuff = null;
		char[] mychars = null;
		int singleQuoteCount = 0;
		int doubleQuoteCount = 0;
		boolean isWithinDoubleQuotes = false;
		boolean isWithinSingleQuotes = false;
		boolean isWithinQuotes = false;
		
		
		if (data == null || data.length()==0) {
			return null;
		}
		
		if (!(data.indexOf("\"") >=0 || data.indexOf("'")>=0)) {
			return data;
		}
		
		mychars = data.toCharArray();
		tmpStrBuff = new StringBuffer();
		
		for (char c : mychars) {
			
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

			if (!isWithinQuotes && c !=  CHAR_SINGLE_QUOTE && c!= CHAR_DOUBLE_QUOTE) {
				tmpStrBuff.append(c);
			}
		}
		
		return tmpStrBuff.toString();
	}

	protected final String truncateBetweenQuotes(String data, char truncateChar) {
		StringBuffer tmpStrBuff = null;
		char[] mychars = null;
		int singleQuoteCount = 0;
		int doubleQuoteCount = 0;
		boolean isWithinDoubleQuotes = false;
		boolean isWithinSingleQuotes = false;
		boolean isWithinQuotes = false;
		
		
		if (data == null || data.length()==0) {
			return null;
		}
		
		if (!(data.indexOf("\"") >=0 || data.indexOf("'")>=0)) {
			return data;
		}
		
		mychars = data.toCharArray();
		tmpStrBuff = new StringBuffer();
		
		for (char c : mychars) {
			
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

			if (isWithinQuotes && c == truncateChar) {
				continue;
			} else {
				tmpStrBuff.append(c);
			}
		}
		
		return tmpStrBuff.toString();
	}
	
	protected FunctionVo getFunctionName(String lineData) {
		int index = 0;
		int index2 = 0;
		boolean isType1Defined = false;
		boolean isType3Defined = false;
		boolean isType4Defined = false;
		boolean isType5Defined = false;
		String balanceLine = null;
		char[] mychars = null;
		String tmpStr = null;
		String tmpLine = null;
		char c;
		char c1;
		String fName = null;
		FunctionVo functionVo = new FunctionVo();

		// Compress the line with no white spaces
		
		if (null == lineData || lineData.trim().length() ==0) {
			return nullFunctionVo;
		}

		tmpLine = new String(lineData);
		index = getFunctionKeywordIndex(tmpLine);
		functionVo.setStartIndex(index);
		isType5Defined = isType5FunctionDefined(lineData);
		if (index <0 && !isType5Defined) {
			return nullFunctionVo;
		}

		isType3Defined = isType3FunctionDefined(lineData);
		
		if (!isType3Defined) {
			isType4Defined = isType4FunctionDefined(lineData);
			if(!isType4Defined) {
				isType5Defined = isType5FunctionDefined(lineData);
				if(!isType5Defined) {
					isType1Defined = true;
				}
			}
		}
		
		if(isType5Defined){
			fName = "networkCall";
		} else if (isType4Defined) {
			
			if (index <0) {
				return nullFunctionVo;
			} else if (tmpLine.length()>(index+9)){
				balanceLine = tmpLine.substring(index+9);
			}
			int startIndex = 0;
			int returnIndex = tmpLine.indexOf(RETURN);
			if(returnIndex >= 0){
				startIndex = returnIndex + 6;
			}
			
			tmpLine = getCompressedString(tmpLine);
			int function4Index = tmpLine.indexOf(FUNCTION_TYPE4);
			mychars = tmpLine.substring(startIndex,function4Index).toCharArray();
			for (int i=0; i<mychars.length;i++) {
				c = mychars[i];
				
				if (c == CHAR_COLON || c == CHAR_EQUALS || c == ' ') {
					tmpStr = tmpLine.substring(startIndex+i+1,function4Index);
					System.out.println("tmpStr: "+tmpStr);
					if (i == mychars.length - 1) {
						// Read Function Name
						boolean nameStarted = false;
						fName = tmpStr.substring(0,i);
					}
				}
				if (i == mychars.length - 1) {
					// Read Function Name
					fName = tmpStr != null ? tmpStr.substring(0,i-1) : tmpLine.substring(0, function4Index);
					break;
				}
			}
			
		}
		else if (isType3Defined) {
			
			if (index <0) {
				return nullFunctionVo;
			} else if (tmpLine.length()>(index+9)){
				balanceLine = tmpLine.substring(index+9);
			}
			
			mychars = tmpLine.substring(0,index).toCharArray();
			for (int i=0; i<mychars.length;i++) {
				c = mychars[i];
				
				if (c == CHAR_COLON || c == CHAR_EQUALS) {
					tmpStr = tmpLine.substring(i+1,index);
					if (tmpStr == null || tmpStr.trim().length() == 0) {
						// Read Function Name
						
						boolean nameStarted = false;
						for (int j=(i-1);j>=0;j--) {
							c1 = mychars[j];
							if (!nameStarted && !Character.isWhitespace(c1)) {
								nameStarted = true;
							}
							
							if (nameStarted && (Character.isWhitespace(c1) || c1==CHAR_COMMA)) {
								// Read the function name
								fName = tmpLine.substring(j,i);
								break;
							}else if (nameStarted && j==0) {
								fName = tmpLine.substring(j,i);
								break;
							}
						}
						
						break;
					}
				}
			}
			
		} else if (isType1Defined) {
			
			if (tmpLine.length()>(index+8)) {
				tmpLine = tmpLine.substring(index+8);
			} else {
				// Not a valid function name
				return nullFunctionVo;
			}
			index2 = tmpLine.indexOf("(");
			
			if (index2>0) {
				fName = tmpLine.substring(0,index2);
			} else {
				// Not a valid function
				return nullFunctionVo;
			}
		
			if (tmpLine.length()>(index2+1)) {
				balanceLine = tmpLine.substring(index2);
			}
		}

		/*
		if (balanceLine != null && balanceLine.trim().length()>0) {
			if (getKeywordIndex(balanceLine, FUNCTION_KEYWORD)>=0) {
				// Multiple functions defined. Do not count this line;
				return nullFunctionVo;
			}
		}
		*/
		
		if (fName != null) {
			fName = stripQuotes(fName.trim());
			functionVo.setFunctionName(fName);
		}
		
		return functionVo;
	}	
	
	protected final int getFunctionKeywordIndex(String reviewLine) {
		// These are the default accepted pre and post chars - typically used for function definition 
		 char[] acceptedPreChars = new char[]{CHAR_EQUALS,CHAR_COLON,CHAR_CURLY_CLOSE,CHAR_SEMI_COLON,CHAR_COMMA,CHAR_PARANTHESIS_OPEN};
		 char[] acceptedPostChars = new char[]{CHAR_PARANTHESIS_OPEN};
		 return getKeywordIndex(reviewLine, FUNCTION_KEYWORD, acceptedPreChars, acceptedPostChars,false);
	}

	protected boolean containsChar(char[] searchChars, char searchChar) {
		boolean isMatched = false;
		
		if (null == searchChars || searchChars.length ==0) {
			return false;
		}
		
		for (char c : searchChars) {
			if (c==searchChar) {
				isMatched = true;
				break;
			}
		}
		
		return isMatched;
	}
	
	protected int getKeywordIndex(String reviewLine, String keyWord, char[] acceptedPreChars, char[] acceptedPostChars) {
		return getKeywordIndex(reviewLine, keyWord, acceptedPreChars, acceptedPostChars, false, false);
	}
	
	protected int getKeywordIndex(String reviewLine, String keyWord, char[] acceptedPreChars, char[] acceptedPostChars, boolean ignoreCase) {
		return  getKeywordIndex(reviewLine, keyWord, acceptedPreChars, acceptedPostChars, ignoreCase, false);
	}
	
	protected int getKeywordIndex(String reviewLine, String keyWord, char[] acceptedPreChars, char[] acceptedPostChars, boolean ignoreCase, boolean ignoreQuotes) {
		int index = 0;
		char prevChar;
		char postChar;
		boolean prevCharOk = false;
		boolean postCharOk = false;
		String line = reviewLine;
		boolean isWithinQuotesFlag = false;
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
				if(ignoreQuotes)
					break;
				
				isWithinQuotesFlag = isWithinQuotes(line,index);
				// Ignore all key words within quotes
				if (!isWithinQuotesFlag) {
					break;
				}
			} 

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
	
		
		return index;
	}	
	
	private boolean isWithinQuotes(String data,int index) {
		
		char[] mychars = null;
		int singleQuoteCount = 0;
		int doubleQuoteCount = 0;
		boolean isWithinDoubleQuotes = false;
		boolean isWithinSingleQuotes = false;
		boolean isWithinQuotes = false;
		char c;
		
		
		if (data == null || data.length()==0 || index<0 || index>=data.length()) {
			return false;
		}
		
		if (!(data.indexOf("\"") >=0 || data.indexOf("'")>=0)) {
			return false;
		}
		
		mychars = data.toCharArray();
		
		for (int i=0; i<mychars.length;i++) {
			 c = mychars[i];
			 
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

			if (index == i) {
				// This is where we want to check the value of isWithinQuotes
				break;
			}
		}
		
		return isWithinQuotes;		
	}
	
	private boolean isType3FunctionDefined(String lineData) {
		int index = 0;
		String tmpStr = getCompressedString(lineData);
		if (tmpStr == null || tmpStr.length() == 0) {
			return false;
		}
		index = tmpStr.toLowerCase().indexOf(FUNCTION_TYPE2);
		if (index<0) {
			index = tmpStr.toLowerCase().indexOf(FUNCTION_TYPE3);
		}
		return ((index<0)?false:true);
	}
	
	private boolean isType4FunctionDefined(String lineData) {
		int index = 0;
		String tmpStr = getCompressedString(lineData);
		index = tmpStr.toLowerCase().indexOf(FUNCTION_TYPE4);
		return ((index<0)?false:true);
	}
	
	private boolean isType5FunctionDefined(String lineData) {
		int index = 0;
		String tmpStr = getCompressedString(lineData);
		index = tmpStr.indexOf(FUNCTION_TYPE5);
		return ((index<0)?false:true);
	}
	
	
}
