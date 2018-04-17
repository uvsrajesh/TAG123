package com.kony.tag.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class RemovePrints implements FileFilter{
	@Override
	public boolean accept(File pathname) {
		// TODO Auto-generated method stub
		if(pathname.getName().endsWith("js"))
			return true;
		else
			return false;
	}
	
	// returns the number of occurrences of searchStr in sourceStr
	private static int getCountOf(String sourceStr, String searchStr){
		
		int searchInd = -1;
		int lastInd = 0;
		int numOccurrences = 0;
		while((searchInd = sourceStr.indexOf(searchStr, lastInd)) >= 0){
			numOccurrences++;
			lastInd = searchInd + searchStr.length();
		}
		return numOccurrences;
	}
	
	public static void main(String[] args)
	{
		  
		final String STRING_LITERAL_REGEX = "\"[^\"]*\"";
		final String STRING_LITERAL_REGEX2 = "'[^\']*'";
		final String KONY_PRINT_REGEX = "kony\\.print *\\(.*\\)[;]*";
		final String KONY_MULTI_LINE_PRINT_REGEX = "^kony\\.print *\\(.*";
		final String IF_STMT_REGEX = "if *\\(.*\\)";
		final String ELSE_IF_STMT_REGEX = "else +if *\\(.*\\)";
		final String ELSE_STMT_REGEX = "[\\}]* *else";
		final String IF_KONY_PRINT_STMT_REGEX = "if *\\(.*\\)" + " *" + KONY_PRINT_REGEX;
		final String ELSE_IF_KONY_PRINT_STMT_REGEX = "else +if *\\(.*\\)" + " *" + KONY_PRINT_REGEX;
		final String ELSE_KONY_PRINT_STMT_REGEX = "else" + " *" + KONY_PRINT_REGEX;

		RemovePrints filterObj = new RemovePrints();
		String path="";
		PrintWriter out=null;
		String everything = "";
		String prevLine = "";
 	   	String prevTrimmedLine = "";
 	   	String trimmedLine = "";
		int numOpenParantheses = 0;
		int numCloseParantheses = 0;
		boolean isMultiLinePrint = false;
		FileReader fr =null;
		BufferedReader br =null;
		String fileName = null;
		//String folderPath = args[0];
		String folderPath = "D:\\KonyWS5.0\\TMBUI\\modules\\test";
		
		
		int lineNumber = 0;
		ArrayList<String> printsRemoved = new ArrayList<String>();
		
		
		final File folder = new File(folderPath);
		final List<File> fileList = Arrays.asList(folder.listFiles());  
		
	    for(int i=0;i<fileList.size();i++){
	    	File f= fileList.get(i);	
			try {
				if(!filterObj.accept(f)){
					// skip non-JS files
					continue;
				}
				fileName = f.getName();
	    		path = folderPath + "\\newJS\\"+fileName;
		    	//System.out.println("Path is "+path);
		        out = new PrintWriter(path);
		        fr = new FileReader(f.getAbsolutePath());
		 	   	br = new BufferedReader(fr); 
		 	   	prevLine = "";
		 	   	lineNumber = 0;
		 	   	while ((everything = br.readLine()) != null) {
		 	   		lineNumber++;
					trimmedLine = everything.trim();
					// remove string literals to make regular expressions easy
					trimmedLine = trimmedLine.replaceAll(STRING_LITERAL_REGEX, "");
					trimmedLine = trimmedLine.replaceAll(STRING_LITERAL_REGEX2, "");
					prevTrimmedLine = prevLine.trim();
					//Check for single kony.print statement in if/else/else-if
					if( (prevTrimmedLine.matches(ELSE_STMT_REGEX) || prevTrimmedLine.matches(IF_STMT_REGEX) || prevTrimmedLine.matches(ELSE_IF_STMT_REGEX)) &&
							trimmedLine.matches(KONY_PRINT_REGEX)){
						// don't write previous line as print statement is the only line in if/else/else if conditions
						// set prevLine to empty string so that print statement is not written in the next loop
						//System.out.println("Removed line:" + prevLine);
						//System.out.println("Removed line:" + everything);
						if(prevTrimmedLine.startsWith("}")){
							//System.out.println("Replaced with:}");
							prevLine = "}";
						}else{
							prevLine = "";							
						}
						printsRemoved.add(fileName + ":" + lineNumber + ":" + everything);
						continue;
					}
					// Check for single kony.print on the same line as if/else/else-if
					if( trimmedLine.matches(ELSE_KONY_PRINT_STMT_REGEX)
							|| trimmedLine.matches(IF_KONY_PRINT_STMT_REGEX)
							|| trimmedLine.matches(ELSE_IF_KONY_PRINT_STMT_REGEX)){
						// kony.print on the same line as if/else/else if
						//System.out.println("Removed line:" + everything);
						printsRemoved.add(fileName + ":" + lineNumber + ":" + everything);
						continue;
					}
					// Check for multi-line kony.print statements
					if(trimmedLine.matches(KONY_MULTI_LINE_PRINT_REGEX)){
						numOpenParantheses = getCountOf(trimmedLine, "(");
						numCloseParantheses = getCountOf(trimmedLine, ")");
						if(numOpenParantheses > numCloseParantheses){							
							// multi-line print statement
							isMultiLinePrint = true;
							//System.out.println("Removed Multi-line Print:" + everything);
							// don't write this line
							printsRemoved.add(fileName + ":" + lineNumber + ":" + everything);
							continue;
						}
					}
					if(isMultiLinePrint){
						numOpenParantheses += getCountOf(trimmedLine, "(");
						numCloseParantheses += getCountOf(trimmedLine, ")");						
						if(numOpenParantheses == numCloseParantheses){
							// end of multi-line print
							isMultiLinePrint = false;
							numOpenParantheses = 0;
							numCloseParantheses = 0;
						}
						// don't write as this line is part of multi-line print statement
						//System.out.println(everything);
						printsRemoved.add(fileName + ":" + lineNumber + ":" + everything);
						continue;
					}
					out.println(prevLine);
					if(trimmedLine.contains("kony.print")){
						printsRemoved.add(fileName + ":" + lineNumber + ":" + everything);
					}
					everything = everything.replaceAll(KONY_PRINT_REGEX, "");
					prevLine = everything;
				}
		 	   out.println(prevLine);
	    	}catch (Exception e) {
				e.printStackTrace();
			}finally{
				try{
					if(null != br)
						br.close();
					if(null != fr)
						fr.close();
					if(null != out)
						out.close();
					
				}catch(Exception e){
				}
			}
		} 
	    Iterator<String> printsItr = printsRemoved.iterator();
	    while(printsItr.hasNext()){
	    	System.out.println(printsItr.next());
	    }
	}
}



