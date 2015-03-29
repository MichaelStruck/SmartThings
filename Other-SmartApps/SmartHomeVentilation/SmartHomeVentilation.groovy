/**
 *  Smart Home Ventilation 3/27/15
 *  Version 1.00 3/28/15
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
    name: "Smart Home Ventilation",
    namespace: "MichaelStruck",
    author: "Michael Struck",
    description: "Allows for setting up schedules for turning on and off home ventilation fan.",
    category: "Convenience",
    iconUrl: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/SmartHomeVentilation/HomeVent.png",
    iconX2Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/SmartHomeVentilation/HomeVent@2x.png",
    iconX3Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/SmartHomeVentilation/HomeVent@2x.png")


preferences {
    section("Select home ventilation switch..."){
		input "switch1", title: "Switch", "capability.switch", multiple: false
	}
    section("Daytime Schedule 1..."){
		input "timeOnDay1", title: "Time to turn on", "time"
        input "timeOffDay1", title: "Time to turn off", "time"
	}
    section("Daytime Schedule 2..."){
		input "timeOnDay2", title: "Time to turn on", "time", required: false
        input "timeOffDay2", title: "Time to turn off", "time", required: false
	}
    section("Nighttime Schedule 1..."){
        input "timeOnNight1", title: "Time to turn on", "time", required: false
        input "timeOffNight1", title: "Time to turn off", "time", required: false
	}
    section("Nighttime Schedule 2..."){
        input "timeOnNight2", title: "Time to turn on", "time", required: false
        input "timeOffNight2", title: "Time to turn off", "time", required: false
	}
}

def installed() {
	log.debug "Installed with settings: ${settings}"
	init()
}

def updated() {
    unschedule()
    log.debug "Updated with settings: ${settings}"
    init()
}

def init () {
    schedule(timeOnDay1, "turnOnDay1")
    schedule(timeOffDay1, "turnOffDay1")
    
    if (timeOnDay2) {
        schedule(timeOnDay2, "turnOnDay2")
    	schedule(timeOffDay2, "turnOffDay2")
    }
    if (timeOnNight1){
    	schedule(timeOnNight1, "turnOnNight1")	
    	schedule(timeOffNight1, "turnOffNight1")
    }
    if (timeOnNight2){
    	schedule(timeOnNight2, "turnOnNight2")	
    	schedule(timeOffNight2, "turnOffNight2")
    }
}

def turnOffDay1() {
    turnOffSwitch()
}

def turnOffDay2() {
    turnOffSwitch()
}

def turnOnDay1() {
    turnOnSwitch()
}

def turnOnDay2() {
    turnOnSwitch()
}

def turnOnNight1() {
    turnOnSwitch()
}

def turnOnNight2() {
    turnOnSwitch()
}

def turnOffNight1() {
    turnOffSwitch()
}

def turnOffNight2() {
    turnOffSwitch()
}

def turnOffSwitch() {
    	switch1.off()
        log.debug "Home ventilation turned off."
}
    
def turnOnSwitch() {
    	switch1.on()
        log.debug "Home ventilation turned on."
}
