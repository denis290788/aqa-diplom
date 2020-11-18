package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$$;

public class DebitPage extends OfferPage {
    private SelenideElement heading = $$(".heading").find(Condition.exactText("Оплата по карте"));

    public DebitPage() {
        heading.shouldBe(Condition.visible);
    }

}
