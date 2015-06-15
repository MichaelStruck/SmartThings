/**
 *  Announce Presence
 *  Version 1.0.0 6/9/15
 *
 *  Copyright 2015 Michael Struck
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *	Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *  Turn specific switched on when it gets dark. Can also turn off specific switches when it becomes light again based on brightness from a connected light sensor.
 *
 */

definition(
    name: "Announce Presence",
    namespace: "MichaelStruck",
    author: "Michael Struck",
    description: "Announce (via Sonos Speakers) the approach of a presense sensor",
    category: "Convenience",
    iconUrl: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Announce-Presence/AnnouncePresence.png",
    iconX2Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Announce-Presence/AnnouncePresence@2x.png",
	iconX3Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Announce-Presence/AnnouncePresence@2x.png")

preferences {
	page name: "mainPage"
  page name: "A_scenario"
	page name: "B_scenario"
	page name: "C_scenario"
	page name: "D_scenario"
	page name: "E_scenario"
}

def mainPage() {
    dynamicPage(name: "mainPage", install:true, uninstall: true) {
		section ("Alerting Scenarios") {
        	href "A_scenario", title: getTitle(A_scenarioName, 1), description: getDesc(A_scenarioName, 1), state: greyedOutState(A_scenarioName, A_sensors, A_friendlyName, A_sonos)
		}
		section {
        	href "B_scenario", title: getTitle(B_scenarioName, 2), description: getDesc(B_scenarioName, 2), state: greyedOutState(B_scenarioName, B_sensors, B_friendlyName, B_sonos)
		}
        section {
        	href "C_scenario", title: getTitle(C_scenarioName, 3), description: getDesc(C_scenarioName, 3), state: greyedOutState(C_scenarioName, C_sensors, C_friendlyName, C_sonos)
		}
        section {
        	href "D_scenario", title: getTitle(D_scenarioName, 4), description: getDesc(D_scenarioName, 4), state: greyedOutState(D_scenarioName, D_sensors, D_friendlyName, D_sonos)
		}
        section {
        	href "E_scenario", title: getTitle(E_scenarioName, 5), description: getDesc(E_scenarioName, 5), state: greyedOutState(E_scenarioName, E_sensors, E_friendlyName, E_sonos)
		}
		section([mobileOnly:true], "Options") {
			label(title: "Assign a name", required: false)
            href "pageAbout", title: "About ${textAppName()}", description: "Tap to get application version, license and instructions"
		}  
	}
}
//scenario A
def A_scenario() {
    dynamicPage(name: "A_scenario") {
    	section("Choose the presence sensors you want to monitor...") {
			input "A_sensors", "capability.presenceSensor", multiple: true, title: "Presence Sensors" , required: false
			input "A_friendlyName", "text", title: "Voice-friendly name for these sensors (%name%)", required: false
        	input "A_friendlyLoc", "text", title: "Voice-friendly location name (%location%)", required: false
        }
       section("Verbal Alerting"){
        	input "A_sonos", "capability.musicPlayer", title: "Sonos speaker for verbal messaging", required: false
            input "A_volume", "number", title: "Alarm volume", description: "0-100%", required: false
            input "A_msgAllPresent", "text", title: "Message: All sensors change to 'present'", required: false
            input "A_msgSomePresent", "text", title: "Message: Any sensor within group changes", required: false
            input "A_msgAllGone", "text", title: "Message: All sensors change to 'not present'", required: false
        } 
       	section ("Options"){
        	input "A_scenarioName", "text", title: "Enter a name for this alert scenario", required: false
	       	input "A_triggerOnce", "bool", title: "Trigger only once per day...", defaultValue: false
        	href "A_timeInput", title: "Only during a certain time...", description: getTimeLabel(A_timeStart, A_timeEnd), state: greyedOutTime(A_timeStart, A_timeEnd)
        	input "A_day", "enum", options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"], title: "Only on certain days of the week...",  multiple: true, required: false
        	input "A_mode", "mode", title: "Only during certain modes...", multiple: true, required: false
       } 
    }
}

page(name: "A_timeInput", title: "Only during a certain time") {
	section {
		input "A_timeStart", "time", title: "Starting", required: false
		input "A_timeEnd", "time", title: "Ending", required: false
	}
}

//scenario B
def B_scenario() {
    dynamicPage(name: "B_scenario") {
    	section("Choose the presence sensors you want to monitor...") {
			input "B_sensors", "capability.presenceSensor", multiple: true, title: "Presence Sensors" , required: false
			input "B_friendlyName", "text", title: "Voice-friendly name for these sensors (%name%)", required: false
        	input "B_friendlyLoc", "text", title: "Voice-friendly location name (%location%)", required: false
        }
       section("Verbal Alerting"){
        	input "B_sonos", "capability.musicPlayer", title: "Sonos speaker for verbal messaging", required: false
            input "V_volume", "number", title: "Alarm volume", description: "0-100%", required: false
            input "B_msgAllPresent", "text", title: "Message: All sensors change to 'present'", required: false
            input "B_msgSomePresent", "text", title: "Message: Any sensor within group changes", required: false
            input "B_msgAllGone", "text", title: "Message: All sensors change to 'not present'", required: false
        } 
       	section ("Options"){
        	input "B_scenarioName", "text", title: "Enter a name for this alert scenario", required: false
	       	input "B_triggerOnce", "bool", title: "Trigger only once per day...", defaultValue: false
        	href "B_timeInput", title: "Only during a certain time...", description: getTimeLabel(B_timeStart, B_timeEnd), state: greyedOutTime(B_timeStart, B_timeEnd)
        	input "B_day", "enum", options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"], title: "Only on certain days of the week...",  multiple: true, required: false
        	input "B_mode", "mode", title: "Only during certain modes...", multiple: true, required: false
       } 
    }
}

page(name: "B_timeInput", title: "Only during a certain time") {
	section {
		input "B_timeStart", "time", title: "Starting", required: false
		input "B_timeEnd", "time", title: "Ending", required: false
	}
}

//scenario C
def C_scenario() {
    dynamicPage(name: "C_scenario") {
    	section("Choose the presence sensors you want to monitor...") {
			input "C_sensors", "capability.presenceSensor", multiple: true, title: "Presence Sensors" , required: false
			input "C_friendlyName", "text", title: "Voice-friendly name for these sensors (%name%)", required: false
        	input "C_friendlyLoc", "text", title: "Voice-friendly location name (%location%)", required: false
        }
       section("Verbal Alerting"){
        	input "C_sonos", "capability.musicPlayer", title: "Sonos speaker for verbal messaging", required: false
            input "C_volume", "number", title: "Alarm volume", description: "0-100%", required: false
            input "C_msgAllPresent", "text", title: "Message: All sensors change to 'present'", required: false
            input "C_msgSomePresent", "text", title: "Message: Any sensor within group changes", required: false
            input "C_msgAllGone", "text", title: "Message: All sensors change to 'not present'", required: false
        } 
       	section ("Options"){
        	input "C_scenarioName", "text", title: "Enter a name for this alert scenario", required: false
	       	input "C_triggerOnce", "bool", title: "Trigger only once per day...", defaultValue: false
        	href "C_timeInput", title: "Only during a certain time...", description: getTimeLabel(C_timeStart, C_timeEnd), state: greyedOutTime(C_timeStart, C_timeEnd)
        	input "C_day", "enum", options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"], title: "Only on certain days of the week...",  multiple: true, required: false
        	input "C_mode", "mode", title: "Only during certain modes...", multiple: true, required: false
       } 
    }
}

page(name: "C_timeInput", title: "Only during a certain time") {
	section {
		input "C_timeStart", "time", title: "Starting", required: false
		input "C_timeEnd", "time", title: "Ending", required: false
	}
}

//scenario D
def D_scenario() {
    dynamicPage(name: "D_scenario") {
    	section("Choose the presence sensors you want to monitor...") {
			input "D_sensors", "capability.presenceSensor", multiple: true, title: "Presence Sensors" , required: false
			input "D_friendlyName", "text", title: "Voice-friendly name for these sensors (%name%)", required: false
        	input "D_friendlyLoc", "text", title: "Voice-friendly location name (%location%)", required: false
        }
       section("Verbal Alerting"){
        	input "D_sonos", "capability.musicPlayer", title: "Sonos speaker for verbal messaging", required: false
            input "D_volume", "number", title: "Alarm volume", description: "0-100%", required: false
            input "D_msgAllPresent", "text", title: "Message: All sensors change to 'present'", required: false
            input "D_msgSomePresent", "text", title: "Message: Any sensor within group changes", required: false
            input "D_msgAllGone", "text", title: "Message: All sensors change to 'not present'", required: false
        } 
       	section ("Options"){
        	input "D_scenarioName", "text", title: "Enter a name for this alert scenario", required: false
	       	input "D_triggerOnce", "bool", title: "Trigger only once per day...", defaultValue: false
        	href "D_timeInput", title: "Only during a certain time...", description: getTimeLabel(D_timeStart, D_timeEnd), state: greyedOutTime(D_timeStart, D_timeEnd)
        	input "D_day", "enum", options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"], title: "Only on certain days of the week...",  multiple: true, required: false
        	input "D_mode", "mode", title: "Only during certain modes...", multiple: true, required: false
       } 
    }
}

page(name: "D_timeInput", title: "Only during a certain time") {
	section {
		input "D_timeStart", "time", title: "Starting", required: false
		input "D_timeEnd", "time", title: "Ending", required: false
	}
}

//scenario E
def E_scenario() {
    dynamicPage(name: "E_scenario") {
    	section("Choose the presence sensors you want to monitor...") {
			input "E_sensors", "capability.presenceSensor", multiple: true, title: "Presence Sensors" , required: false
			input "E_friendlyName", "text", title: "Voice-friendly name for these sensors (%name%)", required: false
        	input "E_friendlyLoc", "text", title: "Voice-friendly location name (%location)", required: false
        }
       section("Verbal Alerting"){
        	input "E_sonos", "capability.musicPlayer", title: "Sonos speaker for verbal messaging", required: false
            input "E_volume", "number", title: "Alarm volume", description: "0-100%", required: false
            input "E_msgAllPresent", "text", title: "Message: All sensors change to 'present'", required: false
            input "E_msgSomePresent", "text", title: "Message: Any sensor within group changes", required: false
            input "E_msgAllGone", "text", title: "Message: All sensors change to 'not present'", required: false
        } 
       	section ("Options"){
        	input "E_scenarioName", "text", title: "Enter a name for this alert scenario", required: false
	       	input "E_triggerOnce", "bool", title: "Trigger only once per day...", defaultValue: false
        	href "E_timeInput", title: "Only during a certain time...", description: getTimeLabel(E_timeStart, E_timeEnd), state: greyedOutTime(E_timeStart, E_timeEnd)
        	input "E_day", "enum", options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"], title: "Only on certain days of the week...",  multiple: true, required: false
        	input "E_mode", "mode", title: "Only during certain modes...", multiple: true, required: false
       } 
    }
}

page(name: "E_timeInput", title: "Only during a certain time") {
	section {
		input "E_timeStart", "time", title: "Starting", required: false
		input "E_timeEnd", "time", title: "Ending", required: false
	}
}

//About page
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
	init()
}

