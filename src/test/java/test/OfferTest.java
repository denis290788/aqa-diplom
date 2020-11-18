package test;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.DataHelper;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.val;
import org.junit.jupiter.api.*;
import pages.CreditPage;
import pages.DebitPage;
import pages.OfferPage;

import java.sql.SQLException;

import static com.codeborne.selenide.Selenide.open;
import static dbUtils.DbUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class OfferTest {

    @AfterEach
    @DisplayName("Чистит базу данных перед каждым тестом")
    void cleanBase() throws SQLException {
        cleanData();
    }

    @BeforeAll
    static void setupAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }


    static DebitPage openDebitPage() {
        val offerPage = open("http://localhost:8080/", OfferPage.class);
        offerPage.debitPage();
        return new DebitPage();
    }

    static CreditPage openCreditPage() {
        val offerPage = open("http://localhost:8080/", OfferPage.class);
        offerPage.creditPage();
        return new CreditPage();
    }

    @Test
    @DisplayName("Покупка при валидных данных по карте APPROVED. " +
            "Покупка подтверждается, создаются PaymentEntity и OrderEntity")
    void shouldConfirmPaymentWithValidDataCardOne() throws SQLException {
        val debitPage = openDebitPage();
        debitPage.enterCardNumber(DataHelper.CardNumber.getApprovedCardNumber());
        debitPage.enterCardMonth(DataHelper.CardMonth.getRandomCardMonth());
        debitPage.enterCardYear(DataHelper.CardYear.getValidCardYear());
        debitPage.enterOwnerName(DataHelper.OwnerName.getValidOwnerName());
        debitPage.enterCardCvv(DataHelper.CardCvv.getValidCardCvv());
        debitPage.continueButton();
        debitPage.checkSuccessNotification();
        debitPage.checkErrorNotificationHidden();
        val cardStatus = getDebitCardStatus();
        assertEquals("APPROVED", cardStatus);
        assertNotEquals("", getOrderEntityId(getPaymentEntityId("APPROVED")));
    }

    @Test
    @DisplayName("Запрос кредита при валидных данных по карте APPROVED. " +
            "Кредит подтверждается, создаются CreditRequestEntity и OrderEntity")
    void shouldConfirmCreditWithValidDataCardOne() throws SQLException {
        val creditPage = openCreditPage();
        creditPage.enterCardNumber(DataHelper.CardNumber.getApprovedCardNumber());
        creditPage.enterCardMonth(DataHelper.CardMonth.getRandomCardMonth());
        creditPage.enterCardYear(DataHelper.CardYear.getValidCardYear());
        creditPage.enterOwnerName(DataHelper.OwnerName.getValidOwnerName());
        creditPage.enterCardCvv(DataHelper.CardCvv.getValidCardCvv());
        creditPage.continueButton();
        creditPage.checkSuccessNotification();
        creditPage.checkErrorNotificationHidden();
        val cardStatus = getCreditCardStatus();
        assertEquals("APPROVED", cardStatus);
        assertNotEquals("", getOrderEntityId(getCreditRequestEntityId("APPROVED")));
    }

    @Test
    @DisplayName("Покупка при валидных данных по карте DECLINED. " +
            "Появляется сообщение об отклонении операции, создается только PaymentEntity без OrderEntity")
    void shouldNotConfirmPaymentWithValidDataCardTwo() throws SQLException {
        val debitPage = openDebitPage();
        debitPage.enterCardNumber(DataHelper.CardNumber.getDeclinedCardNumber());
        debitPage.enterCardMonth(DataHelper.CardMonth.getRandomCardMonth());
        debitPage.enterCardYear(DataHelper.CardYear.getValidCardYear());
        debitPage.enterOwnerName(DataHelper.OwnerName.getValidOwnerName());
        debitPage.enterCardCvv(DataHelper.CardCvv.getValidCardCvv());
        debitPage.continueButton();
        debitPage.checkErrorNotification();
        debitPage.checkSuccessNotificationHidden();
        val cardStatus = getDebitCardStatus();
        assertEquals("DECLINED", cardStatus);
        assertEquals("", getOrderEntityId(getPaymentEntityId("DECLINED")));
    }

    @Test
    @DisplayName("Запрос кредита при валидных данных по карте DECLINED. " +
            "Появляется сообщение об отклонении операции, создается только CreditRequestEntity без OrderEntity")
    void shouldNotConfirmCreditWithValidDataCardTwo() throws SQLException {
        val creditPage = openCreditPage();
        creditPage.enterCardNumber(DataHelper.CardNumber.getDeclinedCardNumber());
        creditPage.enterCardMonth(DataHelper.CardMonth.getRandomCardMonth());
        creditPage.enterCardYear(DataHelper.CardYear.getValidCardYear());
        creditPage.enterOwnerName(DataHelper.OwnerName.getValidOwnerName());
        creditPage.enterCardCvv(DataHelper.CardCvv.getValidCardCvv());
        creditPage.continueButton();
        creditPage.checkErrorNotification();
        creditPage.checkSuccessNotificationHidden();
        val cardStatus = getCreditCardStatus();
        assertEquals("DECLINED", cardStatus);
        assertEquals("", getOrderEntityId(getCreditRequestEntityId("DECLINED")));
    }

    @Test
    @DisplayName("Покупка по несуществующей карте. " +
            "Появляется сообщение об отклонении операции, не создаются OrderEntity и PaymentEntity")
    void shouldNotSubmitPaymentWithNonexistentCard() throws SQLException {
        val debitPage = openDebitPage();
        debitPage.enterCardNumber(DataHelper.CardNumber.getNonexistentCardNumber());
        debitPage.enterCardMonth(DataHelper.CardMonth.getRandomCardMonth());
        debitPage.enterCardYear(DataHelper.CardYear.getValidCardYear());
        debitPage.enterOwnerName(DataHelper.OwnerName.getValidOwnerName());
        debitPage.enterCardCvv(DataHelper.CardCvv.getValidCardCvv());
        debitPage.continueButton();
        debitPage.checkErrorNotification();
        debitPage.checkSuccessNotificationHidden();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @DisplayName("Запрос кредита по несуществующей карте. " +
            "Появляется сообщение об отклонении операции, не создаются OrderEntity и CreditRequestEntity")
    void shouldNotSubmitCreditWithNonexistentCard() throws SQLException {
        val creditPage = openCreditPage();
        creditPage.enterCardNumber(DataHelper.CardNumber.getNonexistentCardNumber());
        creditPage.enterCardMonth(DataHelper.CardMonth.getRandomCardMonth());
        creditPage.enterCardYear(DataHelper.CardYear.getValidCardYear());
        creditPage.enterOwnerName(DataHelper.OwnerName.getValidOwnerName());
        creditPage.enterCardCvv(DataHelper.CardCvv.getValidCardCvv());
        creditPage.continueButton();
        creditPage.checkErrorNotification();
        creditPage.checkSuccessNotificationHidden();
        checkEmptyCreditRequestEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @DisplayName("Покупка по карте с недопустимым номером. " +
            "Появляется сообщение о неверно заполненном поле, не создаются OrderEntity и PaymentEntity")
    void shouldNotSubmitPaymentWithWrongCard() throws SQLException {
        val debitPage = openDebitPage();
        debitPage.enterCardNumber(DataHelper.CardNumber.getIlnvalidCardNumber());
        debitPage.enterCardMonth(DataHelper.CardMonth.getRandomCardMonth());
        debitPage.enterCardYear(DataHelper.CardYear.getValidCardYear());
        debitPage.enterOwnerName(DataHelper.OwnerName.getValidOwnerName());
        debitPage.enterCardCvv(DataHelper.CardCvv.getValidCardCvv());
        debitPage.continueButton();
        debitPage.checkCardNumberError();
        debitPage.checkSuccessNotificationHidden();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @DisplayName("Запрос кредита по карте с недопустимым номером. " +
            "Появляется сообщение о неверно заполненном поле, не создаются OrderEntity и CreditRequestEntity")
    void shouldNotSubmitCreditWithIlnvalidCard() throws SQLException {
        val creditPage = openCreditPage();
        creditPage.enterCardNumber(DataHelper.CardNumber.getIlnvalidCardNumber());
        creditPage.enterCardMonth(DataHelper.CardMonth.getRandomCardMonth());
        creditPage.enterCardYear(DataHelper.CardYear.getValidCardYear());
        creditPage.enterOwnerName(DataHelper.OwnerName.getValidOwnerName());
        creditPage.enterCardCvv(DataHelper.CardCvv.getValidCardCvv());
        creditPage.continueButton();
        creditPage.checkCardNumberError();
        creditPage.checkSuccessNotificationHidden();
        checkEmptyCreditRequestEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @DisplayName("Покупка с неверно заполненным полем имени. " +
            "Появляется сообщение о неверно заполненном поле, не создаются OrderEntity и PaymentEntity")
    void shouldNotSubmitPaymentWithInvalidOwnerName() throws SQLException {
        val debitPage = openDebitPage();
        debitPage.enterCardNumber(DataHelper.CardNumber.getApprovedCardNumber());
        debitPage.enterCardMonth(DataHelper.CardMonth.getRandomCardMonth());
        debitPage.enterCardYear(DataHelper.CardYear.getValidCardYear());
        debitPage.enterOwnerName(DataHelper.OwnerName.getInvalidOwnerName());
        debitPage.enterCardCvv(DataHelper.CardCvv.getValidCardCvv());
        debitPage.continueButton();
        debitPage.checkErrorNotification();
        debitPage.checkSuccessNotificationHidden();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @DisplayName("Запрос кредита по карте с несуществующим месяцем. " +
            "Появляется сообщение о неверно заполненном поле, не создаются OrderEntity и CreditRequestEntity")
    void shouldNotSubmitCreditWithWrongCardMonth() throws SQLException {
        val creditPage = openCreditPage();
        creditPage.enterCardNumber(DataHelper.CardNumber.getApprovedCardNumber());
        creditPage.enterCardMonth(DataHelper.CardMonth.getWrongCardMonth());
        creditPage.enterCardYear(DataHelper.CardYear.getValidCardYear());
        creditPage.enterOwnerName(DataHelper.OwnerName.getValidOwnerName());
        creditPage.enterCardCvv(DataHelper.CardCvv.getValidCardCvv());
        creditPage.continueButton();
        creditPage.checkWrongMonthError();
        creditPage.checkSuccessNotificationHidden();
        checkEmptyCreditRequestEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @DisplayName("Покупка с неверно заполненным полем месяца. " +
            "Появляется сообщение о неверно заполненном поле, не создаются OrderEntity и PaymentEntity")
    void shouldNotSubmitPaymentWithInvalidCardMonth() throws SQLException {
        val debitPage = openDebitPage();
        debitPage.enterCardNumber(DataHelper.CardNumber.getApprovedCardNumber());
        debitPage.enterCardMonth(DataHelper.CardMonth.getInvalidCardMonth());
        debitPage.enterCardYear(DataHelper.CardYear.getValidCardYear());
        debitPage.enterOwnerName(DataHelper.OwnerName.getValidOwnerName());
        debitPage.enterCardCvv(DataHelper.CardCvv.getValidCardCvv());
        debitPage.continueButton();
        debitPage.checkEmptyMonthError();
        debitPage.checkSuccessNotificationHidden();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @DisplayName("Запрос кредита по карте с неверно заполненным полем года. " +
            "Появляется сообщение о неверно заполненном поле, не создаются OrderEntity и CreditRequestEntity")
    void shouldNotSubmitCreditWithWrongCardYear() throws SQLException {
        val creditPage = openCreditPage();
        creditPage.enterCardNumber(DataHelper.CardNumber.getApprovedCardNumber());
        creditPage.enterCardMonth(DataHelper.CardMonth.getRandomCardMonth());
        creditPage.enterCardYear(DataHelper.CardYear.getWrongCardYear());
        creditPage.enterOwnerName(DataHelper.OwnerName.getValidOwnerName());
        creditPage.enterCardCvv(DataHelper.CardCvv.getValidCardCvv());
        creditPage.continueButton();
        creditPage.checkEmptyYearError();
        creditPage.checkSuccessNotificationHidden();
        checkEmptyCreditRequestEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @DisplayName("Покупка по карте с закончившимся сроком действия. " +
            "Появляется сообщение об истекшем сроке действия карты, не создаются OrderEntity и PaymentEntity")
    void shouldNotSubmitPaymentWithEarlyCardYear() throws SQLException {
        val debitPage = openDebitPage();
        debitPage.enterCardNumber(DataHelper.CardNumber.getApprovedCardNumber());
        debitPage.enterCardMonth(DataHelper.CardMonth.getRandomCardMonth());
        debitPage.enterCardYear(DataHelper.CardYear.getEarlyCardYear());
        debitPage.enterOwnerName(DataHelper.OwnerName.getValidOwnerName());
        debitPage.enterCardCvv(DataHelper.CardCvv.getValidCardCvv());
        debitPage.continueButton();
        debitPage.checkEarlyYearError();
        debitPage.checkSuccessNotificationHidden();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @DisplayName("Запрос кредита по карте с неверно заполненным полем CVV. " +
            "Появляется сообщение о неверно заполненном поле, не создаются OrderEntity и CreditRequestEntity")
    void shouldNotSubmitCreditWithInvalidCvv() throws SQLException {
        val creditPage = openCreditPage();
        creditPage.enterCardNumber(DataHelper.CardNumber.getApprovedCardNumber());
        creditPage.enterCardMonth(DataHelper.CardMonth.getRandomCardMonth());
        creditPage.enterCardYear(DataHelper.CardYear.getValidCardYear());
        creditPage.enterOwnerName(DataHelper.OwnerName.getValidOwnerName());
        creditPage.enterCardCvv(DataHelper.CardCvv.getInvalidCardCvv());
        creditPage.continueButton();
        creditPage.checkWrongCvcError();
        creditPage.checkSuccessNotificationHidden();
        checkEmptyCreditRequestEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @DisplayName("Покупка с пустым полем имени. " +
            "Появляется сообщение о неверно заполненном поле, не создаются OrderEntity и PaymentEntity")
    void shouldNotSubmitPaymentWithEmptyOwnerName() throws SQLException {
        val debitPage = openDebitPage();
        debitPage.enterCardNumber(DataHelper.CardNumber.getApprovedCardNumber());
        debitPage.enterCardMonth(DataHelper.CardMonth.getRandomCardMonth());
        debitPage.enterCardYear(DataHelper.CardYear.getValidCardYear());
        debitPage.enterCardCvv(DataHelper.CardCvv.getValidCardCvv());
        debitPage.continueButton();
        debitPage.checkEmptyNameError();
        debitPage.checkSuccessNotificationHidden();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }
}
