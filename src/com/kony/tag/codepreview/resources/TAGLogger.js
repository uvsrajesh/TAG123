var TAG = TAG || {}

TAG.Logger = new function(){
    
	var logcontroller = {}
	
	logcontroller.TRACE = {value: 0, name: "Trace", code: "TRACE"}, 
	logcontroller.DEBUG = {value: 1, name: "Debug", code: "DEBUG"}, 
	logcontroller.INFO = {value: 2, name: "Info", code: "INFO"}, 
	logcontroller.WARN = {value: 3, name: "Warn", code: "WARN"}, 
	logcontroller.ERROR = {value: 4, name: "Error", code: "ERROR"}
	
	// set it to lowest level
	var curLogLevel = logcontroller.TRACE;
	
	// an array to hold all the log messages
	// this array can be used later to display all log messages in a console
	// to dump all log at once
	var logMessages = [];
	
	// represents how many latest log messages to keep in memory
	var numMessagesToHold = 500;

	 
	// checks for the log level set and prints the log message accordingly
	var log = function(logLevel, message) {
		
		// save the message in the array of log messages
		var caller = arguments.callee.caller.caller.name;
		var timestamp = kony.os.time();
		var code = logLevel.code;
		
		var formattedMessage = timestamp + " " + caller + " " + code + " " + message;
		if(logMessages.length >= numMessagesToHold){
			logMessages.shift();
		}
		logMessages.push({"level":code, "timestamp":timestamp, "message":message});
		
		// print the message if the log level is less severe than
		// the passed in log level
		if(logcontroller.getLogLevel().value <= logLevel.value) {
			kony.print(formattedMessage);
		}
	};
	
	/**
	 * Returns the current log level set in the application.
	 * 
	 * The default value is set to DEBUG when the logger is created.
	 * Use this method to change it any time during the application
	 * 
	 * @name getLogLevel
	 * @function
	 * @memberOf TAG.Logger
	 * @return current log level object
	 */
	logcontroller.getLogLevel = function() {
		return curLogLevel;
	};
	

	/**
	 * Prints a logging statement over the trace channel.
	 * 
	 * Use it whenever the output is that fine grained that it's even
	 * to much detail for general debugging purposes
	 * 
	 * @name trace
	 * @function
	 * @memberOf TAG.Logger
	 * @param {String} msg Message with placeholders to be printed.
	 * @param {String[]} params Parameters which will replace the placeholders in the message.
	 */
	logcontroller.trace = function(msg) {
		  log(logcontroller.TRACE, msg);
	};
	
	/**
	 * Prints a logging statement over the debug channel.
	 * 
	 * Use it whenever you want to have some information printed
	 * which are useful for debugging purposes.
	 * 
	 * @name debug
	 * @function
	 * @memberOf TAG.Logger
	 * @param {String} msg Message with placeholders to be printed.
	 * @param {String[]} params Parameters which will replace the placeholders in the message.
	 */
	logcontroller.debug = function(msg) {
		  log(logcontroller.DEBUG, msg);
	};
	
	/**
	 * Prints a logging statement over the info channel.
	 * 
	 * Use it whenever something of interest for developers happens.
	 * 
	 * @name info
	 * @function
	 * @memberOf TAG.Logger
	 * @param {String} msg Message with placeholders to be printed.
	 * @param {String[]} params Parameters which will replace the placeholders in the message.
	 */
	logcontroller.info = function(msg) {
		  log(logcontroller.INFO, msg);
	};
	
	/**
	 * Prints a logging statement over the warn channel.
	 * 
	 * Something odd happened but it's not dangerous for the app.
	 * 
	 * @name warn
	 * @function
	 * @memberOf TAG.Logger
	 * @param {String} msg Message with placeholders to be printed.
	 * @param {String[]} params Parameters which will replace the placeholders in the message.
	 */
	logcontroller.warn = function(msg) {
		  log(logcontroller.WARN, msg);
	};
	
	/**
	 * Prints a logging statement over the warn channel.
	 * 
	 * An error occured which may inflict the apps health.
	 * 
	 * @name error
	 * @function
	 * @memberOf TAG.Logger
	 * @param {String} msg Message with placeholders to be printed.
	 * @param {String[]} params Parameters which will replace the placeholders in the message.
	 */
	logcontroller.error = function(msg) {
		  log(logcontroller.ERROR, msg);
	};
	
	/**
	 * Returns the log messages from begin index to end index
	 * 
	 * @name getMessages
	 * @function
	 * @memberOf TAG.Logger
	 * @param {int} beginInd beginning index from which to retrieve the messages.
	 * @param {int} endInd ending index till which the messages need to be retrieved.
	 */
	logcontroller.getMessages = function(beginInd, endInd){
		return logMessages.slice(beginInd, endInd);
	}
	
	/**
	 * Returns the number of messages available in the array
	 *  
	 * @name getNumberOfMessages
	 * @function
	 * @memberOf TAG.Logger
	 */
	logcontroller.getNumberOfMessages = function(){
		return logMessages.length;
	}
	
	/**
	 * Sets the limit on number of log messages held in memory
	 *  
	 * @name setNumberOfMessagesToHold
	 * @function
	 * @memberOf TAG.Logger
	 */
	logcontroller.setNumberOfMessagesToHold = function(numMessagesLimit){
		return numMessagesToHold = numMessagesLimit;
	}
	
	/**
	 * Opens email client with the passed in information and log messages as message body
	 *  
	 * @name emailLogInfo
	 * @function
	 * @memberOf TAG.Logger
	 * @param toList - array of strings representing email addresses for "to" receipients (pass an empty array of no to recipients)
	 * @param ccList - array of strings representing email addresses for "cc" recipients (pass an empty array of no cc recipients)
	 * @param bccList - array of strings representing email addresses for "bcc" recipients (pass an empty array of no bcc recipients)
	 * @param subject - string representing email subject
	 * @param  attachments - array of attachment objects (pass an empty array if no attachments)
	 */
	logcontroller.emailLogInfo = function(toList, ccList, bccList, subject, attachments){
		var msgBody = "";
		var numMsgs = logcontroller.getNumberOfMessages();
		if(numMsgs > 0){
			msgBody = logcontroller.getMessages(0,logcontroller.getNumberOfMessages).join("\\n");
		}
		kony.print("Log Message:" + msgBody);
		kony.phone.openEmail(toList,ccList,bccList,subject,msgBody,false,attachments);
	}
	/*************  Alert Related Functions **********************************/
	
	var showAlert = function(title, msg, type, alertHandlerFn){
		
		if(alertHandlerFn == null){
			alertHandlerFn = function(){};
		}
		
		var yesLblVal = "OK";
		var noLblVal = "No";
		if(type == constants.ALERT_TYPE_CONFIRMATION){
			yesLblVal = "Yes";
		}
			
		//Defining basicConf parameter for alert
		var basicConf = {message: msg,
						alertType: type,
						alertTitle: title,
						yesLabel:yesLblVal,
						noLabel: noLblVal, 
						alertHandler: alertHandlerFn};
		
		//Defining pspConf parameter for alert
		var pspConf = {};
		
		//Alert definition
		kony.ui.Alert(basicConf,pspConf);
	}

	/**
	 * Show an info type of alert message
	 *  
	 * @name showInfoAlert
	 * @function
	 * @memberOf TAG.Logger
	 * @param title - message title
	 * @param msg - message to be displayed
	 * @param alertHandler - handler functino to be called after user clicks OK button.
	 * 						Pass null if no handler function needs to be called.
	 */
	logcontroller.showInfoAlert = function(title, msg, alertHandler){
		showAlert(title, msg, constants.ALERT_TYPE_INFO, alertHandler);
	}

	/**
	 * Show an error type of alert message
	 *  
	 * @name showErrorAlert
	 * @function
	 * @memberOf TAG.Logger
	 * @param title - message title
	 * @param msg - message to be displayed
	 * @param alertHandler - handler functino to be called after user clicks OK button.
	 * 						Pass null if no handler function needs to be called.
	 */
	logcontroller.showErrorAlert = function(title, msg, alertHandler){
		showAlert(title, msg, constants.ALERT_TYPE_ERROR, alertHandler);
	}

	return logcontroller;
}