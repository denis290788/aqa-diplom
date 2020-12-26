package data;

import com.github.javafaker.Faker;

import java.util.Random;

public class DataHelper {

    private DataHelper() {
    }

    public static String getApprovedCardNumber() {
        return "4444 4444 4444 4441";
    }
    public static String getDeclinedCardNumber() {
        return "4444 4444 4444 4442";
    }
    public static String getNonexistentCardNumber() {
        return "4444 4444 4444 4443";
    }
    public static String getIlnvalidCardNumber() {
        return "WWWW dii9 #%#%  889";
    }

    public static String getValidOwnerName() {
        Faker faker = new Faker();
        return faker.name().fullName();
    }

    public static String getInvalidOwnerName() {
        return "111 &fkdjhf";
    }

    public static String getRandomCardMonth() {
        Random random = new Random();
        int month = 1 + random.nextInt(12);
        return String.format("%02d", month);
    }
    public static String getWrongCardMonth() {
        return "13";
    }
    public static String getInvalidCardMonth() {
        return "&&";
    }

    public static String getValidCardYear() {
        Faker faker = new Faker();
        int year = faker.number().numberBetween(21,24);
        return Integer.toString(year);
    }

    public static String getEarlyCardYear() {
        return "19";
    }

    public static String getWrongCardYear() {
        return "FF";
    }

    public static String getValidCardCvv() {
        Random random = new Random();
        int intCardCvv = 100 + random.nextInt(1000 - 100);
        String cardCvv = Integer.toString(intCardCvv);
        return cardCvv;
    }

    public static String getInvalidCardCvv() {
        return "Y7$";
    }
}

