/**
 *  Talking Alarm Clock
 *
 *  Version - 1.0 5/23/15
 *	Version - 1.1 5/24/15 - A song can now be selected to play after the voice greeting and bug fixes
 *  
 * 
 *  Copyright 2015 Michael Struck - Uses code from Lighting Director by Tim Slagle & Michael Struck
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *	The original licensing applies, with the following exceptions:
 *		1.	These modifications may NOT be used without freely distributing all these modifications freely
 *			and without limitation, in source form.	 The distribution may be met with a link to source code
 *			with these modifications.
 *		2.	These modifications may NOT be used, directly or indirectly, for the purpose of any type of
 *			monetary gain.	These modifications may not be used in a larger entity which is being sold,
 *			leased, or anything other than freely given.
 *		3.	To clarify 1 and 2 above, if you use these modifications, it must be a free project, and
 *			available to anyone with "no strings attached."	 (You may require a free registration on
 *			a free website or portal in order to distribute the modifications.)
 *		4.	The above listed exceptions to the original licensing do not apply to the holder of the
 *			copyright of the original work.	 The original copyright holder can use the modifications
 *			to hopefully improve their original work.  In that event, this author transfers all claim
 *			and ownership of the modifications to "SmartThings."
 *
 *
 *	Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *	in compliance with the License. You may obtain a copy of the License at:
 *
 *		http://www.apache.org/licenses/LICENSE-2.0
 *
 *	Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *	on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *	for the specific language governing permissions and limitations under the License.
 *
 */
 
definition(
	name: "Talking Alarm Clock",
	namespace: "MichaelStruck",
	author: "Michael Struck",
	description: "Control up to 4 waking schedules using a Sonos speaker as an alarm.",
	category: "Convenience",
	iconUrl: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Talking-Alarm-Clock/Talkingclock.png",
	iconX2Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Talking-Alarm-Clock/Talkingclock@2x.png",
	iconX3Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Talking-Alarm-Clock/Talkingclock@2x.png"
    )

preferences {
	page name:"pageMain"
	page name:"pageSetupScenarioA"
	page name:"pageSetupScenarioB"
	page name:"pageSetupScenarioC"
	page name:"pageSetupScenarioD"
}

// Show setup page
def pageMain() {
	dynamicPage(name: "pageMain", install: true, uninstall: true) {
        section ("Alarms") {
            href "pageSetupScenarioA", title: getTitle(ScenarioNameA, 1), description: getDesc(A_timeStart, A_sonos, A_day, A_mode), state: greyOut(ScenarioNameA, A_sonos, A_timeStart, A_alarmOn)
            input "A_alarmOn", "bool", title: "Enable this alarm?", defaultValue: "true", refreshAfterSelection:true
        }
        section {
            href "pageSetupScenarioB", title: getTitle(ScenarioNameB, 2), description: getDesc(B_timeStart, B_sonos, B_day, B_mode), state: greyOut(ScenarioNameB, B_sonos, B_timeStart, B_alarmOn)
            input "B_alarmOn", "bool", title: "Enable this alarm?", defaultValue: "false", refreshAfterSelection:true
        }
        section {
            href "pageSetupScenarioC", title: getTitle(ScenarioNameC, 3), description: getDesc(C_timeStart, C_sonos, C_day, C_mode), state: greyOut(ScenarioNameC, C_sonos, C_timeStart, C_alarmOn)
            input "C_alarmOn", "bool", title: "Enable this alarm?", defaultValue: "false", refreshAfterSelection:true
        }
        section {
            href "pageSetupScenarioD", title: getTitle(ScenarioNameD, 4), description: getDesc(D_timeStart, D_sonos, D_day, D_mode), state: greyOut(ScenarioNameD, D_sonos, D_timeStart, D_alarmOn)
            input "D_alarmOn", "bool", title: "Enable this alarm?", defaultValue: "false", refreshAfterSelection:true
        }
        
        section([title:"Options", mobileOnly:true]) {
            input "zipCode", "text", title: "Zip Code", required: false
            label title:"Assign a name", required:false
        }
    }
}

