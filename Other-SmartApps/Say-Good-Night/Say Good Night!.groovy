/**
 *  Say Good Night!
 *
 *  Version - 1.0.0 6/1/15
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

import groovy.json.JsonSlurper

definition(
    name: "Say Good Night!",
    namespace: "MichaelStruck",
    author: "Michael Struck",
    description: "Control up to 4 'Good Night' scenarios using Sonos speakers and various triggers.",
    category: "Convenience",
    iconUrl: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Say-Good-Night/SayGoodNight.png",
    iconX2Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Say-Good-Night/SayGoodNight@2x.png",
    iconX3Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Say-Good-Night/SayGoodNight@2x.png"
    )

preferences {
	page name: "pageMain"
	page name: "pageSetupScenarioA"
    page name: "pageSetupScenarioB"
    page name: "pageSetupScenarioC"
    page name: "pageSetupScenarioD"
}

// Show setup page
def pageMain() {
	dynamicPage(name: "pageMain", install: true, uninstall: true) {
        section ("Good Night Scenarios") {
            href "pageSetupScenarioA", title: getTitle(ScenarioNameA, 1), description: getDesc(A_sonos), state: greyOut(ScenarioNameA, A_sonos)
        }
        section{
            href "pageSetupScenarioB", title: getTitle(ScenarioNameB, 2), description: getDesc(B_sonos), state: greyOut(ScenarioNameB, B_sonos)
        }
		section{
            href "pageSetupScenarioC", title: getTitle(ScenarioNameC, 3), description: getDesc(C_sonos), state: greyOut(ScenarioNameC, C_sonos)
        }
        section{
            href "pageSetupScenarioD", title: getTitle(ScenarioNameD, 4), description: getDesc(D_sonos), state: greyOut(ScenarioNameD, D_sonos)
        }
 		section([title:"Options", mobileOnly:true]) {
            input "zipCode", "text", title: "Zip Code", required: false
            label title:"Assign a name", required:false
            href "pageAbout", title: "About ${textAppName()}", description: "Tap to get application version,  license and instructions"
        }
    }
}

// Show "pageSetupScenarioA" page
def pageSetupScenarioA() {
    dynamicPage(name: "pageSetupScenarioA") {
		section("Good Night Scenarios") {
        	input "ScenarioNameA", "text", title: "Scenario Name", multiple: false, required: false
			input "A_sonos", "capability.musicPlayer", title: "Choose a Sonos speaker", required: true
    	}
		section("Say 'Good Night' when...") {
			input "A_switches", "capability.switch",title: "Any of these switches are turned off...", multiple: true, required: false
            href "pageButtonControlA", title: "A button is pressed on a controller...", description: buttonDesc(A_buttonDevice, A_buttonPress), state: greyOut(A_buttonDevice, A_buttonPress)
        }
        section("Other Options") {
        	input "A_volume", "number", title: "Set the alarm volume", description: "0-100%", required: false
        	href "pageWeatherSettingsA", title: "Weather Reporting Settings", description: getWeatherTitle(A_weatherReport, A_includeSunrise, A_includeSunset), state: greyOut1(A_weatherReport, A_includeSunrise, A_includeSunset)
            def phrases = location.helloHome?.getPhrases()*.label
            if (phrases) {
				phrases.sort()
				input "A_phrase", "enum", title: "Trigger the following phrase", required: false, options: phrases, multiple: false, refreshAfterSelection:true
				if (A_phrase) {
                	input "A_confirmPhrase", "bool", title: "Confirm Hello, Home phrase in voice message", defaultValue: "false"
                }
            }
            input "A_triggerMode", "mode", title: "Trigger the following mode", required: false, refreshAfterSelection:true
            if (A_triggerMode){
            	input "A_confirmMode", "bool", title: "Confirm mode in voice message", defaultValue: "false"
            }
            input "A_msg", "text", title: "Good night message", defaultValue: "Good Night!", required: false
		}
    	section("Restrictions") {            
        	input name: "A_triggerOnce",type: "bool",title: "Trigger only once per day...", defaultValue: false
        	href "timeIntervalInputA", title: "Only during a certain time...", description: getTimeLabel(A_timeStart, A_timeEnd), state: greyedOutTime(A_timeStart, A_timeEnd)
        	input name:  "A_day", type: "enum", options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"], title: "Only on certain days of the week...",  multiple: true, required: false
        	input "A_mode", "mode", title: "Only during certain modes...", multiple: true, required: false
        }
    } 
}

page(name: "pageButtonControlA", title: "Button Controller Setup") {
	section {
    	input "A_buttonDevice", "capability.button", title: "Button Controller", multiple: false, required: false
        input "A_buttonPress", "enum", title: "Which button...", options: [1:"1", 2:"2", 3:"3", 4:"4"], multiple: false, required: false
	}
}

page(name: "timeIntervalInputA", title: "Only during a certain time") {
	section {
		input "A_timeStart", "time", title: "Starting", required: false
		input "A_timeEnd", "time", title: "Ending", required: false
	}
}

page(name: "pageWeatherSettingsA", title: "Weather Reporting Settings") {
	section {
        input "A_includeTemp", "bool", title: "Speak current temperature (from local forecast)", defaultValue: "false"
        input "A_humidity", "capability.relativeHumidityMeasurement", title: "Speak local humidity (from device)", required: false, multiple: false
        input "A_weatherReport", "bool", title: "Speak tomorrow's weather forecast", defaultValue: "false"
        input "A_includeSunrise", "bool", title: "Speak tomorrow's sunrise", defaultValue: "false"
    	input "A_includeSunset", "bool", title: "Speak tomorrow's sunset", defaultValue: "false"
	}
}

// Show "pageSetupScenarioB" page
def pageSetupScenarioB() {
    dynamicPage(name: "pageSetupScenarioB") {
		section("Good Night Scenarios") {
        	input "ScenarioNameB", "text", title: "Scenario Name", multiple: false, required: false
			input "B_sonos", "capability.musicPlayer", title: "Choose a Sonos speaker", required: true
    	}
		section("Say 'Good Night' when...") {
			input "B_switches", "capability.switch",title: "Any of these switches are turned off...", multiple: true, required: false
            href "pageButtonControlB", title: "A button is pressed on a controller...", description: buttonDesc(B_buttonDevice, B_buttonPress), state: greyOut(B_buttonDevice, B_buttonPress)
        }
        section("Other Options") {
        	input "B_volume", "number", title: "Set the alarm volume", description: "0-100%", required: false
        	href "pageWeatherSettingsB", title: "Weather Reporting Settings", description: getWeatherTitle(B_weatherReport, B_includeSunrise, B_includeSunset), state: greyOut1(B_weatherReport, B_includeSunrise, B_includeSunset)
            def phrases = location.helloHome?.getPhrases()*.label
            if (phrases) {
				phrases.sort()
				input "B_phrase", "enum", title: "Trigger the following phrase", required: false, options: phrases, multiple: false, refreshAfterSelection:true
				if (B_phrase){
                	input "B_confirmPhrase", "bool", title: "Confirm Hello, Home phrase in voice message", defaultValue: "false"
                }
            }
            input "B_triggerMode", "mode", title: "Trigger the following mode", required: false, refreshAfterSelection:true
            if (B_triggerMode){
            	input "B_confirmMode", "bool", title: "Confirm mode in voice message", defaultValue: "false"
            }
            input "B_msg", "text", title: "Good night message", defaultValue: "Good Night!", required: false
		}
    	section("Restrictions") {            
        	input name: "B_triggerOnce",type: "bool",title: "Trigger only once per day...", defaultValue: false
        	href "timeIntervalInputB", title: "Only during a certain time...", description: getTimeLabel(B_timeStart, B_timeEnd), state: greyedOutTime(B_timeStart, B_timeEnd)
        	input name:  "B_day", type: "enum", options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"], title: "Only on certain days of the week...",  multiple: true, required: false
        	input "B_mode", "mode", title: "Only during certain modes...", multiple: true, required: false
        }
    } 
}

page(name: "pageButtonControlB", title: "Button Controller Setup") {
	section {
    	input "B_buttonDevice", "capability.button", title: "Button Controller", multiple: false, required: false
        input "B_buttonPress", "enum", title: "Which button...", options: [1:"1", 2:"2", 3:"3", 4:"4"], multiple: false, required: false
	}
}

page(name: "timeIntervalInputB", title: "Only during a certain time") {
	section {
		input "B_timeStart", "time", title: "Starting", required: false
		input "B_timeEnd", "time", title: "Ending", required: false
	}
}

page(name: "pageWeatherSettingsB", title: "Weather Reporting Settings") {
	section {
		input "B_includeTemp", "bool", title: "Speak current temperature (from local forecast)", defaultValue: "false"
        input "B_humidity", "capability.relativeHumidityMeasurement", title: "Speak local humidity (from device)", required: false, multiple: false
        input "B_weatherReport", "bool", title: "Speak tomorrow's weather forecast", defaultValue: "false"
        input "B_includeSunrise", "bool", title: "Speak tomorrow's sunrise", defaultValue: "false"
    	input "B_includeSunset", "bool", title: "Speak tomorrow's sunset", defaultValue: "false"
	}
}

// Show "pageSetupScenarioC" page
def pageSetupScenarioC() {
    dynamicPage(name: "pageSetupScenarioC") {
		section("Good Night Scenarios") {
        	input "ScenarioNameC", "text", title: "Scenario Name", multiple: false, required: false
			input "C_sonos", "capability.musicPlayer", title: "Choose a Sonos speaker", required: true
    	}
		section("Say 'Good Night' when...") {
			input "C_switches", "capability.switch",title: "Any of these switches are turned off...", multiple: true, required: false
            href "pageButtonControlC", title: "A button is pressed on a controller...", description: buttonDesc(C_buttonDevice, C_buttonPress), state: greyOut(C_buttonDevice, C_buttonPress)
        }
        section("Other Options") {
        	input "C_volume", "number", title: "Set the alarm volume", description: "0-100%", required: false
        	href "pageWeatherSettingsC", title: "Weather Reporting Settings", description: getWeatherTitle(C_weatherReport, C_includeSunrise, C_includeSunset), state: greyOut1(C_weatherReport, C_includeSunrise, C_includeSunset)
            def phrases = location.helloHome?.getPhrases()*.label
            if (phrases) {
				phrases.sort()
				input "C_phrase", "enum", title: "Trigger the following phrase", required: false, options: phrases, multiple: false, refreshAfterSelection:true
				if (C_phrase) {
                	input "C_confirmPhrase", "bool", title: "Confirm Hello, Home phrase in voice message", defaultValue: "false"
                }
            }
            input "C_triggerMode", "mode", title: "Trigger the following mode", required: false, refreshAfterSelection:true
            if (C_triggerMode){
            	input "C_confirmMode", "bool", title: "Confirm mode in voice message", defaultValue: "false"
            }
            input "C_msg", "text", title: "Good night message", defaultValue: "Good Night!", required: false
		}
    	section("Restrictions") {            
        	input name: "C_triggerOnce",type: "bool",title: "Trigger only once per day...", defaultValue: false
        	href "timeIntervalInputC", title: "Only during a certain time...", description: getTimeLabel(C_timeStart, C_timeEnd), state: greyedOutTime(C_timeStart, C_timeEnd)
        	input name:  "C_day", type: "enum", options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"], title: "Only on certain days of the week...",  multiple: true, required: false
        	input "C_mode", "mode", title: "Only during certain modes...", multiple: true, required: false
        }
    } 
}

page(name: "pageButtonControlC", title: "Button Controller Setup") {
	section {
    	input "C_buttonDevice", "capability.button", title: "Button Controller", multiple: false, required: false
        input "C_buttonPress", "enum", title: "Which button...", options: [1:"1", 2:"2", 3:"3", 4:"4"], multiple: false, required: false
	}
}

page(name: "timeIntervalInputC", title: "Only during a certain time") {
	section {
		input "C_timeStart", "time", title: "Starting", required: false
		input "C_timeEnd", "time", title: "Ending", required: false
	}
}

page(name: "pageWeatherSettingsC", title: "Weather Reporting Settings") {
	section {
		input "C_includeTemp", "bool", title: "Speak current temperature (from local forecast)", defaultValue: "false"
        input "C_humidity", "capability.relativeHumidityMeasurement", title: "Speak local humidity (from device)", required: false, multiple: false
        input "C_weatherReport", "bool", title: "Speak tomorrow's weather forecast", defaultValue: "false"
        input "C_includeSunrise", "bool", title: "Speak tomorrow's sunrise", defaultValue: "false"
    	input "C_includeSunset", "bool", title: "Speak tomorrow's sunset", defaultValue: "false"
	}
}

// Show "pageSetupScenarioD" page
def pageSetupScenarioD() {
    dynamicPage(name: "pageSetupScenarioD") {
		section("Good Night Scenarios") {
        	input "ScenarioNameD", "text", title: "Scenario Name", multiple: false, required: false
			input "D_sonos", "capability.musicPlayer", title: "Choose a Sonos speaker", required: true
    	}
		section("Say 'Good Night' when...") {
			input "D_switches", "capability.switch",title: "Any of these switches are turned off...", multiple: true, required: false
            href "pageButtonControlD", title: "A button is pressed on a controller...", description: buttonDesc(D_buttonDevice, D_buttonPress), state: greyOut(D_buttonDevice, D_buttonPress)
        }
        section("Other Options") {
        	input "D_volume", "number", title: "Set the alarm volume", description: "0-100%", required: false
        	href "pageWeatherSettingsD", title: "Weather Reporting Settings", description: getWeatherTitle(D_weatherReport, D_includeSunrise, D_includeSunset), state: greyOut1(D_weatherReport, D_includeSunrise, D_includeSunset)
            def phrases = location.helloHome?.getPhrases()*.label
            if (phrases) {
				phrases.sort()
				input "D_phrase", "enum", title: "Trigger the following phrase", required: false, options: phrases, multiple: false, refreshAfterSelection:true
				if (D_phrase){
                	input "D_confirmPhrase", "bool", title: "Confirm Hello, Home phrase in voice message", defaultValue: "false"
                }
            }
            input "D_triggerMode", "mode", title: "Trigger the following mode", required: false, refreshAfterSelection:true
            if (D_triggerMode){
            	input "D_confirmMode", "bool", title: "Confirm mode in voice message", defaultValue: "false"
            }
            input "D_msg", "text", title: "Good night message", defaultValue: "Good Night!", required: false
		}
    	section("Restrictions") {            
        	input name: "D_triggerOnce",type: "bool",title: "Trigger only once per day...", defaultValue: false
        	href "timeIntervalInputD", title: "Only during a certain time...", description: getTimeLabel(D_timeStart, D_timeEnd), state: greyedOutTime(D_timeStart, D_timeEnd)
        	input name:  "D_day", type: "enum", options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"], title: "Only on certain days of the week...",  multiple: true, required: false
        	input "D_mode", "mode", title: "Only during certain modes...", multiple: true, required: false
        }
    } 
}

page(name: "pageButtonControlD", title: "Button Controller Setup") {
	section {
    	input "D_buttonDevice", "capability.button", title: "Button Controller", multiple: false, required: false
        input "D_buttonPress", "enum", title: "Which button...", options: [1:"1", 2:"2", 3:"3", 4:"4"], multiple: false, required: false
	}
}

page(name: "timeIntervalInputD", title: "Only during a certain time") {
	section {
		input "D_timeStart", "time", title: "Starting", required: false
		input "D_timeEnd", "time", title: "Ending", required: false
	}
}

page(name: "pageWeatherSettingsD", title: "Weather Reporting Settings") {
	section {
		input "D_includeTemp", "bool", title: "Speak current temperature (from local forecast)", defaultValue: "false"
        input "D_humidity", "capability.relativeHumidityMeasurement", title: "Speak local humidity (from device)", required: false, multiple: false
        input "D_weatherReport", "bool", title: "Speak tomorrow's weather forecast", defaultValue: "false"
        input "D_includeSunrise", "bool", title: "Speak tomorrow's sunrise", defaultValue: "false"
    	input "D_includeSunset", "bool", title: "Speak tomorrow's sunset", defaultValue: "false"
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
    initialize()
}

def updated() {
    unschedule()
    unsubscribe()
    initialize()
}

def initialize() {
	midNightReset()
    if (A_switches) {
    	subscribe (A_switches, "switch.off", scenario_A)
    }
	if (B_switches) {
    	subscribe (B_switches, "switch.off", scenario_B)
    }
	if (C_switches) {
    	subscribe (C_switches, "switch.off", scenario_C)
    }
	if (D_switches) {
    	subscribe (D_switches, "switch.off", scenario_D)
    }
    if (A_buttonDevice && A_buttonPress){
    	subscribe(A_buttonDevice, "button.pushed", buttonHandler_A)
    }
    if (B_buttonDevice && B_buttonPress){
    	subscribe(B_buttonDevice, "button.pushed", buttonHandler_B)
    }
    if (C_buttonDevice && C_buttonPress){
    	subscribe(C_buttonDevice, "button.pushed", buttonHandler_C)
    }
	if (D_buttonDevice && D_buttonPress){
    	subscribe(D_buttonDevice, "button.pushed", buttonHandler_D)
    } 
}

//--------------------------------------

def scenario_A(evt) {
	if ((!A_triggerOnce || (A_triggerOnce && !A_triggered)) && getTimeOk(A_timeStart, A_timeEnd) && getDayOk(A_day) && (!A_mode || A_mode.contains(location.mode))) {
		state.fullMsgA = ""
		   
		if (A_weatherReport || A_humidity || A_includeTemp) {
			getWeatherReport(1, A_weatherReport, A_humidity, A_includeTemp)
		}
        
        if (A_includeSunrise || A_includeSunset) {
        	getSunriseSunset(1, A_includeSunrise, A_includeSunset)
        }
                   
		if (A_phrase) {
        	location.helloHome.execute(A_phrase)
        	if (A_confirmPhrase){
            	getPhraseConfirmation(A_phrase, 1)
            }
        }
        
        if (A_triggerMode && location.mode != A_triggerMode) {
			if (location.modes?.find{it.name == A_triggerMode}) {
				setLocationMode(A_triggerMode)
			} else {
				log.debug "Unable to change to undefined mode '${A_triggerMode}'"
			}
		}
        
        if (A_confirmMode){
            	getModeConfirmation(A_triggerMode, 1)
        }
        
        if (A_msg) {
			getGreeting(A_msg, 1)
		} 
      
		state.soundA = textToSpeech(state.fullMsgA, true)
    	if (A_volume) {
			A_sonos.setLevel(A_volume)
		}
		A_sonos.playTrack(state.soundA.uri)
		if (A_triggerOnce) {
			state.A_triggered = true
			runOnce (getMidnight(), midNightReset)
		}
    }
}

def buttonHandler_A(evt){
    def data = new JsonSlurper().parseText(evt.data)
    def button = data.buttonNumber
	def remoteButton = A_buttonPress as Integer
   
	if (button == remoteButton) {
        scenario_A()
    }
}

def scenario_B(evt) {
	if ((!B_triggerOnce || (B_triggerOnce && !B_triggered)) && getTimeOk(B_timeStart, B_timeEnd) && getDayOk(B_day) && (!B_mode || B_mode.contains(location.mode))) {
		state.fullMsgB = ""
		   
		if (B_weatherReport || B_humidity || B_includeTemp) {
			getWeatherReport(2, B_weatherReport, B_humidity, B_includeTemp)
		}
         
        if (B_includeSunrise || B_includeSunset) {
        	getSunriseSunset(2, B_includeSunrise, B_includeSunset)
        }
         
		if (B_phrase) {
        	location.helloHome.execute(B_phrase)
        	if (B_confirmPhrase) {
            	getPhraseConfirmation(B_phrase, 2)
            }
        }
        
        if (B_triggerMode && location.mode != B_triggerMode) {
			if (location.modes?.find{it.name == B_triggerMode}) {
				setLocationMode(B_triggerMode)
			} else {
				log.debug "Unable to change to undefined mode '${B_triggerMode}'"
			}
		}
        
        if (B_confirmMode){
            	getModeConfirmation(B_triggerMode, 2)
        }

		if (B_msg) {
			getGreeting(B_msg, 2)
		} 
      
		state.soundB = textToSpeech(state.fullMsgB, true)
    	if (B_volume) {
			B_sonos.setLevel(B_volume)
		}
		
        B_sonos.playTrack(state.soundB.uri)
		if (B_triggerOnce) {
			state.B_triggered = true
			runOnce (getMidnight(), midNightReset)
		}
    }
}

def buttonHandler_B(evt){
    def data = new JsonSlurper().parseText(evt.data)
    def button = data.buttonNumber
	def remoteButton = B_buttonPress as Integer
   
	if (button == remoteButton) {
        scenario_B()
    }
}

def scenario_C(evt) {
	if ((!C_triggerOnce || (C_triggerOnce && !C_triggered)) && getTimeOk(C_timeStart, C_timeEnd) && getDayOk(C_day) && (!C_mode || C_mode.contains(location.mode))) {
		state.fullMsgC = ""
		
		if (C_weatherReport || C_humidity || C_includeTemp) {
			getWeatherReport(3, C_weatherReport, C_humidity, C_includeTemp)
		}
        
        if (C_includeSunrise || C_includeSunset) {
        	getSunriseSunset(3, C_includeSunrise, C_includeSunset)
        }
        
		if (C_phrase) {
        	location.helloHome.execute(C_phrase)
        	if (C_confirmPhrase){
            	getPhraseConfirmation(C_phrase, 3)
            }
        }
                
        if (C_triggerMode && location.mode != C_triggerMode) {
            if (location.modes?.find{it.name == C_triggerMode}) {
				setLocationMode(C_triggerMode)
			} else {
				log.debug "Unable to change to undefined mode '${C_triggerMode}'"
			}
		}
        
        if (C_confirmMode){
            	getModeConfirmation(C_triggerMode, 3)
        }

		if (C_msg) {
			getGreeting(C_msg, 3)
		} 
      
		state.soundC = textToSpeech(state.fullMsgC, true)
    	if (C_volume) {
			C_sonos.setLevel(C_volume)
		}
		C_sonos.playTrack(state.soundC.uri)
		if (C_triggerOnce) {
			state.C_triggered = true
			runOnce (getMidnight(), midNightReset)
		}
    }
}

def buttonHandler_C(evt){
    def data = new JsonSlurper().parseText(evt.data)
    def button = data.buttonNumber
	def remoteButton = C_buttonPress as Integer
   
	if (button == remoteButton) {
        scenario_C()
    }
}

def scenario_D(evt) {
	if ((!D_triggerOnce || (D_triggerOnce && !D_triggered)) && getTimeOk(D_timeStart, D_timeEnd) && getDayOk(D_day) && (!D_mode || D_mode.contains(location.mode))) {
		state.fullMsgD = ""
		   
		if (D_weatherReport || D_humidity || D_includeTemp) {
			getWeatherReport(4, D_weatherReport, D_humidity, D_includeTemp)
		}
        
        if (D_includeSunrise || D_includeSunset) {
        	getSunriseSunset(4, D_includeSunrise, D_includeSunset)
        }
        
		if (D_phrase) {
        	location.helloHome.execute(D_phrase)
        	if (D_confirmPhrase){
            	getPhraseConfirmation(D_phrase, 4)
            }
        }

        if (D_triggerMode && location.mode != D_triggerMode) {
            if (location.modes?.find{it.name == D_triggerMode}) {
				setLocationMode(D_triggerMode)
			} else {
				log.debug "Unable to change to undefined mode '${D_triggerMode}'"
			}
		}
        
        if (D_confirmMode){
            	getModeConfirmation(D_triggerMode, 4)
        }
		
        if (D_msg) {
			getGreeting(D_msg, 4)
		} 
      
		state.soundD = textToSpeech(state.fullMsgD, true)
    	if (D_volume) {
			D_sonos.setLevel(D_volume)
		}
		
        D_sonos.playTrack(state.soundD.uri)
		if (D_triggerOnce) {
			state.D_triggered = true
			runOnce (getMidnight(), midNightReset)
		}
    }
}

def buttonHandler_D(evt){
    def data = new JsonSlurper().parseText(evt.data)
    def button = data.buttonNumber
	def remoteButton = D_buttonPress as Integer
   
	if (button == remoteButton) {
        scenario_D()
    }
}

//--------------------------------------

def midNightReset() {
	state.A_triggered = false
    state.B_triggered = false
    state.C_triggered = false
    state.D_triggered = false
}

//--------------------------------------

def getMidnight() {
	def midnightToday = timeToday("2000-01-01T23:59:59.999-0000", location.timeZone)
}

def getDesc(sonos) {
	def desc = sonos ? "Tap to edit scenario" : "Tap to setup scenario"
}

def greyedOutTime(start, end){
	def result = start || end ? "complete" : ""
}

def greyOut1(param1, param2, param3){
	def result = param1 || param2 || param3 ? "complete" : ""
}

def greyOut(param1, param2){
	def result = param1 && param2 ? "complete" : ""
}

def getTitle(scenario, num) {
	def title = scenario ? scenario : "Scenario ${num} not configured"
}

def getWeatherTitle(parm1, parm2, parm3) {
	def title = param1 || param2 || param3 ? "Tap to edit weather reporting options" : "Tap to setup weather reporting options"
}

def buttonDesc(button, num){
	def desc = button && num ? "${button}, button: ${num}" : "Tap to set button controller settings"
}

private getDayOk(dayList) {
	def result = true
	if (dayList) {
		result = dayList.contains(getDay())
	}
	log.trace "DayOk = $result"
    result
}

def getTimeLabel(start, end){
	def timeLabel = "Tap to set"
	
    if(start && end){
    	timeLabel = "Between" + " " + hhmm(start) + " "  + "and" + " " +  hhmm(end)
    }
    else if (start) {
		timeLabel = "Start at" + " " + hhmm(start)
    }
    else if(end){
    timeLabel = "End at" + hhmm(end)
    }
	timeLabel	
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

private getWeatherReport(scenario, weatherReport, humidity, includeTemp) {
	if (location.timeZone || zipCode) {
		def isMetric = location.temperatureScale == "C"
        def sb = new StringBuilder()
        
        if (includeTemp){
        	def current = getWeatherFeature("conditions", zipCode)
        	if (isMetric) {
        		sb << "The current temperature is ${Math.round(current.current_observation.temp_c)} degrees. "
        	}
        	else {
        		sb << "The current temperature is ${Math.round(current.current_observation.temp_f)} degrees. "
        	}
        }
        
        if (humidity) {
        	sb << "The local relative humidity is ${humidity.currentValue("humidity")}%. "
        }
        
        if (weatherReport) {
           	def weather = getWeatherFeature("forecast", zipCode)
            
            sb << "Tomorrow's forecast is "
			if (isMetric) {
        		sb << weather.forecast.txt_forecast.forecastday[2].fcttext_metric 
        	}
        	else {
          		sb << weather.forecast.txt_forecast.forecastday[2].fcttext
        	}
        }
        
		def msg = sb.toString()
        msg = msg.replaceAll(/([0-9]+)C/,'$1 degrees')
        msg = msg.replaceAll(/([0-9]+)F/,'$1 degrees')
        msg = msg.replaceAll(' SW ',' South West ')
        msg = msg.replaceAll(' NW ',' North West ')
        msg = msg.replaceAll(' NE ',' North East ')
        msg = msg.replaceAll(' SE ',' South East ')
        //msg = msg.replaceAll(' WNW ',' West North West ') //add back in if you heard the letters and not West North West
        //msg = msg.replaceAll(' ENE ',' East North East ')
        compileMsg(msg, scenario)
	}
	else {
		msg = "Please set the location of your hub with the SmartThings mobile app, or enter a zip code to receive weather forecasts. "
		compileMsg(msg, scenario)
    }
}

private getSunriseSunset(scenario, includeSunrise, includeSunset){
	if (location.timeZone || zipCode) {
    	def tomorrowDate = new Date() + 1
    	def s = getSunriseAndSunset(zipcode: zipCode, date: tomorrowDate)	
		def riseTime = hhmm(convertEpoch(s.sunrise.time))
		def setTime = hhmm(convertEpoch(s.sunset.time))
   		def msg = ""
    	if (includeSunrise && includeSunset) {
			msg = "The sun will rise tomorrow at ${riseTime} and set at ${setTime}. "
    	}
    	else if (includeSunrise && !includeSunset) {
    		msg = "The sun will rise tomorrow at ${riseTime}. "
    	}
    	else if (!includeSunrise && includeSunset) {
    		msg = "The sun will set tomorrow at ${setTime}. "
    	}
    	compileMsg(msg, scenario)
	}
	else {
		msg = "Please set the location of your hub with the SmartThings mobile app, or enter a zip code to receive sunset and sunrise information. "
		compileMsg(msg, scenario)
	}
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
	compileMsg(msg, scenario)
}

private getPhraseConfirmation(phrase, scenario) {
	def msg="The Smart Things Hello Home phrase, ${phrase}, has been activated. "
	compileMsg(msg, scenario)
}

private getModeConfirmation(mode, scenario) {
	def msg="The Smart Things mode will be set to, ${mode}. "
	compileMsg(msg, scenario)
}

private compileMsg(msg, scenario) {
	log.debug "msg = ${msg}"
    if (scenario == 1) {state.fullMsgA = state.fullMsgA + "${msg} "}
	if (scenario == 2) {state.fullMsgB = state.fullMsgB + "${msg} "}
	if (scenario == 3) {state.fullMsgC = state.fullMsgC + "${msg} "}
	if (scenario == 4) {state.fullMsgD = state.fullMsgD + "${msg} "}
}

//Version/Copyright/Information/Help

private def textAppName() {
	def text = "Say Good Night!"
}	

private def textVersion() {
    def text = "Version 1.0.0 (06/01/2015)"
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
    	"Within each scenario, choose a Sonos speaker along with a trigger to have the system wish you a good night. " +
        "Triggers can be certain switches being turning off or the press of a button on a controller. You can also run a Hello, Home phrase or change mode " +
        "when the scenario is triggered. Triggers can be restricted to certain times and days of the week or modes. The voice message can be a simple text phrase, "+
        "or can include the current temperature and tomorrow's weather forecast. Variables to use in the voice greeting include %day%, %time% and %date%."
}

