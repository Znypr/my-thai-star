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
import static ga.codehub.alexa.MyThaiStarStreamHandler.BASE_URL;

public class MenuIntentHandler implements RequestHandler {

    public static void main(String[] args) {
        BasicOperations bo = new BasicOperations();
        Gson gson = new Gson();
        String payload = "{\"categories\":[],\"searchBy\":\"\",\"pageable\":{\"pageSize\":8,\"pageNumber\":0,\"sort\":[{\"property\":\"price\",\"direction\":\"DESC\"}]},\"maxPrice\":null,\"minLikes\":null}";
        String response;
        ga.codehub.entity.menu.Response resp;
        try {
            response = bo.basicPost(payload, BASE_URL + "/mythaistar/services/rest/dishmanagement/v1/dish/search");
            resp = gson.fromJson(response, ga.codehub.entity.menu.Response.class);
            System.out.println(response);
        } catch (Exception ex) {


        }

    }

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("MenuIntent")) 
        || input.matches(intentName("FoodIntent"))
        || input.matches(intentName("DrinkIntent"))
        || input.matches(intentName("CurryIntent"))
        || input.matches(intentName("VeganIntent"))
        || input.matches(intentName("RiceIntent"))
        || input.matches(intentName("VegetarianIntent"))
        || input.matches(intentName("StarterIntent"))
        || input.matches(intentName("DessertIntent"))
        || input.matches(intentName("NoodleIntent"))
        || input.matches(intentName("SortIntent"))
        || input.matches(intentName("ChangeDirectionIntent"));
        // || input.matches(intentName("FavoritIntent"));
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
            String payload = "";
            String dish_category = "";
            String property = "\"price\"";
            String direction = "\"DESC\"";
            String priceLimit = null;


 
            // Filter Menu
            if (input.matches(intentName("MenuIntent")))
                dish_category = "";
            if (input.matches(intentName("FoodIntent")))
                dish_category = "{\"id\":0}";
            if (input.matches(intentName("DrinkIntent")))
                dish_category = "{\"id\":8}";
            if (input.matches(intentName("CurryIntent")))
                dish_category = "{\"id\":5}";
            if (input.matches(intentName("VeganIntent")))
                dish_category = "{\"id\":6}";
            if (input.matches(intentName("RiceIntent")))
                dish_category = "{\"id\":4}";
            if (input.matches(intentName("VegetarianIntent")))
                dish_category = "{\"id\":7}";
            if (input.matches(intentName("StarterIntent")))
                dish_category = "{\"id\":1}";
            if (input.matches(intentName("DessertIntent")))
                dish_category = "{\"id\":2}";
            if (input.matches(intentName("NoodleIntent")))
                dish_category = "{\"id\":3}";

            if (input.matches(intentName("MaxPriceIntent"))) {
                Request request = input.getRequestEnvelope().getRequest();
                IntentRequest intentRequest = (IntentRequest) request;
                Intent intent = intentRequest.getIntent();

                Map<String, Slot> slotMap = intent.getSlots();
                if (slotMap.size() != 1) {
                    throw new AlexaException();
                }
                Slot maxPrice = slotMap.get("maxPrice");
                priceLimit = maxPrice.getValue();
            }


            // Sort Menu    
            if(input.matches(intentName("SortIntent"))) {
                Request request = input.getRequestEnvelope().getRequest();
                IntentRequest intentRequest = (IntentRequest) request;
                Intent intent = intentRequest.getIntent();

                Map<String, Slot> slotMap = intent.getSlots();
                if ((slotMap.size() != 2) && (slotMap.size() != 1)) {
                    throw new AlexaException();
                }
                Slot direction_slot = slotMap.get("direction");
                Slot property_slot = slotMap.get("property");
                if(direction_slot.getValue() != null){
                    if(direction_slot.getValue().equals("aufsteigend")){
                        direction = "\"ASC\"";
                    }
                }

                if(property_slot.getValue().toLowerCase().equals("name")){
                    property = "\"name\"";
                }else if(property_slot.getValue().toLowerCase().equals("likes")){
                    property = "\"description\"";
                }
            }
            if(input.matches(intentName("ChangeDirectionIntent"))) {
                Request request = input.getRequestEnvelope().getRequest();
                IntentRequest intentRequest = (IntentRequest) request;
                Intent intent = intentRequest.getIntent();

                Map<String, Slot> slotMap = intent.getSlots();
                if (slotMap.size() != 1) {
                    throw new AlexaException();
                }
                Slot direction_slot = slotMap.get("direction");
                if(direction_slot.getValue().equals("aufsteigend")){
                    direction = "\"ASC\"";
                }
            }
            // if (input.matches(intentName("FavoritIntent")))
            //     dish_category = "{\"id\":2},";

            payload = "{\"categories\":[" + dish_category + "],\"searchBy\":\"\",\"pageable\":{\"pageSize\":8,\"pageNumber\":0,\"sort\":[{\"property\":" + property + ",\"direction\":" + direction + "}]},\"maxPrice\":" + priceLimit + ",\"minLikes\":null}";
                


            BasicOperations bo = new BasicOperations();
            Gson gson = new Gson();
            String response;
            ga.codehub.entity.menu.Response resp;
            try {
                response = bo.basicPost(payload, BASE_URL + "/mythaistar/services/rest/dishmanagement/v1/dish/search");
                resp = gson.fromJson(response, ga.codehub.entity.menu.Response.class);
            } catch (Exception ex) {
                speechText = "Der MyThaiStar-Server scheint Probleme mit der Verarbeitung deiner Anfrage zu haben ." + ex.toString();
                throw new AlexaException();

            }


            speechText += "Es gibt : " + resp.toString();

        } catch (AlexaException e) {
            e.printStackTrace();
        }
        return input.getResponseBuilder()
                .withSpeech(speechText)
                .withSimpleCard("MyThaiStar", speechText)
                .build();
    }

}
