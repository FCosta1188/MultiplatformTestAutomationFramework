package eu.nets.test.flows.data.VerifyElements.VerifyMiscElements;

import eu.nets.test.core.enums.MpaLanguage;
import eu.nets.test.flows.data.shared.UserData;
import org.junit.jupiter.params.provider.Arguments;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class VerifyPortalAdvertData {
    public static String baseAuthUrl(String userType) {
        userType = userType.trim().toUpperCase();

        switch (userType) {
            case "SMP":
            case "BAU":
                return "https://login.nexi.de/auth/";
            case "TNP":
                return "https://my.nets.eu/";
            case "PUMA":
                return "https://sts.id.nets.eu/login/";
            case "MEPO":
                return "https://merchantreporting.nets.eu/nets";
            default:
                throw new IllegalArgumentException("Invalid user type: " + userType);
        }
    }

    public static Stream<Arguments> stream() {
        List<Arguments> dataEntries = UserData.allUsers().stream()
                .filter(user -> user.flowTestTags().contains("VerifyPortalAdvertFlow"))
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
