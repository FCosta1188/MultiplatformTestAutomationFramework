package eu.nets.test.flows.data.VerifyElements.VerifyMiscElements;

import eu.nets.test.core.enums.MpaLanguage;
import eu.nets.test.flows.data.shared.UserData;
import org.junit.jupiter.params.provider.Arguments;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class VerifyMyInfoData {
    public static Stream<Arguments> stream() {
        List<Arguments> dataEntries = UserData.allUsers().stream()
                .filter(user -> user.flowTestTags().contains("VerifyMyInfoFlow"))
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
