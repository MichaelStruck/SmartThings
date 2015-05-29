/**
 *  Smart Bathroom Ventilation
 *
 *  Version - 1.0 5/25/15
 * 
 *  Copyright 2015 Michael Struck - Uses code from Lighting Director by Tim Slagle & Michael Struck
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *	The original licensing applies, with the following exceptions:
 *		1.	These modifications may NOT be used without freely distributing all these modifications freely
 *			and without limitation, in source form.	 The distribution may be met with a link to source code
 *			with these modifications.
 *		2.	These modifications may NOT be used, directly or indirectly, for the purpose of any type of
 *			monetary gain.	These modifications may not be used in a larger entity which is being sold,
 *			leased, or anything other than freely given.
 *		3.	To clarify 1 and 2 above, if you use these modifications, it must be a free project, and
 *			available to anyone with "no strings attached."	 (You may require a free registration on
 *			a free website or portal in order to distribute the modifications.)
 *		4.	The above listed exceptions to the original licensing do not apply to the holder of the
 *			copyright of the original work.	 The original copyright holder can use the modifications
 *			to hopefully improve their original work.  In that event, this author transfers all claim
 *			and ownership of the modifications to "SmartThings."
 *
 *
 *	Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *	in compliance with the License. You may obtain a copy of the License at:
 *
 *		http://www.apache.org/licenses/LICENSE-2.0
 *
 *	Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *	on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *	for the specific language governing permissions and limitations under the License.
 *
 */
 
definition(
    name: "Smart Bathroom Ventilation",
    namespace: "MichaelStruck",
    author: "Michael Struck",
    description: "Control up to 4 ventilation fans based on humidity in each respective scenario.",
    category: "Convenience",
    iconUrl: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Smart-Bathroom-Ventilation/BathVent.png",
    iconX2Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Smart-Bathroom-Ventilation/BathVent@2x.png",
    iconX3Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Smart-Bathroom-Ventilation/BathVent@2x.png"
    )

preferences {
	page name:"pageSetup"
	page name:"pageSetupScenarioA"
	page name:"pageSetupScenarioB"
	page name:"pageSetupScenarioC"
	page name:"pageSetupScenarioD"
    page name:"pageAbout"
}

// Show setup page
def pageSetup() {
	dynamicPage(name: "pageSetup", install: true, uninstall: true) {
        section("Setup Menu") {
			href "pageSetupScenarioA", title: getTitle(ScenarioNameA), description: getDesc(ScenarioNameA), state: greyOut(ScenarioNameA)
			href "pageSetupScenarioB", title: getTitle(ScenarioNameB), description: getDesc(ScenarioNameB), state: greyOut(ScenarioNameB)
			href "pageSetupScenarioC", title: getTitle(ScenarioNameC), description: getDesc(ScenarioNameC), state: greyOut(ScenarioNameC)
			href "pageSetupScenarioD", title: getTitle(ScenarioNameD), description: getDesc(ScenarioNameD), state: greyOut(ScenarioNameD)
        }
        section([title:"Options", mobileOnly:true]) {
            label title:"Assign a name", required:false
            href "pageAbout", title: "About ${textAppName()}", description: "Tap to get version and license information"
        }
    }
}

// Show "pageSetupScenarioA" page
def pageSetupScenarioA() {
    dynamicPage(name: "pageSetupScenarioA") {
		section("Devices included in the scenario") {
			input "A_switch","capability.switch", title: "Monitor this light switch...", multiple: false, required: true
			input "A_humidity", "capability.relativeHumidityMeasurement",title: "Monitor the following humidity sensor...", multiple: false, required: true
			input "A_fans", "capability.switch", title: "Control the following ventilation fans...", multiple: true, required: true
		}
		section("Fan settings") {
        	input "A_humidityDelta", title: "Fan(s) turns on when lights are on and humidy rises", "number", required: false, description: "0-50%"
        	input "A_fanTime", title: "Turn off ventilation after...", "enum", required: false, options: [[5:"5 Minutes"],[15:"15 Minutes"],[30:"30 Minutes"],[60:"1 hour"],[98:"Light switch is turned off"],[99:"Humidity drops to or below original value"]]
		}
		section("Restrictions") {            
			input "A_day", "enum", options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"], title: "Only on certain days of the week...",  multiple: true, required:   false
        	input "A_mode", "mode", title: "Only during the following modes...", multiple: true, required: false
		}
		section("Name your scenario") {
            input "ScenarioNameA", "text", title: "Scenario Name", multiple: false, required: false, defaultValue: empty
    	}
    }
}

