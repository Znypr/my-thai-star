package ga.codehub;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class RestDateManager {


    public static String getDate(String string) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd hh:mm");

        Date date = sdf.parse(string);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, -2);

        return formatDate(calendar.getTime());


    }

    public static String formatDate(Date date) {


        SimpleDateFormat sdfo = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        return sdfo.format(date);

    }

    public static Date randomBetween(Date startInclusive, Date endExclusive) {
        long startMillis = startInclusive.getTime();
        long endMillis = endExclusive.getTime();
        long randomMillisSinceEpoch = ThreadLocalRandom
                .current()
                .nextLong(startMillis, endMillis);

        return new Date(randomMillisSinceEpoch);
    }
}
