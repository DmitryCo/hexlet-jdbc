package io.hexlet;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Application {
    public static void main(String[] args) throws SQLException {
        // Соединение с базой данных тоже нужно отслеживать
        try (var conn = DriverManager.getConnection("jdbc:h2:mem:hexlet_test")) {

            var sql = "CREATE TABLE users (id BIGINT PRIMARY KEY AUTO_INCREMENT, username VARCHAR(255), phone VARCHAR(255))";
            try (var statement = conn.createStatement()) {
                statement.execute(sql);
            }

            var sql2 = "INSERT INTO users (username, phone) VALUES ('tommy', '123456789')";
            try (var statement2 = conn.createStatement()) {
                statement2.executeUpdate(sql2);
            }

            var sql3 = "INSERT INTO users (username, phone) VALUES (?, ?)";
            try (var preparedStatement = conn.prepareStatement(sql3, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, "Sarah");
                preparedStatement.setString(2, "333333333");
                preparedStatement.addBatch();

                preparedStatement.setString(1, "Joe");
                preparedStatement.setString(2, "444444444");
                preparedStatement.addBatch();

                preparedStatement.executeBatch();
            }

            var sql4 = "SELECT * FROM users";
            try (var statement3 = conn.createStatement()) {
                var resultSet = statement3.executeQuery(sql4);
                while (resultSet.next()) {
                    System.out.println(resultSet.getString("username"));
                    System.out.println(resultSet.getString("phone"));
                }
            }

            var sql5 = "DELETE FROM users (username, phone) WHERE username = ?";
            try (var preparedStatement = conn.prepareStatement(sql5)) {
                preparedStatement.setString(1, "Joe");
                int rowsDeleted = preparedStatement.executeUpdate();
                System.out.println(rowsDeleted + " user(s) deleted.");
            }
        }
    }
}
