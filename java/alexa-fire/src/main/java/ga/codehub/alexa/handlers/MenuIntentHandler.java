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

import com.amazon.ask.attributes.AttributesManager;
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

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("MenuIntent"))
                || input.matches(intentName("FoodIntent")) || input.matches(intentName("DrinkIntent"))
                || input.matches(intentName("CurryIntent")) || input.matches(intentName("VeganIntent"))
                || input.matches(intentName("RiceIntent")) || input.matches(intentName("VegetarianIntent"))
                || input.matches(intentName("StarterIntent")) || input.matches(intentName("DessertIntent"))
                || input.matches(intentName("NoodleIntent")) || input.matches(intentName("SortIntent"))
                || input.matches(intentName("SearchIntent")) || input.matches(intentName("ChangeDirectionIntent"))
                || input.matches(intentName("DescriptionIntent")) || input.matches(intentName("MaxPriceIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        AttributesManager attributesManager = input.getAttributesManager();
        Map<String, Object> attributes = attributesManager.getSessionAttributes();
        String speechText = "";
        String payload = "";
        String dish_category = "";
        String property = "\"price\"";
        String direction = "\"DESC\"";
        String priceLimit = null;
        String keyword = "";
        String extras = "";

        try {
            Request request = input.getRequestEnvelope().getRequest();
            IntentRequest intentRequest = (IntentRequest) request;
            Intent intent = intentRequest.getIntent();
            Map<String, Slot> slotMap = intent.getSlots();

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
                if (slotMap.size() != 1) {
                    throw new AlexaException();
                }

                Slot maxPrice = slotMap.get("maxPrice");
                priceLimit = maxPrice.getValue();
                speechText = "Der Preis wurde auf maximal " + priceLimit + " Euro beschraenkt. ";
            }

            // Sort Menu
            if (input.matches(intentName("SortIntent"))) {
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
                    speechText = "Die Sortierung erfolgt nun nach Preis. ";
                }

                attributes.put("currentProperty", property);
                attributes.put("currentDirection", direction);
            }

            // Search Menu
            if (input.matches(intentName("SearchIntent"))) {
                if (slotMap.size() != 1) {
                    throw new AlexaException();
                }

                Slot search = slotMap.get("keyword");
                keyword = search.getValue();
                speechText = "Es wird nach " + keyword + " gesucht. ";
            }

            // Change the direction of sort
            if (input.matches(intentName("ChangeDirectionIntent"))) {
                if ((slotMap.size() != 1)&&(slotMap.size() != 0)) {
                    throw new AlexaException();
                }

                Slot direction_slot = slotMap.get("direction");
                property = attributes.get("currentProperty").toString();
                direction = attributes.get("currentDirection").toString();

                if (direction_slot.getValue() != null) {
                    if (direction_slot.getValue().toLowerCase().equals("aufsteigend")) {
                        direction = "\"ASC\"";
                        speechText = "Die Sortierung erfolgt nun aufsteigend. ";

                    } else if (direction_slot.getValue().toLowerCase().equals("absteigend")) {
                        speechText = "Die Sortierung erfolgt nun absteigend. ";
                    }

                }else {
                    attributes.remove("currentDirection");
                    if(direction.equals("\"ASC\"")){
                        direction = "\"DESC\"";
                        speechText = "Die Sortierung erfolgt nun absteigend. ";

                    }else{
                        direction = "\"ASC\"";
                        speechText = "Die Sortierung erfolgt nun aufsteigend. ";
                    }
                }

                attributes.put("currentDirection", direction);
            }

            // Read Description
            if (input.matches(intentName("DescriptionIntent"))) {
                if (slotMap.size() != 1) {
                    throw new AlexaException();
                }

                Slot dish = slotMap.get("dish");
                keyword = dish.getValue();
                Boolean tofu = (keyword.equals("thai green chicken curry") || keyword.equals("thai spicy basil fried rice") || keyword.equals("thai peanut beef"));
                Boolean curry = (keyword.equals("thai green chicken curry") || keyword.equals("thai spicy basil fried rice") || keyword.equals("thai thighs fish/prawns") || keyword.equals("garlic paradise salad"));

                if(tofu) {
                    extras = " Tofu Option verfuegbar.";

                }else if(curry) {
                    extras = " Extra Curry Option verfuegbar";

                }else if(tofu && curry){
                    extras = " Extra Curry und Tofu Optionen verfuegbar";

                }else {
                    extras = " Keine weiteren Bestelloptionen verfuegbar.";
                }
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

                    if (input.matches(intentName("DescriptionIntent")) && resp.content.length == 1) {
                        speechText += "Beschreibung von " + resp.toStringName() + " : " + resp.toStringDescription() + " " + extras ;

                    } else {
                        speechText += "Es gibt: " + resp.toStringNames();
                    }

                } else {
                    speechText = "Die Anfrage fuehrte zu keinen Ergebnissen. Bitte versuchen Sie eine andere Anfrage.";
                }

            } catch (Exception ex) {
                speechText = "Der my-thai-star Server scheint Probleme mit der Verarbeitung Ihrer Anfrage zu haben. Bitte versuchen Sie es nochmal.";
                throw new AlexaException();
            }

        } catch (AlexaException e) {
            e.printStackTrace();
        }

        return input.getResponseBuilder()
        .withSpeech(speechText)
        .withSimpleCard("MyThaiStar", speechText)
        .withShouldEndSession(false)
        .build();

    }

}
