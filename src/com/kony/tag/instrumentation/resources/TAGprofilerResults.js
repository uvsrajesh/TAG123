function onFilter() {
    var filter = frmTAGEventInfo.combo.selectedKey;
    if (selectedFilter == filter) {
        return;
    }
    if (filter == "filter") {
        selectedFilter = "filter";
        showProfilerResults();
    } else if (filter == "event") {
        selectedFilter = "event";
        showEventStatistics();
    } else if (filter == "func") {
        selectedFilter = "func";
        showFunctionStatistics();
    }
}

function showProfilerResults() {
    profilerDetailStack = [];
    sortOrder = true;
    selectedFilter = "filter";
	frmTAGEventInfo.txt.text = "";
    var stackTrace = TAG.profiler.getEventStackTrace();
    kony.print("stack Trace Length: " + stackTrace.length);
    kony.print("stack Trace : " + JSON.stringify(stackTrace));
    frmTAGEventInfo.seg.widgetDataMap = {
        lblKey: "lblKey",
        lblValue: "lblValue"
    };
    var eventList = [];
    for (var key in stackTrace) {
        var eventName = stackTrace[key]["widget_event"];
        var eventTime = stackTrace[key]["timeTaken"];
        var eventStack = stackTrace[key]["Functions"];
        var eventNetworkCallStack = stackTrace[key]["NetworkCalls"];
        eventList.push({
            lblKey: eventName,
            lblValue: eventTime,
            lbStack: eventStack,
            lbNetStack: eventNetworkCallStack
        });
    }
    if (eventList.length > 0) {
        frmTAGEventInfo.hbx.setVisibility(false);
        frmTAGEventInfo.seg.setVisibility(true);
        frmTAGEventInfo.seg.setData(eventList);
        originalSegData = eventList;
        frmTAGEventInfo.hbxSubHeader.setVisibility(true);
        frmTAGEventInfo.hbxFooter.setVisibility(true);
    } else {
        frmTAGEventInfo.hbx.setVisibility(true);
        frmTAGEventInfo.seg.setVisibility(false);
        frmTAGEventInfo.hbxSubHeader.setVisibility(false);
        frmTAGEventInfo.hbxFooter.setVisibility(false);
        frmTAGEventInfo.lbl.text = "No Events Captured";
    }
    frmTAGEventInfo.show();
}

function showFunctionStatistics() {
    sortOrder = true;
    var functionTrace = TAG.profiler.getFunctionCounterObject();
    kony.print("functionTrace Length: " + functionTrace.length);
    kony.print("functionTrace : " + JSON.stringify(functionTrace));
    frmTAGEventInfo.seg.widgetDataMap = {
        lblKey: "lblKey",
        lblValue: "lblValue"
    };
    var functionList = [];
    for (var key in functionTrace) {
        functionList.push({
            lblKey: key,
            lblValue: functionTrace[key]
        });
    }
    if (functionList.length > 0) {
        frmTAGEventInfo.hbx.setVisibility(false);
        frmTAGEventInfo.seg.setVisibility(true);
        frmTAGEventInfo.seg.removeAll();
        frmTAGEventInfo.seg.setData(functionList);
        originalSegData = functionList;
        frmTAGEventInfo.hbxSubHeader.setVisibility(true);
        frmTAGEventInfo.hbxFooter.setVisibility(true);
    } else {
        frmTAGEventInfo.hbx.setVisibility(true);
        frmTAGEventInfo.seg.setVisibility(false);
        frmTAGEventInfo.hbxSubHeader.setVisibility(false);
        frmTAGEventInfo.hbxFooter.setVisibility(false);
        frmTAGEventInfo.lbl.text = "No Functions Captured";
    }
}

