package com.kony.tag.codereview.lua;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PartInitException;

public class FindUnusedi18n
{
	public static void main(Shell shell)
	{
		long startTime = System.currentTimeMillis();
		File file = new File(Util.Output_DIR+"\\Unusedi18n.csv");
		Writer output = null;
		
		try
		{
			Util.printToConsole("Getting all the i18n keys names .......... ");
			ArrayList<String> i18nKeysList = getAlli18nKeys(); // get all i18n names
			if(i18nKeysList!=null && i18nKeysList.size()>0)
			{ 
				// Check for un-used i18n if and only if there are some i18ns in the lua files.
				Util.printToConsole("Total i18n list size ..."+i18nKeysList.size());
				Util.printToConsole("Getting all the used i18n keys .......... ");
				
				Set<String> usedI18NKeys = getUsedI18NKeys(i18nKeysList);
				Util.printToConsole("Used I18N Keys  ...."+usedI18NKeys.size());
				Util.printToConsole("Getting all the un-used i18n key names .......... ");
				
				ArrayList<String> unusedI18NKeys = Util.getUnusedItems(i18nKeysList, usedI18NKeys);
				Util.printToConsole("Number of un-used i18ns are "+unusedI18NKeys.size()+"  ");
				
				//deleteUnncessaryi18ns(unusedi18ns); // Un-comment this line if you want to delete the un-used i18ns programetically.
				output = new BufferedWriter(new FileWriter(file, false));
				for(int i=0;i<unusedI18NKeys.size();i++)
				{
					output.append(unusedI18NKeys.get(i));
			    	output.flush();	
				}
			}
		}
		catch (IOException e) 
		{
			e.printStackTrace();
			Util.printToConsole("Encountered difficulties while cleaning i18n keys due to following reason: " + e.getMessage() + ". \nContinuing the script with other tasks.");
			//Util.printToConsole(Util.stackTraceToString(e)); 
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			Util.printToConsole("Encountered difficulties while cleaning i18n keys due to following reason: " + e.getMessage() + ". \nContinuing the script with other tasks.");
			//Util.printToConsole(Util.stackTraceToString(e));
		}
		finally
		{
			if(output!=null)
			{
				try 
				{
					output.close();
				}
				catch (IOException e) 
				{
					e.printStackTrace();
					Util.printToConsole("Encountered difficulties while cleaning i18n keys due to following reason: " + e.getMessage() + ". \nContinuing the script with other tasks.");
				}
			}
		}
		
		long endTime = System.currentTimeMillis();
		Util.printToConsole("Total Time Taken for Processing to find the un-used i18ns is ...... "+(endTime-startTime)+" ms");
	}

	private static ArrayList<String> getAlli18nKeys() throws CodeReviewException, IOException, PartInitException 
	{
		ArrayList<String> i18nKeysList = new ArrayList<String>();
		//System.out.println("i18n directory "+Util.i18nDir); 
		
		File[] i18nFiles = new File(Util.i18nDir).listFiles(new PropFilter());
		if(null==i18nFiles || i18nFiles.length<=0)
		{
			Util.printToConsole("No i18n files found under resource folder.... As per the standard it is recommended to use internationalization even there is a single language in scope.");
			return i18nKeysList;
		}
		else
		{
			LineNumberReader br = new LineNumberReader(new FileReader(i18nFiles[0]));
			String line = null;
			while((line=br.readLine())!=null)
			{
				int index = line.indexOf("=");
				if(index!=-1)
				{
					// This loop is added to take care of new line characters in the i18n definition (Ex: key=20% OFF\r\n by spending Rs 500 above)
					String i18nKey = line.substring(0,index).trim();
					i18nKeysList.add(i18nKey);
				}
			}
			
			if(br != null)
				br.close();
			
			return i18nKeysList;
		}
	}

	public static Set<String> getUsedI18NKeys(ArrayList<String> i18nKeysList) throws IOException
	{
		ArrayList<String> tmpI18NKeysList = i18nKeysList;
		String tempCodeDir = Util.tempCodeDir;
		
		//String starti18n = "i18n";
		Set<String> usedI18NKeys = new TreeSet<String>();
		File tempCodeDirFile = new File(tempCodeDir);
		File[] allTempFiles = tempCodeDirFile.listFiles(new SVNFileFilter());
		
		if(allTempFiles!=null)
		{
			for(File fileName : allTempFiles) 
			{
				LineNumberReader br = null;
				try
				{
					// LineNumberReader is subclass of BufferedReader with Line Number feature
					br = new LineNumberReader(new FileReader(fileName));
					String line = null;
					//String filePath = fileName.getName();
					
					// read lines from file till end of the file
					while(null != (line = br.readLine())) 
					{
						//Util.printToConsole("^^^^^^^^^^^^^^^^^^^^ tmpi18nNamesList size is "+tmpi18nNamesList.size()+" Used i18n list size "+usedi18ns.size()); 
						for (int i = 0; i < tmpI18NKeysList.size(); i++)
						{		
							String i18nKey = tmpI18NKeysList.get(i);
							if (line.contains(i18nKey))
							{						
								usedI18NKeys.add(i18nKey);
								tmpI18NKeysList.remove(i); // Remove the already used i18ns from the verification on new line.
							}
						}
					}
				}
				catch(IOException ioe)
				{
					ioe.printStackTrace();
					throw ioe;
					//Util.printToConsole("IOException"+ioe);
				}
				finally
				{
					if(br!=null)
					{
						try 
						{
							br.close();
						}
						catch (IOException e) 
						{
							e.printStackTrace();
							Util.printToConsole("Encountered difficulties while cleaning i18n keys due to following reason: " + e.getMessage() + ". \nContinuing the script with other tasks.");
						}
					}
				}
			}
		}
		
		return usedI18NKeys;
	}
}