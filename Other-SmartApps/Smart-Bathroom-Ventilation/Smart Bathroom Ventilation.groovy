/**
 *  Smart Bathroom Ventilation-Parent
 *
 *  Version - 3.0.0 6/14/16
 * 
 *  Version 1.0.0 - Initial release
 *  Version 1.1.0 - Added restrictions for time that fan goes on to allow for future features along with logic fixes
 *  Version 1.2.0 - Added the option of starting the fans based on time (to eliminate the time of polling and for those without a humidity sensor)
 *  Version 2.0.0 - Modified to allow more scenarios via parent/child app structure
 *  Version 2.0.1 - Allow ability to see child app version in parent app and moved the remove button
 *  Version 2.1.0 - Added icon on about page
 *  Version 2.1.1 - Removed label from parent app
 *  Version 2.1.2 - Added icons to the main menu
 *  Version 3.0.0 - Combined parent and child app together
 *
 *  Copyright 2016 Michael Struck - Uses code from Lighting Director by Tim Slagle & Michael Struck
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
    name: "Smart Bathroom Ventilation${parent ? "-Scenario" : ""}",
    singleInstance: true,
    namespace: "MichaelStruck",
    author: "Michael Struck",
    parent: parent ? "MichaelStruck.Smart Bathroom Ventilation" : null,
    description: "Control multiple ventilation scenarios based on humidity or certain lights being turned on.",
    category: "Convenience",
    iconUrl: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Smart-Bathroom-Ventilation/BathVent.png",
    iconX2Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Smart-Bathroom-Ventilation/BathVent@2x.png",
    iconX3Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Smart-Bathroom-Ventilation/BathVent@2x.png"
    )

preferences {
    page name:"mainPage"
    page name:"mainPageParent"
    page name:"mainPageChild"
    page name:"pageAbout"
}

def mainPage(){
	if (!parent) mainPageParent() else mainPageChild()
}

def mainPageParent(){
    dynamicPage(name: "mainPage", title: "Ventilation Scenarios", install: true, uninstall: false, submitOnChange: true) {
		section {
        	app(name: "childScenarios", appName: "Smart Bathroom Ventilation", namespace: "MichaelStruck", title: "Create New Scenario...", multiple: true)
		}
		section([title:"Options", mobileOnly:true]) {
			href "pageAbout", title: "About ${textAppName()}", description: "Tap to get application version, license, instructions or to remove the application",
            	image: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/img/info.png"
		}
	}
}

def pageAbout() {
	dynamicPage(name: "pageAbout" , uninstall: true) {
    	section {
        paragraph "${textAppName()}\n${textVersion()}\n${textCopyright()} ", image: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Smart-Bathroom-Ventilation/BathVent@2x.png"
        }
        section ("Apache License") { 
        	paragraph textLicense()
    	}
		section("Instructions") {
            paragraph textHelp()
		}
        section("Tap button below to remove all scenarios and application"){
        }
	}
}
// Child Menu Items----------------------------------------------------------------------------------------------------
def mainPageChild() {
	dynamicPage(name: "mainPageChild", title: "Ventilation Scenario", install: true, uninstall: true) {
		section() {
			label title:"Scenario Name", required: true
    	}
        section("Devices included in the scenario") {
			input "A_switch","capability.switch", title: "Monitor this light switch...", multiple: false, required: true
			input "A_humidity", "capability.relativeHumidityMeasurement",title: "Monitor the following humidity sensor...", multiple: false, required: false, submitOnChange:true
			input "A_fan", "capability.switch", title: "Control the following ventilation fans...", multiple: true, required: true
		}
		section("Fan settings") {
        	if (A_humidity && A_switch){
            	input "A_humidityDelta", title: "Ventilation fans turns on when lights are on and humidity rises(%)", "number", required: false, description: "0-50%"
            	input "A_repoll", "enum", title: "Repoll humidity sensor after (minutes)...", options: [[1:"1 Minute"],[2:"2 Minutes"],[3:"3 Minutes"],[4:"4 Minutes"],[5:"5 Minutes"]], defaultValue: 5
            }
            input "A_timeOn", title: "Optionally, turn on fans after light switch is turned on (minutes, 0=immediately)", "number", required: false
            if (A_humidity) {
            	input "A_fanTime", title: "Turn off ventilation fans after...", "enum", required: false, options: [[5:"5 Minutes"],[10:"10 Minutes"],[15:"15 Minutes"],[30:"30 Minutes"],[60:"1 hour"],[98:"Light switch is turned off"],[99:"Humidity drops to or below original value"]]
			}
            else {
            	input "A_fanTime", title: "Turn off ventilation fans after...", "enum", required: false, options: [[5:"5 Minutes"],[10:"10 Minutes"],[15:"15 Minutes"],[30:"30 Minutes"],[60:"1 hour"],[98:"Light switch is turned off"]]
            }
            input "A_manualFan", title: "If ventilation fans are turned on manually, turn them off automatically using settings above", "bool", defaultValue: "false"
        }
		section("Restrictions") {            
			input "A_day", "enum", options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"], title: "Only Certain Days Of The Week...",  multiple: true, required: false,
            	image: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/img/calendar.png"
        	href "timeIntervalInputA", title: "Only During Certain Times...", description: getTimeLabel(A_timeStart, A_timeEnd), state: greyedOutTime(A_timeStart, A_timeEnd),
            	image: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/img/clock.png"
            input "A_mode", "mode", title: "Only In The Following Modes...", multiple: true, required: false, 
            	image: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/img/modes.png"
		}
        section("Tap the button below to remove this scenario only"){
        }
    }
}

page(name: "timeIntervalInputA", title: "Only during a certain time") {
		section {
			input "A_timeStart", "time", title: "Starting", required: false
			input "A_timeEnd", "time", title: "Ending", required: false
		}
}

def installed() {
    initialize()
}

def updated() {
    if (parent) state.triggeredA = false
    if (parent) unschedule()
    unsubscribe()
    initialize()
}

def initialize() {
    if (parent) initChild() else initParent()
}
def initParent(){
    if (A_switch) {
		state.A_runTime = A_fanTime ? A_fanTime as Integer : 98
		subscribe(A_switch, "switch.on", onEventA)
		subscribe(A_switch, "switch.off", offEventA)
		if (A_manualFan) {
			subscribe(A_fan, "switch.on", turnOnA)
			subscribe(A_fan, "switch.off", turnOffA)
		}
		if (A_humidity && (A_humidityDelta || state.A_runTime == 99)) {
			subscribe(A_humidity, "humidity", humidityHandlerA)
		}
	}
}
def initChild(){
    childApps.each {child -> 
		log.info "Installed Scenario: ${child.label}" 
	}
}

//--------------------------------------------------------------------------------------------------

//A Handlers
def turnOnA(evt){
    if ((!A_mode || A_mode.contains(location.mode)) && getDayOk(A_day) && A_switch.currentValue("switch")=="on" && !state.triggeredA && getTimeOk(A_timeStart,A_timeEnd)) {
        log.debug "Ventilation turned on."
    	A_fan?.on()
        state.triggeredA = true
        if (state.A_runTime < 98) {
			log.debug "Ventilation will be turned off in ${state.A_runTime} minutes."
            unschedule ()
            runIn (state.A_runTime*60, "turnOffA")
        }
	}
}

def turnOffA(evt) {
	log.debug "Ventilation turned off."
    A_fan?.off()
    unschedule ()
}

def humidityHandlerA(evt){
    def currentHumidityA =evt.value as Integer
    log.debug "Humidity value is ${currentHumidityA}."
	if (state.humidityLimitA && currentHumidityA > state.humidityLimitA) {
        turnOnA()
    }
	if (state.humidityStartA && currentHumidityA <= state.humidityStartA && state.A_runTime == 99){
    	turnOffA()
    }      
}
def rePoll(){
	def currentHumidityA =A_humidity.currentValue("humidity")
    log.debug "Humidity value before refresh ${currentHumidityA}."
    A_humidity.refresh()
}

def onEventA(evt) {   
    def humidityDelta = A_humidity && A_humidityDelta ? A_humidityDelta as Integer : 0
    def text = ""
    if (A_humidity){
    	
        state.humidityStartA = A_humidity.currentValue("humidity")
    	state.humidityLimitA = state.humidityStartA + humidityDelta
        text = "Humidity starting value is ${state.humidityStartA}. Ventilation threshold is ${state.humidityLimitA}."
    }
    if (A_repoll){
        log.debug "Re-Polling in ${(A_repoll as int)*60} seconds"
        runIn((A_repoll as int)*60, "rePoll")
    }
    log.debug "Light turned on in ${app.label}. ${text}" 
    if ((!A_humidityDelta || A_humidityDelta == 0) && (A_timeOn != "null" && A_timeOn == 0)) {
    	turnOnA()
    }
    if ((A_timeOn && A_timeOn > 0) && getDayOk(A_day) && !state.triggeredA && getTimeOk(A_timeStart,A_timeEnd)) {
        log.debug "Ventilation will start in ${timeOn} minute(s)"
        runIn (timeOn*60, "turnOnA")
    }
}

def offEventA(evt) {
    def currentHumidityA = ""
    def text = ""
    if (A_humidity) {
    	currentHumidityA = A_humidity.currentValue("humidity") 
        text = "Humidity value is ${currentHumidityA}."
    }
    log.debug "Light turned off in '${app.label}'. ${text}"
	state.triggeredA = false
    if (state.A_runTime == 98){
    	turnOffA()
    }
}
//Common Methods-------------
def getTimeLabel(start, end){
	def timeLabel = "Tap to set"
	
    if(start && end){
    	timeLabel = "Between " + hhmm(start) + " and " +  hhmm(end)
    }
    else if (start) {
		timeLabel = "Start at " + hhmm(start)
    }
    else if(end){
    timeLabel = "End at " + hhmm(end)
    }
	timeLabel	
}

def greyedOutTime(start, end){
	def result = start || end ? "complete" : ""
}

private hhmm(time) {
	new Date().parse("yyyy-MM-dd'T'HH:mm:ss.SSSZ", time).format("h:mm a", timeZone(time))
}

private getDayOk(dayList) {
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

private getTimeOk(startTime,endTime) {
	def result = true
	def currTime = now()
	def start = startTime ? timeToday(startTime).time : null
	def stop =  endTime ? timeToday(endTime).time: null
    if (startTime && endTime) {
		result = start < stop ? currTime >= start && currTime <= stop : currTime <= stop || currTime >= start
	}
	else if (startTime){
    	result = currTime >= start
    }
    else if (endTime){
    	result = currTime <= stop
    }
    result
}
//Version/Copyright/Information/Help

private def textAppName() {
	def text = "Smart Bathroom Ventilation"
}	

private def textVersion() {
    def version = "Version: 3.0.0 (06/14/2016)"
    return version
}

private def textCopyright() {
    def text = "Copyright © 2016 Michael Struck"
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
    	"Within each scenario, select a light switch to monitor, a humidity sensor (optional), and fans to control. You can choose when " +
        "the ventilation fans comes on; either when the room humidity rises over a certain level or after a user definable time after the light switch is turned on. "+
        "The ventilation fans will turn off based on either a timer setting, humidity, or the light switch being turned off. " +
        "You can also choose to have the ventilation fans turn off automatically if they are turned on manually. "+
        "Each scenario can be restricted to specific modes, times of day or certain days of the week."
}
