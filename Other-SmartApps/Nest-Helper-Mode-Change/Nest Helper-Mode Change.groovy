/**
 *  Nest Helper-Mode Change
 *
 *  Copyright 2015 Michael Struck
 *  Version 1.00 3/26/15
 *  Version 1.01 - 4/9/15 Added the ability to change the name of the app and removed modes to run in (redundent)
 *  Version 1.0.2 - 5/25/15 Added About screen
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
    description: "Allow the Nest Thermostat to follow a specific set of modes (Home/Away).",
    category: "Convenience",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Partner/nest-home-app.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Partner/nest-home-app@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Partner/nest-home-app@2x.png")

preferences {
	page name:"getPref"
    page name:"pageAbout"
}

def getPref() {
    dynamicPage(name: "getPref", install:true, uninstall: true) {
	
    	section("Choose a Nest Thermostat...") {
    		input "tstat1", "capability.thermostat", title: "Nest", multiple: false
			}
		section("Change to away on Nest when in which mode(s)...") {
    		input "awayMode", "mode", required: true, multiple: true, title: "Modes?"
		}
    	section("Change to home on Nest when in which mode(s)...") {
    		input "homeMode", "mode", required: true, multiple: true, title: "Modes?"
		}
		section([mobileOnly:true], "Options") {
			label(title: "Assign a name", required: false, defaultValue: "Nest Helper-Mode Change")
            href "pageAbout", title: "About ${textAppName()}", description: "Tap to get version and license information"
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
	log.debug "locationHandler evt: ${evt.value}"
    if (homeMode.contains(evt.value)) {
	    tstat1.present()
    }
    else if (awayMode.contains(evt.value)) {
    	tstat1.away()
    }
}

//Version/Copyright/Information/Help

private def textAppName() {
	def text = "Nest Helper-Change Modes"
}	

private def textVersion() {
    def text = "Version 1.0.2 (05/25/2015)"
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
    	"Instructions:\nChoose a Nest Thermostat that will be linked to various SmartThings modes. " +
        "When the modes change, the Nest Thermostat will change to Home or Away based on the settings." 
}
