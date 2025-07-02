package eu.nets.test.flows.data.Registration;

import eu.nets.test.core.enums.MpaLanguage;
import eu.nets.test.flows.data.shared.MpaData;
import org.junit.jupiter.params.provider.Arguments;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public final class RegistrationData {
    public static Stream<Arguments> stream() {
        List<Arguments> dataEntries = MpaData.allUsers().stream()
                .filter(user -> user.flowTestTags().contains("RegistrationFlow"))
                .map(Arguments::of)
                .toList();

        return dataEntries.stream()
                .flatMap(dataEntry ->
                        Arrays.stream(MpaLanguage.values())
                                .map(lang -> Arguments.of(
                                        dataEntry.get()[0], // user
                                        lang // appLanguage
                                ))
                );
    }
}
