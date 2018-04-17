package com.kony.tag.js.codereview.tasks.jsreview.impl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.TagToolException;
import com.kony.tag.js.codereview.config.ReviewComment;
import com.kony.tag.tasks.JsReviewMasterTask;

public class GlobalVariablesRule extends JsReviewMasterTask 
{
	public static final String PROPS_RULE_NAME = "rules.js.009.global_variables_rule";
	public static String GLOBAL_VARIABLE_FOUND = "Prefix the Global Variable name with : ";
	private static final String SHORT_DESC = "Global Variable naming convention not followed";
	public static final String RULE_ID = "JS-009";
	public static final String PROPS_GLOBAL_VAR_PREFIX = "rules.js.009.global_variable_prefix";
	
	private static String GBL_VAR_PREFIX = BLANK; 
	private String tmpGblVarName = null;

	HashSet<String> gLocalVars = new HashSet<String>();	
	HashSet<String> gGlobalVars = new HashSet<String>();	

	@Override
	protected boolean isLineSplitNeeded(){return true;}

	public GlobalVariablesRule(ProjectConfig codeReviewConfig) 
	{
		super(codeReviewConfig);
		String[] ruleDescriptions = {SHORT_DESC};
		super.init(PROPS_RULE_NAME, RULE_ID, ruleDescriptions, 1);

		init();
	}

	public void init()
	{		
		gGlobalVars.clear();
		GBL_VAR_PREFIX = getProjectConfig().getCodeReviewProperty(PROPS_GLOBAL_VAR_PREFIX);
		
		if (GBL_VAR_PREFIX != null){
			GBL_VAR_PREFIX = GBL_VAR_PREFIX.trim();
		}
	}

	//code for listing global variables
	@Override
	protected List<ReviewComment> reviewSingleJSLine(String fileName, int lineNum, int lastLineNum, String line, int passNumber, String currFunctionName, boolean functionEndFlag,int prevCommentedLinesCount) throws TagToolException 
	{
		gGlobalVars.clear();

		if(fileName!= null && fileName != ""){

			if(!functionEndFlag){
				String trimLine = line.trim();
				// detect the declaration of local variables
				if (trimLine.startsWith("var ")) 
				{
					String varsLine = trimLine.substring(4); //sub string after index i
					String[] vars=null;

					String[] equalSplit = varsLine.trim().split("=");
					vars = new String[1];

					vars[0]=equalSplit[0];

					int index = vars.length;
					for(int i = 0; i<index ;i++)
					{
						String comaSplit[] = vars[i].split(",");
						if(comaSplit.length > 1)
						{
							for(int j=0; j < comaSplit.length;j++)
							{
								String varName = null ;
								String localVar = null;
								varName = comaSplit[j];

								if(varName.endsWith(";"))
								{
									localVar = varName.substring(0,varName.length()-1).trim();																	
								}
								else
								{
									localVar = varName.trim();								
								}
								gLocalVars.add(localVar);	
							}

						}
						else
						{
							String varName = null ;
							String localVar = null;
							varName=vars[i];							

							if(varName.endsWith(";"))
							{
								localVar = varName.substring(0,varName.length()-1).trim();														
							}
							else
							{
								localVar = varName.trim();
							}
							gLocalVars.add(localVar);			
						}						
					}
				}
				else
				{
					//Lines doesnot start with var : Global Vars
					if((trimLine.contains("=") || trimLine.contains(" = ") || trimLine.contains(" =") || trimLine.contains("= ")) && !(trimLine.contains("==") || trimLine.contains(" == ") || trimLine.contains(" ==") || trimLine.contains("== "))){

						String[] vars=null;
						String[] equalSplit = trimLine.split("=");
						vars = new String[1];

						vars[0]=equalSplit[0];						
						String trimVar = vars[0].trim();

						if(lineValidator(trimVar)){
							if(!(gLocalVars.contains(trimVar)))
							{
								gGlobalVars.add(trimVar);																							
							}
						}
					}
				}				

			}else{
				//End of function
				gLocalVars = null;
				gLocalVars = new HashSet<String>();
			}

		}

		Iterator<String> iter = gGlobalVars.iterator();	

		while(iter.hasNext()) 
		{
			tmpGblVarName = iter.next().toString();
			
			// Report all global variables not confirming to the standard prefix for global variables
			// print all global variables if the prefix is blank 
			
			
			if (GBL_VAR_PREFIX == null || GBL_VAR_PREFIX.length()==0 || !tmpGblVarName.startsWith(GBL_VAR_PREFIX)) {
				addWarning(GLOBAL_VARIABLE_FOUND+GBL_VAR_PREFIX, SHORT_DESC, tmpGblVarName, fileName, lineNum, SEV_LOW, RULE_ID);
			}
		}	

		return getComments();
	}

	private static boolean lineValidator(String line) 
	{
		Pattern pattern;
		Matcher matcher;
		String USERNAME_PATTERN = "^[a-zA-Z0-9_]+$";
		pattern = Pattern.compile(USERNAME_PATTERN);

		matcher = pattern.matcher(line);
		return matcher.matches();
	}
}