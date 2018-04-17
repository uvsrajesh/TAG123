package com.kony.tag.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.XMLReader;


import com.kony.tag.codereview.lua.CodeReviewException;
import com.kony.tag.config.WidgetVo;
import com.kony.tag.js.codereview.util.XmlContentHandler;

public class FormUtil {
	
	public void readXML(String konyFormLocation, XmlContentHandler formHandler) throws CodeReviewException{
		
		try {
			
		    SAXParserFactory spf = SAXParserFactory.newInstance();
		    spf.setNamespaceAware(true);
			SAXParser saxParser = spf.newSAXParser();
			XMLReader xmlReader = saxParser.getXMLReader();
			//xmlReader.setContentHandler(new XmlContentHandler());
			xmlReader.setContentHandler(formHandler);
			xmlReader.parse(konyFormLocation);

		} catch (Exception excp) {
			//excp.printStackTrace();
			throw new CodeReviewException("Unable to read " + konyFormLocation + " :: Exception : " + excp.toString());
		}
		
	}

	public void printWidgets(List<WidgetVo> widgets) {
		
	}
}
