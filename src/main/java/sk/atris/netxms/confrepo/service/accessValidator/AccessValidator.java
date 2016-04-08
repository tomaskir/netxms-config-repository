package sk.atris.netxms.confrepo.service.accessValidator;

import sk.atris.netxms.confrepo.exceptions.AccessTokenInvalidException;
import sk.atris.netxms.confrepo.exceptions.AccessTokenNotLoadedException;
import sk.atris.netxms.confrepo.service.configuration.ConfigReader;

abstract class AccessValidator {
    final static String appReadOnlyAccessToken = ConfigReader.getInstance().getReadOnlyAccessToken();
    final static String appReadWriteAccessToken = ConfigReader.getInstance().getReadWriteAccessToken();

    abstract public void check(String providedAccessToken) throws AccessTokenInvalidException, AccessTokenNotLoadedException;
}
