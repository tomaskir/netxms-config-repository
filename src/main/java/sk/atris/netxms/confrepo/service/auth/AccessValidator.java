package sk.atris.netxms.confrepo.service.auth;

import org.springframework.beans.factory.annotation.Value;
import sk.atris.netxms.confrepo.exceptions.AccessTokenInvalidException;
import sk.atris.netxms.confrepo.exceptions.AccessTokenNotSetException;

abstract class AccessValidator {
    @Value("${access.token.ReadOnly}")
    String appReadOnlyAccessToken;

    @Value("${access.token.ReadWrite}")
    String appReadWriteAccessToken;

    public abstract void check(String providedAccessToken) throws AccessTokenInvalidException, AccessTokenNotSetException;
}