// Show "pageSetupScenarioA" page
def pageSetupScenarioA() {
    dynamicPage(name: "pageSetupScenarioA") {
		section("Alarm settings") {
        	input "ScenarioNameA", "text", title: "Scenario Name", multiple: false, required: false
			input "A_sonos", "capability.musicPlayer", title: "Choose a Sonos speaker", required: true, refreshAfterSelection:true
        	input "A_timeStart", "time", title: "Time to trigger alarm", required: false
        	input "A_day", "enum", options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"], title: "Alarm on certain days of the week...", multiple: true, required: false
    		input "A_mode", "mode", title: "Alarm only during the following modes...", multiple: true, required: false
    	}
        
        section("Devices to control in this alarm scenario") {
        	input "A_switches", "capability.switch",title: "Control the following switches...", multiple: true, required: false
        	input "A_dimmers", "capability.switchLevel", title: "Dim the following...", multiple: true, required: false
			input "A_level", "enum", options: [[10:"10%"],[20:"20%"],[30:"30%"],[40:"40%"],[50:"50%"],[60:"60%"],[70:"70%"],[80:"80%"],[90:"90%"],[100:"100%"]],title: "Set dimmers to this level", multiple: false, required: false
        	input "A_thermostats", "capability.thermostat", title: "Thermostats?", multiple: true, required: false
            input "A_temperatureH", "number", title: "Thermostat heating setpoint", required: false, description: "Temperature when in heat mode"
		input "A_temperatureC", "number", title: "Thermostat cooling setpoint", required: false, description: "Temperature when in cool mode"
        }
        
        section("Other Options") {
        	input "A_volume", "number", title: "Set the alarm volume", description: "0-100%", required: false
        	input "A_wakeMsg", "text", title: "Wake voice message", defaultValue: "Good morning! Today is %day%, %date%. It is currently %time%."
			input "A_weatherReport", "bool", title: "Speak today's weather report", defaultValue: "false"
            input "A_musicTrack", "enum", title: "Play this track after voice message", required:false, multiple: false, options: songOptions(A_sonos,1)
			def phrases = location.helloHome?.getPhrases()*.label
			if (phrases) {
				phrases.sort()
				input "A_phrase", "enum", title: "Alarm triggers the following phrase", required: false, options: phrases, multiple: false
			}
        }
    	section("Help") {
    		paragraph helpText()
    	}  
    } 
}

// Show "pageSetupScenarioB" page
def pageSetupScenarioB() {
    dynamicPage(name: "pageSetupScenarioB") {
		section("Alarm settings") {
        	input "ScenarioNameB", "text", title: "Scenario Name", multiple: false, required: true
			input "B_sonos", "capability.musicPlayer", title: "Choose a Sonos speaker", required: true, refreshAfterSelection:true
        	input "B_timeStart", "time", title: "Time to trigger alarm", required: true
        	input "B_day", "enum", options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"], title: "Alarm on certain days of the week...", multiple: true, required: false
    		input "B_mode", "mode", title: "Alarm only during the following modes...", multiple: true, required: false
    	}
        
        section("Devices to control in this alarm scenario") {
        	input "B_switches", "capability.switch",title: "Control the following switches...", multiple: true, required: false
        	input "B_dimmers", "capability.switchLevel", title: "Dim the following...", multiple: true, required: false
			input "B_level", "enum", options: [[10:"10%"],[20:"20%"],[30:"30%"],[40:"40%"],[50:"50%"],[60:"60%"],[70:"70%"],[80:"80%"],[90:"90%"],[100:"100%"]],title: "Set dimmers to this level", multiple: false, required: false
        	input "B_thermostats", "capability.thermostat", title: "Thermostats?", multiple: true, required: false
			input "B_temperatureH", "number", title: "Thermostat heating setpoint", required: false, description: "Temperature when in heat mode"
			input "B_temperatureC", "number", title: "Thermostat cooling setpoint", required: false, description: "Temperature when in cool mode"
        }
        
        section("Other Options") {
        	input "B_volume", "number", title: "Set the alarm volume", description: "0-100%", required: false
        	input "B_wakeMsg", "text", title: "Wake voice message", defaultValue: "Good morning! Today is %day%, %date%. It is currently %time%."
			input "B_weatherReport", "bool", title: "Speak today's weather report", defaultValue: "false"
            input "B_musicTrack", "enum", title: "Play this track after voice message", required:false, multiple: false, options: songOptions(B_sonos,1)
			def phrases = location.helloHome?.getPhrases()*.label
			if (phrases) {
        		phrases.sort()
				input "B_phrase", "enum", title: "Alarm triggers the following phrase", required: false, options: phrases, multiple: false
			}
        }
    	section("Help") {
    		paragraph helpText()
    	}  
    } 
}

