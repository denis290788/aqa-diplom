package dbUtils;

import lombok.val;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNull;

public class DbUtils {

    private static String url = "jdbc:mysql://192.168.99.100:3306/app";
    private static String user = "app";
    private static String password = "pass";

    public static String getDebitCardStatus() throws SQLException {
        val runner = new QueryRunner();
        try (val conn = DriverManager.getConnection(url, user, password)) {
            val debitCardStatus = runner.query(conn, "SELECT * FROM payment_entity", new BeanHandler<>(PaymentEntity.class));
            return debitCardStatus.getStatus();
        }
    }

    public static String getCreditCardStatus() throws SQLException {
        val runner = new QueryRunner();
        try (val conn = DriverManager.getConnection(url, user, password)) {
            val creditCardStatus = runner.query(conn, "SELECT * FROM credit_request_entity", new BeanHandler<>(CreditRequestEntity.class));
            return creditCardStatus.getStatus();
        }
    }

    public static String getPaymentEntityId(String status) throws SQLException {
        val runner = new QueryRunner();
        try (val conn = DriverManager.getConnection(url, user, password)) {
            val paymentEntity = runner.query(conn, "SELECT * FROM payment_entity WHERE status='" + status + "';", new BeanHandler<>(PaymentEntity.class));
            return paymentEntity.getTransaction_id();
        }
    }

    public static String getCreditRequestEntityId(String status) throws SQLException {
        val runner = new QueryRunner();
        try (val conn = DriverManager.getConnection(url, user, password)) {
            val creditRequestEntity = runner.query(conn, "SELECT * FROM credit_request_entity WHERE status='" + status + "';", new BeanHandler<>(CreditRequestEntity.class));
            return creditRequestEntity.getBank_id();
        }
    }

    public static String getOrderEntityId(String paymentEntityId) throws SQLException {
        val runner = new QueryRunner();
        try (val conn = DriverManager.getConnection(url, user, password)) {
            val orderEntity = runner.query(conn, "SELECT * FROM order_entity WHERE payment_id='" + paymentEntityId + "';", new BeanHandler<>(OrderEntity.class));
            return orderEntity.getId();
        }
    }

    public static void checkEmptyPaymentEntity() throws SQLException {
        val runner = new QueryRunner();
        try (val conn = DriverManager.getConnection(url, user, password)) {
            val paymentEntity = runner.query(conn, "SELECT * FROM payment_entity;", new BeanHandler<>(PaymentEntity.class));
            assertNull(paymentEntity);
        }
    }

    public static void checkEmptyCreditRequestEntity() throws SQLException {
        val runner = new QueryRunner();
        try (val conn = DriverManager.getConnection(url, user, password)) {
            val creditRequestEntity = runner.query(conn, "SELECT * FROM credit_request_entity;", new BeanHandler<>(CreditRequestEntity.class));
            assertNull(creditRequestEntity);
        }
    }

    public static void checkEmptyOrderEntity() throws SQLException {
        val runner = new QueryRunner();
        try (val conn = DriverManager.getConnection(url, user, password)) {
            val orderEntity = runner.query(conn, "SELECT * FROM order_entity;", new BeanHandler<>(OrderEntity.class));
            assertNull(orderEntity);
        }
    }

    public static void cleanData() throws SQLException {
        val runner = new QueryRunner();
        try (val conn = DriverManager.getConnection(url, user, password)) {
            runner.update(conn, "DELETE FROM payment_entity;");
            runner.update(conn, "DELETE FROM credit_request_entity;");
            runner.update(conn, "DELETE FROM order_entity;");
        }
    }
}
