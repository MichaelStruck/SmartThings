/**
 *  Smart Remote-Parent
 *
 *  Version - 1.0.0 10/18/15
 *  Version - 2.0.0 11/29/2015 Modified to allow more scenarios via parent/child app structure
 * 
 *  Copyright 2015 Michael Struck - Uses code from Lighting Director by Tim Slagle & Michael Struck
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
    name: "Smart Remote",
    singleInstance: true,
    namespace: "MichaelStruck",
    author: "Michael Struck",
    description: "Control multiple rooms (scenarios) of light/dimmers based on button pushes.",
    category: "My Apps",
    iconUrl: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Smart-Remote/remote.png",
    iconX2Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Smart-Remote/remote@2x.png",
	iconX3Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Smart-Remote/remote@2x.png"
    )

preferences {
    page(name: "mainPage", title: "Room Scenarios", install: true, uninstall: true,submitOnChange: true) {
            section {
                    app(name: "childScenarios", appName: "Smart Remote-Scenario", namespace: "MichaelStruck", title: "Create New Scenario...", multiple: true)
            }
            section([title:"Options", mobileOnly:true]) {
            	label title:"Assign a name", required:false
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

//----------------------
def installed() {
    initialize()
}

def updated() {
    unsubscribe()
    initialize()
}

def initialize() {
    childApps.each {child ->
		log.info "Installed Scenario: ${child.label}"
    }
}

//Version/Copyright/Information/Help

private def textAppName() {
	def text = "Smart Remote-BETA"
}	

private def textVersion() {
    def text = "Version 2.0.0 (11/29/2015)"
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
        "Within each scenario you can select a remote to control a set of lights. " +
        "Each scenario can control dimmers or switches and can also be restricted " +
        "to modes."
}
