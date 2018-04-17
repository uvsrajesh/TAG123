package com.kony.tag.codereview.lua;

import java.io.File;
import java.io.FilenameFilter;

public class KLFileFilter implements FilenameFilter
{
	public boolean accept(File dir, String name) 
	{
		//Util.printToConsole("**************** "+arg1);
		if(name.endsWith(".kl"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
