/**
 *  Smart Water Heater
 *  Version 1.3 5/25/2015
 *
 *  Version 1.01-Initial release
 *  Version 1.1 added a function to turn water heater back on if someone comes home early
 *  Version 1.11 Revised the interface for better flow
 *  Version 1.2 Revised the interface even more for better flow
 *  Version 1.21 Further interface revision
 *  Version 1.3 Added the option to turn off the water heater early if everyone leaves before the scheduled time and code opimization
 *  Version 1.3.1 Added About screen
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
    page name: "pageAbout"
}

def mainPage() {
	dynamicPage(name: "mainPage", title: "", install: true, uninstall: true) {
    	section("Select water heater switch..."){
			input "switchWH", title: "Switch", "capability.switch", multiple: false
		}
    	section("Daytime Options"){
        	href(name: "toDaySchedule", page: "daySchedule", title: "Schedule", description: dayDescription(), state: "complete")
        	input "presence1", "capability.presenceSensor", title: "Remain on if any of these people are home", multiple: true, required: false
        	input "exceptionLeave", "bool", title: "Turn off when everyone leaves before the scheduled turn off time", defaultValue: "true"
            input "exceptionArrive", "bool", title: "Turn on when someone arrives home early", defaultValue: "true"
        	input "weekendRun", "bool", title: "Run daytime schedule during the weekend", defaultValue: "false"
		}
    	section("Nighttime Options"){
    		href(name: "toNightSchedule", page: "nightSchedule", title: "Schedule", description: nightDescription(), state: "complete")
    	}
        section([mobileOnly:true], "Other Options") {
			label(title: "Assign a name", required: false, defaultValue: "Smart Water Heater")
            mode title: "Set for specific mode(s)", required: false
			href "pageAbout", title: "About ${textAppName()}", description: "Tap to get version and license information"
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

def pageAbout() {
	dynamicPage(name: "pageAbout", title: "About ${textAppName()}") {
        section {
            paragraph "${textVersion()}\n${textCopyright()}\n\n${textHelp()}\n"
        }
        section("License") {
            paragraph textLicense()
        }
    }
}

//---------------------------------------------------

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
	if (exceptionArrive || exceptionLeave){
    	subscribe(presence1, "presence", presenceHandler)
    }
    schedule(timeOffDay, "turnOffDay")
	schedule(timeOnDay, "turnOnDay")
    schedule(timeOffNight, "turnOffNight")
	schedule(timeOnNight, "turnOnNight")
}

def turnOffDay() {
	def runToday = !weekendRun && isWeekend() ? false : true

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

def presenceHandler(evt) {
    if (!everyoneGone() && state.status=="Day off"){
    	log.debug "Presence detected, turning water heater back on"
    	turnOnDay()
    }

	if (everyoneGone() && checkTime()){
    	log.debug "Everyone has left early, turning water heater off"
		turnOffDay()
    }
}

private everyoneGone() {
    def result = presence1.find{it.currentPresence == "present"} ? false : true
	result
}


def nightDescription() {
	def title = ""
    if (timeOffNight) {
    	title += "Turn off at ${hhmm(timeOffNight)} then turn back on at ${hhmm(timeOnNight)}"
    }
    title
}

def dayDescription() {
	def title = ""
    if (timeOffDay) {
    	title += "Turn off at ${hhmm(timeOffDay)} then turn back on at ${hhmm(timeOnDay)}"
    }
    title
}

public smartThingsDateFormat() { "yyyy-MM-dd'T'HH:mm:ss.SSSZ" }

public hhmm(dateTxt) {
	new Date().parse(smartThingsDateFormat(), dateTxt).format("h:mm a", timeZone(dateTxt))
}

private isWeekend() {
    def isTheWeekend = dayOfWeek() == 1 || dayOfWeek() == 7 ? true : false
    isTheWeekend
}

public dayOfWeek() {
	def calendar = Calendar.getInstance()
    calendar.setTimeZone(location.timeZone)
    def today = calendar.get(Calendar.DAY_OF_WEEK)
    today
}

private checkTime() {
	def result = true
	if (timeOffDay) {
		def currTime = now()
		def start = timeToday(timeOffDay).time
		result = currTime < start
	}
	result
}

//Version/Copyright/Information/Help

private def textAppName() {
	def text = "Smart Water Heater"
}	

private def textVersion() {
    def text = "Version 1.3.1 (05/25/2015)"
}

private def textCopyright() {
    def text = "Copyright © 2015 Michael Struck"
}

private def textLicense() {
    def text =
		"Licensed under the Apache License, Version 2.0 (the 'License'); "+
		"you may not use this file except in compliance with the License. "+
		"You may obtain a copy of the License at"+
		"\n\n"+
		"    http://www.apache.org/licenses/LICENSE-2.0"+
		"\n\n"+
		"Unless required by applicable law or agreed to in writing, software "+
		"distributed under the License is distributed on an 'AS IS' BASIS, "+
		"WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. "+
		"See the License for the specific language governing permissions and "+
		"limitations under the License."
}

private def textHelp() {
	def text =
    	"Instructions:\nChoose the day and night schedule in which the water heater power is turned " +
        "on and off. During the day, you have various options to determine whether to run the schedule " +
        "based on the status of various presence sensors."
}
