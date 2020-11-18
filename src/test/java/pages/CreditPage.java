package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$$;

public class CreditPage extends OfferPage {
    private SelenideElement heading = $$(".heading").find(Condition.exactText("Кредит по данным карты"));

    public CreditPage() {
        heading.shouldBe(Condition.visible);
    }

}
