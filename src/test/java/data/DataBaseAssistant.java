package data;

import entities.CreditRequestEntity;
import entities.OrderEntity;
import entities.PaymentEntity;
import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import java.sql.Connection;
import java.sql.DriverManager;

public class DataBaseAssistant {

    private static QueryRunner queryRunner = new QueryRunner();
    private static Connection connection = getConnection();

    @SneakyThrows
    private static Connection getConnection() {
        return DriverManager.getConnection(System.getProperty("db.url"), "app", "pass");
    }

    @SneakyThrows
    public static void cleanData() {
        queryRunner.update(connection, "DELETE FROM order_entity;");
        queryRunner.update(connection, "DELETE FROM payment_entity;");
        queryRunner.update(connection, "DELETE FROM credit_request_entity;");
    }

    @SneakyThrows
    public static CreditRequestEntity getCreditRequestEntity() {
        return queryRunner.query(connection, "SELECT * FROM credit_request_entity LIMIT 1;", new BeanHandler<>(CreditRequestEntity.class));
    }

    @SneakyThrows
    public static PaymentEntity getPaymentEntity() {
        return queryRunner.query(connection, "SELECT * FROM payment_entity LIMIT 1;", new BeanHandler<>(PaymentEntity.class));
    }

    @SneakyThrows
    public static OrderEntity getOrderEntity() {
        return queryRunner.query(connection, "SELECT * FROM order_entity LIMIT 1;", new BeanHandler<>(OrderEntity.class));
    }
}