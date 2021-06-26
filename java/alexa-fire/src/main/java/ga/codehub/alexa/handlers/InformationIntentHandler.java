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
            speechText = "Das my-thai-star Restaurant ist ein virtuelles Restaurant der Firma Capgemini. In unserem kleinen, modernen Restaurant koennen Ihre Freunde und Sie gemeinsam bei einem entspannten Abendessen die thailaendische Kueche erleben und geniessen. Wir versuchen stets unserem Motto Mehr als nur leckeres Essen treu zu bleiben und hoffen, dass es Ihnen bei uns gefaellt.";
        } else if (input.matches(intentName("OpeningHoursIntent"))){
            speechText = "Das virtuelle my-thai-star Restaurant hat von Dienstag bis Sonntag zwischen 11 Uhr und 23 Uhr geoeffnet.";
        } else if (input.matches(intentName("AdressIntent"))){
            speechText = "Die Adresse des my-thai-star Restaurants lautet: Matrix-Straße 42 101010 Magrathea.";
        } else if (input.matches(intentName("PaymentIntent"))){
            speechText = "Die folgenden Zahlungsmoeglichkeiten gibt es in unserem Restaurant: Kreditkarte, EC-Karte, Bar, Paypal, Google Pay, Apple Pay, BitCoin.";
        }

        return input.getResponseBuilder()
                .withSpeech(speechText)
                .withSimpleCard("MyThaiStar", speechText)
                .withShouldEndSession(false)
                .build();
    }
}