function showEventStatistics() {
    sortOrder = true;
    var eventTrace = TAG.profiler.getEventCounterObject();
    kony.print("eventTrace Length: " + eventTrace.length);
    kony.print("eventTrace : " + JSON.stringify(eventTrace));
    frmTAGEventInfo.seg.widgetDataMap = {
        lblKey: "lblKey",
        lblValue: "lblValue"
    };
    var eventList = [];
    for (var key in eventTrace) {
        eventList.push({
            lblKey: key,
            lblValue: eventTrace[key]
        });
    }
    if (eventList.length > 0) {
        frmTAGEventInfo.hbx.setVisibility(false);
        frmTAGEventInfo.seg.setVisibility(true);
        frmTAGEventInfo.seg.removeAll();
        frmTAGEventInfo.seg.setData(eventList);
        originalSegData = eventList;
        frmTAGEventInfo.hbxSubHeader.setVisibility(true);
        frmTAGEventInfo.hbxFooter.setVisibility(true);
    } else {
        frmTAGEventInfo.hbx.setVisibility(true);
        frmTAGEventInfo.seg.setVisibility(false);
        frmTAGEventInfo.hbxSubHeader.setVisibility(false);
        frmTAGEventInfo.hbxFooter.setVisibility(false);
        frmTAGEventInfo.lbl.text = "No Events Captured";
    }
}

function showDetailedProfilerResults(eventObject) {
    functionList = [];
    networkList = [];
    functionTime = 0;
    networkTime = 0;
    var functionStack = frmTAGEventInfo.seg.selectedItems[0].lbStack;
    var networkStack = frmTAGEventInfo.seg.selectedItems[0].lbNetStack;
	if(functionStack == undefined || networkStack == undefined) {
		return;
	}
    kony.print("stack Trace : " + frmTAGEventInfo.seg.selectIndices);
    kony.print("functionStack: " + JSON.stringify(functionStack));
    kony.print("networkStack: " + JSON.stringify(networkStack));
    frmTAGFunctionInfo.seg.widgetDataMap = {
        lblFKey: "lblFKey",
        lblFValue: "lblFValue"
    };
    var eventData = [];
    var record = [];
    var eventTime = frmTAGEventInfo.seg.selectedItems[0].lblValue;
    
    record.push({
        lblFKey: "Cumulative Time Taken",
        lblFValue: eventTime
    });
    
    
	var totalTime = 0;
	var functionCalls = [];
    if (functionStack.length > 0) {
        
        for (var key in functionStack) {
            var index = kony.os.toNumber(key) + 1;
            var functionObject = functionStack[key];
            var funcTime = functionObject["timeTaken"];
			kony.print("funcTime of the object: "+funcTime);
			totalTime = totalTime + kony.os.toNumber(funcTime);
			kony.print("totalTime of the object: "+totalTime);
            functionCalls.push({
                lblFKey: functionObject["fnName"],
                lblFValue: funcTime,
                lblFunObject: functionObject
            });
        }
    }
	var networkCalls = [];
    if (networkStack.length > 0) {
        
        for (var key in networkStack) {
            var index = kony.os.toNumber(key) + 1;
            var networkObject = networkStack[key];
            var serviceTime = networkObject["timeTaken"];
			totalTime = totalTime + kony.os.toNumber(serviceTime);
            networkCalls.push({
                lblFKey: networkObject["svcName"],
                lblFValue: serviceTime,
                lblFunObject: networkObject
            });
        }
    }
	kony.print("totalTime: "+totalTime);
	record.push({
		lblFKey: "Self Time Taken",
		lblFValue: eventTime - totalTime
	});
	record.push({
        lblFKey: "Parameters",
        lblFValue: ""
    });
	eventData.push(["	Details", record]);
	if(functionCalls.length > 0) {
		eventData.push(["	Function Calls", functionCalls]);
	}
	if(networkCalls.length > 0) {
		eventData.push(["	Network Calls", networkCalls]);
	}
	
    if (eventData.length > 0) {
		
        frmTAGFunctionInfo.hbx.setVisibility(false);
        frmTAGFunctionInfo.seg.setVisibility(true);
        frmTAGFunctionInfo.seg.setData(eventData);
		profilerDetailStack.push(eventData);
    } else {
        frmTAGFunctionInfo.hbx.setVisibility(true);
        frmTAGFunctionInfo.seg.setVisibility(false);
        frmTAGFunctionInfo.lbl.text = "No Info Captured";
    }
    frmTAGFunctionInfo.show();
}

