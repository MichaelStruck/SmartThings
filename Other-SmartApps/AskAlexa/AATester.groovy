/**
 *  Ask Alexa Test App
 *
 *  Version - 1.0.1
 * 
 *  Version 1.0.0 (3/23/17) - Initial release
 *  Version 1.0.1 (4/6/17) - Added the remove button back in 
 *
 * 
 *  Copyright 2017 Michael Struck 
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
    name: "Ask Alexa Test App",
    singleInstance: true,
    namespace: "MichaelStruck",
    author: "Michael Struck",
    description: "Test app for Ask Alexa",
    category: "Convenience",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png"
    )
preferences {
    page name:"mainPage"
    page name:"pageAbout"
}

def mainPage(){
    dynamicPage(name: "mainPage", title: "Test Parameters", install: true, uninstall: false, submitOnChange: true) {
		section {
            input "listOfMQs", "enum", title: "This is a list of the Ask Alexa Message Queues", options: state.askAlexaMQ, multiple: true, required: false
            input "unit", "text", title: "Unit value", required: false
            input "msg", "text" , title: "Message to send to the queues above", required: false , capitalization: "sentences"
            input "delete", "bool", title: "Turn this on to perform delete routine when DONE pressed", defaultValue: false
		}
        section ("Help"){
        	paragraph "The action (send or delete) will occur when clicking <DONE> at the top of the page. Unit values are not needed if you are not performing a delete function."
        }
        section([title:"Options", mobileOnly:true]) {
			href "pageAbout", title: "About ${textAppName()}", description: "Tap to get application version, license, instructions or to remove the application",
            	image: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/img/info.png"
		}
	}
}
def pageAbout() {
	dynamicPage(name: "pageAbout" , uninstall: true) {
    	section {
        paragraph "${textAppName()}\n${textCopyright()}", image: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png"
        }
        section ("SmartApp Versions") {
			paragraph textVersion()
        }    
        section ("Apache License") { 
        	paragraph textLicense()
    	}
		section("Instructions") {
            paragraph textHelp()
		}
        section("Tap button below to remove all scenarios and application"){
        }
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
	subscribe(location, "askAlexaMQ", askAlexaMQHandler)
    if (!delete) sendLocationEvent(name: "AskAlexaMsgQueue", value: "Test App", unit: unit, isStateChange: true, descriptionText: msg, data: [queues:listOfMQs])
    else sendLocationEvent(name: "AskAlexaMsgQueueDelete", value: "Test App", isStateChange: true, unit: unit, data: [queues:listOfMQs])
}
//Common Code
def askAlexaMQHandler(evt) {
	if (!evt) return
	switch (evt.value) {
		case "refresh":
			state.askAlexaMQ = evt.jsonData && evt.jsonData?.queues ? evt.jsonData.queues : []
            break
	}
}


//Version/Copyright/Information/Help

private def textAppName() {
	def text = "Ask Alexa Test App"
}	

private def textVersion() {
    def version = "Version 1.0.1 (04/06/2017)"
    return "${version}"
}

private def textCopyright() {
    def text = "Copyright © 2017 Michael Struck"
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
    	"Test template app to demonstrate Ask Alexa communication abilities." 
}
