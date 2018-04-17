package com.kony.tag.js.codereview.config;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import com.kony.tag.js.codereview.util.ReviewPropertiesUtil;

/**
 * Author : TAG Group
 */
public class ExcludeFileFilter implements FilenameFilter 
{
	private final static String ALL = "ALL";
	private final static String BLANK = "";
	private final static String KL_FILE = ".kl";

	private List<String> ignoreFileList = null;
	
	public ExcludeFileFilter() {
		ignoreFileList = ReviewPropertiesUtil.getFilesToIgnore(ALL);
		
		if (ignoreFileList == null) {
			ignoreFileList = new ArrayList<String>();
		}
	}
	
	public boolean accept(File arg0, String arg1) 
	{
		//Util.printToConsole("**************** "+arg1);
		String argVal = arg1.trim().toLowerCase();
		argVal = argVal.replaceAll(KL_FILE, BLANK); 
			
		if(arg1.contains(".svn") || ignoreFileList.contains(argVal))
		{
			return false;
		}
		else
		{
			return true;
		}
	}
}
