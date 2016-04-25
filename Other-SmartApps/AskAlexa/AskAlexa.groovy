/**
 *  Ask Alexa
 *
 *  Version 1.0.0 - 4/25/16 Copyright © 2016 Michael Struck
 *  Special thanks for Keith DeLong for code and assistance
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
    name: "Ask Alexa",
    namespace: "MichaelStruck",
    author: "Michael Struck",
    description: "Provide interfacing to control and report on SmartThings devices with the Amazon Echo ('Alexa').",
    category: "Convenience",
    iconUrl: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/AskAlexa/AskAlexa.png",
    iconX2Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/AskAlexa/AskAlexa@2x.png",
    iconX3Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/AskAlexa/AskAlexa@2x.png",
  	oauth: true)
preferences {
    page name:"mainPage"
    page name:"pageReset"
    page name:"pageAbout"
    page name:"pageSettings"
}
//Show main page
def mainPage() {
    dynamicPage(name: "mainPage", title:"", install: true, uninstall: false) {
		section("Devices To Interface to Alexa") {
        	input "switches", "capability.switch", title: "Choose Switches (On/Off/Status)", multiple: true, required: false
            input "dimmers", "capability.switchLevel", title: "Choose Dimmers (On/Off/Level/Status)", multiple: true, required: false
            input "doors", "capability.doorControl", title: "Choose Door Controls (Open/Close/Status)" , multiple: true, required: false
            input "locks", "capability.lock", title: "Choose Locks (Lock/Unlock/Status)", multiple: true, required: false
        }
        section("Configure Voice Report"){
        	href "pageReports", title: "Voice Reports", description: reportDesc(), state: reportGrey(), image: "https://raw.githubusercontent.com/MichaelStruck/SmartThingsPublic/master/img/speak.png"
        }
        section("Options") {
			href "pageSettings", title: "Settings", description: "Tap to get setup settings and to reset the access token",
            	image: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/img/settings.png"
			href "pageAbout", title: "About ${textAppName()}", description: "Tap to get application version, license, instructions or remove the application",
            	image: "https://raw.githubusercontent.com/MichaelStruck/SmartThingsPublic/master/img/info.png"
        }
	}
}
page(name: "pageReports", title: "Voice Reports", install: false, uninstall: false){
	section{
    	app(name: "childReports", appName: "Ask Alexa - Report", namespace: "MichaelStruck", title: "Create New Voice Report...", description: "Tap to create new voice report", multiple: true, 
            	image: "https://raw.githubusercontent.com/MichaelStruck/SmartThingsPublic/master/img/add.png")
	}
}
def pageAbout(){
	dynamicPage(name: "pageAbout", uninstall: true) {
        section {paragraph "${textAppName()}\n${textCopyright()}",
            	image: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/AskAlexa/AskAlexa@2x.png"}
        section ("SmartApp/Voice Report Version") { paragraph "${textVersion()}" } 
        section ("Access Token / Application ID"){
            if (!state.accessToken) OAuthToken()
            def msg = state.accessToken != null ? state.accessToken : "Could not create Access Token. OAuth may not be enabled. Go to the SmartApp IDE settings to enable OAuth."
            paragraph "Access Token:\n${msg}\n\nApplication ID:\n${app.id}"
    	}
        section ("Apache License"){ paragraph textLicense() }
    	section("Instructions") { paragraph textHelp() }
        section("Tap below to remove the application and all reports"){}
	}
}
def pageSettings(){
    dynamicPage(name: "pageSettings", title: none, uninstall: false){
        section { paragraph "Settings", image: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/img/settings.png" }
        section ("Setup Variables (for Amazon Developer Site)"){
        	href url:"${getApiServerUrl()}/api/smartapps/installations/${app.id}/setup?access_token=${state.accessToken}", style:"embedded", required:false, title:"Show Setup Information", description: none
        }
        section ("Reset Access Token") { href "pageReset", title: "Tap To Revoke/Reset Access Token", description: none
        	paragraph "By resetting the access token you will disable the ability to interface this SmartApp with "+
        	"your Amazon Echo. You will need copy the new access token to your Amazon Lambda code."
        }
    }
}
def pageReset(){
	dynamicPage(name: "pageReset", title: "Access Token Reset"){
        section{
			revokeAccessToken()
            state.accessToken = null
            OAuthToken()
            def msg = state.accessToken != null ? "New access token:\n${state.accessToken}\n\nClick 'Done' above to return to the previous menu." : "Could not reset Access Token. OAuth may not be enabled. Go to the SmartApp IDE settings to enable OAuth."
	    	paragraph "${msg}"
		}
	}
}
//---------------------------------------------------------------
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
      path("/:device/:operator/:number") { action: [GET: "processDevice"] }
      path("/setup") { action: [GET: "displayData"] }
      path("/:report") { action: [GET: "processReport"] }
}
//--------------------------------------------------------------
def setupData(){
	def result ="<i><b>Lambda code variables:</b></i><br><br>var STappID = '${app.id}'<br>var STtoken = '${state.accessToken}'<br><br><hr>"
    result += "<br>LIST_OF_DEVICES<br><br>"
    if (switches) switches.each{ result += it.label.toLowerCase()+"<br>" }
    if (dimmers) dimmers.each{ result += it.label.toLowerCase()+"<br>" }
    if (doors) doors.each{ result += it.label.toLowerCase()+"<br>" } 
    if (locks) locks.each{ result += it.label.toLowerCase()+"<br>" }
    result += "<br><hr><br>LIST_OF_OPERATIONS<br><br>on<br>off<br>status<br>"
    result += locks ? "lock<br>unlock<br>" : ""
    result += doors ? "open<br>close<br>" : ""
    result += "<br>"
    if (childApps.size()) {
    	result += "<hr><br>LIST_OF_REPORTS<br><br>"
    	childApps.each { result += it.label.toLowerCase()+"<br>" }
	}
    result
}
def displayData(){
	render contentType: "text/html", data: """<!DOCTYPE html><html><head><meta charset="UTF-8" /><meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=0"/></head><body style="margin: 0;">${setupData()}</body></html>"""
}
def processDevice() {
    log.debug "Command received with params $params"
	def dev = params.device 	//Label of device
	def op = params.operator	//Operation to perform
    def num = params.number     //Number for dimmer type settings
    log.debug "Device: " + dev
    log.debug "Operation: " + op
    log.debug "Number: " + num
    if (op == "undefined" && num == "undefined") op = "status"
    def outputTxt = ""
    if (switches && switches?.find{it.label.toLowerCase() == dev}){ outputTxt = getReply (switches, "switch", dev, op, num) }
    if (doors && doors?.find{it.label.toLowerCase() == dev}) { outputTxt = getReply (doors, "door", dev, op, num) }
	if (locks && locks?.find{it.label.toLowerCase() == dev}) { outputTxt = getReply (locks, "lock", dev, op, num) }
    if (dimmers && dimmers?.find {it.label.toLowerCase() == dev}) { outputTxt = getReply (dimmers, "level" , dev, op, num) }
    if (outputTxt=="" && op != "report") outputTxt = "I had some problems finding the device you specified. Please try again."   
	return ["talk2me":outputTxt]
}
def processReport() {
	//Report Settings
    log.debug "Command received with params $params"
	def rep = params.report 	//Report name
    log.debug "Report Name: " + rep
    def outputTxt = ""
    if (childApps.size()){
        childApps.each {child -> if (child.label.toLowerCase() == rep) outputTxt = child.reportResults()}
        if (!outputTxt) outputTxt = "I could not find the ${rep} report. Please check the name of the report and try again." 
    }
    log.debug outputTxt
    return ["talk2me":outputTxt]
}
def getReply(devices, type, dev, op,num){
	def result = ""
	try {
    	def STdevice = devices?.find{it.label.toLowerCase() == dev}
        if (op=="status") {
            if (type != "level"){
            	op = STdevice.currentValue(type)
            	result = "The ${STdevice} is ${op}."
            }
            else {
                def onOffStatus = STdevice.currentValue("switch")
                def level = STdevice.currentValue(type) as int
                result = "The ${STdevice} is ${onOffStatus}"
                result += onOffStatus != "off" || level==0 ? ", and the current level is ${level}%." : "."
            }
        }
        else {
            if (num == "undefined") STdevice."$op"()
            else STdevice.setLevel(num)
            if (op == "close") op="clos"
        	if (type == "door" || type == "lock") result = "I am ${op}ing the ${STdevice}."
            else if (num == "undefined") result = "I am turning the ${STdevice} ${op}."
            else if (type == "level" && num != "undefined") result = "I am setting the ${STdevice} to ${num}%." 
        }
	}
	catch (e){ result = "I could not process your request on ${dev}." }
    result
}
//Common Code
def OAuthToken(){
	try {
        createAccessToken()
		log.debug "Creating new Access Token"
	} catch (e) { log.error "Access Token not defined. OAuth may not be enabled. Go to the SmartApp IDE settings to enable OAuth." }
}
def reportDesc(){def results = childApps.size() ? childApps.size()==1 ? childApps.size()+ " Voice Report Configured" : childApps.size() + " Voice Reports Configured" : "No Voices Reports Configured - Tap to create new report"}
def reportGrey(){def results =childApps.size() ? "complete" : ""}
//Version/Copyright/Information/Help
private def textAppName() {
	def text = "Ask Alexa"
}	
private def textVersion() {
    def version = "Parent App Version: 1.0.0 (04/25/2016)"
    def childCount = childApps.size()
    def deviceCount= getChildDevices().size()
    def childVersion = childCount ? childApps[0].textVersion() : "No voice reports installed"
    return "${version}\n${childVersion}"
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
