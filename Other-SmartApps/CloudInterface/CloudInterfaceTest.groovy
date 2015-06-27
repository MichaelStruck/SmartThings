Enter file contents here/**
 *  Cloud Interface-Test
 *
 *	Version 1.0 - 6/27/15 Copyright 2015 Michael Struck
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
    name: "Cloud Interface-Test",
    namespace: "MichaelStruck",
    author: "Michael Struck",
    description: "Allows for HTTP single line cloud interfacing to control SmartThings devices. To be used with a local automation server and cURL.",
    category: "SmartThings Labs",
    iconUrl: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/CloudInterface/CloudInterface.png",
    iconX2Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/CloudInterface/CloudInterface@2x.png",
    iconX3Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/CloudInterface/CloudInterface@2x.png",
  	oauth: true)

preferences {
  		section("Allow external visibility to these things...") {
		    input "temperature", "capability.temperatureMeasurement", title: "Which Temperature?", multiple: true, required: false
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
}

mappings {
      path("/read") {action: [GET: "readData"]} 
}

def oauthError() {[error: "OAuth token is invalid or access has been revoked"]}


def readData(){
	log.debug "Read command received with params $params"
    def label = params.label		//The name given to the device by you
	def type = params.type			//The type of the device

	def outputTxt = ""
   
    if (type == "temperature") {
        device = temperature?.find{it.label == label}
        if (device) 
        {
        	outputTxt = device.currentValue("temperature")
        }
    }   
    [temperature:outputTxt]
	
}
