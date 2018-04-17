package com.kony.tag.codereview.lua;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.eclipse.swt.widgets.Shell;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/* Author : TAG Group
 */

public class CleanSkins
{	
	public static void main(Shell shell)
	{	
		Writer unUsedSkinsWriter = null;
		Writer notDefinedSkinsWriter = null;
		File unUsedSkinsFile = new File(Util.Output_DIR+"\\UnUsedSkins.csv");
		File notDefinedSkinsFile = new File(Util.Output_DIR+"\\NotDefinedSkins.csv");
		
		try
		{
			long startTime = System.currentTimeMillis();
			Util.printToConsole("Getting all the skin names .......... ");
			ArrayList<String> allSkinList = getSkinList();
			Util.printToConsole("Getting all the used skin names .......... ");
			Set<String> usedSkinsList = getUsedSkins(allSkinList);
			Util.printToConsole("Getting details of additional skins used which are not defined .......... ");
			ArrayList<String> notDefinedSkinsList = findAdditionalSkinsUsed(allSkinList,usedSkinsList);
			Util.printToConsole("Getting details of the un-defined skins .......... ");
			ArrayList<String> unUsedSkinsList = findUnUsedSkins(allSkinList,usedSkinsList);
			
			
			unUsedSkinsWriter = new BufferedWriter(new FileWriter(unUsedSkinsFile, false));
			for (int i =0; i<unUsedSkinsList.size();i++)
			{
					unUsedSkinsWriter.append(unUsedSkinsList.get(i));
			    	unUsedSkinsWriter.flush();
			}
			
			notDefinedSkinsWriter = new BufferedWriter(new FileWriter(notDefinedSkinsFile, false));
			for(int y=0; y<notDefinedSkinsList.size();y++)
			{
				notDefinedSkinsWriter.append(notDefinedSkinsList.get(y));
		    	notDefinedSkinsWriter.flush();
			}
			
			Util.printToConsole("Total number of skins are "+allSkinList.size());
			Util.printToConsole("Number of un-used skins are "+unUsedSkinsList.size());
			Util.printToConsole("Number of not defined skins are "+notDefinedSkinsList.size());
			//CleanSkins.deleteUnusedSkins(usedSkinsList);
			
			long endTime = System.currentTimeMillis();
			Util.printToConsole("Total Time Taken for Processing to find the un-used/not defined skins is ...... "+(endTime-startTime)+" ms");
		}
		catch (IOException e) 
		{
			e.printStackTrace();
			Util.printToConsole("Encountered difficulties while cleaning skins due to following reason: " + e.getMessage() + ". \nContinuing the script with other tasks.");
			//Util.printToConsole(Util.stackTraceToString(e)); 
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			Util.printToConsole("Encountered difficulties while cleaning skins due to following reason: " + e.getMessage() + ". \nContinuing the script with other tasks.");
			//Util.printToConsole(Util.stackTraceToString(e)); 			
		}
		finally
		{
			if(unUsedSkinsWriter!=null)
			{
				try 
				{
					unUsedSkinsWriter.close();
				}
				catch (IOException e) 
				{
					e.printStackTrace();
					Util.printToConsole("Encountered difficulties while cleaning skins due to following reason: " + e.getMessage() + ". \nContinuing the script with other tasks.");
					//Util.printToConsole(Util.stackTraceToString(e));
				}
			}
			
			if(notDefinedSkinsWriter!=null)
			{
				try 
				{
					notDefinedSkinsWriter.close();
				}
				catch (IOException e) 
				{
					e.printStackTrace();
					Util.printToConsole("Encountered difficulties while cleaning skins due to following reason: " + e.getMessage() + ". \nContinuing the script with other tasks.");
					//Util.printToConsole(Util.stackTraceToString(e));
				}
			}
		}
	}
	
	private static ArrayList<String> findUnUsedSkins(ArrayList<String> allSkinList,Set<String> usedSkinsList) 
	{
		ArrayList<String> unUsedSkinsList = new ArrayList<String>();
		for (String skin : allSkinList) 
		{
			if(!usedSkinsList.contains(skin))
			{
				unUsedSkinsList.add(skin+"\n");
			}
		}
		
		return unUsedSkinsList;
	}

	private static ArrayList<String> findAdditionalSkinsUsed(ArrayList<String> allSkinList, Set<String> usedSkinsList) 
	{
		ArrayList<String> notDefinedSkinsList = new ArrayList<String>();
		for (String skin : usedSkinsList) 
		{
			if(!allSkinList.contains(skin))
			{
				notDefinedSkinsList.add(skin+"\n");
			}
		}
		
		return notDefinedSkinsList;
	}

