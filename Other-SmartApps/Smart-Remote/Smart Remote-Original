/**
 *  Smart Remote
 *
 *  Version - 1.0.0 10/18/15
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
    name: "Smart Remote-BETA",
    namespace: "MichaelStruck",
    author: "Michael Struck",
    description: "Control up to 4 rooms (scenarios) of light/dimmers based on button pushes.",
    category: "Convenience",
    iconUrl: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Smart-Remote/remote.png",
    iconX2Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Smart-Remote/remote@2x.png",
	iconX3Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Smart-Remote/remote@2x.png"
    )

preferences {
    page name:"pageSetup"
    page name:"pageSetupScenarioA"
    page name:"pageSetupScenarioB"
    page name:"pageSetupScenarioC"
    page name:"pageSetupScenarioD"
}

// Show setup page
def pageSetup() {
	dynamicPage(name: "pageSetup", install: true, uninstall: true) {
        section("Setup Menu") {
            href "pageSetupScenarioA", title: getTitle(ScenarioNameA), description: "", state: greyOut(ScenarioNameA)
            href "pageSetupScenarioB", title: getTitle(ScenarioNameB), description: "", state: greyOut(ScenarioNameB)
            href "pageSetupScenarioC", title: getTitle(ScenarioNameC), description: "", state: greyOut(ScenarioNameC)
			href "pageSetupScenarioD", title: getTitle(ScenarioNameD), description: "", state: greyOut(ScenarioNameD)
            }
        section([title:"Options", mobileOnly:true]) {
            label title:"Assign a name", required:false
            href "pageAbout", title: "About ${textAppName()}", description: "Tap to get application version, license and instructions"
        }
    }
}

// Show "pageSetupScenarioA" page
def pageSetupScenarioA() {
    dynamicPage(name: "pageSetupScenarioA") {
		section("Name your scenario") {
           	input name:"ScenarioNameA", type: "text", title: "Scenario Name", multiple: false, required: false, defaultValue: empty
    	}

    	section("Devices included in the scenario") {
        	href "pageButtonControlA", title: "When a button is pressed on a controller...", description: buttonDesc(A_buttonDevice, A_buttonPress), state: greyOut2(A_buttonDevice, A_buttonPress)
        	input "A_switches", "capability.switch",title: "Toggle the following switches...", multiple: true, required: false
        	input "A_dimmers", "capability.switchLevel", title: "Toggle the following dimmers...", multiple: true, required:false, submitOnChange:true
        	input "A_colorControls", "capability.colorControl", title: "Toggle the following colored lights...",  multiple: true, required: false, submitOnChange:true
		}

		section("Lighting settings") {
        	if (A_dimmers){
        		href "levelInputA", title: "Dimmer Options", description: getLevelLabel(A_levelDimOn, A_levelDimOff, A_calcOn), state: "complete"
        	}
        	if (A_colorControls){
        		href "colorInputA", title: "Color Options", description: getColorLabel(A_levelDimOnColor, A_levelDimOffColor, A_calcOnColor, A_color), state: "complete"
        	}
		}
            
		section("Restrictions") {            
			input name: "A_mode", type: "mode", title: "Only during the following modes...", multiple: true, required: false
		}
    }
}

// Show "pageSetupScenarioB" page
def pageSetupScenarioB() {
    dynamicPage(name: "pageSetupScenarioB") {
		section("Name your scenario") {
            input name:"ScenarioNameB", type: "text", title: "Scenario Name", multiple: false, required: false, defaultValue: empty
	    }

        section("Devices included in the scenario") {
        	href "pageButtonControlB", title: "When a button is pressed on a controller...", description: buttonDesc(B_buttonDevice, B_buttonPress), state: greyOut2(B_buttonDevice, B_buttonPress)
        	input "B_switches", "capability.switch",title: "Toggle the following switches...", multiple: true, required: false
        	input "B_dimmers", "capability.switchLevel", title: "Toggle the following dimmers...", multiple: true, required:false, submitOnChange:true
        	input "B_colorControls", "capability.colorControl", title: "Toggle the following colored lights...",  multiple: true, required: false, submitOnChange:true
		}

		section("Lighting settings") {
        	if (B_dimmers){
        		href "levelInputB", title: "Dimmer Options", description: getLevelLabel(B_levelDimOn, B_levelDimOff, B_calcOn), state: "complete"
        	}
        	if (B_colorControls){
        		href "colorInputB", title: "Color Options", description: getColorLabel(B_levelDimOnColor, B_levelDimOffColor, B_calcOnColor, B_color), state: "complete"
        	}
		}
            
		section("Restrictions") {            
			input name: "B_mode", type: "mode", title: "Only during the following modes...", multiple: true, required: false
		}
    }
}

// Show "pageSetupScenarioC" page
def pageSetupScenarioC() {
    dynamicPage(name: "pageSetupScenarioC") {
    	section("Name your scenario") {
			input name:"ScenarioNameC", type: "text", title: "Scenario Name", multiple: false, required: false, defaultValue: empty
    	}
        
        section("Devices included in the scenario") {
        	href "pageButtonControlC", title: "When a button is pressed on a controller...", description: buttonDesc(C_buttonDevice, C_buttonPress), state: greyOut2(C_buttonDevice, C_buttonPress)
        	input "C_switches", "capability.switch",title: "Toggle the following switches...", multiple: true, required: false
        	input "C_dimmers", "capability.switchLevel", title: "Toggle the following dimmers...", multiple: true, required:false, submitOnChange:true
        	input "C_colorControls", "capability.colorControl", title: "Toggle the following colored lights...",  multiple: true, required: false, submitOnChange:true
		}

		section("Lighting settings") {
        	if (C_dimmers){
        		href "levelInputC", title: "Dimmer Options", description: getLevelLabel(C_levelDimOn, C_levelDimOff, C_calcOn), state: "complete"
        	}
        	if (C_colorControls){
        		href "colorInputC", title: "Color Options", description: getColorLabel(C_levelDimOnColor, C_levelDimOffColor, C_calcOnColor, C_color), state: "complete"
        	}
		}
            
		section("Restrictions") {            
			input name: "C_mode", type: "mode", title: "Only during the following modes...", multiple: true, required: false
		}

    }
}

// Show "pageSetupScenarioD" page
def pageSetupScenarioD() {
    dynamicPage(name: "pageSetupScenarioD") {
    	section("Name your scenario") {
    		input name:"ScenarioNameD", type: "text", title: "Scenario Name", multiple: false, required: false, defaultValue: empty
    	}
    	
        section("Devices included in the scenario") {
        	href "pageButtonControlD", title: "When a button is pressed on a controller...", description: buttonDesc(D_buttonDevice, D_buttonPress), state: greyOut2(D_buttonDevice, D_buttonPress)
        	input "D_switches", "capability.switch",title: "Toggle the following switches...", multiple: true, required: false
        	input "D_dimmers", "capability.switchLevel", title: "Toggle the following dimmers...", multiple: true, required:false, submitOnChange:true
        	input "D_colorControls", "capability.colorControl", title: "Toggle the following colored lights...",  multiple: true, required: false, submitOnChange:true
		}

		section("Lighting settings") {
			if (D_dimmers){
        		href "levelInputD", title: "Dimmer Options", description: getLevelLabel(D_levelDimOn, D_levelDimOff, D_calcOn), state: "complete"
        	}
        	if (D_colorControls){
        		href "colorInputD", title: "Color Options", description: getColorLabel(D_levelDimOnColor, D_levelDimOffColor, D_calcOnColor, D_color), state: "complete"
        	}
		}
            
		section("Restrictions") {            
			input name: "D_mode", type: "mode", title: "Only during the following modes...", multiple: true, required: false
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
    unschedule()
    unsubscribe()
    initialize()
}

def initialize() {
	if (A_buttonDevice && A_buttonPress){
    	subscribe(A_buttonDevice, "button.pushed", buttonHandler_A)
	}
	if (B_buttonDevice && B_buttonPress){
    	subscribe(B_buttonDevice, "button.pushed", buttonHandler_B)
	}
	if (C_buttonDevice && C_buttonPress){
    	subscribe(C_buttonDevice, "button.pushed", buttonHandler_C)
	}
	if (D_buttonDevice && D_buttonPress){
    	subscribe(D_buttonDevice, "button.pushed", buttonHandler_D)
	}
}
// A Event
def onEventA() {
    log.debug("Running '${ScenarioNameA}'")
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

// B Event
def onEventB() {
    log.debug("Running '${ScenarioNameB}'")
    def levelSetOn = B_levelDimOn ? B_levelDimOn : 100
	def levelSetOnColor = B_levelDimOnColor ? B_levelDimOnColor : 100
    def levelSetOff = B_levelDimOff ? B_levelDimOff : 0
    def levelSetOffColor = B_levelDimOffColor ? B_levelDimOffColor : 0
     
    if (!state.B_switchesOn) {
		B_switches?.on()
        B_dimmers?.setLevel(levelSetOn)
        setColoredLights(B_colorControls, B_color, levelSetOnColor)
        state.B_switchesOn = true
	}
    else {
		B_switches?.off()
        B_dimmers?.setLevel(levelSetOff)
        B_colorControls?.setLevel(levelSetOffColor)
        state.B_switchesOn = false
	}
}

def buttonHandler_B(evt){
    if (!B_mode || B_mode.contains(location.mode)) {
    	def data = new JsonSlurper().parseText(evt.data)
    	def button = data.buttonNumber
		def remoteButton = B_buttonPress as Integer
   
		if (button == remoteButton) {
        	onEventB()
   		}
	}
}

// C Event
def onEventC() {
    log.debug("Running '${ScenarioNameC}'")
    def levelSetOn = C_levelDimOn ? C_levelDimOn : 100
	def levelSetOnColor = C_levelDimOnColor ? C_levelDimOnColor : 100
    def levelSetOff = C_levelDimOff ? C_levelDimOff : 0
    def levelSetOffColor = C_levelDimOffColor ? C_levelDimOffColor : 0
     
    if (!state.C_switchesOn) {
		C_switches?.on()
        C_dimmers?.setLevel(levelSetOn)
        setColoredLights(C_colorControls, C_color, levelSetOnColor)
        state.C_switchesOn = true
	}
    else {
		C_switches?.off()
        C_dimmers?.setLevel(levelSetOff)
        C_colorControls?.setLevel(levelSetOffColor)
        state.C_switchesOn = false
	}
}

def buttonHandler_C(evt){
    if (!C_mode || C_mode.contains(location.mode)) {
    	def data = new JsonSlurper().parseText(evt.data)
    	def button = data.buttonNumber
		def remoteButton = C_buttonPress as Integer
   
		if (button == remoteButton) {
        	onEventC()
   		}
	}
}

// D Event
def onEventD() {
    log.debug("Running '${ScenarioNameD}'")
    def levelSetOn = D_levelDimOn ? D_levelDimOn : 100
	def levelSetOnColor = D_levelDimOnColor ? d_levelDimOnColor : 100
    def levelSetOff = D_levelDimOff ? D_levelDimOff : 0
    def levelSetOffColor = D_levelDimOffColor ? D_levelDimOffColor : 0
     
    if (!state.D_switchesOn) {
		D_switches?.on()
        D_dimmers?.setLevel(levelSetOn)
        setColoredLights(D_colorControls, D_color, levelSetOnColor)
        state.D_switchesOn = true
	}
    else {
		D_switches?.off()
        D_dimmers?.setLevel(levelSetOff)
        D_colorControls?.setLevel(levelSetOffColor)
        state.D_switchesOn = false
	}
}

def buttonHandler_D(evt){
    if (!D_mode || D_mode.contains(location.mode)) {
    	def data = new JsonSlurper().parseText(evt.data)
    	def button = data.buttonNumber
		def remoteButton = D_buttonPress as Integer
   
		if (button == remoteButton) {
        	onEventD()
   		}
	}
}

//Common Methods

def greyOut(param){
	def result = param ? "complete" : ""
}

def greyOut2(param1, param2){
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

page(name: "levelInputB", title: "Set dimmers options...") {
		section {
			input name: "B_levelDimOn", type: "number", title: "On Level", multiple: false, required: false
        	input name: "B_levelDimOff", type: "number", title: "Off Level", multiple: false, required: false
        }
}

page(name: "levelInputC", title: "Set dimmers options...") {
		section {
			input name: "C_levelDimOn", type: "number", title: "On Level", multiple: false, required: false
        	input name: "C_levelDimOff", type: "number", title: "Off Level", multiple: false, required: false
        }
}

page(name: "levelInputD", title: "Set dimmers options...") {
		section {
			input name: "D_levelDimOn", type: "number", title: "On Level", multiple: false, required: false
        	input name: "D_levelDimOff", type: "number", title: "Off Level", multiple: false, required: false
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

page(name: "colorInputB", title: "Set colored light options...") {
		section {
			input "B_color", "enum", title: "Choose a color", required: false, multiple:false, options: [
					["Soft White":"Soft White"],
					["White":"White - Concentrate"],
					["Daylight":"Daylight - Energize"],
					["Warm White":"Warm White - Relax"],
					"Red","Green","Blue","Yellow","Orange","Purple","Pink"]
            input "B_levelDimOnColor", "number", title: "On Level", multiple: false, required: false
        	input "B_levelDimOffColor", "number", title: "Off Level", multiple: false, required: false
			input "B_calcOnColor", "bool",title: "Calculate 'on' level via lux", defaultValue: false
        }
}

page(name: "colorInputC", title: "Set colored light options...") {
		section {
			input "C_color", "enum", title: "Choose a color", required: false, multiple:false, options: [
					["Soft White":"Soft White"],
					["White":"White - Concentrate"],
					["Daylight":"Daylight - Energize"],
					["Warm White":"Warm White - Relax"],
					"Red","Green","Blue","Yellow","Orange","Purple","Pink"]
            input "C_levelDimOnColor", "number", title: "On Level", multiple: false, required: false
        	input "C_levelDimOffColor", "number", title: "Off Level", multiple: false, required: false
			input "C_calcOnColor", "bool",title: "Calculate 'on' level via lux", defaultValue: false
        }
}

page(name: "colorInputD", title: "Set colored light options...") {
		section {
			input "D_color", "enum", title: "Choose a color", required: false, multiple:false, options: [
					["Soft White":"Soft White"],
					["White":"White - Concentrate"],
					["Daylight":"Daylight - Energize"],
					["Warm White":"Warm White - Relax"],
					"Red","Green","Blue","Yellow","Orange","Purple","Pink"]
            input "D_levelDimOnColor", "number", title: "On Level", multiple: false, required: false
        	input "D_levelDimOffColor", "number", title: "Off Level", multiple: false, required: false
			input "D_calcOnColor", "bool",title: "Calculate 'on' level via lux", defaultValue: false
        }
}

page(name: "pageButtonControlA", title: "Button Controller Setup") {
	section {
    	input "A_buttonDevice", "capability.button", title: "Button Controller", multiple: false, required: false
        input "A_buttonPress", "enum", title: "Which button...", options: [1:"1", 2:"2", 3:"3", 4:"4"], multiple: false, required: false
	}
}

page(name: "pageButtonControlB", title: "Button Controller Setup") {
	section {
    	input "B_buttonDevice", "capability.button", title: "Button Controller", multiple: false, required: false
        input "B_buttonPress", "enum", title: "Which button...", options: [1:"1", 2:"2", 3:"3", 4:"4"], multiple: false, required: false
	}
}

page(name: "pageButtonControlC", title: "Button Controller Setup") {
	section {
    	input "C_buttonDevice", "capability.button", title: "Button Controller", multiple: false, required: false
        input "C_buttonPress", "enum", title: "Which button...", options: [1:"1", 2:"2", 3:"3", 4:"4"], multiple: false, required: false
	}
}

page(name: "pageButtonControlD", title: "Button Controller Setup") {
	section {
    	input "D_buttonDevice", "capability.button", title: "Button Controller", multiple: false, required: false
        input "D_buttonPress", "enum", title: "Which button...", options: [1:"1", 2:"2", 3:"3", 4:"4"], multiple: false, required: false
	}
}

//Version/Copyright/Information/Help

private def textAppName() {
	def text = "Smart Remote-BETA"
}	

private def textVersion() {
    def text = "Version 1.0.0 (10/18/2015)"
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
