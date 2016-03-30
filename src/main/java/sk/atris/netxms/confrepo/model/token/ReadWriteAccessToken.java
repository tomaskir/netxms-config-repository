package sk.atris.netxms.confrepo.model.token;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReadWriteAccessToken extends AccessToken {
    @Getter
    private static final ReadWriteAccessToken instance = new ReadWriteAccessToken();

    // FIXME: externalize the token to a property file
}
