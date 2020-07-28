package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import data.DataHelper;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class OfferPage {
    private SelenideElement debitButton = $$("button").find(Condition.exactText("Купить"));
    private SelenideElement creditButton = $$("button").find(Condition.exactText("Купить в кредит"));
    private SelenideElement cardNumberField = $("[placeholder=\"0000 0000 0000 0000\"]");
    private SelenideElement cardMonthField = $("[placeholder=\"08\"]");
    private SelenideElement cardYearField = $("[placeholder=\"22\"]");
    private SelenideElement cardOwnerField = $("div:nth-child(3) > span > span:nth-child(1) > span > span > span.input__box > input");
    private SelenideElement cardCvcField = $("placeholder=\"999\"");
    private SelenideElement continueButton = $$("button").find(Condition.exactText("Продолжить"));
    private SelenideElement successNotification = $(withText("Операция одобрена Банком."));
    private SelenideElement errorNotification = $(withText("Ошибка! Банк отказал в проведении операции."));

    public DebitPage debitPage() {
        debitButton.click();
        return new DebitPage();
    }

    public CreditPage creditPage() {
        creditButton.click();
        return new CreditPage();
    }

    public void enterCardNumber(DataHelper.CardNumber cardNumber) {
        cardNumberField.setValue(cardNumber.getCardNumber());
    }

    public void enterCardMonth(DataHelper.CardMonth cardMonth) {
        cardMonthField.setValue(cardMonth.getCardMonth());
    }

    public void enterCardYear(DataHelper.CardYear cardYear) {
        cardYearField.setValue(cardYear.getCardYear());
    }

    public void enterOwnerName(DataHelper.OwnerName ownerName) {
        cardOwnerField.setValue(ownerName.getOwnerName());
    }

    public void enterCardCvv(DataHelper.CardCvv cardCvv) {
        cardCvcField.setValue(cardCvv.getCardCvv());
    }

    public void continueButton() {
        continueButton.click();
    }

    public void checkSuccessNotification() {
        successNotification.waitUntil(Condition.visible, 15000);
    }

    public void checkErrorNotification() {
        errorNotification.waitUntil(Condition.visible, 15000);
    }

    public void checkCardNumberError() {
        cardNumberField.$(".input__sub").shouldHave(Condition.exactText("Неверный формат"));
    }

    public void checkWrongMonthError() {
        cardMonthField.$(".input__sub").shouldHave(Condition.exactText("Неверно указан срок действия карты"));
    }

    public void checkEarlyYearError() {
        cardYearField.$(".input__sub").shouldHave(Condition.exactText("Истёк срок действия карты"));
    }

    public void checkWrongCvcError() {
        cardCvcField.$(".input__sub").shouldHave(Condition.exactText("Неверный формат"));
    }

    public void checkEmptyNameError() {
        cardOwnerField.$(".input__sub").shouldHave(Condition.exactText("Поле обязательно для заполнения"));
    }

    public void checkEmptyCardError() {
        cardNumberField.$(".input__sub").shouldHave(Condition.exactText("Неверный формат"));
    }

    public void cleanData() {
        cardNumberField.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        cardMonthField.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        cardYearField.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        cardOwnerField.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        cardCvcField.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
    }
}
