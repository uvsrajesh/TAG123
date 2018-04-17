package com.kony.tag.codereview.lua;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PartInitException;

/**
 * This class deletes the unused images in the project and places them in a back up folder 
 * with in the project some images which are not referred in any of the project should be taken care manually
 *  
 */

/**
 * @author Surya Teja
 */

class FilterName implements FileFilter  
{  
	public boolean accept(File file)  
	{  
		return file.getName().endsWith(".xml");  
	}  
} 

public class UnusedResourceFinder 
{	
	public static final String clientapplocation = Util.projectPath+"\\"; // client code workspace location
	public static final String reslocation = clientapplocation+"resources"; //resources Path
	public static final String DST_DIR = clientapplocation+"resbackup";
	public static ArrayList<String> extenstions = new ArrayList<String>();
	
	static String newLocation = "";
	static String strDir = "";
	
	
	static
	{
		try
		{
			BufferedReader brDir = new BufferedReader(new FileReader("finder.txt"));
			String extension ="";
			while ((extension = brDir.readLine()) != null) 
			{
				extenstions.add(extension);
			}
		}
		catch(Exception e)
		{			
		}
	}
	
	public static void findUnusedResourceFinder(Shell shell) 
	{
		try
		{
			// Creating a backup directory.
			if(new File(DST_DIR).mkdir())
			{
				Util.printToConsole("Backup Folder "+ DST_DIR +" Created"); // Remove this and attach a logger.
			}
			else
			{
				Util.printToConsole("Unable to Create Backup Folder "+DST_DIR); // Remove this and attach a logger.
			}
		
/*			BufferedReader resourceFile = new BufferedReader(new FileReader("resource.txt"));
			File[] files = null;
			while (null!=(strDir = resourceFile.readLine())) {
				strDir = strDir.trim();
				// Directory is available or not
				File resDir = new File(reslocation + "\\common" + strDir);
				if(resDir.isDirectory()){
					files = resDir.listFiles();
				}
				resDir = new File(reslocation + "\\mobilerichclient" + strDir);
				if(resDir.isDirectory()){
					files = resDir.listFiles();
				}
				resDir = new File(reslocation + "\\mobilethinclient" + strDir);
				if(resDir.isDirectory()){
					files = resDir.listFiles();
				}
				resDir = new File(reslocation + "\\tabletrichclient" + strDir);
				if(resDir.isDirectory()){
					files = resDir.listFiles();
				}
				resDir = new File(reslocation + "\\tabletthinclient" + strDir);
				if(resDir.isDirectory()){
					files = resDir.listFiles();
				}
				if(null != files)
				{
					for (int i = 0; i < files.length; i++) {
						File file = files[i];
						newLocation = "";
						if(file!=null && !(file.isHidden()) ){
							directoryFilesCheck(file);
						}
					}
				}
			}
	*/
		}
		catch(Exception e)
		{			
		}
	}

	public static void  checkresource(File f) throws Exception
	{
		File fscr;
		boolean flag ;
		String temp=null,extension = null;
		int cnt=0;
		BufferedReader brDir = new BufferedReader(new FileReader("finder.txt"));
		while ((extension = brDir.readLine()) != null) 
		{
			extension = extension.trim();
			if (XMLResourceLogic(extension , f ) == true)
			{
				cnt++;
                if(cnt==4)
                { 				
                	String name = f.getParent();
                	name = name.replace(reslocation+"\\","");
                	if(!name.equals(temp))
                	{
				    	flag = new File(DST_DIR+"/"+name).mkdirs();
				    	Util.printToConsole(" flag : "+flag);
				    	temp= name;
                	}
				    fscr = new File(DST_DIR+"/"+name+"/"+f.getName());//DST_DIR+"/"+
				    copy(f,fscr);// backup 
					Util.printToConsole("@@@@@@@@@@@@@@@@@@@@@"+f.getAbsolutePath());
					f.delete();
                }
			}
		}
	}
	
	public static void directoryFilesCheck(File file) 
	{
		try
		{		
			if (file.isFile()&& (file.getName().endsWith(".png")))
			{		
				checkresource(file);
			}
			else if (file.isDirectory() && !(file.isHidden()))
			{
				File newDir = new File(file.getAbsolutePath());
				File[] newfiles = newDir.listFiles(new SVNFileFilter());
				if(newfiles != null )
				{
					for (int j = 0; j < newfiles.length; j++) 
					{
						File newfile = newfiles[j];
						if (newfile.isFile() && !(newfile.isHidden()))
						{									
							checkresource(newfile);						
						}
						else
						{ 
							if (newfile.isDirectory() && !(newfile.isHidden())) 
							{
								directoryFilesCheck(newfile);
								Util.printToConsole(newfile.getAbsolutePath());
							}
						}
					}								
				}						
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			//Util.printToConsole("$$$$$$$$$$$$$$$$$ "+e);
		}
	}

	public static boolean XMLResourceLogic( String extension , File f) throws Exception
	{
		String thisLine = null;
		File[] filesss = null;
		File app = null;
		boolean notfound = false;
		
		if (extension.equals("xml"))
		{
			app = new File(clientapplocation);
			if (app.isDirectory() && !(app.isHidden())) 
			{
				filesss = app.listFiles();
			}
		}
		else if (extension.equals("lua"))
		{
			app = new File(clientapplocation+"\\modules\\lua\\");
			if (app.isDirectory() && !(app.isHidden()) ) 
			{
				filesss = app.listFiles();
			}
		}
		else if (extension.equals("kl"))
		{
			app = new File(clientapplocation+"\\forms\\tablet\\");
			if (app.isDirectory() && !(app.isHidden()) ) 
			{
				filesss = app.listFiles();
			}
		}	
		else if (extension.equals("kl"))
		{
			app = new File(clientapplocation+"\\forms\\mobile\\");
			if (app.isDirectory() && !(app.isHidden()) ) 
			{
				filesss = app.listFiles();
			}
		}	
		/*else if (extension.equals("java"))
		{
			Util.printToConsole("Inside java section");
			app = new File(javaapplocation+javarelativepath);
			if (app.isDirectory() && !(app.isHidden()) ) 
			{
				filesss = app.listFiles();
			}
		}*/
		
		if (filesss != null)
		{
			for(int i =0;i<filesss.length;i++)
			{
				File file = filesss[i];
				if (file.isFile() && file.getName().contains(extension)  ==true)
				{
					BufferedReader br = new BufferedReader(new FileReader(file));
					String name=f.getName();
					if(name.contains("@2x.png"))
						name=name.replaceAll("@2x.png", ".png");
					
					while((thisLine = br.readLine()) !=null)
					{
						if ((!(thisLine.contains(name))))
						{
							notfound = true;				    		
						}				    										
						else
						{
							notfound = false;
							return notfound;
						}
					}					
				}			
			}
		}
			
		return notfound;
	}
	
	public static void copy(File src, File dst) throws IOException, PartInitException 
	{
	    InputStream in = new FileInputStream(src);
	    OutputStream out = new FileOutputStream(dst);
	    //Util.printToConsole("out directory :"+out);

	    // Transfer bytes from in to out
	    byte[] buf = new byte[1024];
	    int len;
	    while ((len = in.read(buf)) > 0) 
	    {
	        out.write(buf, 0, len);
	        Util.printToConsole("out.write(buf, 0, len);      : "+buf+ len);
	    }
	    
	    in.close();
	    out.close();
	}
}