// Show "pageSetupScenarioC" page
def pageSetupScenarioC() {
    dynamicPage(name: "pageSetupScenarioC") {
		section("Alarm settings") {
        	input "ScenarioNameC", "text", title: "Scenario Name", multiple: false, required: true
			input "C_sonos", "capability.musicPlayer", title: "Choose a Sonos speaker", required: true, refreshAfterSelection:true
        	input "C_timeStart", "time", title: "Time to trigger alarm", required: true
        	input "C_day", "enum", options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"], title: "Alarm on certain days of the week...", multiple: true, required: false
    		input "C_mode", "mode", title: "Alarm only during the following modes...", multiple: true, required: false
    	}
        
        section("Devices to control in this alarm scenario") {
        	input "C_switches", "capability.switch",title: "Control the following switches...", multiple: true, required: false
        	input "C_dimmers", "capability.switchLevel", title: "Dim the following...", multiple: true, required: false
			input "C_level", "enum", options: [[10:"10%"],[20:"20%"],[30:"30%"],[40:"40%"],[50:"50%"],[60:"60%"],[70:"70%"],[80:"80%"],[90:"90%"],[100:"100%"]],title: "Set dimmers to this level", multiple: false, required: false
        	input "C_thermostats", "capability.thermostat", title: "Thermostats?", multiple: true, required: false
        	input "C_temperatureH", "number", title: "Thermostat heating setpoint", required: false, description: "Temperature when in heat mode"
			input "C_temperatureC", "number", title: "Thermostat cooling setpoint", required: false, description: "Temperature when in cool mode"
        }
        
        section("Other Options") {
        	input "C_volume", "number", title: "Set the alarm volume", description: "0-100%", required: false
        	input "C_wakeMsg", "text", title: "Wake voice message", defaultValue: "Good morning! Today is %day%, %date%. It is currently %time%."
			input "C_weatherReport", "bool", title: "Speak today's weather report", defaultValue: "false"
            input "C_musicTrack", "enum", title: "Play this track after voice message", required:false, multiple: false, options: songOptions(C_sonos,1)
			def phrases = location.helloHome?.getPhrases()*.label
			if (phrases) {
				phrases.sort()
				input "C_phrase", "enum", title: "Alarm triggers the following phrase", required: false, options: phrases, multiple: false
			}
        }
    	section("Help") {
    		paragraph helpText()
    	}  
    } 
}

