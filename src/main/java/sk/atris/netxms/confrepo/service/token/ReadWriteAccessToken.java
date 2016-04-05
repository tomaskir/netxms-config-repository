package sk.atris.netxms.confrepo.service.token;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sk.atris.netxms.confrepo.service.configurator.ConfigReader;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReadWriteAccessToken extends AccessToken {
    @Getter
    private static final ReadWriteAccessToken instance = new ReadWriteAccessToken();

    @Override
    String loadToken() {
        return ConfigReader.getInstance().getReadWriteAccessToken();
    }
}