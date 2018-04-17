package com.kony.tag.codereview.lua;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Writer;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

import org.eclipse.swt.widgets.Shell;

/**
 * Unused functions on lua modules.
 * @author TAG Group
 *
 */
public class CheckCommonMistakes
{		
	public static void findCommonMistakes(Shell shell)
	{	
		long startTime = System.currentTimeMillis();
		String luaDirName =  Util.tempCodeDir; // source Lua files path
		File file = new File(Util.Output_DIR+"\\CommonMistakes.csv");
	    Writer output = null;		
		int appMiddlewareCount=0;
		int countFunctionLines=0;
		int flag=0;
		
		try
		{
			//Util.printToConsole("luaDirName "+luaDirName);
			//String startFunction = "function";			
			Set<String> gLocalVars = new TreeSet<String>();
		    StringBuffer gIncorrectElseSB = new StringBuffer(1000);
		    StringBuffer gLineLenSB = new StringBuffer(1000);
		    StringBuffer gGlobalVarSB = new StringBuffer(1000);
		    StringBuffer gHardCodedSB = new StringBuffer(1000);
		    StringBuffer gFunLevelCommentsSB = new StringBuffer(1000);
			StringBuffer gPrintStatSB = new StringBuffer(1000);
			StringBuffer gLocalVNCSB = new StringBuffer(1000);
			StringBuffer gGlobalVNCSB = new StringBuffer(1000);
			StringBuffer gSetSegmentDataInLoop = new StringBuffer(1000);
			StringBuffer gTrueFalseComparisons = new StringBuffer(1000);
			StringBuffer gFunctionLinesSB = new StringBuffer(1000);
			StringBuffer gwindowAlertSB = new StringBuffer(1000);
			File[] luaFolder = new File(luaDirName).listFiles(new LuaFileFilter());
			String functionName="";
			
			for( File fileName : luaFolder) 
			{
				LineNumberReader br = null;		
				String line = "";
				String previousline ="";
				int gcodelinelength = 150;
				String	gCommentText = "";
				boolean gCommentOpen = false ;
				boolean gJustClosedComment = false;
				boolean gIfdefOpen = false;
				boolean gStatementOpen = false ;
				//Stack<String> allTokens = new Stack<String>();
				Stack<String> functionTokens = new Stack<String>();
				boolean gFunctionOpen = false;
				Stack<String> braceStartTokens = new Stack<String>();
				boolean isBraceStarted = false;
				Stack<String> ifLoopTokens = new Stack<String>();
				String filePath = fileName.getName();
				File formattedLuaFile = new File(Util.formattedLuaDir+"\\" + filePath);
				formattedLuaFile.createNewFile();
				BufferedWriter formattedLuaBfrWriter = new BufferedWriter(new FileWriter(formattedLuaFile));

				// LineNumberReader is subclass of BufferedReader with Line Number feature
				br = new LineNumberReader(new FileReader(fileName));
				int count = 1;
			    
				// read lines from file till end of the file
				while(null != (line = br.readLine())) 
				{
					int lineNumber = br.getLineNumber();//count;
					String trimmedLine = line;
					String commentPart = "";
				
					// remove the comment part of the line if any
					int commentInd = trimmedLine.indexOf("--");
					if(commentInd >= 0 && !(trimmedLine.contains("--[[")) && !(trimmedLine.contains("--]]")))
					{
						commentPart = trimmedLine.substring(commentInd);
						trimmedLine = trimmedLine.substring(0, commentInd);
					}
					
					trimmedLine = trimmedLine.trim();		
					int lineLen = trimmedLine.length();
					
					// write the line with proper indentation
					int numTabs = ifLoopTokens.size();
					if(trimmedLine.startsWith("else") || trimmedLine.startsWith("function ") || trimmedLine.equals("end") || !(gFunctionOpen))
					{
						numTabs--;
					}
					
					int numBraces = braceStartTokens.size();
					numTabs += numBraces;
					for(int i=0; i<=numTabs; i++)
					{
						formattedLuaBfrWriter.append("\t");
					}
					
					formattedLuaBfrWriter.append(trimmedLine);
					formattedLuaBfrWriter.append(commentPart);
					formattedLuaBfrWriter.append("\n");
					
					// Below condition is to avoid finding the variables as global which are defined in a table.
					if(trimmedLine.contains("{"))
					{
						isBraceStarted = true;
						braceStartTokens.push("{");
					}
					
					//checking for the comment text and adding it to comment text global variable.                                                     
					if(!(trimmedLine.endsWith("]]")) && !(trimmedLine.equals("")) && gCommentOpen)
					{
						gCommentText = gCommentText+trimmedLine;
					}
					
					// Checking for big lines.
					if (lineLen>gcodelinelength)
					{
						gLineLenSB.append(filePath+","+"At Line: "+ lineNumber+","+"Length of the line is too long. Length:"+lineLen+","+"Low"+"\n");	
					} // Low 
					  
					// check if it is starting comment block
					if(trimmedLine.startsWith("--[[") && !(trimmedLine.endsWith("]]"))) 
					{
						gCommentOpen = true;
						gJustClosedComment = false ;
						gCommentText = "" ;
					}
					  
					//Check if AppMiddlewareinvoker is used more then once
					int s=line.indexOf("--");						  
					if(s<line.indexOf("appmiddlewaresecureinvoker"))
					{							 
					}
					
					if(s!=-1)
					{
						if(line.contains("appmiddlewaresecureinvoker"))
							appMiddlewareCount++;								  
					}						 
					
					// Check if any == true present	
					if((trimmedLine.contains("== true"))||(trimmedLine.contains("==true")))
					{
						gTrueFalseComparisons.append(filePath+",At Line: "+lineNumber+","+"\""+trimmedLine+"\",\"== true\" is not needed.  You can simply compare as if(var)\n");
					}
					  
					// Check if any == false present
					if((trimmedLine.contains("== false"))||(trimmedLine.contains("==false")))
					{
						gTrueFalseComparisons.append(filePath+",At Line: "+lineNumber+","+"\""+trimmedLine+"\",\"== false\" is not needed.  You can simply compare as if(not(var))\n");
					}
					  
					// check if the comment is closed
					if((trimmedLine.endsWith("]]")) && gCommentOpen)
					{
						gCommentOpen = false;
						gJustClosedComment = true;
					}
					  
					// check if it is a "ifdef"
					if(trimmedLine.matches( "^ifdef[\\s]*[(].*") && trimmedLine.indexOf("!)",1) == -1)
					{
						gIfdefOpen = true;
					}
					  
					if(trimmedLine.lastIndexOf( "!)", 1) != -1 && gIfdefOpen)
					{
						gIfdefOpen = false;
					}
					
					// check for the statement open
					if((trimmedLine.indexOf( "{",-1) == lineLen || 
						trimmedLine.indexOf( "(",-1) == lineLen || 
						trimmedLine.indexOf( ",",-1) == lineLen) && !(gStatementOpen)) 
					{
						gStatementOpen = true ;
						 
					}

					// check for the start of a loop (for or while)
					if (trimmedLine.startsWith("for ") ||
						trimmedLine.startsWith("for("))
					{  
						ifLoopTokens.push("for");	  
					}
					
					if (trimmedLine.startsWith("while ") ||
						trimmedLine.startsWith("while("))
					{
						ifLoopTokens.push("while");	  
					}
					
					if (trimmedLine.startsWith("if ") ||
					    trimmedLine.startsWith("if("))
					{	  
						ifLoopTokens.push("if");	  
					}  
					 
					// check if the data is set in the segment inside a loop						  
					if(trimmedLine.indexOf("segui.setdata") >= 0)
					{
						 if(!(ifLoopTokens.isEmpty()) && (ifLoopTokens.contains("for") || ifLoopTokens.contains("while")))
						 {
							 // setting the segment data in a loop
							 // report a high level warning
							 gSetSegmentDataInLoop.append(filePath+","+"At Line:"+lineNumber+",Setting segment data inside a loop.  This causes animation effect in segment display and is highly discouraged.,High\n"); 
						  }
				    }
					
					// check for some common mistakes    
					// check for "else if" instead of "elseif".  this needs an additional "end" unnecessarily
					if (trimmedLine.contains("else if")) 
					{						  	
						gIncorrectElseSB.append(filePath+","+"At Line:"+lineNumber+","+" An incorrect 'else if' is found "+","+"Low"+"\n"); 
					} //Low
					
					// check for the statement close
					if(gStatementOpen)
					{
					    if((trimmedLine.indexOf( "}",-1) == lineLen || trimmedLine.indexOf( ")",-1) == lineLen)) 
					    {
					    	gStatementOpen = false;
					    }
					}
					  
					// check for function level comments 
					// below condition is to close the comment text once the function is ended. This is to find the function which has 
					// empty function level comments.					 
					if(trimmedLine.equals("end") &&  gFunctionOpen  && ifLoopTokens.isEmpty())
					{
						gFunctionOpen = false;
						functionTokens.pop();
						if(countFunctionLines>100)
						{
							gFunctionLinesSB.append(filePath+","+" Function Name "+functionName+","+" Has more than 100 lines "+","+"Low"+"\n");
						}
						
						countFunctionLines=0;
						if(functionTokens.size()==0)
						{
							gCommentText="";
						}
					}
					
					if(trimmedLine.startsWith("function"))
					{
						gFunctionOpen = true;						 
						functionTokens.add("function");
						//Util.printToConsole("chking for tokens");
					    
						if(!(gJustClosedComment)) 
						{
							functionName = "<Function Name>";
							String functionNamePart = trimmedLine.substring(8);									
							int functionInd = functionNamePart.indexOf("(", 1);
							if(functionInd != -1)
							{
								functionName = functionNamePart.substring(0, functionInd);
								functionName = functionName.trim();
							}
							gFunLevelCommentsSB.append(filePath+","+functionName+","+"Function does not have function level comments "+","+"Medium"+"\n");
						}
							
						if(gJustClosedComment && gCommentText.equals(""))
						{
							functionName = "<Function Name>" ;
							String functionNamePart = trimmedLine.substring(8);
							int functionInd = functionNamePart.indexOf("(", 1) ;
							if(functionInd != -1) {
								functionName = functionNamePart.substring(0, functionInd);
								functionName = functionName.trim();
							}
							gFunLevelCommentsSB.append(filePath+","+functionName+","+"Function does not have function level comments "+","+"Medium"+"\n");
						}
						
						gJustClosedComment = false;
						gCommentText ="";
						
						// Below logic is to get the function parameters and add them to local variables list
						//Get the function parameters and add them to the local variables list gLocalVars.
						int start = trimmedLine.indexOf("(");
						int end = trimmedLine.lastIndexOf(")");
						if(start!=-1 && end!=-1)
						{
							String funcParamsStr = trimmedLine.substring(start+1, end);
							String[] funcParams = funcParamsStr.split(",");
							
							for(int i=0;i<funcParams.length;i++)
							{
								if(!funcParams[i].equals(""))
								{
									gLocalVars.add(funcParams[i].trim());
								}
							}
						}
					}
					
					if(gFunctionOpen)
					{
						countFunctionLines++;  //counting number of lines in a function
					}
					  				 
					if(trimmedLine.equals("end") && !(ifLoopTokens.isEmpty()))
					{
						ifLoopTokens.pop();
						if(ifLoopTokens.size()==0)
						{
							gCommentText="";
						}
					}
					  
					// detect the declaration of local variables
					if (trimmedLine.startsWith("local ") && !isBraceStarted) 
					{
						String varsLine = trimmedLine.substring(6); //sub string after index i
						String[] vars=null;
						
						if(!varsLine.contains("("))
						{ 
							// This check is to avoid to take the any function call related params as variables
							if(varsLine.contains(","))
							{
								vars = varsLine.split(",");
							}
							else
							{
								vars = new String[1];
								vars[0]=varsLine;
							}
						}
						else
						{
							String[] equalSplit = varsLine.split("=");
							vars = new String[1];
							vars[0]=equalSplit[0];
						}
						
						int index = vars.length;
						for(int i = 0; i<index ;i++)
						{
							int equalInd = vars[i].indexOf("=");
							String varName = null ;
							if(equalInd != -1) 
							{
								varName = vars[i].substring(0,equalInd);
								varName = varName.trim();
							}
							else
							{
								varName=vars[i];
							}
							
							if(varName.endsWith(";"))
							{
								String localVar = varName.substring(0,varName.length()-1).trim();
								gLocalVars.add(localVar);
								if(!isLocalNamingConventionFollowed(localVar))
								{
									gLocalVNCSB.append(filePath+","+"At line: "+ lineNumber+","+"'"+localVar+"'"+" is not following the Local Variable naming convention"+","+"Low"+"\n") ;
								}
							}
							else
							{
								String localVar = varName.trim();
								gLocalVars.add(localVar);
								
								if(!isLocalNamingConventionFollowed(localVar))
								{
									gLocalVNCSB.append(filePath+","+"At line: "+ lineNumber+","+"'"+localVar+"'"+" is not following the Local Variable naming convention"+","+"Low"+"\n") ;
								}
							}
						}							
					}
					  
					  
					// detect the usage of variable -
					if(!(gCommentOpen) && !(gStatementOpen) && 
					   !(gIfdefOpen)
					   && !trimmedLine.matches("^local[\\s]*.*")  
					   && !trimmedLine.matches( "^--.*")  
					   && !trimmedLine.matches( "^for[\\s]*.*")
					   && !trimmedLine.matches( "^while[\\s]*[(].*") 
					   && !trimmedLine.matches( "while[\\s]"))  
					{	  
						int equalInd = trimmedLine.indexOf("=");
						if(equalInd != -1 && 
					  	   trimmedLine.substring(0, equalInd).indexOf(".", 1) == -1 && 
						   trimmedLine.indexOf( "[") == -1 &&
						   trimmedLine.indexOf( "~=") == -1 && 
						   trimmedLine.indexOf( "==") == -1 &&
						   trimmedLine.indexOf( "<=") == -1 &&
						   trimmedLine.indexOf( ">=") == -1 &&
						   trimmedLine.indexOf( "(") == -1 && !isBraceStarted)
						{	  
							// a variable is being assigned a value
							String varName = trimmedLine.substring(0, equalInd) ;
							varName = varName.trim();
							
							// check if this is part of local variable declarations
							if(!gLocalVars.contains(varName)) 
							{
								// this variable is not declared to be local
								if(!isGlobalNamingConventionFollowed(varName))
								{
									gGlobalVNCSB.append(filePath+","+"At line: "+lineNumber+","+"'"+varName+"'"+" is not following the Global Variable naming convention"+","+"Medium"+"\n") ;
								}
							}
					  	}
					}
					  
					// find for the hardcoded strings.
					if(trimmedLine.indexOf( "=", 1) != -1 && trimmedLine.indexOf( "=", 1) > 1 && !isBraceStarted
					   && !(trimmedLine.matches("^if[\\s]*.*"))&& !(trimmedLine.matches("^elseif[\\s]*.*")) && !(trimmedLine.matches("^[\\s]*--.*"))
					   && !(trimmedLine.contains(".png"))&& !(trimmedLine.contains("serviceID")) && !(trimmedLine.endsWith("\"\"")))
					{
						if (trimmedLine.contains(","))
						{
							trimmedLine = trimmedLine.replace(",", " ");
						}
						
						if(trimmedLine.contains("\""))
						{
							trimmedLine = trimmedLine.replace("\"", "\"\"");
					  	}
						
						String[] splittedString = trimmedLine.split("[=]");
					  	if(splittedString.length==2 )
					  	{
						  	if(splittedString[1].matches("^[\\s]*\".*\"$") && !splittedString[1].matches("^[\\s]*\"\"")) 
						  	{
						  		gHardCodedSB.append(filePath+","+"At line:"+lineNumber+","+"\""+trimmedLine+"\""+" Hardcoded Data is found"+","+"High"+"\n");
						  	}
					  	}
					} 
					  
					count++;
					
					// This logic must be here. We have to pop the braces after checking for all conditions.
					if(trimmedLine.contains("}"))
					{
						if(!braceStartTokens.isEmpty())
						{
							braceStartTokens.pop();
						}
					  
						if(braceStartTokens.size()==0)
						{
							isBraceStarted = false;
						}
					}	
				
					//Check for window.alert is the last line of the code
					if(line.contains("--"))
					{
						flag=1;
					}
					
					if(line.contains("--"))
					{
						flag=0;
						continue;
					}
					
					if(flag==0)
					{
						if(previousline.contains("window.Alert")&& !line.equals("end"))
						{
							gwindowAlertSB.append(filePath+","+"At Line:"+lineNumber+","+"window.Alert should be the last statement "+","+"Low"+"\n"); 
						}					  						  
						previousline=line;
					}
				}// end of while : EOF reached	
				
				// close the formatted lua file writer
				if(formattedLuaBfrWriter != null)
				{
					formattedLuaBfrWriter.flush();
					formattedLuaBfrWriter.close();
				}
				
				if(br != null)
					br.close();
			}
			 
			String globalNamingStr = gGlobalVNCSB.toString();
		    String localNamingStr = gLocalVNCSB.toString();
		    String gIncorrectElseStr = gIncorrectElseSB.toString();
		    String gLineLenStr = gLineLenSB.toString();
		    String gGlobalVarStr = gGlobalVarSB.toString();
		    String gHardCodedStr = gHardCodedSB.toString();
		    String gFunLevelCommentsMessageStr = gFunLevelCommentsSB.toString();
		    String gPrintStatStr = gPrintStatSB.toString();
		    String gSetSegmentDataInLoopStr = gSetSegmentDataInLoop.toString();
		    String gTrueFalseComparisonsStr = gTrueFalseComparisons.toString();
		    String gFunctionLinesStr = gFunctionLinesSB.toString();
		    String gwindowAlertStr = gwindowAlertSB.toString();
			
			if(!gSetSegmentDataInLoopStr.isEmpty())
			{
				Util.printToConsole("Writing the segment in loop errors."); 
		    	output = new BufferedWriter(new FileWriter(file, true));
		    	output.append(gSetSegmentDataInLoopStr);
		    	output.flush();
		    	output.close();
		    	output = null;
		    }
			
			if(!gTrueFalseComparisonsStr.isEmpty())
			{
				Util.printToConsole("Writing the true/false comparison errors.");
		    	output = new BufferedWriter(new FileWriter(file, true));
		    	output.append(gTrueFalseComparisonsStr);
		    	output.flush();
		    	output.close();
		    	output = null;
		    }
			
			if(!gFunLevelCommentsMessageStr.isEmpty())
			{
				Util.printToConsole("Writing the function level comment errors.");
		    	output = new BufferedWriter(new FileWriter(file, true));
		    	output.append(gFunLevelCommentsMessageStr);
		    	output.flush();
		    	output.close();
		    	output = null;
		    }
			
		    if(!gGlobalVarStr.isEmpty())
		    {
		    	Util.printToConsole("Writing the global variable errors.");
		    	output = new BufferedWriter(new FileWriter(file, true));
		    	output.append(gGlobalVarStr);
		    	output.flush();
		    	output.close();
		    	output = null;
		    }
		    
		    if(!gPrintStatStr.isEmpty())
		    {
		    	Util.printToConsole("Writing the print statement errors.");
		    	output = new BufferedWriter(new FileWriter(file, true));
		    	output.append(gPrintStatStr);
		    	output.flush();
		    	output.close();
		    	output = null;
		    }
		    
		    if(!gIncorrectElseStr.isEmpty())
		    {
		    	Util.printToConsole("Writing the else if errors.");
		    	output = new BufferedWriter(new FileWriter(file, true));
		    	output.append(gIncorrectElseStr);
		    	output.flush();
		    	output.close();
		    	output = null;
		    }
		    
		    if(!gHardCodedStr.isEmpty())
		    {
		    	Util.printToConsole("Writing the hard-coded string errors.");
		    	output = new BufferedWriter(new FileWriter(file, true));
		    	output.append(gHardCodedStr);
		    	output.flush();
		    	output.close();
		    	output = null;
		    }
		    
		    if(!gLineLenStr.isEmpty())
		    {
		    	Util.printToConsole("Writing the line length errors.");
		    	output = new BufferedWriter(new FileWriter(file, true));
		    	output.append(gLineLenStr);
		    	output.flush();
		    	output.close();
		    	output = null;
		    }
		    
		    if(!globalNamingStr.isEmpty())
		    {
		    	Util.printToConsole("Writing the global variable naming errors.");
		    	output = new BufferedWriter(new FileWriter(file, true));
		    	output.append(globalNamingStr);
		    	output.flush();
		    	output.close();
		    	output = null;
		    }
		    
		    if(!localNamingStr.isEmpty())
		    {
		    	Util.printToConsole("Writing the local variable naming errors.");
		    	output = new BufferedWriter(new FileWriter(file, true));
		    	output.append(localNamingStr);
		    	output.flush();
		    	output.close();
		    	output = null;
		    }
		    
		    if(appMiddlewareCount==1)
		    {
		    	Util.printToConsole("Writing the appmiddleware usage.");
		    	output = new BufferedWriter(new FileWriter(file, true));
		    	output.append("You have used appmiddlewaresecureinvoker "+appMiddlewareCount+" number of times in the application");
		    	output.flush();
		    	output.close();
		    	output = null;
			}
		    
		    if(!gFunctionLinesStr.isEmpty())
		    {
		    	Util.printToConsole("Writing the function size.");
		    	output = new BufferedWriter(new FileWriter(file, true));
		    	output.append(gFunctionLinesStr);
		    	output.flush();
		    	output.close();
		    	output = null;
		    }
		    
		    if(!gwindowAlertStr.isEmpty())
		    {
		    	Util.printToConsole("Writing the window.Alert defining error.");
		    	output = new BufferedWriter(new FileWriter(file, true));
		    	output.append(gwindowAlertStr);
		    	output.flush();
		    	output.close();
		    	output = null;
		    }
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			Util.printToConsole(e.getMessage());
			Util.printToConsole(Util.stackTraceToString(e));
		}
		finally
		{
			try 
			{
				if(output != null)
					output.close();				
			}
			catch (IOException e) 
			{
				e.printStackTrace();
				//Util.printToConsole("IOException"+e);
			}
		}
		long endTime = System.currentTimeMillis();
		Util.printToConsole("Total Time Taken for Processing to find common mistake is ...... "+(endTime-startTime)+" ms");
	}

	private static boolean isGlobalNamingConventionFollowed(String globalVar) 
	{
		if(globalVar.startsWith("g") || globalVar.startsWith("G"))
			return true;
		else
			return false;
	}

	private static boolean isLocalNamingConventionFollowed(String localVar) 
	{
		if(localVar.startsWith("l") || localVar.startsWith("L"))
			return true;
		else
			return false;
	}
}