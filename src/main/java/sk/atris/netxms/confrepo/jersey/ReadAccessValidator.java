package sk.atris.netxms.confrepo.jersey;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sk.atris.netxms.confrepo.exceptions.AccessTokenInvalidException;
import sk.atris.netxms.confrepo.model.token.ReadOnlyAccessToken;
import sk.atris.netxms.confrepo.model.token.ReadWriteAccessToken;

// TODO: add tests

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class ReadAccessValidator {
    @Getter
    private static final ReadAccessValidator instance = new ReadAccessValidator();

    private final String applicationReadOnlyAccessToken = ReadOnlyAccessToken.getInstance().getToken();
    private final String applicationReadWriteAccessToken = ReadWriteAccessToken.getInstance().getToken();

    final void check(String providedAccessToken) throws AccessTokenInvalidException {
            if (providedAccessToken == null || (applicationReadOnlyAccessToken == null && applicationReadWriteAccessToken == null))
            throw new AccessTokenInvalidException();

        if (!(providedAccessToken.equals(applicationReadOnlyAccessToken) || providedAccessToken.equals(applicationReadWriteAccessToken)))
            throw new AccessTokenInvalidException();
    }
}
