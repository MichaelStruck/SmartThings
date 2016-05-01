/**
 *  Ask Alexa - Lambda Code
 *
 *  Version 1.0.0 - 4/28/16 Copyright © 2016 Michael Struck
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
'use strict';
exports.handler = function( event, context ) {
   var https = require( 'https' );
   // Paste app code here between the breaks------------------------------------------------
    var STappID = 'x';
    var STtoken = x';
    var url='https://graph.api.smartthings.com:443/api/smartapps/installations/' + STappID + '/' ;
   //---------------------------------------------------------------------------------------
   var process = false;
   var cardName ="";

    if (event.request.intent.name == "DeviceOperation") {
        var Operator = event.request.intent.slots.Operator.value;
        var Device = event.request.intent.slots.Device.value;
        var Num = event.request.intent.slots.Num.value;
        var Param = event.request.intent.slots.Param.value;
        url += 'd?Device=' + Device + '&Operator=' + Operator + '&Num=' + Num + '&Param=' + Param; 
        process = true;
        cardName = "SmartThings Device Operation";
    } 
    if (event.request.intent.name == "ReportOperation") {
        var Report = event.request.intent.slots.Report.value;
        url += 'r?Report=' + Report;
        process = true; 
        cardName = "SmartThings Reports";
    }
    if (event.request.intent.name == "DeviceStatus") {
        var Status = event.request.intent.slots.Status.value;
        url += 's?Device=' + Status;
        process = true;
        cardName = "SmartThings Status Report";
    }
    if (event.request.intent.name == "SmartHomeOperation") {
        var SHCmd = event.request.intent.slots.SHCmd.value;
        var SHParam = event.request.intent.slots.SHParam.value;
        url += 'h?SHCmd=' + SHCmd + '&SHParam=' + SHParam;
        process = true;
        cardName = "SmartThings Home Operation";
    }
    if (!process) {
        output("I am not sure what you are asking. Please try again", context, "Ask Alexa Error");   
    }
    else {
        url += '&access_token=' + STtoken;
        https.get( url, function( response ) {
            response.on( 'data', function( data ) {
            var resJSON = JSON.parse(data);
            var speechText = "The SmartThings SmartApp returned an error. I was unable to complete your request";
            if (resJSON.talk2me) { speechText = resJSON.talk2me; }
            console.log(speechText);
            output(speechText, context, cardName);
            } );
        } );
    }
};

function output( text, context, card ) {
   var response = {
      outputSpeech: {
         type: "PlainText",
         text: text
      },
      card: {
         type: "Simple",
         title: card,
         content: text
      },
   shouldEndSession: true
   };
   context.succeed( { response: response } );
}
