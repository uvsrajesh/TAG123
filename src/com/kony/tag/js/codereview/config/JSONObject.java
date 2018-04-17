package com.kony.tag.js.codereview.config;

import java.util.List;

public class JSONObject {
	
	private List<KeyValuePair> keyValuePairs;
	String jsonString;
	
	public List<KeyValuePair> getKeyValuePairs() {
		return keyValuePairs;
	}
	public void setKeyValuePairs(List<KeyValuePair> keyValuePairs) {
		this.keyValuePairs = keyValuePairs;
	}
	public String getJsonString() {
		return jsonString;
	}
	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}
	
	
	
}
