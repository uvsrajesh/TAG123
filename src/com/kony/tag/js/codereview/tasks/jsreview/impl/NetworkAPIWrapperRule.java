package com.kony.tag.js.codereview.tasks.jsreview.impl;

import java.util.ArrayList;
import java.util.List;

import com.kony.tag.config.CodeReviewStatus;
import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.TagToolException;
import com.kony.tag.js.codereview.config.FunctionVo;
import com.kony.tag.js.codereview.config.ReviewComment;
import com.kony.tag.tasks.JsReviewMasterTask;

public class NetworkAPIWrapperRule extends JsReviewMasterTask {
	
	public static final String PROPS_RULE_NAME = "rules.js.006.network_api_wrapper_rule";
	public static final String RULE_ID = "JS-006";
	
	public static final String ERR_MULTIPLE_WRAPPERS = "Network API invoked in multiple functions. Instead, use a single re-usable wrapper function which invokes the Network API";
	private static final String ERR_SHORT_DESC = "Multiple Network API functions found";
	
	private final String[] NETWORK_API_LIST1 = new String[]{
			"appmiddlewareinvokerasync","appmiddlewaresecureinvokerasync"
	};
	
	private final String[] NETWORK_API_LIST2 = new String[]{
			"appmiddlewareinvoker","appmiddlewareinvokerasync"
	};
	
	private final String[] NETWORK_API_LIST3 = new String[]{
			"invokeServiceAsync","invokeservice"
	};	

	private final String NETWORK_API_LIST1_VAL = "appmiddlewareinvokerasync, appmiddlewaresecureinvokerasync";
	private final String NETWORK_API_LIST2_VAL = 	"appmiddlewareinvoker, appmiddlewareinvokerasync";
	private final String NETWORK_API_LIST3_VAL = "invokeServiceAsync,invokeservice";	
	
	private final char[] _acceptedPrevChars = new char[]{
			CHAR_DOT,CHAR_EQUALS
	};
	
	private final char[] _acceptedPostChars = new char[]{
			CHAR_PARANTHESIS_OPEN
	};	
	
	private String _functionName = null;
	private List<String> _networkAPI1FunctionList = null;
	private List<String> _networkAPI2FunctionList = null;
	private List<String> _networkAPI3FunctionList = null;
	private int _functionStartLineNum = 0;

	@Override
	protected boolean isLineSplitNeeded(){return true;}

	public NetworkAPIWrapperRule(ProjectConfig codeReviewConfig) {
		super(codeReviewConfig);
		//super.init(PROPS_RULE_NAME,1);
		String[] ruleDescriptions = {ERR_SHORT_DESC};
		super.init(PROPS_RULE_NAME,RULE_ID,ruleDescriptions,1);
		
		init();
	}
	
	public void init() {
		_functionName = null;
		
		if (null == _networkAPI1FunctionList) {
			_networkAPI1FunctionList = new ArrayList<String>();
			_networkAPI2FunctionList = new ArrayList<String>();
			_networkAPI3FunctionList = new ArrayList<String>();
		}
	}
	
