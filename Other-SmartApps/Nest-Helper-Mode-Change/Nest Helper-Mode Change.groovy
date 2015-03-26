/**
 *  Nest Helper-Mode Change
 *
 *	Version 1.0 3/26/15
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
    name: "Nest Helper-Mode change",
    namespace: "MichaelStruck",
    author: "Michael Struck",
    description: "Allow the Nest Thermostat to follow a specific set of modes (Home/Away).",
    category: "Convenience",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Partner/nest-home-app.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Partner/nest-home-app@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Partner/nest-home-app@2x.png")


preferences {
	section("Choose a Nest Thermostat...") {
    	input "tstat1", "capability.thermostat", title: "Nest", multiple: false
		}
	section("Change to away on Nest when in which mode(s)...") {
    	input "awayMode", "mode", required: true, multiple: true, title: "Modes?"
	}
    section("Change to home on Nest when in which mode(s)...") {
    	input "homeMode", "mode", required: true, multiple: true, title: "Modes?"
	}
}

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
