package ga.codehub.alexa.handlers;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.*;
import com.google.gson.Gson;
import ga.codehub.RestDateManager;
import ga.codehub.alexa.Exceptions.AlexaException;
import ga.codehub.entity.booking.Booking;
import ga.codehub.tools.BasicOperations;
import ga.codehub.tools.exceptions.Different;
import ga.codehub.tools.exceptions.NotFound;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;
import static ga.codehub.alexa.MyThaiStarStreamHandler.BASE_URL;

public class AskForHelpIntentHandler implements RequestHandler {
    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("AskForHelpIntent"));
    }


    @Override
    public Optional<Response> handle(HandlerInput input) {
        String speechText = "";
        AttributesManager attributesManager = input.getAttributesManager();
        Map<String, Object> attributes = attributesManager.getSessionAttributes();
        String bookingToken = (String) attributes.get("orderLines");
        String response;
        BasicOperations bo = new BasicOperations();
        String payload ="";

        try {
            response = bo.basicPost(bookingToken, BASE_URL + "/mythaistar/services/rest/bookingmanagement/v1/booking/help");
            speechText = "Ein Kellner wurde benachrichtigt und wird so bald wie möglich bei dir sein.";
        } catch (IOException | NotFound | Different e) {
            speechText = "Der my thai star Server scheint Probleme bei der Verarbeitung deiner Anfrage zu haben.";
            e.printStackTrace();
        }

        return input.getResponseBuilder()
                .withSpeech(speechText)
                .withSimpleCard("MyThaiStar", speechText)
                .build();
    }
}
