package sk.atris.netxms.confrepo.jersey;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sk.atris.netxms.confrepo.exceptions.AccessTokenInvalidException;
import sk.atris.netxms.confrepo.model.util.AccessToken;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class CheckAccessToken {
    @Getter
    private static final CheckAccessToken instance = new CheckAccessToken();

    private AccessToken applicationAccessToken = AccessToken.getInstance();

    final void check(String providedAccessToken) throws AccessTokenInvalidException {
        if (providedAccessToken == null)
            throw new AccessTokenInvalidException();

        if (!providedAccessToken.equals(applicationAccessToken.getToken()))
            throw new AccessTokenInvalidException();
    }
}
