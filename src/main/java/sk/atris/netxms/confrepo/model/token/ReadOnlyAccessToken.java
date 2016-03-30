package sk.atris.netxms.confrepo.model.token;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReadOnlyAccessToken extends AccessToken {
    @Getter
    private static final ReadOnlyAccessToken instance = new ReadOnlyAccessToken();

    // FIXME: externalize the token to a property file
}
