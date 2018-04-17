package com.kony.tag.codereview.lua;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Writer;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class UnusedImages
{
	private String luaDirName=""; // source Lua files path<%--  --%>
	private File file; // output file i.e .csv
	private Set<String> gUnusedImage=new TreeSet<String>();	
	
	public UnusedImages()
	{		
		luaDirName= Util.tempCodeDir;
		file = new File(Util.Output_DIR+"\\UnusedImage.csv");		
	}
	
	public void filterImages(String path)
	{
		long startTime = System.currentTimeMillis();
		
		boolean lIsSkinFldrExists = new File(Util.skinFilesDirName).exists();
		if(lIsSkinFldrExists)
			Util.copyFiles(Util.skinFilesDirName,Util.tempCodeDir);
		
		parseAllFiles(path);
		Util.printToConsole("Total number of images used: " + gUnusedImage.size());
		
		writeUnUsed(luaDirName);
		writeToFile();
		
		long endTime = System.currentTimeMillis();
		Util.printToConsole("Total Time Taken for finding unused images is ...... "+(endTime-startTime)+" ms");
	}
	
	public void parseAllFiles(String parentDirectory)
	{
		File lParentFile = new File(parentDirectory);
		if(lParentFile.exists())
		{
			File[] filesInDirectory = lParentFile.listFiles();        
	        if(filesInDirectory != null)
	        {
	        	for(File f : filesInDirectory)
	            {
	        		if(f.isDirectory())
	                {
	                    parseAllFiles(f.getAbsolutePath());
	                }
	                
	                String tmp=f.getAbsolutePath();
	                if(tmp.endsWith(".png"))
	                {
	                	String name=f.getName();
	    				if(name.contains("@2x.png"))
	    				{
	    					name=name.replaceAll("@2x.png", ".png");            	
	    				}
	    				gUnusedImage.add(name);
	                }                      
	            }	        	
	        }
	        else
	        {
	        	Util.printToConsole("******************************* Resources folder with name : " + lParentFile.getName() + " doesn't exist.");
	        }
		}
		else
		{
			Util.printToConsole("******************************* Resources folder with name : " + lParentFile.getName() + " doesn't exist.");
		}		
	}             
  
	
	public void writeUnUsed(String parentDirectory)
	{	
		File[] filesInDirectory = new File(parentDirectory).listFiles();		
		try
		{
			for(File f : filesInDirectory)
	        {
				//System.out.println(f);
	        	LineNumberReader br = null;		
				
	            if(f.isFile())
	            {	            	
	            	br=new LineNumberReader(new FileReader(f));
	            	String line = "";
	        		
        			while(null != (line = br.readLine())) 
        			{
                    	int lineNumber = br.getLineNumber();
                    	if(line.contains(".png"))
                    	{
                    		//System.out.println(lineNumber);
                    		extractImageName(line);
                    	}
        			}
        			br.close();
	            }
	        }
			Util.printToConsole("Total number of unused images: " + gUnusedImage.size());
		}
		catch(Exception e)
		{
			Util.printToConsole("File Error: "+e);
		}		
	}
	
	void extractImageName(String ImgLine)
	{
		/*Set<String> tmpImages=new TreeSet<String>();
		try 
		{
			tmpImages=gUnusedImage;
		} 
		catch (Exception e) 
		{
			Util.printToConsole("Error Copying Images");
			e.printStackTrace();
		}
		
		for(String it: tmpImages)
		{
			if(ImgLine.contains(it))
			{	
				Util.printToConsole("##############this is image which is going to be deleted#######"+it);
				gUnusedImage.remove(it);
			}
		}*/
		
		
		Iterator<String> it=gUnusedImage.iterator();
		while(it.hasNext())
		{
			String tmp=it.next();
			if(ImgLine.contains(tmp))
			{	
				//Util.printToConsole("##############this is image which is going to be deleted#######"+tmp);
				it.remove();
			}
		}
	}
	
	void writeToFile()
	{
		Writer output = null;
		try
		{
			output = new BufferedWriter(new FileWriter(file, false));			
			
			if(gUnusedImage.size()==0)
			{				
				output.append("######## NO iMAGE Found In APP ##############");
			}
			else
			{
				Iterator<String> it=gUnusedImage.iterator();
				while(it.hasNext())
				{
					String imageName=it.next();
					output.append(imageName+"\n");
				}
			}			
		}
		catch(Exception e)
		{
			Util.printToConsole(""+e);			
		}
		finally
		{
			if(output != null)
			{
				try 
				{
					output.close();
				}
				catch (IOException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}