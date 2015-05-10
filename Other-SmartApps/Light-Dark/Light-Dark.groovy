/**
 *  Light > Dark
 *  Version 1.04 5/9/15
 *
 *	1.01 Added a verify so they event has to trip trice in a row to do the action.
 *	1.02 Added custom icon
 *  	1.03 Revision to interface for better flow
 *	1.04 Added dimmer switches/levels, reorganized interface and added time restrictions options
 *
 *
 *  Using code from SmartThings Light Up The Night App and the Sunrise/Sunset app
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
 *  Turn specific switched on when it gets dark. Can also turn off specific switches when it becomes light again based on brightness from a connected light sensor.
 *
 */
definition(
    name: "Light > Dark",
    namespace: "MichaelStruck",
    author: "Michael Struck",
    description: "Turn specific switches on when it gets dark and optionally changes the mode. Will also turn off specific switches and optionally change modes when it becomes light again based on brightness from a connected light sensor.",
    category: "Convenience",
    iconUrl: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Light-Dark/LockDark.png",
    iconX2Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Light-Dark/LockDark@2x.png",
    iconX3Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Light-Dark/LockDark@2x.png"
    )

preferences {
	page(name: "getPref")
}

def getPref() {
    dynamicPage(name: "getPref", install:true, uninstall: true) {
    	section("Monitor the luminosity and set brightness thresholds...") {
			input "lightSensor", "capability.illuminanceMeasurement", title: "Light Sensor"
			input "luxOn", "number", title: "Lower lux threshold (default=100)", required: false, description:100
            input "luxOff", "number", title: "Upper lux threshold", required: false
        }
		section("Turn on lights/set mode when brightness below lower lux threshold...") {
			input "lightsOn", "capability.switch", multiple: true, title: "Lights/Switches", required: false
			input "dimmersOn","capability.switchLevel", multiple: true, required: false, title: "Dimmers"
            input "dimmerLevelOn", "enum", multiple:false, required: false, options: [[10:"10%"],[20:"20%"],[30:"30%"],[40:"40%"],[50:"50%"],[60:"60%"],[70:"70%"],[80:"80%"],[90:"90%"],[100:"100%"]], title: "Turn on dimmers to this level (100% default)"
        	input "onMode", "mode", title: "Change mode to?", required: false
		}
    	section("Optionally, turn off lights/set mode when brightness above upper lux threshold...") {
			input "lightsOff", "capability.switch", multiple: true, title: "Lights/Switches", required: false
			input "dimmersOff","capability.switchLevel", multiple: true, title: "Dimmers", required: false
            input "dimmerLevelOff", "enum", multiple:false, required: false, options: [[0:"Off"],[10:"10%"],[20:"20%"],[30:"30%"],[40:"40%"],[50:"50%"],[60:"60%"],[70:"70%"],[80:"80%"],[90:"90%"],[100:"100%"]], title: "Turn off dimmers to this level (0% default)"
        	input "offMode", "mode", title: "Change mode to?", required: false
        }
        section([mobileOnly:true], "Options") {
			label(title: "Assign a name", required: false, defaultValue: "Light > Dark")
		    href "timeIntervalInput", title: "Set for specific times", description: getTimeLabel(timeStart, timeEnd), state: greyedOutTime(timeStart, timeEnd), refreshAfterSelection:true
            mode title: "Set for specific mode(s)", required: false
        }
	}
}

def installed() {
	log.debug "Installed with settings: ${settings}"
    init()
}

def updated() {
	log.debug "Updated with settings: ${settings}"
    unsubscribe()
	init()
}

def init(){
	state.dimLevelOn = dimmerLevelOn as Integer
	state.dimLevelOff = dimmerLevelOff as Integer
    	state.lumOn = luxOn
    	state.lumOff = luxOff
    
    if (!luxOn) {
       	state.lumOn=100
    } 
    if (!luxOff) {
       	state.lumOff = state.lumOn
    }
   	if (!state.dimLevelOn) {
		state.dimLevelOn = 100
   	}
   	if (!state.dimLevelOff) {
   		state.dimLevelOff = 0
   	}
    if (lightSensor.latestValue("illuminance") < state.lumOn) {
    	state.lastStatus = "on"
    }
    else {
    	state.lastStatus = "off"
    }
    subscribe(lightSensor, "illuminance", illuminanceHandler)
}
//Handlers
def illuminanceHandler(evt) {
  
    if (getTimeOk()) {
        if (state.lastStatus == "off" && evt.integerValue < state.lumOn) {
           	lightsOn?.on()
            dimmersOn?.setLevel(state.dimLevelOn)
        	state.lastStatus = "on"
 	        changeMode(onMode)
        } 
		if (state.lastStatus  == "on" && evt.integerValue > state.lumOff) {
           	lightsOff?.off()
           	dimmersOff?.setLevel(state.dimLevelOff)
        	state.lastStatus = "off"
            changeMode(offMode)
		} 
	}
}

def changeMode(newMode) {
	if (newMode && location.mode != newMode) {
		if (location.modes?.find{it.name == newMode}) {
			setLocationMode(newMode)
		}
		else {
			log.debug "Unable to change to undefined mode '${newMode}'"
		}
	}
}

//Common Methods

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

def greyedOutTime(start, end){
	def result = ""
    if (start || end) {
    	result = "complete"	
    }
    result
}

private hhmm(time, fmt = "h:mm a")
{
	def t = timeToday(time, location.timeZone)
	def f = new java.text.SimpleDateFormat(fmt)
	f.setTimeZone(location.timeZone ?: timeZone(time))
	f.format(t)
}

private getTimeOk() {
	def result = true
	if (timeStart && timeEnd) {
		def currTime = now()
		def start = timeToday(timeStart).time
		def stop = timeToday(timeEnd).time
		result = start < stop ? currTime >= start && currTime <= stop : currTime <= stop || currTime >= start || currTime <= stop
	}
	result
}

page(name: "timeIntervalInput", title: "Run only during a certain time", refreshAfterSelection:true) {
		section {
			input "timeStart", "time", title: "Starting", required: false, refreshAfterSelection:true
			input "timeEnd", "time", title: "Ending", required: false, refreshAfterSelection:true
		}
        }
