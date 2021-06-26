package ga.codehub.alexa.handlers;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.*;
import com.google.gson.Gson;
import ga.codehub.alexa.Exceptions.AlexaException;
import ga.codehub.entity.booking.Booking;
import ga.codehub.tools.BasicOperations;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;
import static ga.codehub.alexa.MyThaiStarStreamHandler.BASE_URL;

public class InhouseOrderIntentHandler implements RequestHandler {
    private String speechText = "";

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("InhouseOrderIntent")) || input.matches(intentName("InhouseAddOrderIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {

        AttributesManager attributesManager = input.getAttributesManager();
        Map<String, Object> attributes = attributesManager.getSessionAttributes();

        // Booking
        String name = "";
        String userEmail = "";
        String personCount = "";
        String bookingToken = "";

        // Order
        String dish = "";
        String extrasIds = "";
        String amount = "";
        Integer dishID = -1;
        String extras = "";


        try {
            try {
                name = input.getServiceClientFactory().getUpsService().getProfileName();
                userEmail = input.getServiceClientFactory().getUpsService().getProfileEmail();
            } catch (NullPointerException nullp) {
                speechText = "Deine Alexa braucht zusätzliche Berechtigungen!";
                throw new AlexaException();
            }

            Request request = input.getRequestEnvelope().getRequest();
            IntentRequest intentRequest = (IntentRequest) request;
            Intent intent = intentRequest.getIntent();

            if(intent.getConfirmationStatus().toString().equals("DENIED")){
                speechText = "Die Bestellung wurde abgebrochen. Um die Bestellung neu zu starten, sagen Sie bitte Bestellung taetigen.";
            }else{
                Map<String, Slot> slotMap = intent.getSlots();
                if ((slotMap.size() != 4)&&(slotMap.size() != 3)) {
                    throw new AlexaException();
                }
                Slot amount_s = slotMap.get("amount");
                Slot dish_s = slotMap.get("dish");
                Slot extras_s = slotMap.get("extras");

                amount = amount_s.getValue();
                dish = dish_s.getValue();
                extras = extras_s.getValue();
                Boolean tofu = (dish.equals("thai green chicken curry") || dish.equals("thai spicy basil fried rice") || dish.equals("thai peanut beef"));
                Boolean curry = (dish.equals("thai green chicken curry") || dish.equals("thai spicy basil fried rice") || dish.equals("thai thighs fish/prawns") || dish.equals("garlic paradise salad"));

                switch (extras) {
                    case "tofu":
                        if(tofu){
                            extrasIds = "{\"id\":0}";
                        }else {
                            extrasIds = "";
                        }
                        break;
                    case "curry":
                        if(curry){
                            extrasIds = "{\"id\":1}";
                        }else {
                            extrasIds = "";
                        }
                        break;
                    case "tofu und curry":
                    case "curry und tofu":
                        if(tofu && curry){
                            extrasIds = "{\"id\":0},{\"id\":1}";
                        }else {
                            extrasIds = "";
                        }
                        break;
                    default:
                        extrasIds = "";
                        break;
                }

                dishID = getDishIDbyName(dish);

                if (dishID == -1) {
                    speechText = "Leider fuehrte die Suche nach dem Gericht zu keinen Ergebnissen. Bitte versuchen Sie eine andere Anfrage. ";

                } else {
                    ArrayList<String> orderlines;
                    ArrayList<String> shoppingcart;
                    if (!attributes.containsKey("orderLines")) {
                        Slot personCount_s = slotMap.get("personCount");
                        personCount = personCount_s.getValue();

                        ga.codehub.entity.booking.Request myApiRequest = new ga.codehub.entity.booking.Request();
                        myApiRequest.booking = new Booking();
                        myApiRequest.booking.email = userEmail;
                        myApiRequest.booking.assistants = personCount;
                        Instant time = Instant.now().plus(2, ChronoUnit.MINUTES);
                        myApiRequest.booking.bookingDate = time.toString();
                        myApiRequest.booking.name = name;

                        BasicOperations bo = new BasicOperations();
                        Gson gson = new Gson();
                        String payload = gson.toJson(myApiRequest);
                        String response;
                        ga.codehub.entity.booking.Booking resp;

                        try {
                            response = bo.basicPost(payload, BASE_URL + "/mythaistar/services/rest/bookingmanagement/v1/booking");

                        } catch (Exception ex) {
                            speechText = "Der my-thai-star Server scheint Probleme mit der Verarbeitung deiner Anfrage zu haben";
                            throw new AlexaException();
                        }

                        resp = gson.fromJson(response, ga.codehub.entity.booking.Booking.class);
                        bookingToken = resp.bookingToken;
                        attributes.put("bookingToken", resp.bookingToken);
                        attributesManager.setSessionAttributes(attributes);

                        orderlines = new ArrayList<String>();
                        attributes.put("orderLines", orderlines);

                        shoppingcart = new ArrayList<String>();
                        attributes.put("shoppingcart", shoppingcart);

                        String payload_beginning = "{\"booking\":{\"bookingToken\":\"" + bookingToken + "\"},\"orderLines\":[";
                        String payload_ending = "]}";

                        attributes.put("beginning", payload_beginning);
                        attributes.put("ending", payload_ending);

                    } else {
                        orderlines = (ArrayList<String>) attributes.get("orderLines");
                        shoppingcart = (ArrayList<String>) attributes.get("shoppingcart");
                    }

                    orderlines.add("{\"orderLine\":{\"dishId\":" + dishID + ",\"amount\":" + amount + ",\"comment\":\"\"},\"extras\":[" + extrasIds + "]}");
                    shoppingcart.add(dish + ";" + amount + ";" + extras);
                    speechText = "Das Gericht wurde in den Warenkorb gelegt. Antworten Sie mit Weiteres Gericht hinzufuegen, wenn Sie ein weiteres Gericht hinzufuegen wollen und mit Bestellung absenden, um die Bestellung zu beenden.";
                }
            }

        } catch (AlexaException e) {
            speechText = "Ein Fehler ist aufgetreten...";
            e.printStackTrace();
        }

        return input.getResponseBuilder()
                .withSpeech(speechText)
                .withSimpleCard("MyThaiStar", speechText)
                .build();
    }

    private Integer getDishIDbyName(String DishName) {

        String payload = "";
        Integer dishID = -1;
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

                    dishID = Integer.parseInt(resp.content[0].dish.id);

                } else {
                    dishID = -1;
                }

            } catch (Exception ex) {
                speechText = "Der my-thai-star Server scheint Probleme mit der Verarbeitung deiner Anfrage zu haben. "
                        + ex.toString();
                throw new AlexaException();
            }
        } catch (AlexaException e) {
            e.printStackTrace();
        }

        return dishID;
    }
}
