/**
 *  Smart Water Heater
 *  Version 1.2 3/30/2015
 *
 *  Version 1.01-Initial release
 *  Version 1.1 added a function to turn water heater back on if someone comes home early.
 *  Version 1.11 Revised the interface for better flow
 *  Version 1.2 Revised the interface even more for better flow
 *
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
    name: "Smart Water Heater",
    namespace: "MichaelStruck",
    author: "Michael Struck",
    description: "Allows for setting up schedules for turning on and off the power to a water heater. ",
    category: "Green Living",
    iconUrl: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Smart-Water-Heater/WHApp.png",
    iconX2Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Smart-Water-Heater/WHApp@2x.png",
    iconX3Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Smart-Water-Heater/WHApp@2x.png")


preferences {
    page(name: "mainPage")
    page(name: "daySchedule")
    page(name: "nightSchedule")
}

def mainPage() {
	dynamicPage(name: "mainPage", title: "", install: true, uninstall: true) {
    	section("Select water heater switch..."){
			input "switchWH", title: "Switch", "capability.switch", multiple: false
		}
    	section("Daytime Options"){
        	href(name: "toDaySchedule", page: "daySchedule", title: "Schedule", description: dayDescription(), state: "complete")
        	input "presence1", "capability.presenceSensor", title: "Remain on if any of these people are home", multiple: true, required: false
        	input "exceptionArrive", "bool", title: "Turn on when someone above arrives home early", defaultValue: "true"
        	input "weekendRun", "bool", title: "Run daytime schedule during the weekend", defaultValue: "false"
		}
    	section("Nighttime Options"){
    		href(name: "toNightSchedule", page: "nightSchedule", title: "Schedule", description: nightDescription(), state: "complete")
    	}
    	section([mobileOnly:true]) {
			label(title: "Assign a name", required: false, defaultValue: "Smart Water Heater")
            mode title: "Set for specific mode(s)", required: false
		}
    }
}

def daySchedule() {
	dynamicPage(name: "daySchedule", title: "Daytime Schedule") {
		section {
			input "timeOffDay", title: "Time to turn off", "time"
        	input "timeOnDay", title: "Time to turn back on", "time"
		}
	}
}

def nightSchedule() {
	dynamicPage(name: "nightSchedule", title: "Daytime Schedule") {
		section {
			input "timeOffNight", title: "Time to turn off", "time"
        	input "timeOnNight", title: "Time to turn back on", "time"
		}
	}
}

def installed() {
   log.debug "Installed with settings: ${settings}"
   init()
}

def updated() {
    unsubscribe()
    unschedule()
    log.debug "Updated with settings: ${settings}"
    init()
}

def init () {
    if (exceptionArrive){
    	subscribe(presence1, "presence", homeEarly)
    }
    schedule(timeOffDay, "turnOffDay")
    schedule(timeOnDay, "turnOnDay")
    schedule(timeOffNight, "turnOffNight")
    schedule(timeOnNight, "turnOnNight")
}

def turnOffDay() {
    def calendar = Calendar.getInstance()
    calendar.setTimeZone(location.timeZone)
    def today = calendar.get(Calendar.DAY_OF_WEEK)
    def runToday = true
    if (!weekendRun && (today == 1 || today == 7)) {
    	runToday = false
    }   

    if (runToday && everyoneGone()) {
    	state.status="Day off"
        turnOffSwitch()
    } else {
        log.debug "It is the weekend or presense is detected so the water heater will remain on."
    }    
}

def turnOnDay() {
    state.status="Day on"
    turnOnSwitch()
}

def turnOnNight() {
    state.status="Night on"
    turnOnSwitch()
}

def turnOffNight() {
    state.status="Night off"
    turnOffSwitch()
}

def turnOffSwitch() {
    switchWH.off()
    log.debug "Water heater turned off."
}
    
def turnOnSwitch() {
   switchWH.on()
   log.debug "Water heater turned on."
}

def homeEarly(evt) {
    def someoneArrived = presence1.find{it.currentPresence == "present"}
    if (someoneArrived && state.status=="Day off"){
    	log.debug "Presence detected, turning water heater back on"
    	turnOnDay()
    }
}

private everyoneGone() {
    def result = true
    def someoneHome = presence1.find{it.currentPresence == "present"}
    if (someoneHome) {
    	result = false
    }
    return result
}

def nightDescription() {
    def title = ""
    if (timeOffNight) {
    	title += "Turn off at ${humanReadableTime(timeOffNight)} then turn back on at ${humanReadableTime(timeOnNight)}"
    }
    return title
}

def dayDescription() {
    def title = ""
    if (timeOffDay) {
    	title += "Turn off at ${humanReadableTime(timeOffDay)} then turn back on at ${humanReadableTime(timeOnDay)}"
    }
    return title
}

public smartThingsDateFormat() { "yyyy-MM-dd'T'HH:mm:ss.SSSZ" }

public humanReadableTime(dateTxt) {
	new Date().parse(smartThingsDateFormat(), dateTxt).format("h:mm a", timeZone(dateTxt))
}
