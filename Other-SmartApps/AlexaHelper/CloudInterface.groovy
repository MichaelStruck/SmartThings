/**
 *  Alexa Helper-Cloud Interface
 *
 *  Version 1.0.0 - 12/24/15 Copyright 2015 Michael Struck
 *  
 *  Version 1.0.0 - Initial release
 *
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
    name: "Alexa Helper-Cloud Interface",
    namespace: "MichaelStruck",
    author: "Michael Struck",
    description: "Allows for HTTP single line cloud interfacing to control SmartThings devices.",
    category: "Convenience",
    iconUrl: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/CloudInterface/CloudInterface.png",
    iconX2Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/CloudInterface/CloudInterface@2x.png",
    iconX3Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/CloudInterface/CloudInterface@2x.png",
  	oauth: true)


preferences {
    page name:"mainPage"
    page name:"showURLs"
    page name:"pageReset"
    page name:"pageAbout"
}

//Show main page
def mainPage() {
    dynamicPage(name: "mainPage", title:"", install: true, uninstall: true) {
		section("External control") {
        	input "switches", "capability.switch", title: "Choose Switches", multiple: true, required: false, submitOnChange:true
			if (switches){
            	href "showURLs", title: "Show URLs", description: "Tap to show URLs to control switches"
			}
        }
        section([title:"Options", mobileOnly:true]) {
			href "pageSecurity", title: "Security Options", description: "Tap to show security options"
            label title:"Assign a name", required:false
			href "pageAbout", title: "About ${textAppName()}", description: "Tap to get application version, license and instructions"
        }
	}
}

def showURLs(){
	dynamicPage(name: "showURLs", title:"Copy the desired URLs to Alexa Helper") {
		if (!state.accessToken) {
	    		createAccessToken()
		}
        	def swName=""
        	switches.each{
        		swName= "${it.label}"
        		section ("Turn on ${swName}") {
					paragraph "", title: "https://graph.api.smartthings.com/api/smartapps/installations/${app.id}/w?l=${swName}&c=on&access_token=${state.accessToken}"
				}
        		section ("Turn off ${swName}") {
					paragraph "", title: "https://graph.api.smartthings.com/api/smartapps/installations/${app.id}/w?l=${swName}&c=off&access_token=${state.accessToken}"
				}	
       		}
	}
}
def pageAbout(){
	dynamicPage(name: "pageAbout", title: "About ${textAppName()}") {
		section {
    		def showToken = state.accessToken ? state.accessToken : "Not set. Save app or choose security options"
            paragraph "${textVersion()}\n${textCopyright()}\n\nAccess Token:\n${showToken}\n\n${textLicense()}\n"
    	}
    	section("Instructions") {
        	paragraph textHelp()
    	}
	}
}

page(name: "pageSecurity", title: "Security Options"){
	section{
    	href "pageReset", title: "Reset Access token", description: "Tap to revoke access token. All current URLs in use will need to be re-generated"
	}
}

def pageReset(){
	dynamicPage(name: "pageReset", title: "Access token has been reset"){
		createAccessToken()
    	section{
    		paragraph "New access token:\n${state.accessToken}\n\nClick 'Done' above to return to the previous menu"
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
		createAccessToken()
	}
}

mappings {
      path("/w") {action: [GET: "writeData"]}
}

void writeData() {
	log.debug "Command received with params $params"
	def command = params.c  	//The action you want to take i.e. on/off 
    	def label = params.l		//The name given to the device by you
    
	if (switches){
		def device = switches?.find{it.label == label}
       	device."$command"()
	}
}

//Version/Copyright/Information/Help

private def textAppName() {
	def text = "Alexa Helper-Cloud Interface"
}	

private def textVersion() {
    def text = "Version 1.0.0 (12/20/2015)"
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
		"To be used with Alexa Helper 3.3.2+ (with Alexa Helper-Scenario version 1.2.0+), this app allows you to define switches (typically virtual) to be controlled via a single url."+
		"This is useful when using your Alexa with two different SmartThings locations/accounts. Now you can command Alexa at "+
		"one location and have actions happen at another location also controlled by a SmartThings hub."
}