def updated() {
	log.debug "Updated with settings: ${settings}"
	unschedule()
    unsubscribe()
	init()
}

def init(){
	midNightReset()
    if (A_sonos && A_sensors && (A_msgAllPresent || A_msgSomePresent || A_msgAllGone || A_msgSomeGone)) {
    	subscribe(A_sensors, "presence", presenceHandlerA)	
    }
    if (B_sonos && B_sensors && (B_msgAllPresent || B_msgSomePresent || B_msgAllGone || B_msgSomeGone)) {
    	subscribe(B_sensors, "presence", presenceHandlerB)	
    }
    if (C_sonos && C_sensors && (C_msgAllPresent || C_msgSomePresent || C_msgAllGone || C_msgSomeGone)) {
    	subscribe(C_sensors, "presence", presenceHandlerC)	
    }
    if (D_sonos && D_sensors && (D_msgAllPresent || D_msgSomePresent || D_msgAllGone || D_msgSomeGone)) {
    	subscribe(D_sensors, "presence", presenceHandlerD)	
    }
    if (E_sonos && E_sensors && (E_msgAllPresent || E_msgSomePresent || E_msgAllGone || E_msgSomeGone)) {
    	subscribe(E_sensors, "presence", presenceHandlerE)	
    }
}

//
def presenceHandlerA(evt) {	
	if ((!A_triggerOnce || (A_triggerOnce && !state.A_triggered)) && (!A_mode || A_mode.contains(location.mode)) && getTimeOk (A_timeStart, A_timeEnd) && getDayOk(A_day)) {
		def everyonePresent = A_sensors.find {it.currentPresence == "not present"} ? false : true
    	def somePresent = A_sensors.find {it.currentPresence == "present"} ? true : false
    	state.A_fullMsg = ""
    	if (everyonePresent && A_msgAllPresent){
    		compileMsg (A_msgAllPresent, 1, A_friendlyName, A_friendlyLoc)
    	}
    
    	if (somePresent && (A_msgSomePresent)) {
       		compileMsg (A_msgSomePresent, 1, A_friendlyName, A_friendlyLoc)
    	}
    
    	if (!everyonePresent && !somePresent && A_msgAllGone){
    		compileMsg (A_msgAllGone, 1, A_friendlyName, A_friendlyLoc)
    	}
		
        state.A_sound = textToSpeech(state.A_fullMsg, true)
        if (A_volume) {
        	A_sonos.setLevel(A_volume)
		}
        A_sonos.playTrack(state.A_sound.uri)
		
        if (A_triggerOnce){
    		state.A_triggered = true
			runOnce (getMidnight(), midNightReset)
   		}
    }
}

