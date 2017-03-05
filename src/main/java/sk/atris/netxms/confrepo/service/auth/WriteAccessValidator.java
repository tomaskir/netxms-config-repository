package sk.atris.netxms.confrepo.service.auth;

import org.springframework.stereotype.Component;
import sk.atris.netxms.confrepo.exceptions.AccessTokenInvalidException;
import sk.atris.netxms.confrepo.exceptions.AccessTokenNotSetException;

@Component
public final class WriteAccessValidator extends AccessValidator {

    @Override
    public void check(String providedAccessToken) throws AccessTokenInvalidException, AccessTokenNotSetException {
        if (appReadWriteAccessToken.equals(""))
            throw new AccessTokenNotSetException();

        if (providedAccessToken.equals(""))
            throw new AccessTokenInvalidException();

        if (!providedAccessToken.equals(appReadWriteAccessToken))
            throw new AccessTokenInvalidException();
    }

}
