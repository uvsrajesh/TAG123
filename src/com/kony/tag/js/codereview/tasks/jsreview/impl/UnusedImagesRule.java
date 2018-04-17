package com.kony.tag.js.codereview.tasks.jsreview.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.kony.tag.config.CodeReviewStatus;
import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.TagToolException;
import com.kony.tag.js.codereview.config.ReviewComment;
import com.kony.tag.js.codereview.tasks.formreview.impl.UnusedItemsRule;
import com.kony.tag.js.codereview.util.ReviewPropertiesUtil;
import com.kony.tag.js.codereview.util.ReviewUtil;
import com.kony.tag.tasks.JsReviewMasterTask;
import com.kony.tag.util.ProjectPropertiesReader;

public class UnusedImagesRule extends JsReviewMasterTask {
	
	public static final String PROPS_RULE_NAME = UnusedItemsRule.PROPS_RULE_NAME_UNUSED_IMAGES;
	public static final String RULE_ID = UnusedItemsRule.RULE_ID_UNUSED_IMAGES;
	
	public static final String ERR_UNUSED_IMAGE = "Unused Image";
	public static final String ERR_INVALID_IMAGE_NAME = "Rename the Image file. Else it may not be recognized";

	private static final String ERR_SHORT_DESC_INVALID_IMAGE = "Invalid Image Name";
	private static final String ERR_SHORT_DESC_UNUSED_IMAGE = UnusedItemsRule.ERR_SHORT_DESC_IMAGES;
	
	private static final String NOTES_UPPER_CHARS = "Upper case characters found in Image Name";
	private static final String NOTES_FIRST_CHAR = "File Name should start with a lower case alphabet";
	private static final String NOTES_INVALID_CHARS = "Invalid Character found in File Name. Valid Chars: [a-z][0-9][.][_]";
	
	private List<String> _allUnusedImages = null;
	
	private List<String> _tmpImageList = null;
	private final char[] _acceptedPreChars = new char[]{CHAR_EQUALS,CHAR_COLON,CHAR_CURLY_CLOSE,CHAR_CURLY_OPEN, CHAR_SEMI_COLON,CHAR_COMMA,CHAR_DOT,CHAR_PARANTHESIS_OPEN,CHAR_PARANTHESIS_CLOSE,CHAR_DOUBLE_QUOTE,CHAR_SINGLE_QUOTE,CHAR_EXCLAMATION};
	private final char[] _acceptedPostChars = new char[]{CHAR_CURLY_CLOSE,CHAR_CURLY_OPEN, CHAR_SEMI_COLON,CHAR_COMMA,CHAR_DOT,CHAR_PARANTHESIS_OPEN,CHAR_PARANTHESIS_CLOSE,CHAR_DOUBLE_QUOTE,CHAR_SINGLE_QUOTE};
	
	private final String[] projectPropImages = {ProjectPropertiesReader.PROP_APP_LOGOS, ProjectPropertiesReader.PROP_SPLASH_IMAGES};
	
	private List<String> ignoreImageList = ReviewPropertiesUtil.getIgnoreImagesList();

	@Override
	protected boolean isLineSplitNeeded(){return true;}

	public List<String> getAllUnusedImages() {
		return _allUnusedImages;
	}
	
	public UnusedImagesRule(ProjectConfig codeReviewConfig) {
		super(codeReviewConfig,BLANK);
		//super.init(PROPS_RULE_NAME,1);
		String[] ruleDescriptions = {ERR_SHORT_DESC_INVALID_IMAGE,ERR_SHORT_DESC_UNUSED_IMAGE};
		super.init(PROPS_RULE_NAME,RULE_ID,ruleDescriptions,1);
		
		init();
	}
	
	public void init() {
		if(_allUnusedImages == null) {
			ignoreImageList = ReviewPropertiesUtil.getIgnoreImagesList();
			
			_allUnusedImages = new ArrayList<String>();
			String imagesLocation = getProjectConfig().getProjectPath() + PROJECT_RESOURCES_PATH;
			try {
				updateImageList(imagesLocation);
				
				for(int i=0;i<projectPropImages.length;i++) {
					String logos = ProjectPropertiesReader.getProperty(projectPropImages[i]);
					
					if(logos != null) {
						String[] appImages = logos.split(",");
						for(String appImage : appImages) {
							if (_allUnusedImages.contains(appImage)) {
								_allUnusedImages.remove(appImage);
							}
						}
					}
				}
				
			} catch (Exception excp) {
				CodeReviewStatus.getInstance().addErrorMessage(excp, excp.toString());
			}
		}

		if (null == _tmpImageList) {
			_tmpImageList = new ArrayList<String>();
		}
	}
	
