var TAG = TAG || {}

function cbServerDetailsPreviewCodeClick(){
	TAG.CodePreviewController.previewCode();
}

function cbShowServerDetailsPopup(){
	TAG.CodePreviewController.showPopup();
}

function cbCloseServerDetailsPopup(){
	TAG.CodePreviewController.closePopup();
}

TAG.CodePreviewController = new function(){

	var controller = {};
	
	//TODO:  Need to get this from the user input
	var uri = "/tagcodepreview/readcode.jsp";
	var curAppForm = null;
	
	var decode_base64 = function(s) {
	
		// need to remove extra =s that are added by encoder to
		// to make the encoded string length to be a multiple of 3
		if(s[s.length-1] == '='){
			s = s.substring(0, s.length-1);
		}
		if(s[s.length-1] == '='){
			s = s.substring(0, s.length-1);
		}
	
	    var e={},i,k,v=[],r='',w=String.fromCharCode;
	    var n=[[65,91],[97,123],[48,58],[43,44],[47,48]];
	
	    for(z in n){for(i=n[z][0];i<n[z][1];i++){v.push(w(i));}}
	    for(i=0;i<64;i++){e[v[i]]=i;}
	
	    for(i=0;i<s.length;i+=72){
	    var b=0,c,x,l=0,o=s.substring(i,i+72);
	         for(x=0;x<o.length;x++){
	                c=e[o.charAt(x)];b=(b<<6)+c;l+=6;
	                while(l>=8){
	                	var chCode = (b>>>(l-=8))%256;
	                	var ch = w(chCode);
	                	if(chCode == 0){
	                		// temporary fix to handle '>'
	                		ch = '>';
	                	}
	                	r+=ch;
	                }
	         }
	    }
	    return r.trim();
	}
	
	// Public Methods	
	controller.previewCode = function(){
	
		kony.application.showLoadingScreen("","Updating the code..." , constants.LOADING_SCREEN_POSITION_ONLY_CENTER, false,true,null);

		function cbBase64Code(status, result){
			if(status != 400){
				return;
			}
	
			var encodedString = result["code"];
			if(encodedString == null){
				kony.application.dismissLoadingScreen();
				TAG.Logger.showErrorAlert("Update Code", "Failed to update the code.  Please check that the server details are correct.");
				return;
			}
			var decodedVal = decode_base64(encodedString);
			kony.print("New Code:-" + decodedVal + "-");
			try{
				var decLen = decodedVal.length;
				eval(decodedVal);
			}catch(err){
				kony.application.dismissLoadingScreen();
				TAG.Logger.showErrorAlert("Update Code", "Error while evaluating the new code:" + err.message);
				return;
			}
			kony.application.dismissLoadingScreen();
			TAG.Logger.showInfoAlert("Update Code", "Successfully updated the binary with new code.");
		}
		
		var url = "http://" + TAGCPServerDetailsPopup.textServerIP.text + ":" + TAGCPServerDetailsPopup.textServerPort.text + uri;
		kony.print("Updating the code from:" + url);
		var inputParams = {};
		//TAGCPServerDetailsPopup.dismiss();
		controller.closePopup();
		kony.net.invokeServiceAsync(url, inputParams, cbBase64Code, null);		
	}
	
	controller.showPopup = function(){
		curAppForm = kony.application.getCurrentForm();
		TAGCPServerDetailsPopup.show();
	}
	
	controller.closePopup = function(){
		TAGCPServerDetailsPopup.dismiss();
		var deviceInfo = kony.os.deviceInfo();
		if ((deviceInfo["name"] == "iphone" || deviceInfo["name"] == "iPhone" || deviceInfo["name"] == "iPhone Simulator") || 
			(deviceInfo["name"] == "ipad" || deviceInfo["name"] == "iPad" || deviceInfo["name"] == "iPad Simulator")) {
			// navigate to the form from where we came here
			curAppForm.show();
		}
	}

	controller.addControl = function(parentWidgets){
		if(parentWidgets == null){
			return;
		}
		
		if(parentWidgets.length <= 0){
			return;
		}
		//Creating a button.
		var btnPCBasic = {"id":"btnPC", "text":"Preview Code", "isVisible":true};
		var btnPC = new kony.ui.Button(btnPCBasic, {}, {});
		btnPC.onClick = controller.showPopup;
		//Adding button to the parent widgets.
		for(var i=0; i<parentWidgets.length; i++){
			parentWidgets[i].add(btnPC);
		}
	}
	
	controller.addAsAppMenuItem = function(pAppMenuId){
	
		var appMenuItem = ["tagcodepreview","Preview Code", "home.png", controller.showPopup];
		
		var lCurrenAppMenuId =  kony.application.getCurrentAppMenu();
		kony.print("************* App Menu ID: " + lCurrenAppMenuId);
		
		if(lCurrenAppMenuId == null){
			// create app menu
			kony.print("Creating new app menu");
			var pAppMenu = [appMenuItem];
			kony.application.createAppMenu (pAppMenuId, pAppMenu, "", "");
			kony.print("Setting new app menu as current app menu");
			kony.application.setCurrentAppMenu(pAppMenuId);
		}else{
			// give index as 100 so that it is added as the last item
			kony.print("Adding to the existing app menu");
			kony.application.addAppMenuItemAt(lCurrenAppMenuId, 0, appMenuItem)
		}
		
	} 
	
	return controller;
};

