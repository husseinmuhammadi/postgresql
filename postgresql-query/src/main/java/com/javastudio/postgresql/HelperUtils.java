package com.javastudio.postgresql;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public class HelperUtils {
    private static DateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

    public static Date now() {
        Date date = Date.from(LocalDateTime.of(2000, 1, 1, 13, 40).atZone(ZoneOffset.systemDefault()).toInstant());
        return date;
    }

    public static String format(Date date) {
        return sdf.format(date);
    }

    public static String getSubscriberPointCardNo(String mobileNo) {
        String cardNo = "983255" + mobileNo.substring(2);
        cardNo = cardNo + getLuhnCheckDigit(cardNo);
        return cardNo;
    }

    private static byte getLuhnCheckDigit(String cardNo) {
        int sum = 0;
        for (int i = cardNo.length() - 1; i >= 0; i--) {
            if (i % 2 == 0) {
                int d = (cardNo.charAt(i) - 48) * 2;
                if (d > 9) d = d - 9;
                sum += d;
            } else
                sum += (cardNo.charAt(i) - 48);
        }
        return (byte) ((sum * 9) % 10);
    }
}
