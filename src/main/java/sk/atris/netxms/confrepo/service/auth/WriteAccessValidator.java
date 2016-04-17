package sk.atris.netxms.confrepo.service.auth;

import org.springframework.stereotype.Component;
import sk.atris.netxms.confrepo.exceptions.AccessTokenInvalidException;
import sk.atris.netxms.confrepo.exceptions.AccessTokenNotSetException;

@Component
public final class WriteAccessValidator extends AccessValidator {
    @Override
    public void check(String providedAccessToken) throws AccessTokenInvalidException, AccessTokenNotSetException {
        System.out.println("Configured token: " + appReadWriteAccessToken);
        System.out.println("Received token: " + providedAccessToken);

        if (appReadWriteAccessToken.equals(""))
            throw new AccessTokenNotSetException();

        if (providedAccessToken.equals(""))
            throw new AccessTokenInvalidException();

        if (!providedAccessToken.equals(appReadWriteAccessToken))
            throw new AccessTokenInvalidException();
    }
}
