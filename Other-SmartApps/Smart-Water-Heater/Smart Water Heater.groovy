/**
 *  Smart Water Heater
 *  Version 1.01 3/17/2015
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
    name: "Smart Water Heater",
    namespace: "MichaelStruck",
    author: "Michael Struck",
    description: "Allows for setting up schedules for turning on and off the power to a water heater. ",
    category: "Green Living",
    iconUrl: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Smart-Water-Heater/WHApp.png",
    iconX2Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Smart-Water-Heater/WHApp@2x.png",
    iconX3Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Smart-Water-Heater/WHApp@2x.png")


preferences {
    	section("Select water heater switch..."){
		input "switchWH", title: "Switch", "capability.switch", multiple: false
	}
    	section("Daytime Schedule..."){
		input "timeOffDay", title: "Time to turn off", "time"
        	input "timeOnDay", title: "Time to turn back on", "time"
        	input "exceptionDay", "capability.presenceSensor", title: "Remain on if any of these people are home", multiple: true, required: false
        	input "weekendRun", "enum", title: "Run daytime schedule during the weekend?", multiple: false, required: true, options: ["Yes", "No"]
	}
    	section("Nighttime Schedule..."){
		input "timeOffNight", title: "Time to turn off", "time"
        	input "timeOnNight", title: "Time to turn back on", "time"
	}
}

def installed() {
	log.debug "Installed with settings: ${settings}"
	init()
}

def updated(settings) {
	unschedule()
	log.debug "Updated with settings: ${settings}"
    	init()
}

def init () {
	schedule(timeOffDay, "turnOffSwitchDay")
	schedule(timeOnDay, "turnOnSwitch")
    	schedule(timeOffNight, "turnOffSwitch")
	schedule(timeOnNight, "turnOnSwitch")
}

def turnOffSwitchDay() {
	def calendar = Calendar.getInstance()
	calendar.setTimeZone(location.timeZone)
	def today = calendar.get(Calendar.DAY_OF_WEEK)
	def runToday = true
	if (weekendRun != "Yes" && (today == 1 || today == 7)) {
		runToday = false
    }   

    	if (runToday && everyoneGone()) {
    		turnOffSwitch()
   		} else {
		log.debug "It is the weekend or presense is detected so the water heater will remain on."
    	}    
}
    
def turnOffSwitch() {
    	switchWH.off()
        log.debug "Water heater turned off."
}
    
def turnOnSwitch() {
    	switchWH.on()
        log.debug "Water heater turned on."
}

private everyoneGone() {
    	def result = true
	if (exceptionDay) {
        	for (i in exceptionDay) {
           		if (i.currentPresence == "present") {
               		result = false
        		break
            		}
        	}
	}
	return result
}
