package com.kony.tag.js.codereview.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import com.kony.tag.arabiclayout.util.BuildPropertiesUtil;
import com.kony.tag.config.CodeReviewStatus;
import com.kony.tag.config.ProjectConstants;
import com.kony.tag.config.TagToolException;
import com.kony.tag.js.codereview.config.ExcludeFileFilter;
import com.kony.tag.js.codereview.config.SVNFileFilter;

/**
 * Author : TAG Group
 */
public class ReviewUtil 
{
	public static final String FILE_DELIMITER = "\\";
	//static String tempCodeDir = PropertyFileReader.outputPath+"\\tempCodeDir"; //destination Lua files path
	public static String projectPath = "" ;
	public static String tempCodeDir = projectPath+"tempCodeDir";
	public static String formSrcDir = projectPath+"forms\\"; // source Lua files path
	public static String xmlSrcPath = projectPath+"projectevents.xml";
	public static String headersDir = projectPath+"headers\\";
	public static String footersDir = projectPath+"footers\\";
	public static String popupsDir = projectPath+"dialogs\\";
	public static String templatesDir = projectPath+"templates\\";
	public static String luaSrcDirName = projectPath+"modules\\lua";
	public static String skinFilesDirName = projectPath+"themes\\default";
	public static String Output_DIR = projectPath+"output";
	public static String formattedLuaDir = projectPath+"output\\formattedLua";
	public static String i18nDir = projectPath+"resources\\i18n\\properties";
	
	public static void setProjectPath(String path)
	{
		projectPath = path+"\\";
		//Util.printToConsole("%%%%%%%%%%%%%%% after setting the project path "+projectPath); 

		formSrcDir = projectPath+"forms\\"; // source Lua files path
		xmlSrcPath = projectPath+"projectevents.xml";
		headersDir = projectPath+"headers\\";
		footersDir = projectPath+"footers\\";
		popupsDir = projectPath+"dialogs\\";
		templatesDir = projectPath+"templates\\";
		luaSrcDirName = projectPath+"modules\\lua";
		skinFilesDirName = projectPath+"themes\\default";
		tempCodeDir = projectPath+"tempCodeDir";
		Output_DIR = projectPath+"output";
		formattedLuaDir = projectPath+"output\\formattedLua";
		i18nDir = projectPath+"resources\\i18n\\properties";
	}
	
	// CERTIFIED !!
	// This is tailored for Kony Architecture. Does not search sub folders
	public void copyAllFiles(String sourceDir,String destinationDir) throws TagToolException {		
		File[] srcformDir = new File(sourceDir).listFiles(new SVNFileFilter());
		String tempDestFile;
		String tempDestDir;
	
		if(srcformDir != null)
		{
			for(File formSrcFile : srcformDir)
			{
				if(formSrcFile.isDirectory())
				{
					tempDestDir = destinationDir+FILE_DELIMITER+formSrcFile.getName();
					createDirectory(tempDestDir);
					copyAllFiles(formSrcFile.getAbsolutePath(),tempDestDir);
					//CodeReviewStatus.getCodeReviewStatus().addErrorMessage(
							//"Did not review the files at "+formSrcFile.getAbsolutePath());
				}
				else if(!isRTLFile(formSrcFile.getName()))
				{
					tempDestFile = destinationDir+FILE_DELIMITER+formSrcFile.getName();
					copy(formSrcFile, new File(tempDestFile));
				}
			}
		}
		else
		{
			CodeReviewStatus.getInstance().addErrorMessage(" "+sourceDir+" not found");
		}
	}
	
