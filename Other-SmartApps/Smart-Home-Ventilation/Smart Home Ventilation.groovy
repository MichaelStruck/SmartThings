/**
 *  Smart Home Ventilation
 *	Version 2.00
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
    description: "Allows for setting up various schedule scenarios for turning on and off home ventilation switches.",
    category: "Convenience",
    iconUrl: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Smart-Home-Ventilation/HomeVent.png",
    iconX2Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Smart-Home-Ventilation/HomeVent@2x.png",
    iconX3Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Smart-Home-Ventilation/HomeVent@2x.png")

preferences {
	page(name: "mainPage")
	page(name: "A_Scenario")
	page(name: "B_Scenario")
	page(name: "C_Scenario")
	page(name: "D_Scenario")
}

def mainPage() {
	dynamicPage(name: "mainPage", title: "", install: true, uninstall: true) {
     	section("Select ventilation switches..."){
			input "switches", title: "Switches", "capability.switch", multiple: true
		}
        section ("Scheduling scenarios...") {
        	href(name: "toA_Scenario", page: "A_Scenario", title: getTitle (titleA, "A"), description: schedDesc(timeOnA1,timeOffA1,timeOnA2,timeOffA2,timeOnA3,timeOffA3,timeOnA4,timeOffA4, modeA, daysA), state: greyOut(timeOnA1,timeOnA2,timeOnA3,timeOnA4))
        	href(name: "toB_Scenario", page: "B_Scenario", title: getTitle (titleB, "B"), description: schedDesc(timeOnB1,timeOffB1,timeOnB2,timeOffB2,timeOnB3,timeOffB3,timeOnB4,timeOffB4, modeB, daysB), state: greyOut(timeOnB1,timeOnB2,timeOnB3,timeOnB4))
        	href(name: "toC_Scenario", page: "C_Scenario", title: getTitle (titleC, "C"), description: schedDesc(timeOnC1,timeOffC1,timeOnC2,timeOffC2,timeOnC3,timeOffC3,timeOnC4,timeOffC4, modeC, daysC), state: greyOut(timeOnC1,timeOnC2,timeOnC3,timeOnC4))
        	href(name: "toD_Scenario", page: "D_Scenario", title: getTitle (titleD, "D"), description: schedDesc(timeOnD1,timeOffD1,timeOnD2,timeOffD2,timeOnD3,timeOffD3,timeOnD4,timeOffD4, modeD, daysD), state: greyOut(timeOnD1,timeOnD2,timeOnD3,timeOnD4))
        }
		section([mobileOnly:true], "Options") {
			label(title: "Assign a name", required: false, defaultValue: "Smart Home Ventilation")
		}
    }
}
//----Scheduling Pages
def A_Scenario() {
	dynamicPage(name: "A_Scenario", title: getTitle (titleA, "A")) {
    	section("Schedule 1..."){
			input "timeOnA1", title: "Time to turn on", "time", required: false
        	input "timeOffA1", title: "Time to turn off", "time", required: false
		}
    	section("Schedule 2..."){
			input "timeOnA2", title: "Time to turn on", "time", required: false
        	input "timeOffA2", title: "Time to turn off", "time", required: false
		}
    	section("Schedule 3..."){
        	input "timeOnA3", title: "Time to turn on", "time", required: false
        	input "timeOffA3", title: "Time to turn off", "time", required: false
		}
    	section("Schedule 4..."){
        	input "timeOnA4", title: "Time to turn on", "time", required: false
        	input "timeOffA4", title: "Time to turn off", "time", required: false
		}
		section("Option") {
    		input "titleA", title: "Assign a scenario name", "text", required: false
            input "modeA", "mode", required: false, multiple: true, title: "Run in specific mode(s)", description: "Choose Modes"
		   	input "daysA", "enum", multiple: true, title: "Run on specific day(s)", description: "Choose Days", required: false, options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"]
		}
    }
}

def B_Scenario() {
	dynamicPage(name: "B_Scenario", title: getTitle (titleB, "B")) {
    	section("Schedule 1..."){
			input "timeOnB1", title: "Time to turn on", "time", required: false
        	input "timeOffB1", title: "Time to turn off", "time", required: false
		}
    	section("Schedule 2..."){
			input "timeOnB2", title: "Time to turn on", "time", required: false
        	input "timeOffB2", title: "Time to turn off", "time", required: false
		}
    	section("Schedule 3..."){
        	input "timeOnB3", title: "Time to turn on", "time", required: false
        	input "timeOffB3", title: "Time to turn off", "time", required: false
		}
    	section("Schedule 4..."){
        	input "timeOnB4", title: "Time to turn on", "time", required: false
        	input "timeOffB4", title: "Time to turn off", "time", required: false
		}
		section("Option") {
    		input "titleB", title: "Assign a scenario name", "text", required: false
            input "modeB", "mode", required: false, multiple: true, title: "Run in specific mode(s)", description: "Choose Modes"
		   	input "daysB", "enum", multiple: true, title: "Run on specific day(s)", description: "Choose Days", required: false, options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"]
		}
    }
}

def C_Scenario() {
	dynamicPage(name: "C_Scenario", title: getTitle (titleC, "C")) {
    	section("Schedule 1..."){
			input "timeOnC1", title: "Time to turn on", "time", required: false
        	input "timeOffC1", title: "Time to turn off", "time", required: false
		}
    	section("Schedule 2..."){
			input "timeOnC2", title: "Time to turn on", "time", required: false
        	input "timeOffC2", title: "Time to turn off", "time", required: false
		}
    	section("Schedule 3..."){
        	input "timeOnC3", title: "Time to turn on", "time", required: false
        	input "timeOffC3", title: "Time to turn off", "time", required: false
		}
    	section("Schedule 4..."){
        	input "timeOnC4", title: "Time to turn on", "time", required: false
        	input "timeOffC4", title: "Time to turn off", "time", required: false
		}
		section("Option") {
    		input "titleC", title: "Assign a scenario name", "text", required: false
            input "modeC", "mode", required: false, multiple: true, title: "Run in specific mode(s)", description: "Choose Modes"
		   	input "daysC", "enum", multiple: true, title: "Run on specific day(s)", description: "Choose Days", required: false, options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"]
		}
    }
}

def D_Scenario() {
	dynamicPage(name: "D_Scenario", title: getTitle (titleD, "D")) {
    	section("Schedule 1..."){
			input "timeOnD1", title: "Time to turn on", "time", required: false
        	input "timeOffD1", title: "Time to turn off", "time", required: false
		}
    	section("Schedule 2..."){
			input "timeOnD2", title: "Time to turn on", "time", required: false
        	input "timeOffD2", title: "Time to turn off", "time", required: false
		}
    	section("Schedule 3..."){
        	input "timeOnD3", title: "Time to turn on", "time", required: false
        	input "timeOffD3", title: "Time to turn off", "time", required: false
		}
    	section("Schedule 4..."){
        	input "timeOnD4", title: "Time to turn on", "time", required: false
        	input "timeOffD4", title: "Time to turn off", "time", required: false
		}
        section("Option") {
    		input "titleD", title: "Assign a scenario name", "text", required: false
            input "modeD", "mode", required: false, multiple: true, title: "Run in specific mode(s)", description: "Choose Modes"
		   	input "daysD", "enum", multiple: true, title: "Run on specific day(s)", description: "Choose Days", required: false, options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"]
		}
    }
}

// Install and initiate

def installed() {
    log.debug "Installed with settings: ${settings}"
    init()
}

def updated() {
    unschedule()
    log.debug "Updated with settings: ${settings}"
    init()
}

def init() {
    schedule ("2015-04-01T00:00:00.000-0700", midNight)
	startProcess()
}    

// Common methods

def startProcess () {
    createDayArray() 
	state.dayCount=state.data.size()
    if (state.dayCount){
		state.counter = 0
        startDay()
    }
}

def startDay() {
    def times = "${state.data.get(state.counter)}"
    def startTime = times.substring(0,13)
    def endTime = times.substring(13,26)
    
    def start = convertEpochString(startTime)
    def stop = convertEpochString(endTime)
    
    runOnce(start, turnOnSwitch, [overwrite: true])
    runOnce(stop, incDay, [overwrite: true])
}

def incDay() {
    turnOffSwitch()
    state.counter = state.counter + 1
    if (state.counter < state.dayCount) {
    	startDay()
    }
}

def midNight(){
    startProcess()
}

def turnOnSwitch() {
    switches.on()
    log.debug "Home ventilation switches are on."
}

def turnOffSwitch() {
    switches.each {
    	if (it.currentValue("switch")=="on"){
			it.off()
        }
    }
    log.debug "Home ventilation switches are off."
}
    
def schedDesc(on1, off1, on2, off2, on3, off3, on4, off4, modeList, dayList) {
	def title = ""
	def dayListClean = "On "
    def modeListClean ="Scenario runs in "
    if (dayList) {
    	def dayListSize = dayList.size()
        for (dayName in dayList) {
        	dayListClean = "${dayListClean}"+"${dayName}"
    		dayListSize = dayListSize -1
            if (dayListSize) {
            	dayListClean = "${dayListClean}, "
            }
        }
	} 
    else {
    	dayListClean = "Every day"
    }
    if (modeList) {
    	def modeListSize = modeList.size()
        def modePrefix ="modes"
        if (modeListSize == 1) {
        	modePrefix = "mode"
        }
        for (modeName in modeList) {
        	modeListClean = "${modeListClean}"+"'${modeName}'"
    		modeListSize = modeListSize -1
            if (modeListSize) {
            	modeListClean = "${modeListClean}, "
            }
            else {
            	modeListClean = "${modeListClean} ${modePrefix}"
        	}
        }
	} 
    else {
    	modeListClean = "${modeListClean}all modes"
    }
    if (on1 && off1){
    	title += "Schedule 1: ${humanReadableTime(on1)} to ${humanReadableTime(off1)}"
    }
    if (on2 && off2) {
    	title += "\nSchedule 2: ${humanReadableTime(on2)} to ${humanReadableTime(off2)}"
    }
    if (on3 && off3) {
    	title += "\nSchedule 3: ${humanReadableTime(on3)} to ${humanReadableTime(off3)}" 
    }
    if (on4 && off4) {
    	title += "\nSchedule 4: ${humanReadableTime(on4)} to ${humanReadableTime(off4)}" 
    }
    if (on1 || on2 || on3 || on4) {
    	title += "\n$modeListClean"
    	title += "\n$dayListClean" 
    }
    
    if (!on1 && !on2 && !on3 && !on4) {
    	title="Click to configure scenario"
    }
    title
}

def greyOut(on1, on2, on3, on4){
	def result = ""
    if (on1 || on2 || on3 || on4) {
    	result = "complete"
    }
    result
}

public humanReadableTime(dateTxt) {
	new Date().parse("yyyy-MM-dd'T'HH:mm:ss.SSSZ", dateTxt).format("h:mm a", timeZone(dateTxt))
}

public convertEpochString(dateTxt) {
	long longDate = Long.valueOf(dateTxt).longValue()
	new Date(longDate).format("yyyy-MM-dd'T'HH:mm:ss.SSSZ", location.timeZone)
}

private getTitle(txt, scenario) {
	def title = "Scenario ${scenario}"
    if (txt) {
    	title=txt
    }
    title	
}

private daysOk(dayList) {
	def result = true
	if (dayList) {
		def df = new java.text.SimpleDateFormat("EEEE")
		if (location.timeZone) {
			df.setTimeZone(location.timeZone)
		}
		else {
			df.setTimeZone(TimeZone.getTimeZone("America/New_York"))
		}
		def day = df.format(new Date())
		result = dayList.contains(day)
	}
	result
}

private modeOk(modeList) {
	def result=true
    if (modeList){
    	result = modeList.contains(location.mode)
	}
    result
}

private timeOk(starting, ending) {
    if (starting && ending) {
		def currTime = now()
		def start = timeToday(starting).time
		def stop = timeToday(ending).time
        if (start < stop && start >= currTime && stop>=currTime) {
        	state.data << "${start}${stop}"
        }
    }
}

def createDayArray() {
	state.data = []
    if (modeOk(modeA)) {
        if (daysOk(daysA)){
			timeOk(timeOnA1, timeOffA1)
			timeOk(timeOnA2, timeOffA2)
			timeOk(timeOnA3, timeOffA3)
			timeOk(timeOnA4, timeOffA4)
        }
    }
	if (modeOk(modeB)) {
        if (daysOk(daysB)){
			timeOk(timeOnB1, timeOffB1)
            timeOk(timeOnB2, timeOffB2)
            timeOk(timeOnB3, timeOffB3)
            timeOk(timeOnB4, timeOffB4)
        }
    }
    if (modeOk(modeC)) {
        if (daysOk(daysC)){
            timeOk(timeOnC1, timeOffC1)
            timeOk(timeOnC2, timeOffC2)
            timeOk(timeOnC3, timeOffC3)
            timeOk(timeOnC4, timeOffC4)
        }
    }
	if (modeOk(modeD)) {
        if (daysOk(daysD)){
           timeOk(timeOnD1, timeOffD1)
           timeOk(timeOnD2, timeOffD2)
           timeOk(timeOnD3, timeOffD3)
           timeOk(timeOnD4, timeOffD4)        
        }
    }
    state.data.sort()
}


