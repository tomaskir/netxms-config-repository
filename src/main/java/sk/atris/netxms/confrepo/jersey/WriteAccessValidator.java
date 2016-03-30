package sk.atris.netxms.confrepo.jersey;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sk.atris.netxms.confrepo.exceptions.AccessTokenInvalidException;
import sk.atris.netxms.confrepo.model.token.ReadWriteAccessToken;

// TODO: add tests

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class WriteAccessValidator {
    @Getter
    private static final WriteAccessValidator instance = new WriteAccessValidator();

    private String applicationReadWriteAccessToken = ReadWriteAccessToken.getInstance().getToken();

    final void check(String providedAccessToken) throws AccessTokenInvalidException {
        if (providedAccessToken == null || applicationReadWriteAccessToken == null)
            throw new AccessTokenInvalidException();

        if (!providedAccessToken.equals(applicationReadWriteAccessToken))
            throw new AccessTokenInvalidException();
    }
}
