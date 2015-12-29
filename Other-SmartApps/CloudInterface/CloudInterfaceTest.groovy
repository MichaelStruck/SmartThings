/**
 *  Cloud Interface
 *
 *	Version 1.0 - 4/24/15 Copyright 2015 Michael Struck
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
    name: "Cloud Interface",
    namespace: "MichaelStruck",
    author: "Michael Struck",
    description: "Allows for HTTP single line cloud interfacing to control SmartThings devices. To be used with a local automation server and cURL.",
    category: "SmartThings Labs",
    iconUrl: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/CloudInterface/CloudInterface.png",
    iconX2Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/CloudInterface/CloudInterface@2x.png",
    iconX3Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/CloudInterface/CloudInterface@2x.png",
  	oauth: true)

preferences {
		section("Allow external control to these things...") {
	    	input "switches", "capability.switch", title: "Which Switches?", multiple: true, required: false
    		input "dimmers", "capability.switchLevel", title: "Which Dimmers?", multiple: true, required: false
    		input "locks", "capability.lock", title: "Which Locks?", multiple: true, required: false
	    	input "thermostats", "capability.thermostat", title: "Which Thermostats?", multiple: true, required: false
    		input "alarms", "capability.alarm", title: "Which Alarms?", multiple: true, required: false
    		input "doorControls", "capability.doorControl", title: "Which Doors?", multiple: true, required: false
  		}
  
  		section("Allow external visibility to these things...") {
    		input "presence", "capability.presenceSensor", title: "Which Presence?", multiple: true, required: false
		    input "temperature", "capability.temperatureMeasurement", title: "Which Temperature?", multiple: true, required: false
  			input "illuminance", "capability.illuminanceMeasurement", title: "Which Light Level?", multiple: true, required: false
		    input "humidity", "capability.relativeHumidityMeasurement", title: "Which Hygrometer?", multiple: true, required: false
		    input "motions", "capability.motionSensor", title: "Which Motion Sensors?", multiple: true, required: false
		    input "contactSensors", "capability.contactSensor", title: "Which Contact Sensors?", multiple: true, required: false
		    input "buttonDevices", "capability.button", title: "Which Buttons?", multiple: true, required: false
		    input "waterSensors", "capability.waterSensor", title: "Which Water Sensors?", multiple: true, required: false
		    input "carbonMonoxideDetectors", "capability.carbonMonoxideDetector", title: "Which Carbon Monoxide Detectors?", multiple: true, required: false
            input "smokeDetectors", "capability.smokeDetector", title: "Which Smoke Detectors?", multiple: true, required: false
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
	//createAccessToken()
    log.debug "${app.id}"
    log.debug "${state.accessToken}"

}

mappings {
      path("/write") {action: [GET: "writeData"]}
      path("/read") {action: [GET: "readData"]} 
      path("/listW") {action: [GET: "listWriteData"]}
      path("/listR") {action: [GET: "listReadData"]}
}

def oauthError() {[error: "OAuth token is invalid or access has been revoked"]}


void writeData() {
	log.debug "write command received with params $params"
    
    def command = params.command  	//The action you want to take i.e. on/off 
    def label = params.label		//The name given to the device by you
    def type = params.type			//The type of the device
    def value = params.value		//additional items to send like dimmer level
    
	def device

        if (type == "switch")         
        {
            device = switches?.find{it.label == label}
            log.debug "${device}"
            if (device) 
            {
            	device."$command"()
            }
        }
        if (type == "alarm") 
        {
            device = alarms?.find{it.id == id}
            if (device) 
            {
                device."$command"()
            }
        }
        
        if (type == "lock") 
        {
            device = locks?.find{it.label == label}
            if (device) 
            {
                device."$command"()
            }
        }
        
        if (type == "doorControl") 
        {
            device = doorControls?.find{it.id == id}
            if (device) 
            {
                device."$command"()
            }
        }
        
        if (type == "dimmer") 
        {
            device = dimmers?.find{it.label == label}
            if (device) 
            {
            	device.setLevel(value as int)
                
            }
        }
        if (type == "heatingsetpoint") 
        {
            device = thermostats?.find{it.id == id}
            if (device) 
            {
                device.setHeatingSetpoint(value)                
            }
        }
        if (type == "coolingsetpoint") 
        {
            device = thermostats?.find{it.id == id}
            if (device) 
            {
            	device.setCoolingSetpoint(value)
            }
        }
       
    }


def readData(){
	log.debug "read command received with params $params"
 
    def label = params.label		//The name given to the device by you
    def id = params.id				//the internal ID of the device (long)
	def type = params.type			//The type of the device

	def outputTxt = ""
    
	if (type == "switch") {
		device = switches?.find{it.label == label}
        if (device) {
        	outputTxt = device.currentValue("switch")
        }
    }

	if (type == "temperature") {
        device = temperature?.find{it.label == label}
        if (device) 
        {
        	outputTxt = device.currentValue("temperature")
        }
    }
    
    if (type == "dimmer") {
		device = dimmers?.find{it.label == label}
        if (device) {
        	outputTxt = device.currentValue("level")
        }
    }
    
    if (type == "lock") {
        device = locks?.find{it.label == label}
        if (device) 
        {
        	outputTxt = device.currentValue("lock")
        }
    }   
    

	if (outputTxt) {
		printData(outputTxt)
	}
    
    
}

def printData(txt) {
    [output:txt]
}


def listWriteData() {
	state.data=[]
    writeDataList()
    showData()
}

def listReadData() {
	state.data=[]
    writeDataList()
    readDataList()
    showData()
}

def writeDataList() {
    switches.collect{device(it,"switch")}
    locks.collect{device(it,"lock")}
    alarms.collect{device(it,"alarm")}
	thermostats.collect{device(it,"thermostat")}
    doorControls.collect{device(it,"doorControl")}   
}

def readDataList() {
	presence.collect{device(it,"presenceSensor")}
    temperature.collect{device(it,"temperatureMeasurement")} 
	illuminance.collect{device(it,"illuminanceMeasurement")}
	humidity.collect{device(it,"relativeHumidityMeasurement")}
    motions .collect{device(it,"motionSensor")}
	contactSensors.collect{device(it,"contactSensor")}
	buttonDevices.collect{device(it,"button")}
	waterSensors.collect{device(it,"waterSensor")}   
	carbonMonoxideDetectors.collect{device(it,"carbonMonoxideDetector")}
    smokeDetectors.collect{device(it,"smokeDetector")}
}

private device(it, type) {
	state.data << [id: it.id, label: it.label, type: type]
}

def showData() {
    //render contentType: "text/html", data:"<!DOCTYPE html>\n<html>\n<head></head>\n<body>\n${state.data}\n</body></html>"
    state.data
}

