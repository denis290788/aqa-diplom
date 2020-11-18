package data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.util.Random;

public class DataHelper {
    private DataHelper() {
    }

    @Value
    public static class CardNumber {
        private String cardNumber;

        public static CardNumber getApprovedCardNumber() {
            return new CardNumber("4444 4444 4444 4441");
        }
        public static CardNumber getDeclinedCardNumber() {
            return new CardNumber("4444 4444 4444 4442");
        }
        public static CardNumber getNonexistentCardNumber() {
            return new CardNumber("4444 4444 4444 4443");
        }
        public static CardNumber getIlnvalidCardNumber() {
            return new CardNumber("WWWW dii9 #%#%  889");
        }
    }

    @Value
    public static class OwnerName {
        private String ownerName;

        public static OwnerName getValidOwnerName() {
            Faker faker = new Faker();
            String ownerName = faker.name().fullName();
            return new OwnerName(ownerName);
        }

        public static OwnerName getInvalidOwnerName() {
            return new OwnerName("111 &fkdjhf");
        }
    }

    @Value
    public static class CardMonth {
        private String cardMonth;

        public static CardMonth getRandomCardMonth() {
            Random random = new Random();
            int month = 1 + random.nextInt(12);
            return new CardMonth(String.format("%02d", month));
        }

        public static CardMonth getWrongCardMonth() {
            return new CardMonth("13");
        }

        public static CardMonth getInvalidCardMonth() {
            return new CardMonth("&&");
        }
    }

    @Value
    public static class CardYear {
        private String cardYear;

        public static CardYear getValidCardYear() {
            Faker faker = new Faker();
            int year = faker.number().numberBetween(21,24);
            return new CardYear(Integer.toString(year));
        }

        public static CardYear getEarlyCardYear() {
            return new CardYear("19");
        }

        public static CardYear getWrongCardYear() {
            return new CardYear("FF");
        }
    }

    @Value
    public static class CardCvv {
        private String cardCvv;

        public static CardCvv getValidCardCvv() {
            Random random = new Random();
            int intCardCvv = 100 + random.nextInt(1000 - 100);
            String cardCvv = Integer.toString(intCardCvv);
            return new CardCvv(cardCvv);
        }

        public static CardCvv getInvalidCardCvv() {
            return new CardCvv("Y7$");
        }
    }
}
