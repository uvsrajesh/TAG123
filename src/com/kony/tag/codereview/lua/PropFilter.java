package com.kony.tag.codereview.lua;

import java.io.File;
import java.io.FilenameFilter;

public class PropFilter implements FilenameFilter 
{
	@Override
	public boolean accept(File file, String name) 
	{	
		return name.endsWith(".prop"); 
	}
}
