/*
     Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
     except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.
*/

package ga.codehub.alexa.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.*;
import com.google.gson.Gson;
import ga.codehub.alexa.Exceptions.AlexaException;
import ga.codehub.entity.booking.Booking;
import ga.codehub.tools.BasicOperations;

import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;
import static ga.codehub.alexa.MyThaiStartStreamHandler.BASE_URL;

public class MenueIntentHandler implements RequestHandler {


    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("MenueIntent"));
    }


    @Override
    public Optional<Response> handle(HandlerInput input) {

        String speechText = "";

        try {
/*

            Request request = input.getRequestEnvelope().getRequest();
            IntentRequest intentRequest = (IntentRequest) request;
            Intent intent = intentRequest.getIntent();
*/


            BasicOperations bo = new BasicOperations();
            Gson gson = new Gson();
            String payload = "{\"categories\":[],\"searchBy\":\"\",\"pageable\":{\"pageSize\":8,\"pageNumber\":0,\"sort\":[{\"property\":\"price\",\"direction\":\"DESC\"}]},\"maxPrice\":null,\"minLikes\":null}";
            String response;
            ga.codehub.entity.menue.Response resp;
            try {
                response = bo.basicPost(payload, BASE_URL + "/mythaistar/services/rest/dishmanagement/v1/dish/search");
                resp = gson.fromJson(response, ga.codehub.entity.menue.Response.class);
            } catch (Exception ex) {
                speechText = "Der MyThaiStar-Server scheint Probleme mit der Verarbeitung deiner Anfrage zu haben";
                throw new AlexaException();

            }


            speechText = "Es gibt : " + resp.toString();

        } catch (AlexaException e) {
            e.printStackTrace();
        }
        return input.getResponseBuilder()
                .withSpeech(speechText)
                .withSimpleCard("MyThaiStar", speechText)
                .build();
    }

}
