package com.kony.tag.instrumentation.tasks;

import java.util.Date;

import com.kony.tag.config.ReviewModeConstants;
import com.kony.tag.instrumentation.main.InstrumentationOrchestrator;

public class InstrumentationAntTask {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		System.out.println("STARTED INSTRUMENTATION TASK");
		try {
			
			InstrumentationOrchestrator instruOrchestrator =  new InstrumentationOrchestrator();
			
			ReviewModeConstants.TESTING_MODE_FLAG = true;
			
			if (args != null && args.length>0 && args[0] != null && args[0].trim().length()>0) {
				System.out.println("REVIEW FOLDER : " + args[0]);
			} else {
				System.out.println("Unable to locate the Project !! Project Folder should be passed as an input arg to this task !! ");
				return;
			}
			
			ReviewModeConstants.TEST_LOCATION_PROJECT_PATH = args[0];
			
			instruOrchestrator.performCodeInstrumentation((new Date()).getTime());
			
 		}catch (Exception e) {
			System.out.println("ERROR : " + e);
			e.printStackTrace();
		}

		System.out.println("COMPLETED INSTRUMENTATION TASK");
	}
}
