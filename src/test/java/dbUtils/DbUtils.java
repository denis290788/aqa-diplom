package dbUtils;

import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.DriverManager;

import static org.junit.jupiter.api.Assertions.assertNull;

public class DbUtils {

    private static String url = System.getProperty("test.dburl");;
    private static String user = System.getProperty("test.dblogin");;
    private static String password = System.getProperty("test.dbpassword");

    @SneakyThrows
    public static PaymentEntity getEntryFromPaymentEntity() {
        val runner = new QueryRunner();
        try (val conn = DriverManager.getConnection(url, user, password)) {
            return runner.query(conn, "SELECT * FROM payment_entity ORDER BY created DESC", new BeanHandler<>(PaymentEntity.class));
        }
    }

    @SneakyThrows
    public static CreditRequestEntity getEntryFromCreditRequestEntity() {
        val runner = new QueryRunner();
        try (val conn = DriverManager.getConnection(url, user, password)) {
            return runner.query(conn, "SELECT * FROM credit_request_entity ORDER BY created DESC", new BeanHandler<>(CreditRequestEntity.class));
        }
    }

    @SneakyThrows
    public static OrderEntity getEntryFromOrderEntity() {
        val runner = new QueryRunner();
        try (val conn = DriverManager.getConnection(url, user, password)) {
            return runner.query(conn, "SELECT * FROM order_entity ORDER BY created DESC", new BeanHandler<>(OrderEntity.class));
        }
    }

    @SneakyThrows
    public static void checkEmptyPaymentEntity() {
        val runner = new QueryRunner();
        try (val conn = DriverManager.getConnection(url, user, password)) {
            val paymentEntity = runner.query(conn, "SELECT * FROM payment_entity;", new BeanHandler<>(PaymentEntity.class));
            assertNull(paymentEntity);
        }
    }

    @SneakyThrows
    public static void checkEmptyCreditRequestEntity() {
        val runner = new QueryRunner();
        try (val conn = DriverManager.getConnection(url, user, password)) {
            val creditRequestEntity = runner.query(conn, "SELECT * FROM credit_request_entity;", new BeanHandler<>(CreditRequestEntity.class));
            assertNull(creditRequestEntity);
        }
    }

    @SneakyThrows
    public static void checkEmptyOrderEntity() {
        val runner = new QueryRunner();
        try (val conn = DriverManager.getConnection(url, user, password)) {
            val orderEntity = runner.query(conn, "SELECT * FROM order_entity;", new BeanHandler<>(OrderEntity.class));
            assertNull(orderEntity);
        }
    }

    @SneakyThrows
    public static void cleanData() {
        val runner = new QueryRunner();
        try (val conn = DriverManager.getConnection(url, user, password)) {
            runner.update(conn, "DELETE FROM payment_entity;");
            runner.update(conn, "DELETE FROM credit_request_entity;");
            runner.update(conn, "DELETE FROM order_entity;");
        }
    }
}
