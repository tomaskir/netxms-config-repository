package sk.atris.netxms.confrepo.service.accessValidator;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sk.atris.netxms.confrepo.exceptions.AccessTokenInvalidException;
import sk.atris.netxms.confrepo.exceptions.AccessTokenNotLoadedException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReadAccessValidator extends AccessValidator {
    @Getter
    private static final ReadAccessValidator instance = new ReadAccessValidator();

    @Override
    public void check(String providedAccessToken) throws AccessTokenInvalidException, AccessTokenNotLoadedException {
        if (appReadOnlyAccessToken == null && appReadWriteAccessToken == null)
            throw new AccessTokenNotLoadedException();

        if (providedAccessToken == null)
            throw new AccessTokenInvalidException();

        if (appReadOnlyAccessToken != null)
            if (providedAccessToken.equals(appReadOnlyAccessToken))
                return;

        if (appReadWriteAccessToken != null)
            if (providedAccessToken.equals(appReadWriteAccessToken))
                return;

        throw new AccessTokenInvalidException();
    }
}
