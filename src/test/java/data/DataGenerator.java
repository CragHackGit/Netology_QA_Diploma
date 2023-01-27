package data;

import com.github.javafaker.Faker;
import entities.Card;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DataGenerator {

    private static final Faker faker = new Faker();
    private static final String approvedCardNumber = "4444 4444 4444 4441";
    private static final String declinedCardNumber = "4444 4444 4444 4442";

    private static String generateMonth(int shiftedMonths) {
        return LocalDate.now().plusMonths(shiftedMonths).format(DateTimeFormatter.ofPattern("MM"));
    }

    private static String generateYear(int shiftedYears) {
        return LocalDate.now().plusYears(shiftedYears).format(DateTimeFormatter.ofPattern("yy"));
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
        var currentMonth = Integer.parseInt(generateMonth(0));
        var currentYear = Integer.parseInt(generateYear(0));
        if (currentMonth == 1) {
            currentMonth = 12;
            currentYear = currentYear - 1;
        } else currentMonth = currentMonth - 1;
        String m = String.valueOf(currentMonth);
        String y = String.valueOf(currentYear);
        return new Card(
                approvedCardNumber,
                m,
                y,
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

    public static Card getEmptyCardNumberCard() {
        return new Card(
                "",
                generateMonth(0),
                generateYear(0),
                generateHolder(),
                generateCVC()
        );
    }
    public static Card getOneDigitCardNumberCard() {
        return new Card(
                faker.numerify("#"),
                generateMonth(0),
                generateYear(0),
                generateHolder(),
                generateCVC()
        );
    }

    public static Card getFifteenDigitsCardNumberCard() {
        return new Card(
                faker.numerify("#### #### #### ###"),
                generateMonth(0),
                generateYear(0),
                generateHolder(),
                generateCVC()
        );
    }
    public static Card getEmptyMonthCard() {
        return new Card(
                approvedCardNumber,
                "",
                generateYear(0),
                generateHolder(),
                generateCVC()
        );
    }

    public static Card getMonthGreaterTwelveCard() {
        return new Card(
                approvedCardNumber,
                "77",
                generateYear(0),
                generateHolder(),
                generateCVC()
        );
    }

    public static Card getMonthLessOneCard() {
        return new Card(
                approvedCardNumber,
                "00",
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

    public static Card getYearOfZerosCard() {
        return new Card(
                approvedCardNumber,
                generateMonth(0),
                "00",
                generateHolder(),
                generateCVC()
        );
    }
    public static Card getEmptyCvvCard() {
        return new Card(
                approvedCardNumber,
                generateMonth(0),
                generateYear(0),
                generateHolder(),
                ""
        );
    }

    public static Card getOneDigitCvvCard() {
        return new Card(
                approvedCardNumber,
                generateMonth(0),
                generateYear(0),
                generateHolder(),
                faker.numerify("#")
        );
    }

    public static Card getTwoDigitsCvvCard() {
        return new Card(
                approvedCardNumber,
                generateMonth(0),
                generateYear(0),
                generateHolder(),
                faker.numerify("##")
        );
    }

    public static Card getCvvOfZerosCard() {
        return new Card(
                approvedCardNumber,
                generateMonth(0),
                generateYear(0),
                generateHolder(),
                "000"
        );
    }
    public static Card getCapitalOwnerCard() {
        return new Card(
                approvedCardNumber,
                generateMonth(0),
                generateYear(0),
                generateHolder().toUpperCase(),
                generateCVC()
        );
    }

    public static Card getOwnerWithDashCard() {
        return new Card(
                approvedCardNumber,
                generateMonth(0),
                generateYear(0),
                "KROLIKOV-VOLKOV OLEG",
                generateCVC()
        );
    }

    public static Card getEmptyOwnerCard() {
        return new Card(
                approvedCardNumber,
                generateMonth(0),
                generateYear(0),
                "",
                generateCVC()
        );
    }

    public static Card getOneWordOwnerCard() {
        return new Card(
                approvedCardNumber,
                generateMonth(0),
                generateYear(0),
                "PETROVICH",
                generateCVC()
        );
    }

    public static Card getCyrillicOwnerCard() {
        return new Card(
                approvedCardNumber,
                generateMonth(0),
                generateYear(0),
                "САЛОЕДОВ АНТОН",
                generateCVC()
        );
    }

    public static Card getSymbolsOwnerCard() {
        return new Card(
                approvedCardNumber,
                generateMonth(0),
                generateYear(0),
                "@|**$^ #!<~][",
                generateCVC()
        );
    }
}