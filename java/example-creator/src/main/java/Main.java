import com.google.gson.Gson;
import entity.User;
import ga.codehub.RestDateManager;
import ga.codehub.entity.booking.Booking;
import ga.codehub.entity.booking.Request;
import ga.codehub.tools.BasicOperations;
import ga.codehub.tools.exceptions.Different;
import ga.codehub.tools.exceptions.NotFound;
import org.apache.http.HttpResponse;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class Main {

    public static final int iterations = 500;


    public static void main(String[] args) {


        BasicOperations bo = new BasicOperations();

        /*
        For Windows users change chromedrive to chromedriver.exe
         */
        System.setProperty("webdriver.chrome.driver", "java/example-creator/driver/chromedriver");
        System.setProperty("webdriver.gecko.driver", "java/example-creator/driver/geckodriver/geckodriver");

        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
        //Nutzer Creation


        //login in a user;

        String json_body = "{\"username\":\"admin\",\"password\":\"waiter\"}";

        try {
            HttpResponse res = bo.basicPostWithHttpResponse(json_body, "http://localhost:8081" + "/mythaistar/login");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotFound notFound) {
            notFound.printStackTrace();
        } catch (Different different) {
            different.printStackTrace();
        }


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
                        System.out.println(bo.basicPost(payload, "http://localhost:8081" + "/mythaistar/services/rest/bookingmanagement/v1/booking"));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }


                    String user_creation = "{\"username\":\"" + user.username + "\",\"email\":\"" + user.email + "\",\"userRoleId\":\"1\",\"password\":\"waiter\"}";

                    try {
                        bo.basicPost(user_creation, "http://localhost:8081/mythaistar/services/rest/usermanagement/v1/user");
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (NotFound notFound) {
                        notFound.printStackTrace();
                    } catch (Different different) {
                        different.printStackTrace();
                    }

                    chromeDriver.close();
                }
            });
        }
    }
}
