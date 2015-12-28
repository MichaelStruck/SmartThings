/**
 *  Smart Room Lighting and Dimming
 *
 *  Version - 1.0.0 5/4/15
 *  Version - 1.0.2 5/19/15 Code clean up for timeframe conditional check
 *  Version - 1.0.4 5/31/15 Added About screen from main menu
 *  Version - 1.0.5 6/17/15 Code optimization
 *  Version - 1.1.0 7/4/15 Added more dynamic interface options and the ability to utilize color controlled lights
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
    name: "Smart Room Lighting and Dimming",
    namespace: "MichaelStruck",
    author: "Michael Struck",
    description: "Control up to 4 rooms (scenarios) of light/dimmers based on motion and lux levels.",
    category: "Convenience",
    iconUrl: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Smart-Room-Lighting-and-Dimming/SmartLight.png",
    iconX2Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Smart-Room-Lighting-and-Dimming/SmartLight@2x.png",
	iconX3Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Smart-Room-Lighting-and-Dimming/SmartLight@2x.png"
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
    	input "A_motion", "capability.motionSensor", title: "Using these motion sensors...", multiple: true, required: false
        input "A_switches", "capability.switch",title: "Control the following switches...", multiple: true, required: false
        input "A_dimmers", "capability.switchLevel", title: "Dim the following...", multiple: true, required:false, submitOnChange:true
        input "A_colorControls", "capability.colorControl", title: "Control the following colored lights...",  multiple: true, required: false, submitOnChange:true
	}

	section("Lighting settings") {
        if (A_dimmers){
        	href "levelInputA", title: "Dimmer Options", description: getLevelLabel(A_levelDimOn, A_levelDimOff, A_calcOn), state: "complete"
        }
        if (A_colorControls){
        	href "colorInputA", title: "Color Options", description: getColorLabel(A_levelDimOnColor, A_levelDimOffColor, A_calcOnColor, A_color), state: "complete"
        }
        input name: "A_turnOnLux",type: "number",title: "Only run this scenario if lux is below...", multiple: false, required: false
        input name: "A_luxSensors",type: "capability.illuminanceMeasurement",title: "On these lux sensors",multiple: false,required: false
        input name: "A_turnOff",type: "number",title: "Turn off this scenario after motion stops (minutes)...", multiple: false, required: false
	}
            
	section("Restrictions") {            
        input name: "A_triggerOnce",type: "bool",title: "Trigger only once per day...", defaultValue: false
        input name: "A_switchDisable", type:"bool", title: "Stop triggering if physical switches/dimmers are turned off...", defaultValue:false
        href "timeIntervalInputA", title: "Only during a certain time...", description: getTimeLabel(A_timeStart, A_timeEnd), state: greyedOutTime(A_timeStart, A_timeEnd)
        input name:  "A_day", type: "enum", options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"], title: "Only on certain days of the week...",  multiple: true, required: false
        input name: "A_mode", type: "mode", title: "Only during the following modes...", multiple: true, required: false
	}
	section("Name your scenario") {
            input name:"ScenarioNameA", type: "text", title: "Scenario Name", multiple: false, required: false, defaultValue: empty
    }
    }
}

// Show "pageSetupScenarioB" page
def pageSetupScenarioB() {
    dynamicPage(name: "pageSetupScenarioB") {
	section("Devices included in the scenario") {
    	input "B_motion", "capability.motionSensor", title: "Using these motion sensors...", multiple: true, required: false
        input "B_switches", "capability.switch",title: "Control the following switches...", multiple: true, required: false
        input "B_dimmers", "capability.switchLevel", title: "Dim the following...", multiple: true, required:false, submitOnChange:true
        input "B_colorControls", "capability.colorControl", title: "Control the following colored lights...",  multiple: true, required: false, submitOnChange:true
	}

	section("Lighting settings") {
        if (B_dimmers){
        	href "levelInputB", title: "Dimmer Options", description: getLevelLabel(B_levelDimOn, B_levelDimOff, B_calcOn), state: "complete"
        }
        if (B_colorControls){
        	href "colorInputB", title: "Color Options", description: getColorLabel(B_levelDimOnColor, B_levelDimOffColor, B_calcOnColor, B_color), state: "complete"
        }
        input name: "B_turnOnLux",type: "number",title: "Only run this scenario if lux is below...", multiple: false, required: false
        input name: "B_luxSensors",type: "capability.illuminanceMeasurement",title: "On these lux sensors",multiple: false,required: false
        input name: "B_turnOff",type: "number",title: "Turn off this scenario after motion stops (minutes)...", multiple: false, required: false
	}
            
	section("Restrictions") {            
        input name: "B_triggerOnce",type: "bool",title: "Trigger only once per day...", defaultValue: false
        input name: "B_switchDisable", type:"bool", title: "Stop triggering if physical switches/dimmers are turned off...", defaultValue:false
        href "timeIntervalInputB", title: "Only during a certain time...", description: getTimeLabel(B_timeStart, B_timeEnd), state: greyedOutTime(B_timeStart, B_timeEnd)
        input name: "B_day", type: "enum", options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"], title: "Only on certain days of the week...",  multiple: true, required:   false
        input name: "B_mode", type: "mode", title: "Only during the following modes...", multiple: true, required: false
	}
	section("Name your scenario") {
            input name:"ScenarioNameB", type: "text", title: "Scenario Name", multiple: false, required: false, defaultValue: empty
    }
    }
}

// Show "pageSetupScenarioC" page
def pageSetupScenarioC() {
    dynamicPage(name: "pageSetupScenarioC") {
	section("Devices included in the scenario") {
    	input "C_motion", "capability.motionSensor", title: "Using these motion sensors...", multiple: true, required: false
        input "C_switches", "capability.switch",title: "Control the following switches...", multiple: true, required: false
        input "C_dimmers", "capability.switchLevel", title: "Dim the following...", multiple: true, required:false, submitOnChange:true
        input "C_colorControls", "capability.colorControl", title: "Control the following colored lights...",  multiple: true, required: false, submitOnChange:true
	}

	section("Lighting settings") {
        if (C_dimmers){
        	href "levelInputC", title: "Dimmer Options", description: getLevelLabel(C_levelDimOn, C_levelDimOff, C_calcOn), state: "complete"
        }
        if (C_colorControls){
        	href "colorInputC", title: "Color Options", description: getColorLabel(C_levelDimOnColor, C_levelDimOffColor, C_calcOnColor, C_color), state: "complete"
        }
        input name: "C_turnOnLux",type: "number", title: "Only run this scenario if lux is below...", multiple: false, required: false
        input name: "C_luxSensors",type: "capability.illuminanceMeasurement",title: "On these lux sensors",multiple: false,required: false
        input name: "C_turnOff",type: "number",title: "Turn off this scenario after motion stops (minutes)...", multiple: false, required: false
	}
            
	section("Restrictions") {            
        input name: "C_triggerOnce",type: "bool",title: "Trigger only once per day...", defaultValue: false
        input name: "C_switchDisable", type:"bool", title: "Stop triggering if physical switches/dimmers are turned off...", defaultValue:false
        href "timeIntervalInputC", title: "Only during a certain time...", description: getTimeLabel(C_timeStart, C_timeEnd), state: greyedOutTime(C_timeStart, C_timeEnd)
        input name: "C_day", type: "enum", options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"], title: "Only on certain days of the week...",  multiple: true, required:   false
        input name: "C_mode", type: "mode", title: "Only during the following modes...", multiple: true, required: false
	}
	section("Name your scenario") {
           input name:"ScenarioNameC", type: "text", title: "Scenario Name", multiple: false, required: false, defaultValue: empty
    }
    }
}

// Show "pageSetupScenarioD" page
def pageSetupScenarioD() {
    dynamicPage(name: "pageSetupScenarioD") {
	section("Devices included in the scenario") {
    	input "D_motion", "capability.motionSensor", title: "Using these motion sensors...", multiple: true, required: false
        input "D_switches", "capability.switch",title: "Control the following switches...", multiple: true, required: false
        input "D_dimmers", "capability.switchLevel", title: "Dim the following...", multiple: true, required:false, submitOnChange:true
        input "D_colorControls", "capability.colorControl", title: "Control the following colored lights...",  multiple: true, required: false, submitOnChange:true
	}

	section("Lighting settings") {
		if (D_dimmers){
        	href "levelInputD", title: "Dimmer Options", description: getLevelLabel(D_levelDimOn, D_levelDimOff, D_calcOn), state: "complete"
        }
        if (D_colorControls){
        	href "colorInputD", title: "Color Options", description: getColorLabel(D_levelDimOnColor, D_levelDimOffColor, D_calcOnColor, D_color), state: "complete"
        }
        input name: "D_turnOnLux",type: "number",title: "Only run this scenario if lux is below...", multiple: false, required: false
        input name: "D_luxSensors",type: "capability.illuminanceMeasurement",title: "On these lux sensors",multiple: false,required: false
        input name: "D_turnOff",type: "number",title: "Turn off this scenario after motion stops (minutes)...", multiple: false, required: false
	}
            
	section("Restrictions") {            
        input name: "D_triggerOnce",type: "bool",title: "Trigger only once per day...", defaultValue: false
        input name: "D_switchDisable", type:"bool", title: "Stop triggering if physical switches/dimmers are turned off...", defaultValue:false
        href "timeIntervalInputD", title: "Only during a certain time...", description: getTimeLabel(D_timeStart, D_timeEnd), state: greyedOutTime(D_timeStart, D_timeEnd)
        input name: "D_day", type: "enum", options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"], title: "Only on certain days of the week...",  multiple: true, required:   false
        input name: "D_mode", type: "mode", title: "Only during the following modes...", multiple: true, required: false
	}
	section("Name your scenario") {
    	input name:"ScenarioNameD", type: "text", title: "Scenario Name", multiple: false, required: false, defaultValue: empty
    }
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

//----------------------
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

if(A_motion) {
	subscribe(A_motion, "motion", onEventA)
}

if(B_motion) {
	subscribe(B_motion, "motion", onEventB)
}

if(C_motion) {
	subscribe(C_motion, "motion", onEventC)
}

if(D_motion) {
	subscribe(D_motion, "motion", onEventD)
}

if(A_switchDisable) {
	subscribe(A_switches, "switch.off", onPressA)
    subscribe(A_dimmers, "switch.off", onPressA)
    subscribe(A_colorControls, "switch.off", onPressA)
}

if(B_switchDisable) {
	subscribe(B_switches, "switch.off", onPressB)
    subscribe(B_dimmers, "switch.off", onPressB)
    subscribe(B_colorControls, "switch.off", onPressA)
}

if(C_switchDisable) {
	subscribe(C_switches, "switch.off", onPressC)
    subscribe(C_dimmers, "switch.off", onPressC)
    subscribe(C_colorControls, "switch.off", onPressC)
}

if(D_switchDisable) {
	subscribe(D_switches, "switch.off", onPressD)
    subscribe(D_dimmers, "switch.off", onPressD)
    subscribe(D_colorControls, "switch.off", onPressD)
}
}

def onEventA(evt) {
if ((!A_triggerOnce || (A_triggerOnce && !state.A_triggered)) && (!A_switchDisable || (A_switchDisable && !state.A_triggered))) {  	
if ((!A_mode || A_mode.contains(location.mode)) && getTimeOk (A_timeStart, A_timeEnd) && getDayOk(A_day)) {
	if (!A_luxSensors || (A_luxSensors.latestValue("illuminance") <= A_turnOnLux)){
    	if (A_motion.latestValue("motion").contains("active")) {
        	
        		log.debug("Motion Detected Running '${ScenarioNameA}'")
            
            	def levelSetOn = A_levelDimOn ? A_levelDimOn : 100
                def levelSetOnColor = A_levelDimOnColor ? A_levelDimOnColor : 100
            	def levelSetOff = A_levelDimOff ? A_levelDimOff : 0
                def levelSetOffColor = A_levelDimOffColor ? A_levelDimOffColor : 0

            	if (A_calcOn && A_luxSensors) {
    				levelSetOn = (levelSetOn * (1-(A_luxSensors.latestValue("illuminance")/A_turnOnLux))) + levelSetOff
                	if (levelSetOn > 100) {
               			levelSetOn = 100
               		}
    			}
                if (A_calcOnColor && A_luxSensors) {
    				levelSetOnColor = (levelSetOnColor * (1-(A_luxSensors.latestValue("illuminance")/A_turnOnLux))) + levelSetOffColor
                	if (levelSetOnColor > 100) {
               			levelSetOnColor = 100
               		}
    			}
        		A_dimmers?.setLevel(levelSetOn)
                setColoredLights(A_colorControls, A_color, levelSetOnColor)
        		A_switches?.on()
        		if (A_triggerOnce){
           			state.A_triggered = true
            		if (!A_turnOff) {
						runOnce (getMidnight(), midNightReset)
            		}
				}
				if (state.A_timerStart){
           			unschedule(delayTurnOffA)
       	   			state.A_timerStart = false
        		}	
		}
		else {
    		if (A_turnOff) {
				runIn(A_turnOff * 60, "delayTurnOffA")
        		state.A_timerStart = true
        	}
        	else {
        		A_switches?.off()
        		def levelSetOff = A_levelDimOff ? A_levelDimOff : 0
        		A_dimmers?.setLevel(levelSetOff)
                def levelSetOffColor = A_levelDimOffColor ? A_levelDimOffColor : 0
                A_colorControls?.setLevel(levelSetOffColor)
        		if (state.A_triggered) {
    				runOnce (getMidnight(), midNightReset)
    			}
        	}
		}
	}
}
else{
	log.debug("Motion outside of mode or time/day restriction.  Not running scenario.")
}
}
}

def delayTurnOffA(){
	A_switches?.off()
	def levelSetOff = A_levelDimOff ? A_levelDimOff : 0
    A_dimmers?.setLevel(levelSetOff)
    def levelSetOffColor = A_levelDimOffColor ? A_levelDimOffColor : 0
    A_colorControls?.setLevel(levelSetOffColor)
	state.A_timerStart = false
	if (state.A_triggered) {
    	runOnce (getMidnight(), midNightReset)
    }
}

def onPressA(evt) {
if ((!A_mode || A_mode.contains(location.mode)) && getTimeOk (A_timeStart, A_timeEnd) && getDayOk(A_day)) {
if (!A_luxSensors || (A_luxSensors.latestValue("illuminance") <= A_turnOnLux)){
if ((!A_triggerOnce || (A_triggerOnce && !state.A_triggered)) && (!A_switchDisable || (A_switchDisable && !state.A_triggered))) {	
    if (evt.physical){
    	state.A_triggered = true
        unschedule(delayTurnOffA)
        runOnce (getMidnight(), midNightReset)
        log.debug "Physical switch in '${ScenarioNameA}' pressed. Trigger for this scenario disabled."
	}
}
}
}
}

def onEventB(evt) {
if ((!B_triggerOnce || (B_triggerOnce && !state.B_triggered)) && (!B_switchDisable || (B_switchDisable && !state.B_triggered))) {
if ((!B_mode || B_mode.contains(location.mode)) && getTimeOk (B_timeStart, B_timeEnd) && getDayOk(B_day)) {
if (!B_luxSensors || (B_luxSensors.latestValue("illuminance") <= B_turnOnLux)){
    if (B_motion.latestValue("motion").contains("active")) {
        	log.debug("Motion Detected Running '${ScenarioNameB}'")
            def levelSetOn = B_levelDimOn ? B_levelDimOn : 100
			def levelSetOnColor = B_levelDimOnColor ? B_levelDimOnColor : 100
            def levelSetOff = B_levelDimOff ? B_levelDimOff : 0
            def levelSetOffColor = B_levelDimOffColor ? B_levelDimOffColor : 0

            if (B_calcOn && B_luxSensors) {
    			levelSetOn = (levelSetOn * (1-(B_luxSensors.latestValue("illuminance")/B_turnOnLux))) + levelSetOff
                if (levelSetOn > 100) {
               		levelSetOn = 100
               	}
    		}
            if (B_calcOnColor && B_luxSensors) {
    				levelSetOnColor = (levelSetOnColor * (1-(B_luxSensors.latestValue("illuminance")/B_turnOnLux))) + levelSetOffColor
                	if (levelSetOnColor > 100) {
               			levelSetOnColor = 100
               		}
    			}
        	B_dimmers?.setLevel(levelSetOn)
            setColoredLights(B_colorControls, B_color, levelSetOnColor)
        	B_switches?.on()
        	if (B_triggerOnce){
           		state.B_triggered = true
            	if (!B_turnOff) {
					runOnce (getMidnight(), midNightReset)
            	}
			}
			if (state.B_timerStart){
           		unschedule(delayTurnOffB)
       	   		state.B_timerStart = false
        	}	
}
else {
    	if (B_turnOff) {
			runIn(B_turnOff * 60, "delayTurnOffB")
        	state.B_timerStart = true
        }
        
        else {
        	B_switches?.off()
			def levelSetOff = B_levelDimOff ? B_levelDimOff : 0
    		B_dimmers?.setLevel(levelSetOff)
            def levelSetOffColor = B_levelDimOffColor ? B_levelDimOffColor : 0
            B_colorControls?.setLevel(levelSetOffColor)
            if (state.B_triggered) {
    			runOnce (getMidnight(), midNightReset)
    		}
        }
}
}
}
else{
log.debug("Motion outside of mode or time/day restriction.  Not running scenario.")
}
}
}

def delayTurnOffB(){
	B_switches?.off()
	def levelSetOff = B_levelDimOff ? B_levelDimOff : 0
    B_dimmers?.setLevel(levelSetOff)
    def levelSetOffColor = B_levelDimOffColor ? B_levelDimOffColor : 0
    B_colorControls?.setLevel(levelSetOffColor)
	state.B_timerStart = false
    if (state.B_triggered) {
    	runOnce (getMidnight(), midNightReset) 
	}
}

def onPressB(evt) {
if ((!B_mode || B_mode.contains(location.mode)) && getTimeOk (B_timeStart, B_timeEnd) && getDayOk(B_day)) {
if (!B_luxSensors || (B_luxSensors.latestValue("illuminance") <= B_turnOnLux)){
if ((!B_triggerOnce || (B_triggerOnce && !state.B_triggered)) && (!B_switchDisable || (B_switchDisable && !state.B_triggered))) {
	if (evt.physical){
    	state.B_triggered = true
        unschedule(delayTurnOffB)
        runOnce (getMidnight(), midNightReset)
        log.debug "Physical switch in '${ScenarioNameB}' pressed. Triggers for this scenario disabled."
	}
}
}
}
}

def onEventC(evt) {
if ((!C_triggerOnce || (C_triggerOnce && !state.C_triggered)) && (!C_switchDisable || (C_switchDisable && !state.C_triggered))) {
if ((!C_mode || C_mode.contains(location.mode)) && getTimeOk (C_timeStart, C_timeEnd) && getDayOk(C_day)) {
if (!C_luxSensors || (C_luxSensors.latestValue("illuminance") <= C_turnOnLux)){
    if (C_motion.latestValue("motion").contains("active")) {
	       	log.debug("Motion Detected Running '${ScenarioNameC}'")
            def levelSetOn = C_levelDimOn ? C_levelDimOn : 100
            def levelSetOnColor = C_levelDimOnColor ? C_levelDimOnColor : 100
            def levelSetOff = C_levelDimOff ? C_levelDimOff : 0
            def levelSetOffColor = C_levelDimOffColor ? C_levelDimOffColor : 0
            if (C_calcOn && C_luxSensors) {
    			levelSetOn = (levelSetOn * (1-(C_luxSensors.latestValue("illuminance")/C_turnOnLux))) + levelSetOff
                if (levelSetOn > 100) {
               		levelSetOn = 100
               	}
    		}
            if (C_calcOnColor && C_luxSensors) {
    			levelSetOnColor = (levelSetOnColor * (1-(C_luxSensors.latestValue("illuminance")/C_turnOnLux))) + levelSetOffColor
                if (levelSetOnColor > 100) {
               		levelSetOnColor = 100
               	}
			}

        	C_dimmers?.setLevel(levelSetOn)
            setColoredLights(C_colorControls, C_color, levelSetOnColor)
        	C_switches?.on()
        	if (C_triggerOnce){
           		state.C_triggered = true
            	if (!C_turnOff) {
					runOnce (getMidnight(), midNightReset)
            	}
			}
			if (state.C_timerStart){
           		unschedule(delayTurnOffC)
       	   		state.C_timerStart = false
        	}	
	}
	else {
        if (C_turnOff) {
			runIn(C_turnOff * 60, "delayTurnOffC")
        	state.C_timerStart = true
        }
        else {
            C_switches?.off()
			def levelSetOff = C_levelDimOff ? C_levelDimOff : 0
    		C_dimmers?.setLevel(levelSetOff)
        	def levelSetOffColor = C_levelDimOffColor ? C_levelDimOffColor : 0
			C_colorControls?.setLevel(levelSetOffColor)
        	if (state.C_triggered) {
    			runOnce (getMidnight(), midNightReset)
    		}
		}
	}
}
}
else{
log.debug("Motion outside of mode or time/day restriction.  Not running scenario.")
}
}
}

def delayTurnOffC(){
    C_switches?.off()
	def levelSetOff = C_levelDimOff ? C_levelDimOff : 0
    C_dimmers?.setLevel(levelSetOff)
    def levelSetOffColor = C_levelDimOffColor ? C_levelDimOffColor : 0
    C_colorControls?.setLevel(levelSetOffColor)
	state.C_timerStart = false
	if (state.C_triggered) {
    	runOnce (getMidnight(), midNightReset)
    }
}

def onPressC(evt) {
if ((!C_mode || C_mode.contains(location.mode)) && getTimeOk (C_timeStart, C_timeEnd) && getDayOk(C_day)) {
if (!C_luxSensors || (C_luxSensors.latestValue("illuminance") <= C_turnOnLux)){
if ((!C_triggerOnce || (C_triggerOnce && !state.C_triggered)) && (!C_switchDisable || (C_switchDisable && !state.C_triggered))) {
	if (evt.physical){
    	state.C_triggered = true
        unschedule(delayTurnOffC)
        runOnce (getMidnight(), midNightReset)
        log.debug "Physical switch in '${ScenarioNameC}' pressed. Triggers for this scenario disabled."
	}
}
}
}
}

def onEventD(evt) {
if ((!D_triggerOnce || (D_triggerOnce && !state.D_triggered)) && (!D_switchDisable || (D_switchDisable && !state.D_triggered))) {
if ((!D_mode || D_mode.contains(location.mode)) && getTimeOk (D_timeStart, D_timeEnd) && getDayOk(D_day)) {
if (!D_luxSensors || (D_luxSensors.latestValue("illuminance") <= D_turnOnLux)){
    if (D_motion.latestValue("motion").contains("active")) {
           	log.debug("Motion Detected Running '${ScenarioNameD}'")
            def levelSetOn = D_levelDimOn ? D_levelDimOn : 100
            def levelSetOnColor = D_levelDimOnColor ? D_levelDimOnColor : 100
            def levelSetOff = D_levelDimOff ? D_levelDimOff : 0
            def levelSetOffColor = D_levelDimOffColor ? D_levelDimOffColor : 0

			if (D_calcOn && D_luxSensors) {
    			levelSetOn = (levelSetOn * (1-(D_luxSensors.latestValue("illuminance")/D_turnOnLux))) + levelSetOff
                if (levelSetOn > 100) {
               		levelSetOn = 100
               	}
    		}
            if (D_calcOnColor && D_luxSensors) {
    			levelSetOnColor = (levelSetOnColor * (1-(D_luxSensors.latestValue("illuminance")/D_turnOnLux))) + levelSetOffColor
                if (levelSetOnColor > 100) {
               		levelSetOnColor = 100
               	}
    		}
        	D_dimmers?.setLevel(levelSetOn)
            setColoredLights(D_colorControls, D_color, levelSetOnColor)
        	D_switches?.on()
        	if (D_triggerOnce){
           		state.D_triggered = true
            	if (!D_turnOff) {
					runOnce (getMidnight(), midNightReset)
            	}
			}
			if (state.D_timerStart){
           		unschedule(delayTurnOffD)
       	   		state.D_timerStart = false
        	}	
}
else {
    	if (D_turnOff) {
		runIn(D_turnOff * 60, "delayTurnOffD")
        state.D_timerStart = true
        }
        else {
        D_switches?.off()
		def levelSetOff = D_levelDimOff ? D_levelDimOff : 0
    	D_dimmers?.setLevel(levelSetOff)
        def levelSetOffColor = D_levelDimOffColor ? D_levelDimOffColor : 0
        D_colorControls.setLevel(levelSetOffColor)
        	if (state.D_triggered) {
    			runOnce (getMidnight(), midNightReset)
    		}
        }
}
}
}
else{
log.debug("Motion outside of mode or time/day restriction.  Not running scenario.")
}
}
}

def delayTurnOffD(){
	D_switches?.off()
	def levelSetOff = D_levelDimOff ? D_levelDimOff : 0
    D_dimmers?.setLevel(levelSetOff)
    def levelSetOffColor = D_levelDimOffColor ? D_levelDimOffColor : 0
    D_colorControls.setLevel(levelSetOffColor)
	state.D_timerStart = false
	if (state.D_triggered) {
    	runOnce (getMidnight(), midNightReset)
    }
}

def onPressD(evt) {
if ((!D_mode || D_mode.contains(location.mode)) && getTimeOk (D_timeStart, D_timeEnd) && getDayOk(D_day)) {
if (!D_luxSensors || (D_luxSensors.latestValue("illuminance") <= D_turnOnLux)){
if ((!D_triggerOnce || (D_triggerOnce && !state.D_triggered)) && (!D_switchDisable || (D_switchDisable && !state.D_triggered))) {
	if (evt.physical){
    	state.C_triggered = true
        unschedule(delayTurnOffC)
        runOnce (getMidnight(), midNightReset)
        log.debug "Physical switch in '${ScenarioNameC}' pressed. Triggers for this scenario disabled."
	}
}
}
}
}

//Common Methods

def midNightReset() {
	state.A_triggered = false
    state.B_triggered = false
    state.C_triggered = false
    state.D_triggered = false
}

def greyOut(param){
	def result = param ? "complete" : ""
}

def greyedOutTime(start, end){
	def result = start || end ? "complete" : ""
}

def getTitle(scenario) {
	def title = scenario ? scenario : "Empty"
}

def getMidnight() {
	def midnightToday = timeToday("2000-01-01T23:59:59.999-0000", location.timeZone)
	midnightToday
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

def getLevelLabel(on, off, calcOn) {
	def levelLabel="'On' level: "
	if (!on) {
		on= 100
	}
	if (!off) {
		off = 0
    }
	if (calcOn) {
		levelLabel = levelLabel + "Between ${off}% and ${on}% based on lux"
	}
	else {
        if (on) {
    		levelLabel = levelLabel + "${on}%"
    	}
	}
	levelLabel = levelLabel + "\n'Off' level: ${off}%" 
    levelLabel
}

def getColorLabel(on, off, calcOn, color) {
    def levelLabel = color ? "Color: ${color}" : "Color: Soft White" 
    levelLabel += "\n'On' level: "
	if (!on) {
		on= 100
	}
	if (!off) {
		off = 0
    }
	if (calcOn) {
		levelLabel = levelLabel + "Between ${off}% and ${on}% based on lux"
	}
	else {
        if (on) {
    		levelLabel = levelLabel + "${on}%"
    	}
	}
	levelLabel = levelLabel + "\n'Off' level: ${off}%" 
    
    levelLabel
}

private setColoredLights(colorControls, color, lightLevel){
   	def chooseColor = color ? "${color}" : "Soft White"
    def hueColor = 0
	def saturationLevel = 100
	switch(chooseColor) {
		case "White":
			hueColor = 52
			saturationLevel = 19
			break;
		case "Daylight":
			hueColor = 52
			saturationLevel = 16
			break;
		case "Soft White":
			hueColor = 23
			saturationLevel = 56
			break;
		case "Warm White":
			hueColor = 13
			saturationLevel = 30 
			break;
		case "Blue":
			hueColor = 64
			break;
		case "Green":
			hueColor = 37
			break;
		case "Yellow":
			hueColor = 16
			break;
		case "Orange":
			hueColor = 8
			break;
		case "Purple":
			hueColor = 78
			break;
		case "Pink":
			hueColor = 87
			break;
		case "Red":
			hueColor = 100
			break;
	}
    def newValue = [hue: hueColor as Integer, saturation: saturationLevel as Integer, level: lightLevel as Integer]
    colorControls?.setColor(newValue)
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

page(name: "timeIntervalInputA", title: "Only during a certain time") {
		section {
			input "A_timeStart", "time", title: "Starting", required: false
			input "A_timeEnd", "time", title: "Ending", required: false
		}
}  
page(name: "timeIntervalInputB", title: "Only during a certain time") {
		section {
			input "B_timeStart", "time", title: "Starting", required: false
			input "B_timeEnd", "time", title: "Ending", required: false
		}
}  
page(name: "timeIntervalInputC", title: "Only during a certain time") {
		section {
			input "C_timeStart", "time", title: "Starting", required: false
			input "C_timeEnd", "time", title: "Ending", required: false
		}
}         

page(name: "timeIntervalInputD", title: "Only during a certain time") {
		section {
			input "D_timeStart", "time", title: "Starting", required: false
			input "D_timeEnd", "time", title: "Ending", required: false
		}
}

page(name: "levelInputA", title: "Set dimmers options...") {
		section {
			input name: "A_levelDimOn", type: "number", title: "On Level", multiple: false, required: false
        	input name: "A_levelDimOff", type: "number", title: "Off Level", multiple: false, required: false
			input name: "A_calcOn",type: "bool",title: "Calculate 'on' level via lux", defaultValue: false
        }
}

page(name: "levelInputB", title: "Set dimmers options...") {
		section {
			input name: "B_levelDimOn", type: "number", title: "On Level", multiple: false, required: false
        	input name: "B_levelDimOff", type: "number", title: "Off Level", multiple: false, required: false
            input name: "B_calcOn",type: "bool",title: "Calculate 'on' level via lux", defaultValue: false
        }
}

page(name: "levelInputC", title: "Set dimmers options...") {
		section {
			input name: "C_levelDimOn", type: "number", title: "On Level", multiple: false, required: false
        	input name: "C_levelDimOff", type: "number", title: "Off Level", multiple: false, required: false
            input name: "C_calcOn",type: "bool",title: "Calculate 'on' level via lux", defaultValue: false
        }
}

page(name: "levelInputD", title: "Set dimmers options...") {
		section {
			input name: "D_levelDimOn", type: "number", title: "On Level", multiple: false, required: false
        	input name: "D_levelDimOff", type: "number", title: "Off Level", multiple: false, required: false
            input name: "D_calcOn",type: "bool",title: "Calculate 'on' level via lux", defaultValue: false
        }
}

page(name: "colorInputA", title: "Set colored light options...") {
		section {
			input "A_color", "enum", title: "Choose a color", required: false, multiple:false, options: [
					["Soft White":"Soft White"],
					["White":"White - Concentrate"],
					["Daylight":"Daylight - Energize"],
					["Warm White":"Warm White - Relax"],
					"Red","Green","Blue","Yellow","Orange","Purple","Pink"]
            input "A_levelDimOnColor", "number", title: "On Level", multiple: false, required: false
        	input "A_levelDimOffColor", "number", title: "Off Level", multiple: false, required: false
			input "A_calcOnColor", "bool", title: "Calculate 'on' level via lux", defaultValue: false
        }
}

page(name: "colorInputB", title: "Set colored light options...") {
		section {
			input "B_color", "enum", title: "Choose a color", required: false, multiple:false, options: [
					["Soft White":"Soft White"],
					["White":"White - Concentrate"],
					["Daylight":"Daylight - Energize"],
					["Warm White":"Warm White - Relax"],
					"Red","Green","Blue","Yellow","Orange","Purple","Pink"]
            input "B_levelDimOnColor", "number", title: "On Level", multiple: false, required: false
        	input "B_levelDimOffColor", "number", title: "Off Level", multiple: false, required: false
			input "B_calcOnColor", "bool",title: "Calculate 'on' level via lux", defaultValue: false
        }
}

page(name: "colorInputC", title: "Set colored light options...") {
		section {
			input "C_color", "enum", title: "Choose a color", required: false, multiple:false, options: [
					["Soft White":"Soft White"],
					["White":"White - Concentrate"],
					["Daylight":"Daylight - Energize"],
					["Warm White":"Warm White - Relax"],
					"Red","Green","Blue","Yellow","Orange","Purple","Pink"]
            input "C_levelDimOnColor", "number", title: "On Level", multiple: false, required: false
        	input "C_levelDimOffColor", "number", title: "Off Level", multiple: false, required: false
			input "C_calcOnColor", "bool",title: "Calculate 'on' level via lux", defaultValue: false
        }
}

page(name: "colorInputD", title: "Set colored light options...") {
		section {
			input "D_color", "enum", title: "Choose a color", required: false, multiple:false, options: [
					["Soft White":"Soft White"],
					["White":"White - Concentrate"],
					["Daylight":"Daylight - Energize"],
					["Warm White":"Warm White - Relax"],
					"Red","Green","Blue","Yellow","Orange","Purple","Pink"]
            input "D_levelDimOnColor", "number", title: "On Level", multiple: false, required: false
        	input "D_levelDimOffColor", "number", title: "Off Level", multiple: false, required: false
			input "D_calcOnColor", "bool",title: "Calculate 'on' level via lux", defaultValue: false
        }
}

//Version/Copyright/Information/Help

private def textAppName() {
	def text = "Smart Room Lighting and Dimming"
}	

private def textVersion() {
    def text = "Version 1.1.0 (07/04/2015)"
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
        "Within each scenario you can select motion sensors to control a set of lights. " +
        "Each scenario can control dimmers or switches and can also be restricted " +
        "to modes or between certain times and turned off after motion " +
        "motion stops. Scenarios can also be limited to running once " +
        "or to stop running if the physical switches are turned off."+
        "\n\nOn the dimmer options page, enter the 'on' or 'off' levels for the dimmers. You can choose to have the " +
        "dimmers' level calculated between the 'on' and 'off' settings " +
        "based on the current lux value. In other words, as it gets " +
        "darker, the brighter the light level will be when motion is sensed."
}