	public void copyGeneratedArabicFiles(String sourceDir,String destinationDir) throws TagToolException {		
		File[] srcformDir = new File(sourceDir).listFiles(new SVNFileFilter());
		String tempDestFile;
		String tempDestDir;
	
		if(srcformDir != null)
		{
			for(File formSrcFile : srcformDir)
			{
				if(formSrcFile.isDirectory())
				{
					tempDestDir = destinationDir+FILE_DELIMITER+formSrcFile.getName();
					createDirectory(tempDestDir);
					copyAllFiles(formSrcFile.getAbsolutePath(),tempDestDir);
					//CodeReviewStatus.getCodeReviewStatus().addErrorMessage(
							//"Did not review the files at "+formSrcFile.getAbsolutePath());
				}
				else
				{
					tempDestFile = destinationDir+FILE_DELIMITER+formSrcFile.getName();
					copy(formSrcFile, new File(tempDestFile));
				}
			}
		}
		else
		{
			CodeReviewStatus.getInstance().addErrorMessage(" "+sourceDir+" not found");
		}
	}
	
	private boolean isRTLFile(String fileName){
		return (fileName.startsWith(ProjectConstants.AR_FILE_PREFIX) || fileName.startsWith(ProjectConstants.AR_LAYOUT_MANAGER));
	}
	
	public void deleteSpecificFiles(String destDir,String identifierPrefix) throws TagToolException {		
		File[] files = new File(destDir).listFiles(new SVNFileFilter());
		String tempDestDir;
	
			for(File file : files)
			{
				if(file.isDirectory())
				{
					tempDestDir = destDir+FILE_DELIMITER+file.getName();
					deleteSpecificFiles(tempDestDir,identifierPrefix);
				}
				else
				{
					if (file.getName().startsWith(identifierPrefix)) {
						file.delete();
					}
				}				
			}
	}
	

	// CERTIFIED...
	public void copyFile(String srcFilePath, String destFilePath) throws TagToolException {
		File srcFile = new File(srcFilePath);
		File destFile = new File(destFilePath);
		
		if (srcFile.exists()) {
			copy(srcFile, destFile);
		} else {
			CodeReviewStatus.getInstance().addErrorMessage(
					" Unable to find the file : " + srcFilePath);
		}
		
	}
	
