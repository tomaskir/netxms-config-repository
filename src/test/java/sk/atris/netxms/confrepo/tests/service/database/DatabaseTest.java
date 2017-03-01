package sk.atris.netxms.confrepo.tests.service.database;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sk.atris.netxms.confrepo.exceptions.DatabaseException;
import sk.atris.netxms.confrepo.service.database.DbConnectionManager;
import sk.atris.netxms.confrepo.service.database.DbObjectHandler;
import sk.atris.netxms.confrepo.tests.MockedDatabase;

public class DatabaseTest {
    @Before
    public void environmentSetup() throws Exception {
        MockedDatabase.setup();
    }

    @After
    public void environmentCleanup() throws Exception {
        MockedDatabase.cleanup();
    }

    @Test
    public void testDatabase() throws DatabaseException {
        Revision object = new Revision("test", "test", 1);

        // make sure database works
        DbObjectHandler.getInstance().saveToDb(object);
        DbObjectHandler.getInstance().removeFromDb(object);

        // test shutdown
        DbConnectionManager.getInstance().shutdown();
    }
}
