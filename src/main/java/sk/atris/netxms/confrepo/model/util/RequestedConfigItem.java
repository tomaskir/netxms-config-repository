package sk.atris.netxms.confrepo.model.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class RequestedConfigItem {
    @Getter
    private String guid;

    // FIXME: adjust the type of this field depending on how versioning is handled
    @Getter
    private String revisionVersion;
}