def presenceHandlerB(evt) {	
	if ((!B_triggerOnce || (B_triggerOnce && !state.B_triggered)) && (!B_mode || B_mode.contains(location.mode)) && getTimeOk (B_timeStart, B_timeEnd) && getDayOk(B_day)) {
		def everyonePresent = B_sensors.find {it.currentPresence == "not present"} ? false : true
    	def somePresent = B_sensors.find {it.currentPresence == "present"} ? true : false
    	state.B_fullMsg = ""
    	if (everyonePresent && B_msgAllPresent){
    		compileMsg (B_msgAllPresent, 2, B_friendlyName, B_friendlyLoc)
    	}
    
    	if (somePresent && (B_msgSomePresent)) {
       		compileMsg (B_msgSomePresent, 2, B_friendlyName, B_friendlyLoc)
    	}
    
    	if (!everyonePresent && !somePresent && B_msgAllGone){
    		compileMsg (B_msgAllGone, 2, B_friendlyName, B_friendlyLoc)
    	}

		state.B_sound = textToSpeech(state.B_fullMsg, true)
        if (B_volume) {
        	B_sonos.setLevel(B_volume)
		}
        B_sonos.playTrack(state.B_sound.uri)

		if (B_triggerOnce){
    		state.B_triggered = true
			runOnce (getMidnight(), midNightReset)
   		}
    }
}

