/**
 *  Light > Dark
 *  Version 1.0 3/6/15
 *
 *  Using code from SmartThings Light Up The Night App and the Sunrise/Sunset app
 *  Copyright 2015 Michael Struck
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
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
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Meta/light_outlet-luminance.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Meta/light_outlet-luminance@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Meta/light_outlet-luminance@2x.png")

preferences {

    section("Monitor the luminosity...") {
		input "lightSensor", "capability.illuminanceMeasurement", title: "Light Sensor"
	}
	section("Turn on lights/set mode when brightness below specific luminosity") {
		input "lightsOn", "capability.switch", multiple: true, title: "Lights/Switches", required: true
		input "luxOn", "number", title: "Lux Setting", required: true, description:30
        input "onMode", "mode", title: "Change mode to?", required: false
	}
    section("Optionally, turn off lights/set mode when brightness above specific luminosity") {
		input "lightsOff", "capability.switch", multiple: true, title: "Lights/Switches", required: false
		input "luxOff", "number", title: "Lux Setting", required: false, description:50
        input "offMode", "mode", title: "Change mode to?", required: false
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
	if (lastStatus != "on" && evt.integerValue < luxOn) {
		lightsOn.on()
        state.lastStatus = "on"
        changeMode(onMode)
	}
	else if (lightsOff && lastStatus != "off" && evt.integerValue > luxOff) {
		lightsOff.off()
        changeMode(offMode)
        state.lastStatus = "off"
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
