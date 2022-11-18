package data;

import com.github.javafaker.Faker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DataGenerator {

    private static final Faker faker = new Faker();
    private static final String approvedCardNumber = "4444 4444 4444 4441";
    private static final String declinedCardNumber = "4444 4444 4444 4442";

    private static String generateMonth(int i) {
        return LocalDate.now().plusMonths(i).format(DateTimeFormatter.ofPattern("MM"));
    }

    private static String generateYear(int i) {
        return LocalDate.now().plusYears(i).format(DateTimeFormatter.ofPattern("yy"));
    }

    private static String generateHolder() {
        return faker.name().fullName().toUpperCase();
    }

    private static String generateCVC() {
        return faker.numerify("###");
    }

    public static Card getValidApprovedCard() {
        return new Card(
                approvedCardNumber,
                generateMonth(0),
                generateYear(0),
                generateHolder(),
                generateCVC()
        );
    }

    public static Card getValidDeclinedCard() {
        return new Card(
                declinedCardNumber,
                generateMonth(0),
                generateYear(0),
                generateHolder(),
                generateCVC()
        );
    }

    public static Card getCardNotInDB() {
        return new Card(
                "1111 1111 1111 1111",
                generateMonth(0),
                generateYear(0),
                generateHolder(),
                generateCVC()
        );
    }

    public static Card getPreviousMonthCard() {
        return new Card(
                approvedCardNumber,
                generateMonth(-1),
                generateYear(0),
                generateHolder(),
                generateCVC()
        );
    }

    public static Card getEmptyYearCard() {
        return new Card(
                approvedCardNumber,
                generateMonth(0),
                "",
                generateHolder(),
                generateCVC()
        );
    }

    public static Card getPreviousYearCard() {
        return new Card(
                approvedCardNumber,
                generateMonth(0),
                generateYear(-1),
                generateHolder(),
                generateCVC()
        );
    }

    public static Card getFarFutureYearCard() {
        return new Card(
                approvedCardNumber,
                generateMonth(0),
                generateYear(6),
                generateHolder(),
                generateCVC()
        );
    }
}