function showDetailedFunctionInfo() {
    var selectedRow = frmTAGFunctionInfo.seg.selectedItems;
	kony.print("selectedRow: "+selectedRow);
	kony.print("selected Indices: "+frmTAGFunctionInfo.seg.selectedIndices);
    if (selectedRow != undefined) {
        var functionObject = selectedRow[0]["lblFunObject"];
		kony.print("functionObject: "+functionObject);
        if (functionObject != undefined) {
            var functionData = [];
            var record = [];
            
            record.push({
                lblFKey: "Cumulative Time Taken",
                lblFValue: functionObject["timeTaken"]
            });
            
            
            var functionList = functionObject["Functions"];
			var totalTime = 0;
			var functionrecord = [];
            if (functionList != undefined && functionList.length > 0) {
                
                for (var key in functionList) {
					var fnObj =  functionList[key];
					var funcTime = fnObj["timeTaken"];
					kony.print("funcTime of the object: "+funcTime);
					totalTime = totalTime + kony.os.toNumber(funcTime);
					kony.print("totalTime of the object: "+totalTime);
                    functionrecord.push({
                        lblFKey: functionList[key]["fnName"],
                        lblFValue: funcTime,
                        lblFunObject: fnObj
                    })
                }
            }
			
			record.push({
				lblFKey: "Self Time Taken",
				lblFValue: functionObject["timeTaken"] - totalTime
			});
			record.push({
                lblFKey: "Parameters",
                lblFValue: "",
                lblFunParameters: functionObject["params"]
            });
			functionData.push(["		Details", record]);
			if(functionrecord.length > 0) {
				functionData.push(["		Function Calls", functionrecord]);
			}
			
			frmTAGFunctionInfo.seg.removeAll();
            frmTAGFunctionInfo.seg.setData(functionData);
            profilerDetailStack.push(functionData);
			kony.print("profilerDetailStack length "+profilerDetailStack.length);
        }
        var paramObject = selectedRow[0]["lblFunParameters"];
		kony.print("paramObject: "+paramObject);
        if (paramObject != undefined) {
            if (paramObject != undefined) {
                var paramData = [];
                var record = [];
                for (var key in paramObject) {
                    record.push({
                        lblFKey: key,
                        lblFValue: paramObject[key]
                    });
                }
                showProfilerAlert(JSON.stringify(paramObject));
                return;
            }
        }
        var networkObject = selectedRow[0]["lblNetObject"];
		kony.print("networkObject: "+networkObject);
        if (networkObject != undefined) {
            var networkData = [];
            var record = [];
            record.push({
                lblFKey: "Self Time Taken",
                lblFValue: networkObject["timeTaken"]
            });
            record.push({
                lblFKey: "Cumulative Time Taken",
                lblFValue: networkObject["timeTaken"]
            });
            networkData.push("		Details", record);
            var networkList = networkObject["Functions"];
            if (networkList.length > 0) {
                record = [];
                for (var key in networkList) {
                    record.push({
                        lblFKey: networkList[key]["fnName"],
                        lblFValue: networkList[key]["timeTaken"],
                        lblFunObject: networkList[key]
                    })
                }
                networkData.push(["		Function Calls", record]);
            }
            frmTAGFunctionInfo.seg.removeAll();
            frmTAGFunctionInfo.seg.setData(networkData);
            profilerDetailStack.push(networkData);
        }
    }
}

function profilerNavigation() {
	kony.print("profiler navigation: "+profilerDetailStack.length);
	profilerDetailStack.pop();
    if (profilerDetailStack.length < 1) {
        frmTAGEventInfo.show();
        return;
    }
	
    var previousPage = profilerDetailStack[profilerDetailStack.length - 1];
    frmTAGFunctionInfo.seg.removeAll();
    frmTAGFunctionInfo.seg.setData(previousPage);
}

function clearProfilerResults() {
    frmTAGEventInfo.seg.removeAll();
	TAG.profiler.clearEventList();
	originalSegData = [];
	frmTAGEventInfo.txt.text = "";
	frmTAGEventInfo.hbxSubHeader.setVisibility(false);
    frmTAGEventInfo.hbxFooter.setVisibility(false);
    showProfilerAlert("Cleared Results");
}

