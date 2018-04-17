package com.kony.tag.js.codereview.config;

import java.util.List;

public class KeyValuePair {
	public final static int VALUE_TYPE_STRING = 1001;
	public final static int VALUE_TYPE_ARRAY = 1002;
	public final static int VALUE_TYPE_JSON_OBJ = 1003;

	private String key;
	private String value;
	private List<String> values;
	private JSONObject jsonObject;
	private int valueType;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	public List<String> getValues() {
		return values;
	}
	public void setValues(List<String> values) {
		this.values = values;
	}
	
	public JSONObject getJsonObject() {
		return jsonObject;
	}
	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}

	public int getValueType() {
		return valueType;
	}
	public void setValueType(int valueType) {
		this.valueType = valueType;
	}
}
