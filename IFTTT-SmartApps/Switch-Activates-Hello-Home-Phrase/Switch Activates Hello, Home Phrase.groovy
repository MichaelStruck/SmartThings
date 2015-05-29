/**
 *  Switch Activates Hello, Home Phrase
 *
 *  Copyright 2015 Michael Struck
 *  Version 1.0.1 3/8/15
 *  Version 1.0.2 4/24/15 added the ability to rename the app and limit it to certain modes
 *  Version 1.0.3 5/29/15 Added an About Screen
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
 *  Ties a Hello, Home phrase to a switch's (virtual or real) on/off state. Perfect for use with IFTTT.
 *  Simple define a switch to be used, then tie the on/off state of the switch to a specific Hello, Home phrases.
 *  Connect the switch to an IFTTT action, and the Hello, Home phrase will fire with the switch state change.
 *
 *
 */
definition(
    name: "Switch Activates Home Phrase",
    namespace: "MichaelStruck",
    author: "Michael Struck",
    description: "Ties a Hello, Home phrase to a switch's state. Perfect for use with IFTTT.",
    category: "Convenience",
    iconUrl: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/IFTTT-SmartApps/App1.png",
    iconX2Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/IFTTT-SmartApps/App1@2x.png",
    iconX3Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/IFTTT-SmartApps/App1@2x.png")


preferences {
	page(name: "getPref")
}
	
def getPref() {    
    dynamicPage(name: "getPref", title: "Choose Switch and Phrases", install:true, uninstall: true) {
    section("Choose a switch to use...") {
		input "controlSwitch", "capability.switch", title: "Switch", multiple: false, required: true
    }
   
    def phrases = location.helloHome?.getPhrases()*.label
		if (phrases) {
        	phrases.sort()
			section("Perform which phrase when...") {
				log.trace phrases
				input "phrase_on", "enum", title: "Switch is on", required: true, options: phrases
				input "phrase_off", "enum", title: "Switch is off", required: true, options: phrases
			}
		}
		section([mobileOnly:true], "Options") {
			label(title: "Assign a name", required: false)
            mode title: "Set for specific mode(s)", required: false
            href "pageAbout", title: "About ${textAppName()}", description: "Tap to get version and license information"
		}
    }
}

page(name: "pageAbout", title: "About ${textAppName()}") {
	section {
		paragraph "${textVersion()}\n${textCopyright()}\n\n${textHelp()}\n"
	}
	section("License") {
		paragraph textLicense()
	}
}

def installed() {
	log.debug "Installed with settings: ${settings}"
	subscribe(controlSwitch, "switch", "switchHandler")
}

def updated() {
	log.debug "Updated with settings: ${settings}"
	unsubscribe()
	subscribe(controlSwitch, "switch", "switchHandler")
}

def switchHandler(evt) {
	if (evt.value == "on") {
    	location.helloHome.execute(settings.phrase_on)
    } else {
    	location.helloHome.execute(settings.phrase_off)
    }
}

//Version/Copyright/Information/Help

private def textAppName() {
	def text = "Switch Activates Hello, Home Phrase"
}	

private def textVersion() {
    def text = "Version 1.0.3 (05/29/2015)"
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
    	"Instructions:\nTies a Hello, Home phrase to a switch (virtual or real) on/off state. Perfect for use with IFTTT. "+
		"Simple define a switch to be used, then tie the on/off state of the switch to a specific Hello, Home phrases. "+
		"Connect the switch to an IFTTT action, and the Hello, Home phrase will fire with the switch state change." 
}
