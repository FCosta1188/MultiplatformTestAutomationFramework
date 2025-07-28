package eu.nets.test.flows.data.BAUStatements;

import eu.nets.test.core.enums.MpaLanguage;
import eu.nets.test.flows.data.shared.UserData;
import org.junit.jupiter.params.provider.Arguments;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class BAUStatementsData {
    public enum Filter {
        STATEMENT_NO,
        DATE,
        DATE_AND_STATEMENT_NO,
        NO_RESULTS
    }

    public static final String STATEMENT_NO_VALID = "150673442-2025-0603-W-EUR-DEUC";
    public static final String STATEMENT_NO_INVALID = "123456789-4202-5210-M-RUE-CUED";
    public static final String DATE_YEAR = "2025";
    public static final String DATE_MMM_YYYY = "Jul 2025";
    public static final String DATE_MONTH_YYYY = "July 2025";
    public static final String DATE_DD_MMM = "07 Jul";
    public static final String DATE_DD_MMM_YYYY = "07 Jul 2025";
    public static final String DATE_DAY_DD_MONTH_YYYY = "Monday, 07 July 2025";

    public static Stream<Arguments> stream() {
        List<Arguments> dataEntries = UserData.allUsers().stream()
                .filter(user -> user.flowTestTags().contains("VerifyBAUStatementsFlow"))
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
