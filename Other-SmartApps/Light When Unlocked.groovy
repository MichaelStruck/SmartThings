/**
 *  Light When Unlocked
 *
 *	Version 1.0 3/17/15
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
    name: "Light When Unlocked",
    namespace: "MichaelStruck",
    author: "Michael Struck",
    description: "Will turn on certain lights when a door is unlocked and luminosity is below a certain level.",
    category: "Convenience",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {

    section("When this lock is unlocked...") {
		input "lock1","capability.lock", title: "Lock", multiple: false
	}
	section("Turn on these lights/switches") {
		input "lightsOn", "capability.switch", multiple: true, title: "Lights/Switches", required: true
	}
    section("Use this light sensor to determine when it is dark (enter 10,000 to have the light come on regardless of lighting)") {
		input "lightSensor", "capability.illuminanceMeasurement", title: "Light Sensor", required: false, multiple: false
        input "luxOn", "number", title: "Lux Threshold", required: true, description:0
	}
}

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
	subscribe(lock1, "lock", eventHandler)
}

def eventHandler(evt) {
        def oktoFire=true
        if (lightSensor.currentIlluminance > luxOn){
			oktoFire=false
        }
        if (evt.value == "unlocked" && oktoFire){
        	lightsOn.on()
        } 
}
