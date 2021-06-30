package ga.codehub.alexa.handlers;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import ga.codehub.tools.BasicOperations;
import ga.codehub.tools.exceptions.Different;
import ga.codehub.tools.exceptions.NotFound;

import java.io.IOException;
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
        AttributesManager attributesManager = input.getAttributesManager();
        Map<String, Object> attributes = attributesManager.getSessionAttributes();
        String bookingToken = (String) attributes.get("bookingToken");

        String speechText = "";
        BasicOperations bo = new BasicOperations();


        try {
            bo.basicGET(BASE_URL + "/bookingmanagement/v1/booking/help/" + bookingToken);
            speechText = "Ein Kellner wurde benachrichtigt und wird so bald wie moeglich bei dir sein.";

        } catch (Different | NullPointerException ex) {
            speechText = "Ein Kellner wurde benachrichtigt und wird so bald wie moeglich bei dir sein.";
        } catch (NotFound | IOException ex) {
            speechText = "Der my-thai-star Server scheint Probleme bei der Verarbeitung deiner Anfrage zu haben.";
        }

        return input.getResponseBuilder()
                .withSpeech(speechText)
                .withShouldEndSession(false)
                .withSimpleCard("MyThaiStar", speechText)
                .build();
    }
}
