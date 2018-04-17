var TAG = TAG || {};
TAG.profiler = new function() {
    var eventList = [];
    var eventHierearchyObject = {};
    var functionEndCount = 0;
    var callStack = [];
    var numNetworkCallsInProgress = 0;
    var eventWaiting = false;
    var profiler = {};
    var functionCounterObject = {};
    var eventCounterObject = {};
	var networkCallEnded = true;
    profiler.startEvent = function(eventKey) {
        kony.print("##### Start event");
        if (eventCounterObject[eventKey] == undefined) {
            eventCounterObject[eventKey] = 1;
        } else {
            var count = kony.os.toNumber(eventCounterObject[eventKey]) + 1;
            eventCounterObject[eventKey] = count;
        }
        eventHierearchyObject = this.createEvent(eventKey);
        callStack.push(eventHierearchyObject);
        eventWaiting = true;
    };
    profiler.createEvent = function(eventKey) {
        kony.print("##### Create event");
        var eventHierearchyObject = {};
        eventHierearchyObject["widget_event"] = eventKey;
        eventHierearchyObject["startTime"] = (new Date()).getTime();
        eventHierearchyObject["Functions"] = [];
        eventHierearchyObject["NetworkCalls"] = [];
        return eventHierearchyObject;
    };
    profiler.endEvent = function() {
        if (numNetworkCallsInProgress > 0) {
            // event did not finish
            kony.print("#####Event waiting for network call to finish");
            return;
        }
        eventHierearchyObject["timeTaken"] = (new Date()).getTime() - eventHierearchyObject["startTime"];
		//delete eventHierearchyObject["startTime"];
        eventList.push(eventHierearchyObject);
        callStack.pop();
        //TAG.Logger.showInfoAlert("EVENT HIERARCHY", JSON.stringify(eventList));
        kony.print("Event List: " + JSON.stringify(eventList));
        kony.print("End List: " + eventList.length);
        eventWaiting = false;
        kony.print("#####JSON String for function hierarchy: " + JSON.stringify(eventHierearchyObject));
        kony.print("#####END EVENT: JSON String : " + JSON.stringify(eventList));
    };
    profiler.startFunction = function(fnName, lineNo, paramObject, fileName) {
        if (functionCounterObject[fnName] == undefined) {
            functionCounterObject[fnName] = 1;
        } else {
            var count = kony.os.toNumber(functionCounterObject[fnName]) + 1;
            functionCounterObject[fnName] = count;
        }
        kony.print("#####start Function: " + fnName + " lineNo: " + lineNo+" fileName: " +fileName);
        var funcMap = {};
        funcMap["fnName"] = fnName;
		funcMap["fileName"] = fileName;
        funcMap["startTime"] = (new Date()).getTime();
        funcMap["Functions"] = [];
        // if the call stack is empty, then add the functions to eventHierearchyObject
        if (callStack.length > 0) {
            kony.print("#####Call Stack is active");
            callStack[callStack.length - 1]["Functions"].push(funcMap);
        } else {
            kony.print("#####Call stack is inactive");
            if (eventHierearchyObject["Functions"] == undefined) {
                eventHierearchyObject = this.createEvent("Dynamic Event");
            }
            eventHierearchyObject["Functions"].push(funcMap);
        }
		callStack.push(funcMap);
		kony.print("SF List: " + JSON.stringify(callStack));
    };
    profiler.endFunction = function(fName, lineNo, fileName) {
        kony.print("#####Ending function:" + fName+" lineNo: "+lineNo +" fName: "+fileName);
        kony.print("EF List: " + JSON.stringify(callStack));
		if(callStack.length == 0){
			return;
		}
        callStack[callStack.length - 1]["endTime"] = (new Date()).getTime();
		var networkTime = eventHierearchyObject["networkTime"];
		kony.print("networkTime: " + networkTime);
		if(networkTime == undefined || networkCallEnded) {
			networkTime = (new Date()).getTime();
		}
        callStack[callStack.length - 1]["timeTaken"] = networkTime - callStack[callStack.length - 1]["startTime"];
		delete callStack[callStack.length - 1]["endTime"];
		delete callStack[callStack.length - 1]["startTime"];
        callStack.pop();
        if (callStack.length <= 0) {
            // call stack was re-activated by the callback functios
            // so update the event times
            kony.print("#####Updating event end time as callback just finished");
            eventHierearchyObject["endTime"] = (new Date()).getTime();
            eventHierearchyObject["timeTaken"] = eventHierearchyObject["endTime"] - eventHierearchyObject["startTime"];
			delete eventHierearchyObject["startTime"];
			delete eventHierearchyObject["endTime"];
        }
    };
    profiler.startNetworkCall = function(svcName, lineNo, fileName) {
        kony.print("#####Start Network Call: svcName "+svcName+" line No: "+lineNo+" fileName: "+fileName)
		networkCallEnded = false;
		eventHierearchyObject["networkTime"] = (new Date()).getTime();
        numNetworkCallsInProgress++;
        kony.print("#####Event Object:" + JSON.stringify(eventHierearchyObject))
        var networkArray = eventHierearchyObject["NetworkCalls"];
        var networkMap = {};
        networkMap["svcName"] = svcName == undefined ? "Network Call" : svcName;
		networkMap["fileName"] = fileName;
        var paramMap = {};
        networkMap["startTime"] = (new Date()).getTime();
        eventHierearchyObject["NetworkCalls"].push(networkMap);
        kony.print("#####Network Call Object:" + JSON.stringify(networkMap));
    };
    profiler.endNetworkCall = function(fName, lineNo, fileName) {
        kony.print("#####End NetworkCall: lineNo: " + lineNo + " fName: " + fName+ " fileName: "+fileName);
		networkCallEnded = true;
        var networkArray = eventHierearchyObject["NetworkCalls"];
        var numNWCalls = eventHierearchyObject["NetworkCalls"].length;
		numNetworkCallsInProgress--;
		if(numNWCalls == 0) {
			return;
		}
        eventHierearchyObject["NetworkCalls"][numNWCalls - 1]["endTime"] = (new Date()).getTime();
        eventHierearchyObject["NetworkCalls"][numNWCalls - 1]["timeTaken"] = eventHierearchyObject["NetworkCalls"][numNWCalls - 1]["endTime"] - eventHierearchyObject["NetworkCalls"][numNWCalls - 1]["startTime"];
		delete eventHierearchyObject["NetworkCalls"][numNWCalls - 1]["startTime"];
		delete eventHierearchyObject["NetworkCalls"][numNWCalls - 1]["endTime"];
        kony.print("#####endNetworkCall networkArray: " + JSON.stringify(eventHierearchyObject["NetworkCalls"]));
        kony.print("#####endNetworkCall eventHierearchyObject: " + JSON.stringify(eventHierearchyObject));
        if (eventWaiting) {
            kony.print("#####Ending waiting event");
            profiler.endEvent();
        }
    };
    profiler.getEventStackTrace = function() {
        return eventList;
    };
	profiler.clearEventList = function() {
		eventList = [];
		eventCounterObject = {};
		functionCounterObject = {};
	};
    profiler.getEventCounterObject = function() {
        return eventCounterObject;
    };
    profiler.getFunctionCounterObject = function() {
        return functionCounterObject;
    };
    return profiler;
}