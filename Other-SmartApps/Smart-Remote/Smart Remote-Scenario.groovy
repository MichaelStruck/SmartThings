/**
 *  Smart Remote
 *
 *  Version 1.0.0 (11/29/15) - Initial release of child app
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
 
import groovy.json.JsonSlurper
 
definition(
    name: "Smart Remote-Scenario",
    namespace: "MichaelStruck",
    author: "Michael Struck",
    description: "Child app (do not publish) that allows multiple rooms (scenarios) of light/dimmers based on button pushes.",
    category: "Convenience",
    iconUrl: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Smart-Remote/remote.png",
    iconX2Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Smart-Remote/remote@2x.png",
	iconX3Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Smart-Remote/remote@2x.png"
    )

preferences {
    page name:"pageSetup"
}

// Show setup page
def pageSetup() {
	dynamicPage(name: "pageSetup", install: true, uninstall: true) {
		section("Name your scenario") {
			label title:"Scenario Name", required: true
    	}
    	section("Devices included in the scenario") {
        	href "pageButtonControlA", title: "When a button is pressed on a controller...", description: buttonDesc(A_buttonDevice, A_buttonPress), state: greyOut(A_buttonDevice, A_buttonPress)
        	input "A_switches", "capability.switch",title: "Toggle the following switches...", multiple: true, required: false
        	input "A_dimmers", "capability.switchLevel", title: "Toggle the following dimmers...", multiple: true, required:false, submitOnChange:true
        	input "A_colorControls", "capability.colorControl", title: "Toggle the following colored lights...",  multiple: true, required: false, submitOnChange:true
		}

       	if (A_dimmers || A_colorControls) {
        	section("Lighting Settings") {
        		if (A_dimmers){
					href "levelInputA", title: "Dimmer Options", description: getLevelLabel(A_levelDimOn, A_levelDimOff, A_calcOn), state: "complete"
      			}
        		if (A_colorControls){
      				href "colorInputA", title: "Color Options", description: getColorLabel(A_levelDimOnColor, A_levelDimOffColor, A_calcOnColor, A_color), state: "complete"
       			}
        	}
        }
		section("Restrictions") {            
			input name: "A_mode", type: "mode", title: "Only during the following modes...", multiple: true, required: false
		}
        section("About ${textAppName()}") { 
			paragraph "${textVersion()}\n${textCopyright()}"
    	}
    }
}

//----------------------
def installed() {
    initialize()
}

def updated() {
    unschedule()
    unsubscribe()
    initialize()
}

def initialize() {
	if (A_buttonDevice && A_buttonPress){
    	subscribe(A_buttonDevice, "button.pushed", buttonHandler_A)
	}
}
// A Event
def onEventA() {
    def levelSetOn = A_levelDimOn ? A_levelDimOn : 100
	def levelSetOnColor = A_levelDimOnColor ? A_levelDimOnColor : 100
    def levelSetOff = A_levelDimOff ? A_levelDimOff : 0
    def levelSetOffColor = A_levelDimOffColor ? A_levelDimOffColor : 0
     
    if (!state.A_switchesOn) {
		A_switches?.on()
        A_dimmers?.setLevel(levelSetOn)
        setColoredLights(A_colorControls, A_color, levelSetOnColor)
        state.A_switchesOn = true
	}
    else {
		A_switches?.off()
        A_dimmers?.setLevel(levelSetOff)
        A_colorControls?.setLevel(levelSetOffColor)
        state.A_switchesOn = false
	}
}

def buttonHandler_A(evt){
    if (!A_mode || A_mode.contains(location.mode)) {
    	def data = new JsonSlurper().parseText(evt.data)
    	def button = data.buttonNumber
		def remoteButton = A_buttonPress as Integer

		if (button == remoteButton) {
        	onEventA()
   		}
	}
}

//Common Methods

def greyOut(param1, param2){
	def result = param1 || param2 ? "complete" : ""
}

def getTitle(scenario) {
	def title = scenario ? scenario : "Empty"
}

def buttonDesc(button, num){
	def desc = button && num ? "${button}, button: ${num}" : "Tap to set button controller settings"
}

def getLevelLabel(on, off, calcOn) {
	def levelLabel="'On' level: "
	if (!on) {
		on= 100
	}
	if (!off) {
		off = 0
    }
	levelLabel = levelLabel + "${on}%"
	levelLabel = levelLabel + "\n'Off' level: ${off}%" 
    levelLabel
}

def getColorLabel(on, off, calcOn, color) {
    def levelLabel = color ? "Color: ${color}" : "Color: Soft White" 
    levelLabel += "\n'On' level: "
	if (!on) {
		on= 100
	}
	if (!off) {
		off = 0
    }
	levelLabel = levelLabel + "${on}%"
	levelLabel = levelLabel + "\n'Off' level: ${off}%" 
    levelLabel
}

private setColoredLights(colorControls, color, lightLevel){
   	def chooseColor = color ? "${color}" : "Soft White"
    def hueColor = 0
	def saturationLevel = 100
	switch(chooseColor) {
		case "White":
			hueColor = 52
			saturationLevel = 19
			break;
		case "Daylight":
			hueColor = 52
			saturationLevel = 16
			break;
		case "Soft White":
			hueColor = 23
			saturationLevel = 56
			break;
		case "Warm White":
			hueColor = 13
			saturationLevel = 30 
			break;
		case "Blue":
			hueColor = 64
			break;
		case "Green":
			hueColor = 37
			break;
		case "Yellow":
			hueColor = 16
			break;
		case "Orange":
			hueColor = 8
			break;
		case "Purple":
			hueColor = 78
			break;
		case "Pink":
			hueColor = 87
			break;
		case "Red":
			hueColor = 100
			break;
	}
    def newValue = [hue: hueColor as Integer, saturation: saturationLevel as Integer, level: lightLevel as Integer]
    colorControls?.setColor(newValue)
}


page(name: "levelInputA", title: "Set dimmers options...") {
		section {
			input name: "A_levelDimOn", type: "number", title: "On Level", multiple: false, required: false
        	input name: "A_levelDimOff", type: "number", title: "Off Level", multiple: false, required: false
        }
}
page(name: "colorInputA", title: "Set colored light options...") {
		section {
			input "A_color", "enum", title: "Choose a color", required: false, multiple:false, options: [
					["Soft White":"Soft White"],
					["White":"White - Concentrate"],
					["Daylight":"Daylight - Energize"],
					["Warm White":"Warm White - Relax"],
					"Red","Green","Blue","Yellow","Orange","Purple","Pink"]
            input "A_levelDimOnColor", "number", title: "On Level", multiple: false, required: false
        	input "A_levelDimOffColor", "number", title: "Off Level", multiple: false, required: false
			input "A_calcOnColor", "bool", title: "Calculate 'on' level via lux", defaultValue: false
        }
}

page(name: "pageButtonControlA", title: "Button Controller Setup") {
	section {
    	input "A_buttonDevice", "capability.button", title: "Button Controller", multiple: false, required: false
        input "A_buttonPress", "enum", title: "Which button...", options: [1:"1", 2:"2", 3:"3", 4:"4"], multiple: false, required: false
	}
}

//Version/Copyright

private def textAppName() {
	def text = "Smart Remote-Scenario"
}	

private def textVersion() {
    def text = "Version 1.0.0 (11/28/2015)"
}

private def textCopyright() {
    def text = "Copyright © 2015 Michael Struck"
}
