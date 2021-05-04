package org.smartchoice.factory;

import lombok.NoArgsConstructor;
import org.smartchoice.utils.ThirdPartyType;

@NoArgsConstructor
public class SearchFactory {
    public static final SearchResult getSearch(ThirdPartyType thirdPartyType) {
        switch (thirdPartyType) {

            case Lozado:
                return new Lozado();

            default:
                throw new IllegalArgumentException("This bank type is unsupported");
        }
    }
}