	private void updateImageList(String directoryName) throws TagToolException {
		// First Review All JS Files
		ReviewUtil reviewUtil = new ReviewUtil();
		File[] imageFiles = null;
		String fileName = null;
		
		 // This is a JS project. Review All JS Files
		imageFiles = reviewUtil.fetchFiles(directoryName);
			
			if (imageFiles == null || imageFiles.length==0) {
				return;
			}
				
			for(File reviewFile : imageFiles) {
					
				if (reviewFile.isDirectory()) {
					updateImageList(reviewFile.getAbsolutePath());
					continue;
				}
				fileName = 	reviewFile.getName().trim();
				
				// Take out the @2x tag used for retina display
				if (fileName.indexOf("@2x")>=0) {
					fileName = fileName.replaceAll("@2x", BLANK);
				}

				if (!isValidImageName(fileName)) {
					continue;
				}
				
				if (!_allUnusedImages.contains(fileName) && !ignoreImageList.contains(fileName.trim().toLowerCase())) {
					_allUnusedImages.add(fileName);
				}
			}
						
		}
	
	private boolean isValidImageName(String fileName) throws TagToolException {
		String tmpFileName = null;
		boolean isValidFile = false;
		String fileType = null;
		String tmpFileType = null;
		int fileNameLength = 0;
		boolean isValidChar = false;
		char firstChar;
		
		if (fileName == null || fileName.length() <= 4) {
			return isValidFile;
		}
		
		fileNameLength = fileName.length();
		fileType = fileName.substring(fileNameLength-3);
		tmpFileType = fileType.toLowerCase();
		
		if (!(tmpFileType.equals("png") 
				|| tmpFileType.equals("gif")
				|| tmpFileType.equals("jpg")
				|| tmpFileType.equals("jpeg"))) {
			return isValidFile;
		}
		
		// Check is uppper case is used anywhere
		tmpFileName= fileName.toLowerCase();
		
		if (!fileName.equals(tmpFileName)) {
			addError(ERR_INVALID_IMAGE_NAME, ERR_SHORT_DESC_INVALID_IMAGE,NOTES_UPPER_CHARS, fileName, 0, SEV_MED, RULE_ID);
			return isValidFile;
		}
		
		firstChar = fileName.charAt(0);
		if (!(firstChar >= 'a' && firstChar <= 'z')) {
			addError(ERR_INVALID_IMAGE_NAME, ERR_SHORT_DESC_INVALID_IMAGE,NOTES_FIRST_CHAR, fileName, 0, SEV_MED, RULE_ID);
			return isValidFile;
		}
		
		isValidFile = true;
		for (char c : fileName.toCharArray()) {
			isValidChar = false;
			
			if (c >= 'a' && c <= 'z') {
				isValidChar = true;
			} else if (c >= '0' && c <= '9') {
				isValidChar = true;
			} else if (c == '_' || c == '.') {
				isValidChar = true;
			}
			
			if (!isValidChar) {
				addError(ERR_INVALID_IMAGE_NAME, ERR_SHORT_DESC_INVALID_IMAGE, NOTES_INVALID_CHARS, fileName, 0, SEV_MED, RULE_ID);
				isValidFile = false;
				break;
			}
		}
		
		return isValidFile;
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
		
			_tmpImageList.clear();
			fetchImageKeyWords(line,_allUnusedImages);
			
			if (_tmpImageList.size()>0) {
				for (String _imageName : _tmpImageList) {
					// Remove used Functions
					
					if (_allUnusedImages.contains(_imageName)) {
						_allUnusedImages.remove(_imageName);
						//System.out.println("USED SKIN : " + skinName);
					}
				}
			}
		
		return getComments();
}

private void fetchImageKeyWords(String reviewLine, List<String> imagesList) {
		int index = -1;
		String tmpReviewLine = null;
		int imageLength = 0; 

		if (reviewLine == null || reviewLine.length() ==0) {
			return;
		}
		
		if (!(reviewLine.indexOf(".png") >=0 || 
				reviewLine.indexOf(".gif") >=0 ||
				reviewLine.indexOf(".jpg") >=0 ||
				reviewLine.indexOf(".jpeg") >=0)) {
			// Nothing of interest
			return;	
		}
				
			for (String imageName : imagesList) {
				if(reviewLine.indexOf("@2x") >= 0) {
					reviewLine = reviewLine.replace("@2x", BLANK);
				}
				index = getKeywordIndex(reviewLine,imageName,_acceptedPreChars,_acceptedPostChars,false,true);
				if (index>=0) {
					// Found that function is used.  
						_tmpImageList.add(imageName);
					
					imageLength = imageName.length();
					if ((reviewLine.length()>(index+(imageLength-1))) && (index+(imageLength-1)) >=0) {
						
						if (index >0) {
							tmpReviewLine = reviewLine.substring(0,index);
						} else {
							tmpReviewLine = BLANK;
						}
						tmpReviewLine += reviewLine.substring(index+(imageLength));
						fetchImageKeyWords(tmpReviewLine,imagesList);
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