// Backup Logic

/*
// remove the inline comments
trimmedLine = trimmedLine.replaceAll("\"[^\"]*\"", "");
trimmedLine = trimmedLine.replaceAll("'[^\']*'", "");
if(trimmedLine.contains("//")){
	// empty out literal strings
	int commentInd = trimmedLine.indexOf("//");
	if(commentInd > 0)
		trimmedLine = trimmedLine.substring(0, commentInd).trim();
}
if(trimmedLine.startsWith("kony.print(")){
	// is single line comment
	if(trimmedLine.endsWith(";") || trimmedLine.endsWith(")")){
		if(prevLine.equals("else") || prevLine.startsWith("if")){
			if(prevLine.endsWith("{")){
					continue;
			}
		}else{
			continue;
		}
	}else{
		// multi-line kony.print statements
		isMultiLinePrint = true;
		continue;
	}
}
if(isMultiLinePrint){
	if(trimmedLine.endsWith(";") || trimmedLine.endsWith(")")){
		isMultiLinePrint = false;
	}
	continue;
}
if(trimmedLine.startsWith("//kony")){
	if(trimmedLine.endsWith(";") || trimmedLine.endsWith(")")){
		continue;
	}
}
if(trimmedLine.matches("^//\\s*kony\\.print.*")){
	continue;
}
*/

