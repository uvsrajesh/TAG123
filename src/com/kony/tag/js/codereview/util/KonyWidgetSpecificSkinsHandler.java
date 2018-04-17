package com.kony.tag.js.codereview.util;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class KonyWidgetSpecificSkinsHandler extends XmlContentHandler {
	
	private List<String> allButtonSkinsWithImages = null;
	private String currentSkin = null;
	private boolean isImageBackground = false;
	private String currentSkinWidgetType = null;
	
	public List<String> getAllButtonSkinsWithImages() {
		return allButtonSkinsWithImages;
	}

	public void init() {
		super.init();
		if (allButtonSkinsWithImages == null) {
			allButtonSkinsWithImages = new ArrayList<String>();
		}
		
		allButtonSkinsWithImages.clear();
		currentSkin = null;
		isImageBackground = false;
		currentSkinWidgetType = null;
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
		
		if (qName != null && qName.equalsIgnoreCase("skin")) {
			if (null != currentSkin && null != currentSkinWidgetType 
					&& currentSkinWidgetType.equalsIgnoreCase("Button") && isImageBackground
					&& !allButtonSkinsWithImages.contains(currentSkin)) { 

					allButtonSkinsWithImages.add(currentSkin);
					currentSkin = null;
					isImageBackground = false;
					currentSkinWidgetType = null;
			}
		}
	}

	@Override
	public void startElement(String uri, String localName,String qName, 
            Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		String key = null;
		String value = null;
		
		key = attributes.getValue("name");
		value = attributes.getValue("value");
		
		if (key != null && key.equalsIgnoreCase("Name")) {
			currentSkin = value;
			isImageBackground = false;
			currentSkinWidgetType = null;
		} else if (key != null && key.equalsIgnoreCase("WidgetType")) {
			isImageBackground = false;
			currentSkinWidgetType = value;
		} else if (!isImageBackground && key != null && key.toLowerCase().indexOf("_image")>=0) {
			if (value != null && value.toLowerCase().trim().length()>0)
				value = value.toLowerCase();
			if (value.endsWith(".png") 
					|| value.endsWith(".gif")
					|| value.endsWith(".jpg")
					|| value.endsWith(".jpeg")) {
					isImageBackground = true;
				}
		}
		
		//System.out.println("Start Element :" + qName + "   Attributes :: " + sb.toString());		
	}

}
