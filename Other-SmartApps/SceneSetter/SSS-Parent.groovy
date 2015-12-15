/**
 *  Sebastian's Scene Setter-Parent
 *
 *  Version - 1.0.1 12/15/15
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
    name: "Sebastian's Scene Setter",
    singleInstance: true,
    namespace: "MichaelStruck",
    author: "Michael Struck",
    description: "Control multiple scenes, which can control multiple colored bulbs",
    category: "My Apps",
    iconUrl: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Lighting-Director/LightingDirector.png",
    iconX2Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Lighting-Director/LightingDirector@2x.png",
    iconX3Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Lighting-Director/LightingDirector@2x.png")

preferences {
    page(name: "mainPage", title: "Scenes", install: true, uninstall: true,submitOnChange: true) {
            section {
            	app(name: "childScenes", appName: "Sebastian's Scene Setter-Scene", namespace: "MichaelStruck", title: "Create New Scene...", multiple: false)
            }
            section {
                app(name: "childScenes", appName: "Sebastian's Scene Setter-Scene", namespace: "MichaelStruck", title: "Create New Scene...", multiple: true)
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
		log.info "Installed Scenes: ${child.label}"
    }
}

//Version/Copyright/Information/Help

private def textAppName() {
	def text = "Sebastian's Scene Setter"
}	

private def textVersion() {
    def text = "Version 1.0.1 (12/15/2015)"
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
        "Within each scene you can select a set of lights and paramenters that you can control via a certain trigger (i.e. virtual switch). " +
        "Each scenario can control dimmers or switches and can also be restricted to modes." 
}
