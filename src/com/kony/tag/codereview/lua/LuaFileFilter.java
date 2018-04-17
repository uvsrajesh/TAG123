package com.kony.tag.codereview.lua;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Author : TAG Group
 */
public class LuaFileFilter implements FilenameFilter 
{
	public boolean accept(File dir, String name) 
	{
		if(name.endsWith(".lua"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