	//code for ignoring comments - please write
	@Override
	protected List<ReviewComment> reviewSingleJSLine(String fileName, int lineNum, int lastLineNum, 
			String reviewLine, int passNumber, String currFunctionName, boolean functionEndFlag ,int prevCommentedLinesCount) throws TagToolException {
		

		//final String CHECK_STRING = "Someone else is enrolled on this device and enrolling will unenroll the other customer";
		/*
		if (line.indexOf(CHECK_STRING)>=0) {
			System.out.println("CHECKING : ");
			System.out.println(line);
		}
		*/
		
		String fName = null;
		FunctionVo fnVo = null;
		
		fnVo = getFunctionName(reviewLine);
		fName = fnVo.getFunctionName();
			
			if (fName != null) {
				if (fName.equals(NULL)) {
					_functionName = null;
					_functionStartLineNum = 0;
				} else {
					if (_functionName == null || !_functionName.equals(fName)) {
						_functionName = fName;
						_functionStartLineNum = lineNum;
						//System.out.println("ADDING FUNCTION NAME " + _functionName);
						//myFunctions.add(_functionName);
					}
				}
			}
			
			 if (_functionName != null) {
				 
				 if (isThereNetworkAPIStatement(1, reviewLine) 
						 && !_networkAPI1FunctionList.contains(_functionName)) {
					 _networkAPI1FunctionList.add(fileName + ":" + _functionStartLineNum+ ":" +  _functionName);
				 }
				 
				 if (isThereNetworkAPIStatement(2, reviewLine) 
						 && !_networkAPI2FunctionList.contains(_functionName)) {
					 _networkAPI2FunctionList.add(fileName + ":" + _functionStartLineNum+ ":" +  _functionName);
				 }
	
				 if (isThereNetworkAPIStatement(3, reviewLine) 
						 && !_networkAPI3FunctionList.contains(_functionName)) {
					 _networkAPI3FunctionList.add(fileName + ":" + _functionStartLineNum+ ":" +  _functionName);
				 }
			}
		
		
		return getComments();
}
	
	
	private boolean isThereNetworkAPIStatement(int apiListNumber, String reviewLine) {
		boolean isNetworkApiFound = false;
		int index = 0;
		String[] networkApiList = null;
		
		switch (apiListNumber) {
			case 1: networkApiList = NETWORK_API_LIST1;break;
			case 2: networkApiList = NETWORK_API_LIST2;break;
			case 3: networkApiList = NETWORK_API_LIST3;break;
		}

		if (networkApiList == null || networkApiList.length==0) {
			return false;
		}
		
		for (String keyWord : networkApiList) {
			index = getKeywordIndex(reviewLine, keyWord, _acceptedPrevChars, _acceptedPostChars);
			if (index>=0) {
				isNetworkApiFound = true;
				break;
			}
		}
		
		return isNetworkApiFound;
	}
	
	public void updateComments() {
		
		if (getComments() !=null) {
			getComments().clear();
		}
		String consolidatedFunctions = null;
		
		try {
		if (_networkAPI1FunctionList != null && _networkAPI1FunctionList.size()>1) {
			consolidatedFunctions = BLANK;
			for (String functionName :_networkAPI1FunctionList) {
				consolidatedFunctions+= " , " + functionName;
			}
			addError(ERR_MULTIPLE_WRAPPERS,ERR_SHORT_DESC," Network API :"+NETWORK_API_LIST1_VAL+" ::Functions :"+consolidatedFunctions, 
					"NA", 0, SEV_HIGH, RULE_ID);
		}

		if (_networkAPI2FunctionList != null && _networkAPI2FunctionList.size()>1) {
			consolidatedFunctions = BLANK;
			for (String functionName :_networkAPI2FunctionList) {
				consolidatedFunctions+= " , " + functionName;
			}
			addError(ERR_MULTIPLE_WRAPPERS,ERR_SHORT_DESC," Network API :"+NETWORK_API_LIST2_VAL+" ::Functions :"+consolidatedFunctions, 
					"NA", 0, SEV_HIGH, RULE_ID);
		}

		if (_networkAPI3FunctionList != null && _networkAPI3FunctionList.size()>1) {
			consolidatedFunctions = BLANK;
			for (String functionName :_networkAPI3FunctionList) {
				consolidatedFunctions+= " , " + functionName;
			}
			addError(ERR_MULTIPLE_WRAPPERS,ERR_SHORT_DESC," Network API :"+NETWORK_API_LIST3_VAL+" ::Functions :"+consolidatedFunctions, 
					"NA", 0, SEV_HIGH, RULE_ID);
		}
		
		}catch(TagToolException excp) {
			CodeReviewStatus.getInstance().addErrorMessage(excp, excp.getErrorMessage());
		}
	}
}
