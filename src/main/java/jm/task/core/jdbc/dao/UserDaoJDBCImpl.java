package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import java.sql.Connection;
import jm.task.core.jdbc.util.Util;
import org.hibernate.SessionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private final SessionFactory sessionFactory = Util.getSessionFactory();
    private final Connection conn = Util.getConnection();
    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {

        String query = "CREATE TABLE IF NOT EXISTS `test`.`Users` " +
                "(" +
                "`id` INT NOT NULL AUTO_INCREMENT, " +
                "`name` VARCHAR(45) NOT NULL, " +
                "`lastName` VARCHAR(45) NOT NULL," +
                "`age` TINYINT ZEROFILL NOT NULL, " +
                "PRIMARY KEY (`id`)) ENGINE INNODB" ;

        try (Connection conn = Util.getConnection();
            Statement stmnt = conn.createStatement()) {

            stmnt.executeUpdate(query);
            conn.commit();

        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }

    public void dropUsersTable() {
        String query = "DROP TABLE IF EXISTS `test`.`Users` ";

        try(Connection conn = Util.getConnection();
            Statement stmnt = conn.createStatement()) {
            System.out.println(stmnt.executeUpdate(query));
            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String query = "INSERT INTO Users (name, lastName, age) VALUES (?, ?, ?)";

        try(Connection conn = Util.getConnection();
            PreparedStatement ps = conn.prepareStatement(query)) {
            //ps.setLong(1, usr.getId());
            ps.setString(1, name);
            ps.setString(2, lastName);
            ps.setByte(3, age);
            ps.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }

    public void removeUserById(long id) {
        String query = "DELETE FROM Users WHERE id = VALUES (id)";

        try(Connection conn = Util.getConnection();
            PreparedStatement ps = conn.prepareStatement(query)) {
            ps.executeUpdate(query);
            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }

    public List<User> getAllUsers() {
        List<User> lsu = new ArrayList<>();

        String query = "SELECT id, name, lastName, age FROM Users";
        try(Connection conn = Util.getConnection();
            PreparedStatement ps = conn.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                User usr = new User();
                usr.setId(rs.getLong("id"));
                usr.setName(rs.getNString("name"));
                usr.setLastName(rs.getNString("lastName"));
                usr.setAge(rs.getByte("age"));
                lsu.add(usr);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lsu;
    }

    public void cleanUsersTable() {
        String query = "DELETE FROM Users";

        try(Connection conn = Util.getConnection();
            PreparedStatement ps = conn.prepareStatement(query)) {
            ps.executeUpdate(query);
            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }
}
