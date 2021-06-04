package main.java.ga.codehub.alexa.handlers;

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

public class OrderIntentHandler implements RequestHandler {

    private String speechText = "";

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("OrderIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        
        String payload = "";

        String bookingToken = "aadfasd";

        String item = "";
        String comment = "";
        String extras = "";
        String amount = "";
        String dishID = "";

        try {

            Request request = input.getRequestEnvelope().getRequest(); 
            IntentRequest intentRequest = (IntentRequest) request; 
            Intent intent = intentRequest.getIntent();

            Map<String, Slot> slotMap = intent.getSlots();
            if (slotMap.size() != 2) {
                throw new AlexaException();
            }
            Slot amount_s = slotMap.get("amount");
            Slot item_s = slotMap.get("item");
            // Slot extras_s = slotMap.get("extra");

            amount = amount_s.getValue();
            item = item_s.getValue();
            // extras = extras_s.getValue();

            dishID = getDishIDbyName(item);
            System.out.println(":::::::::::::::::"+ dishID +"::::::::::::");

            if (dishID.equals("-1")) {
                speechText = "Die Anfrage fuehrte zu keinen Ergebnissen. Bitte versuchen Sie es eine andere Anfrage. ";  
            } else {

                payload = "{\"booking\": {\"bookingToken\":\"" + bookingToken +
                          "\"},\"orderLines\": [{\"orderLine\": {\"dishId\":" + dishID + 
                          ",\"amount\":" + amount + ",\"comment\":\"\"}, \"extras\":[]}]}";

                BasicOperations bo = new BasicOperations();
                Gson gson = new Gson();
                String response;
                ga.codehub.entity.menu.Response resp;

                try {
                    response = bo.basicPost(payload, BASE_URL + "/mythaistar/services/rest/ordermanagement/v1/order");

                } catch (Exception ex) {
                    speechText = "Der MyThaiStar-Server scheint Probleme mit der Verarbeitung deiner Anfrage zu haben. "
                            + ex.toString();
                    throw new AlexaException();
                }
            }

        } catch (AlexaException e) {
            e.printStackTrace();
        }

        return input.getResponseBuilder()
                    .withSpeech(speechText)
                    .withSimpleCard("MyThaiStar", speechText)
                    .withReprompt(speechText)
                    .build();

    }

    private String getDishIDbyName (String DishName) {

        System.out.println("::::::::::::::::: GETDISH ::::::::::::");

        String payload = "";
        String dishID = "";
        try {

            payload = "{\"categories\":[],\"searchBy\":\"" + DishName + "\",\"pageable\":{\"pageSize\":8,\"pageNumber\":0,"
                    + "\"sort\":[{\"property\":\"price\",\"direction\":\"DESC\"}]},\"maxPrice\":null,\"minLikes\":null}";
        
            BasicOperations bo = new BasicOperations();
            Gson gson = new Gson();
            String response;
            ga.codehub.entity.menu.Response resp;
            try {
                response = bo.basicPost(payload, BASE_URL + "/mythaistar/services/rest/dishmanagement/v1/dish/search");
                if (!response.equals("no match")) {
                    resp = gson.fromJson(response, ga.codehub.entity.menu.Response.class);

                    dishID = resp.content[0].dish.id;

                } else {
                    speechText = "Die Anfrage führte zu keinen Ergebnissen. Bitte versuchen Sie es eine andere Anfrage.";

                    dishID = "no result";
                }

            } catch (Exception ex) {
                speechText = "Der MyThaiStar-Server scheint Probleme mit der Verarbeitung deiner Anfrage zu haben. "
                    + ex.toString();
                throw new AlexaException();
             }
        } catch(AlexaException e) {
            e.printStackTrace();
        }
        return dishID;
    }
}
