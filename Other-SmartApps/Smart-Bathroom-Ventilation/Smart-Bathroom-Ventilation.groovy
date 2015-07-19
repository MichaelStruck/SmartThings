/**
 *  Smart Bathroom Ventilation
 *
 *  Version - 1.3.0 7/18/15
 * 
 *  Version 1.0.0 - Initial release
 *  Version 1.1.0 - Added restrictions for time that fan goes on to allow for future features along with logic fixes
 *  Version 1.2.0 - Added the option of starting the fans based on time (to eliminate the time of polling and for those without a humidity sensor)
 *  Version 1.3.0 - Code optimization
 * 
 *  Copyright 2015 Michael Struck - Uses code from Lighting Director by Tim Slagle & Michael Struck
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
    name: "Smart Bathroom Ventilation",
    namespace: "MichaelStruck",
    author: "Michael Struck",
    description: "Control up to 4 ventilation scenarios based on humidity or certain lights being turned on.",
    category: "Convenience",
    iconUrl: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Smart-Bathroom-Ventilation/BathVent.png",
    iconX2Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Smart-Bathroom-Ventilation/BathVent@2x.png",
    iconX3Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Smart-Bathroom-Ventilation/BathVent@2x.png"
    )

preferences {
	page name:"pageSetup"
    page name:"pageSetupScenarioA"
    page name:"pageSetupScenarioB"
    page name:"pageSetupScenarioC"
    page name:"pageSetupScenarioD"
}

// Show setup page
def pageSetup() {
	dynamicPage(name: "pageSetup", install: true, uninstall: true) {
        section("Setup Menu") {
			href "pageSetupScenarioA", title: getTitle(ScenarioNameA), description: "", state: greyOut(ScenarioNameA)
			href "pageSetupScenarioB", title: getTitle(ScenarioNameB), description: "", state: greyOut(ScenarioNameB)
			href "pageSetupScenarioC", title: getTitle(ScenarioNameC), description: "", state: greyOut(ScenarioNameC)
			href "pageSetupScenarioD", title: getTitle(ScenarioNameD), description: "", state: greyOut(ScenarioNameD)
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
		section("Devices included in the scenario") {
			input "A_switch","capability.switch", title: "Monitor this light switch...", multiple: false, required: true
			input "A_humidity", "capability.relativeHumidityMeasurement",title: "Monitor the following humidity sensor...", multiple: false, required: false, submitOnChange:true
			input "A_fan", "capability.switch", title: "Control the following ventilation fans...", multiple: true, required: true
		}
		section("Fan settings") {
        	if (A_humidity){
            	input "A_humidityDelta", title: "Ventilation fans turns on when lights are on and humidity rises(%)", "number", required: false, description: "0-50%"
        	}
            input "A_timeOn", title: "Optionally, turn on fans after light switch is turned on (minutes, 0=immediately)", "number", required: false
            if (A_humidity) {
            	input "A_fanTime", title: "Turn off ventilation fans after...", "enum", required: false, options: [[5:"5 Minutes"],[10:"10 Minutes"],[15:"15 Minutes"],[30:"30 Minutes"],[60:"1 hour"],[98:"Light switch is turned off"],[99:"Humidity drops to or below original value"]]
			}
            else {
            	input "A_fanTime", title: "Turn off ventilation fans after...", "enum", required: false, options: [[5:"5 Minutes"],[10:"10 Minutes"],[15:"15 Minutes"],[30:"30 Minutes"],[60:"1 hour"],[98:"Light switch is turned off"]]
            }
            input "A_manualFan", title: "If ventilation fans are turned on manually, turn them off automatically using settings above", "bool", defaultValue: "false"
        }
		section("Restrictions") {            
			input "A_day", "enum", options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"], title: "Only on certain days of the week...",  multiple: true, required: false
        	href "timeIntervalInputA", title: "Only during a certain time...", description: getTimeLabel(A_timeStart, A_timeEnd), state: greyedOutTime(A_timeStart, A_timeEnd)
            input "A_mode", "mode", title: "Only during the following modes...", multiple: true, required: false
		}
		section("Name your scenario") {
            input "ScenarioNameA", "text", title: "Scenario Name", multiple: false, required: false, defaultValue: empty
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
		section("Devices included in the scenario") {
    		input "B_switch","capability.switch", title: "Monitor this light switch...", multiple: false, required: true
        	input "B_humidity", "capability.relativeHumidityMeasurement",title: "Monitor the following humidity sensor...", multiple: false, required: false, submitOnChange:true
        	input "B_fan", "capability.switch",title: "Control the following ventilation fans...", multiple: true, required: true
		}
		section("Fan settings") {
        	if (B_humidity){
            	input "B_humidityDelta", title: "Ventilation fans turns on when lights are on and humidity rises(%)", "number", required: false, description: "0-50%"
        	}
            input "B_timeOn", title: "Optionally, turn on fans after light switch is turned on (minutes, 0=immediately)", "number", required: false
            if (B_humidity){
            	input "B_fanTime", title: "Turn off ventilation fans after...", "enum", required: false, options: [[5:"5 Minutes"],[10:"10 Minutes"],[15:"15 Minutes"],[30:"30 Minutes"],[60:"1 hour"],[98:"Light switch is turned off"],[99:"Humidity drops to or below original value"]]
			}
            else{
            	input "B_fanTime", title: "Turn off ventilation fans after...", "enum", required: false, options: [[5:"5 Minutes"],[10:"10 Minutes"],[15:"15 Minutes"],[30:"30 Minutes"],[60:"1 hour"],[98:"Light switch is turned off"]]
            }
            input "B_manualFan", title: "If ventilation fans are turned on manually, turn them off automatically using settings above", "bool", defaultValue: "false"
        }
		section("Restrictions") {            
			input "B_day", "enum", options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"], title: "Only on certain days of the week...",  multiple: true, required:   false
        	href "timeIntervalInputB", title: "Only during a certain time...", description: getTimeLabel(B_timeStart, B_timeEnd), state: greyedOutTime(B_timeStart, B_timeEnd)
            input "B_mode", "mode", title: "Only during the following modes...", multiple: true, required: false
		}
		section("Name your scenario") {
            input "ScenarioNameB", "text", title: "Scenario Name", multiple: false, required: false, defaultValue: empty
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
		section("Devices included in the scenario") {
    		input "C_switch","capability.switch", title: "Monitor this light switch...", multiple: false, required: true
        	input "C_humidity", "capability.relativeHumidityMeasurement",title: "Monitor the following humidity sensor...", multiple: false, required: false, submitOnChange:true
        	input "C_fan", "capability.switch",title: "Control the following ventilation fans...", multiple: true, required: true
		}
		section("Fan settings") {
        	if (C_humidity){
            	input "C_humidityDelta", title: "Ventilation fans turns on when lights are on and humidity rises(%)", "number", required: false, description: "0-50%"
        	}
            input "C_timeOn", title: "Optionally, turn on fans after light switch is turned on (minutes, 0=immediately)", "number", required: false
            if (C_humidity){
            	input "C_fanTime", title: "Turn off ventilation fans after...", "enum", required: false, options: [[5:"5 Minutes"],[10:"10 Minutes"],[15:"15 Minutes"],[30:"30 Minutes"],[60:"1 hour"],[98:"Light switch is turned off"],[99:"Humidity drops to or below original value"]]
			}
            else {
            	input "C_fanTime", title: "Turn off ventilation fans after...", "enum", required: false, options: [[5:"5 Minutes"],[10:"10 Minutes"],[15:"15 Minutes"],[30:"30 Minutes"],[60:"1 hour"],[98:"Light switch is turned off"]]
			
            }
            input "C_manualFan", title: "If ventilation fans are turned on manually, turn them off automatically using settings above", "bool", defaultValue: "false"
        }
		section("Restrictions") {            
			input "C_day", "enum", options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"], title: "Only on certain days of the week...",  multiple: true, required:   false
        	href "timeIntervalInputC", title: "Only during a certain time...", description: getTimeLabel(C_timeStart, C_timeEnd), state: greyedOutTime(C_timeStart, C_timeEnd)
            input "C_mode", "mode", title: "Only during the following modes...", multiple: true, required: false
		}
		section("Name your scenario") {
            input "ScenarioNameC", "text", title: "Scenario Name", multiple: false, required: false, defaultValue: empty
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
	dynamicPage(name: "pageSetupScenarioA") {
		section("Devices included in the scenario") {
    		input "D_switch","capability.switch", title: "Monitor this light switch...", multiple: false, required: true
        	input "D_humidity", "capability.relativeHumidityMeasurement",title: "Monitor the following humidity sensor...", multiple: false, required: false, submitOnChange:true
        	input "D_fan", "capability.switch",title: "Control the following ventilation fans...", multiple: true, required: true
		}
		section("Fan settings") {
        	if (D_humidity){
            	input "D_humidityDelta", title: "Ventilation fans turn on when lights are on and humidity rises(%)", "number", required: false, description: "0-50%"
        	}
            input "D_timeOn", title: "Optionally, turn on fans after light switch is turned on (minutes, 0=immediately)", "number", required: false
            if (D_humidity){
            	input "D_fanTime", title: "Turn off ventilation fans after...", "enum", required: false, options: [[5:"5 Minutes"],[10:"10 Minutes"],[15:"15 Minutes"],[30:"30 Minutes"],[60:"1 hour"],[98:"Light switch is turned off"],[99:"Humidity drops to or below original value"]]
			}
            else {
            	input "D_fanTime", title: "Turn off ventilation fans after...", "enum", required: false, options: [[5:"5 Minutes"],[10:"10 Minutes"],[15:"15 Minutes"],[30:"30 Minutes"],[60:"1 hour"],[98:"Light switch is turned off"]]
            }
            input "D_manualFan", title: "If ventilation fans are turned on manually, turn them off automatically using settings above", "bool", defaultValue: "false"
        }
		section("Restrictions") {            
			input "D_day", "enum", options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"], title: "Only on certain days of the week...",  multiple: true, required:   false
        	href "timeIntervalInputD", title: "Only during a certain time...", description: getTimeLabel(D_timeStart, D_timeEnd), state: greyedOutTime(D_timeStart, D_timeEnd)
            input "D_mode", "mode", title: "Only during the following modes...", multiple: true, required: false
		}
		section("Name your scenario") {
            input "ScenarioNameD", "text", title: "Scenario Name", multiple: false, required: false, defaultValue: empty
    	}
    }
}

page(name: "timeIntervalInputD", title: "Only during a certain time") {
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

def installed() {
    initialize()
}

def updated() {
    state.triggeredA = false
    state.triggeredB = false
    state.triggeredC = false
    state.triggeredD = false
    unschedule()
    unsubscribe()
    initialize()
}

def initialize() {
	
    if(A_switch) {
        state.A_runTime = A_fanTime ? A_fanTime as Integer : 98
        subscribe(A_switch, "switch.on", onEventA)
    	subscribe(A_switch, "switch.off", offEventA)
    	if (A_manualFan) {
        	subscribe(A_fan, "switch.on", turnOnA)
            subscribe(A_fan, "switch.off", turnOffA)
        }
        if (A_humidity && (A_humidityDelta || state.A_runTime == 99)) {
    		subscribe(A_humidity, "humidity", humidityHandlerA)
		}
	}
    if(B_switch) {
        state.B_runTime = B_fanTime ? B_fanTime as Integer : 98
        subscribe(B_switch, "switch.on", onEventB)
    	subscribe(B_switch, "switch.off", offEventB)
    	if (B_manualFan) {
        	subscribe(B_fan, "switch.on", turnOnB)
            subscribe(B_fan, "switch.off", turnOffB)
        }
        if (B_humidity && (B_humidityDelta || state.B_runTime == 99)) {
    		subscribe(B_humidity, "humidity", humidityHandlerB)
		}
	}
    if(C_switch) {
		state.C_runTime = C_fanTime ? C_fanTime as Integer : 98
        subscribe(C_switch, "switch.on", onEventC)
    	subscribe(C_switch, "switch.off", offEventC)
    	if (C_manualFan) {
        	subscribe(C_fan, "switch.on", turnOnC)
            subscribe(C_fan, "switch.off", turnOffC)
        }
        if (C_humidity && (C_humidityDelta || state.C_runTime == 99)) {
    		subscribe(C_humidity, "humidity", humidityHandlerC)
		}
	}
    if(D_switch) {
		state.D_runTime = D_fanTime ? D_fanTime as Integer : 98
        subscribe(D_switch, "switch.on", onEventD)
    	subscribe(D_switch, "switch.off", offEventD)
    	if (D_manualFan) {
        	subscribe(D_fan, "switch.on", turnOnD)
            subscribe(D_fan, "switch.off", turnOffD)
        }
        if (D_humidity && (D_humidityDelta || state.D_runTime == 99)) {
    		subscribe(D_humidity, "humidity", humidityHandlerD)
		}
	}
}

//Handlers----------------
//A Handlers
def turnOnA(evt){
    if ((!A_mode || A_mode.contains(location.mode)) && getDayOk(A_day) && A_switch.currentValue("switch")=="on" && !state.triggeredA && getTimeOk(A_timeStart,A_timeEnd)) {
        log.debug "Ventilation turned on in '${ScenarioNameA}'."
    	A_fan?.on()
        state.triggeredA = true
        if (state.A_runTime < 98) {
			log.debug "Ventilation will be turned off in ${state.A_runTime} minutes in '${ScenarioNameA}'."
            unschedule (turnOnA)
            runIn (state.A_runTime*60, "turnOffA")
        }
	}
}

def turnOffA(evt) {
	log.debug "Ventilation turned off in '${ScenarioNameA}'."
    A_fan?.off()
    unschedule (turnOffA)
}

def humidityHandlerA(evt){
    def currentHumidityA =evt.value as Integer
    log.debug "Humidity value is ${currentHumidityA} in '${ScenarioNameA}'."
	if (state.humidityLimitA && currentHumidityA > state.humidityLimitA) {
        turnOnA()
    }
	if (state.humidityStartA && currentHumidityA <= state.humidityStartA && state.A_runTime == 99){
    	turnOffA()
    }      
}

def onEventA(evt) {
    def humidityDelta = A_humidity && A_humidityDelta ? A_humidityDelta as Integer : 0
    def text = ""
    if (A_humidity){
    	state.humidityStartA = A_humidity.currentValue("humidity")
    	state.humidityLimitA = state.humidityStartA + humidityDelta
        text = "Humidity starting value is ${state.humidityStartA} in '${ScenarioNameA}'. Ventilation threshold is ${state.humidityLimitA}."
    }
    log.debug "Light turned on in ${ScenarioNameA}. ${text}" 
    if ((!A_humidityDelta || A_humidityDelta == 0) && (A_timeOn != "null" && A_timeOn == 0)) {
    	turnOnA()
    }
    if ((A_timeOn && A_timeOn > 0) && getDayOk(A_day) && !state.triggeredA && getTimeOk(A_timeStart,A_timeEnd)) {
    	def timeOn = A_timeOn as Integer
        log.debug "Ventilation in '${ScenarioNameA}' will start in ${timeOn} minute(s)"
        runIn (timeOn*60, "turnOnA")
    }
}

def offEventA(evt) {
    def currentHumidityA = ""
    def text = ""
    if (A_humidity) {
    	currentHumidityA = A_humidity.currentValue("humidity") 
        text = "Humidity value is ${currentHumidityA}."
    }
    log.debug "Light turned off in '${ScenarioNameA}'. ${text}"
	state.triggeredA = false
    if (state.A_runTime == 98){
    	turnOffA()
    }
}

//B Handlers
def turnOnB(evt){
    if ((!B_mode || B_mode.contains(location.mode)) && getDayOk(B_day) && B_switch.currentValue("switch")=="on" && !state.triggeredB && getTimeOk(B_timeStart,B_timeEnd)) {
        log.debug "Ventilation turned on in '${ScenarioNameB}'."
    	B_fan?.on()
        state.triggeredA = true
        if (state.B_runTime < 98) {
			log.debug "Ventilation will be turned off in ${state.B_runTime} minutes in '${ScenarioNameB}'."
            unschedule (turnOnB)
            runIn (state.B_runTime*60, "turnOffB")
        }
	}
}

def turnOffB(evt) {
	log.debug "Ventilation turned off in '${ScenarioNameB}'."
    B_fan?.off()
    unschedule (turnOffB)
}

def humidityHandlerB(evt){
    def currentHumidityB =evt.value as Integer
    log.debug "Humidity value is ${currentHumidityB} in '${ScenarioNameB}'."
	if (state.humidityLimitB && currentHumidityB > state.humidityLimitB) {
        turnOnB()
    }
	if (state.humidityStartB && currentHumidityB <= state.humidityStartB && state.B_runTime == 99){
    	turnOffB()
    }      
}

def onEventB(evt) {
	def humidityDelta = B_humidity && B_humidityDelta ? B_humidityDelta as Integer : 0
    def text = ""
    if (B_humidity){
    	state.humidityStartB = B_humidity.currentValue("humidity")
    	state.humidityLimitB = state.humidityStartB + humidityDelta
        text = "Humidity starting value is ${state.humidityStartB} in '${ScenarioNameB}'. Ventilation threshold is ${state.humidityLimitB}."
    }
    log.debug "Light turned on in ${ScenarioNameB}. ${text}" 
    if ((!B_humidityDelta || B_humidityDelta == 0) && (B_timeOn != "null" && B_timeOn == 0)) {
    	turnOnB()
    }
    if ((B_timeOn && B_timeOn > 0) && getDayOk(B_day) && !state.triggeredB && getTimeOk(B_timeStart,B_timeEnd)) {
    	def timeOn = B_timeOn as Integer
        log.debug "Ventilation in '${ScenarioNameB}' will start in ${timeOn} minute(s)"
        runIn (timeOn*60, "turnOnB")
    }
}

def offEventB(evt) {
    def currentHumidityB = ""
    def text = ""
    if (B_humidity) {
    	currentHumidityB = B_humidity.currentValue("humidity") 
        text = "Humidity value is ${currentHumidityB}."
    }
    log.debug "Light turned off in '${ScenarioNameB}'. ${text}"
	state.triggeredB = false
    if (state.B_runTime == 98){
    	turnOffB()
    }
}

//C Handlers
def turnOnC(evt){
    if ((!C_mode || C_mode.contains(location.mode)) && getDayOk(C_day) && C_switch.currentValue("switch")=="on" && !state.triggeredC && getTimeOk(C_timeStart,C_timeEnd)) {
        log.debug "Ventilation turned on in '${ScenarioNameC}'."
    	C_fan?.on()
        state.triggeredC = true
        if (state.C_runTime < 98) {
			log.debug "Ventilation will be turned off in ${state.C_runTime} minutes in '${ScenarioNameC}'."
            unschedule (turnOnC)
            runIn (state.C_runTime*60, "turnOffC")
        }
	}
}

def turnOffC(evt) {
	log.debug "Ventilation turned off in '${ScenarioNameC}'."
    C_fan?.off()
    unschedule (turnOffC)
}

def humidityHandlerC(evt){
    def currentHumidityC =evt.value as Integer
    log.debug "Humidity value is ${currentHumidityC} in '${ScenarioNameC}'."
	if (state.humidityLimitC && currentHumidityC > state.humidityLimitC) {
        turnOnC()
    }
	if (state.humidityStartC && currentHumidityC <= state.humidityStartC && state.C_runTime == 99){
    	turnOffC()
    }      
}

def onEventC(evt) {
    def humidityDelta = C_humidity && C_humidityDelta ? C_humidityDelta as Integer : 0
    def text = ""
    if (C_humidity){
    	state.humidityStartC = C_humidity.currentValue("humidity")
    	state.humidityLimitC = state.humidityStartC + humidityDelta
        text = "Humidity starting value is ${state.humidityStartC} in '${ScenarioNameC}'. Ventilation threshold is ${state.humidityLimitC}."
    }
    log.debug "Light turned on in ${ScenarioNameC}. ${text}" 
    if ((!C_humidityDelta || C_humidityDelta == 0) && (C_timeOn != "null" && C_timeOn == 0)) {
    	turnOnC()
    }
    if ((C_timeOn && C_timeOn > 0) && getDayOk(C_day) && !state.triggeredC && getTimeOk(C_timeStart,C_timeEnd)) {
    	def timeOn = C_timeOn as Integer
        log.debug "Ventilation in '${ScenarioNameC}' will start in ${timeOn} minute(s)"
        runIn (timeOn*60, "turnOnC")
    }
}

def offEventC(evt) {
    def currentHumidityC = ""
    def text = ""
    if (C_humidity) {
    	currentHumidityC = C_humidity.currentValue("humidity") 
        text = "Humidity value is ${currentHumidityC}."
    }
    log.debug "Light turned off in '${ScenarioNameC}'. ${text}"
	state.triggeredC = false
    if (state.C_runTime == 98){
    	turnOffC()
    }
}

//D Handlers
def turnOnD(evt){
    if ((!D_mode || D_mode.contains(location.mode)) && getDayOk(D_day) && D_switch.currentValue("switch")=="on" && !state.triggeredD && getTimeOk(D_timeStart,D_timeEnd)) {
        log.debug "Ventilation turned on in '${ScenarioNameD}'."
    	D_fan?.on()
        state.triggeredD = true
        if (state.D_runTime < 98) {
			log.debug "Ventilation will be turned off in ${state.D_runTime} minutes in '${ScenarioNameD}'."
            unschedule (turnOnD)
            runIn (state.A_runTime*60, "turnOffD")
        }
	}
}

def turnOffD(evt) {
	log.debug "Ventilation turned off in '${ScenarioNameD}'."
    D_fan?.off()
    unschedule (turnOffD)
}

def humidityHandlerD(evt){
    def currentHumidityD =evt.value as Integer
    log.debug "Humidity value is ${currentHumidityd} in '${ScenarioNameD}'."
	if (state.humidityLimitD && currentHumidityD > state.humidityLimitD) {
        turnOnD()
    }
	if (state.humidityStartD && currentHumidityD <= state.humidityStartd && state.d_runTime == 99){
    	turnOffD()
    }      
}

def onEventD(evt) {
    def humidityDelta = D_humidity && D_humidityDelta ? D_humidityDelta as Integer : 0
    def text = ""
    if (D_humidity){
    	state.humidityStartD = D_humidity.currentValue("humidity")
    	state.humidityLimitD = state.humidityStartA + humidityDelta
        text = "Humidity starting value is ${state.humidityStartD} in '${ScenarioNameD}'. Ventilation threshold is ${state.humidityLimitd}."
    }
    log.debug "Light turned on in ${ScenarioNameD}. ${text}" 
    if ((!D_humidityDelta || D_humidityDelta == 0) && (D_timeOn != "null" && D_timeOn == 0)) {
    	turnOnD()
    }
    if ((D_timeOn && D_timeOn > 0) && getDayOk(D_day) && !state.triggeredD && getTimeOk(D_timeStart,D_timeEnd)) {
    	def timeOn = D_timeOn as Integer
        log.debug "Ventilation in '${ScenarioNameD}' will start in ${timeOn} minute(s)"
        runIn (timeOn*60, "turnOnD")
    }
}

def offEventD(evt) {
    def currentHumidityD = ""
    def text = ""
    if (D_humidity) {
    	currentHumidityA = D_humidity.currentValue("humidity") 
        text = "Humidity value is ${currentHumidityD}."
    }
    log.debug "Light turned off in '${ScenarioNameD}'. ${text}"
	state.triggeredD = false
    if (state.D_runTime == 98){
    	turnOffD()
    }
}


//Common Methods-------------

def greyOut(scenario){
    def result = scenario ? "complete" : ""
}

def getTitle(scenario) {
	def title = scenario ? scenario : "Empty"
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
	def text = "Smart Bathroom Ventilation"
}	

private def textVersion() {
    def text = "Version 1.3.0 (07/18/2015)"
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
    	"Select a light switch to monitor, a humidity sensor (optional), and fans to control. You can choose when " +
        "the ventilation fans comes on; either when the room humidity rises over a certain level or come on after a user definable time after the light switch. "+
        "The ventilation fans will turn off based on either a timer setting, humidity, or the light switch being turned off. " +
        "You can also choose to have the ventilation fans turn off automatically if they are turned on manually. "+
        "Each scenario can be restricted to specific modes, times of day or certain days of the week."
}
