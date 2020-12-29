package tests;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.DataHelper;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.*;
import pages.OfferPage;

import static dbUtils.DbUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class OfferTest {

    @SneakyThrows
    @AfterEach
    @DisplayName("Чистит базу данных перед каждым тестом")
    void cleanBase() {
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

    @SneakyThrows
    @Test
    @DisplayName("Покупка при валидных данных по карте APPROVED. " +
            "Покупка подтверждается, создаются PaymentEntity и OrderEntity")
    void shouldConfirmPaymentWithValidDataCardOne() {
        val offerPage = new OfferPage();
        val debitPage = offerPage.openDebitPage();
        debitPage.enterCardNumber(DataHelper.getApprovedCardNumber());
        debitPage.enterCardMonth(DataHelper.getRandomCardMonth());
        debitPage.enterCardYear(DataHelper.getValidCardYear());
        debitPage.enterOwnerName(DataHelper.getValidOwnerName());
        debitPage.enterCardCvv(DataHelper.getValidCardCvv());
        debitPage.continueButton();
        debitPage.checkSuccessNotification();
        debitPage.checkErrorNotificationHidden();
        assertEquals("APPROVED", getEntryFromPaymentEntity().getStatus());
        assertNotEquals("", getEntryFromOrderEntity().getId());
    }

    @SneakyThrows
    @Test
    @DisplayName("Запрос кредита при валидных данных по карте APPROVED. " +
            "Кредит подтверждается, создаются CreditRequestEntity и OrderEntity")
    void shouldConfirmCreditWithValidDataCardOne() {
        val offerPage = new OfferPage();
        val creditPage = offerPage.openCreditPage();
        creditPage.enterCardNumber(DataHelper.getApprovedCardNumber());
        creditPage.enterCardMonth(DataHelper.getRandomCardMonth());
        creditPage.enterCardYear(DataHelper.getValidCardYear());
        creditPage.enterOwnerName(DataHelper.getValidOwnerName());
        creditPage.enterCardCvv(DataHelper.getValidCardCvv());
        creditPage.continueButton();
        creditPage.checkSuccessNotification();
        creditPage.checkErrorNotificationHidden();
        assertEquals("APPROVED", getEntryFromCreditRequestEntity().getStatus());
        assertNotEquals("", getEntryFromOrderEntity().getId());
    }

    @SneakyThrows
    @Test
    @DisplayName("Покупка при валидных данных по карте DECLINED. " +
            "Появляется сообщение об отклонении операции, создается только PaymentEntity без OrderEntity")
    void shouldNotConfirmPaymentWithValidDataCardTwo() {
        val offerPage = new OfferPage();
        val debitPage = offerPage.openDebitPage();
        debitPage.enterCardNumber(DataHelper.getDeclinedCardNumber());
        debitPage.enterCardMonth(DataHelper.getRandomCardMonth());
        debitPage.enterCardYear(DataHelper.getValidCardYear());
        debitPage.enterOwnerName(DataHelper.getValidOwnerName());
        debitPage.enterCardCvv(DataHelper.getValidCardCvv());
        debitPage.continueButton();
        debitPage.checkErrorNotification();
        debitPage.checkSuccessNotificationHidden();
        assertEquals("DECLINED", getEntryFromPaymentEntity().getStatus());
        assertEquals("", getEntryFromOrderEntity().getId());
    }

    @SneakyThrows
    @Test
    @DisplayName("Запрос кредита при валидных данных по карте DECLINED. " +
            "Появляется сообщение об отклонении операции, создается только CreditRequestEntity без OrderEntity")
    void shouldNotConfirmCreditWithValidDataCardTwo() {
        val offerPage = new OfferPage();
        val creditPage = offerPage.openCreditPage();
        creditPage.enterCardNumber(DataHelper.getDeclinedCardNumber());
        creditPage.enterCardMonth(DataHelper.getRandomCardMonth());
        creditPage.enterCardYear(DataHelper.getValidCardYear());
        creditPage.enterOwnerName(DataHelper.getValidOwnerName());
        creditPage.enterCardCvv(DataHelper.getValidCardCvv());
        creditPage.continueButton();
        creditPage.checkErrorNotification();
        creditPage.checkSuccessNotificationHidden();
        assertEquals("DECLINED", getEntryFromCreditRequestEntity().getStatus());
        assertEquals("", getEntryFromOrderEntity().getId());
    }

    @SneakyThrows
    @Test
    @DisplayName("Покупка по несуществующей карте. " +
            "Появляется сообщение об отклонении операции, не создаются OrderEntity и PaymentEntity")
    void shouldNotSubmitPaymentWithNonexistentCard() {
        val offerPage = new OfferPage();
        val debitPage = offerPage.openDebitPage();
        debitPage.enterCardNumber(DataHelper.getNonexistentCardNumber());
        debitPage.enterCardMonth(DataHelper.getRandomCardMonth());
        debitPage.enterCardYear(DataHelper.getValidCardYear());
        debitPage.enterOwnerName(DataHelper.getValidOwnerName());
        debitPage.enterCardCvv(DataHelper.getValidCardCvv());
        debitPage.continueButton();
        debitPage.checkErrorNotification();
        debitPage.checkSuccessNotificationHidden();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @SneakyThrows
    @Test
    @DisplayName("Запрос кредита по несуществующей карте. " +
            "Появляется сообщение об отклонении операции, не создаются OrderEntity и CreditRequestEntity")
    void shouldNotSubmitCreditWithNonexistentCard() {
        val offerPage = new OfferPage();
        val creditPage = offerPage.openCreditPage();
        creditPage.enterCardNumber(DataHelper.getNonexistentCardNumber());
        creditPage.enterCardMonth(DataHelper.getRandomCardMonth());
        creditPage.enterCardYear(DataHelper.getValidCardYear());
        creditPage.enterOwnerName(DataHelper.getValidOwnerName());
        creditPage.enterCardCvv(DataHelper.getValidCardCvv());
        creditPage.continueButton();
        creditPage.checkErrorNotification();
        creditPage.checkSuccessNotificationHidden();
        checkEmptyCreditRequestEntity();
        checkEmptyOrderEntity();
    }

    @SneakyThrows
    @Test
    @DisplayName("Покупка по карте с недопустимым номером. " +
            "Появляется сообщение о неверно заполненном поле, не создаются OrderEntity и PaymentEntity")
    void shouldNotSubmitPaymentWithWrongCard() {
        val offerPage = new OfferPage();
        val debitPage = offerPage.openDebitPage();
        debitPage.enterCardNumber(DataHelper.getIlnvalidCardNumber());
        debitPage.enterCardMonth(DataHelper.getRandomCardMonth());
        debitPage.enterCardYear(DataHelper.getValidCardYear());
        debitPage.enterOwnerName(DataHelper.getValidOwnerName());
        debitPage.enterCardCvv(DataHelper.getValidCardCvv());
        debitPage.continueButton();
        debitPage.checkCardNumberError();
        debitPage.checkSuccessNotificationHidden();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @SneakyThrows
    @Test
    @DisplayName("Запрос кредита по карте с недопустимым номером. " +
            "Появляется сообщение о неверно заполненном поле, не создаются OrderEntity и CreditRequestEntity")
    void shouldNotSubmitCreditWithIlnvalidCard() {
        val offerPage = new OfferPage();
        val creditPage = offerPage.openCreditPage();
        creditPage.enterCardNumber(DataHelper.getIlnvalidCardNumber());
        creditPage.enterCardMonth(DataHelper.getRandomCardMonth());
        creditPage.enterCardYear(DataHelper.getValidCardYear());
        creditPage.enterOwnerName(DataHelper.getValidOwnerName());
        creditPage.enterCardCvv(DataHelper.getValidCardCvv());
        creditPage.continueButton();
        creditPage.checkCardNumberError();
        creditPage.checkSuccessNotificationHidden();
        checkEmptyCreditRequestEntity();
        checkEmptyOrderEntity();
    }

    @SneakyThrows
    @Test
    @DisplayName("Покупка с неверно заполненным полем имени. " +
            "Появляется сообщение о неверно заполненном поле, не создаются OrderEntity и PaymentEntity")
    void shouldNotSubmitPaymentWithInvalidOwnerName() {
        val offerPage = new OfferPage();
        val debitPage = offerPage.openDebitPage();
        debitPage.enterCardNumber(DataHelper.getApprovedCardNumber());
        debitPage.enterCardMonth(DataHelper.getRandomCardMonth());
        debitPage.enterCardYear(DataHelper.getValidCardYear());
        debitPage.enterOwnerName(DataHelper.getInvalidOwnerName());
        debitPage.enterCardCvv(DataHelper.getValidCardCvv());
        debitPage.continueButton();
        debitPage.checkErrorNotification();
        debitPage.checkSuccessNotificationHidden();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @SneakyThrows
    @Test
    @DisplayName("Запрос кредита по карте с несуществующим месяцем. " +
            "Появляется сообщение о неверно заполненном поле, не создаются OrderEntity и CreditRequestEntity")
    void shouldNotSubmitCreditWithWrongCardMonth() {
        val offerPage = new OfferPage();
        val creditPage = offerPage.openCreditPage();
        creditPage.enterCardNumber(DataHelper.getApprovedCardNumber());
        creditPage.enterCardMonth(DataHelper.getWrongCardMonth());
        creditPage.enterCardYear(DataHelper.getValidCardYear());
        creditPage.enterOwnerName(DataHelper.getValidOwnerName());
        creditPage.enterCardCvv(DataHelper.getValidCardCvv());
        creditPage.continueButton();
        creditPage.checkWrongMonthError();
        creditPage.checkSuccessNotificationHidden();
        checkEmptyCreditRequestEntity();
        checkEmptyOrderEntity();
    }

    @SneakyThrows
    @Test
    @DisplayName("Покупка с неверно заполненным полем месяца. " +
            "Появляется сообщение о неверно заполненном поле, не создаются OrderEntity и PaymentEntity")
    void shouldNotSubmitPaymentWithInvalidCardMonth() {
        val offerPage = new OfferPage();
        val debitPage = offerPage.openDebitPage();
        debitPage.enterCardNumber(DataHelper.getApprovedCardNumber());
        debitPage.enterCardMonth(DataHelper.getInvalidCardMonth());
        debitPage.enterCardYear(DataHelper.getValidCardYear());
        debitPage.enterOwnerName(DataHelper.getValidOwnerName());
        debitPage.enterCardCvv(DataHelper.getValidCardCvv());
        debitPage.continueButton();
        debitPage.checkEmptyMonthError();
        debitPage.checkSuccessNotificationHidden();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @SneakyThrows
    @Test
    @DisplayName("Запрос кредита по карте с неверно заполненным полем года. " +
            "Появляется сообщение о неверно заполненном поле, не создаются OrderEntity и CreditRequestEntity")
    void shouldNotSubmitCreditWithWrongCardYear() {
        val offerPage = new OfferPage();
        val creditPage = offerPage.openCreditPage();
        creditPage.enterCardNumber(DataHelper.getApprovedCardNumber());
        creditPage.enterCardMonth(DataHelper.getRandomCardMonth());
        creditPage.enterCardYear(DataHelper.getWrongCardYear());
        creditPage.enterOwnerName(DataHelper.getValidOwnerName());
        creditPage.enterCardCvv(DataHelper.getValidCardCvv());
        creditPage.continueButton();
        creditPage.checkEmptyYearError();
        creditPage.checkSuccessNotificationHidden();
        checkEmptyCreditRequestEntity();
        checkEmptyOrderEntity();
    }

    @SneakyThrows
    @Test
    @DisplayName("Покупка по карте с закончившимся сроком действия. " +
            "Появляется сообщение об истекшем сроке действия карты, не создаются OrderEntity и PaymentEntity")
    void shouldNotSubmitPaymentWithEarlyCardYear() {
        val offerPage = new OfferPage();
        val debitPage = offerPage.openDebitPage();
        debitPage.enterCardNumber(DataHelper.getApprovedCardNumber());
        debitPage.enterCardMonth(DataHelper.getRandomCardMonth());
        debitPage.enterCardYear(DataHelper.getEarlyCardYear());
        debitPage.enterOwnerName(DataHelper.getValidOwnerName());
        debitPage.enterCardCvv(DataHelper.getValidCardCvv());
        debitPage.continueButton();
        debitPage.checkEarlyYearError();
        debitPage.checkSuccessNotificationHidden();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @SneakyThrows
    @Test
    @DisplayName("Запрос кредита по карте с неверно заполненным полем CVV. " +
            "Появляется сообщение о неверно заполненном поле, не создаются OrderEntity и CreditRequestEntity")
    void shouldNotSubmitCreditWithInvalidCvv() {
        val offerPage = new OfferPage();
        val creditPage = offerPage.openCreditPage();
        creditPage.enterCardNumber(DataHelper.getApprovedCardNumber());
        creditPage.enterCardMonth(DataHelper.getRandomCardMonth());
        creditPage.enterCardYear(DataHelper.getValidCardYear());
        creditPage.enterOwnerName(DataHelper.getValidOwnerName());
        creditPage.enterCardCvv(DataHelper.getInvalidCardCvv());
        creditPage.continueButton();
        creditPage.checkWrongCvcError();
        creditPage.checkSuccessNotificationHidden();
        checkEmptyCreditRequestEntity();
        checkEmptyOrderEntity();
    }

    @SneakyThrows
    @Test
    @DisplayName("Покупка с пустым полем имени. " +
            "Появляется сообщение о неверно заполненном поле, не создаются OrderEntity и PaymentEntity")
    void shouldNotSubmitPaymentWithEmptyOwnerName() {
        val offerPage = new OfferPage();
        val debitPage = offerPage.openDebitPage();
        debitPage.enterCardNumber(DataHelper.getApprovedCardNumber());
        debitPage.enterCardMonth(DataHelper.getRandomCardMonth());
        debitPage.enterCardYear(DataHelper.getValidCardYear());
        debitPage.enterCardCvv(DataHelper.getValidCardCvv());
        debitPage.continueButton();
        debitPage.checkEmptyNameError();
        debitPage.checkSuccessNotificationHidden();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }
}
