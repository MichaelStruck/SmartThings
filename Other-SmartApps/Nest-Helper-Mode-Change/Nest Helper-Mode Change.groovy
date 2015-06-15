/**
 *  Nest Helper-Mode Change
 *
 *  Copyright 2015 Michael Struck
 *  Version 1.0.0 - 3/26/15
 *  Version 1.0.1 - 4/9/15 Added the ability to change the name of the app and removed modes to run in (redundent)
 *  Version 1.0.2 - 5/31/15 Added About screen and code optimizations
 *  Version 1.0.3 - 6/15/15 Changed internal logic for home and away to allow for better interface layout
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
    name: "Nest Helper-Mode Change",
    namespace: "MichaelStruck",
    author: "Michael Struck",
    description: "Allow the Nest Thermostat to follow a specific set of SmartThings modes.",
    category: "Convenience",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Partner/nest-home-app.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Partner/nest-home-app@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Partner/nest-home-app@2x.png")

preferences {
	page name:"getPref"
}

def getPref() {
    dynamicPage(name: "getPref", install:true, uninstall: true) {
    	section("Choose a Nest Thermostat...") {
    		input "tstat1", "capability.thermostat", title: "Nest Thermostat", multiple: false , required: true
			}
		section("Change Nest HOME/AWAY based on SmartThing modes") {
    		input "awayMode", "mode", required: true, multiple: true, title: "Nest AWAY modes"
			input "homeMode", "mode", required: false, multiple: true, title: "Nest HOME modes (optional)"
		}
		section([mobileOnly:true], "Options") {
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
	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"
	unsubscribe()
	initialize()
}

def initialize() {
    subscribe(location, "mode", locationHandler)
}

def locationHandler(evt) {
    log.debug "Mode set to ${evt.value}"
    def currentState = tstat1.latestValue('presence')
    if (currentState =="present" && awayMode && awayMode.contains(evt.value)) {
    	tstat1.away()
        log.debug "Nest set to AWAY"
    }
    if (currentState =="not present" && (!homeMode || (homeMode && homeMode.contains(evt.value)))) {
	    tstat1.present()
        log.debug "Nest set to HOME"
    }
}

//Version/Copyright/Information/Help

private def textAppName() {
	def text = "Nest Helper-Change Modes"
}	

private def textVersion() {
    def text = "Version 1.0.3 (06/15/2015)"
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
    	"Choose a Nest Thermostat that will be linked to various SmartThings modes. " +
        "When the modes change, the Nest Thermostat will change to Home or Away based on the settings. If no HOME settings are specified, "+
        "then the AWAY mode setting will be the trigger for both events (i.e. In that mode=AWAY, not in that mode=HOME."
}
