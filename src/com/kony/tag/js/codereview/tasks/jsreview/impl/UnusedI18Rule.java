package com.kony.tag.js.codereview.tasks.jsreview.impl;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import com.kony.tag.config.CodeReviewStatus;
import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.TagToolException;
import com.kony.tag.js.codereview.config.ReviewComment;
import com.kony.tag.js.codereview.tasks.formreview.impl.UnusedItemsRule;
import com.kony.tag.js.codereview.util.ReviewPropertiesUtil;
import com.kony.tag.tasks.JsReviewMasterTask;
import com.kony.tag.util.ProjectPropertiesReader;

public class UnusedI18Rule extends JsReviewMasterTask {
	
	public static final String PROPS_RULE_NAME = UnusedItemsRule.PROPS_RULE_NAME_UNUSED_I18;
	public static final String RULE_ID = UnusedItemsRule.RULE_ID_UNUSED_I18_KEYS;
	
	public static final String ERR_UNUSED_SKIN = "Unused i18 key";
	private static final String ERR_SHORT_DESC = UnusedItemsRule.ERR_SHORT_DESC_I18;
	
	private List<String> _allUnusedI18Keys = null;
	
	private List<String> _tmpI18KeysList = null;
	
	private final char[] _acceptedPreChars = new char[]{CHAR_EQUALS,CHAR_COLON,CHAR_CURLY_CLOSE,CHAR_CURLY_OPEN, CHAR_SEMI_COLON,CHAR_COMMA,CHAR_DOT,CHAR_PARANTHESIS_OPEN,CHAR_PARANTHESIS_CLOSE,CHAR_DOUBLE_QUOTE,CHAR_SINGLE_QUOTE,CHAR_EXCLAMATION};
	private final char[] _acceptedPostChars = new char[]{CHAR_CURLY_CLOSE,CHAR_CURLY_OPEN, CHAR_SEMI_COLON,CHAR_COMMA,CHAR_DOT,CHAR_PARANTHESIS_OPEN,CHAR_PARANTHESIS_CLOSE,CHAR_DOUBLE_QUOTE,CHAR_SINGLE_QUOTE};
	
	
	private List<String> ignoreI18nkeysList = ReviewPropertiesUtil.getIgnoreI18nkeysList();
	
	@Override
	protected boolean isLineSplitNeeded(){return true;}

	public List<String> getAllUnusedI18Keys() {
		return _allUnusedI18Keys;
	}
	
	public UnusedI18Rule(ProjectConfig codeReviewConfig) {
		super(codeReviewConfig,BLANK);
		//super.init(PROPS_RULE_NAME,1);
		String[] ruleDescriptions = {ERR_SHORT_DESC};
		super.init(PROPS_RULE_NAME,RULE_ID,ruleDescriptions,1);
		
		init();
	}
	
	public void init() {
		
		if (null == _tmpI18KeysList) {
			_tmpI18KeysList = new ArrayList<String>();
		}
		
		if (null == _allUnusedI18Keys) {
			ignoreI18nkeysList = ReviewPropertiesUtil.getIgnoreI18nkeysList();
			String defaultLocale = ProjectPropertiesReader.getProperty(ProjectPropertiesReader.PROP_DEFAULT_LOCALE);
			
			if (null == defaultLocale || defaultLocale.trim().length()==0) {
				CodeReviewStatus.getInstance().addErrorMessage("No Default Locale Specfied. Did not check Unused I18 Keys");
				reset();
				return;
			}
			
			String defaultI18LocalePropsFile = 
				getProjectConfig().getProjectPath() + 
				DEFAULT_I18_PROPS_PATH + 
				ProjectPropertiesReader.getProperty(ProjectPropertiesReader.PROP_DEFAULT_LOCALE)
				+ ".prop";
			
			try {
				Properties props = new Properties();
				File propsFile = new File(defaultI18LocalePropsFile);
				FileInputStream fos = new FileInputStream(propsFile);
				props.load(fos);
				
				Enumeration<Object> keysEnum = props.keys();
				
				if (keysEnum == null) {
					_allUnusedI18Keys = null;
					return;
				} else {
					_allUnusedI18Keys = new ArrayList<String>();
				}
				
				while(keysEnum.hasMoreElements()) {
					String i18nKey = keysEnum.nextElement().toString();
					if(!ignoreI18nkeysList.contains(i18nKey.trim().toLowerCase())) {
						_allUnusedI18Keys.add(i18nKey);
					}
				}
				
			} catch(Exception excp) {
				CodeReviewStatus.getInstance().addErrorMessage(excp,"Unable to read i18n properties file for default locale. Expected File Location : " + defaultI18LocalePropsFile + "  ERROR : " + excp);
				reset();
			}
			
			finally{
				if (_allUnusedI18Keys == null || _allUnusedI18Keys.size()==0) {
					reset();
				}
			}

		}
	}
	
	private void reset(){
		_allUnusedI18Keys = null;
		setEnabled(false);
	}
	
	//code for ignoring comments - please write
	@Override
	protected List<ReviewComment> reviewSingleJSLine(String fileName, int lineNum, int lastLineNum, String line, int passNumber, String currFunctionName, boolean functionEndFlag ,int prevCommentedLinesCount) throws TagToolException {

		/*		
		final String CHECK_STRING = "dashboardOnClick";
		
		if (line.indexOf(CHECK_STRING)>=0 && passNumber == 2) {
			System.out.println("CHECKING : ");
			System.out.println(line);
		}
		*/
		
			_tmpI18KeysList.clear();
			fetchI18KeyWords(line,_allUnusedI18Keys);
			
			if (_tmpI18KeysList.size()>0) {
				for (String keyName : _tmpI18KeysList) {
					// Remove used Functions
					
					if (_allUnusedI18Keys.contains(keyName)) {
						_allUnusedI18Keys.remove(keyName);
						//System.out.println("USED SKIN : " + skinName);
					}
				}
			}
		
		return getComments();
}

private void fetchI18KeyWords(String reviewLine, List<String> i18KeysList) {
		int index = -1;
		String tmpReviewLine = null;
		int keyLength = 0; 

		if (reviewLine == null || reviewLine.length() ==0) {
			return;
		}
				
			for (String keyName : i18KeysList) {
				index = getKeywordIndex(reviewLine,keyName,_acceptedPreChars,_acceptedPostChars,false,true);
				if (index>=0) {
					// Found that function is used.  
						_tmpI18KeysList.add(keyName);
					
					keyLength = keyName.length();
					if ((reviewLine.length()>(index+(keyLength-1))) && (index+(keyLength-1)) >=0) {
						
						if (index >0) {
							tmpReviewLine = reviewLine.substring(0,index);
						} else {
							tmpReviewLine = BLANK;
						}
						tmpReviewLine += reviewLine.substring(index+(keyLength));
						fetchI18KeyWords(tmpReviewLine,i18KeysList);
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
			// Ignore all key words within
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
