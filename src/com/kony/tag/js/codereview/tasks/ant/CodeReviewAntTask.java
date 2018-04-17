package com.kony.tag.js.codereview.tasks.ant;

import java.util.Date;
import com.kony.tag.js.codereview.config.ReviewModeConstants;
import com.kony.tag.js.codereview.main.ReviewOrchestrator;

public class CodeReviewAntTask {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		System.out.println("STARTED CODE REVIEW TASK");
		try {
			
			ReviewOrchestrator reviewOrchestrator =  new ReviewOrchestrator();
			
			ReviewModeConstants.TESTING_MODE_FLAG = true;
			
			if (args != null && args.length>0 && args[0] != null && args[0].trim().length()>0) {
				System.out.println("REVIEW FOLDER : " + args[0]);
			} else {
				System.out.println("Unable to locate the Project !! Project Folder should be passed as an input arg to this task !! ");
				return;
			}
			
			ReviewModeConstants.TEST_LOCATION_PROJECT_PATH = args[0];
			
			reviewOrchestrator.performCodeReview((new Date()).getTime());
			
			//FormUtil formUtil = new FormUtil();
			//KonySkinsXmlHandler konySkinsXmlHandler = new KonySkinsXmlHandler();
			//konySkinsXmlHandler.init();
			//formUtil.readXML("D:\\work\\temp\\testing\\CitiGlobalUI\\CitiGlobalUI\\forms\\mobile\\frmGMACCCreditCardAccountDetails.kl");
			//formUtil.readXML("D:\\work\\temp\\frmBtn.xml", new KonyFormContentHandler());
			//formUtil.readXML("D:\\projects\\tag\\sampleapps5.0\\KitchenSinkApp\\themes\\default\\skin.xml", konySkinsXmlHandler);
			
			//for (String skinName : konySkinsXmlHandler.getAllSkinNames()) {
			//	System.out.println("SKIN: " + skinName);
			//}
			
 		}catch (Exception e) {
			System.out.println("ERROR : " + e);
			e.printStackTrace();
		}

		System.out.println("COMPLETED CODE REVIEW TASK");
	}
}