function filterProfilerData() {
    var filterText = frmTAGEventInfo.txt.text;
    if (filterText != "") {
        var eventData = frmTAGEventInfo.seg.data;
        var records = [];
        var pattern = new RegExp(".*" + filterText + ".*", "i");
        for (var key in eventData) {
            var record = eventData[key];
            if (pattern.test(record.lblKey)) {
                records.push(record);
            }
        }
        frmTAGEventInfo.seg.removeAll();
        frmTAGEventInfo.seg.setData(records);
    } else {
        frmTAGEventInfo.seg.setData(originalSegData);
    }
}

function sortAmountAscending(amtData) {
    try {
        amtData.sort(

        function(a, b) {
            if (a.lblValue - b.lblValue > 0) {
                return 1;
            } else if (a.lblValue - b.lblValue < 0) {
                return -1;
            } else {
                return 0;
            }
        });
        return amtData;
    } catch (err) {
        kony.print("the value of err.message is:" + err);
    }
}

function sortAmountDecending(amtData) {
    try {
        amtData.sort(

        function(a, b) {
            if (a.lblValue - b.lblValue > 0) {
                return -1;
            } else if (a.lblValue - b.lblValue < 0) {
                return 1;
            } else {
                return 0;
            }
        });
        return amtData;
    } catch (err) {
        kony.print("the value of err.message is:" + err);
    }
}

function sortProfilerData() {
    var eventData = frmTAGEventInfo.seg.data;
    if (sortOrder) {
        eventData = sortAmountDecending(eventData);
        sortOrder = false;
    } else {
        eventData = sortAmountAscending(eventData);
        sortOrder = true;
    }
    frmTAGEventInfo.seg.removeAll();
    frmTAGEventInfo.seg.setData(eventData);
}

function showProfilerAlert(message) {
    var basicConf = {};
    basicConf["message"] = message;
    basicConf["alertType"] = constants.ALERT_TYPE_INFO;
    basicConf["alertTitle"] = "Info";
    basicConf["yesLabel"] = "OK";
    basicConf["noLabel"] = "";
    basicConf["alertHandler"] = null;
    kony.ui.Alert(basicConf, {});
}

function loopThroughStack(functionArray, timeTaken) {
    kony.print("loopThroughStack: functionArray: " + JSON.stringify(functionArray));
    kony.print("functionArray type : " + (functionArray instanceof Array));
    if (!(functionArray instanceof Array) || functionArray.length == 0) {
        if (timeTaken == undefined) {
            functionTime = 0;
        } else {
            functionTime = timeTaken;
        }
    } else {
        functionList.push(functionArray[0]["fnName"]);
        kony.print("st: " + functionArray[0]["timeTaken"] + " name: " + functionArray[0]["fnName"]);
        kony.print("timeTaken1: " + timeTaken + " functionTime1: " + functionTime);
        functionTime = timeTaken + loopThroughStack(functionArray[0]["Functions"], functionArray[0]["timeTaken"]);
        kony.print("timeTaken: " + timeTaken + " functionTime: " + functionTime);
    }
    kony.print("return function time: " + functionTime);
    return functionTime;
}

function loopThroughNetworkStack(functionArray) {
    kony.print("loopThroughNetowrkStack: functionArray: " + JSON.stringify(functionArray));
    networkTime = 0;
    for (var key in functionArray) {
        var networkObject = functionArray[key];
        networkList.push(networkObject["svcName"]);
        networkTime = networkTime + networkObject["timeTaken"]
    }
    kony.print("return network time: " + networkTime);
    return networkTime;
}

function emailResults() {
	var toRecipient = ["padma.ramaraju@kony.com"];
	var ccRecipient = ["koteswararao.pentakota@kony.com", "saikrishna.vidhyula@kony.com"];
	var sub = "Profiler Results";
	var eventList = TAG.profiler.getEventStackTrace();
	var functionHitCount = TAG.profiler.getFunctionCounterObject();
	var eventHitCount = TAG.profiler.getEventCounterObject();
	var body = "\nEventList: \n"+JSON.stringify(eventList)+ 
			   "\n\nFunction Hit Count: \n"+JSON.stringify(functionHitCount)+
			   "\n\nEvent Hit Count: \n"+JSON.stringify(eventHitCount);
	kony.phone.openEmail(toRecipient, ccRecipient, [], sub, body, false, []);
}