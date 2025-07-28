package eu.nets.test.flows.data.VerifyEmptyState;

import eu.nets.test.core.enums.MpaLanguage;
import eu.nets.test.flows.data.shared.UserData;
import org.junit.jupiter.params.provider.Arguments;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class VerifyEmptyStateData {
    public static final String TRANSACTIONS_EMPTY_STATE_TEXT = "Start transacting to see your transactions here. Please note that transactions can take 1 to 2 days to appear in the app.";
    public static final String ACCOUNTING_SETTLEMENTS_EMPTY_STATE_TEXT = "You have not received any payouts yet, check back later to see your settlements here";
    public static final String ACCOUNTING_INVOICES_EMPTY_STATE_TEXT = "No invoices yet, please check back later to view your invoices here.";
    public static final String AMOUNT_INVALID = "1234";
    public static final String INVOICE_NO_INVALID = "654321";

    public static Stream<Arguments> stream() {
        List<Arguments> dataEntries = UserData.allUsers().stream()
                .filter(user -> user.flowTestTags().contains("VerifyEmptyState"))
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
