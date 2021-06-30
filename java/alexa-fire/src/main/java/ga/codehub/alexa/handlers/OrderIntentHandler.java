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

        String bookingToken = "";
        String dish = "";
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
                    speechText = "Die Bestellung wurde abgebrochen. Um die Bestellung neu zu starten, sagen Sie bitte Bestellung taetigen.";

                }else{
                    Map<String, Slot> slotMap = intent.getSlots();

                    if (slotMap.size() != 3) {
                        throw new AlexaException();
                    }

                    Slot amountSlot = slotMap.get("amount");
                    Slot dishSlot = slotMap.get("dish");
                    Slot extrasSlot = slotMap.get("extras");

                    amount = amountSlot.getValue();
                    dish = dishSlot.getValue();
                    extras = extrasSlot.getValue();

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
                        speechText = "Leider fuehrte die Suche nach dem Gericht zu keinen Ergebnissen. Bitte versuchen Sie eine andere Anfrage.";

                    } else {
                        ArrayList<String> orderlines;
                        ArrayList<String> shoppingcart;

                        if (!attributes.containsKey("orderLines")) {
                            orderlines = new ArrayList<String>();
                            attributes.put("orderLines", orderlines);

                            shoppingcart = new ArrayList<String>();
                            attributes.put("shoppingcart", shoppingcart);

                            String payloadStart = "{\"booking\":{\"bookingToken\":\"" + bookingToken + "\"},\"orderLines\":[";
                            attributes.put("start", payloadStart);

                            String payloadEnd = "]}";
                            attributes.put("end", payloadEnd);

                        } else {
                            orderlines = (ArrayList<String>)attributes.get("orderLines");
                            shoppingcart = (ArrayList<String>) attributes.get("shoppingcart");
                        }

                        orderlines.add("{\"orderLine\":{\"dishId\":" + dishID + ",\"amount\":" + amount + ",\"comment\":\"\"},\"extras\":[" + extrasIds + "]}");
                        shoppingcart.add(dish + ";" + amount + ";" + extras);

                        speechText = "Das Gericht wurde in den Warenkorb gelegt. Antworten Sie mit Weiteres Gericht hinzufuegen, wenn Sie ein weiteres Gericht hinzufuegen wollen oder mit Bestellung absenden, um die Bestellung zu beenden.";
                    }
                }

            } catch (AlexaException e) {
                speechText = "Der my-thai-star Server scheint Probleme mit dem Hinzufuegen Ihrer Bestellung zum Warenkorb zu haben. Bitte versuchen Sie es nochmal.";
            }

            return input.getResponseBuilder()
                        .withSpeech(speechText)
                        .withSimpleCard("MyThaiStar", speechText)
                        .build();

    
        } else {
            Intent intent = Intent.builder()
                .withName("BookingIntent")
                .build();

            return input.getResponseBuilder()
                    .withSpeech("Sie müssen zunaechst einen Tisch buchen um eine Bestellung taetigen zu koennen. ")
                    .build();     
        }
    }
        

       

    private Integer getDishIDbyName (String dish) {
        String payload;
        Integer dishID = -1;

        try {
            payload = "{\"categories\":[],\"searchBy\":\"" + dish + "\",\"pageable\":{\"pageSize\":8,\"pageNumber\":0,"
                    + "\"sort\":[{\"property\":\"price\",\"direction\":\"DESC\"}]},\"maxPrice\":null,\"minLikes\":null}";
            BasicOperations bo = new BasicOperations();
            Gson gson = new Gson();
            String response;
            ga.codehub.entity.menu.Response resp;

            try {
                response = bo.basicPost(payload, BASE_URL + "/dishmanagement/v1/dish/search");

                if (!response.equals("no match")) {
                    resp = gson.fromJson(response, ga.codehub.entity.menu.Response.class);
                    dishID = Integer.parseInt(resp.content[0].dish.id);

                } else {
                    dishID = -1;
                }

            } catch (Exception ex) {
                speechText = "Der my-thai-star Server scheint Probleme beim Finden des Gerichts in der Datenbank zu haben. Bitte versuchen Sie es nochmal.";
                throw new AlexaException();
            }

        } catch(AlexaException e) {
            e.printStackTrace();
        }

        return dishID;
    }
}
