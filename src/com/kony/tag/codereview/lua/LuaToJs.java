package com.kony.tag.codereview.lua;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class LuaToJs
{
	public static boolean ValidateTable(String smallTable)
	{
		boolean mixedTable=false;
		if(!smallTable.contains("="))
		{
			return false;
		}
		else 
		{
			String splitArray[]={};
			splitArray=smallTable.split(",");
			for(int i=0;i<splitArray.length;i++)
			{
				if(!splitArray[i].startsWith("}")&& !splitArray[i].isEmpty())
				{
					if(splitArray[i].contains("="))
					{
						mixedTable=false;
					}
					else 
					{
						mixedTable=true;
						break;
					}
				}
			}
			
			return mixedTable;
		}
	}
	
	public static boolean tables(String tableLine)
	{
		Stack<Integer> startBracesIndex=new Stack<Integer>();
		char openingBraces='{';
		char closingBraces='}';
		String smallTable="";
		boolean mixedTable=false;
		
		for(int i=0;i<tableLine.length();i++)
		{
			if(tableLine.charAt(i)==openingBraces)
			{
				startBracesIndex.push(i);	
			}
			else if (tableLine.charAt(i)==closingBraces)
			{
				if(!startBracesIndex.isEmpty())
				{
					int StartBraces=startBracesIndex.pop();
					int CloseBraces=i+1;
					
					smallTable=(tableLine.substring(StartBraces, CloseBraces));					
					mixedTable=ValidateTable(smallTable);
					if(mixedTable)
					{
						return true;
					}
				}
			}
		}
		
		return false;
	}

	public static void checkForKeywords () throws IOException 
	{
		Util.printToConsole("checking for keywords in project");
		StringBuffer gKeywordsSB = new StringBuffer(1000);
		ArrayList<LuaFunction> functionNamesList=new ArrayList<LuaFunction>();
		ArrayList<String> skinList=new ArrayList<String>();

		ArrayList <String> listOfKeywords= new ArrayList <String>();
		try
		{
			functionNamesList=Util.getAllFunctionNames();
			skinList=CleanSkins.getSkinList();

			listOfKeywords.add("math");
			listOfKeywords.add("menu");
			listOfKeywords.add("volatile");
			listOfKeywords.add("Web");
			listOfKeywords.add("while");
			listOfKeywords.add("try");
			listOfKeywords.add("until");
			listOfKeywords.add("void");
			listOfKeywords.add("public");
			listOfKeywords.add("radiobuttongroup");
			listOfKeywords.add("checkboxgroup");
			listOfKeywords.add("class");
			listOfKeywords.add("combobox");
			listOfKeywords.add("contact");
			listOfKeywords.add("continue");
			listOfKeywords.add("Datagrid");
			listOfKeywords.add("default");
			listOfKeywords.add("do");
			listOfKeywords.add("double");
			listOfKeywords.add("ds");
			listOfKeywords.add("end");
			listOfKeywords.add("Else");
			listOfKeywords.add("elseif");
			listOfKeywords.add("Showloading");
			listOfKeywords.add("screen");
			listOfKeywords.add("skin");
			listOfKeywords.add("slider");
			listOfKeywords.add("static");
			listOfKeywords.add("Streaming");
			listOfKeywords.add("strictfp");
			listOfKeywords.add("string");
			listOfKeywords.add("super");
			listOfKeywords.add("switch");
			listOfKeywords.add("synchronized");
			listOfKeywords.add("Table");
			listOfKeywords.add("tabwidget");
			listOfKeywords.add("textarea");
			listOfKeywords.add("then");
			listOfKeywords.add("this");
			listOfKeywords.add("throw");
			listOfKeywords.add("Throws");
			listOfKeywords.add("timer");
			listOfKeywords.add("transient");
			listOfKeywords.add("true");
			listOfKeywords.add("enum");
			listOfKeywords.add("extends");
			listOfKeywords.add("False");
			listOfKeywords.add("final");
			listOfKeywords.add("finally");
			listOfKeywords.add("float");
			listOfKeywords.add("for");
			listOfKeywords.add("form");
			listOfKeywords.add("Function");
			listOfKeywords.add("gallery");
			listOfKeywords.add("i18n");
			listOfKeywords.add("if");
			listOfKeywords.add("image");
			listOfKeywords.add("imagestrip");
			listOfKeywords.add("In");
			listOfKeywords.add("implements");
			listOfKeywords.add("repeat");
			listOfKeywords.add("return");
			listOfKeywords.add("richtext");
			listOfKeywords.add("segui");
			listOfKeywords.add("Short");
			listOfKeywords.add("widget");
			listOfKeywords.add("window");
			listOfKeywords.add("native");
			listOfKeywords.add("net");
			listOfKeywords.add("new");
			listOfKeywords.add("Null");
			listOfKeywords.add("os");
			listOfKeywords.add("package");
			listOfKeywords.add("phone");
			listOfKeywords.add("popup");
			listOfKeywords.add("popupContextTable");
			listOfKeywords.add("Private");
			listOfKeywords.add("protected");
			listOfKeywords.add("eval");
			listOfKeywords.add("alert");


			for(LuaFunction element:functionNamesList ) 
			{				
				if(listOfKeywords.contains(element.functionName))
				{
					gKeywordsSB.append("keyWord used in function name " + element.functionName+"\n");									 
				}
			}	
			
			for(String skinName:skinList)
			{
				if(listOfKeywords.contains(skinName))
				{
					gKeywordsSB.append("keyWord used in function name " + skinName+"\n");
				}
			}
		}
		catch (CodeReviewException e) 
		{
			e.printStackTrace();
			Util.printToConsole("can not load function names and Skin Names");
		}
		catch (IOException e)
		{
			Util.printToConsole("Skin file not found........moving to next Step");
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Util.printToConsole("checking for keywords failed.......Moving to next step");
		}
		finally 
		{	
			Writer outputWrtr = null;
			String gKeywordsStr=gKeywordsSB.toString();
			if(gKeywordsStr!=null)
			{
				try 
				{
					File file = new File(Util.Output_DIR+"\\LuaToJs.csv");
					outputWrtr = new BufferedWriter(new FileWriter(file, true));
					outputWrtr.append(gKeywordsStr);
					outputWrtr.flush();
					outputWrtr.close();
				}
				catch(IOException e)
				{
					Util.printToConsole("Error Writing to File");
				}
			}
			uniqueName(functionNamesList,listOfKeywords);		
		}
	}
	
	public static void uniqueName(ArrayList<LuaFunction> functionNamesList ,ArrayList<String> listOfKeywords)
	{
		Set<String> uniqueNameFunction =new HashSet<String>();
		StringBuffer gDuplicateFunctionNamesSB = new StringBuffer(1000);

		if(functionNamesList.size()>0) 
		{
			try 
			{
				for(LuaFunction element:functionNamesList ) 
				{
					if(!uniqueNameFunction.contains(element.functionName))
					{
						uniqueNameFunction.add(element.functionName); 
					}
					else 
						gDuplicateFunctionNamesSB.append(element.functionName +" is repeated \n");
				} 	
				
				if(uniqueNameFunction.size()<functionNamesList.size())
				{
					//Util.printToConsole("Total unique function in Project "+uniqueNameFunction.size());
					Util.printToConsole("duplicate names exist in function name in function");
					uniqueNameFunction.clear();
				} 
				else 
				{
					//System.out.println("no duplicates");
				}
			} 
			catch (Exception e)
			{
				e.printStackTrace();
				Util.printToConsole("error finding duplicate Function Names names");
			}
		} 
		else 
		{
			Util.printToConsole("No functions found in project");
		} 

		Writer output=null;
		String gDuplicateFunctionNamesStr=gDuplicateFunctionNamesSB.toString();
		if(gDuplicateFunctionNamesStr!=null) 
		{
			try 
			{
				Util.printToConsole("Writing Duplicate Function Names");
				File file = new File(Util.Output_DIR+"\\LuaToJs.csv");
				output = new BufferedWriter(new FileWriter(file, true));
				output.append(gDuplicateFunctionNamesStr);
				output.flush();		    	
			}
			catch(IOException e)
			{
				Util.printToConsole("Error Writing Duplicate Functions to File");
			}
			finally 
			{
				try 
				{
					output.close();
				}
				catch (IOException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					Util.printToConsole("Error Closing The Files");
				}
			}
		}
	}
	
	public static void luaToJsPorting()
	{
		StringBuffer gLinesOutsideFunctionSB = new StringBuffer(1000);
		StringBuffer gReturnStatementsSB = new StringBuffer(1000);
		StringBuffer gSynchronousCallsSB = new StringBuffer(1000);
		StringBuffer gEqualParamsSB = new StringBuffer(1000);
		StringBuffer gMixedTablesSB=new StringBuffer(1000);
		Writer output=null;
		
		long startTime = System.currentTimeMillis();	
		String luaDirName =  Util.tempCodeDir; // source Lua files path
		ArrayList<LuaFunction> tmpFunctionNamesList=new ArrayList<LuaFunction>();
	
		try 
		{
			tmpFunctionNamesList=Util.getAllFunctionNames();
		}
		catch(CodeReviewException e) 
		{
			e.printStackTrace();
			Util.printToConsole("couldn't load Function List .....trying other operations");
		}
		
		Stack<String> ifLoopTokens = new Stack<String>();
		Stack<String> functionTokens = new Stack<String>();
		String startFunction = "function ";	
		int countElse=0;
		int countReturn=0;
		
		try
		{
			File[] luaFolder = new File(luaDirName).listFiles(new LuaFileFilter());
			for( File fileName : luaFolder) 
			{
				LineNumberReader br = null;		
				String line = "";	
				boolean gFunctionOpen = false;
				boolean gCommentOpen=false;
				boolean ifStart=false;
				boolean containsReturn=false;
				boolean containsReturnIf=false;
				boolean containsReturnElse=false;
				boolean elseStart=false;
				boolean tableOpen=false;
				Stack<String> tableStack = new Stack<String>();

				String filePath = fileName.getName();
				File formattedLuaFile = new File(Util.formattedLuaDir+"\\" + filePath);
				formattedLuaFile.createNewFile();
				ifLoopTokens.clear();
				functionTokens.clear();
				String tableLine= "";
				BufferedWriter formattedLuaBfrWriter = new BufferedWriter(new FileWriter(formattedLuaFile));
				br = new LineNumberReader(new FileReader(fileName));

				//reading till last line		
				while(null != (line = br.readLine())) 
				{
					int lineNumber = br.getLineNumber();
					String trimmedLine = line;
					String commentPart = "";			
					boolean functionJustClosed=false;
					boolean commentJustClosed=false;
					boolean tableJustClosed=false;
				
					// remove the comment part of the line if any
					int commentInd = trimmedLine.indexOf("--");
					if(commentInd >= 0)
					{
						commentPart = trimmedLine.substring(commentInd);
						trimmedLine = trimmedLine.substring(0, commentInd);
					}
					
					trimmedLine = trimmedLine.trim();		
					int lineLen = trimmedLine.length();
					
					//checking for block comments
					if(line.contains("--[[")&&!gCommentOpen ) 
					{
						gCommentOpen=true;
						continue;
					}

					if(line.contains("--]]"))
					{
						gCommentOpen=false;
						commentJustClosed=true;
					}

					if(gCommentOpen == true)
					{
						continue;
					}

					//checking for tables and combining to one line
					//New Code
					if(!gCommentOpen && trimmedLine.contains("{")) 
					{
						tableOpen=true;				
					}
					
					if(tableOpen)
					{
						char openingBraces='{';
						char closingBraces='}';
						for(int i=0;i<trimmedLine.length();i++)
						{
							if(trimmedLine.charAt(i)==openingBraces)
							{
								tableStack.push("{");	
							}
							
							if(trimmedLine.charAt(i)==closingBraces)
							{
								if(!tableStack.isEmpty())
								{
									tableStack.pop();
									if(tableStack.isEmpty())
									{
										tableJustClosed=true;
										tableOpen=false;
									}
								}
							}
						}
					}
					
					if(tableStack.isEmpty()||!gFunctionOpen)
					{
						tableOpen=false;
					}
					
					if(tableOpen ||tableJustClosed)
					{
						tableLine=tableLine+trimmedLine;
					}
					
					if(tableJustClosed)
					{
						boolean tableType=tables(tableLine);
						if(tableType)
						{
							gMixedTablesSB.append("mixed table at:"+filePath+":"+lineNumber+"\n");
							//System.out.println("mixed table at:"+filePath+":"+lineNumber);
							//System.out.println(tableLine);
						}

						tableLine="";
					}
					//end of new code

					/*	old code
					 * int indexOfBraces=trimmedLine.lastIndexOf("}");
					int indexOfBracket=trimmedLine.lastIndexOf(")");
					int indexOfColon=trimmedLine.lastIndexOf(";");
					if((!gCommentOpen && (indexOfBraces==(lineLen-1)))||
					   (!gCommentOpen && (indexOfBracket==(lineLen-1)))||
					   (!gCommentOpen && (indexOfColon==(lineLen-1)))) 
					{
					 */

					//checking start of function
					if(!gCommentOpen && (trimmedLine.contains("function ")||trimmedLine.contains("function("))) 
					{
						gFunctionOpen=true;
						functionTokens.push("function");
						containsReturn=false;					
					}
					
					//adding the ifLoopTokens
					if(trimmedLine.startsWith("if")&& gFunctionOpen && !gCommentOpen) 
					{
						ifStart=true;
						containsReturnIf=false;
						containsReturnElse=false;
						countElse=0;
						countReturn=0;
						ifLoopTokens.add("if ");			
					}
					
					if(trimmedLine.startsWith("for ")&& gFunctionOpen && !gCommentOpen) 
					{
						ifLoopTokens.add("for");
					}
					
					if(trimmedLine.startsWith("while ")&& gFunctionOpen && !gCommentOpen) 
					{
						ifLoopTokens.add("while");
					}
					
					//poping the ifLoopTokens tokens
					if(!gFunctionOpen && !gCommentOpen) 
					{
						ifLoopTokens.clear();
					}
					
					if(gFunctionOpen && trimmedLine.endsWith("end") && !commentJustClosed  && !gCommentOpen) 
					{
						if(!ifLoopTokens.isEmpty()) 
						{
							ifLoopTokens.pop();
						}
						else if(ifLoopTokens.isEmpty()&& !functionTokens.isEmpty()) 
						{
							functionTokens.pop();	
						}
						
						if(ifLoopTokens.isEmpty()&& functionTokens.isEmpty())
						{
							gFunctionOpen=false;					
							functionJustClosed=true;
						}
					}

					// NOT WORKING
					/*	 		
 					//checking for table type			
					if(tableLine.contains("{") && !gCommentOpen && tableJustClosed)
					{
						int stop=tableLine.lastIndexOf("}");
						if(stop!=-1)
						{
							String Line=tableLine.substring(tableLine.indexOf("{")+1,stop);
							if(Line.contains("="))
							{
								int pos=0;
								int i=-1;
								int num=-1;
								int num1=-1;
								int num2=-1;
								int num3=-1;
								int num4=-1;
								int num5=-1;
								//	counting "="			
								while (pos != -1) 
								{
									pos = Line.indexOf("=", i + 1);
									num++;
									i = pos;
								}
								
								pos=0;
								i=-1;
								//	counting ","			
								while (pos != -1) 
								{
									pos = Line.indexOf(",", i + 1);
									num1++;
									i = pos;
								}									
								
								pos=0;
								i=-1;
								//	counting "{"
								 * 
								while (pos != -1) 
								{
									pos = Line.indexOf("{", i + 1);
									num2++;
									i = pos;
								}	
								
								pos=0;
								i=-1;
								//	counting "{"

								while (pos != -1) 
								{
									pos = tableLine.indexOf(",}", i + 1);
									num3++;
									i = pos;
								}	
								
								pos=0;
								i=-1;
								//	counting "{"

								while (pos != -1) 
								{
									pos = tableLine.indexOf("{}", i + 1);
									num4++;
									i = pos;
								}	
								
								pos=0;
								i=-1;
								//	counting "{{"

								while (pos != -1) 
								{
									pos = tableLine.indexOf("{{", i + 1);
									num5++;
									i = pos;
								}	


								if(num!=(num1+1+num2-(num3+num4+num5))){
								//System.out.println("mixed table "+ filePath +" line No "+lineNumber);
								//System.out.println(tableLine);
								//System.out.println(Line);
								//System.out.println(num +" "+num1+" "+num2+" "+num3+" "+num4+" "+num5);
							}
						}
					}  
				} 
				
				if(!tableOpen && tableJustClosed)
				{
					tableLine="";
				}
				 */
					//Checking for Lines Outside Function
					if(!gFunctionOpen && !gCommentOpen )
					{
						if(trimmedLine.equals("")|| trimmedLine.contains("end"))
						{  
						}
						else 
						{
							gLinesOutsideFunctionSB.append(filePath+","+"At Line:"+lineNumber+","+"Statement outside function  "+ trimmedLine+ "  \n");
						}					
					}

					//checking for Return Statements
					if(gFunctionOpen && !gCommentOpen) 
					{				 
						if(ifStart) 
						{						  
							if(trimmedLine.endsWith("end")) 
							{
								ifStart=false;
								elseStart=false;
							}
							
							if(trimmedLine.contains("else")||trimmedLine.contains("elseif"))
							{
								elseStart=true;
								countElse++;
							}	
							
							if(ifStart && !elseStart) 
							{
								if(trimmedLine.contains("return")) 
								{
									containsReturnIf=true;
								}
							}
							
							if(ifStart && elseStart) 
							{
								if(trimmedLine.contains("return")) 
								{
									containsReturnElse=true;
									countReturn++;
								}						 
							}	
						}	
						else 
						{
							if(trimmedLine.contains("return"))
							{
								containsReturn=true;
							}					   
						}				  
					}
					
					if(functionJustClosed) 
					{
						if(containsReturn) 
						{
							if(containsReturnIf && countElse==0) 
							{	
								containsReturnIf=false;
							}
							else if((containsReturnIf && !containsReturnElse && countElse!=0)||(!containsReturnIf && containsReturnElse && countElse!=0))
							{
								gReturnStatementsSB.append( filePath+"::"+lineNumber+":"+"Function ending here should return at every exit point:\n");
								containsReturnIf=false;
								containsReturnElse=false;
							}
							else if(containsReturnIf && containsReturnElse) 
							{
								containsReturnIf=false;
								containsReturnElse=false;
								if(countElse!=countReturn)
								{
									gReturnStatementsSB.append( filePath+"::"+lineNumber+":"+"Function ending here should return at every exit point:\n");
								}
							}
							containsReturn=false;
						}
						else 
						{
							if(containsReturnIf && countElse==0) 
							{
								gReturnStatementsSB.append( filePath+"::"+lineNumber+":"+"Function ending here should return at every exit point:\n");
								containsReturnIf=false;
							}
							else if((containsReturnIf && !containsReturnElse)||(!containsReturnIf && containsReturnElse))
							{
								gReturnStatementsSB.append( filePath+"::"+lineNumber+":"+"Function ending here should return at every exit point:\n");
								containsReturnIf=false;
								containsReturnElse=false;
							}
							else if(containsReturnIf && containsReturnElse) 
							{
								containsReturnIf=false;
								containsReturnElse=false;
								if(countElse!=countReturn)
								{
									gReturnStatementsSB.append( filePath+"::"+lineNumber+":"+"Function ending here should return at every exit point:\n");
								}
							}
						}
					}
					//checking for synchronous calls			 

					if((trimmedLine.contains("appmiddlewareinvoker"))&& !trimmedLine.contains("appmiddlewareinvokerasync")&& !gCommentOpen)
					{
						gSynchronousCallsSB.append( filePath+"::"+lineNumber+":"+"Sychronous calls used \n");
					}
					if(trimmedLine.contains("invokeservice"))
					{
						gSynchronousCallsSB.append( filePath+"::"+lineNumber+":"+"Sychronous calls used \n"); 
					}


					//checking for equal parameters
					for(LuaFunction element:tmpFunctionNamesList ) 
					{
						String paramList[]={};
						int paramCount=0;
						if(trimmedLine.contains(element.functionName+"(")&& !trimmedLine.contains(startFunction) && !gCommentOpen)
						{
							String Line=trimmedLine.substring(trimmedLine.indexOf(element.functionName));
							int start = Line.indexOf("(");
							int end = Line.indexOf(")");
						
							//System.out.println(line+" Start : "+start+" End: "+end);
							if(start!=-1 && end !=-1)
							{
								if(Line.contains(","))
								{
									paramList= (Line.substring(start+1,end)).split(",");
									paramCount=paramList.length;
								}
								else if(Line.substring(start+1,end).length()==0)
								{
									paramCount=0;
								}
								else 
									paramCount=1;

								if(paramCount!=element.paramCount)
								{
									gEqualParamsSB.append( filePath+"::"+lineNumber+":"+element.functionName+" : Parameter List not equal\n");
								}
							}	
						}
					}
				} // end of while : EOF reached	
				// close the formatted lua file writer

				if(formattedLuaBfrWriter != null)
				{
					formattedLuaBfrWriter.flush();
					formattedLuaBfrWriter.close();
				}
				
				if(br != null)
					br.close();
			}
		}
		catch (IOException e) 
		{
			e.printStackTrace();
			Util.printToConsole("Cannot read File");
		}
		finally 
		{
			try 
			{
				checkForKeywords();
			}
			catch (IOException e)
			{
				Util.printToConsole("Error checking for keywords in project");
			}
		}
		
		File file = new File(Util.Output_DIR+"\\LuaToJs.csv");
		String gLinesOutsideFunctionStr = gLinesOutsideFunctionSB.toString();
		String gReturnStatementsStr = gReturnStatementsSB.toString();
		String gSynchronousCallsStr = gSynchronousCallsSB.toString();
		String gEqualParamsStr =gEqualParamsSB.toString();
		String gMixedTableStr=gMixedTablesSB.toString();
		if(gMixedTableStr!=null) 
		{
			try 
			{
				Util.printToConsole("Writing the Mixed table Errors");
				output = new BufferedWriter(new FileWriter(file, true));
				output.append(gMixedTableStr);
				output.flush();
				output.close();
				output = null;
			}
			catch(IOException e)
			{
				Util.printToConsole("Error writing to file");
			}
		}
		
		if(gLinesOutsideFunctionStr!=null) 
		{
			try 
			{
				Util.printToConsole("Writing Statements Outside function Errors");
				output = new BufferedWriter(new FileWriter(file, true));
				output.append(gLinesOutsideFunctionStr);
				output.flush();
				output.close();
				output = null;
			}
			catch(IOException e)
			{
				Util.printToConsole("Error writing to file");
			}
		}
		
		if(gReturnStatementsStr!=null) 
		{
			try 
			{
				Util.printToConsole("Writing Return Statements Error");

				output = new BufferedWriter(new FileWriter(file, true));
				output.append(gReturnStatementsStr);
				output.flush();
				output.close();
				output = null;
			}
			catch(IOException e)
			{
				Util.printToConsole("Error Writing to files");
			}
		}
		
		if(gSynchronousCallsStr!=null) 
		{
			try 
			{
				Util.printToConsole("Writing Synchronous Error");
				output = new BufferedWriter(new FileWriter(file, true));
				output.append(gSynchronousCallsStr);
				output.flush();
				output.close();
				output = null;
			}
			catch(IOException e)
			{
				Util.printToConsole("Error Writing to files");
			}
		}
		
		if(gEqualParamsStr!=null) 
		{
			try 
			{
				Util.printToConsole("Writing functions with Unequal Params Error");

				output = new BufferedWriter(new FileWriter(file, true));
				output.append(gEqualParamsStr);
				output.flush();
				output.close();
				output = null;
			}
			catch(IOException e)
			{
				Util.printToConsole("Error writing to file");
			}
		}
		
		try 
		{
			if(output != null)
				output.close();
		}
		catch (IOException e)
		{
			Util.printToConsole("error closing the file");
		}
		
		long stopTime=System.currentTimeMillis();
		Util.printToConsole("total time taken for Lua To Js porting is "+(stopTime-startTime));
	}
}