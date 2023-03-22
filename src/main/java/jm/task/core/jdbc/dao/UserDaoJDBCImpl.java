package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

public class UserDaoJDBCImpl implements UserDao {

    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (id INT AUTO_INCREMENT,`name` VARCHAR(30),lastName VARCHAR(30),age INT,PRIMARY KEY (id))";
        try (Connection connection = Util.getConnection()) {
            executeDDL(sql, connection);
        } catch (SQLException e) {
            //log something
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public void dropUsersTable() {
        String sql = "DROP TABLE IF EXISTS users";
        try (Connection connection = Util.getConnection()) {
            executeDDL(sql, connection);
        } catch (SQLException e) {
            //log something
            throw new RuntimeException(e);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String sql = "INSERT INTO users (name, lastName, age) VALUES(?, ?, ?)";
        try (Connection connection = Util.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                preparedStatement.setString(1, name);
                preparedStatement.setString(2, lastName);
                preparedStatement.setInt(3, age);

                preparedStatement.executeUpdate();
                connection.commit();
                System.out.printf("User с именем - %s добавлен в базу данных\n", name);
            } catch (SQLException e) {
                //log something
                connection.rollback();
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            //log something
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void removeUserById(long id) {
        String sql = "DELETE FROM users " + "WHERE id=?";
        try (Connection connection = Util.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, (int) id);
                preparedStatement.executeUpdate();
                connection.commit();
            } catch (SQLException e) {
                //log something
                connection.rollback();
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            //log something
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public List<User> getAllUsers() {
        String sql = "SELECT * FROM users";
        List<User> users = new ArrayList<>();
        try (Connection connection = Util.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    User user = new User();
                    user.setId(resultSet.getLong("id"));
                    user.setName(resultSet.getString("name"));
                    user.setLastName(resultSet.getString("lastName"));
                    user.setAge(resultSet.getByte("age"));
                    users.add(user);
                }
                connection.commit();
                System.out.println(users);
                return users;
            } catch (SQLException e) {
                //log something
                connection.rollback();
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            //log something
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void cleanUsersTable() {
        String sql = "DELETE * FROM users";
        try (Connection connection = Util.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(sql);
                connection.commit();
            } catch (SQLException e) {
                //log something
                connection.rollback();
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            //log something
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void executeDDL(String sqlDDL, Connection connection) throws SQLException {
        connection.setAutoCommit(true);
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sqlDDL);
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            //log something
            connection.setAutoCommit(false);
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
