/**
 *  Smart Water Heater
 *  Version 1.0 3/14/2015
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
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


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
	if (!weekendRun) {
    	weekendRun = "No"
    }
    def calendar = Calendar.getInstance()
	calendar.setTimeZone(location.timeZone)
	def today = calendar.get(Calendar.DAY_OF_WEEK)
	def runToday = true
	if (today != "1" && today != "7") {
        runToday = true
        } else {
    		if (weekendRun == "Yes") {
        		runToday = false
            } else {
        		runToday = true
    		}    
    }
    def everyoneGone = true
    if (exceptionDay) {
        for (i in exceptionDay) {
           	if (i.currentPresence == "present") {
               	everyoneGone = false
                break
            }
        }
    }
	
    if (runToday && everyoneGone) {
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