// Show "pageSetupScenarioB" page
def pageSetupScenarioB() {
    dynamicPage(name: "pageSetupScenarioB") {
		section("Devices included in the scenario") {
    		input "B_switch","capability.switch", title: "Monitor this light switch...", multiple: false, required: true
        	input "B_humidity", "capability.relativeHumidityMeasurement",title: "Monitor the following humidity sensor...", multiple: false, required: true
        	input "B_fans", "capability.switch",title: "Control the following ventilation fans...", multiple: true, required: true
		}
		section("Fan settings") {
        	input "B_humidityDelta", title: "Fan(s) turns on when lights are on and humidy rises", "number", required: false, description: "0-50%"
        	input "B_fanTime", title: "Turn off ventilation after...", "enum", required: false, options: [[5:"5 Minutes"],[15:"15 Minutes"],[30:"30 Minutes"],[60:"1 hour"],[98:"Light switch is turned off"],[99:"Humidity drops to or below original value"]]
		}
		section("Restrictions") {            
			input "B_day", "enum", options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"], title: "Only on certain days of the week...",  multiple: true, required:   false
        	input "B_mode", "mode", title: "Only during the following modes...", multiple: true, required: false
		}
		section("Name your scenario") {
            input "ScenarioNameB", "text", title: "Scenario Name", multiple: false, required: false, defaultValue: empty
    	}
    }
}

// Show "pageSetupScenarioC" page
def pageSetupScenarioC() {
    dynamicPage(name: "pageSetupScenarioC") {
		section("Devices included in the scenario") {
    		input "C_switch","capability.switch", title: "Monitor this light switch...", multiple: false, required: true
        	input "C_humidity", "capability.relativeHumidityMeasurement",title: "Monitor the following humidity sensor...", multiple: false, required: true
        	input "C_fans", "capability.switch",title: "Control the following ventilation fans...", multiple: true, required: true
		}
		section("Fan settings") {
        	input "C_humidityDelta", title: "Fan(s) turns on when lights are on and humidy rises", "number", required: false, description: "0-50%"
        	input "C_fanTime", title: "Turn off ventilation after...", "enum", required: false, options: [[5:"5 Minutes"],[15:"15 Minutes"],[30:"30 Minutes"],[60:"1 hour"],[98:"Light switch is turned off"],[99:"Humidity drops to or below original value"]]
		}
		section("Restrictions") {            
			input "C_day", "enum", options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"], title: "Only on certain days of the week...",  multiple: true, required:   false
        	input "C_mode", "mode", title: "Only during the following modes...", multiple: true, required: false
		}
		section("Name your scenario") {
            input "ScenarioNameC", "text", title: "Scenario Name", multiple: false, required: false, defaultValue: empty
    	}
    }
}

