/**
 *  Alexa Helper-Child
 *
 *  Copyright 2015 Michael Struck
 *  Version 1.2.1 12/23/15
 * 
 *  Version 1.0.0 - Initial release of child app
 *  Version 1.1.0 - Added framework to show version number of child app and copyright
 *  Version 1.1.1 - Code optimization
 *  Version 1.2.0 - Added the ability to add a HTTP request on specific actions
 *  Version 1.2.1 - Added child app version information into main app
 * 
 *  Uses code from Lighting Director by Tim Slagle & Michael Struck
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */

definition(
    name: "Alexa Helper-Scenario",
    namespace: "MichaelStruck",
    author: "Michael Struck",
    description: "Child app (do not publish) that allows for routines or modes to be tied to various switch's state controlled by Alexa (Amazon Echo).",
    category: "Convenience",
	parent: "MichaelStruck:Alexa Helper",
    iconUrl: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/AlexaHelper/Alexa.png",
    iconX2Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/AlexaHelper/Alexa@2x.png",
    iconX3Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/AlexaHelper/Alexa@2x.png")
    
preferences {
    page name:"pageSetup"
}

// Show "pageSetup" page
def pageSetup() {
	dynamicPage(name: "pageSetup", install: true, uninstall: true) {
    section("Name your scenario") {
    		label title:"Scenario Name", required:true
    	}
        section("Choose an Alexa switch to use...") {
			input "AlexaSwitch", "capability.switch", title: "Alexa Switch", multiple: false, required: true
    		input "momentary", "bool", title: "Is this a momentary switch?", defaultValue: false, submitOnChange:true
        }
        def phrases = location.helloHome?.getPhrases()*.label
		if (phrases) {
        	phrases.sort()
		}	
        section ("When switch is on..."){
        	if (phrases) {
            	input "onPhrase", "enum", title: "Perform this routine", options: phrases, required: false
            }
        	input "onMode", "mode", title: "Change to this mode", required: false
            input "onSwitches", "capability.switch", title: "Turn on these switches...", multiple: true, required: false
            input "onHTTP", "text", title:"Run this HTTP request...", required: false
        	input "delayOn", "number", title: "Delay in minutes", defaultValue: 0, required: false
       }
        
        if (!momentary) {
        	section ("When switch is off..."){
        		if (phrases) {
            		input "offPhrase", "enum", title: "Perform this routine", options: phrases, required: false
            	}
        		input "offMode", "mode", title: "Change to this mode", required: false
                input "offSwitches", "capability.switch", title: "Turn off these switches...", multiple: true, required: false
                input "offHTTP", "text", title:"Run this HTTP request...", required: false
                input "delayOff", "number", title: "Delay in minutes", defaultValue: 0, required: false
        	}
        }
        section("Restrictions") {            
			input "runDay", "enum", options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"], title: "Only on certain days of the week...",  multiple: true, required: false
        	href "timeIntervalInput", title: "Only during a certain time...", description: getTimeLabel(timeStart, timeEnd), state: greyedOutTime(timeStart, timeEnd)
            input "runMode", "mode", title: "Only during the following modes...", multiple: true, required: false
		}
    }
}

page(name: "timeIntervalInput", title: "Only during a certain time") {
		section {
			input "timeStart", "time", title: "Starting", required: false
			input "timeEnd", "time", title: "Ending", required: false
		}
} 

def installed() {
    log.debug "Installed with settings: ${settings}"
    initialize()
}

def updated() {
    log.debug "Updated with settings: ${settings}"
    unschedule()
    unsubscribe()
    initialize()
}

def initialize() {
	if (AlexaSwitch){
    	subscribe(AlexaSwitch, "switch", "switchHandler")	
	}
}

//Handlers----------------
def switchHandler(evt) {
    if ((!runMode || runMode.contains(location.mode)) && getDayOk(runDay) && getTimeOk(timeStart,timeEnd)) {    
        log.debug "Alexa Helper scenario triggered"
        if (evt.value == "on" && (onPhrase || onMode || onSwitches || onHTTP)) {
        	if (!delayOn || delayOn == 0) {
            	turnOn()
            }
            else {
            	unschedule 
                runIn(delayOn*60, turnOn, [overwrite: true])
            }
    	} 
    	else if (evt.value == "off" && !momentary && (offPhrase || offMode || offSwitches || offHTTP)) {
        	if (!delayOff || delayOff == 0) {
            	turnOff()
            }
            else {
            	runIn(delayOff*60, turnOff, [overwrite: true])
            }
    	}
	}
}

def turnOn(){
	if (onPhrase){
		location.helloHome.execute(onPhrase)
	}
	if (onMode) {
		changeMode(onMode)
	}
    if (onSwitches){
    	onSwitches?.on()
    }
	if (onHTTP){
    	log.debug "Attempting to run: ${onHTTP}"
        httpGet("${onHTTP}")   
    }
}

def turnOff(){
	if (offPhrase){
		location.helloHome.execute(offPhrase)
	}
	if (offMode) {
		changeMode(offMode)
	}
    if (offSwitches){
    	offSwitches?.off()
    }
    if (offHTTP){
    	log.debug "Attempting to run: ${offHTTP}"
        httpGet("${offHTTP}")
    }
}

//Common Methods-------------

def changeMode(newMode) {
	if (location.mode != newMode) {
		if (location.modes?.find{it.name == newMode}) {
			setLocationMode(newMode)
		} else {
			log.debug "Unable to change to undefined mode '${newMode}'"
		}
	}
}

def getTimeLabel(start, end){
	def timeLabel = "Tap to set"
	
    if(start && end){
    	timeLabel = "Between " + hhmm(start) + " and " +  hhmm(end)
    }
    else if (start) {
		timeLabel = "Start at " + hhmm(start)
    }
    else if(end){
    timeLabel = "End at " + hhmm(end)
    }
	timeLabel	
}

def greyedOutTime(start, end){
	def result = start || end ? "complete" : ""
}

private hhmm(time) {
	new Date().parse("yyyy-MM-dd'T'HH:mm:ss.SSSZ", time).format("h:mm a", timeZone(time))
}

private getDayOk(dayList) {
	def result = true
    if (dayList) {
		def df = new java.text.SimpleDateFormat("EEEE")
		if (location.timeZone) {
			df.setTimeZone(location.timeZone)
		}
		else {
			df.setTimeZone(TimeZone.getTimeZone("America/New_York"))
		}
		def day = df.format(new Date())
		result = dayList.contains(day)
	}
    result
}

private getTimeOk(startTime, endTime) {
	def result = true
	if (startTime && endTime) {
		def currTime = now()
		def start = timeToday(startTime).time
		def stop = timeToday(endTime).time
		result = start < stop ? currTime >= start && currTime <= stop : currTime <= stop || currTime >= start
	}
	else if (startTime){
    	result = currTime >= start
    }
    else if (endTime){
    	result = currTime <= stop
    }
    result
}
//Version
private def textVersion() {
    def text = "Child App Version: 1.2.1 (12/23/2015)"
}
