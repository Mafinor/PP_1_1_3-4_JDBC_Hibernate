package jm.task.core.jdbc;

import jm.task.core.jdbc.dao.UserDao;
import jm.task.core.jdbc.dao.UserDaoJDBCImpl;

public class Main {
    public static void main(String[] args) {
        // реализуйте алгоритм здесь
        UserDao userDao = new UserDaoJDBCImpl();

        userDao.createUsersTable();

        userDao.saveUser("username1", "lastname1", (byte)10);
        userDao.saveUser("username2", "lastname2", (byte)20);
        userDao.saveUser("username3", "lastname3", (byte)30);
        userDao.saveUser("username4", "lastname4", (byte)40);

        userDao.removeUserById(1);
        userDao.getAllUsers();
        userDao.cleanUsersTable();
        userDao.dropUsersTable();
    }
}
