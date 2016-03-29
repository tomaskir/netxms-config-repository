package sk.atris.netxms.confrepo.model.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class RequestedConfigItem {
    @Getter
    private String guid;

    @Getter
    private int requestedRevisionVersion;
}
