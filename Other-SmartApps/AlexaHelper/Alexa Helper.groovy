/**
 *  Alexa Helper
 *
 *  Copyright 2015 Michael Struck
 *  Version 2.2.2 10/13/15
 * 
 *  Version 1.0.0 - Initial release
 *  Version 2.0.0 - Added 6 slots to allow for one app to control multiple on/off actions
 *  Version 2.0.1 - Changed syntax to reflect SmartThings Routines (instead of Hello, Home Phrases)
 *  Version 2.1.0 - Added timers to the first 4 slots to allow for delayed triggering of routines or modes
 *  Version 2.2.1 - Allow for on/off control of switches and changed the UI slightly to allow for other controls in the future
 *  Version 2.2.2 - Fixed an issue with slot 4
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
    description: "Allows for routines or modes to be tied to various switch's state controlled by Alexa (Amazon Echo).",
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
		}	
        section ("When switch is on..."){
        	if (phrases) {
            	input "A_onPhrase", "enum", title: "Perform this routine", options: phrases, required: false
            }
        	input "A_onMode", "mode", title: "Change to this mode", required: false
            input "A_onSwitches", "capability.switch", title: "Turn on these switches...", multiple: true, required: false
        	input "A_delayOn", "number", title: "Delay in minutes", defaultValue: 0, required: false
       }
        
        if (!A_momentary) {
        	section ("When switch is off..."){
        		if (phrases) {
            		input "A_offPhrase", "enum", title: "Perform this routine", options: phrases, required: false
            	}
        		input "A_offMode", "mode", title: "Change to this mode", required: false
                input "A_offSwitches", "capability.switch", title: "Turn off these switches...", multiple: true, required: false
                input "A_delayOff", "number", title: "Delay in minutes", defaultValue: 0, required: false
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
		}	
        section ("When switch is on..."){
        	if (phrases) {
            	input "B_onPhrase", "enum", title: "Perform this routine", options: phrases, required: false
            }
        	input "B_onMode", "mode", title: "Change to this mode", required: false
            input "B_onSwitches", "capability.switch", title: "Turn on these switches...", multiple: true, required: false
            input "B_delayOn", "number", title: "Delay in minutes", defaultValue: 0, required: false
        }
        
        if (!B_momentary) {
        	section ("When switch is off..."){
        		if (phrases) {
            		input "B_offPhrase", "enum", title: "Perform this routine", options: phrases, required: false
            	}
        		input "B_offMode", "mode", title: "Change to this mode", required: false
                input "B_offSwitches", "capability.switch", title: "Turn off these switches...", multiple: true, required: false
                input "B_delayOff", "number", title: "Delay in minutes", defaultValue: 0, required: false
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
		}	
        section ("When switch is on..."){
        	if (phrases) {
            	input "C_onPhrase", "enum", title: "Perform this routine", options: phrases, required: false
            }
        	input "C_onMode", "mode", title: "Change to this mode", required: false
            input "C_onSwitches", "capability.switch", title: "Turn on these switches...", multiple: true, required: false
            input "C_delayOn", "number", title: "Delay in minutes", defaultValue: 0, required: false
        }
        
        if (!C_momentary) {
        	section ("When switch is off..."){
        		if (phrases) {
            		input "C_offPhrase", "enum", title: "Perform this routine", options: phrases, required: false
            	}
        		input "C_offMode", "mode", title: "Change to this mode", required: false
                input "C_offSwitches", "capability.switch", title: "Turn off these switches...", multiple: true, required: false
                input "C_delayOff", "number", title: "Delay in minutes", defaultValue: 0, required: false
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
		}	
        section ("When switch is on..."){
        	if (phrases) {
            	input "D_onPhrase", "enum", title: "Perform this routine", options: phrases, required: false
            }
        	input "D_onMode", "mode", title: "Change to this mode", required: false
            input "D_onSwitches", "capability.switch", title: "Turn on these switches...", multiple: true, required: false
            input "D_delayOn", "number", title: "Delay in minutes", defaultValue: 0, required: false
        }
        
        if (!D_momentary) {
        	section ("When switch is off..."){
        		if (phrases) {
            		input "D_offPhrase", "enum", title: "Perform this routine", options: phrases, required: false
            	}
        		input "D_offMode", "mode", title: "Change to this mode", required: false
                input "D_offSwitches", "capability.switch", title: "Turn off these switches...", multiple: true, required: false
                input "D_delayOff", "number", title: "Delay in minutes", defaultValue: 0, required: false
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
		}	
        section ("When switch is on..."){
        	if (phrases) {
            	input "E_onPhrase", "enum", title: "Perform this routine", options: phrases, required: false
            }
        	input "E_onMode", "mode", title: "Change to this mode", required: false
            input "E_onSwitches", "capability.switch", title: "Turn on these switches...", multiple: true, required: false
        }
        
        if (!E_momentary) {
        	section ("When switch is off..."){
        		if (phrases) {
            		input "E_offPhrase", "enum", title: "Perform this routine", options: phrases, required: false
            	}
        		input "E_offMode", "mode", title: "Change to this mode", required: false
                input "E_offSwitches", "capability.switch", title: "Turn off these switches...", multiple: true, required: false
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
		}	
        section ("When switch is on..."){
        	if (phrases) {
            	input "F_onPhrase", "enum", title: "Perform this routine", options: phrases, required: false
            }
        	input "F_onMode", "mode", title: "Change to this mode", required: false
            input "F_onSwitches", "capability.switch", title: "Turn on these switches...", multiple: true, required: false
        }
        
        if (!F_momentary) {
        	section ("When switch is off..."){
        		if (phrases) {
            		input "F_offPhrase", "enum", title: "Perform this routine", options: phrases, required: false
            	}
        		input "F_offMode", "mode", title: "Change to this mode", required: false
                input "F_offSwitches", "capability.switch", title: "Turn off these switches...", multiple: true, required: false
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
    unschedule()
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
        if (evt.value == "on" && (A_onPhrase || A_onMode || A_onSwitches)) {
        	if (!A_delayOn || A_delayOn == 0) {
            	A_on()
            }
            else {
            	unschedule 
                runIn(A_delayOn*60, A_on, [overwrite: true])
            }
    	} 
    	else if (evt.value == "off" && !A_momentary && (A_offPhrase || A_offMode || A_offSwitches)) {
        	if (!A_delayOff || A_delayOff == 0) {
            		A_off()
            }
            else {
            	runIn(A_delayOff*60, A_off, [overwrite: true])
            }
    	}
	}
}

def A_on(){
	if (A_onPhrase){
		location.helloHome.execute(A_onPhrase)
	}
	if (A_onMode) {
		changeMode(A_onMode)
	}
    if (A_onSwitches){
    	A_onSwitches?.on()
    }
}

def A_off(){
	if (A_offPhrase){
		location.helloHome.execute(A_offPhrase)
	}
	if (A_offMode) {
		changeMode(A_offMode)
	}
    if (A_offSwitches){
    	A_offSwitches?.off()
    }
}

//B Handlers
def B_switchHandler(evt) {
    if ((!B_mode || B_mode.contains(location.mode)) && getDayOk(B_day) && getTimeOk(B_timeStart,B_timeEnd)) { 
    	log.debug "Running ${B_ScenarioName}"
        if (evt.value == "on" && (B_onPhrase || B_onMode || B_onSwitches)) {
    		if (!B_delayOn || B_delayOn == 0) {
            	B_on()
            }
            else {
            	runIn(B_delayOn*60, B_on, [overwrite: true])
            }
    	} 
    	else if (evt.value == "off" && !B_momentary && (B_offPhrase || B_offMode || B_offSwitches)) {
        	if (!B_delayOff || B_delayOff == 0) {
            	B_off()
            }
            else {
            	runIn(B_delayOff*60, B_off, [overwrite: true])
            }
    	}
	}
}

def B_on(){
	if (B_onPhrase){
		location.helloHome.execute(B_onPhrase)
	}
	if (B_onMode) {
		changeMode(B_onMode)
	}
    if (B_offSwitches){
    	B_offSwitches?.on()
    }
}

def B_off(){
	if (B_offPhrase){
		location.helloHome.execute(B_offPhrase)
	}
	if (B_offMode) {
		changeMode(B_offMode)
	}
    if (B_offSwitches){
    	B_offSwitches?.off()
    }
}

//C Handlers
def C_switchHandler(evt) {
    if ((!C_mode || C_mode.contains(location.mode)) && getDayOk(C_day) && getTimeOk(C_timeStart,C_timeEnd)) { 
    	log.debug "Running ${C_ScenarioName}"
        if (evt.value == "on" && (C_onPhrase || C_onMode || C_onSwitches)) {
    		if (!C_delayOn || C_delayOn == 0) {
            	C_on()
            }
            else {
            	runIn(C_delayOn*60, C_on, [overwrite: true])
            }
    	} 
    	else if (evt.value == "off" && !C_momentary && (C_offPhrase || C_offMode || C_offSwitches)) {
        	if (!C_delayOff || C_delayOff == 0) {
            	C_off()
            }
            else {
            	runIn(C_delayOff*60, C_off, [overwrite: true])
            }
   		}
	}
}

def C_on(){
	if (C_onPhrase){
		location.helloHome.execute(C_onPhrase)
	}
	if (C_onMode) {
		changeMode(C_onMode)
	}
    if (C_onSwitches){
    	C_onSwitches?.on()
    }
}

def C_off(){
	if (C_offPhrase){
		location.helloHome.execute(C_offPhrase)
        
	}
	if (C_offMode) {
		changeMode(C_offMode)
	}
    if (C_offSwitches){
    	C_offSwitches?.off()
    }
}

//D Handlers
def D_switchHandler(evt) {
    if ((!D_mode || D_mode.contains(location.mode)) && getDayOk(D_day) && getTimeOk(D_timeStart,D_timeEnd)) { 
        log.debug "Running ${D_ScenarioName}"
        if (evt.value == "on" && (D_onPhrase || D_onMode || D_onSwitches)) {
    		if (!D_delayOn || D_delayOn == 0) {
            	D_on()
            }
            else {
            	runIn(D_delayOn*60, D_on, [overwrite: true])
            }
    	} 
    	else if (evt.value == "off" && !D_momentary && (D_offPhrase || D_offMode || D_offSwitches)) {
        	if (!D_delayOff || D_delayOff == 0) {
            	D_off()
            }
            else {
            	runIn(D_delayOff*60, D_off, [overwrite: true])
            }
    	}
	}
}

def D_on(){
	if (D_onPhrase){
            location.helloHome.execute(D_onPhrase)
	}
	if (D_onMode) {
		changeMode(D_onMode)
	}
    if (D_onSwitches){
    	D_onSwitches?.on()
    }
}

def D_off(){
	if (D_offPhrase){
		location.helloHome.execute(D_offPhrase)
	}
	if (D_offMode) {
		changeMode(D_offMode)
	}
    if (D_offSwitches){
    	D_offSwitches?.off()
    }
}

//E Handlers
def E_switchHandler(evt) {
	if ((!E_mode || E_mode.contains(location.mode)) && getDayOk(E_day) && getTimeOk(E_timeStart,E_timeEnd)) {    
    	log.debug "Running ${E_ScenarioName}"
        if (evt.value == "on" && (E_onPhrase || E_onMode || E_onSwitches)) {
        	if (E_onPhrase){
        		location.helloHome.execute(E_onPhrase)
        	}
        	if (E_onMode) {
        		changeMode(E_onMode)
        	}
            if (E_onSwitches){
    			E_onSwitches?.on()
   			 }
    	} 
    	else if (evt.value == "off" && !E_momentary && (E_offPhrase || E_offMode || E_offSwitches)) {
        	if (E_offPhrase){
        		location.helloHome.execute(E_offPhrase)
    		}
        	if (E_offMode) {
        		changeMode(E_offMode)
        	}
            if (E_offSwitches){
    			E_offSwitches?.off()
    		}
    	}
	}
}

//F Handlers
def F_switchHandler(evt) {
	if ((!F_mode || F_mode.contains(location.mode)) && getDayOk(F_day) && getTimeOk(F_timeStart,F_timeEnd)) {    
    	log.debug "Running ${F_ScenarioName}"
        if (evt.value == "on" && (F_onPhrase || F_onMode || F_onSwitches)) {
        	if (F_onPhrase){
        		location.helloHome.execute(F_onPhrase)
        	}
        	if (F_onMode) {
        		changeMode(F_onMode)
        	}
            if (F_onSwitches){
    			F_onSwitches?.on()
    		}
    	} 
    	else if (evt.value == "off" && !F_momentary && (F_offPhrase || F_offMode || F_offSwitches)) {
        	if (F_offPhrase){
        		location.helloHome.execute(F_offPhrase)
    		}
        	if (F_offMode) {
        		changeMode(F_offMode)
        	}
            if (F_offSwitches){
    			F_offSwitches?.off()
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
    def text = "Version 2.2.2 (10/13/2015)"
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
		"Ties SmartThings routines, modes or switches to the on/off state of a specifc switch. "+
		"Perfect for use with Alexa.\n\nTo use, first create the required momentary button tiles or virtual switches from the SmartThings IDE. "+
		"You may also use any physical switches already associated with SmartThings. Include these switches within the Echo/SmartThings app, then discover the switches on the Echo. "+
		"Finally, within one of the six scenarios of this app, define the switch to be used and tie the on/off state of that switch to a specific routine, mode or on/off state of other switches. "+
		"The routine, mode or switches will fire with the switch state change, except in cases where you have a delay specified. This time delay is optional and only available in the first four scenarios. "+
        "\n\nPlease note that if you are using a momentary switch you should only define the 'on' action within each scenario." 

}
