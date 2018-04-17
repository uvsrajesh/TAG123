package com.kony.tag.js.codereview.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class KonySkinsXmlHandler extends XmlContentHandler {
	
	private List<String> allSkinNames = null;
	private Map<String, List<String>> allImageNames = null;
	private String mSkinName = null;
	private List<String> ignoreSkinsList = ReviewPropertiesUtil.getIgnoreSkinsList();

	public Map<String, List<String>> getAllImageNames() {
		return allImageNames;
	}

	public List<String> getAllSkinNames() {
		return allSkinNames;
	}

	public void init() {
		super.init();
		if (allSkinNames == null) {
			allSkinNames = new ArrayList<String>();
		}
		
		if (allImageNames == null) {
			allImageNames = new HashMap<String, List<String>>();
		}
		
		mSkinName = null;
		allSkinNames.clear();
		allImageNames.clear();
		ignoreSkinsList = ReviewPropertiesUtil.getIgnoreSkinsList();
	}
	
	@Override
	public void characters(char ch[], int start, int length) throws SAXException {
		//Nothing to read. Kony skins.xml will not have any content
	}

	@Override
	public void endElement(String uri, String localName,
			String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		//Nothing to read.
	}

	@Override
	public void startElement(String uri, String localName,String qName, 
            Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		
		//int attributeCount = attributes.getLength();
		String key = null;
		String value = null;
		
		key = attributes.getValue("name");
		value = attributes.getValue("value");
		
		if (key != null && key.equalsIgnoreCase("Name")) {
			mSkinName = new String(value);
			
			if (!allSkinNames.contains(value) && !ignoreSkinsList.contains(value.trim().toLowerCase())) {
				allSkinNames.add(value);
			}
		}
		
		if (value != null) {
			value = value.trim();
			
			if (value.length() >4) {
				if (value.toLowerCase().endsWith(".png") || 
						value.toLowerCase().endsWith(".gif") ||
						value.toLowerCase().endsWith(".jpg") ||
						value.toLowerCase().endsWith(".jpeg")) {
					if (!allImageNames.containsKey(value)) 
					{
						List<String> lSkinList = new ArrayList<String>();
						lSkinList.add(mSkinName);
						allImageNames.put(value, lSkinList);
					}
					else
					{
						List<String> lSkinList = allImageNames.get(value);
						if(!lSkinList.contains(mSkinName))
							lSkinList.add(mSkinName);
					}
				}
			}
		}
		//sb.append(" :: " + attributes.getQName(i) + " : " + attributes.getValue(i));
		//System.out.println("Start Element :" + qName + "   Attributes :: " + sb.toString());		
	}

}
