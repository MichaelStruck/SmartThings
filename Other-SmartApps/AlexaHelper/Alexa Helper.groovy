/**
 *  Alexa Helper
 *
 *  Copyright 2015 Michael Struck
 *  Version 2.0.0 9/1/15
 * 
 *  Version 1.0.0 - Initial release
 *  Version 2.0.0 - Added 6 slots to allow for one app to control multiple on/off actions
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
    name: "Alexa Helper",
    namespace: "MichaelStruck",
    author: "Michael Struck",
    description: "Allows for Hello, Home phrases or modes to be tied to various switch's state controlled by Alexa (Amazon Echo).",
    category: "Convenience",
    iconUrl: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/AlexaHelper/Alexa.png",
    iconX2Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/AlexaHelper/Alexa@2x.png",
    iconX3Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/AlexaHelper/Alexa@2x.png")

preferences {
	page name:"pageSetup"
    page name:"pageSetupScenarioA"
    page name:"pageSetupScenarioB"
    page name:"pageSetupScenarioC"
    page name:"pageSetupScenarioD"
    page name:"pageSetupScenarioE"
    page name:"pageSetupScenarioF"
}

// Show setup page
def pageSetup() {
	dynamicPage(name: "pageSetup", install: true, uninstall: true) {
        section("Setup Menu") {
			href "pageSetupScenarioA", title: getTitle(A_ScenarioName), description: "" , state: greyOut(A_ScenarioName)
            href "pageSetupScenarioB", title: getTitle(B_ScenarioName), description: "" , state: greyOut(B_ScenarioName)
            href "pageSetupScenarioC", title: getTitle(C_ScenarioName), description: "" , state: greyOut(C_ScenarioName)
            href "pageSetupScenarioD", title: getTitle(D_ScenarioName), description: "" , state: greyOut(D_ScenarioName)
            href "pageSetupScenarioE", title: getTitle(E_ScenarioName), description: "" , state: greyOut(E_ScenarioName)
            href "pageSetupScenarioF", title: getTitle(F_ScenarioName), description: "" , state: greyOut(F_ScenarioName)
        }
        section([title:"Options", mobileOnly:true]) {
            label title:"Assign a name", required:false
            href "pageAbout", title: "About ${textAppName()}", description: "Tap to get application version, license and instructions"
        }
    }
}

// Show "pageSetupScenarioA" page
def pageSetupScenarioA() {
	dynamicPage(name: "pageSetupScenarioA") {
    	section("Name your scenario") {
            input "A_ScenarioName", "text", title: "Scenario Name", multiple: false, required: false, defaultValue: empty
    	}
        section("Choose a switch to use...") {
			input "A_switch", "capability.switch", title: "Switch", multiple: false, required: true
    		input "A_momentary", "bool", title: "Is this a momentary switch?", defaultValue: false, submitOnChange:true
        }
        def phrases = location.helloHome?.getPhrases()*.label
			if (phrases) {
        		phrases.sort()
				section("Perform which phrase when...") {
					input "A_onPhrase", "enum", title: "Switch is on", options: phrases, required: false
					if (!A_momentary) {
                    	input "A_offPhrase", "enum", title: "Switch is off", options: phrases, required: false
					}
				}
			}
        section("Change to which mode when...") {
			input "A_onMode", "mode", title: "Switch is on", required: false
			if (!A_momentary) {
            	input "A_offMode", "mode", title: "Switch is off", required: false
            }
		}
        section("Restrictions") {            
			input "A_day", "enum", options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"], title: "Only on certain days of the week...",  multiple: true, required: false
        	href "timeIntervalInputA", title: "Only during a certain time...", description: getTimeLabel(A_timeStart, A_timeEnd), state: greyedOutTime(A_timeStart, A_timeEnd)
            input "A_mode", "mode", title: "Only during the following modes...", multiple: true, required: false
		}
    }
}

page(name: "timeIntervalInputA", title: "Only during a certain time") {
		section {
			input "A_timeStart", "time", title: "Starting", required: false
			input "A_timeEnd", "time", title: "Ending", required: false
		}
} 

// Show "pageSetupScenarioB" page
def pageSetupScenarioB() {
	dynamicPage(name: "pageSetupScenarioB") {
    	section("Name your scenario") {
            input "B_ScenarioName", "text", title: "Scenario Name", multiple: false, required: false, defaultValue: empty
    	}
        section("Choose a switch to use...") {
			input "B_switch", "capability.switch", title: "Switch", multiple: false, required: true
    		input "B_momentary", "bool", title: "Is this a momentary switch?", defaultValue: false, submitOnChange:true
        }
        def phrases = location.helloHome?.getPhrases()*.label
			if (phrases) {
        		phrases.sort()
				section("Perform which phrase when...") {
					input "B_onPhrase", "enum", title: "Switch is on", options: phrases, required: false
					if (!B_momentary) {
                    	input "B_offPhrase", "enum", title: "Switch is off", options: phrases, required: false
					}
				}
			}
        section("Change to which mode when...") {
			input "B_onMode", "mode", title: "Switch is on", required: false
			if (!B_momentary) {
            	input "B_offMode", "mode", title: "Switch is off", required: false
            }
		}
        section("Restrictions") {            
			input "B_day", "enum", options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"], title: "Only on certain days of the week...",  multiple: true, required: false
        	href "timeIntervalInputB", title: "Only during a certain time...", description: getTimeLabel(A_timeStart, A_timeEnd), state: greyedOutTime(A_timeStart, A_timeEnd)
            input "B_mode", "mode", title: "Only during the following modes...", multiple: true, required: false
		}
    }
}

page(name: "timeIntervalInputB", title: "Only during a certain time") {
		section {
			input "B_timeStart", "time", title: "Starting", required: false
			input "B_timeEnd", "time", title: "Ending", required: false
		}
} 

// Show "pageSetupScenarioC" page
def pageSetupScenarioC() {
	dynamicPage(name: "pageSetupScenarioC") {
    	section("Name your scenario") {
            input "C_ScenarioName", "text", title: "Scenario Name", multiple: false, required: false, defaultValue: empty
    	}
        section("Choose a switch to use...") {
			input "C_switch", "capability.switch", title: "Switch", multiple: false, required: true
    		input "C_momentary", "bool", title: "Is this a momentary switch?", defaultValue: false, submitOnChange:true
        }
        def phrases = location.helloHome?.getPhrases()*.label
			if (phrases) {
        		phrases.sort()
				section("Perform which phrase when...") {
					input "C_onPhrase", "enum", title: "Switch is on", options: phrases, required: false
					if (!C_momentary) {
                    	input "C_offPhrase", "enum", title: "Switch is off", options: phrases, required: false
					}
				}
			}
        section("Change to which mode when...") {
			input "C_onMode", "mode", title: "Switch is on", required: false
			if (!C_momentary) {
            	input "C_offMode", "mode", title: "Switch is off", required: false
            }
		}
        section("Restrictions") {            
			input "C_day", "enum", options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"], title: "Only on certain days of the week...",  multiple: true, required: false
        	href "timeIntervalInputC", title: "Only during a certain time...", description: getTimeLabel(A_timeStart, A_timeEnd), state: greyedOutTime(A_timeStart, A_timeEnd)
            input "C_mode", "mode", title: "Only during the following modes...", multiple: true, required: false
		}
    }
}

page(name: "timeIntervalInputC", title: "Only during a certain time") {
		section {
			input "C_timeStart", "time", title: "Starting", required: false
			input "C_timeEnd", "time", title: "Ending", required: false
		}
}

// Show "pageSetupScenarioD" page
def pageSetupScenarioD() {
	dynamicPage(name: "pageSetupScenarioD") {
    	section("Name your scenario") {
            input "D_ScenarioName", "text", title: "Scenario Name", multiple: false, required: false, defaultValue: empty
    	}
        
        section("Choose a switch to use...") {
			input "D_switch", "capability.switch", title: "Switch", multiple: false, required: true
    		input "D_momentary", "bool", title: "Is this a momentary switch?", defaultValue: false, submitOnChange:true
        }
        def phrases = location.helloHome?.getPhrases()*.label
			if (phrases) {
        		phrases.sort()
				section("Perform which phrase when...") {
					input "D_onPhrase", "enum", title: "Switch is on", options: phrases, required: false
					if (!D_momentary) {
                    	input "D_offPhrase", "enum", title: "Switch is off", options: phrases, required: false
					}
				}
			}
        section("Change to which mode when...") {
			input "D_onMode", "mode", title: "Switch is on", required: false
			if (!D_momentary) {
            	input "D_offMode", "mode", title: "Switch is off", required: false
            }
		}
section("Restrictions") {            
			input "D_day", "enum", options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"], title: "Only on certain days of the week...",  multiple: true, required: false
        	href "timeIntervalInputD", title: "Only during a certain time...", description: getTimeLabel(A_timeStart, A_timeEnd), state: greyedOutTime(A_timeStart, A_timeEnd)
            input "D_mode", "mode", title: "Only during the following modes...", multiple: true, required: false
		}
    }
}

page(name: "timeIntervalInputD", title: "Only during a certain time") {
		section {
			input "D_timeStart", "time", title: "Starting", required: false
			input "D_timeEnd", "time", title: "Ending", required: false
		}
}

// Show "pageSetupScenarioE" page
def pageSetupScenarioE() {
	dynamicPage(name: "pageSetupScenarioE") {
    	section("Name your scenario") {
            input "E_ScenarioName", "text", title: "Scenario Name", multiple: false, required: false, defaultValue: empty
    	}
        
        section("Choose a switch to use...") {
			input "E_switch", "capability.switch", title: "Switch", multiple: false, required: true
    		input "E_momentary", "bool", title: "Is this a momentary switch?", defaultValue: false, submitOnChange:true
        }
        def phrases = location.helloHome?.getPhrases()*.label
			if (phrases) {
        		phrases.sort()
				section("Perform which phrase when...") {
					input "E_onPhrase", "enum", title: "Switch is on", options: phrases, required: false
					if (!E_momentary) {
                    	input "E_offPhrase", "enum", title: "Switch is off", options: phrases, required: false
					}
				}
			}
        section("Change to which mode when...") {
			input "E_onMode", "mode", title: "Switch is on", required: false
			if (!E_momentary) {
            	input "E_offMode", "mode", title: "Switch is off", required: false
            }
		}
        section("Restrictions") {            
			input "E_day", "enum", options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"], title: "Only on certain days of the week...",  multiple: true, required: false
        	href "timeIntervalInputE", title: "Only during a certain time...", description: getTimeLabel(E_timeStart, E_timeEnd), state: greyedOutTime(E_timeStart, E_timeEnd)
            input "E_mode", "mode", title: "Only during the following modes...", multiple: true, required: false
		}
    }
}

page(name: "timeIntervalInputE", title: "Only during a certain time") {
		section {
			input "E_timeStart", "time", title: "Starting", required: false
			input "E_timeEnd", "time", title: "Ending", required: false
		}
}

// Show "pageSetupScenarioF" page
def pageSetupScenarioF() {
	dynamicPage(name: "pageSetupScenarioF") {
    	section("Name your scenario") {
            input "F_ScenarioName", "text", title: "Scenario Name", multiple: false, required: false, defaultValue: empty
    	}
        
        section("Choose a switch to use...") {
			input "F_switch", "capability.switch", title: "Switch", multiple: false, required: true
    		input "F_momentary", "bool", title: "Is this a momentary switch?", defaultValue: false, submitOnChange:true
        }
        def phrases = location.helloHome?.getPhrases()*.label
			if (phrases) {
        		phrases.sort()
				section("Perform which phrase when...") {
					input "F_onPhrase", "enum", title: "Switch is on", options: phrases, required: false
					if (!F_momentary) {
                    	input "F_offPhrase", "enum", title: "Switch is off", options: phrases, required: false
					}
				}
			}
        section("Change to which mode when...") {
			input "F_onMode", "mode", title: "Switch is on", required: false
			if (!F_momentary) {
            	input "F_offMode", "mode", title: "Switch is off", required: false
            }
		}
        section("Restrictions") {            
			input "F_day", "enum", options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"], title: "Only on certain days of the week...",  multiple: true, required: false
        	href "timeIntervalInputF", title: "Only during a certain time...", description: getTimeLabel(F_timeStart, F_timeEnd), state: greyedOutTime(F_timeStart, F_timeEnd)
            input "F_mode", "mode", title: "Only during the following modes...", multiple: true, required: false
		}
    }
}

page(name: "timeIntervalInputF", title: "Only during a certain time") {
		section {
			input "F_timeStart", "time", title: "Starting", required: false
			input "F_timeEnd", "time", title: "Ending", required: false
		}
}

page(name: "pageAbout", title: "About ${textAppName()}") {
        section {
            paragraph "${textVersion()}\n${textCopyright()}\n\n${textLicense()}\n"
        }
        section("Instructions") {
            paragraph textHelp()
        }
}

def installed() {
    log.debug "Installed with settings: ${settings}"
    initialize()
}

def updated() {
    log.debug "Updated with settings: ${settings}"
    unsubscribe()
    initialize()
}

def initialize() {
	if (A_ScenarioName && A_switch){
    	subscribe(A_switch, "switch", "A_switchHandler")	
	}
    if (B_ScenarioName && B_switch){
    	subscribe(B_switch, "switch", "B_switchHandler")	
	}
	if (C_ScenarioName && C_switch){
    	subscribe(C_switch, "switch", "C_switchHandler")	
	}
    if (D_ScenarioName && D_switch){
    	subscribe(D_switch, "switch", "D_switchHandler")	
	}
    if (E_ScenarioName && E_switch){
    	subscribe(E_switch, "switch", "E_switchHandler")	
	}
    if (F_ScenarioName && F_switch){
    	subscribe(F_switch, "switch", "F_switchHandler")	
	}
}
//Handlers----------------
//A Handlers
def A_switchHandler(evt) {
	if ((!A_mode || A_mode.contains(location.mode)) && getDayOk(A_day) && getTimeOk(A_timeStart,A_timeEnd)) {    
    	log.debug "Running ${A_ScenarioName}"
        if (evt.value == "on" && (A_onPhrase || A_onMode)) {
        	if (A_onPhrase){
        		location.helloHome.execute(A_onPhrase)
        	}
        	if (A_onMode) {
        		changeMode(A_onMode)
        	}
    	} 
    	else if (evt.value == "off" && !A_momentary && (A_offPhrase || A_offMode)) {
        	if (A_offPhrase){
        		location.helloHome.execute(A_offPhrase)
    		}
        	if (A_offMode) {
        		changeMode(A_offMode)
        	}
    	}
	}
}

//B Handlers
def B_switchHandler(evt) {
    if ((!B_mode || B_mode.contains(location.mode)) && getDayOk(B_day) && getTimeOk(B_timeStart,B_timeEnd)) { 
    	log.debug "Running ${B_ScenarioName}"
        if (evt.value == "on" && (B_onPhrase || B_onMode)) {
    		if (B_onPhrase){
        		location.helloHome.execute(B_onPhrase)
        	}
        	if (B_onMode) {
        		changeMode(B_onMode)
        	}
    	} 
    	else if (evt.value == "off" && !B_momentary && (B_offPhrase || B_offMode)) {
        	if (B_offPhrase){
        		location.helloHome.execute(B_offPhrase)
    		}
        	if (B_offMode) {
        		changeMode(B_offMode)
        	}
    	}
	}
}

//C Handlers
def C_switchHandler(evt) {
    if ((!C_mode || C_mode.contains(location.mode)) && getDayOk(C_day) && getTimeOk(C_timeStart,C_timeEnd)) { 
    	log.debug "Running ${C_ScenarioName}"
        if (evt.value == "on" && (C_onPhrase || C_onMode)) {
    		if (C_onPhrase){
        		location.helloHome.execute(C_onPhrase)
        	}
        	if (C_onMode) {
        		changeMode(C_onMode)
        	}
    	} 
    	else if (evt.value == "off" && !C_momentary && (C_offPhrase || C_offMode)) {
        	if (C_offPhrase){
        		location.helloHome.execute(C_offPhrase)
    		}
        	if (C_offMode) {
        		changeMode(C_offMode)
        	}
   		}
	}
}

//D Handlers
def D_switchHandler(evt) {
    if ((!D_mode || D_mode.contains(location.mode)) && getDayOk(D_day) && getTimeOk(D_timeStart,D_timeEnd)) { 
        log.debug "Running ${D_ScenarioName}"
        if (evt.value == "on" && (D_onPhrase || D_onMode)) {
    		if (D_onPhrase){
        		location.helloHome.execute(D_onPhrase)
        	}
        	if (D_onMode) {
        		changeMode(D_onMode)
        	}
    	} 
    	else if (evt.value == "off" && !D_momentary && (D_offPhrase || D_offMode)) {
        	if (D_offPhrase){
        		location.helloHome.execute(D_offPhrase)
    		}
        	if (D_offMode) {
        		changeMode(D_offMode)
        	}
    	}
	}
}

//E Handlers
def E_switchHandler(evt) {
	if ((!E_mode || E_mode.contains(location.mode)) && getDayOk(E_day) && getTimeOk(E_timeStart,E_timeEnd)) {    
    	log.debug "Running ${E_ScenarioName}"
        if (evt.value == "on" && (E_onPhrase || E_onMode)) {
        	if (E_onPhrase){
        		location.helloHome.execute(E_onPhrase)
        	}
        	if (E_onMode) {
        		changeMode(E_onMode)
        	}
    	} 
    	else if (evt.value == "off" && !E_momentary && (E_offPhrase || E_offMode)) {
        	if (E_offPhrase){
        		location.helloHome.execute(E_offPhrase)
    		}
        	if (E_offMode) {
        		changeMode(E_offMode)
        	}
    	}
	}
}

//F Handlers
def F_switchHandler(evt) {
	if ((!F_mode || F_mode.contains(location.mode)) && getDayOk(F_day) && getTimeOk(F_timeStart,F_timeEnd)) {    
    	log.debug "Running ${F_ScenarioName}"
        if (evt.value == "on" && (F_onPhrase || F_onMode)) {
        	if (F_onPhrase){
        		location.helloHome.execute(F_onPhrase)
        	}
        	if (F_onMode) {
        		changeMode(F_onMode)
        	}
    	} 
    	else if (evt.value == "off" && !F_momentary && (F_offPhrase || F_offMode)) {
        	if (F_offPhrase){
        		location.helloHome.execute(F_offPhrase)
    		}
        	if (F_offMode) {
        		changeMode(F_offMode)
        	}
    	}
	}
}

//Common Methods-------------

def greyOut(scenario){
    def result = scenario ? "complete" : ""
}

def getTitle(scenario) {
	def title = scenario ? scenario : "Empty"
}

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

//Version/Copyright/Information/Help

private def textAppName() {
	def text = "Alexa Helper"
}	

private def textVersion() {
    def text = "Version 2.0.0 (09/01/2015)"
}

private def textCopyright() {
    def text = "Copyright © 2015 Michael Struck"
}

private def textLicense() {
    def text =
		"Licensed under the Apache License, Version 2.0 (the 'License'); "+
		"you may not use this file except in compliance with the License. "+
		"You may obtain a copy of the License at"+
		"\n\n"+
		"    http://www.apache.org/licenses/LICENSE-2.0"+
		"\n\n"+
		"Unless required by applicable law or agreed to in writing, software "+
		"distributed under the License is distributed on an 'AS IS' BASIS, "+
		"WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. "+
		"See the License for the specific language governing permissions and "+
		"limitations under the License."
}

private def textHelp() {
	def text =
		"Ties Hello, Home phrases or modes to switches' on/off state. The switches can be virtual on/off swithes, "+
		"virtual momentary tiles, or physical switches. Perfect for use with Alexa.\n\n"+
		"To use, first create momentary button tiles or virtual switchs from the IDE and discover the new devices so it can be seen by Alexa. "+
		"Then, within one of the six scnearios, simple define which switch is to be used and tie the on/off state of the switch to a specific Hello, Home phrases or mode. "+
		"TThe Hello, Home phrase or mode will fire with the switch state changes. Please note that if you are using a momentary switch you should only define the 'on' action."
}
