package com.kony.tag.codereview.lua;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.ui.PartInitException;

public class CleanImages
{
	static String ProjectPath=Util.projectPath;
	static String ResourcesPath=ProjectPath+"\\resources";
	static String ResourcesBackupPath = Util.Output_DIR+"\\resourcesBackup";
	
	
	public static void main(String[] args) throws PartInitException, IOException
	{
		CleanImages.takeResourcesBackup();
	}
	
	public static void takeResourcesBackup() throws PartInitException, IOException
	{
		//File[] allResourceFiles = null;
		ArrayList<String> allResourceFiles = new ArrayList<String>();
		ArrayList<String> visitedPathList = new ArrayList<String>();

		if(new File(ResourcesBackupPath).exists()==true)
		{
			new File(ResourcesBackupPath).delete();
			new File(ResourcesBackupPath).mkdir();
		}
		else
		{
			new File(ResourcesBackupPath).mkdir();
		}
		
		if(new File(ResourcesPath).exists() == true)
		{
			File tempFile = null;	
			String currentPath = ResourcesPath;
			
			while(new File(currentPath).isDirectory()==true)
			{
				if(visitedPathList.contains(new File(currentPath).getAbsolutePath())==false)
				{
					for(int i=0;i<new File(currentPath).listFiles().length;i++)
					{
						tempFile = new File(currentPath).listFiles()[i];
						allResourceFiles.add(tempFile.getAbsolutePath());
						Util.printToConsole("Adding to allResourceFiles list:"+tempFile.getAbsolutePath());
						if(tempFile.isDirectory())
						{
							currentPath =  tempFile.getAbsolutePath();
							Util.printToConsole("current Path:"+currentPath);
						}
						else
						{
							if(visitedPathList.contains(tempFile.getAbsolutePath())==false)
							{
								visitedPathList.add(tempFile.getAbsolutePath());
								currentPath = tempFile.getParentFile().getAbsolutePath();
								Util.printToConsole("current item:"+currentPath);
							}						
						}				
					}
					
					if(visitedPathList.contains(tempFile.getAbsolutePath())==false)
					{
						visitedPathList.add(tempFile.getAbsolutePath());
						currentPath = tempFile.getParentFile().getAbsolutePath();
						Util.printToConsole("current path is set to tempFile's parent:"+currentPath);
					}
				}
				else
				{
					//visitedPathList.add(new File(currentPath).getParentFile().getAbsolutePath());
					currentPath = new File(currentPath).getParentFile().getAbsolutePath();
					visitedPathList.add(new File(currentPath).getParentFile().getAbsolutePath());
					//Util.printToConsole("current path is set to its parent:"+currentPath);
				}
			}
		}		
	}
}