package sk.atris.netxms.confrepo.service.auth;

import org.springframework.stereotype.Component;
import sk.atris.netxms.confrepo.exceptions.AccessTokenInvalidException;
import sk.atris.netxms.confrepo.exceptions.AccessTokenNotSetException;

@Component
public final class ReadAccessValidator extends AccessValidator {
    @Override
    public void check(String providedAccessToken) throws AccessTokenInvalidException, AccessTokenNotSetException {
        if (appReadOnlyAccessToken.equals("") && appReadWriteAccessToken.equals(""))
            throw new AccessTokenNotSetException();

        if (providedAccessToken.equals(""))
            throw new AccessTokenInvalidException();

        if (!appReadOnlyAccessToken.equals(""))
            if (providedAccessToken.equals(appReadOnlyAccessToken))
                return;

        if (!appReadWriteAccessToken.equals(""))
            if (providedAccessToken.equals(appReadWriteAccessToken))
                return;

        throw new AccessTokenInvalidException();
    }
}
