package ga.codehub.alexa.handlers;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class ShoppingCartIntentHandler implements RequestHandler {
    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("ShoppingCartIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        String speechText = "";
        AttributesManager attributesManager = input.getAttributesManager();
        Map<String, Object> attributes = attributesManager.getSessionAttributes();

        if (!attributes.containsKey("orderLines")) {
            speechText = "Ihr Warenkorb ist derzeit leer.";

        }else{
            ArrayList<String> shoppingcart = (ArrayList<String>) attributes.get("shoppingcart");
            speechText = "Ihr Warenkorb beinhaltet derzeit folgende Gerichte: ";

            for (String itemString : shoppingcart) {
                String[] item = itemString.split(";");

                if ((item[2].equals("nichts")) || (item[2].equals("kein")) || (item[2].equals("nein"))) {
                    speechText += item[1] + " " + item[0];

                } else {
                    speechText += item[1] + " " + item[0] + " mit extra " + item[2];
                }

                if(!itemString.equals(shoppingcart.get(shoppingcart.size() - 1))){
                    speechText += ", ";
                }
            }
        }

        return input.getResponseBuilder()
                .withSpeech(speechText)
                .withSimpleCard("MyThaiStar", speechText)
                .withShouldEndSession(false)
                .build();
    }
}
