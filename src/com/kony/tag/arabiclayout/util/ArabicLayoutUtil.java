package com.kony.tag.arabiclayout.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

import com.kony.tag.arabiclayout.impl.ArLayoutManager;
import com.kony.tag.config.CodeReviewStatus;
import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.ProjectConstants;


public class ArabicLayoutUtil implements ProjectConstants {
	
	private static ProjectConfig projectConfig = null;
	
	private static ArabicLayoutUtil reportsUtil= null;
	
	protected ArabicLayoutUtil() {
		// Do not use
	}
	
	public static ArabicLayoutUtil getInstance() {
		return new ArabicLayoutUtil();
	}
	
	public static ArabicLayoutUtil initNewInstance(ProjectConfig reviewConfig) {
		reportsUtil = new ArabicLayoutUtil();
		projectConfig = reviewConfig;

		return reportsUtil;
	}
	
	
	public void createReportFile(List<String> comments, String reportFileName)  {
		File file = new File(reportFileName);
	    Writer output = null;
	    
	    if (comments == null || comments.size()==0) {
	    	return;
	    }
		
		try {
			if(file.exists()){
				file.delete();
				file.createNewFile();
			}
			output = new BufferedWriter(new FileWriter(file, true));

			for (String comment : comments) {
				//System.out.println(comment);
				//output.append(comment);
				output.write(comment);
				output.write(NEW_LINE);
			}
		} catch (IOException excp) {
			CodeReviewStatus.getInstance().addErrorMessage(excp, "Error Preparing Report : " + excp.toString());
		}  catch (Exception e) {
			CodeReviewStatus.getInstance().addErrorMessage(e,e.toString());
		}
		
		finally {
			try {
				if (output != null) {
					output.flush();
					output.close();
				}
			} catch (IOException excp) {
				CodeReviewStatus.getInstance().addErrorMessage(excp,"Error Preparing Report : " + excp.toString());
			}  catch (Exception e) {
				CodeReviewStatus.getInstance().addErrorMessage(e,e.toString());
			}
		}
		
 	}
	
}
