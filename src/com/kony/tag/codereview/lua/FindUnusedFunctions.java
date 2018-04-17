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

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PartInitException;

/**
 * Author : TAG Group
 * Unused functions on lua modules.
 * @author User
 *
 */

public class FindUnusedFunctions
{
	public static void main (Shell shell) 
	{
		File file = new File(Util.Output_DIR+"\\UnusedFunctions.csv");
		Writer output = null;
		
		long startTime = System.currentTimeMillis();
		try
		{
			Util.printToConsole("Getting all the function names .......... ");
			ArrayList<LuaFunction> functionNamesList = Util.getAllFunctionNames(); // get all function names
				
			if(functionNamesList!=null && functionNamesList.size()>0)
			{ 
				// Check for un-used function if and only if there are some functions in the lua files.
				Util.printToConsole("totalFunctionNames size ..."+functionNamesList.size());
				Util.printToConsole("Getting all the used function names .......... ");
				Set<String> usedFunctions = getUsedFunctions(functionNamesList);
				Util.printToConsole("usedFunctions  ...."+usedFunctions.size());
				
				//Util.printToConsole("Deleting the files in the tempDirectory folder......");
				//Util.deleteDirectory(tempCodeDir);
				Util.printToConsole("Getting all the un-usedfunction names .......... ");
				//LuaFunction luaList= new LuaFunction();
				//ArrayList<String> list=luaList.getFunctionName();
				
				ArrayList<String> unusedFunctions = Util.getUnusedFunctions(functionNamesList, usedFunctions);
				Util.printToConsole("Number of un-used functions are "+unusedFunctions.size()+"  ");
				//deleteUnncessaryFunctions(unusedFunctions); // Un-comment this line if you want to delete the un-used functions programetically.
				output = new BufferedWriter(new FileWriter(file, false));
				for(int i=0;i<unusedFunctions.size();i++)
				{
					output.append(unusedFunctions.get(i));
			    	//output.flush();	
				}
			}
			
			long endTime = System.currentTimeMillis();
			Util.printToConsole("Total Time Taken for Processing to find the un-used functions is ...... "+(endTime-startTime)+" ms");
		}
		catch (IOException e) 
		{
			//Try out scenarios which will make the code reach this block 
			//and try to print a meaningful statement here, before verbosing the stack trace
			e.printStackTrace();
			Util.printToConsole("Encountered difficulties while finding UnUsed functions due to following reason: " + e.getMessage() + ". \nContinuing the script with other tasks.");
			//Util.printToConsole(Util.stackTraceToString(e)); 
		}
		catch (CodeReviewException e) 
		{
			e.printStackTrace();
			Util.printToConsole("Encountered difficulties while finding UnUsed functions due to following reason: " + e.getMessage() + ". \nContinuing the script with other tasks.");
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			Util.printToConsole("Encountered difficulties while finding UnUsed functions due to following reason: " + e.getMessage() + ". \nContinuing the script with other tasks.");
			//Util.printToConsole(Util.stackTraceToString(e)); 
		} 
		finally
		{
			if(output!=null)
			{
				try 
				{
					output.close();
				}
				catch (IOException e) 
				{
					e.printStackTrace();
					Util.printToConsole("Encountered difficulties while finding UnUsed functions due to following reason: " + e.getMessage() + ". \nContinuing the script with other tasks.");
				}
			}
		}
			 
	}	
		
