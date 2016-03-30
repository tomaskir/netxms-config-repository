package sk.atris.netxms.confrepo.model.util;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccessToken {
    @Getter
    private static final AccessToken instance = new AccessToken();

    @Getter
    private final String token = "123";

    // FIXME: externalize the token to a property file
}
