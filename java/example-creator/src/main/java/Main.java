import com.google.gson.Gson;
import entity.User;
import ga.codehub.RestDateManager;
import ga.codehub.entity.booking.Booking;
import ga.codehub.entity.booking.Request;
import ga.codehub.entity.menu.Response;
import ga.codehub.entity.order.OrderLine;
import ga.codehub.entity.order.OrderLines;
import ga.codehub.tools.BasicOperations;
import ga.codehub.tools.exceptions.Different;
import ga.codehub.tools.exceptions.NotFound;
import org.apache.http.HttpResponse;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class Main {

    public static final int iterations = 500;

    static String baseUrl = "http://localhost:8081/mythaistar/services/rest/";

    public static void main(String[] args) {


        BasicOperations bo = new BasicOperations();

        /*
        For Windows users change chromedrive to chromedriver.exe
         */
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Joost\\Desktop\\Projekte\\my-thai-star\\java\\example-creator\\driver\\chromedriver.exe");
        System.setProperty("webdriver.gecko.driver", "java/example-creator/driver/geckodriver/geckodriver");

        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);

        //login in a user;
        String json_body = "{\"username\":\"admin\",\"password\":\"waiter\"}";




        for (int i = 0; i < iterations; i++) {

            threadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {

                    //Create realistic Users

                    ChromeDriver chromeDriver = new ChromeDriver();
                    chromeDriver.get("https://fake-it.ws/de/");


                    User user = new User();
                    user.username = chromeDriver.findElementByXPath(user.username).getText();
                    user.name = chromeDriver.findElementByXPath(user.name).getText();
                    user.adress = chromeDriver.findElementByXPath(user.adress).getText();
                    user.city = chromeDriver.findElementByXPath(user.city).getText();
                    user.postcode = chromeDriver.findElementByXPath(user.postcode).getText();
                    user.dateOfBirth = chromeDriver.findElementByXPath(user.dateOfBirth).getText();
                    user.gender = chromeDriver.findElementByXPath(user.gender).getText();
                    user.phone = chromeDriver.findElementByXPath(user.phone).getText();
                    user.email = chromeDriver.findElementByXPath(user.email).getText();
                    user.linkToMailBox = chromeDriver.findElementByXPath(user.linkToMailBox).getAttribute("href");

                    //Create Bookings


                    chromeDriver.close();
                    String token = bookTable(user);
                    randomBookDishes(token);


                }
            });
        }
    }

    public static void createUser(User user) {
        BasicOperations bo = new BasicOperations();
        String user_creation = "{\"username\":\"" + user.username + "\",\"email\":\"" + user.email + "\",\"userRoleId\":\"1\",\"password\":\"waiter\"}";

        try {
            bo.basicPost(user_creation, baseUrl + "usermanagement/v1/user");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotFound notFound) {
            notFound.printStackTrace();
        } catch (Different different) {
            different.printStackTrace();
        }

    }

    public static String bookTable(User user) {

        BasicOperations bo = new BasicOperations();

        long aDay = TimeUnit.DAYS.toMillis(1);
        long now = new Date().getTime();
        Date hundredYearsAgo = new Date(now + aDay * 365 * 1);
        Date nowD = new Date();
        Date randomDate = RestDateManager.randomBetween(nowD, hundredYearsAgo);
        int randomNum = ThreadLocalRandom.current().nextInt(1, 12 + 1);

        Request myApiRequest = new Request();
        myApiRequest.booking = new Booking();
        myApiRequest.booking.email = user.email;
        myApiRequest.booking.assistants = randomNum + "";
        myApiRequest.booking.bookingDate = RestDateManager.formatDate(randomDate);
        myApiRequest.booking.name = user.name;

        Gson gson = new Gson();
        String payload = gson.toJson(myApiRequest);

        // Post booking to DataBase //
        try {
            ga.codehub.entity.booking.Response resp = gson.fromJson(bo.basicPost(payload, baseUrl + "bookingmanagement/v1/booking"), ga.codehub.entity.booking.Response.class);
            return resp.bookingToken;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;

    }

    public static void randomBookDishes(String bookingID) {

        BasicOperations bo = new BasicOperations();

        try {

            String menuePayload = "{\"categories\":[],\"searchBy\":\"\",\"pageable\":{\"pageSize\":21,\"pageNumber\":0,\"sort\":[{\"property\":\"price\",\"direction\":\"DESC\"}]},\"maxPrice\":null,\"minLikes\":null}";

            Gson gson = new Gson();
            Response res = gson.fromJson(bo.basicPost(menuePayload, baseUrl + "dishmanagement/v1/dish/search"), Response.class);
            ga.codehub.entity.order.Request orderRequest = new ga.codehub.entity.order.Request();
            ArrayList<OrderLines> orders = new ArrayList<>();
            for (int i = 0; i < getRandomNumberInRange(1, 8); i++) {
                OrderLines orderLines = new OrderLines();
                orderLines.orderLine = new OrderLine();
                orderLines.orderLine.amount = getRandomNumberInRange(1, 10) + "";
                orderLines.orderLine.dishId = res.content[getRandomNumberInRange(0, res.content.length - 1)].dish.id;
                orderLines.orderLine.comment = "";
                orders.add(orderLines);
            }
            orderRequest.booking = new ga.codehub.entity.order.Booking();
            orderRequest.booking.bookingToken = bookingID;
            orderRequest.orderLines = orders.toArray(new OrderLines[orders.size()]);
            String orderPayload = gson.toJson(orderRequest);
            System.out.println(orderPayload);
            bo.basicPost(orderPayload, baseUrl + "ordermanagement/v1/order");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotFound notFound) {
            notFound.printStackTrace();
        } catch (Different different) {
            different.printStackTrace();
        }

    }

    private static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
}
