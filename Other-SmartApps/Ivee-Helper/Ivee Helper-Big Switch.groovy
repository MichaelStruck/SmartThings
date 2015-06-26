/**
 *  Ivee Helper-Big Switch
 *  Version 1.0.0 3/18/15
 *  Version 1.0.1 4/9/15 - Added options sub heading to last section of preferences
 *  Version 1.0.2 6/13/15 - Added About screen and allow for phrase and mode changes tied to master switch
 *  Version 1.1.0 6/15/15 - Allowed for 4 different Ivee control scenarios
 *
 *  Copyright 2015 Michael Struck
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
    name: "Ivee Helper-Big Switch",
    namespace: "MichaelStruck",
    author: "Michael Struck",
    description: "Within each of the 4 scenarios, tie control of a group of switches to a Master Swtich that can take voice commands via the Ivee Talking Alarm Clock.",
    category: "Convenience",
    iconUrl: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Ivee-Helper/Ivee.png",
    iconX2Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Ivee-Helper/Ivee@2x.png",
    iconX3Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Ivee-Helper/Ivee@2x.png")

preferences {
	page name: "mainPage"
    page name: "A_getPref"
    page name: "B_getPref"
    page name: "C_getPref"
    page name: "D_getPref"
}

def mainPage() {
	dynamicPage(name: "mainPage", install:true, uninstall: true) {
    	section("Ivee Controls"){
       		href "A_getPref", title: getTitle(A_name, 1) , description:"", state: greyOut(A_name, A_master)
       		href "B_getPref", title: getTitle(B_name, 2), description:"", state: greyOut(B_name, B_master)
       		href "C_getPref", title: getTitle(C_name, 3), description:"", state: greyOut(C_name, C_master)
       		href "D_getPref", title: getTitle(D_name, 4), description:"", state: greyOut(D_name, D_master)
        }
        section([mobileOnly:true], "Options") {
			label(title: "Assign a name", required: false)
            href "pageAbout", title: "About ${textAppName()}", description: "Tap to get application version, license and instructions"
		}
	}    
}
def A_getPref() {
	dynamicPage(name: "A_getPref") {
		section("Define a 'Master Switch' which Ivee will use.") {
			input "A_master", "capability.switch", multiple: false, title: "Master Switch", required: false
   		}    
   		section("When Ivee turns on the Master Switch...") {
			input "A_lightsOn", "capability.switch", multiple: true, title: "Turn on...", required: false
    		def phrases = location.helloHome?.getPhrases()*.label
            if (phrases) {
				phrases.sort()
				input "A_phraseOn", "enum", title: "Trigger the following phrase...", required: false, options: phrases, multiple: false
			}
        	input "A_modeOn", "mode", title: "Trigger the following mode...", required: false
        
        } 
        section("When Ivee turns off the Master Switch...") {
			input "A_lightsOff", "capability.switch", multiple: true, title: "Turn off...", required: false
    		def phrases = location.helloHome?.getPhrases()*.label
            if (phrases) {
				phrases.sort()
				input "A_phraseOff", "enum", title: "Trigger the following phrase...", required: false, options: phrases, multiple: false
			}
        	input "A_modeOff", "mode", title: "Trigger the following mode...", required: false
        }
        section ("Options"){
        	input "A_name", "text", title: "Ivee Control Name", required: false
            input name:  "A_day", type: "enum", options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"], title: "Run on certain days of the week...",  multiple: true, required: false
        	href "A_timeIntervalInput", title: "Only during a certain time...", description: getTimeLabel(A_timeStart, A_timeEnd), state: greyOut1(A_timeStart, A_timeEnd)
        }
	}
}

page(name: "A_timeIntervalInput", title: "Only during a certain time") {
	section {
		input "A_timeStart", "time", title: "Starting", required: false
		input "A_timeEnd", "time", title: "Ending", required: false
	}
}

def B_getPref() {
	dynamicPage(name: "B_getPref") {
		section("Define a 'Master Switch' which Ivee will use.") {
			input "B_master", "capability.switch", multiple: false, title: "Master Switch", required: false
   		}    
   		section("When Ivee turns on the Master Switch...") {
			input "B_lightsOn", "capability.switch", multiple: true, title: "Turn on...", required: false
    		def phrases = location.helloHome?.getPhrases()*.label
            if (phrases) {
				phrases.sort()
				input "B_phraseOn", "enum", title: "Trigger the following phrase...", required: false, options: phrases, multiple: false
			}
        	input "B_modeOn", "mode", title: "Trigger the following mode...", required: false
        
        } 
        section("When Ivee turns off the Master Switch...") {
			input "B_lightsOff", "capability.switch", multiple: true, title: "Turn off...", required: false
    		def phrases = location.helloHome?.getPhrases()*.label
            if (phrases) {
				phrases.sort()
				input "B_phraseOff", "enum", title: "Trigger the following phrase...", required: false, options: phrases, multiple: false
			}
        	input "B_modeOff", "mode", title: "Trigger the following mode...", required: false
        }
        section ("Options"){
        	input "B_name", "text", title: "Ivee Control Name", required: false
            input name:  "B_day", type: "enum", options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"], title: "Run on certain days of the week...",  multiple: true, required: false
        	href "B_timeIntervalInput", title: "Only during a certain time...", description: getTimeLabel(B_timeStart, B_timeEnd), state: greyOut1(B_timeStart, B_timeEnd)
        }
	}
}

page(name: "B_timeIntervalInput", title: "Only during a certain time") {
	section {
		input "B_timeStart", "time", title: "Starting", required: false
		input "B_timeEnd", "time", title: "Ending", required: false
	}
}


def C_getPref() {
	dynamicPage(name: "C_getPref") {
		section("Define a 'Master Switch' which Ivee will use.") {
			input "C_master", "capability.switch", multiple: false, title: "Master Switch", required: false
   		}    
   		section("When Ivee turns on the Master Switch...") {
			input "C_lightsOn", "capability.switch", multiple: true, title: "Turn on...", required: false
    		def phrases = location.helloHome?.getPhrases()*.label
            if (phrases) {
				phrases.sort()
				input "C_phraseOn", "enum", title: "Trigger the following phrase...", required: false, options: phrases, multiple: false
			}
        	input "C_modeOn", "mode", title: "Trigger the following mode...", required: false
        
        } 
        section("When Ivee turns off the Master Switch...") {
			input "C_lightsOff", "capability.switch", multiple: true, title: "Turn off...", required: false
    		def phrases = location.helloHome?.getPhrases()*.label
            if (phrases) {
				phrases.sort()
				input "C_phraseOff", "enum", title: "Trigger the following phrase...", required: false, options: phrases, multiple: false
			}
        	input "C_modeOff", "mode", title: "Trigger the following mode...", required: false
        }
        section ("Options"){
        	input "C_name", "text", title: "Ivee Control Name", required: false
        	input name:  "C_day", type: "enum", options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"], title: "Run on certain days of the week...",  multiple: true, required: false
        	href "C_timeIntervalInput", title: "Only during a certain time...", description: getTimeLabel(C_timeStart, C_timeEnd), state: greyOut1(C_timeStart, C_timeEnd)
        }
	}
}

page(name: "C_timeIntervalInput", title: "Only during a certain time") {
	section {
		input "C_timeStart", "time", title: "Starting", required: false
		input "C_timeEnd", "time", title: "Ending", required: false
	}
}
def D_getPref() {
	dynamicPage(name: "D_getPref") {
		section("Define a 'Master Switch' which Ivee will use.") {
			input "D_master", "capability.switch", multiple: false, title: "Master Switch", required: false
   		}    
   		section("When Ivee turns on the Master Switch...") {
			input "D_lightsOn", "capability.switch", multiple: true, title: "Turn on...", required: false
    		def phrases = location.helloHome?.getPhrases()*.label
            if (phrases) {
				phrases.sort()
				input "D_phraseOn", "enum", title: "Trigger the following phrase...", required: false, options: phrases, multiple: false
			}
        	input "D_modeOn", "mode", title: "Trigger the following mode...", required: false
        
        } 
        section("When Ivee turns off the Master Switch...") {
			input "D_lightsOff", "capability.switch", multiple: true, title: "Turn off...", required: false
    		def phrases = location.helloHome?.getPhrases()*.label
            if (phrases) {
				phrases.sort()
				input "D_phraseOff", "enum", title: "Trigger the following phrase...", required: false, options: phrases, multiple: false
			}
        	input "D_modeOff", "mode", title: "Trigger the following mode...", required: false
        }
        section ("Options"){
        	input "D_name", "text", title: "Ivee Control Name", required: false
        	input name:  "D_day", type: "enum", options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"], title: "Run on certain days of the week...",  multiple: true, required: false
        	href "D_timeIntervalInput", title: "Only during a certain time...", description: getTimeLabel(D_timeStart, D_timeEnd), state: greyOut1(D_timeStart, D_timeEnd)
        }
	}
}

page(name: "D_timeIntervalInput", title: "Only during a certain time") {
	section {
		input "D_timeStart", "time", title: "Starting", required: false
		input "D_timeEnd", "time", title: "Ending", required: false
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

//--------------------------------------

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
	if (A_master){
    	subscribe(A_master, "switch.on", "onHandler_A")
    	subscribe(A_master, "switch.off", "offHandler_A")
    }
    if (B_master){
    	subscribe(B_master, "switch.on", "onHandler_B")
    	subscribe(B_master, "switch.off", "offHandler_B")
    }
    if (C_master){
    	subscribe(C_master, "switch.on", "onHandler_C")
    	subscribe(C_master, "switch.off", "offHandler_C")
    }
    if (D_master){
    	subscribe(D_master, "switch.on", "onHandler_D")
    	subscribe(D_master, "switch.off", "offHandler_D")
    }
}

//Scenario A
def onHandler_A(evt) {
	if (getDayOk(A_day) && getTimeOk(A_timeStart, A_timeEnd)){
		A_lightsOn?.on()
		if (A_phraseOn) {
        	location.helloHome.execute(A_phraseOn)
		}
		if (A_modeOn && location.mode != A_modeOn) {
			if (location.modes?.find{it.name == A_modeOn}) {
				setLocationMode(A_modeOn)
			} else {
				log.debug "Unable to change to undefined mode '${A_modeOn}'"
			}
		}
		log.debug "Master switch in ${A_name} was turned on. Completing on actions for this Ivee control."
	}
}
def offHandler_A(evt) {
	if (getDayOk(A_day) && getTimeOk(A_timeStart, A_timeEnd)){
        A_lightsOff?.off()
		if (A_phraseOff) {
        	location.helloHome.execute(A_phraseOff)
		}
		if (A_modeOff && location.mode != A_modeOff) {
			if (location.modes?.find{it.name == A_modeOff}) {
				setLocationMode(A_modeOff)
			} else {
				log.debug "Unable to change to undefined mode '${A_modeOff}'"
			}
		}
		log.debug "Master switch in ${A_name} was turned off. Completing off actions for this Ivee control."
	}
}
//Scenario B
def onHandler_B(evt) {
	if (getDayOk(B_day) && getTimeOk(B_timeStart, B_timeEnd)){
		B_lightsOn?.on()
		if (B_phraseOn) {
        	location.helloHome.execute(B_phraseOn)
		}
		if (B_modeOn && location.mode != B_modeOn) {
			if (location.modes?.find{it.name == B_modeOn}) {
				setLocationMode(B_modeOn)
			} else {
				log.debug "Unable to change to undefined mode '${B_modeOn}'"
			}
		}
		log.debug "Master switch in ${B_name} was turned on. Completing on actions for this Ivee control."
	}
}
def offHandler_B(evt) {
	if (getDayOk(B_day) && getTimeOk(B_timeStart, B_timeEnd)){
        B_lightsOff?.off()
		if (B_phraseOff) {
        	location.helloHome.execute(B_phraseOff)
		}
		if (B_modeOff && location.mode != B_modeOff) {
			if (location.modes?.find{it.name == B_modeOff}) {
				setLocationMode(B_modeOff)
			} else {
				log.debug "Unable to change to undefined mode '${B_modeOff}'"
			}
		}
		log.debug "Master switch in ${B_name} was turned off. Completing off actions for this Ivee control."
	}
}
//Scenario C
def onHandler_C(evt) {
	if (getDayOk(C_day) && getTimeOk(C_timeStart, C_timeEnd)){
		C_lightsOn?.on()
		if (C_phraseOn) {
        	location.helloHome.execute(C_phraseOn)
		}
		if (C_modeOn && location.mode != C_modeOn) {
			if (location.modes?.find{it.name == C_modeOn}) {
				setLocationMode(C_modeOn)
			} else {
				log.debug "Unable to change to undefined mode '${C_modeOn}'"
			}
		}
		log.debug "Master switch in ${C_name} was turned on. Completing on actions for this Ivee control."
	}
}
def offHandler_C(evt) {
	if (getDayOk(C_day) && getTimeOk(C_timeStart, C_timeEnd)){
        C_lightsOff?.off()
		if (C_phraseOff) {
        	location.helloHome.execute(C_phraseOff)
		}
		if (C_modeOff && location.mode != C_modeOff) {
			if (location.modes?.find{it.name == C_modeOff}) {
				setLocationMode(C_modeOff)
			} else {
				log.debug "Unable to change to undefined mode '${C_modeOff}'"
			}
		}
		log.debug "Master switch in ${C_name} was turned off. Completing off actions for this Ivee control."
	}
}
//Scenario D
def onHandler_D(evt) {
	if (getDayOk(D_day) && getTimeOk(D_timeStart, D_timeEnd)){
		D_lightsOn?.on()
		if (D_phraseOn) {
        	location.helloHome.execute(D_phraseOn)
		}
		if (D_modeOn && location.mode != D_modeOn) {
			if (location.modes?.find{it.name == D_modeOn}) {
				setLocationMode(D_modeOn)
			} else {
				log.debug "Unable to change to undefined mode '${D_modeOn}'"
			}
		}
		log.debug "Master switch in ${D_name} was turned on. Completing on actions for this Ivee control."
	}
}
def offHandler_D(evt) {
	if (getDayOk(D_day) && getTimeOk(D_timeStart, D_timeEnd)){
        D_lightsOff?.off()
		if (D_phraseOff) {
        	location.helloHome.execute(D_phraseOff)
		}
		if (D_modeOff && location.mode != D_modeOff) {
			if (location.modes?.find{it.name == D_modeOff}) {
				setLocationMode(D_modeOff)
			} else {
				log.debug "Unable to change to undefined mode '${D_modeOff}'"
			}
		}
		log.debug "Master switch in ${D_name} was turned off. Completing off actions for this Ivee control."
	}
}


//----------------------------------
def getTitle(scenario, num) {
	def title = scenario ? scenario : "Ivee Control ${num} not configured"
}

def greyOut(param1, param2){
	def result = param1 && param2 ? "complete" : ""
}

def greyOut1(param1, param2){
	def result = param1 || param2 ? "complete" : ""
}

private getDayOk(dayList) {
	def result = true
	if (dayList) {
		result = dayList.contains(getDay())
	}
	log.trace "DayOk = $result"
    result
}

private getTimeOk(starting, ending) {
	def result = true
	if (starting && ending) {
		def currTime = now()
		def start = timeToday(starting).time
		def stop = timeToday(ending).time
		result = start < stop ? currTime >= start && currTime <= stop : currTime <= stop || currTime >= start
	}
    
    else if (starting){
    	result = currTime >= start
    }
    else if (ending){
    	result = currTime <= stop
    }
	log.trace "timeOk = $result"
	result
}

private getDay(){
	def df = new java.text.SimpleDateFormat("EEEE")
	if (location.timeZone) {
		df.setTimeZone(location.timeZone)
	}
	else {
		df.setTimeZone(TimeZone.getTimeZone("America/New_York"))
	}
	def day = df.format(new Date())
}

def getTimeLabel(start, end){
	def timeLabel = "Tap to set"
	
    if(start && end){
    	timeLabel = "Between" + " " + parseDate(start, "", "h:mm a") + " "  + "and" + " " +  parseDate(end, "", "h:mm a")
    }
    else if (start) {
		timeLabel = "Start at" + " " + parseDate(start, "",  "h:mm a")
    }
    else if(end){
    timeLabel = "End at" + parseDate(end, "", "h:mm a")
    }
	timeLabel	
}

private parseDate(date, epoch, type){
    def parseDate = ""
    if (epoch){
    	long longDate = Long.valueOf(epoch).longValue()
        parseDate = new Date(longDate).format("yyyy-MM-dd'T'HH:mm:ss.SSSZ", location.timeZone)
    }
    else {
    	parseDate = date
    }
    new Date().parse("yyyy-MM-dd'T'HH:mm:ss.SSSZ", parseDate).format("${type}", timeZone(parseDate))
}

//Version/Copyright/Information/Help

private def textAppName() {
	def text = "Ivee Helper - Big Switch"
}	

private def textVersion() {
    def text = "Version 1.1.0 (06/15/2015)"
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
    	"Within each of the 4 Ivee control scenarios, choose a switch that will be considered the 'master switch' to control a  " +
        "group of other switches, a phrase or mode. "+
        "It is recommended these master switches be virtual switches named something that Ivee can understand. While virtual switches are recommended, " +
        "they can be physical devices as well. You will need to associate each of the master switches with the Ivee Talking Alarm Clock (online at the Ivee web site). "+ 
        "Once you choose the various 'slave' switches, phrase and mode you would like to control "+
        "you can then toggle these by saying 'Hello Ivee, turn <on/off> <name of master switch>'."
}
