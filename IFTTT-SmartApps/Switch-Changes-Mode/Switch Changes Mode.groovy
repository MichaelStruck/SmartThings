/**
 *  Switch Changes Mode
 *
 *  Copyright 2015 Michael Struck
 *  Version 1.0.1 3/8/15
 *  Version 1.0.2 3/24/15 Code revisions for better portability
 *  Version 1.0.3 4/9/15 Added the ability to change the name of the app
 *  Version 1.0.4 5/31/14 Added an About screen
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
 *  Ties a mode to a switch (virtual or real) on/off state. Perfect for use with IFTTT.
 *  Simple define a switch to be used, then tie the on/off state of the switch to a specific mode.
 *  Connect the switch to an IFTTT action, and the mode will fire with the switch state change.
 *
 *
 */
definition(
    name: "Switch Changes Mode",
    namespace: "MichaelStruck",
    author: "Michael Struck",
    description: "Ties a mode to a switch's state. Perfect for use with IFTTT.",
    category: "Convenience",
    iconUrl: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/IFTTT-SmartApps/App1.png",
    iconX2Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/IFTTT-SmartApps/App1@2x.png",
    iconX3Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/IFTTT-SmartApps/App1@2x.png")

preferences {
	page(name: "getPref", title: "Choose Switch and Modes", install:true, uninstall: true) {
    	section("Choose a switch to use...") {
			input "controlSwitch", "capability.switch", title: "Switch", multiple: false, required: true
   		}
		section("Change to which mode when...") {
			input "onMode", "mode", title: "Switch is on", required: false
			input "offMode", "mode", title: "Switch is off", required: false 
		}
		section([mobileOnly:true], "Options") {
			label(title: "Assign a name", required: false, defaultValue: "Switch Changes Mode")
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

def installed() {
	log.debug "Installed with settings: ${settings}"
	init() 
}

def updated() {
	log.debug "Updated with settings: ${settings}"
	unsubscribe()
	init()
}

def init(){
	subscribe(controlSwitch, "switch", "switchHandler")
	}

def switchHandler(evt) {
	if (evt.value == "on") {
    	changeMode(onMode)
    } else {
    	changeMode(offMode)
    }
}

def changeMode(newMode) {

	if (newMode && location.mode != newMode) {
		if (location.modes?.find{it.name == newMode}) {
			setLocationMode(newMode)
		} else {
			log.debug "Unable to change to undefined mode '${newMode}'"
		}
	}
}


//Version/Copyright/Information/Help

private def textAppName() {
	def text = "Switch Changes Mode"
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
    	"Ties a mode to a switch (virtual or real) on/off state. Perfect for use with IFTTT. "+
		"Simple define a switch to be used, then tie the on/off state of the switch to a specific mode. "+
		"Connect the switch to an IFTTT action, and the mode will fire with the switch state change." 
}