// Show "pageSetupScenarioD" page
def pageSetupScenarioD() {
    dynamicPage(name: "pageSetupScenarioD") {
		section("Alarm settings") {
        	input "ScenarioNameD", "text", title: "Scenario Name", multiple: false, required: true
			input "D_sonos", "capability.musicPlayer", title: "Choose a Sonos speaker", required: true, refreshAfterSelection:true
        	input "D_timeStart", "time", title: "Time to trigger alarm", required: true
        	input "D_day", "enum", options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"], title: "Alarm on certain days of the week...", multiple: true, required: false
    		input "D_mode", "mode", title: "Alarm only during the following modes...", multiple: true, required: false
    	}
        
        section("Devices to control in this alarm scenario") {
        	input "D_switches", "capability.switch",title: "Control the following switches...", multiple: true, required: false
        	input "D_dimmers", "capability.switchLevel", title: "Dim the following...", multiple: true, required: false
			input "D_level", "enum", options: [[10:"10%"],[20:"20%"],[30:"30%"],[40:"40%"],[50:"50%"],[60:"60%"],[70:"70%"],[80:"80%"],[90:"90%"],[100:"100%"]],title: "Set dimmers to this level", multiple: false, required: false
        	input "D_thermostats", "capability.thermostat", title: "Thermostats?", multiple: true, required: false
			input "D_temperatureH", "number", title: "Thermostat heating setpoint", required: false, description: "Temperature when in heat mode"
			input "D_temperatureC", "number", title: "Thermostat cooling setpoint", required: false, description: "Temperature when in cool mode"
        }
        
        section("Other Options") {
        	input "D_volume", "number", title: "Set the alarm volume", description: "0-100%", required: false
        	input "D_wakeMsg", "text", title: "Wake voice message", defaultValue: "Good morning! Today is %day%, %date%. It is currently %time%."
			input "D_weatherReport", "bool", title: "Speak today's weather report", defaultValue: "false"
            input "D_musicTrack", "enum", title: "Play this track after voice message", required:false, multiple: false, options: songOptions(D_sonos,1)
            def phrases = location.helloHome?.getPhrases()*.label
			if (phrases) {
        		phrases.sort()
				input "D_phrase", "enum", title: "Alarm triggers the following phrase", required: false, options: phrases, multiple: false
			}
        }
    	section("Help") {
    		paragraph helpText()
    	}  
    } 
}

//--------------------------------------

def installed() {
    initialize()
}

def updated() {
    unschedule()
    initialize()
}

def initialize() {
	if (ScenarioNameA && A_timeStart && A_sonos && A_alarmOn){
		schedule (A_timeStart, alarm_A)
        if (A_musicTrack){
        	saveSelectedSong(A_sonos, A_musicTrack, 1)
        }
	}
    if (ScenarioNameB && B_timeStart && B_sonos &&B_alarmOn){
		schedule (B_timeStart, alarm_B)
        if (B_musicTrack){
        	saveSelectedSong(B_sonos, B_musicTrack, 2)
        }
	}
    if (ScenarioNameC && C_timeStart && C_sonos && C_alarmOn){
		schedule (C_timeStart, alarm_C)
        if (C_musicTrack){
        	saveSelectedSong(C_sonos, C_musicTrack, 3)
        }
	}
	if (ScenarioNameD && D_timeStart && D_sonos && D_alarmOn){
		schedule (D_timeStart, alarm_D)
        if (D_musicTrack){
        	saveSelectedSong(D_sonos, D_musicTrack, 4)
        }
	}    
}

//--------------------------------------

