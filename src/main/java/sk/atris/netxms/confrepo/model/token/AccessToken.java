package sk.atris.netxms.confrepo.model.token;

import lombok.Getter;

abstract class AccessToken {
    @Getter
    private final String token = "123"; // FIXME: externalize the token to a property file
}
