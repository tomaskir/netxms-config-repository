package sk.atris.netxms.confrepo.tests.service.accessValidator;

import org.junit.Test;
import sk.atris.netxms.confrepo.exceptions.AccessTokenInvalidException;
import sk.atris.netxms.confrepo.exceptions.AccessTokenNotLoadedException;
import sk.atris.netxms.confrepo.service.accessValidator.ReadAccessValidator;
import sk.atris.netxms.confrepo.service.accessValidator.WriteAccessValidator;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class AccessValidatorTest {
    private final static String readOnlyToken = "ReadOnlyToken";
    private final static String readWriteToken = "ReadWriteToken";

    @Test(expected = AccessTokenInvalidException.class)
    public void testReadValidator_InvalidToken() throws AccessTokenInvalidException, AccessTokenNotLoadedException, ReflectiveOperationException {
        setTokens(readOnlyToken, readWriteToken);
        ReadAccessValidator.getInstance().check("");
    }

    @Test(expected = AccessTokenNotLoadedException.class)
    public void testReadValidator_TokenNotLoaded() throws AccessTokenInvalidException, AccessTokenNotLoadedException, ReflectiveOperationException {
        setTokens(null, null);
        ReadAccessValidator.getInstance().check("");
    }

    @Test
    public void testReadValidator_ValidToken() throws AccessTokenInvalidException, AccessTokenNotLoadedException, ReflectiveOperationException {
        setTokens(readOnlyToken, readWriteToken);
        ReadAccessValidator.getInstance().check(readOnlyToken);
    }

    @Test(expected = AccessTokenInvalidException.class)
    public void testWriteValidator_InvalidToken() throws AccessTokenInvalidException, AccessTokenNotLoadedException, ReflectiveOperationException {
        setTokens(readOnlyToken, readWriteToken);
        WriteAccessValidator.getInstance().check("");
    }

    @Test(expected = AccessTokenNotLoadedException.class)
    public void testWriteValidator_TokenNotLoaded() throws AccessTokenInvalidException, AccessTokenNotLoadedException, ReflectiveOperationException {
        setTokens(null, null);
        WriteAccessValidator.getInstance().check("");
    }

    @Test
    public void testWriteValidator_ValidToken() throws AccessTokenInvalidException, AccessTokenNotLoadedException, ReflectiveOperationException {
        setTokens(readOnlyToken, readWriteToken);
        WriteAccessValidator.getInstance().check(readWriteToken);
    }

    /**
     * Normally the AccessValidator classes read the application access tokens directly from the configuration file.
     * For tests, this is of course un-wanted behaviour, so this method can set the tokens using reflection to arbitrary values.
     *
     * @param readOnlyToken  application readOnly access token
     * @param readWriteToken application readWrite access token
     * @throws ReflectiveOperationException
     */
    private static void setTokens(String readOnlyToken, String readWriteToken) throws ReflectiveOperationException {
        Class clazz = Class.forName("sk.atris.netxms.confrepo.service.accessValidator.AccessValidator");

        Field f1 = clazz.getDeclaredField("appReadOnlyAccessToken");
        Field f2 = clazz.getDeclaredField("appReadWriteAccessToken");

        f1.setAccessible(true);
        f2.setAccessible(true);

        // reset the final modifier
        Field modifiers = Field.class.getDeclaredField("modifiers");
        modifiers.setAccessible(true);
        modifiers.setInt(f1, f1.getModifiers() & ~Modifier.FINAL);
        modifiers.setInt(f2, f2.getModifiers() & ~Modifier.FINAL);

        // set the new value
        f1.set(clazz, readOnlyToken);
        f2.set(clazz, readWriteToken);
    }
}
