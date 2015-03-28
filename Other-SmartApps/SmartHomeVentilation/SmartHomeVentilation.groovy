/**
 *  Smart Home Ventilation 3/27/15
 *  Version 1.00
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
    iconUrl: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/SmartHomeVentilation/HomeVent.png",
    iconX2Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/SmartHomeVentilation/HomeVent@2x.png",
    iconX3Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/SmartHomeVentilation/HomeVent@2x.png")


preferences {
    section("Select home ventilation switch..."){
		input "switch1", title: "Switch", "capability.switch", multiple: false
	}
    section("Daytime Schedule..."){
		input "timeOnDay", title: "Time to turn on", "time"
        input "timeOffDay", title: "Time to turn off", "time"
	}
    section("Nighttime Schedule..."){
        input "timeOnNight", title: "Time to turn on", "time"
        input "timeOffNight", title: "Time to turn off", "time"
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
    schedule(timeOnDay, "turnOnDay")
    schedule(timeOffDay, "turnOffDay")
    schedule(timeOnNight, "turnOnNight")	
    schedule(timeOffNight, "turnOffNight")
}

def turnOffDay() {
    turnOffSwitch()
}

def turnOnDay() {
    turnOnSwitch()
}

def turnOnNight() {
    turnOnSwitch()
}

def turnOffNight() {
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
