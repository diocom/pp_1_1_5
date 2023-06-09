package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
private Session sessionEx = null;
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
        try (Session session = Util.getSessionFactory().openSession()) {
            sessionEx = session;
            session.beginTransaction();
            session.createSQLQuery(query).executeUpdate();
            session.getTransaction().commit();
        } catch (RuntimeException e) {
            sessionEx.getTransaction().rollback();
            throw new RuntimeException();
        }
    }

    @Override
    public void dropUsersTable() {
        String sqlQuery = "DROP TABLE IF EXISTS `test`.`Users` ";

        try(Session session = Util.getSessionFactory().openSession()) {
            sessionEx = session;
            session.beginTransaction();
            session.createSQLQuery(sqlQuery).executeUpdate();
            session.getTransaction().commit();
        } catch (RuntimeException e) {
            sessionEx.getTransaction().rollback();
            throw new RuntimeException();
        }

    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try(Session session = Util.getSessionFactory().openSession()) {
                sessionEx = session;
                session.beginTransaction();
                User user = new User(name, lastName, age);
                session.save(user);
                session.getTransaction().commit();

        } catch (RuntimeException e) {
            sessionEx.getTransaction().rollback();
            throw new RuntimeException();
        }
    }

    @Override
    public void removeUserById(long id) {
        try(Session session = Util.getSessionFactory().openSession()) {
            sessionEx = session;
            session.beginTransaction();
            User user = session.get(User.class, id);
            session.delete(user);
            session.getTransaction().commit();

        } catch (RuntimeException e) {
            sessionEx.getTransaction().rollback();
            throw new RuntimeException();
        }
    }

    @Override
    public List<User> getAllUsers() {
        try(Session session = Util.getSessionFactory().openSession()) {
            return session.createQuery("SELECT u FROM User u", User.class).getResultList();
        } catch (RuntimeException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public void cleanUsersTable() {
        try(Session session = Util.getSessionFactory().openSession()) {
            sessionEx = session;
            session.beginTransaction();
            session.createQuery("DELETE FROM User").executeUpdate();
            session.getTransaction().commit();
        } catch (RuntimeException e) {
            sessionEx.getTransaction().rollback();
            throw new RuntimeException();
        }
    }
}
