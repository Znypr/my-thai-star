package ga.codehub.alexa.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class InformationIntentHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("InformationIntent")) || input.matches(intentName("OpeningHoursIntent")) || input.matches(intentName("AdressIntent")) || input.matches(intentName("PaymentIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        String speechText = " ";
        if(input.matches(intentName("InformationIntent"))) {
            speechText = "Das My-Thai-Star Restaurant ist ein virtuelles Restaurant der Firma Capgemini. In unserem kleinen, modernen Restaurant können Ihre Freunde und Sie gemeinsam bei einem entspannten Abendessen die thailändische Küche erleben und genießen. Wir versuchen stets unserem Motto Mehr als nur leckeres Essen treu zu bleiben und hoffen, dass es Ihnen bei uns gefällt.";
        } else if (input.matches(intentName("OpeningHoursIntent"))){
            speechText = "Das virtuelle My-Thai-Star Restaurant hat von Dienstag bis Sonntag zwischen 11 Uhr und 23 Uhr geöffnet.";
        } else if (input.matches(intentName("AdressIntent"))){
            speechText = "Die Adresse des My-Thai-Star Restaurants lautet: Matrix-Straße 42 101010 Magrathea.";
        } else if (input.matches(intentName("PaymentIntent"))){
            speechText = "Die folgenden Zahlungsmöglichkeiten gibt es in unserem Restaurant: Kreditkarte, EC-Karte, Bar, Paypal, Google Pay, Apple Pay, BitCoin.";
        }

        return input.getResponseBuilder()
                .withSpeech(speechText)
                .withSimpleCard("MyThaiStar", speechText)
                .withShouldEndSession(false)
                .build();
    }
}
