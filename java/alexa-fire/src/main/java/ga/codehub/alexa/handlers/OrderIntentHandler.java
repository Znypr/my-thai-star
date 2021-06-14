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
import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;
import static ga.codehub.alexa.MyThaiStarStreamHandler.BASE_URL;
public class OrderIntentHandler implements RequestHandler {

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
        String dish = "";
        //String comment = "";
        String extras = "";
        String amount = "";
        String extrasIds = "";
        Integer dishID = -1;

        if (attributes.containsKey("bookingToken")) {
            bookingToken = attributes.get("bookingToken").toString();

            try {
                Request request = input.getRequestEnvelope().getRequest(); 
                IntentRequest intentRequest = (IntentRequest) request; 
                Intent intent = intentRequest.getIntent();

                if(intent.getConfirmationStatus().toString().equals("DENIED")){
                    /*attributes.put("cancledSlots", intent.getSlots());
                    attributesManager.setSessionAttributes(attributes);*/
                    speechText = "Die Bestellung wurde abgebrochen. Um die Bestellung neu zu starten, sagen Sie bitte Bestellung tätigen.";
                }else{
                    Map<String, Slot> slotMap = intent.getSlots();
                    if (slotMap.size() != 3) {
                        throw new AlexaException();
                    }
                    Slot amount_s = slotMap.get("amount");
                    Slot dish_s = slotMap.get("item");
                    Slot extras_s = slotMap.get("extra");

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
                            orderlines = (ArrayList<String>)attributes.get("orderLines");
                        }


                        orderlines.add("{\"orderLine\":{\"dishId\":" + dishID + ",\"amount\":" + amount + ",\"comment\":\"\"},\"extras\":[" + extrasIds + "]}");
                        speechText = "Das Gericht wurde in den Warenkorb gelegt. Antworten Sie mit Weiteres Gericht hinzufuegen, wenn Sie ein weiteres Gericht hinzufuegen wollen und mit Bestellung absenden, um die Bestellung zu beenden.";
                    }
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
