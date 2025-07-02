package eu.nets.test.flows.data.models;

import eu.nets.test.core.enums.AndroidSnapshot;

import java.util.List;

public record MpaUser(
        String type,
        String description,
        String fullName,
        String vat,
        String address,
        String country,
        String email,
        String org,
        List<MpaCard> applicableCards,
        AndroidSnapshot loggedInAndroidSnapshot,
        java.util.Set<String> flowTestTags
) {
}
