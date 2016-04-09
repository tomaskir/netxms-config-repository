package sk.atris.netxms.confrepo.service.application;

import sk.atris.netxms.confrepo.exceptions.RepositoryInitializationException;
import sk.atris.netxms.confrepo.model.netxmsConfig.NetxmsConfigRepository;
import sk.atris.netxms.confrepo.service.database.DbConnectionManager;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ApplicationManager implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        // this will kick off the loading of all data from the database
        try {
            NetxmsConfigRepository.getInstance();
        } catch (RepositoryInitializationException ignored) {
            // we ignore the exception here, every user request will try to load the database connection
            // until the connection is successful (users will be send HTTP.500 on un-successful database connections)
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        DbConnectionManager.getInstance().shutdown();
    }
}
