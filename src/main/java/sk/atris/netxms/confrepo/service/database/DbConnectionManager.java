package sk.atris.netxms.confrepo.service.database;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import sk.atris.netxms.confrepo.exceptions.DatabaseException;
import sk.atris.netxms.confrepo.model.entities.*;
import sk.atris.netxms.confrepo.service.configuration.ConfigReader;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DbConnectionManager {
    @Getter
    private final static DbConnectionManager instance = new DbConnectionManager();

    private final ConfigReader appConfig = ConfigReader.getInstance();
    private SessionFactory sessionFactory;

    private final String dbAccessMode = "update";
    private final String dbDriverClass = "org.hsqldb.jdbcDriver";
    private final String dbDialect = "org.hibernate.dialect.HSQLDialect";
    private final String dbUrl = "jdbc:hsqldb:file:" + appConfig.getDatabasePath();
    private final String dbUser = appConfig.getDatabaseUser();
    private final String dbPass = appConfig.getDatabaseUser();
    private final String dbConnectionPoolSize = "2";
    private final String dbCacheProviderClass = "org.hibernate.cache.internal.NoCacheProvider";

    SessionFactory getSessionFactory() throws DatabaseException {
        if (sessionFactory == null)
            init();

        return sessionFactory;
    }

    @Synchronized
    private void init() throws DatabaseException {
        if (sessionFactory != null)
            return;

        log.debug("Initializing Hibernate configuration.");
        Configuration hibernateConfig = new Configuration()
                .setProperty("hibernate.connection.driver_class", dbDriverClass)
                .setProperty("hibernate.dialect", dbDialect)
                .setProperty("hibernate.connection.url", dbUrl)
                .setProperty("hibernate.connection.username", dbUser)
                .setProperty("hibernate.connection.password", dbPass)
                .setProperty("hibernate.connection.pool_size", dbConnectionPoolSize)
                .setProperty("hibernate.cache.provider_class", dbCacheProviderClass)
                .setProperty("hibernate.hbm2ddl.auto", dbAccessMode);

        hibernateConfig
                .addAnnotatedClass(ConfigItem.class)
                .addAnnotatedClass(DatabaseEntity.class)
                .addAnnotatedClass(DciSummaryTable.class)
                .addAnnotatedClass(EppRule.class)
                .addAnnotatedClass(Event.class)
                .addAnnotatedClass(ObjectTool.class)
                .addAnnotatedClass(Revision.class)
                .addAnnotatedClass(Script.class)
                .addAnnotatedClass(Template.class)
                .addAnnotatedClass(Trap.class);

        log.debug("Building the Hibernate session factory.");
        log.debug("Database URL: '{}'.", dbUrl);

        try {
            sessionFactory = hibernateConfig.buildSessionFactory();
        } catch (HibernateException e) {
            // get to the root exception
            Throwable ex = e;
            while (ex.getCause() != null)
                ex = ex.getCause();

            // log the root exception
            log.error("Failed to build Hibernate session factory - '{}'!", ex.getMessage());

            // throw an actual exception
            throw new DatabaseException(e);
        }
    }

    @Synchronized
    public void shutdown() {
        if (sessionFactory == null)
            return;

        log.debug("Closing the Hibernate session factory.");
        sessionFactory.close();

        sessionFactory = null;
    }
}
