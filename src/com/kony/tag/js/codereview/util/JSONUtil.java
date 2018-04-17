package com.kony.tag.js.codereview.util;

import java.util.ArrayList;
import java.util.List;

import com.kony.tag.config.ProjectConstants;
import com.kony.tag.js.codereview.config.JSONObject;
import com.kony.tag.js.codereview.config.KeyValuePair;

public class JSONUtil implements ProjectConstants{
	
	public JSONObject fetchJSONObject(String jsonString) {
		JSONObject jsonObject = null;
		List<KeyValuePair> keyValuePairs = null;
		List<KeyValuePair> jsonKeyValuePairs = null;
		String value = null;
		int valueType = 0;
		
		keyValuePairs = fetchKeyValuePairs(jsonString);
		
		if (null == keyValuePairs || keyValuePairs.size()==0) {
			return null;
		}
		
		jsonObject = new JSONObject();
		jsonKeyValuePairs = new ArrayList<KeyValuePair>();
		
		for (KeyValuePair keyValuePair : keyValuePairs) {
			value = keyValuePair.getValue();
			valueType = fetchValueType(value);
			keyValuePair.setValueType(valueType);
			
			if (valueType == KeyValuePair.VALUE_TYPE_ARRAY) {
				keyValuePair.setValues(fetchArrayValues(value));
			} else if (valueType == KeyValuePair.VALUE_TYPE_JSON_OBJ) {
				keyValuePair.setJsonObject(fetchJSONObject(value));
			}
			jsonKeyValuePairs.add(keyValuePair);
		}
		
		jsonObject.setKeyValuePairs(jsonKeyValuePairs);
		jsonObject.setJsonString(jsonString);
		
		return jsonObject;
	}
	
	
	private List<String> fetchArrayValues(String arrayString) {
		List<String> values = null;
		StringBuffer value = null;
		int singleQuoteCount = 0;
		int doubleQuoteCount = 0;
		char[] mychars = null;
		boolean isArrayStarted = false;
		boolean isWithinDoubleQuotes = false;
		boolean isWithinSingleQuotes = false;
		boolean isWithinQuotes = false;

		
		if (null == arrayString || arrayString.trim().length() == 0) {
			return null;
		}
		
		values = new ArrayList<String>();
		value = new StringBuffer();
		mychars = arrayString.toCharArray();
		
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
			
			if (c==CHAR_SQUARE_OPEN && !isWithinQuotes) {
				isArrayStarted = true;
				continue;
			}
			
			if (!isArrayStarted) {
				continue;
			}
			if ((c==CHAR_COMMA && !isWithinQuotes)) {
				if (null != value && value.toString().trim().length()>0) {
					values.add(value.toString().trim());
				}
				value = new StringBuffer();
				continue;
			}
			
			if (c==CHAR_SQUARE_CLOSE || c==CHAR_SQUARE_OPEN) {
				continue;
			}
			
			value.append(c);
		}
		
		if (value != null && value.toString().trim().length() == 0) {
			values.add(value.toString().trim());
		}
		
		return values;
	}
	
	private int fetchValueType(String value) {
		int type = KeyValuePair.VALUE_TYPE_STRING;
		String val = value.trim();
		char firstChar;
		char lastChar;
		
		if (null == value || value.trim().length() == 0) {
			return type;
		}
		
		int size = value.length();
		
		if (size>=2) {
			firstChar = val.charAt(0);
			lastChar = val.charAt(size-1);
			
			if (firstChar == CHAR_CURLY_OPEN && lastChar == CHAR_CURLY_CLOSE) {
				type = KeyValuePair.VALUE_TYPE_JSON_OBJ;
			} else if (firstChar == CHAR_SQUARE_OPEN && lastChar == CHAR_SQUARE_CLOSE) {
				type = KeyValuePair.VALUE_TYPE_ARRAY;
			}
		}
		
		return type;
	}
	
	private List<KeyValuePair> fetchKeyValuePairs(String jsonString) {
		List<KeyValuePair> keyValuePairs = new ArrayList<KeyValuePair>();
		KeyValuePair keyValuePair = null;
		char[] mychars = null;
		StringBuffer key = null;
		StringBuffer value = null;
		String val;
		boolean isJsonStarted = false;
		boolean isWithinDoubleQuotes = false;
		boolean isWithinSingleQuotes = false;
		boolean isWithinQuotes = false;
		boolean isReadingValue = false;
		int openBracesCount = 0; 
		int singleQuoteCount = 0;
		int doubleQuoteCount = 0;
		int squareBracesCount = 0;
		char c;
		
		if (null == jsonString || jsonString.trim().length() == 0) {
			return null;
		}
		
		mychars = jsonString.toCharArray();
		key = new StringBuffer();
		value = new StringBuffer();
		keyValuePair = new KeyValuePair();
		
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
			
			if (c == CHAR_SQUARE_OPEN && !isWithinQuotes) {
				squareBracesCount++;
			}

			if (c == CHAR_SQUARE_CLOSE && !isWithinQuotes) {
				squareBracesCount--;
			}

			
			if (c == CHAR_CURLY_OPEN && !isWithinQuotes) {
				openBracesCount++;
				if (!isJsonStarted) {
					isJsonStarted = true;
					continue;
				}
			}

			if (c == CHAR_CURLY_CLOSE && !isWithinQuotes && isJsonStarted) {
				openBracesCount--;
			}

			if (c == CHAR_COLON && !isWithinQuotes) {
				isReadingValue = true;
				//continue;
			}	
			
			if ((c == CHAR_COMMA && !isWithinQuotes && squareBracesCount==0 && openBracesCount==1) || (c == CHAR_CURLY_CLOSE && openBracesCount==0)) {
				
				
				if (null != key && key.toString().trim().length()>0 ) {
					keyValuePair.setKey(key.toString().trim());
					val = BLANK;
					if (value != null) {
						val = value.toString().trim();
						
						if (val.length()>1 && val.charAt(0) == CHAR_COLON) {
							val = val.substring(1).trim();
						}
					}
					keyValuePair.setValue(val);
					keyValuePairs.add(keyValuePair);
				}
				
				key = new StringBuffer();
				value = new StringBuffer();
				keyValuePair = new KeyValuePair();
				isReadingValue = false;
				singleQuoteCount = 0;
				doubleQuoteCount = 0;
				squareBracesCount = 0;
				
				if (c==CHAR_COMMA && !isWithinQuotes) {
					// Read Next Key Value Pair
					continue;
				}

				if (c==CHAR_CURLY_CLOSE && openBracesCount ==0) {
					// Done reading the JSON String
					break;
				}
			}
			
			if (!isReadingValue && c != CHAR_SINGLE_QUOTE && 
					c != CHAR_DOUBLE_QUOTE && !Character.isWhitespace(c)) {
				key.append(c);
			}
			
			if (isReadingValue) {
				value.append(c);
			}
		}
		return keyValuePairs;
	}
}
