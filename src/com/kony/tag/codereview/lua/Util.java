package com.kony.tag.codereview.lua;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Set;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

import com.kony.tag.codereview.console.ReviewConsole;

/**
 * Author : TAG Group
 */
public class Util 
{
	//static String tempCodeDir = PropertyFileReader.outputPath+"\\tempCodeDir"; //destination Lua files path
	static String projectPath = "" ;
	static String tempCodeDir = projectPath+"tempCodeDir";
	static String formSrcDir = projectPath+"forms\\"; // source Lua files path
	static String xmlSrcPath = projectPath+"projectevents.xml";
	static String headersDir = projectPath+"headers\\";
	static String footersDir = projectPath+"footers\\";
	static String popupsDir = projectPath+"dialogs\\";
	static String templatesDir = projectPath+"templates\\";
	static String luaSrcDirName = projectPath+"modules\\lua";
	static String skinFilesDirName = projectPath+"themes\\default";
	static String Output_DIR = projectPath+"output";
	static String formattedLuaDir = projectPath+"output\\formattedLua";
	static String i18nDir = projectPath+"resources\\i18n\\properties";
	
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
	
	public static void copyFiles(String aSrcDir,String tempCodeDir)
	{		
		File[] srcformDir = new File(aSrcDir).listFiles(new SVNFileFilter());
		if(srcformDir != null)
		{
			for(File formSrcFile : srcformDir)
			{
				if(formSrcFile.isDirectory())
				{
					copyFiles(formSrcFile.getAbsolutePath(),tempCodeDir);
				}
				else
				{
					String tempDestFile = tempCodeDir+"\\"+formSrcFile.getName();
					copy(formSrcFile, new File(tempDestFile));
				}
			}
		}
		else
		{
			Util.printToConsole(" "+aSrcDir+" not found");
		}
	}