def presenceHandlerC(evt) {	
	if ((!C_triggerOnce || (C_triggerOnce && !state.C_triggered)) && (!C_mode || C_mode.contains(location.mode)) && getTimeOk (C_timeStart, C_timeEnd) && getDayOk(C_day)) {
		def everyonePresent = C_sensors.find {it.currentPresence == "not present"} ? false : true
    	def somePresent = C_sensors.find {it.currentPresence == "present"} ? true : false
    	state.C_fullMsg = ""
    	if (everyonePresent && C_msgAllPresent){
    		compileMsg (C_msgAllPresent, 3, C_friendlyName, C_friendlyLoc)
    	}
    
    	if (somePresent && (C_msgSomePresent)) {
       		compileMsg (C_msgSomePresent, 3, C_friendlyName, C_friendlyLoc)
    	}
    
    	if (!everyonePresent && !somePresent && C_msgAllGone){
    		compileMsg (C_msgAllGone, 3, C_friendlyName, C_friendlyLoc)
    	}
        
        state.C_sound = textToSpeech(state.C_fullMsg, true)
        if (C_volume) {
        	C_sonos.setLevel(C_volume)
		}
        C_sonos.playTrack(state.C_sound.uri)

		if (C_triggerOnce){
    		state.C_triggered = true
			runOnce (getMidnight(), midNightReset)
   		}
    }
}

def presenceHandlerD(evt) {	
	if ((!D_triggerOnce || (D_triggerOnce && !state.D_triggered)) && (!D_mode || D_mode.contains(location.mode)) && getTimeOk (D_timeStart, D_timeEnd) && getDayOk(D_day)) {
		def everyonePresent = D_sensors.find {it.currentPresence == "not present"} ? false : true
    	def somePresent = D_sensors.find {it.currentPresence == "present"} ? true : false
    	state.D_fullMsg = ""
    	if (everyonePresent && D_msgAllPresent){
    		compileMsg (D_msgAllPresent, 4, D_friendlyName, D_friendlyLoc)
    	}
    
    	if (somePresent && (D_msgSomePresent)) {
       		compileMsg (D_msgSomePresent, 4, D_friendlyName, D_friendlyLoc)
    	}
    
    	if (!everyonePresent && !somePresent && D_msgAllGone){
    		compileMsg (D_msgAllGone, 4, D_friendlyName, D_friendlyLoc)
    	}

		state.D_sound = textToSpeech(state.D_fullMsg, true)
        if (D_volume) {
        	D_sonos.setLevel(D_volume)
		}
        D_sonos.playTrack(state.D_sound.uri)

		if (D_triggerOnce){
    		state.D_triggered = true
			runOnce (getMidnight(), midNightReset)
   		}
    }
}

