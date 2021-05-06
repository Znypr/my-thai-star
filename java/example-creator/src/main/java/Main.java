import com.google.gson.Gson;
import entity.User;
import ga.codehub.RestDateManager;
import ga.codehub.entity.booking.Booking;
import ga.codehub.entity.booking.Request;
import ga.codehub.tools.BasicOperations;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;


public class Main {

    public static final int iterations = 5;


    public static void main(String[] args) {

        System.setProperty("webdriver.chrome.driver", "java/example-creator/driver/chromedriver");
        System.setProperty("webdriver.gecko.driver", "java/example-creator/driver/geckodriver/geckodriver");


        ChromeDriver chromeDriver = new ChromeDriver();


        //Nutzer Creation

        for (int i = 0; i < iterations; i++) {
            chromeDriver.get("https://fake-it.ws/de/");
            User user = new User();
            user.name = chromeDriver.findElementByXPath(user.name).getText();
            user.adress = chromeDriver.findElementByXPath(user.adress).getText();
            user.city = chromeDriver.findElementByXPath(user.city).getText();
            user.postcode = chromeDriver.findElementByXPath(user.postcode).getText();
            user.dateOfBirth = chromeDriver.findElementByXPath(user.dateOfBirth).getText();
            user.gender = chromeDriver.findElementByXPath(user.gender).getText();
            user.phone = chromeDriver.findElementByXPath(user.phone).getText();
            user.email = chromeDriver.findElementByXPath(user.email).getText();
            user.linkToMailBox = chromeDriver.findElementByXPath(user.linkToMailBox).getAttribute("href");
            System.out.println(user);


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


            BasicOperations bo = new BasicOperations();
            Gson gson = new Gson();
            String payload = gson.toJson(myApiRequest);

            try {
                System.out.println(bo.basicPost(payload, "http://localhost:8081" + "/mythaistar/services/rest/bookingmanagement/v1/booking"));
            } catch (Exception ex) {
                ex.printStackTrace();
            }


            System.out.println(randomDate);
            System.out.println(randomNum);


        }


        chromeDriver.close();


    }


}
