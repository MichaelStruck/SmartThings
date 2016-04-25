// Copyright 2016 Michael Struck & Keith DeLong (n8xd)
// Version 1.0.0 4/25/16
// Apache 2.0 license - http://www.apache.org/licenses/LICENSE-2.0
'use strict';
exports.handler = function( event, context ) {
   var https = require( 'https' );
   var STappID = '636505f6-82e3-4967-9405-9b19942e1b0c';
   var STtoken = '3071d549-133d-4a54-947f-20621537926e';
   var url = 'https://graph.api.smartthings.com/api/smartapps/installations/' + STappID + '/' ;
   var process = false;

    if (event.request.intent.name == "DeviceOperation") {
        var Operator = event.request.intent.slots.Operator.value;
        var Device = event.request.intent.slots.Device.value;
        var Num = event.request.intent.slots.Num.value;
        url += Device + '/' + Operator + '/' + Num ;
        process = true;
    } 
    if (event.request.intent.name == "ReportOperation") {
        var Report = event.request.intent.slots.Report.value;
        url +=  Report;
        process = true;
    }
    else {
        output("I am not sure what you are asking. Please try again", context);   
    }
    if (process) {
        url += '?access_token=' + STtoken;
        https.get( url, function( response ) {
            response.on( 'data', function( data ) {
            var resJSON = JSON.parse(data);
            var speechText = "I don't understand.";
            if (resJSON.talk2me) { speechText = resJSON.talk2me; }
            console.log(speechText);
            output(speechText, context);
            } );
        } );
    }
};

function output( text, context ) {
   var response = {
      outputSpeech: {
         type: "PlainText",
         text: text
      },
      card: {
         type: "Simple",
         title: "System Data",
         content: text
      },
   shouldEndSession: true
   };
   context.succeed( { response: response } );
}
