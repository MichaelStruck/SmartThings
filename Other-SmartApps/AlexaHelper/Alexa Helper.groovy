/**
 *  Alexa Helper-Parent
 *
 *  Copyright 2015 Michael Struck
 *  Version 3.1.0 11/27/15
 * 
 *  Version 1.0.0 - Initial release
 *  Version 2.0.0 - Added 6 slots to allow for one app to control multiple on/off actions
 *  Version 2.0.1 - Changed syntax to reflect SmartThings Routines (instead of Hello, Home Phrases)
 *  Version 2.1.0 - Added timers to the first 4 slots to allow for delayed triggering of routines or modes
 *  Version 2.2.1 - Allow for on/off control of switches and changed the UI slightly to allow for other controls in the future
 *  Version 2.2.2 - Fixed an issue with slot 4
 *  Version 3.0.0 - Allow for parent/child 'slots'
 *  Version 3.1.0 - Added ability to control as thermostat
 *  Version 3.1.1 - Refined thermostat controls and GUI (thanks to @SDBOBRESCU "Bobby")
 * 
 *  Uses code from Lighting Director by Tim Slagle & Michael Struck
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
    name: "Alexa Helper",
    singleInstance: true,
    namespace: "MichaelStruck",
    author: "Michael Struck",
    description: "Allows for routines or modes to be tied to various switch's state controlled by Alexa (Amazon Echo).",
    category: "My Apps",
    iconUrl: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/AlexaHelper/Alexa.png",
    iconX2Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/AlexaHelper/Alexa@2x.png",
    iconX3Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/AlexaHelper/Alexa@2x.png")

preferences {
    page name:"mainPage"
}

//Show main page
def mainPage() {
    dynamicPage(name: "mainPage", title: "Alexa Helper Scenarios", install: true, uninstall: true) {
            section {
            	app(name: "childScenarios", appName: "Alexa Helper-Scenario", namespace: "MichaelStruck", title: "Create New Alexa Scenario...", multiple: true)
            }
            section ("Thermostat") {
            	href "tstatControl", title: "Thermostat Controls", description: getDesc(), state: greyOut(vDimmer, tstat)
            }
            section([title:"Options", mobileOnly:true]) {
            	label title:"Assign a name", required:false
            	href "pageAbout", title: "About ${textAppName()}", description: "Tap to get application version, license and instructions"
        	}
    }
}

page(name: "tstatControl", title: "Thermostat Controls"){
   	section {
    	input "vDimmer", "capability.switchLevel", title: "Alexa Dimmer Switch", multiple: false, required:false
		input "tstat", "capability.thermostat", title: "Thermostat To Control", multiple: false , required: false
    	input "upLimit", "number", title: "Thermostat Upper Limit", required: false
    	input "lowLimit", "number", title: "Thermostat Lower Limit", required: false
        input "autoControl", "bool", title: "Control when thermostat in 'Auto' mode", defaultValue: false
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
	if (vDimmer && tstat) {
    	subscribe (vDimmer, "level", "thermoHandler")
	}
}
//Thermostat Handler
def thermoHandler(evt){
    // Get settings between limits
    def tstatLevel = vDimmer.currentValue("level")
    if (upLimit && vDimmer.currentValue("level") > upLimit){
    	tstatLevel = upLimit
    }
    if (lowLimit && vDimmer.currentValue("level") < lowLimit){
    	tstatLevel = lowLimit
    }
	//Turn thermostat to proper level depending on mode
    def tstatMode=tstat.currentValue("thermostatMode")
    if (tstatMode == "heat") {
        tstat.setHeatingSetpoint(tstatLevel)	
    }
    if (tstatMode == "cool") {
        tstat.setCoolingSetpoint(tstatLevel)	
    }
    if (tstatMode == "auto" && autoControl){
    	tstat.setHeatingSetpoint(tstatLevel)
        tstat.setCoolingSetpoint(tstatLevel)
    }
    log.debug "Thermostat set to ${tstatLevel}"
}

//Common Methods

def getDesc(){
    def result = "Tap to setup theromstat controls"
    if (vDimmer && tstat) {
		result = "${vDimmer} controls ${tstat}"
        if (upLimit) {
           	result += "\nLimits: Not greater than ${upLimit}"
		}
		if (lowLimit) {
        	result += " and no lower than ${lowLimit}"
        }
        if (autoControl){
        	result += "\nControl when thermostat in 'Auto' mode"
        }    
	}
    result
}

def greyOut(param1,param2){
    def result = param1 && param2 ? "complete" : ""
}

//Version/Copyright/Information/Help

private def textAppName() {
	def text = "Alexa Helper"
}	

private def textVersion() {
    def text = "Version 3.1.1 (12/02/2015)"
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
		"Ties SmartThings routines, modes or switches to the on/off state of a specifc switch. You may also control a thermostat using a dimmer control. "+
		"Perfect for use with Alexa.\n\nTo use, first create the required momentary button tiles or virtual switches/dimmers from the SmartThings IDE. "+
		"You may also use any physical switches already associated with SmartThings. Include these switches within the Echo/SmartThings app, then discover the switches on the Echo. "+
		"For on/off or momentary buttons, add a scenario and choose the discovered switch to be monitored and tie the on/off state of that switch to a specific routine, mode or on/off state of other switches. "+
		"The routine, mode or switches will fire with the switch state change, except in cases where you have a delay specified. This time delay is optional. "+
		"\n\nPlease note that if you are using a momentary switch you should only define the 'on' action within each scenario.\n\n" +
		"To control a thermostat, tap the thermostat controls and choose a dimmer switch (usually a virtual dimmer) and the thermostat you wish to control. "+
		"You can also limit the range the thermostat will reach (for example, even if you accidently set the dimmer to 100, the value sent to the "+
		"thermostat could be limited to 72)."
}