def alarm_A() {
	if ((!A_mode || A_mode.contains(location.mode)) && getDayOk(A_day)) {	
   		state.fullMsgA = ""
        if (A_wakeMsg) {
	       	getGreeting(A_wakeMsg, 1)
        }    
        if (A_weatherReport) {
          	getWeatherReport(1)
        }
      	if (A_switches || A_dimmers || A_thermostats) {
        	def dimLevel = A_level as Integer
            A_switches?.on()
    		A_dimmers?.setLevel(dimLevel)
            	if (A_thermostats) {
        		def thermostatState = A_thermostats.currentThermostatMode
				if (thermostatState == "auto") {
					A_thermostats.setHeatingSetpoint(A_temperatureH)
					A_thermostats.setCoolingSetpoint(A_temperatureC)
				}
            }
			else if (thermostatState == "heat") {
				A_thermostats.setHeatingSetpoint(A_temperatureH)
        		log.info "Set $A_thermostats Heat $A_temperatureH°"
			}
			else {
				A_thermostats.setCoolingSetpoint(A_temperatureC)
        		log.info "Set $A_thermostats Cool $A_temperatureC°"
            }
       		getOnConfimation(A_switches, A_dimmers, A_thermostats, 1)
       	}
        if (A_phrase) {
        	location.helloHome.execute(A_phrase)
        	getPhraseConfirmation(A_phrase)
        }
        
    	state.soundA = textToSpeech(state.fullMsgA, true)
    	if (A_volume) {
        	A_sonos.setLevel(A_volume)
		}
        if (A_musicTrack) {
        	A_sonos.playSoundAndTrack (state.soundA.uri, state.soundA.duration, state.selectedSongA)
        }
        else {
        	A_sonos.playTrack(state.soundA.uri)
        }
	}
}

def alarm_B() {
	if ((!B_mode || B_mode.contains(location.mode)) && getDayOk(B_day)) {	
        state.fullMsgB = ""
        if (B_wakeMsg) {
        	getGreeting(B_wakeMsg, 2)
        }    
        if (B_weatherReport) {
        	getWeatherReport(2)
        }
        if (B_switches || B_dimmers || B_thermostats) {
        	def dimLevel = B_level as Integer
            B_switches?.on()
    		B_dimmers?.setLevel(dimLevel)
        	if (B_thermostats) {
        		def thermostatState = B_thermostats.currentThermostatMode
				if (thermostatState == "auto") {
				B_thermostats.setHeatingSetpoint(B_temperatureH)
				B_thermostats.setCoolingSetpoint(B_temperatureC)
				}
            }
			else if (thermostatState == "heat") {
				B_thermostats.setHeatingSetpoint(B_temperatureH)
        		log.info "Set $B_thermostats Heat $B_temperatureH°"
			}
			else {
				B_thermostats.setCoolingSetpoint(B_temperatureC)
        		log.info "Set $B_thermostats Cool $B_temperatureC°"
            }
		   	getOnConfimation(B_switches, B_dimmers, B_thermostats, 2)
        }
        if (B_phrase) {
        	location.helloHome.execute(B_phrase)
        	getPhraseConfirmation(B_phrase)
        }
                
    	state.soundB = textToSpeech(state.fullMsgB, true)
    	if (B_volume) {
        	B_sonos.setLevel(B_volume)
		}
        if (B_musicTrack) {
        	B_sonos.playSoundAndTrack (state.soundB.uri, state.soundB.duration, state.selectedSongB)
        }
        else {
        	B_sonos.playTrack(state.soundB.uri)
        }
    }
}

def alarm_C() {
	if ((!C_mode || C_mode.contains(location.mode)) && getDayOk(C_day)) {	
  		state.fullMsgC = ""
		if (C_wakeMsg) {
			getGreeting(C_wakeMsg, 3)
        	}    
        	if (C_weatherReport) {
           		getWeatherReport(3)
        	}
       		if (C_switches || C_dimmers || C_thermostats) {
 		  	 	def dimLevel = C_level as Integer
                C_switches?.on()
    			C_dimmers?.setLevel(dimLevel)
            	if (C_thermostats) {
        			def thermostatState = C_thermostats.currentThermostatMode
					if (thermostatState == "auto") {
					C_thermostats.setHeatingSetpoint(C_temperatureH)
                    C_thermostats.setCoolingSetpoint(C_temperatureC)
				}
			}
			else if (thermostatState == "heat") {
				C_thermostats.setHeatingSetpoint(C_temperatureH)
        		log.info "Set $C_thermostats Heat $C_temperatureH°"
			}
			else {
				C_thermostats.setCoolingSetpoint(C_temperatureC)
        		log.info "Set $C_thermostats Cool $C_temperatureC°"
            }
            getOnConfimation(C_switches, C_dimmers, C_thermostats, 3)
       	}
        if (C_phrase) {
           	location.helloHome.execute(C_phrase)
        	getPhraseConfirmation(C_phrase)
        }
        
    	state.soundC = textToSpeech(state.fullMsgC, true)
    	if (C_volume) {
        	C_sonos.setLevel(C_volume)
		}
        if (C_musicTrack) {
        	C_sonos.playSoundAndTrack (state.soundC.uri, state.soundC.duration, state.selectedSongC)
        }
        else {
        	C_sonos.playTrack(state.soundC.uri)
        }
    }
}

