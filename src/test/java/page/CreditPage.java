package page;

import data.Card;

public class CreditPage extends Form {
    public void pay(Card card) {
        cardNumberField.setValue(card.getNumber());
        monthField.setValue(card.getMonth());
        yearField.setValue(card.getYear());
        holderField.setValue(card.getHolder());
        cvvField.setValue(card.getCvc());
        continueButton.click();
    }
}