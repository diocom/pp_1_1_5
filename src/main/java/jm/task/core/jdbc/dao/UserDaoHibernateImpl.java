package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private SessionFactory sessionFactory = Util.getSessionFactory();

    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {
        String query = "CREATE TABLE IF NOT EXISTS `test`.`Users` " +
                "(" +
                "`id` INT NOT NULL AUTO_INCREMENT, " +
                "`name` VARCHAR(45) NOT NULL, " +
                "`lastName` VARCHAR(45) NOT NULL," +
                "`age` TINYINT ZEROFILL NOT NULL, " +
                "PRIMARY KEY (`id`)) ENGINE INNODB" ;
        try (Session session = sessionFactory.getCurrentSession()){
            session.createSQLQuery(query).executeUpdate();
            session.getTransaction().commit();
        } finally {
            sessionFactory.close();
        }
    }

    @Override
    public void dropUsersTable() {
        String query = "DROP TABLE IF EXISTS `test`.`Users` ";

        try(Session session = sessionFactory.getCurrentSession();) {
            session.beginTransaction();
            session.createSQLQuery(query).executeUpdate();
            session.getTransaction().commit();

        } finally {
            sessionFactory.close();
        }

    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try(Session session = sessionFactory.getCurrentSession()) {
                session.beginTransaction();
                User user = new User(name, lastName, age);
                session.save(user);
                session.getTransaction().commit();

        } catch (RuntimeException e) {
            //session.getTransaction().rollback();
            throw new RuntimeException();
        } finally {
            sessionFactory.close();
        }
    }

    @Override
    public void removeUserById(long id) {
        try(Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            User user = session.get(User.class, id);
            session.delete(user);
            session.getTransaction().commit();

        } catch (RuntimeException e) {
            //session.getTransaction().rollback();
            throw new RuntimeException();
        } finally {
            sessionFactory.close();
        }
    }

    @Override
    public List<User> getAllUsers() {
        try(Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            List<User> listUsers = session.createQuery("FROM User").getResultList();
            session.getTransaction().commit();
            return listUsers;
        } finally {
            sessionFactory.close();
        }
    }

    @Override
    public void cleanUsersTable() {
        try(Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            session.createQuery("DELETE FROM User");
            session.getTransaction().commit();
        } finally {
            sessionFactory.close();
        }
    }
}