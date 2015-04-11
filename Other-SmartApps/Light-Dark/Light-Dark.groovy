/**
 *  Light > Dark
 *  Version 1.02 3/21/15
 *
 *	1.01 Added a verify so they event has to trip trice in a row to do the action.
 *	1.02 Added custom icon
 *  1.03 Revision to interface for better flow
 *
 *
 *	Using code from SmartThings Light Up The Night App and the Sunrise/Sunset app
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
    iconX3Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Light-Dark/LockDark@2x.png")

preferences {
	page(name: "getPref")
}

def getPref() {
    dynamicPage(name: "getPref", install:true, uninstall: true) {
    	section("Monitor the luminosity...") {
			input "lightSensor", "capability.illuminanceMeasurement", title: "Light Sensor"
		}
		section("Turn on lights/set mode when brightness below specific luminosity...") {
			input "lightsOn", "capability.switch", multiple: true, title: "Lights/Switches", required: true
			input "luxOn", "number", title: "Lux Setting", required: true, description:30
        	input "onMode", "mode", title: "Change mode to?", required: false
		}
    	section("Optionally, turn off lights/set mode when brightness above specific luminosity...") {
			input "lightsOff", "capability.switch", multiple: true, title: "Lights/Switches", required: false
			input "luxOff", "number", title: "Lux Setting", required: false, description:50
        	input "offMode", "mode", title: "Change mode to?", required: false
        }
        section([mobileOnly:true], "Options") {
			label(title: "Assign a name", required: false, defaultValue: "Light > Dark")
		    mode title: "Set for specific mode(s)", required: false
        }
	}
}

def installed() {
	log.debug "Installed with settings: ${settings}"
    subscribe(lightSensor, "illuminance", illuminanceHandler)
}

def updated() {
	log.debug "Updated with settings: ${settings}"
    unsubscribe()
	subscribe(lightSensor, "illuminance", illuminanceHandler)
}

def illuminanceHandler(evt) {
	def lastStatus = state.lastStatus
    def luxVerify = state.luxVerify
    log.debug "$luxVerify"
	if (lastStatus != "on" && evt.integerValue < luxOn) {
		// Prevent false positives by having the sensor trip twice before firing event 
		if (luxVerify == "TurnOn") {
        	lightsOn.on()
        	state.lastStatus = "on"
 	        changeMode(onMode)
        } 
        else {
        	state.luxVerify = "TurnOn"
        }
	}
	else if (lightsOff && lastStatus != "off" && evt.integerValue > luxOff) {
		// Prevent false positives by having the sensor trip twice before firing event 
		if (luxVerify == "TurnOff") {
			lightsOff.off()
        	changeMode(offMode)
        	state.lastStatus = "off"
		} 
        else {
        	state.luxVerify = "TurnOff"
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