def alarm_D() {
	if ((!D_mode || D_mode.contains(location.mode)) && getDayOk(D_day)) {	
	   	state.fullMsgD = ""
       	if (D_wakeMsg) {
    		getGreeting(D_wakeMsg, 4)
       	}    
        if (D_weatherReport) {
        	getWeatherReport(4)
        }
       	if (D_switches || D_dimmers || D_thermostats) {
      		def dimLevel = D_level as Integer
        	D_switches?.on()
    		D_dimmers?.setLevel(dimLevel)
            if (D_thermostats) {
        		def thermostatState = D_thermostats.currentThermostatMode
				if (thermostatState == "auto") {
					D_thermostats.setHeatingSetpoint(D_temperatureH)
					D_thermostats.setCoolingSetpoint(D_temperatureC)
				}
            }
			else if (thermostatState == "heat") {
				D_thermostats.setHeatingSetpoint(D_temperatureH)
        		log.info "Set $D_thermostats Heat $D_temperatureH°"
				}
			else {
				D_thermostats.setCoolingSetpoint(D_temperatureC)
        		log.info "Set $D_thermostats Cool $D_temperatureC°"
            }
            getOnConfimation(D_switches, D_dimmers, D_thermostats, 4)
       	}
        if (D_phrase) {
        	location.helloHome.execute(D_phrase)
        	getPhraseConfirmation(D_phrase)
        }
        
    	state.soundD = textToSpeech(state.fullMsgD, true)
    	if (D_volume) {
        	D_sonos.setLevel(D_volume)
		}
        if (D_musicTrack) {
        	D_sonos.playSoundAndTrack (state.soundD.uri, state.soundD.duration, state.selectedSongD)
        }
        else {
        	D_sonos.playTrack(state.soundD.uri)
        }
    }
}

//--------------------------------------

def getDesc(timeStart, sonos, day, mode) {
	def desc = "Tap to set alarm"
   
	if (timeStart) {
    	desc = "Alarm set to " + hhmm(timeStart) +" on ${sonos}\n"
		
        def dayListSize = day ? day.size() : 7
             
        if (day && dayListSize < 7) {
        	desc = desc + "On "
            for (dayName in day) {
 				desc = desc + "${dayName}"
    			dayListSize = dayListSize -1
                if (dayListSize) {
            		desc = "${desc}, "
        		}
        	}
        }
        else {
    		desc = desc + "Every day"
    	}
    	
        if (mode) {
    		def modeListSize = mode.size()
        	def modePrefix ="\nIn the following modes: "
        	if (modeListSize == 1) {
        		modePrefix = "\nIn the following mode: "
        	}
            desc = desc + "${modePrefix}" 
       		for (modeName in mode) {
        		desc = desc + "'${modeName}'"
    			modeListSize = modeListSize -1
            	if (modeListSize) {
            		desc = "${desc}, "
            	}
            	else {
            		desc = "${desc}"
        		}
        	}
		}
     	else {
    		desc = desc + "\nIn all modes"
        }
    }
	desc	
}

private def helpText() {
	def text =
    	"Choose a Sonos speaker along with an alarm time and switches, dimmers " +
        "and thermostats to control when the alarm is triggered. You can also " +
        "choose to have a weather report spoken as part of the alarm and a song " +
        "to play after the voice greetings."
	text
}