	public static void copy(File src, File dest)
	{
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
	
	public static void prepareNecessaryDir()
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
	
	public static void deleteTempDirectory(Shell shell)
	{
		try
		{
			File tempCodeDirFile = new File(tempCodeDir);
			File[] allTempFiles = tempCodeDirFile.listFiles(new SVNFileFilter());
			
			Util.printToConsole("Deleting the files ....");
			if(allTempFiles != null) {
				for(File fileName : allTempFiles) 
				{
					fileName.delete();
				}
			}
			
			Util.printToConsole("Deleting the directory ....");
			if(tempCodeDirFile.exists())
			{
				tempCodeDirFile.delete();
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			Util.printToConsole(e.getMessage());
			Util.printToConsole(Util.stackTraceToString(e));			
		}
	}
	
	public static void deleteOutputDirectory(Shell shell)
	{
		try
		{
			/*File formattedLuaDirFile = new File(formattedLuaDir);			
			if(formattedLuaDirFile.exists())
			{
				File[] allTempFiles = formattedLuaDirFile.listFiles(new SVNFileFilter());
				
				Util.printToConsole("Deleting the files from formatted Lua folder....");
				for(File fileName : allTempFiles) 
				{
					fileName.delete();
				}
				
				Util.printToConsole("Deleting the Formatted Lua directory ....");
				formattedLuaDirFile.delete();
			}*/
			
			File outputDirFile = new File(Output_DIR);			
			if(outputDirFile.exists())
			{
				File[] allTempFiles = outputDirFile.listFiles(new SVNFileFilter());
				
				Util.printToConsole("Deleting the files from output folder....");
				for(File fileName : allTempFiles) 
				{
					fileName.delete();
				}
				
				Util.printToConsole("Deleting the output directory ....");
				outputDirFile.delete();
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			Util.printToConsole(e.getMessage());
			Util.printToConsole(Util.stackTraceToString(e)); 			
		}
	}
	
	public static void validateNCopyFilesToTempDir(Shell shell) throws CodeReviewException
	{		
		boolean lIsLuaFldrExists = new File(luaSrcDirName).exists();
		boolean lIsFormsFldrExists = new File(formSrcDir).exists();
		boolean lIsTemplatesFldrExists = new File(templatesDir).exists();
		boolean lIsPopupsFldrExists = new File(popupsDir).exists();
		boolean lIsHeadersFldrExists = new File(headersDir).exists();
		boolean lIsFootersFldrExists = new File(footersDir).exists();
		boolean lIsProjEventsFileExists = new File(xmlSrcPath).exists();
		
		if(lIsLuaFldrExists && lIsFormsFldrExists && lIsPopupsFldrExists && (lIsTemplatesFldrExists || (lIsHeadersFldrExists && lIsFootersFldrExists)))
		{
			// Copy files from lua folder to temp directory 
			Util.copyFiles(luaSrcDirName,tempCodeDir);
			
			// Copy files from Forms folders to temp directory
			Util.copyFiles(formSrcDir, tempCodeDir);
			
			// Copy files from popups folder to temp directory
			Util.copyFiles(popupsDir,tempCodeDir);
			
			// Copy files from templates folder to temp directory
			if(lIsTemplatesFldrExists)
				Util.copyFiles(templatesDir,tempCodeDir);
			
			// Copy files from headers folder to temp directory
			if(lIsHeadersFldrExists)
				Util.copyFiles(headersDir,tempCodeDir);
				
			// Copy files from footers folder to temp directory
			if(lIsFootersFldrExists)
				Util.copyFiles(footersDir,tempCodeDir);
					
			//Copy projectevents.xml file to temp directory
			if(lIsProjEventsFileExists)
				Util.copy(new File(xmlSrcPath), new File(tempCodeDir+"\\projectevents.xml"));
		}
		else
		{
			throw new CodeReviewException("Terminating the program: Source project doesn't have proper Kony Project folder structure");
		}		
	}
	
	public static ArrayList<LuaFunction> getAllFunctionNames() throws CodeReviewException
	{
		//Util.printToConsole("Inside getAllFunctionNames");
		String startFunction = "function ";			
		File[] luaFolder = new File(luaSrcDirName).listFiles(new SVNFileFilter());
		ArrayList<LuaFunction> functionNamesList = new ArrayList<LuaFunction>(); 
		String startBlockComment = "--[[";
		String endBlockComment = "--]]";
		String singleComment = "--";
		
		if(luaFolder==null || luaFolder.length<=0)
		{
			throw new CodeReviewException(Constants.LUA_FILE_NOT_FOUND_EXCEP);
		}
		else
		{
			for( File fileName : luaFolder) 
			{				
				String line = "";
				String functionName = "";
				LineNumberReader br = null;
			
				try
				{
					// LineNumberReader is subclass of BufferedReader with Line Number feature
					br = new LineNumberReader(new FileReader(fileName));
					
					// read lines from file till end of the file
					while(null != (line = br.readLine())) 
					{												
						if (line.trim().startsWith(startBlockComment))
						{
							// check if line started with block comment						
							while(null != (line = br.readLine()) && !(line.trim().endsWith(endBlockComment))){}	// read lines until block comment ends					
						}
						else if(line.trim().startsWith(singleComment))
						{
							// check if line started with single line comment & ignore that line							
						}
						else if(line.trim().startsWith(startFunction))
						{
							//check if line started with function keyword
							functionName = line.substring(startFunction.length(),line.indexOf("(")).trim();
							String paramList[]={};
							
							LuaFunction luaFun = new LuaFunction();	
							if(line.contains(","))
							{
								paramList= (line.substring(line.indexOf("(")+1, line.indexOf(")"))).split(",");							
								luaFun.setParamCount(paramList.length);
							}
							else if(line.substring(line.indexOf("(")+1, line.indexOf(")")).length()==0) 
							{
								luaFun.setParamCount(0);
							}
							else
								luaFun.setParamCount(1);
							
							luaFun.setFunctionName(functionName.trim());							
							functionNamesList.add(luaFun); // add function names if not already added
						} // end of function keyword check
					} // end of while : EOF reached				
				}
				catch(IOException ioe)
				{
					ioe.printStackTrace();
					throw new CodeReviewException(ioe.getMessage());
				}
				finally
				{
					if(br!=null)
					{
						try 
						{
							br.close();
						}
						catch (IOException e) 
						{
							e.printStackTrace();
						}
					}
				}
			}		
		}
		
		return functionNamesList;		
	}
	
	public static void writeDataToFile(String outFileName, ArrayList<String> data) throws PartInitException, IOException 
	{
		try 
		{
			if(!data.isEmpty())
			{
				File outFile = new File(outFileName);
				PrintWriter pw = new PrintWriter(outFile);
				
				for (String name : data) 
				{
					pw.write(name+"\n");
				}
				
				pw.close();
				Util.printToConsole("Writing the details to file "+outFile.getAbsolutePath());
			}
		}
		catch (FileNotFoundException e) 
		{
			Util.printToConsole(" File not found "+outFileName);
			e.printStackTrace();
		}
	}
	
	public static void printToConsole(String message)
	{		
		try 
		{
			MessageConsole messageConsole = ReviewConsole.getMessageConsole();
			MessageConsoleStream mesConStream = messageConsole.newMessageStream();
			mesConStream.setActivateOnWrite(true);
			mesConStream.println(message);
			mesConStream.flush();
			mesConStream.close();
		}
		catch (PartInitException e) 
		{
			e.printStackTrace();
			//printToConsole(Util.stackTraceToString(e));
		}
		catch (IOException e) 
		{
			e.printStackTrace();
			//printToConsole(Util.stackTraceToString(e));
		}
	}
	
	public static void displayDialog(Shell shell, String mesType, String message) 
	{
		MessageDialog.openInformation(shell, mesType,message);	
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
	
	public static ArrayList<String> getUnusedFunctions(ArrayList<LuaFunction> allItemsList, Set<String> usedItemsList)
	{	
		ArrayList<String> unusedItems = new ArrayList<String>();
		int size = allItemsList.size();
	
		for (int i = 0; i < size; i++)
		{			
			if(!usedItemsList.contains(allItemsList.get(i).getFunctionName().trim()))
			{
				unusedItems.add(allItemsList.get(i).getFunctionName()+"\n");
			}				
		}	
		
		return unusedItems;
	}
}