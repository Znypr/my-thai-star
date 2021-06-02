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

                || input.matches(intentName("FoodIntent")) || input.matches(intentName("DrinkIntent"))
                || input.matches(intentName("CurryIntent")) || input.matches(intentName("VeganIntent"))
                || input.matches(intentName("RiceIntent")) || input.matches(intentName("VegetarianIntent"))
                || input.matches(intentName("StarterIntent")) || input.matches(intentName("DessertIntent"))
                || input.matches(intentName("NoodleIntent"))

                || input.matches(intentName("SortIntent")) || input.matches(intentName("SearchIntent"))
                || input.matches(intentName("ChangeDirectionIntent")) || input.matches(intentName("DescriptionIntent"))
                || input.matches(intentName("MaxPriceIntent"));
        // || input.matches(intentName("FavoritIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {

        String speechText = "";

        try {
            /*
             * Request request = input.getRequestEnvelope().getRequest(); IntentRequest
             * intentRequest = (IntentRequest) request; Intent intent =
             * intentRequest.getIntent();
             */
            String payload = "";
            String dish_category = "";
            String property = "\"price\"";
            String direction = "\"DESC\"";
            String priceLimit = null;
            String keyword = "";

            // Search Menu

            if (input.matches(intentName("SearchIntent"))) {
                Request request = input.getRequestEnvelope().getRequest();
                IntentRequest intentRequest = (IntentRequest) request;
                Intent intent = intentRequest.getIntent();

                Map<String, Slot> slotMap = intent.getSlots();
                if (slotMap.size() != 1) {
                    throw new AlexaException();
                }
                Slot search = slotMap.get("keyword");
                keyword = search.getValue();

                speechText = "Es wird nach " + keyword + " gesucht. ";
            }

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

                speechText = "Der Preis wurde auf " + priceLimit + " Euro eingeschraenkt. ";
            }

            // Sort Menu
            if (input.matches(intentName("SortIntent"))) {
                Request request = input.getRequestEnvelope().getRequest();
                IntentRequest intentRequest = (IntentRequest) request;
                Intent intent = intentRequest.getIntent();

                Map<String, Slot> slotMap = intent.getSlots();
                if ((slotMap.size() != 2) && (slotMap.size() != 1)) {
                    throw new AlexaException();
                }
                Slot direction_slot = slotMap.get("direction");
                Slot property_slot = slotMap.get("property");
                if (direction_slot.getValue() != null) {
                    if (direction_slot.getValue().equals("aufsteigend")) {
                        direction = "\"ASC\"";
                    }
                }

                if (property_slot.getValue().toLowerCase().equals("name")) {
                    property = "\"name\"";
                    speechText = "Die Sortierung erfolgt nun nach Name. ";
                } else if (property_slot.getValue().toLowerCase().equals("likes")) {
                    property = "\"description\"";
                    speechText = "Die Sortierung erfolgt nun nach Anzahl von Likes. ";
                } else if (property_slot.getValue().toLowerCase().equals("preis")) {
                    speechText = "Die Sortierung erfolgt nun nach Anzahl von Likes. ";
                }
            }
            if (input.matches(intentName("ChangeDirectionIntent"))) {
                Request request = input.getRequestEnvelope().getRequest();
                IntentRequest intentRequest = (IntentRequest) request;
                Intent intent = intentRequest.getIntent();

                Map<String, Slot> slotMap = intent.getSlots();
                if (slotMap.size() != 1) {
                    throw new AlexaException();
                }
                Slot direction_slot = slotMap.get("direction");
                if (direction_slot.getValue().toLowerCase().equals("aufsteigend")) {
                    direction = "\"ASC\"";
                    speechText = "Die Sortierung erfolgt nun aufsteigend. ";
                } else if (direction_slot.getValue().toLowerCase().equals("absteigend")) {
                    speechText = "Die Sortierung erfolgt nun absteigend. ";
                }
            }
            // if (input.matches(intentName("FavoritIntent")))
            // dish_category = "{\"id\":2},";

            // Read Description
            if (input.matches(intentName("DescriptionIntent"))) {
                Request request = input.getRequestEnvelope().getRequest();
                IntentRequest intentRequest = (IntentRequest) request;
                Intent intent = intentRequest.getIntent();

                Map<String, Slot> slotMap = intent.getSlots();
                if (slotMap.size() != 1) {
                    throw new AlexaException();
                }
                Slot dish = slotMap.get("dish");
                keyword = dish.getValue().toLowerCase();
            }

            payload = "{\"categories\":[" + dish_category + "],\"searchBy\":\"" + keyword
                    + "\",\"pageable\":{\"pageSize\":8,\"pageNumber\":0,\"sort\":[{\"property\":" + property
                    + ",\"direction\":" + direction + "}]},\"maxPrice\":" + priceLimit + ",\"minLikes\":null}";

            BasicOperations bo = new BasicOperations();
            Gson gson = new Gson();
            String response;
            ga.codehub.entity.menu.Response resp;
            try {
                response = bo.basicPost(payload, BASE_URL + "/mythaistar/services/rest/dishmanagement/v1/dish/search");
                if (!response.equals("no match")) {
                    resp = gson.fromJson(response, ga.codehub.entity.menu.Response.class);

                    if (input.matches(intentName("DescriptionIntent"))) {
                        speechText += "Beschreibung von " + resp.toStringName() + ": " + resp.toStringDescription();
                    }
                    else {
                        speechText += "Es gibt: " + resp.toStringNames();
                    }

                } else {
                    speechText = "Die Anfrage führte zu keinen Ergebnissen. Bitte versuchen Sie es eine andere Anfrage.";
                }
            } catch (Exception ex) {
                speechText = "Der MyThaiStar-Server scheint Probleme mit der Verarbeitung deiner Anfrage zu haben. "
                        + ex.toString();
                throw new AlexaException();
            }

        } catch (AlexaException e) {
            e.printStackTrace();
        }
        return input.getResponseBuilder().withSpeech(speechText).withSimpleCard("MyThaiStar", speechText)
                .withReprompt(speechText).build();

    }

}
