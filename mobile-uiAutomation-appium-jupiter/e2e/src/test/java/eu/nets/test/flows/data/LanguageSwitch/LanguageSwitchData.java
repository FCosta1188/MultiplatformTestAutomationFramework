package eu.nets.test.flows.data.LanguageSwitch;

import eu.nets.test.core.enums.MpaLanguage;
import eu.nets.test.flows.data.shared.MpaData;
import org.junit.jupiter.params.provider.Arguments;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class LanguageSwitchData {
    public static Stream<Arguments> streamDeviceLanguageNotSupportedByMpa() {
        List<Arguments> dataEntries = MpaData.allUsers().stream()
                .filter(user -> user.flowTestTags().contains("LanguageSwitchFlow"))
                .map(Arguments::of)
                .toList();

        return dataEntries.stream()
                .flatMap(dataEntry ->
                        Arrays.stream(MpaLanguage.values())
                                .filter(lang -> !lang.isSupportedByMpa())
                                .map(lang -> Arguments.of(
                                        dataEntry.get()[0], // user
                                        lang // appLanguage
                                ))
                );
    }

    public static Stream<Arguments> streamDeviceLanguageSupportedByMpa() {
        List<Arguments> dataEntries = MpaData.allUsers().stream()
                .filter(user -> user.flowTestTags().contains("LanguageSwitchFlow"))
                .map(Arguments::of)
                .toList();

        return dataEntries.stream()
                .flatMap(dataEntry ->
                        Arrays.stream(MpaLanguage.values())
                                .filter(lang -> lang.isSupportedByMpa())
                                .map(lang -> Arguments.of(
                                        dataEntry.get()[0], // user
                                        lang // appLanguage
                                ))
                );
    }
}
