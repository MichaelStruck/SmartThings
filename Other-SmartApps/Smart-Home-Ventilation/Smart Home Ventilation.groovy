/**
 *  Smart Home Ventilation 3/27/15
 *  Version 1.00 3/28/15
 *  Version 1.11 4/1/15-increased the number of options to control the fan
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
    description: "Allows for setting up schedules for turning on and off a home ventilation fan.",
    category: "Convenience",
    iconUrl: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/SmartHomeVentilation/HomeVent.png",
    iconX2Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/SmartHomeVentilation/HomeVent@2x.png",
    iconX3Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/SmartHomeVentilation/HomeVent@2x.png")

preferences {
	page(name: "mainPage")
    page(name: "dailySchedule")
    page(name: "weekendSchedule")
}

def mainPage() {
	dynamicPage(name: "mainPage", title: "", install: true, uninstall: true) {
    
    	section("Select home ventilation switch..."){
			input "switch1", title: "Switch", "capability.switch", multiple: false
		}
           
        section {
        	href(name: "toDailySchedule", page: "dailySchedule", title: "Daily Schedule", description: DailyDesc(), state: "complete")
        }
        section {
        	input "runWeekend", title: "Run Different Schedule on Weekend", "bool", defaultValue: "false"
            href(name: "toWeekendSchedule", page: "weekendSchedule", title: "Weekend Schedule", description: WEDesc())
        }
		section([mobileOnly:true]) {
			label(title: "Assign a name", required: false, defaultValue: "Smart Home Ventilation")
            mode title: "Set for specific mode(s)", required: false
		}
    }
}

def dailySchedule() {
	dynamicPage(name: "dailySchedule", title: "Daily Schedule-Be sure to list the schedules in chronological order for proper execution.") {
    	
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
}

def weekendSchedule() {
	dynamicPage(name: "weekendSchedule", title: "Weekend Schedule-Be sure to list the schedules in chronological order for proper execution.") {
    	
    	section("Daytime Schedule 1..."){
			input "timeOnWEDay1", title: "Time to turn on", "time", required: false
        	input "timeOffWEDay1", title: "Time to turn off", "time", required: false
		}
    	section("Daytime Schedule 2..."){
			input "timeOnWEDay2", title: "Time to turn on", "time", required: false
        	input "timeOffWEDay2", title: "Time to turn off", "time", required: false
		}
    	section("Nighttime Schedule 1..."){
        	input "timeOnWENight1", title: "Time to turn on", "time", required: false
        	input "timeOffWENight1", title: "Time to turn off", "time", required: false
		}
    	section("Nighttime Schedule 2..."){
        	input "timeOnWENight2", title: "Time to turn on", "time", required: false
        	input "timeOffWENight2", title: "Time to turn off", "time", required: false
		}
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
    if (dayOfWeek() != 1 || dayOfWeek() < 6 || !runWeekend) {
    	startWeekday()
 	} else if ((dayOfWeek() == 7 || dayOfWeek() == 1) && runWeekend) {
    	startWeekend()
    }
}

//-------Weekday Handler

def startWeekday() {
    if (timeOnDay1) {
    	schedule(timeOnDay1, "turnOnSwitch")
    	schedule(timeOffDay1, "turnOffWeekday1")
    } else {
     	turnOffWeekday1()
	}
}

def turnOffWeekday1() {
    turnOffSwitch()
    if (timeOnDay2) {
    	schedule(timeOnDay2, "turnOnSwitch")
    	schedule(timeOffDay2, "turnOffWeekday2")
    } else {
     	turnOffWeekday2()
	}
}
def turnOffWeekday2() {
    turnOffSwitch()
    if (timeOnNight1) {
    	schedule(timeOnNight1, "turnOnSwitch")
    	schedule(timeOffNight1, "turnOffWeekday3")
    } else {
     	turnOffWeekday3()
	}
}
def turnOffWeekday3() {
    turnOffSwitch()
    if (timeOnNight2) {
    	schedule(timeOnNight2, "turnOnSwitch")
    	schedule(timeOffNight2, "nextDay")
   } else {
     	nextDay()
	}
}

//-------Weekend Handlers 


def startWeekend() {
    if (timeOnWEDay1) {
    	schedule(timeOnWEDay1, "turnOnSwitch")
    	schedule(timeOffWEDay1, "turnOffWeekend1")
    } else {
     	turnOffWeekday1()
	}
}

def turnOffWeekend1() {
    turnOffSwitch()
    if (timeOnWEDay2) {
    	schedule(timeOnWEDay2, "turnOnSwitch")
    	schedule(timeOffWEDay2, "turnOffWeekend2")
    } else {
     	turnOffWeekday2()
	}
}

def turnOffWeekend2() {
    turnOffSwitch()
    if (timeOnWENight1) {
    	schedule(timeOnWENight1, "turnOnSwitch")
    	schedule(timeOffWENight1, "turnOffWeekend3")
    } else {
     	turnOffWeekday3()
	}
}

def turnOffWeekend3() {
    turnOffSwitch()
    if (timeOnWENight2) {
    	schedule(timeOnWENight2, "turnOnSwitch")
    	schedule(timeOffWENight2, "nextDay")
    } else {
     	nextDay()
	}
}

//-----

def nextDay() {
    turnOffSwitch()
    init()
}


def turnOffSwitch() {
    unschedule()
    if (switch1.currentValue("switch")=="on"){
    	switch1.off()
   		log.debug "Home ventilation turned off."
    }
}
    
def turnOnSwitch() {
    switch1.on()
    log.debug "Home ventilation turned on."
}

def DailyDesc() {
	def title = ""
    if (timeOnDay1){
    	title += "Day Schedule 1: ${humanReadableTime(timeOnDay1)} to ${humanReadableTime(timeOffDay1)}"
    }
    if (timeOnDay2) {
    	title += "\nDay Schedule 2: ${humanReadableTime(timeOnDay2)} to ${humanReadableTime(timeOffDay2)}"
    }
    if (timeOnNight1) {
    	title += "\nNight Schedule 1: ${humanReadableTime(timeOnNight1)} to ${humanReadableTime(timeOffNight1)}"
    }
    if (timeOnNight2) {
    	title += "\nNight Schedule 2: ${humanReadableTime(timeOnNight2)} to ${humanReadableTime(timeOffNight2)}"
    }
    return title
}

def WEDesc() {
	def title = ""
    if (timeOnWEDay1) {
    	title += "Weekend Day Schedule 1: ${humanReadableTime(timeOnWEDay1)} to ${humanReadableTime(timeOffWEDay1)}"
    }
    if (timeOnWEDay2) {
    	title += "\nWeekend Day Schedule 2: ${humanReadableTime(timeOnWEDay2)} to ${humanReadableTime(timeOffWEDay2)}"
    }
    if (timeOnWENight1) {
    	title += "\nWeekend Night Schedule 1: ${humanReadableTime(timeOnWENight1)} to ${humanReadableTime(timeOffWENight1)}"
    }
    if (timeOnWENight2) {
    	title += "\nWeekend Night Schedule 2: ${humanReadableTime(timeOnWENight2)} to ${humanReadableTime(timeOffWENight2)}"
    }
    return title 
}

public smartThingsDateFormat() { "yyyy-MM-dd'T'HH:mm:ss.SSSZ" }

public humanReadableTime(dateTxt) {
	new Date().parse(smartThingsDateFormat(), dateTxt).format("h:mm a", timeZone(dateTxt))
}

private isWeekend() {
    def isTheWeekend = false
    if (dayOfWeek() == 1 || dayOfWeek() == 7) {
    	isTheWeekend = true
    } 
    return isTheWeekend
}

public dayOfWeek() {
	def calendar = Calendar.getInstance()
    calendar.setTimeZone(location.timeZone)
    def today = calendar.get(Calendar.DAY_OF_WEEK)
    return today
}
