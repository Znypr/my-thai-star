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

public class MenuIntentHandler implements RequestHandler {

    public static void main(String[] args) {
        BasicOperations bo = new BasicOperations();
        Gson gson = new Gson();
        String payload = "{\"categories\":[],\"searchBy\":\"\",\"pageable\":{\"pageSize\":8,\"pageNumber\":0,\"sort\":[{\"property\":\"price\",\"direction\":\"DESC\"}]},\"maxPrice\":null,\"minLikes\":null}";
        String response;
        ga.codehub.entity.menue.Response resp;
        try {
            response = bo.basicPost(payload, BASE_URL + "/mythaistar/services/rest/dishmanagement/v1/dish/search");
            resp = gson.fromJson(response, ga.codehub.entity.menue.Response.class);
            System.out.println(response);
        } catch (Exception ex) {


        }

    }

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("MenueIntent")) 
        || input.matches(intentName("FoodIntent"))
        || input.matches(intentName("DrinkIntent"))
        || input.matches(intentName("CurryIntent"))
        || input.matches(intentName("VeganIntent"))
        || input.matches(intentName("RiceIntent"))
        || input.matches(intentName("VegetarianIntent"))
        || input.matches(intentName("StarterIntent"))
        || input.matches(intentName("DessertIntent"))
        || input.matches(intentName("NoodleIntent"))
        || input.matches(intentName("SortByNameDESCIntent"))
        || input.matches(intentName("SortByPriceDESCIntent"))
        || input.matches(intentName("SortByLikesDESCIntent"))
        || input.matches(intentName("SortByNameASCIntent"))
        || input.matches(intentName("SortByPriceASCIntent"))
        || input.matches(intentName("SortByLikesASCIntent"));
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
            String property = "";
            String direction = "\"DESC\"";

 
            // Filter Menu
            if (input.matches(intentName("MenueIntent")))
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


            // Sort Menu
            if(input.matches(intentName("SortByNameDESCIntent"))) {
                property = "\"name\"";
                speechText = "Es wird nun nach dem Namen in absteigender Richtung sortiert.";
            }if(input.matches(intentName("SortByPriceDESCIntent"))) {
                property = "\"price\"";
                speechText = "Es wird nun nach dem Preis in absteigender Richtung sortiert.";
            }if(input.matches(intentName("SortByLikesDESCIntent"))) {
                property = "\"description\"";
                speechText = "Es wird nun nach Likes in absteigender Richtung sortiert.";
            }
            if(input.matches(intentName("SortByNameASCIntent"))) {
                property = "\"name\"";
                direction = "\"ASC\"";
                speechText = "Es wird nun nach dem Namen in aufsteigender Richtung sortiert.";
            } if(input.matches(intentName("SortByPriceASCIntent"))) {
                property = "\"price\"";
                direction = "\"ASC\"";
                speechText = "Es wird nun nach dem Preis in aufsteigender Richtung sortiert.";
            }
            if(input.matches(intentName("SortByLikesASCIntent"))) {
                property = "\"description\"";
                direction = "\"ASC\"";
                 speechText = "Es wird nun nach Likes in aufsteigender Richtung sortiert.";
            }


            payload = "{\"categories\":[" + dish_category + "],\"searchBy\":\"\",\"pageable\":{\"pageSize\":8,\"pageNumber\":0,\"sort\":[{\"property\":" + property + ",\"direction\":" + direction + "}]},\"maxPrice\":null,\"minLikes\":null}";
                


            BasicOperations bo = new BasicOperations();
            Gson gson = new Gson();
            String response;
            ga.codehub.entity.menue.Response resp;
            try {
                response = bo.basicPost(payload, BASE_URL + "/mythaistar/services/rest/dishmanagement/v1/dish/search");
                resp = gson.fromJson(response, ga.codehub.entity.menue.Response.class);
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