	// CERTIFIED...
	private void copy(File src, File dest) throws TagToolException {
		InputStream in = null;
		OutputStream out = null;
		try
		{
			in = new FileInputStream(src);
			out = new FileOutputStream(dest);				
			byte[] buf = new byte[1024];
			int len;
			
			while((len = in.read(buf)) > 0)
			{
				out.write(buf,0,len);
			}
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
			throw new TagToolException(("Error copying files for review " + ioe.toString()));
		}
		finally
		{
			if(in!=null)
			{
				try 
				{
					in.close();
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
			
			if(out!=null)
			{
				try 
				{
					out.close();
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}		
	}

	// *** CERTIFIED ... 
	public void deleteDirectory(String directoryName) {
		
		File tmpDirFile = new File(directoryName);
		
		if (tmpDirFile.exists()) {
			File[] allTempFiles = tmpDirFile.listFiles();

			// Recursively delete all files and sub directories
			if(allTempFiles != null) {
				for(File file : allTempFiles) {
					if(file.isDirectory()) {
						deleteDirectory(file.getAbsolutePath());
					} 
					file.delete();
				}
			}
			
			// Now Delete Empty Directory
			tmpDirFile.delete();
		}
	}

	
	public long getLastModifiedTimeStamp(List<String> directoryNames) {
		long lastModifiedTime = -1;
		long tmpTime = -1;
		
		for (String directoryName: directoryNames) {
			tmpTime = getLastModifiedTimeStamp(directoryName);
			if (tmpTime > lastModifiedTime) {
				lastModifiedTime = tmpTime;
			}
		}
		
		return lastModifiedTime;
	}
	
	public long getLastModifiedTimeStamp(String directoryName) {
		long lastModifiedTime = -1;
		long tmpTime = -1;
		
		File tmpDirFile = new File(directoryName);
		
		if (tmpDirFile.exists()) {
			File[] allTempFiles = tmpDirFile.listFiles();

			// Recursively delete all files and sub directories
			if(allTempFiles != null) {
				for(File file : allTempFiles) {
					if(file.isDirectory()) {
						tmpTime = getLastModifiedTimeStamp(file.getAbsolutePath());
						if (tmpTime > lastModifiedTime) {
							lastModifiedTime = tmpTime;
						}
					} else {
						tmpTime = file.lastModified();
						if (tmpTime > lastModifiedTime) {
							lastModifiedTime = tmpTime;
						}
					}
				}
			}
		
	}
		return lastModifiedTime;
	}
	
	
	public void createDirectory(String directoryName) {
		File tmpFile = new File(directoryName);
		
		if(!tmpFile.exists()) {
			tmpFile.mkdir();
		}
	}

	// *** DELETE NOT NEEDEDE
	private static void prepareNecessaryDir()
	{
		// Uncomment below code if you want to auto-correct the code.		
		/*File destLuaPathFile = new File(cleanedLua);
		if (destLuaPathFile.exists())
		{ 
			// dir for destination Lua path
			destLuaPathFile.delete();			
		}
		destLuaPathFile.mkdir();*/
		
		// Create temporary folder to copy the files
		File tempDirPathFile = new File(tempCodeDir);
		if (tempDirPathFile.exists())
		{ 
			// dir for temp lua, kl and xml files for functionNames search
			File[] allTempFiles = tempDirPathFile.listFiles();
			if(allTempFiles != null)
			{
				for(File fileName : allTempFiles) 
				{
					fileName.delete();
				}
			}			
			tempDirPathFile.delete();
		}
		tempDirPathFile.mkdir();
		
		// Create the output folder.
		File outputDirPathFile = new File(Output_DIR);
		File formattedLuaDirPathFile = new File(formattedLuaDir);
		if (outputDirPathFile.exists())
		{
			// dir for temp lua, kl and xml files for functionNames search			
			if (formattedLuaDirPathFile.exists())
			{
				// dir for temp lua formatted files
				File[] allLuaFiles = formattedLuaDirPathFile.listFiles();
				if(allLuaFiles != null)
				{
					for(File LuaFileName : allLuaFiles) 
					{
						LuaFileName.delete();
					}
				}
				formattedLuaDirPathFile.delete();
			}
			
			File[] allOutputFiles = outputDirPathFile.listFiles();
			if(allOutputFiles != null)
			{
				for(File outputFileName : allOutputFiles) 
				{
					outputFileName.delete();
				}
			}
			outputDirPathFile.delete();
		}
		
		//Create output and formatted Lua folders
		outputDirPathFile.mkdir();		
		formattedLuaDirPathFile.mkdir();
	}
	
	public static ArrayList<String> getUnusedItems(ArrayList<String> allItemsList, Set<String> usedItemsList)
	{	
		ArrayList<String> unusedItems = new ArrayList<String>();
	
		for (int i = 0; i < allItemsList.size(); i++)
		{			
			if(!usedItemsList.contains(allItemsList.get(i)))
			{
				unusedItems.add(allItemsList.get(i)+"\n");
			}				
		}	
		
		return unusedItems;
	}
	
	public static String stackTraceToString(Throwable e) 
	{
	    StringBuilder sb = new StringBuilder();
	    for (StackTraceElement element : e.getStackTrace()) 
	    {
	        sb.append(element.toString());
	        sb.append("\n");
	    }
	    
	    return sb.toString();
	}
	
	public File[] fetchFilesWithoutFilter(String directoryName) {
		File[] files = new File(directoryName).listFiles();
		return files;
	}
	
	public File[] fetchFiles(String directoryName) {
		File[] files = new File(directoryName).listFiles(new ExcludeFileFilter());
		return files;
	}

	public static void resetDefaultBuildProperties(){
		try {
			Properties props = BuildPropertiesUtil.getBuildProperties();

			FileOutputStream out = new FileOutputStream(projectPath + ProjectConstants.BUILD_PROPERTIES_FILE);
			props.store(out, null);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}