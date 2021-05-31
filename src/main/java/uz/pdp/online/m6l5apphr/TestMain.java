package uz.pdp.online.m6l5apphr;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TestMain {
    public static void main(String[] args) {

    long expireDate=(long)1000*60*60*24*10;
    Timestamp timestamp=new Timestamp(System.currentTimeMillis()+864000000);
    Timestamp timestamp1=new Timestamp(System.currentTimeMillis());
        System.out.println(timestamp.after(timestamp1));

        long time = timestamp1.getTime()+expireDate;
        Timestamp timestamp2=new Timestamp(time);
        System.out.println(timestamp2);

        LocalDateTime localDateTime=LocalDateTime.now();
        LocalDateTime localDateTime2=LocalDateTime.of(2021, 2,25,15,12,22,5);
        System.out.println(localDateTime2);
        System.out.println(localDateTime2.isAfter(timestamp2.toLocalDateTime()));

        Calendar calendar= GregorianCalendar.getInstance();
        Calendar calendar1=new GregorianCalendar();
        Date date =new Date();
        System.out.println(date);
        long year = localDateTime2.getLong(ChronoField.YEAR);
        long month = localDateTime2.getLong(ChronoField.MONTH_OF_YEAR);
        long day = localDateTime2.getLong(ChronoField.DAY_OF_MONTH);
        long hour = localDateTime2.getLong(ChronoField.HOUR_OF_DAY);
        long minute = localDateTime2.getLong(ChronoField.MINUTE_OF_DAY);
        long second = localDateTime2.getLong(ChronoField.SECOND_OF_MINUTE);
        long d=year+month+day+hour+minute+second;
        Timestamp timestampCustom=new Timestamp(d);


        java.sql.Timestamp t1=java.sql.Timestamp.valueOf("2021-05-29 23:39:45.177000");
            long time1 = t1.getTime();
        Timestamp timestampttt=new Timestamp(time1);


        java.sql.Timestamp t2=java.sql.Timestamp.valueOf("2021-05-29 23:39:58.308000");
        long time2 = t2.getTime();
        Timestamp timestamptttt=new Timestamp(time2);
        System.out.println(timestamptttt);

        System.out.println(time1);
        System.out.println(time2);





    }








}
