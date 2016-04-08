package sk.atris.netxms.confrepo.service.accessValidator;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sk.atris.netxms.confrepo.exceptions.AccessTokenInvalidException;
import sk.atris.netxms.confrepo.exceptions.AccessTokenNotLoadedException;

// TODO: add tests

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class WriteAccessValidator extends AccessValidator {
    @Getter
    private static final WriteAccessValidator instance = new WriteAccessValidator();

    @Override
    public void check(String providedAccessToken) throws AccessTokenInvalidException, AccessTokenNotLoadedException {
        if (appReadWriteAccessToken == null)
            throw new AccessTokenNotLoadedException();

        if (providedAccessToken == null)
            throw new AccessTokenInvalidException();

        if (!providedAccessToken.equals(appReadWriteAccessToken))
            throw new AccessTokenInvalidException();
    }
}
