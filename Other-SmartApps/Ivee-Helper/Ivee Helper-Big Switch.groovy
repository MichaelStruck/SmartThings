/**
 *  Ivee Helper-Big Switch
 *  Version 1.0 3/18/15
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
    name: "Ivee Helper-Big Switch",
    namespace: "MichaelStruck",
    author: "Michael Struck",
    description: "Used to control a group of switches tied to a Master Swtich.",
    category: "Convenience",
    iconUrl: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Ivee-Helper/Ivee.png",
    iconX2Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Ivee-Helper/Ivee@2x.png",
    iconX3Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Ivee-Helper/Ivee@2x.png")


preferences {
	section("Define a 'Master Switch' which Ivee will use.") {
		input "master", "capability.switch", multiple: false, title: "Master Switch", required: true
    }    
    section("When Ivee turns on the Master Switch, turn on these lights/switches...") {
		input "lightsOn", "capability.switch", multiple: true, title: "Lights/Switches", required: false
    } 
    section("When Ivee turns off the Master Switch, turn off these lights/switches...") {
		input "lightsOff", "capability.switch", multiple: true, title: "Lights/Switches", required: false
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
	subscribe(master, "switch.on", "onHandler")
    subscribe(master, "switch.off", "offHandler")
}

def onHandler(evt) {
	lightsOn.on()
}

def offHandler(evt) {
	lightsOff.off()
}
