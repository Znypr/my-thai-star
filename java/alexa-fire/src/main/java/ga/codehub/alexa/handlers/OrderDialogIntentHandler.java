package ga.codehub.alexa.handlers;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.*;
import com.google.gson.Gson;
import ga.codehub.alexa.Exceptions.AlexaException;
import ga.codehub.entity.booking.Booking;
import ga.codehub.tools.BasicOperations;
import ga.codehub.RestDateManager;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.amazon.ask.request.Predicates.intentName;
import static ga.codehub.alexa.MyThaiStarStreamHandler.BASE_URL;

public class OrderDialogIntentHandler implements RequestHandler {

    
    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("FinishOrderIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        AttributesManager attributesManager = input.getAttributesManager();
        Map<String, Object> attributes = attributesManager.getSessionAttributes();
        String speechText = "";

        try {
            BasicOperations bo = new BasicOperations();
            String payloadStart = (String) attributes.get("start");
            String payloadEnd = (String) attributes.get("end");
            ArrayList<String> orderlines = (ArrayList<String>) attributes.get("orderLines");
            String payload = buildPayLoad(orderlines, payloadStart, payloadEnd);

            try {
                bo.basicPost(payload, BASE_URL + "/mythaistar/services/rest/ordermanagement/v1/order");     
                speechText = "Vielen Dank fuer Ihre Bestellung. ";
                attributes.remove("orderLines");
                attributes.remove("shoppingcart");

            } catch (Exception ex) {
                speechText = "Der my-thai-star Server scheint Probleme mit dem Abschließen Ihrer Bestellung zu haben. Bitte versuchen Sie es nochmal.";
                throw new AlexaException();
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

    public static String buildPayLoad(ArrayList<String> orderlines, String start, String end) {
        Iterator<String> iterator = orderlines.iterator();
        String payload = start;

        while(iterator.hasNext()) {
            String orderline = iterator.next();
        
            if(!iterator.hasNext()) {
                payload += orderline;

            } else {
                payload += orderline + ",";
            }
        }

        payload += end;
        return payload;
    }
}