	public static Set<String> getUsedFunctions(ArrayList<LuaFunction> functionNamesList) throws IOException
	{
		ArrayList<LuaFunction> tmpFunctionNamesList = functionNamesList;
		String tempCodeDir = Util.tempCodeDir;
		String startFunction = "function";
		Set<String> usedFunctions = new TreeSet<String>();
		File tempCodeDirFile = new File(tempCodeDir);
		File[] allTempFiles = tempCodeDirFile.listFiles(new SVNFileFilter());
			
		if(allTempFiles!=null)
		{
			for(File fileName : allTempFiles) 
			{
				LineNumberReader br = null;
				try
				{
					// LineNumberReader is subclass of BufferedReader with Line Number feature
					br = new LineNumberReader(new FileReader(fileName));
					String line = null;
					String filePath = fileName.getName();
					
					// read lines from file till end of the file
					while(null != (line = br.readLine())) 
					{
						//Util.printToConsole("^^^^^^^^^^^^^^^^^^^^ tmpFunctionNamesList size is "+tmpFunctionNamesList.size()+" Used function list size "+usedFunctions.size()); 
						for (int i = 0; i < tmpFunctionNamesList.size(); i++)
						{		
							String functionName = tmpFunctionNamesList.get(i).getFunctionName();
							int noOfParams = tmpFunctionNamesList.get(i).getParamCount();
							//System.out.println("commentStart "+commentStart+" functionNameStart "+functionNameStart); 
							if (!line.startsWith(startFunction) && line.contains(functionName))
							{
								int commentStart = line.indexOf("--");
								int functionNameStart = line.indexOf(functionName);
								if(commentStart!=-1)
								{
									if(functionNameStart < commentStart)
									{	
										int start = line.indexOf("(",line.indexOf(functionName));
										int end = line.lastIndexOf(")",line.indexOf(functionName));
											
										if(start!=-1 && end !=-1&& start<=end)
										{
											//System.out.println(line+" Start : "+start+" End: "+end);
											String paramList[]= (line.substring(start,end)).split(",");
											if(paramList.length!=noOfParams)
											{
												//System.out.println("Parameter List not equal for file: "+filePath+" :: "+br.getLineNumber()+" :: "+functionName);
											}
										}	
										usedFunctions.add(functionName);
										tmpFunctionNamesList.remove(i); // Remove the already used functions from the verification on new line.
									}	
								}
								else
								{
									int start = line.indexOf("(",line.indexOf(functionName));
									int end = line.indexOf(")",line.indexOf(functionName));
									//System.out.println(line+" Start : "+start+" End: "+end);
								
									if(start!=-1 && end !=-1 && start<=end)
									{
										String paramList[]= (line.substring(start,end)).split(",");
										if(paramList.length!=noOfParams)
										{
											//System.out.println("Parameter List not equal for file: "+filePath+" :: "+br.getLineNumber()+" :: "+functionName);
										}
									}	
									usedFunctions.add(functionName);
									tmpFunctionNamesList.remove(i);
								}
							}
						}
					}
				}
				catch(IOException ioe)
				{
					ioe.printStackTrace();
					throw ioe;
					//Util.printToConsole("IOException"+ioe);
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
							Util.printToConsole("Encountered difficulties while finding UnUsed functions due to following reason: " + e.getMessage() + ". \nContinuing the script with other tasks.");
						}
					}
				}
			}
		}
		return usedFunctions;
	}
		
	// Below code is to correct the code. As a TRB activity we just report the unused functions. Deletion should be done by the project teams.
	public static void deleteUnncessaryFunctions(Set<String> unusedFunctions) throws PartInitException
	{	
		String luaSrcDirName = Util.luaSrcDirName;
		String startFunction = "function";
		String line = "",line1="";
		String startBlockComment = "--[[";
		String endBlockComment = "--]]";
		String blockComment = "";
		String destLuaFile = "";
		String functionBlock = "";
		File[] luaSrcfolder = new File(luaSrcDirName).listFiles(new SVNFileFilter());
		String cleanedLua = Util.projectPath+"cleanedLua"; //destination Lua files path
		
		// creating fresh folder for putting cleaned lua files
		File cleanedLuaFileObj = new File(cleanedLua);
		if (cleanedLuaFileObj.exists())
		{
			// dir for destination Lua path
			cleanedLuaFileObj.delete();
		}
		cleanedLuaFileObj.mkdir();
		
		
		boolean validFunction = false;
		for(File fileName : luaSrcfolder)
		{
			line1 = "";
			BufferedWriter cleanCodeWriter = null;
			LineNumberReader br = null;
			
			try
			{
				destLuaFile = cleanedLua+"\\"+fileName.getName();
				cleanCodeWriter = new BufferedWriter(new FileWriter(destLuaFile));					
	
				// LineNumberReader is subclss of BufferedReader with Line Number feature
				br = new LineNumberReader(new FileReader(fileName));
					
				// read lines from file till end of the file
				int endFunction = 0;				
				while(null != (line = br.readLine())) 
				{
					// check if line started with block comment
					String functionName = "";
					if (line.trim().startsWith(startBlockComment))
					{
						if (line.contains(endBlockComment))
						{
							blockComment = "\n"+line;
						}
						else
						{										
							blockComment = "\n"+line;
						
							// read lines until block comment ends
							while(null != (line = br.readLine()) && !(line.trim().endsWith(endBlockComment)))
							{
									blockComment = blockComment+"\n"+line;										
							}
							blockComment = blockComment+"\n"+line;										
						}		
					}
					else if(line.trim().startsWith(startFunction) && 
							unusedFunctions.contains(line.trim().substring(startFunction.length()+1,line.indexOf("(")).trim())== false)
					{
						//check if line started with function keyword
						functionName = line.trim().substring(startFunction.length()+1,line.indexOf("(")+1).trim();
						line1 = line1+functionBlock;
						functionBlock = blockComment+"\n"+line;
						validFunction = true;								
					}
					else if(line.trim().startsWith(startFunction) && 
							unusedFunctions.contains(line.trim().substring(startFunction.length()+1,line.indexOf("(")).trim())== true)
					{
						validFunction = false;
					}
					else
					{
						if(validFunction == true) 
						{
							if(line.trim().startsWith("end"))
							{
								endFunction = br.getLineNumber();
							}
							
							if(line.trim().startsWith(startBlockComment))
							{										
								blockComment = "\n"+line;
								
								//read lines until block comment ends
								while(null != (line = br.readLine()) && !(line.trim().endsWith(endBlockComment)))
								{
									blockComment = blockComment+"\n"+line;										
								}									
								functionBlock = functionBlock +"\n"+blockComment;
							}
							
							functionBlock = functionBlock +"\n"+line;
						}
					}
				}

				//br.close();
				Util.printToConsole("File:"+fileName.getName());
				if(validFunction == true) 
				{
					line1= line1+functionBlock;
				}
				
				functionBlock = "";
				cleanCodeWriter.write(line1);				
			}
			catch(IOException ioe)
			{
				ioe.printStackTrace();
				Util.printToConsole("Encountered difficulties while finding UnUsed functions due to following reason: " + ioe.getMessage() + ". \nContinuing the script with other tasks.");
			}
			finally
			{
				try
				{					
					if(cleanCodeWriter != null)
						cleanCodeWriter.close();
				}
				catch(Exception e)
				{
					Util.printToConsole("Encountered difficulties while finding UnUsed functions due to following reason: " + e.getMessage() + ". \nContinuing the script with other tasks.");
				}
				
				try
				{					
					if(br != null)
						br.close();
				}
				catch(Exception e)
				{
					Util.printToConsole("Encountered difficulties while finding UnUsed functions due to following reason: " + e.getMessage() + ". \nContinuing the script with other tasks.");
				}
			}
		}
	}
}		