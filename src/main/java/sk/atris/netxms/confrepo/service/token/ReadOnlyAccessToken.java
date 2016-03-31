package sk.atris.netxms.confrepo.service.token;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sk.atris.netxms.confrepo.enums.ApplicationConfiguration;
import sk.atris.netxms.confrepo.service.configurator.ConfigReader;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReadOnlyAccessToken extends AccessToken {
    @Getter
    private static final ReadOnlyAccessToken instance = new ReadOnlyAccessToken();

    @Override
    String loadToken() {
        return ConfigReader.getInstance().getReadOnlyAccessToken();
    }
}
