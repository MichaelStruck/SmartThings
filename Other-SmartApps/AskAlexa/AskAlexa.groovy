/**
 *  Ask Alexa Interface
 *
 *  Version 1.0.0 - 4/19/16 Copyright © 2016 Michael Struck
 *  
 *  Version 1.0.0 - Initial release
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
    name: "Ask Alexa Interface",
    namespace: "MichaelStruck",
    author: "Michael Struck",
    description: "Provide interfacing to control SmartThings devices with the Amazon Echo ('Alexa').",
    category: "Convenience",
    iconUrl: "https://raw.githubusercontent.com/MichaelStruck/SmartThingsPublic/master/smartapps/michaelstruck/cloud-interface.src/CloudInterface.png",
    iconX2Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThingsPublic/master/smartapps/michaelstruck/cloud-interface.src/CloudInterface@2x.png",
    iconX3Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThingsPublic/master/smartapps/michaelstruck/cloud-interface.src/CloudInterface@2x.png",
  	oauth: true)
preferences {
    page name:"mainPage"
    page name:"pageReset"
    page name:"pageAbout"
    page name: "pageSettings"
}
//Show main page
def mainPage() {
    dynamicPage(name: "mainPage", title:"", install: true, uninstall: false) {
		section("External control") {
        	input "switches", "capability.switch", title: "Choose Switches (On/Off)", multiple: true, required: false,
            	image: "https://raw.githubusercontent.com/MichaelStruck/SmartThingsPublic/master/img/add.png"
        }
        section("Options") {
			href "pageSettings", title: "Settings", description: none, 
            	image: "https://raw.githubusercontent.com/MichaelStruck/SmartThingsPublic/master/img/settings.png"
			href "pageAbout", title: "About ${textAppName()}", description: "Tap to get application version, license, instructions or remove the application",
            	image: "https://raw.githubusercontent.com/MichaelStruck/SmartThingsPublic/master/img/info.png"
        }
	}
}
def pageAbout(){
	dynamicPage(name: "pageAbout", uninstall: true) {
        section {
        	paragraph "${textAppName()}\n${textVersion()}\n${textCopyright()}",image: "https://raw.githubusercontent.com/MichaelStruck/SmartThingsPublic/master/smartapps/michaelstruck/cloud-interface.src/CloudInterface@2x.png"
        }
        section ("Access Token / Application ID"){
            if (!state.accessToken) {
				OAuthToken()
			}
            def msg = state.accessToken != null ? state.accessToken : "Could not create Access Token. OAuth may not be enabled. Go to the SmartApp IDE settings to enable OAuth."
            paragraph "\nAccess Token:\n${msg}\n\nApplication ID:\n${app.id}"
    	}
        section ("Apache License"){
        	paragraph textLicense()
        }
    	section("Instructions") {
        	paragraph textHelp()
            paragraph "Read data:\n\n${getApiServerUrl()}/api/smartapps/installations/${app.id}/r?l=NameOFDevice&access_token=${state.accessToken}"
    		 paragraph "Read data:\n\n${getApiServerUrl()}/api/smartapps/installations/${app.id}/w?l=<LABELOFDEVICE>&c=<COMMAND>&access_token=${state.accessToken}"
        }
        section("Tap button below to remove the application"){
        }
	}
}
def pageSettings(){
    dynamicPage(name: "pageSettings", title: "Settings", uninstall: true){
        section("Security Settings"){
            href "pageReset", title: "Reset Access Token", description: "Tap to revoke access token."
        }
    }
}
def pageReset(){
	dynamicPage(name: "pageReset", title: "Access Token Reset"){
        section{
			state.accessToken = null
            OAuthToken()
            def msg = state.accessToken != null ? "New access token:\n${state.accessToken}\n\nClick 'Done' above to return to the previous menu." : "Could not reset Access Token. OAuth may not be enabled. Go to the SmartApp IDE settings to enable OAuth."
	    	paragraph "${msg}"
		}
	}
}
def installed() {
	log.debug "Installed with settings: ${settings}"
	initialize()
}
def updated() {
	log.debug "Updated with settings: ${settings}"
	initialize()
}
def initialize() {
	if (!state.accessToken) {
		log.error "Access token not defined. Ensure OAuth is enabled in the SmartThings IDE."
	}
}
mappings {
      path("/r") {action: [GET: "readData"]}
      path("/w") {action: [GET: "writeData"]}
}
def writeData() {
    log.debug "Command received with params $params"
	def command = params.c  	//The action you want to take i.e. on/off 
	def label = params.l		//The name given to the device by you
	def outputTxt = []
    if (switches){
        try {
        	def device = switches?.find{it.label == label}
       		device."$command"()
            outputTxt << [label:label, status: "ok"]
        }
        catch (e){
        	outputTxt << [label:label, status: null]
        }
	}
    outputTxt
}
def readData() {
    log.debug "Command received with params $params"
	def label = params.l		//The name given to the device by you
	def outputTxt = []
    if (switches){
        try {
        	def device = switches?.find{it.label == label}
        	outputTxt << [label: label, status: device.currentValue("switch")]
        }
        catch (e){
        	outputTxt << [label:label, status: null]
        }
	} 
    outputTxt
}

//Common Code
def OAuthToken(){
	try {
		createAccessToken()
		log.debug "Creating new Access Token"
	} catch (e) {
		log.error "Access Token not defined. OAuth may not be enabled. Go to the SmartApp IDE settings to enable OAuth."
	}
}

//Version/Copyright/Information/Help
private def textAppName() {
	def text = "Ask Alexa Interface"
}	
private def textVersion() {
    def text = "Version 1.0.0 (04/19/2016)"
}
private def textCopyright() {
    def text = "Copyright © 2016 Michael Struck"
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
		"This app allows provides an interface to control and query the SmartThings environment via the Amazon Echo ('Alexa')"
}
