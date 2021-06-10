package ga.codehub.alexa.handlers;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.*;
import com.google.gson.Gson;
import ga.codehub.alexa.Exceptions.AlexaException;
import ga.codehub.tools.BasicOperations;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;
import static ga.codehub.alexa.MyThaiStarStreamHandler.BASE_URL;

public class InhouseOrderIntentHandler implements RequestHandler {
    private String speechText = "";

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("OrderIntent")) || input.matches(intentName("AddOrderIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {

        AttributesManager attributesManager = input.getAttributesManager();
        Map<String, Object> attributes = attributesManager.getSessionAttributes();

        // Booking
        String bookingToken = "";

        // Order
        String item = "";
        String comment = "";
        String extras = "";
        String amount = "";
        Integer dishID = -1;

        if (attributes.containsKey("bookingToken")) {

            bookingToken = attributes.get("bookingToken").toString();

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

                if (dishID == -1) {
                    speechText = "Die Anfrage fuehrte zu keinen Ergebnissen. Bitte versuchen Sie es eine andere Anfrage. ";
                } else {

                    ArrayList<String> orderlines;

                    if (!attributes.containsKey("orderLines")) {
                        orderlines = new ArrayList<String>();
                        attributes.put("orderLines", orderlines);

                        String payload_beginning = "{\"booking\":{\"bookingToken\":\"" + bookingToken + "\"},\"orderLines\":[";
                        String payload_ending = "]}";

                        attributes.put("beginning", payload_beginning);
                        attributes.put("ending", payload_ending);

                    } else {
                        orderlines = (ArrayList<String>) attributes.get("orderLines");
                    }


                    orderlines.add("{\"orderLine\":{\"dishId\":" + dishID + ",\"amount\":" + amount + ",\"comment\":\"\"},\"extras\":[]}");
                    speechText = "Möchten Sie noch weitere Dinge bestellen? ";

                    // BasicOperations bo = new BasicOperations();
                    // Gson gson = new Gson();
                    // String response;
                    // ga.codehub.entity.menu.Response resp;

                    // try {
                    //     response = bo.basicPost(payload, BASE_URL + "/mythaistar/services/rest/ordermanagement/v1/order");
                    //     resp = gson.fromJson(response, ga.codehub.entity.menu.Response.class);
                    //     speechText = "Sie haben erfolgreich " + amount + " mal " + item + " bestellt. ";

                    // } catch (Exception ex) {
                    //     speechText = "Der MyThaiStar-Server scheint Probleme mit der Verarbeitung deiner Anfrage zu haben. "
                    //             + ex.toString();
                    //     throw new AlexaException();
                    // }
                }

            } catch (AlexaException e) {
                e.printStackTrace();
            }

            return input.getResponseBuilder()
                    .withSpeech(speechText)
                    .withSimpleCard("MyThaiStar", speechText)
                    .withReprompt("Möchten Sie noch weitere Dinge bestellen?")
                    .build();


        } else {
            // The user must have invoked this intent before they order.
            // Trigger the BookingIntent.

            // Create the intent.
            Intent intent = Intent.builder()
                    .withName("BookingIntent")
                    .build();

            return input.getResponseBuilder()
                    .withSpeech("Sie müssen zunächst einen Tisch buchen um Bestellungen zu tätigen. ")
                    .build();
        }
    }

    private Integer getDishIDbyName (String DishName) {

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
                    speechText = "Die Anfrage führte zu keinen Ergebnissen. Bitte versuchen Sie es eine andere Anfrage.";

                    dishID = -1;
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
