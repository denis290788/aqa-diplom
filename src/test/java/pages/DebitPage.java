package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import data.DataHelper;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DebitPage {
    private SelenideElement heading = $$(".heading").find(Condition.exactText("Оплата по карте"));
    private ElementsCollection formField = $$(".input__inner");
    private SelenideElement cardNumberField = formField.findBy(Condition.text("Номер карты")).$(".input__control");
    private SelenideElement cardMonthField = formField.findBy(Condition.text("Месяц")).$(".input__control");
    private SelenideElement cardYearField = formField.findBy(Condition.text("Год")).$(".input__control");
    private SelenideElement cardOwnerField = formField.findBy(Condition.text("Владелец")).$(".input__control");
    private SelenideElement cardCvcField = formField.findBy(Condition.text("CVC/CVV")).$(".input__control");
    private SelenideElement continueButton = $$("button").find(Condition.exactText("Продолжить"));
    private SelenideElement successNotification = $(".notification_status_ok");
    private SelenideElement errorNotification = $(".notification_status_error");

    public DebitPage() {
        heading.shouldBe(Condition.visible);
    }

    public void enterCardNumber(String cardNumber) {
        cardNumberField.setValue(cardNumber);
    }

    public void enterCardMonth(String cardMonth) {
        cardMonthField.setValue(cardMonth);
    }

    public void enterCardYear(String cardYear) {
        cardYearField.setValue(cardYear);
    }

    public void enterOwnerName(String ownerName) {
        cardOwnerField.setValue(ownerName);
    }

    public void enterCardCvv(String cardCvv) {
        cardCvcField.setValue(cardCvv);
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