// Show "pageSetupScenarioD" page
def pageSetupScenarioD() {
    dynamicPage(name: "pageSetupScenarioD") {
		section("Devices included in the scenario") {
    		input "D_switch","capability.switch", title: "Monitor this light switch...", multiple: false, required: true
        	input "D_humidity", "capability.relativeHumidityMeasurement",title: "Monitor the following humidity sensor...", multiple: false, required: true
        	input "D_fans", "capability.switch",title: "Control the following ventilation fans...", multiple: true, required: true
		}
		section("Fan settings") {
        	input "D_humidityDelta", title: "Fan(s) turns on when lights are on and humidy rises", "number", required: false, description: "0-50%"
        	input "D_fanTime", title: "Turn off ventilation after...", "enum", required: false, options: [[5:"5 Minutes"],[15:"15 Minutes"],[30:"30 Minutes"],[60:"1 hour"],[98:"Light switch is turned off"],[99:"Humidity drops to or below original value"]]
		}
		section("Restrictions") {            
			input "D_day", "enum", options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"], title: "Only on certain days of the week...",  multiple: true, required:   false
        	input "D_mode", "mode", title: "Only during the following modes...", multiple: true, required: false
		}
		section("Name your scenario") {
            input "ScenarioNameD", "text", title: "Scenario Name", multiple: false, required: false, defaultValue: empty
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

def installed() {
    initialize()
}

def updated() {
    state.triggeredA = false
    state.triggeredB = false
    state.triggeredC = false
    state.triggeredD = false
    unschedule()
    unsubscribe()
    initialize()
}

def initialize() {
	
    if(A_switch) {
        state.A_runTime = A_fanTime ? A_fanTime as Integer : 98
        subscribe(A_switch, "switch.on", onEventA)
    	subscribe(A_switch, "switch.off", offEventA)
    	if (A_humidityDelta || state.A_runTime == 99) {
    		subscribe(A_humidity, "humidity", humidityHandlerA)
		}
	}
    if(B_switch) {
        state.B_runTime = B_fanTime ? B_fanTime as Integer : 98
        subscribe(B_switch, "switch.on", onEventB)
    	subscribe(B_switch, "switch.off", offEventB)
    	if (B_humidityDelta || state.B_runTime == 99) {
    		subscribe(B_humidity, "humidity", humidityHandlerB)
		}
	}
    if(C_switch) {
		state.C_runTime = C_fanTime ? C_fanTime as Integer : 98
        subscribe(C_switch, "switch.on", onEventC)
    	subscribe(C_switch, "switch.off", offEventC)
    	if (C_humidityDelta || state.C_runTime == 99) {
    		subscribe(C_humidity, "humidity", humidityHandlerC)
		}
	}
    if(D_switch) {
		state.D_runTime = D_fanTime ? D_fanTime as Integer : 98
        subscribe(D_switch, "switch.on", onEventD)
    	subscribe(D_switch, "switch.off", offEventD)
    	if (D_humidityDelta || state.D_runTime == 99) {
    		subscribe(D_humidity, "humidity", humidityHandlerD)
		}
	}
}

//Handlers----------------
//A Handlers
def turnOnA(){
    if ((!A_mode || A_mode.contains(location.mode)) && getDayOk(A_day) && A_switch.currentValue("switch")=="on" && !state.triggeredA) {
        log.debug "Ventilation fans turned on in ${ScenarioNameA}."
    	A_fans?.on()
        state.triggeredA = true
        if (state.A_runTime < 98) {
			log.debug "Humidity fans will be turned off in ${state.A_runTime} minutes in ${ScenarioNameA}."
            runIn (state.A_runTime*60, "turnOffA")
        }
	}
}

def turnOffA() {
	log.debug "Ventilation fans turned off in ${ScenarioNameA}."
    A_fans?.off()
    state.triggeredA = false
}

def humidityHandlerA(evt){
    def currentHumidityA =evt.value as Integer
    log.debug "Humidity value is ${currentHumidityA} in ${ScenarioNameA}."
	if (state.humidityLimitA && currentHumidityA > state.humidityLimitA) {
        turnOnA()
    }
	if (state.humidityStartA && currentHumidityA <= state.humidityStartA && state.A_runTime == 99){
    	turnOffA()
    }      
}

def onEventA(evt) {
    def humidityDelta = A_humidityDelta ? A_humidityDelta as Integer : 0
    state.humidityStartA = A_humidity.currentValue("humidity")
    state.humidityLimitA = state.humidityStartA + humidityDelta
    log.debug "Light turned on in ${ScenarioNameA}. Humidity starting value is ${state.humidityStartA} in ${ScenarioNameA}. Ventilation threshold is ${state.humidityLimitA}"
    if (!A_humidityDelta) {
    	turnOnA()
    }
}

def offEventA(evt) {
	def currentHumidityA = A_humidity.currentValue("humidity")
    log.debug "Light turned off in ${ScenarioNameA}. Humidity value is ${currentHumidityA} in ${ScenarioNameA}"
    if (state.A_runTime == 98){
    	turnOffA()
    }
}
//B Handlers
def turnOnB(){
    if ((!B_mode || B_mode.contains(location.mode)) && getDayOk(B_day) && B_switch.currentValue("switch")=="on" && !state.triggeredB ) {
        log.debug "Ventilation fans turned on in ${ScenarioNameB}."
    	B_fans?.on()
        state.triggeredB = true
        if (state.B_runTime < 98) {
			log.debug "Humidity fans will be turned off in ${state.B_runTime} minutes in ${ScenarioNameB}."
            runIn (state.B_runTime*60, "turnOffB")
        }
	}
}

def turnOffB() {
	log.debug "Ventilation fans turned off in ${ScenarioNameB}."
    B_fans?.off()
    state.triggeredB = false
}

def humidityHandlerB(evt){
    def currentHumidityB =evt.value as Integer
    log.debug "Humidity value is ${currentHumidityB} in ${ScenarioNameB}."
	if (state.humidityLimitB && currentHumidityB > state.humidityLimitB) {
        turnOnB()
    }
	if (state.humidityStartB && currentHumidityB <= state.humidityStartB && state.B_runTime == 99){
    	turnOffB()
    }      
}

def onEventB(evt) {
    def humidityDelta = B_humidityDelta ? B_humidityDelta as Integer : 0
    state.humidityStartB = B_humidity.currentValue("humidity")
    state.humidityLimitB = state.humidityStartB + humidityDelta
    log.debug "Light turned on in ${ScenarioNameB}. Humidity starting value is ${state.humidityStartB} in ${ScenarioNameB}. Ventilation threshold is ${state.humidityLimitB}"
    if (!B_humidityDelta) {
    	turnOnB()
    }
}

def offEventB(evt) {
	def currentHumidityB = B_humidity.currentValue("humidity")
    log.debug "Light turned off in ${ScenarioNameB}. Humidity value is ${currentHumidityB} in ${ScenarioNameB}"
    if (state.B_runTime == 98){
    	turnOffB()
    }
}

//C Handlers
def turnOnC(){
    if ((!C_mode || C_mode.contains(location.mode)) && getDayOk(C_day) && C_switch.currentValue("switch")=="on" && !state.triggeredC) {
        log.debug "Ventilation fans turned on in ${ScenarioNameC}."
    	C_fans?.on()
        state.triggeredC = true
        if (state.C_runTime < 98) {
			log.debug "Humidity fans will be turned off in ${state.C_runTime} minutes in ${ScenarioNameC}."
            runIn (state.C_runTime*60, turnOffC)
        }
	}
}

def turnOffC() {
	log.debug "Ventilation fans turned off in ${ScenarioNameC}."
    C_fans?.off()
    state.triggeredC = false
}

def humidityHandlerC(evt){
    def currentHumidityC =evt.value as Integer
    log.debug "Humidity value is ${currentHumidityC} in ${ScenarioNameC}."
	if (state.humidityLimitC && currentHumidityC > state.humidityLimitC) {
        turnOnC()
    }
	if (state.humidityStartC && currentHumidityC <= state.humidityStartC && state.C_runTime == 99){
    	turnOffC()
    }      
}

def onEventC(evt) {
    def humidityDelta = C_humidityDelta ? C_humidityDelta as Integer : 0
    state.humidityStartC = C_humidity.currentValue("humidity")
    state.humidityLimitC = state.humidityStartC + humidityDelta
    log.debug "Light turned on in ${ScenarioNameC}. Humidity starting value is ${state.humidityStartC} in ${ScenarioNameC}. Ventilation threshold is ${state.humidityLimitC}"
    if (!C_humidityDelta) {
    	turnOnC()
    }
}

def offEventC(evt) {
	def currentHumidityC = C_humidity.currentValue("humidity")
    log.debug "Light turned off in ${ScenarioNameC}. Humidity value is ${currentHumidityC} in ${ScenarioNameC}"
    if (state.C_runTime == 98){
    	turnOffC()
    }
}

//D Handlers
def turnOnD(){
    if ((!D_mode || D_mode.contains(location.mode)) && getDayOk(D_day) && D_switch.currentValue("switch")=="on" && !state.triggeredD) {
        log.debug "Ventilation fans turned on in ${ScenarioNameD}."
    	D_fans?.on()
        state.triggeredD = true
        if (state.D_runTime < 98) {
			log.debug "Humidity fans will be turned off in ${state.D_runTime} minutes in ${ScenarioNameD}."
            runIn (state.D_runTime*60, "turnOffD")
        }
	}
}

def turnOffD() {
	log.debug "Ventilation fans turned off in ${ScenarioNameD}."
    D_fans?.off()
    state.triggeredD = false
}

def humidityHandlerD(evt){
    def currentHumidityD =evt.value as Integer
    log.debug "Humidity value is ${currentHumidityD} in ${ScenarioNameD}."
	if (state.humidityLimitD && currentHumidityD > state.humidityLimitD) {
        turnOnD()
    }
	if (state.humidityStartD && currentHumidityD <= state.humidityStartD && state.D_runTime == 99){
    	turnOffD()
    }      
}

def onEventD(evt) {
    def humidityDelta = D_humidityDelta ? D_humidityDelta as Integer : 0
    state.humidityStartD = D_humidity.currentValue("humidity")
    state.humidityLimitD = state.humidityStartD + humidityDelta
    log.debug "Light turned on in ${ScenarioNameD}. Humidity starting value is ${state.humidityStartD} in ${ScenarioNameD}. Ventilation threshold is ${state.humidityLimitD}"
    if (!D_humidityDelta) {
    	turnOnD()
    }
}

def offEventD(evt) {
	def currentHumidityD = D_humidity.currentValue("humidity")
    log.debug "Light turned off in ${ScenarioNameD}. Humidity value is ${currentHumidityD} in ${ScenarioNameD}"
    if (state.D_runTime == 98){
    	turnOffD()
    }
}


//Common Methods-------------

def greyOut(scenario){
    def result = scenario ? "complete" : ""
}

def getTitle(scenario) {
	def title = scenario ? scenario : "Empty"
}

def getDesc(scenario) {
	def desc = scenario ? "Tap to edit scenario" : "Tap to create a scenario"
}

private getDayOk(dayList) {
	def result = true
    if (dayList) {
		def df = new java.text.SimpleDateFormat("EEEE")
		if (location.timeZone) {
			df.setTimeZone(location.timeZone)
		}
		else {
			df.setTimeZone(TimeZone.getTimeZone("America/New_York"))
		}
		def day = df.format(new Date())
		result = dayList.contains(day)
	}
    result
}

//Version/Copyright/Information/Help

private def textAppName() {
	def text = "Smart Bathroom Ventilation"
}	

private def textVersion() {
    def text = "Version 1.0.0 (05/25/2015)"
}

private def textCopyright() {
    def text = "Copyright © 2015 Michael Struck"
}

private def textLicense() {
    def text =
		"Licensed under the Apache License, Version 2.0 (the 'License');"+
		"you may not use this file except in compliance with the License."+
		"You may obtain a copy of the License at"+
		"\n\n"+
		"    http://www.apache.org/licenses/LICENSE-2.0"+
		"\n\n"+
		"Unless required by applicable law or agreed to in writing, software"+
		"distributed under the License is distributed on an 'AS IS' BASIS,"+
		"WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied."+
		"See the License for the specific language governing permissions and"+
		"limitations under the License."
}

private def textHelp() {
	def text =
    	"Instructions:\nSelect a light switch to monitor. When the switch is turned on, a humidity reading is taken. You can choose when " +
        "the ventilation fan comes on; either when the room humidity rises over a certain level or come on with the light switch. "+
        "The ventilation fans will turn off based on either a timer setting, humidity, or the light switch being turned off. " +
        "Each scenario can be restricted to specific modes or certain days of the week."
}
