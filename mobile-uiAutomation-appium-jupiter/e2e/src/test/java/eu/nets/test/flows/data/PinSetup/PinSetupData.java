package eu.nets.test.flows.data.PinSetup;

import eu.nets.test.core.enums.MpaLanguage;
import eu.nets.test.flows.data.shared.UserData;
import org.junit.jupiter.params.provider.Arguments;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class PinSetupData {
    public static String TITLE = "Rules For 6 Digit Passcode";
    public static String BULLET_POINT_1 = "Ensure at least three different digits are supplied (i.e. 111222 is not allowed)";
    public static String BULLET_POINT_2 = "Ascending and/or descending sub-parts of 3 digits are not allowed (i.e. 123123 is not allowed)";
    public static String BULLET_POINT_BOTH = "  Ensure at least three different digits are supplied (i.e. 111222 is not allowed)\\n\\n  Ascending and/or descending sub-parts of 3 digits are not allowed (i.e. 123123 is not allowed)\\n\\n";

    public static Stream<Arguments> stream() {
        List<Arguments> dataEntries = UserData.allUsers().stream()
                .filter(user -> user.flowTestTags().contains("PinSetupFlow"))
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
