/**
 *  Life360 Helper-Phrase Change
 *  Version 1.00 3/20/15
 *  Version 1.01 3/24/15 - Updated with more effecient code
 *  Version 1.02 4/9/15 - Added ability to change name of app
 *  Version 1.03 5/15/15 - Optimized code, added a switch to limit the trigger to once a day
 *  Version 1.0.4 5/31/15 - Added About screen
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
    name: "Life360 Helper-Phrase Change",
    namespace: "MichaelStruck",
    author: "Michael Struck",
    description: "Monitors the Life360 presence sensors to trigger differnt Hello, Home phrases based on selected modes.",
    category: "Convenience",
    iconUrl: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Life360-Helper/life360.png",
    iconX2Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Life360-Helper/life360@2x.png",
	iconX3Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Life360-Helper/life360@2x.png")

preferences {
	page name: "getPref"
}


def getPref() {
    dynamicPage(name: "getPref", title: "Choose Presence Sensors and Phrases", install:true, uninstall: true) {
    
    	section("Choose the Life360 presence sensors you want to monitor...") {
			input "people", "capability.presenceSensor", multiple: true, title: "Life360 Presence Sensor" 
		}
		section("When presence is detected and in one of these modes...") {
			input "modes", "mode", title: "Mode(s)", required: true, multiple: true
		}
		def phrases = location.helloHome?.getPhrases()*.label
		if (phrases) {
        	phrases.sort()
			section("Perform the following Hello, Home phrase when...") {
				input "phrase1", "enum", title: "Any of the modes above are active", required: true, options: phrases
				input "phrase2", "enum", title: "None of the modes above are active", required: true, options: phrases
			}
        }
        section ("Limit the trigger to once a day") {
        	input "triggerOnce", title: "Trigger only once per day...", "bool", defaultValue: true
        }
		section([mobileOnly:true], "Options") {
			label(title: "Assign a name", required: false, defaultValue: "Life360 Helper-Phrase Change")
            href "pageAbout", title: "About ${textAppName()}", description: "Tap to get application version,  license and instructions"
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
    def midnightToday = timeToday("2000-01-01T23:59:59.999-0000", location.timeZone)
    schedule (midnightToday, midNightReset)
    subscribe(people, "presence", presence)
}

def presence(evt){
    if (everyoneIsPresent() && (!triggerOnce || (triggerOnce && !state.triggered))){
    	if (runMode()) {
        	location.helloHome.execute(settings.phrase1)
    	} 
        else {
    		location.helloHome.execute(settings.phrase2)
    	}
	state.triggered = true
    }
}

private everyoneIsPresent() {
    def result = people.find {it.currentPresence == "not present"} ? false : true
	result
}

private runMode() {
	def result = modes.contains(location.mode)
	result
}

def midNightReset() {
	state.triggered = false
}

//Version/Copyright/Information/Help

private def textAppName() {
	def text = "Life360 Helper-Phrase Change"
}	

private def textVersion() {
    def text = "Version 1.0.4 (05/31/2015)"
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
        "hub is located; this could be your work location, or a location on the way home during your commute. Choose this sensor in this app; " +
        "when you are in a certain mode and this sensor shows 'present' you can activate a Hello, Home phrase. This can be used to pre-heat a house "+ 
        "or turn on a specific set of lights. "
}
