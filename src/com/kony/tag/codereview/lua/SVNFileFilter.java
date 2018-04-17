package com.kony.tag.codereview.lua;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Author : TAG Group
 */
public class SVNFileFilter implements FilenameFilter 
{
	public boolean accept(File arg0, String arg1) 
	{
		//Util.printToConsole("**************** "+arg1);
		if(arg1.contains(".svn"))
		{
			return false;
		}
		else
		{
			return true;
		}
	}
}
