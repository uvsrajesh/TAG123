package com.kony.tag.js.codereview.tasks.jsreview.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.kony.tag.config.CodeReviewStatus;
import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.TagToolException;
import com.kony.tag.js.codereview.config.ReviewComment;
import com.kony.tag.js.codereview.tasks.formreview.impl.UnusedItemsRule;
import com.kony.tag.js.codereview.util.KonySkinsXmlHandler;
import com.kony.tag.js.codereview.util.ReviewUtil;
import com.kony.tag.tasks.JsReviewMasterTask;
import com.kony.tag.util.FormUtil;

public class UnusedSkinsRule extends JsReviewMasterTask {
	
	public static final String PROPS_RULE_NAME = UnusedItemsRule.PROPS_RULE_NAME_UNUSED_SKINS;
	public static final String RULE_ID = UnusedItemsRule.RULE_ID_UNUSED_SKINS;
	
	//public static final String ERR_UNUSED_SKIN = "Unused Skin";
	private static final String ERR_SHORT_DESC = UnusedItemsRule.ERR_SHORT_DESC_SKINS;
	
	private List<String> _allUnusedSkins = null;
	private Map<String, List<String>> _imagesUsedInSkins = null;
	
	private List<String> _tmpSkinList = null;
	private final char[] _acceptedPreChars = new char[]{CHAR_EQUALS,CHAR_COLON,CHAR_CURLY_CLOSE,CHAR_CURLY_OPEN, CHAR_SEMI_COLON,CHAR_COMMA,CHAR_DOT,CHAR_PARANTHESIS_OPEN,CHAR_PARANTHESIS_CLOSE,CHAR_DOUBLE_QUOTE,CHAR_SINGLE_QUOTE,CHAR_EXCLAMATION};
	private final char[] _acceptedPostChars = new char[]{CHAR_CURLY_CLOSE,CHAR_CURLY_OPEN, CHAR_SEMI_COLON,CHAR_COMMA,CHAR_DOT,CHAR_PARANTHESIS_OPEN,CHAR_PARANTHESIS_CLOSE,CHAR_DOUBLE_QUOTE,CHAR_SINGLE_QUOTE};
	
	private KonySkinsXmlHandler konySkinsXmlHandler = new KonySkinsXmlHandler();
	private FormUtil formUtil = new FormUtil();


	@Override
	protected boolean isLineSplitNeeded(){return true;}

	public List<String> getAllUnusedSkins() {
		return _allUnusedSkins;
	}
	
	public Map<String, List<String>> getImagesUsedInSkins() {
		return _imagesUsedInSkins;
	}
	
	public UnusedSkinsRule(ProjectConfig codeReviewConfig) {
		super(codeReviewConfig,BLANK);
		//super.init(PROPS_RULE_NAME,1);
		String[] ruleDescriptions = {ERR_SHORT_DESC};
		super.init(PROPS_RULE_NAME,RULE_ID,ruleDescriptions,1);
		
		init();
	}
	
	public void init() {
		
		if (null == _allUnusedSkins) {
			String projectSkinsFileLocation = getProjectConfig().getProjectPath() + DEFAULT_THEMES_PATH + getProjectConfig().getCodeReviewProperty(PROP_DEFAULT_THEME_FOR_SKINS); 
			konySkinsXmlHandler = new KonySkinsXmlHandler();
			formUtil = new FormUtil();
			konySkinsXmlHandler.init();

			readAllSkins(projectSkinsFileLocation);

			_allUnusedSkins = konySkinsXmlHandler.getAllSkinNames();
			_imagesUsedInSkins = konySkinsXmlHandler.getAllImageNames();
		}

		if (null == _tmpSkinList) {
			_tmpSkinList = new ArrayList<String>();
		}

	}
	
	private void readAllSkins(String directoryName) {
		// First Review All JS Files
		ReviewUtil reviewUtil = new ReviewUtil();
		File[] skinFiles = null;
		String fileName = null;
		
		File file = new File(directoryName);
		
		if (!file.exists()) {
			CodeReviewStatus.getInstance().addErrorMessage("Unable to read project skins from the folder : " + directoryName);
			return;
		}
			
		 // This is a JS project. Review All JS Files
		skinFiles = reviewUtil.fetchFiles(directoryName);
			
			if (skinFiles == null || skinFiles.length==0) {
				return;
			}
				
			for(File reviewFile : skinFiles) {
				
					
				if (reviewFile.isDirectory()) {
					readAllSkins(reviewFile.getAbsolutePath());
					continue;
				}
				fileName = 	reviewFile.getName().trim();
				
				// Take out the @2x tag used for retina display
				if (!fileName.toLowerCase().endsWith(".xml")) {
					continue;
				}

				
				try {
					formUtil.readXML(reviewFile.getAbsolutePath(), konySkinsXmlHandler);
					
					// Wait till the file is read completely
					while(!konySkinsXmlHandler.isEndOfFileReached()) {};
				} catch (Exception excp) {
					CodeReviewStatus.getInstance().addErrorMessage(excp, excp.toString());
				}
			}		
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
		
			_tmpSkinList.clear();
			fetchSkinKeyWords(line,_allUnusedSkins);
			
			if (_tmpSkinList.size()>0) {
				for (String skinName : _tmpSkinList) {
					// Remove used Functions
					
					if (_allUnusedSkins.contains(skinName)) {
						_allUnusedSkins.remove(skinName);
						//System.out.println("USED SKIN : " + skinName);
					}
				}
			}
		
		return getComments();
}

private void fetchSkinKeyWords(String reviewLine, List<String> skinsList) {
		int index = -1;
		String tmpReviewLine = null;
		int skinLength = 0; 

		if (reviewLine == null || reviewLine.length() ==0) {
			return;
		}
				
			for (String skinName : skinsList) {
				index = getKeywordIndex(reviewLine,skinName,_acceptedPreChars,_acceptedPostChars,false,true);
				if (index>=0) {
					// Found that function is used.  
						_tmpSkinList.add(skinName);
					
					skinLength = skinName.length();
					if ((reviewLine.length()>(index+(skinLength-1))) && (index+(skinLength-1)) >=0) {
						
						if (index >0) {
							tmpReviewLine = reviewLine.substring(0,index);
						} else {
							tmpReviewLine = BLANK;
						}
						tmpReviewLine += reviewLine.substring(index+(skinLength));
						fetchSkinKeyWords(tmpReviewLine,skinsList);
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
