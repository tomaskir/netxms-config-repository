package sk.atris.netxms.confrepo.tests;

import sk.atris.netxms.confrepo.service.database.DbConnectionManager;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class MockedDatabase {
    private static final DbConnectionManager dbConnectionManager = DbConnectionManager.getInstance();

    private static Object f1_originalValue;
    private static Object f2_originalValue;
    private static Object f3_originalValue;

    public static void setup() throws ReflectiveOperationException {
        Field f1 = dbConnectionManager.getClass().getDeclaredField("dbUrl");
        Field f2 = dbConnectionManager.getClass().getDeclaredField("dbUser");
        Field f3 = dbConnectionManager.getClass().getDeclaredField("dbPass");

        f1.setAccessible(true);
        f2.setAccessible(true);
        f3.setAccessible(true);

        // reset the final modifier
        Field modifiers = Field.class.getDeclaredField("modifiers");
        modifiers.setAccessible(true);
        modifiers.setInt(f1, f1.getModifiers() & ~Modifier.FINAL);
        modifiers.setInt(f2, f2.getModifiers() & ~Modifier.FINAL);
        modifiers.setInt(f3, f3.getModifiers() & ~Modifier.FINAL);

        // store original values
        f1_originalValue = f1.get(dbConnectionManager);
        f2_originalValue = f2.get(dbConnectionManager);
        f3_originalValue = f3.get(dbConnectionManager);

        // set the new value
        f1.set(dbConnectionManager, "jdbc:hsqldb:mem:MockedDatabase");
        f2.set(dbConnectionManager, "user");
        f3.set(dbConnectionManager, "pass");
    }

    public static void cleanup() throws ReflectiveOperationException {
        Field f1 = dbConnectionManager.getClass().getDeclaredField("dbUrl");
        Field f2 = dbConnectionManager.getClass().getDeclaredField("dbUser");
        Field f3 = dbConnectionManager.getClass().getDeclaredField("dbPass");

        f1.setAccessible(true);
        f2.setAccessible(true);
        f3.setAccessible(true);

        // return to the original values
        f1.set(dbConnectionManager, f1_originalValue);
        f2.set(dbConnectionManager, f2_originalValue);
        f3.set(dbConnectionManager, f3_originalValue);
    }
}