def presenceHandlerE(evt) {	
	if ((!E_triggerOnce || (E_triggerOnce && !state.E_triggered)) && (!E_mode || E_mode.contains(location.mode)) && getTimeOk (E_timeStart, E_timeEnd) && getDayOk(E_day)) {
		def everyonePresent = E_sensors.find {it.currentPresence == "not present"} ? false : true
    	def somePresent = E_sensors.find {it.currentPresence == "present"} ? true : false
    	state.E_fullMsg = ""
    	if (everyonePresent && E_msgAllPresent){
    		compileMsg (E_msgAllPresent, 5, E_friendlyName, E_friendlyLoc)
    	}
    
    	if (somePresent && (E_msgSomePresent)) {
       		compileMsg (E_msgSomePresent, 5, E_friendlyName, E_friendlyLoc)
    	}
    
    	if (!everyonePresent && !somePresent && E_msgAllGone){
    		compileMsg (E_msgAllGone, 5, E_friendlyName, E_friendlyLoc)
    	}
        
        state.E_sound = textToSpeech(state.E_fullMsg, true)
        if (E_volume) {
        	E_sonos.setLevel(E_volume)
		}
        E_sonos.playTrack(state.E_sound.uri)

		if (E_triggerOnce){
    		state.E_triggered = true
			runOnce (getMidnight(), midNightReset)
   		}
    }
}

//Common Methods
def getMidnight() {
	def midnightToday = timeToday("2000-01-01T23:59:59.999-0000", location.timeZone)
}

def midNightReset() {
	state.A_triggered = false
    state.B_triggered = false
    state.C_triggered = false
    state.D_triggered = false
    state.E_triggered = false
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

private compileMsg(msg, scenario, friendlyName, friendlyLoc) {
   
    msg = msg.replace('%name%',"${friendlyName}")
    msg = msg.replace('%location%',"${friendlyLoc}")
   
	log.debug "msg = ${msg}"
	if (scenario == 1) {state.A_fullMsg = state.A_fullMsg + "${msg}. "}
	if (scenario == 2) {state.B_fullMsg = state.B_fullMsg + "${msg}. "}
	if (scenario == 3) {state.C_fullMsg = state.C_fullMsg + "${msg}. "}
	if (scenario == 4) {state.D_fullMsg = state.D_fullMsg + "${msg}. "}
    if (scenario == 5) {state.E_fullMsg = state.E_fullMsg + "${msg}. "}
}

//Preferences methods

def getTitle(name, num){
	def title = name ? name : "Alert ${num}"
}

def getDesc(name, num){
	def desc = name ? "Tap to edit alart scenario" : "Tap to setup alart scenario"
}

def greyedOutState(param1, param2, param3, param4){
	def result = param1 || param2 || param3 || param4 ? "complete" : ""
}

def greyedOutTime(start, end){
	def result = start || end ? "complete" : ""
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

// Decision making methods

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

private getDayOk(dayList) {
	def result = true
	if (dayList) {
		result = dayList.contains(getDay())
	}
	log.trace "DayOk = $result"
    result
}

//Version/Copyright/Information/Help

private def textAppName() {
	def text = "Announce Presence"
}	

private def textVersion() {
    def text = "Version 1.0.0 (06/09/2015)"
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
    	"Using presense sensors and Sonos speakers, you can set up 5 different scenarios to announce when someone enters or leaves an area. If used with Life360, you can also "+
        "alert on movements outside of the normal range of the SmartThings Hub location. In each scenario, define a single or group of sensors and a 'voice-friendly' name for them. "+
        "You can then chose a custom greeting (using the %name% and %location% variables for the 'voice-friendly' names) to speak when an individual or group of sensors approaches or leaves a location."
}
