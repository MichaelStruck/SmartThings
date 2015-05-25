/**
 *  Ivee Helper-Big Switch
 *  Version 1.0 3/18/15
 *  Version 1.01 4/9/15 - Added options sub heading to last section of preferences
 *  Version 1.0.2 5/25/15 - Added About screen
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
	page name: "getPref"
    page name: "pageAbout"
}

def getPref() {
	dynamicPage(name: "getPref", install:true, uninstall: true) {
		section("Define a 'Master Switch' which Ivee will use.") {
			input "master", "capability.switch", multiple: false, title: "Master Switch", required: true
   		}    
   		section("When Ivee turns on the Master Switch, turn on these lights/switches...") {
			input "lightsOn", "capability.switch", multiple: true, title: "Lights/Switches", required: false
    	} 
    	section("When Ivee turns off the Master Switch, turn off these lights/switches...") {
			input "lightsOff", "capability.switch", multiple: true, title: "Lights/Switches", required: false
    	}
    	section([mobileOnly:true], "Options") {
			label(title: "Assign a name", required: false, defaultValue: "Ivee Helper-Big Switch")
            mode title: "Set for specific mode(s)", required: false
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
	subscribe(master, "switch.on", "onHandler")
    subscribe(master, "switch.off", "offHandler")
}

def onHandler(evt) {
	lightsOn.on()
}

def offHandler(evt) {
	lightsOff.off()
}


//Version/Copyright/Information/Help

private def textAppName() {
	def text = "Ivee Helper - Big Switch"
}	

private def textVersion() {
    def text = "Version 1.0.2 (05/25/2015)"
}

private def textCopyright() {
    def text = "Copyright © 2015 Michael Struck"
}

private def textLicense() {
    def text =
        "This program is free software: you can redistribute it and/or " +
        "modify it under the terms of the GNU General Public License as " +
        "published by the Free Software Foundation, either version 3 of " +
        "the License, or (at your option) any later version.\n\n" +
        "This program is distributed in the hope that it will be useful, " +
        "but WITHOUT ANY WARRANTY; without even the implied warranty of " +
        "MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU " +
        "General Public License for more details.\n\n" +
        "You should have received a copy of the GNU General Public License " +
        "along with this program. If not, see <http://www.gnu.org/licenses/>."
}

private def textHelp() {
	def text =
    	"Instructions:\nChoose a switch that will be considered the 'master switch' to control a  " +
        "group of other switches. It is recommended this master switch be a virtual switch called 'All Lights', but it can be " +
        "physical device as well. You will need to associate the master switch with the Ivee Talking Alarm Clock (online at the Ivee web site). "+ 
        "Then, choose various switches you would like to control "+
        "with the the on or off states of the master switch. You can then toggle these groups of switches by saying " +
        "'Hello Ivee, turn <on/off> ALL LIGHTS'."
}
