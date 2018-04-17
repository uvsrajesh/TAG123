package com.kony.tag.js.codereview.util;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

public class XmlContentHandler implements ContentHandler {
	
	private boolean isEndOfFileReached = false;
	
	public boolean isEndOfFileReached() {
		return isEndOfFileReached;
	}
	
	public void init() {
		isEndOfFileReached = false;
	}


	@Override
	public void characters(char ch[], int start, int length) throws SAXException {
		//System.out.println("Attribute : " + new String(ch, start, length));
	}

	@Override
	public void endElement(String uri, String localName,
			String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		//System.out.println("End Element :" + qName);
	}

	@Override
	public void startElement(String uri, String localName,String qName, 
            Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		int attributeCount = attributes.getLength();
		
		for (int i=0;i<attributeCount;i++) {
			sb.append(" :: " + attributes.getType(i) + " : " + attributes.getValue(i));
		}
		//System.out.println("Start Element :" + qName + "   Attributes :: " + sb.toString());		
	}

	@Override
	public void endDocument() throws SAXException {
		// Review the Form here
		isEndOfFileReached = true;
	}

	@Override
	public void endPrefixMapping(String arg0) throws SAXException {
		// TODO Auto-generated method stub
		//System.out.println("END PREFIX MAPPING : " + arg0);
	}

	@Override
	public void ignorableWhitespace(char[] arg0, int arg1, int arg2)
			throws SAXException {
		// TODO Auto-generated method stub
		//System.out.println("ignorableWhitespace : " + new String(arg0, arg1, arg2));
	}

	@Override
	public void processingInstruction(String arg0, String arg1)
			throws SAXException {
		// TODO Auto-generated method stub
		//System.out.println("PROCESSING INSTRUCTION : " + arg0 + " : "+arg1);
	}

	@Override
	public void setDocumentLocator(Locator arg0) {
		// TODO Auto-generated method stub
		//System.out.println("Setting Locator : " + arg0.getSystemId());
	}

	@Override
	public void skippedEntity(String arg0) throws SAXException {
		// TODO Auto-generated method stub
		//System.out.println("SKIPPED: " + arg0);

	}

	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		//System.out.println("START DOCUMENT");
		isEndOfFileReached = false;
	}
	
	@Override
	public void startPrefixMapping(String arg0, String arg1)
			throws SAXException {
		// TODO Auto-generated method stub
		//System.out.println("START PREFIX MAPPING : " + arg0 + " : "+arg1);
	}
}
