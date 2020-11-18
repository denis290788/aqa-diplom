package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import data.DataHelper;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class OfferPage {

    private SelenideElement debitButton = $$("button").find(Condition.exactText("Купить"));
    private SelenideElement creditButton = $$("button").find(Condition.exactText("Купить в кредит"));
    private ElementsCollection formField = $$(".input__inner");
    private SelenideElement cardNumberField = formField.findBy(Condition.text("Номер карты")).$(".input__control");
    private SelenideElement cardMonthField = formField.findBy(Condition.text("Месяц")).$(".input__control");
    private SelenideElement cardYearField = formField.findBy(Condition.text("Год")).$(".input__control");
    private SelenideElement cardOwnerField = formField.findBy(Condition.text("Владелец")).$(".input__control");
    private SelenideElement cardCvcField = formField.findBy(Condition.text("CVC/CVV")).$(".input__control");
    private SelenideElement continueButton = $$("button").find(Condition.exactText("Продолжить"));
    private SelenideElement successNotification = $(".notification_status_ok");
    private SelenideElement errorNotification = $(".notification_status_error");

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
        successNotification.waitUntil(Condition.visible, 30000);
    }

    public void checkErrorNotification() {
        errorNotification.waitUntil(Condition.visible, 30000);
    }

    public void checkSuccessNotificationHidden() {
        successNotification.waitUntil(Condition.hidden, 15000);
    }

    public void checkErrorNotificationHidden() {
        errorNotification.waitUntil(Condition.hidden, 15000);
    }

    public void checkCardNumberError() {
        formField.findBy(Condition.text("Номер карты")).$(".input__sub").shouldHave(Condition.exactText("Неверный формат"));
    }

    public void checkWrongMonthError() {
        formField.findBy(Condition.text("Месяц")).$(".input__sub").shouldHave(Condition.exactText("Неверно указан срок действия карты"));
    }

    public void checkEarlyYearError() {
        formField.findBy(Condition.text("Год")).$(".input__sub").shouldHave(Condition.exactText("Истёк срок действия карты"));
    }

    public void checkWrongCvcError() {
        formField.findBy(Condition.text("CVC/CVV")).$(".input__sub").shouldHave(Condition.exactText("Неверный формат"));
    }

    public void checkEmptyNameError() {
        formField.findBy(Condition.text("Владелец")).$(".input__sub").shouldHave(Condition.exactText("Поле обязательно для заполнения"));
    }

    public void checkEmptyMonthError() {
        formField.findBy(Condition.text("Месяц")).$(".input__sub").shouldHave(Condition.exactText("Неверный формат"));
    }

    public void checkEmptyYearError() {
        formField.findBy(Condition.text("Год")).$(".input__sub").shouldHave(Condition.exactText("Неверный формат"));
    }
}
