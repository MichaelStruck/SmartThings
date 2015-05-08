/**
 *  Light When Unlocked
 *
 *	Version 1.1 3/22/15 -  Adds a timer to turn off the lights after a certain amount of time
 *	Version 1.11 3/29/15 Fixes a small scheduling issue
 *	Version 1.12 4/9/15	Added an options section
 *	Version 1.13 4/22/15 Added a door open option in addition to just a lock
 *
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
    iconUrl: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Light-When-Unlocked/LockLight.png",
    iconX2Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Light-When-Unlocked/LockLight@2x.png",
    iconX3Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Light-When-Unlocked/LockLight@2x.png")

preferences {
	page(name: "getPref")
}

def getPref() {
    dynamicPage(name: "getPref", install:true, uninstall: true) {
    	section("When this lock is unlocked...") {
			input "lock1","capability.lock", title: "Lock", multiple: false, required: false
		}
        section("Or when these door(s) open...") {
        	input "contact1", "capability.contactSensor", title: "Door Sensor(s)", multiple: true, required: false
        }
		section("Turn on these lights/switches...") {
			input "lightsOn", "capability.switch", multiple: true, title: "Lights/Switches", required: true
		}
    	section("Use this light sensor to determine when it is dark (enter 10,000 to have the light come on regardless of lighting)") {
			input "lightSensor", "capability.illuminanceMeasurement", title: "Light Sensor", required: false, multiple: false
       		input "luxOn", "number", title: "Lux Threshold", required: false, description:0 
		}
   		section("Turn off light(s) after this many minutes (Enter 0 to not set timer)..."){
			input "delayMinutes", "number", title: "Minutes"
        	input "onClose","bool", title: "Start timer only after door(s) lock/close?"
        }
		section([mobileOnly:true], "Options") {
			label(title: "Assign a name", required: false, defaultValue: "Light When Unlocked")
            mode title: "Set for specific mode(s)", required: false
		}
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
	subscribe(lock1, "lock.unlocked", eventHandler)
    subscribe(contact1, "contact.open", eventHandler)
    if (onClose && delayMinutes) {
    	subscribe(lock1, "lock.locked", startTimer)
        subscribe(contact1, "contact.closed", startTimer)
    }
}

def eventHandler(evt) {
	def oktoFire=true
    if (lightSensor && lightSensor.currentIlluminance > luxOn){
		oktoFire=false
    }
    if (oktoFire){
  		lightsOn.on()
        if (delayMinutes && !onClose) {
        	startTimer()
    	}	
	}
}

def turnOffAfterDelay() {
	log.debug "Turning off lights"
	lightsOn.off()
}

def startTimer(evt) {
		runIn(delayMinutes * 60, turnOffAfterDelay, [overwrite: true])
}
