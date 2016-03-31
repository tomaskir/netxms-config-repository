package sk.atris.netxms.confrepo.jersey;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sk.atris.netxms.confrepo.exceptions.AccessTokenInvalidException;
import sk.atris.netxms.confrepo.exceptions.AccessTokenNotLoadedException;
import sk.atris.netxms.confrepo.service.token.ReadOnlyAccessToken;
import sk.atris.netxms.confrepo.service.token.ReadWriteAccessToken;

// TODO: add tests

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class ReadAccessValidator {
    @Getter
    private static final ReadAccessValidator instance = new ReadAccessValidator();

    private final String applicationReadOnlyAccessToken = ReadOnlyAccessToken.getInstance().getToken();
    private final String applicationReadWriteAccessToken = ReadWriteAccessToken.getInstance().getToken();

    final void check(String providedAccessToken) throws AccessTokenInvalidException, AccessTokenNotLoadedException {
        if (applicationReadOnlyAccessToken == null && applicationReadWriteAccessToken == null)
            throw new AccessTokenNotLoadedException();

        if (providedAccessToken == null)
            throw new AccessTokenInvalidException();

        if (applicationReadOnlyAccessToken != null)
            if (providedAccessToken.equals(applicationReadOnlyAccessToken))
                return;

        if (applicationReadWriteAccessToken != null)
            if (providedAccessToken.equals(applicationReadWriteAccessToken))
                return;

        throw new AccessTokenInvalidException();
    }
}