def greyOut(scenario, sonos, alarmTime, alarmOn){
	def result = scenario && sonos  && alarmTime && alarmOn ? "complete" : ""
	result
}

def getTitle(scenario, num) {
	def title = scenario ? scenario : "Alarm ${num} not configured"
    title
}

private getDayOk(dayList) {
	def result = true
    	if (dayList) {
		result = dayList.contains(getDay())
		}
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
    	day
}

private hhmm(time) {
	new Date().parse("yyyy-MM-dd'T'HH:mm:ss.SSSZ", time).format("h:mm a", timeZone(time))
}
public convertEpoch(epochDate) {
	new Date(epochDate).format("yyyy-MM-dd'T'HH:mm:ss.SSSZ", location.timeZone)
}
private getMonth(date) {
	new Date().parse("yyyy-MM-dd'T'HH:mm:ss.SSSZ", date).format("MMMM", timeZone(date))
}
private getYear(date) {
	new Date().parse("yyyy-MM-dd'T'HH:mm:ss.SSSZ", date).format("yyyy", timeZone(date))
}
private getDayNum(date) {
	new Date().parse("yyyy-MM-dd'T'HH:mm:ss.SSSZ", date).format("dd", timeZone(date))
}
private getGreeting(msg, scenario) {
	def day = getDay()
    def time = hhmm(convertEpoch(now()))
    def month = getMonth(convertEpoch(now()))
    def year = getYear(convertEpoch(now()))
    def dayNum = getDayNum(convertEpoch(now()))
	msg = msg.replace('%day%', day)
    msg = msg.replace('%date%', "${month} ${dayNum}, ${year}")
    msg = msg.replace('%time%', "${time}")
    log.debug "msg = ${msg}"
    if (scenario == 1) {state.fullMsgA = "${msg} "}
    if (scenario == 2) {state.fullMsgB = "${msg} "}
    if (scenario == 3) {state.fullMsgC = "${msg} "}
    if (scenario == 4) {state.fullMsgD = "${msg} "}
}

private getWeatherReport(scenario) {
	if (location.timeZone || zipCode) {
		def weather = getWeatherFeature("forecast", zipCode)
		def current = getWeatherFeature("conditions", zipCode)
		def isMetric = location.temperatureScale == "C"
        def sb = new StringBuilder()
		if (isMetric) {
        	sb << "The current temperature is ${Math.round(current.current_observation.temp_c)} degrees. "
        }
        else {
           	sb << "The current temperature is ${Math.round(current.current_observation.temp_f)} degrees. "
        }
		sb << "Today's forecast is "
		if (isMetric) {
        	sb << weather.forecast.txt_forecast.forecastday[0].fcttext_metric 
        }
        else {
          	sb << weather.forecast.txt_forecast.forecastday[0].fcttext
        }
		def msg = sb.toString()
        msg = msg.replaceAll(/([0-9]+)C/,'$1 degrees')
        log.debug "msg = ${msg}"
        if (scenario == 1) {state.fullMsgA = state.fullMsgA + "${msg} "}
    	if (scenario == 2) {state.fullMsgB = state.fullMsgB + "${msg} "}
    	if (scenario == 3) {state.fullMsgC = state.fullMsgC + "${msg} "}
    	if (scenario == 4) {state.fullMsgD = state.fullMsgD + "${msg} "}
		
	}
	else {
		msg = "Please set the location of your hub with the SmartThings mobile app, or enter a zip code to receive weather forecasts."
		log.debug "msg = ${msg}"
        if (scenario == 1) {state.fullMsgA = state.fullMsgA + "${msg} "}
    	if (scenario == 2) {state.fullMsgB = state.fullMsgB + "${msg} "}
    	if (scenario == 3) {state.fullMsgC = state.fullMsgC + "${msg} "}
    	if (scenario == 4) {state.fullMsgD = state.fullMsgD + "${msg} "}
    }
}

