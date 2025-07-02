package eu.nets.test.flows.data.shared;

import eu.nets.test.core.enums.MpaLanguage;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

public final class LanguageData {
    public static Stream<Arguments> stream() {
        return Stream.of(MpaLanguage.values())
                .map(Arguments::of);
    }
}
