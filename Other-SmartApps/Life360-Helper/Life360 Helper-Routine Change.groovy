/**
 *  Life360 Helper-Routine Change
 *  Version 1.0.0 3/20/15
 *  Version 1.0.1 3/24/15 - Updated with more effecient code
 *  Version 1.0.2 4/9/15 - Added ability to change name of app
 *  Version 1.0.3 5/15/15 - Optimized code, added a switch to limit the trigger to once a day
 *  Version 1.0.4 7/1/15 - Added About screen and updated GUI for more consistency between apps
 *  version 1.0.5 7/17/15  - Added ability to not run the app when in certain modes
 *  Version 1.0.6 9/13/14 - Changed syntax to reflect new SmartThings Routines (from Hello, Home)
 *
 *  Copyright 2015 Michael Struck
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *	Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *  Turn specific switched on when it gets dark. Can also turn off specific switches when it becomes light again based on brightness from a connected light sensor.
 *
 */

definition(
    name: "Life360 Helper-Routine Change",
    namespace: "MichaelStruck",
    author: "Michael Struck",
    description: "Monitors the Life360 presence sensors to trigger differnt routines based on selected modes.",
    category: "Convenience",
    iconUrl: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Life360-Helper/life360.png",
    iconX2Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Life360-Helper/life360@2x.png",
	iconX3Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Life360-Helper/life360@2x.png")

preferences {
	page name: "getPref"
}

def getPref() {
    dynamicPage(name: "getPref", install:true, uninstall: true) {
    
    	section("Choose the Life360 presence sensors you want to monitor...") {
			input "people", "capability.presenceSensor", multiple: true, title: "Life360 Presence Sensors" 
		}
		def phrases = location.helloHome?.getPhrases()*.label
		if (phrases) {
        	phrases.sort()
            
        	section("When all are present and...") {
				input "modes1", "mode", title: "In these modes...", required: true, multiple: true
				input "phrase1", "enum", title: "Perform the following routine", required: true, options: phrases
        	}
            
            section("When all are present and...") {
				input "modes2", "mode", title: "In these modes...", required: false, multiple: true
				input "phrase2", "enum", title: "Perform the following routine", required: true, options: phrases
        	}
			section ("Do nothing if in one of these modes..."){
        		input "modesNoAction", "mode", title: "Mode(s)", required: false, multiple: true
        	}
        }
		section([mobileOnly:true], "Options") {
			input "triggerOnce", title: "Trigger only once per day...", "bool", defaultValue: "true"
            label(title: "Assign a name", required: false)
            href "pageAbout", title: "About ${textAppName()}", description: "Tap to get application version, license and instructions"
		}  
	}
}

page(name: "pageAbout", title: "About ${textAppName()}") {
        section {
            paragraph "${textVersion()}\n${textCopyright()}\n\n${textLicense()}\n"
        }
        section("Instructions") {
            paragraph textHelp()
        }
}

//--------------------------------------
def installed() {
	log.debug "Installed with settings: ${settings}"
	init()
}

def updated() {
	log.debug "Updated with settings: ${settings}"
	unschedule()
    unsubscribe()
	init()
}

def init(){
	state.triggered = false
    subscribe(people, "presence", presenceHandler)
}

def presenceHandler(evt){
	log.debug "Presence detected"
    if (everyoneIsPresent() && (!triggerOnce || (triggerOnce && !state.triggered)) && !runMode(modesNoAction)){
    	if (runMode(modes1)) {
            location.helloHome.execute(phrase1)
    	}
        else if (runMode(modes2)) {
            location.helloHome.execute(phrase2)
    	}
    }
	if (triggerOnce){	
        state.triggered = true
    	schedule (getMidnight(), midNightReset)
    }
}

private everyoneIsPresent() {
    def result = people.find {it.currentPresence == "not present"} ? false : true
    result
}

private runMode(modes) {
    def result = modes ? modes.contains(location.mode) : false
    result
}

def midNightReset() {
	state.triggered = false
}

def getMidnight() {
	def midnightToday = timeToday("2000-01-01T23:59:59.999-0000", location.timeZone)
	midnightToday
}

//Version/Copyright/Information/Help

private def textAppName() {
	def text = "Life360 Helper-Phrase Change"
}	

private def textVersion() {
    def text = "Version 1.0.6 (09/13/2015)"
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
    	"In Life360, choose a location you want to monitor. This does NOT need to be the location where the SmartThings " +
        "hub is located; this could be your work location, or a location on the way home during your commute. Choose this sensor; " +
        "when you are in a certain mode and this sensor shows 'present' you can activate a routine. This can be used to pre-heat a house "+ 
        "or turn on a specific set of lights. You may also choose a mode in which all actions are bypassed."
}