private getOnConfimation(switches, dimmers, thermostats, scenario) {
	def msg = ""
    if ((switches || dimmers) && !thermostats) {
    	msg = "All switches"	
    }
    if (!switches && !dimmers && thermostats) {
    	msg = "Thermostats"
    }
    if ((switches || dimmers) && thermostats) {
    	msg = "All switches and thermostats"
    } 
    msg = "${msg} are now on and set. "
    log.debug "msg = ${msg}"
    if (scenario == 1) {state.fullMsgA = state.fullMsgA + "${msg} "}
    if (scenario == 2) {state.fullMsgB = state.fullMsgB + "${msg} "}
    if (scenario == 3) {state.fullMsgC = state.fullMsgC + "${msg} "}
    if (scenario == 4) {state.fullMsgD = state.fullMsgD + "${msg} "}
}

private getPhraseConfirmation(phrase) {
	def msg="The Smart Things Hello Home phrase, ${phrase}, has been activated."
	log.debug "msg = ${msg}"
	state.fullMsg = state.fullMsg + "${msg} "
}

//Sonos Aquire Track from SmartThings code
private songOptions(sonos, scenario) {
if (sonos){
	// Make sure current selection is in the set

	def options = new LinkedHashSet()
	if (scenario == 1){
    	if (state.selectedSongA?.station) {
			options << state.selectedSongA.station
		}
		else if (state.selectedSongA?.description) {
			options << state.selectedSongA.description
		}
    }
    if (scenario == 2){
    	if (state.selectedSongB?.station) {
			options << state.selectedSongB.station
		}
		else if (state.selectedSongB?.description) {
			options << state.selectedSongB.description
		}
    }
    if (scenario == 3){
    	if (state.selectedSongC?.station) {
			options << state.selectedSongC.station
		}
		else if (state.selectedSongC?.description) {
			options << state.selectedSongC.description
		}
    }
    if (scenario == 4){
    	if (state.selectedSongD?.station) {
			options << state.selectedSongD.station
		}
		else if (state.selectedSongD?.description) {
			options << state.selectedSongD.description
		}
    }
	// Query for recent tracks
	def states = sonos.statesSince("trackData", new Date(0), [max:30])
	def dataMaps = states.collect{it.jsonValue}
	options.addAll(dataMaps.collect{it.station})

	log.trace "${options.size()} songs in list"
	options.take(20) as List
}
}

private saveSelectedSong(sonos, song, scenario) {
	try {
		def thisSong = song
		log.info "Looking for $thisSong"
		def songs = sonos.statesSince("trackData", new Date(0), [max:30]).collect{it.jsonValue}
		log.info "Searching ${songs.size()} records"

		def data = songs.find {s -> s.station == thisSong}
		log.info "Found ${data?.station}"
		if (data) {
			if (scenario == 1) {state.selectedSongA = data}
            if (scenario == 2) {state.selectedSongB = data}
            if (scenario == 3) {state.selectedSongC = data}
            if (scenario == 4) {state.selectedSongD = data}
			log.debug "Selected song for Scenario ${scenario} = ${data}"
		}
		else if (song == state.selectedSongA?.station) {
			log.debug "Selected existing entry '$song', which is no longer in the last 20 list"
		}
        else if (song == state.selectedSongB?.station) {
			log.debug "Selected existing entry '$song', which is no longer in the last 20 list"
		}
        else if (song == state.selectedSongC?.station) {
			log.debug "Selected existing entry '$song', which is no longer in the last 20 list"
		}
        else if (song == state.selectedSongD?.station) {
			log.debug "Selected existing entry '$song', which is no longer in the last 20 list"
		}
		else {
			log.warn "Selected song '$song' not found"
		}
	}
	catch (Throwable t) {
		log.error t
	}
}
