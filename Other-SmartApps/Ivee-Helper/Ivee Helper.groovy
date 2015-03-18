/**
 *  Ivee Helper
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
    name: "Ivee Helper",
    namespace: "MichaelStruck",
    author: "Michael Struck",
    description: "Allows you to control the status of a switch by changing its status based on a group of other switches.",
    category: "Convenience",
    iconUrl: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Ivee-Helper/Ivee.png",
    iconX2Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Ivee-Helper/Ivee@2x.png",
    iconX3Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Ivee-Helper/Ivee@2x.png")


preferences {
	section("When any of these lights/switches are on or off...") {
		input "lights1", "capability.switch", multiple: true, title: "Lights/Switches", required: true
    }
    section("Turn on or off this switch...") {
		input "master", "capability.switch", multiple: false, title: "Master Switch", required: true
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
	subscribe(lights1, "switch", "switchHandler")
}

def switchHandler(evt) {
	if (allStatus()){
    	master.on()
    } 
    else {
    	master.off()
    }
}
   
private allStatus() {
    def result = false
	def currSwitches = lights1.currentSwitch
    def onSwitches = currSwitches.findAll { switchVal ->
        switchVal == "on" ? true : false
    }
    if (onSwitches.size()) {
    	result = true
    }
	return result

}
