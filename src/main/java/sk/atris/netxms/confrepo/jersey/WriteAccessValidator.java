package sk.atris.netxms.confrepo.jersey;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sk.atris.netxms.confrepo.exceptions.AccessTokenInvalidException;
import sk.atris.netxms.confrepo.exceptions.AccessTokenNotLoadedException;
import sk.atris.netxms.confrepo.service.token.ReadWriteAccessToken;

// TODO: add tests

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class WriteAccessValidator {
    @Getter
    private static final WriteAccessValidator instance = new WriteAccessValidator();

    private final String applicationReadWriteAccessToken = ReadWriteAccessToken.getInstance().getToken();

    final void check(String providedAccessToken) throws AccessTokenInvalidException, AccessTokenNotLoadedException {
        if (applicationReadWriteAccessToken == null)
            throw new AccessTokenNotLoadedException();

        if (providedAccessToken == null)
            throw new AccessTokenInvalidException();

        if (!providedAccessToken.equals(applicationReadWriteAccessToken))
            throw new AccessTokenInvalidException();
    }
}
