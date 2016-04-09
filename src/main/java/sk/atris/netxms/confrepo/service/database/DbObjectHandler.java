package sk.atris.netxms.confrepo.service.database;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import sk.atris.netxms.confrepo.exceptions.DatabaseException;

import java.util.List;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DbObjectHandler {
    @Getter
    private static final DbObjectHandler instance = new DbObjectHandler();

    public void saveToDb(Object obj) throws DatabaseException {
        log.debug("Saving a '{}' object to database.", obj.getClass().getSimpleName());
        Session sqlSession = getDbSession();

        try {
            log.trace("Beginning a transaction.");
            sqlSession.beginTransaction();

            log.trace("Saving object to database.");
            sqlSession.save(obj);

            log.trace("Committing the transaction.");
            sqlSession.getTransaction().commit();
        } catch (HibernateException e) {
            log.error("SQL transaction exception '{}'!", e.getMessage());
            rollbackDbCommit(sqlSession);
            throw new DatabaseException(e);
        } finally {
            closeDbSession(sqlSession);
        }
    }

    public void removeFromDb(Object obj) throws DatabaseException {
        log.debug("Removing a '{}' object from database.", obj.getClass().getSimpleName());
        Session sqlSession = getDbSession();

        try {
            log.trace("Beginning a transaction.");
            sqlSession.beginTransaction();

            log.trace("Removing object from database.");
            sqlSession.delete(obj);

            log.trace("Committing the transaction.");
            sqlSession.getTransaction().commit();
        } catch (HibernateException e) {
            log.error("SQL transaction exception '{}'!", e.getMessage());
            rollbackDbCommit(sqlSession);
            throw new DatabaseException(e);
        } finally {
            closeDbSession(sqlSession);
        }
    }

    public List getObjects(Class clazz) throws DatabaseException {
        log.debug("Getting all '{}' objects from database.", clazz.getSimpleName());
        Session sqlSession = getDbSession();

        try {
            log.trace("Getting '{}' class objects from database.", clazz.getSimpleName());
            return sqlSession.createCriteria(clazz).list();
        } catch (HibernateException e) {
            log.error("SQL object retrieval error - '{}'!", e.getMessage());
            throw new DatabaseException(e);
        } finally {
            closeDbSession(sqlSession);
        }
    }

    private Session getDbSession() throws DatabaseException {
        log.trace("Getting the Hibernate session factory.");
        SessionFactory sessionFactory = DbConnectionManager.getInstance().getSessionFactory();

        log.debug("Opening a database sessions.");
        return sessionFactory.openSession();
    }

    private void rollbackDbCommit(Session sqlSession) {
        log.trace("Rollback-ing the transaction.");

        try {
            sqlSession.getTransaction().rollback();
        } catch (HibernateException e) {
            log.error("Transaction rollback failed - '{}'!", e.getMessage());
        }
    }

    private void closeDbSession(Session sqlSession) {
        log.debug("Closing a database session.");

        try {
            sqlSession.flush();
        } catch (HibernateException e) {
            log.error("Failed to flush a database session - '{}'!", e.getMessage());
        }

        try {
            sqlSession.close();
        } catch (HibernateException e) {
            log.error("Failed to close a database session - '{}'!", e.getMessage());
        }
    }
}
