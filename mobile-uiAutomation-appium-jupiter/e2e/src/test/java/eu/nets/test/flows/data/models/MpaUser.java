package eu.nets.test.flows.data.models;

import eu.nets.test.core.enums.AndroidSnapshot;
import org.openqa.selenium.Platform;

import java.util.List;
import java.util.Set;

public record MpaUser(
        String type,
        String description,
        Platform platform,
        String fullName,
        String vat,
        String address,
        String country,
        String email,
        String org,
        List<MpaCard> applicableCards,
        AndroidSnapshot loggedInAndroidSnapshot,
        Set<String> flowTestTags
) {
}