	public static Set<String> getUsedSkins(ArrayList<String> allSkinList) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException
	{		
		Set<String> usedSkinList = new TreeSet<String>();
		
		LineNumberReader br = null;
		String line = null;
		File tempCodeDirFile = new File(Util.tempCodeDir);
		File[] allTempFiles = tempCodeDirFile.listFiles(new SVNFileFilter());
		
		ArrayList<String> tmpAllSkinList = new ArrayList<String>();
		for(String skinName:allSkinList)
		{
			tmpAllSkinList.add(skinName);
		}

		for(File eachFile : allTempFiles) 
		{				
			br = new LineNumberReader(new FileReader(eachFile));
			ArrayList<String> tmpUsedList = new ArrayList<String>();
			while(null != (line = br.readLine()))
			{
				int skinListSize = tmpAllSkinList.size();
				for(int k=0;k<skinListSize;k++)
				{
					String skinName = tmpAllSkinList.get(k).trim();
					if(line.contains(skinName))
					{
						usedSkinList.add(skinName);	
						tmpUsedList.add(skinName); // Remove the used skin from the list for the verification from next file onwards.
					}
				}
			}
			
			tmpAllSkinList.removeAll(tmpUsedList);
			tmpUsedList = null;
			
			if(br != null)
				br.close();
		}
		
		return usedSkinList;		
	}


    public static ArrayList<String> getSkinList() throws ParserConfigurationException, SAXException, IOException, XPathExpressionException
    {
    	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		ArrayList<String> skinList = new ArrayList<String>();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document dom = null ;
		
		File skinFile = new File(Util.projectPath+"\\skin.xml");  
		if(skinFile.exists())
		{
			dom = db.parse(Util.projectPath+"skin.xml");
		}
		else
		{
			dom = db.parse(Util.projectPath+"themes\\default\\skin.xml");
		}
	
		XPath xpath = XPathFactory.newInstance().newXPath();
		XPathExpression expr = xpath.compile("//skin//att[@name='Name']");
		
		Object result = expr.evaluate(dom,XPathConstants.NODESET);
		NodeList nodes = (NodeList)result;
		int length = nodes.getLength();
		for (int i = 0; i < length; i++) 
		{
			skinList.add(nodes.item(i).getAttributes().getNamedItem("value").getNodeValue().trim());
		}
		
		return skinList;
    }

    public static void deleteUnusedSkins(ArrayList<String> usedSkins) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException, TransformerException
    {
    	ArrayList<String> ListUsedSkins = usedSkins;
    	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    	DocumentBuilder db = dbf.newDocumentBuilder();
		Document tempSkin,skin;
		
		boolean skinsBackupDirExists = new File(Util.projectPath+"SkinsBackUp").exists();
		if (skinsBackupDirExists == true)
		{
			boolean skinsBackupIsDir = new File(Util.projectPath+"SkinsBackUp").isDirectory();
			if(skinsBackupIsDir == true)
			{
				File[] allBackUpFiles = new File(Util.projectPath+"SkinsBackUp").listFiles();
				for(File eachBackUpFile : allBackUpFiles)
				{
					boolean eachFileDeleted = eachBackUpFile.delete();
				}
			}
			
			boolean skinsBackupDeleted = new File(Util.projectPath+"SkinsBackUp").delete();
			boolean skinsBackupDirCreated = new File(Util.projectPath+"SkinsBackUp").mkdir();
		}
		else
		{
			boolean skinsBackupDirCreated = new File(Util.projectPath+"SkinsBackUp").mkdir();
		}
		
		String[] skins = {"skin","iphoneskin","androidskin","xhtmladvandroidskin","xhtmladvbbskin","xhtmladviphoneskin","xhtmladvpalmskin","xhtmlbjsskin","xhtmllargeskin"};
		
		for(String eachSkin : skins)
		{
			skin = null;
			tempSkin = null;
			
			skin = db.parse(Util.projectPath+""+eachSkin+".xml");
			File skinFile = new File(Util.projectPath+"\\skin.xml");
			if(skinFile.exists())
			{
				tempSkin = db.parse(Util.projectPath+"skin.xml");
			}
			else
			{
				tempSkin = db.parse(Util.projectPath+"themes\\default\\skin.xml");
			}
			
			XPath xpath = XPathFactory.newInstance().newXPath();
			XPathExpression expr = xpath.compile("//skin//att");
			Object result = expr.evaluate(skin,XPathConstants.NODESET);
			NodeList nodes = (NodeList)result;
			Node list = tempSkin.getDocumentElement();
			for (int i = 0; i < nodes.getLength(); i++) 
			{
				if(nodes.item(i).getNodeName().matches("att") == true)
				{
					if(nodes.item(i).getAttributes().getNamedItem("name").getNodeValue().equals("Name"))
					{
						if(ListUsedSkins.contains(nodes.item(i).getAttributes().getNamedItem("value").getNodeValue().trim())== true)
						{
							Node n1 = nodes.item(i).getParentNode();
							Node n2 = tempSkin.importNode(n1, true);
							list.appendChild(n2);
						}
					}
				}
			}
			
			TransformerFactory tFactory = TransformerFactory.newInstance(); 
			Transformer transformer = tFactory.newTransformer();
			Source source = new DOMSource(tempSkin);
			boolean skinBackup = new File(Util.projectPath+""+eachSkin+".xml").renameTo(new File(Util.projectPath+"SkinsBackUp\\"+eachSkin+".xml"));
			Result output = new StreamResult(new File(Util.projectPath+""+eachSkin+".xml"));
			transformer.transform(source, output);
			//Util.printToConsole("Writing to file:"+projectPath+""+eachSkin+".xml");
		}
    }
}