package eu.nets.test.flows.data.shared;

import eu.nets.test.core.enums.MpaLanguage;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

public final class LanguageData {
    public static Stream<Arguments> streamAll() {
        return Stream.of(MpaLanguage.values())
                .map(Arguments::of);
    }

    public static Stream<Arguments> streamSupportedByMpa() {
        return Stream.of(MpaLanguage.values())
                .filter(lang -> lang.isSupportedByMpa())
                .map(Arguments::of);
    }

    public static Stream<Arguments> streamNotSupportedByMpa() {
        return Stream.of(MpaLanguage.values())
                .filter(lang -> !lang.isSupportedByMpa())
                .map(Arguments::of);
    }
}
