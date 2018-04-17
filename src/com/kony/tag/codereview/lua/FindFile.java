package com.kony.tag.codereview.lua;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Surya Teja
 */

class FilterImageName implements FileFilter  
{  
	public boolean accept(File file)  
	{	  
		return !(file.getName().endsWith("@2x.png"));  
    }  
} 

public class FindFile 
{	
	public static final String DST_DIR = Util.Output_DIR; // location where you have to get the images which have no @2x images
	public static final String SRC_DIR = Util.projectPath+"\\resources\\mobilerichclient\\iphone\\"; //Source Path - iphone folder of project
	
	
	public static void main()  
	{
		try
		{
		     File app=new File(SRC_DIR);
		     int cnt=0; 
		     Util.printToConsole("Execution started  ... !!");
		     
		     File[] filessrc = app.listFiles(new FilterImageName());
			 for(int i =0;i<filessrc.length;i++)
			 {
				File file = filessrc[i];
				if (file.isFile())
				{
					File filesrc = file;
					String name=file.getName();
					File filedst = null ;
					File filesrcdir = new File(SRC_DIR+name.replaceAll(".png", "@2X.png"));
					if(!(filesrcdir.exists()))
					{
						filedst = new File(DST_DIR+name);
						copy(filesrc,filedst);
						cnt++;
					}
				}
			 }		
		
			 Util.printToConsole("Execution completed ... !!");
			 Util.printToConsole("Total "+cnt+" images are located ..");
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	// Copies src file to dst file.
	// If the dst file does not exist, it is created
	public static void copy(File src, File dst) throws IOException 
	{
	    InputStream in = new FileInputStream(src);
	    OutputStream out = new FileOutputStream(dst);

	    // Transfer bytes from in to out
	    byte[] buf = new byte[1024];
	    int len;
	    while ((len = in.read(buf)) > 0) 
	    {
	        out.write(buf, 0, len);
	    }
	    
	    in.close();
	    out.close();
	}  
}