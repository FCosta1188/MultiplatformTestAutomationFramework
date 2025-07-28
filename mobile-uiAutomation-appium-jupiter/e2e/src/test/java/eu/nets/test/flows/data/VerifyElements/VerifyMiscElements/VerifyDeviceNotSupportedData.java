package eu.nets.test.flows.data.VerifyElements.VerifyMiscElements;

import eu.nets.test.core.enums.MpaLanguage;
import eu.nets.test.core.exceptions.UnsupportedPlatformException;
import eu.nets.test.flows.data.shared.UserData;
import eu.nets.test.util.EnvUtil;
import org.junit.jupiter.params.provider.Arguments;
import org.openqa.selenium.Platform;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class VerifyDeviceNotSupportedData {
    public static final String HEADER = "Something went wrong...";
    public static final String TITLE = "Unfortunately this Device is not supported";
    public static final String DESCRIPTION_ANDROID_NEXI = "This Android device is not compatible with Nexi SoftPOS. Please upgrade or switch to another device to get started.";
    public static final String DESCRIPTION_IOS_NEXI = "This iOS Device is not compatible with Nexi SoftPOS. Please upgrade or switch to another device to get started.";
    public static final String DESCRIPTION_IOS_NETS = "This iOS Device is not compatible with Nets SoftPOS. Please upgrade or switch to another device to get started.";

    public static Stream<Arguments> stream() {
        List<Arguments> dataEntries;
        if(EnvUtil.isAndroid()) {
            dataEntries = UserData.allUsers().stream()
                    .filter(user ->
                            user.platform() != null &&
                            user.platform().equals(Platform.ANDROID) &&
                            user.flowTestTags().contains("VerifyDeviceNotSupportedFlow"))
                    .map(Arguments::of)
                    .toList();
        } else if (EnvUtil.isIos()) {
            dataEntries = UserData.allUsers().stream()
                    .filter(user ->
                            user.platform() != null &&
                            user.platform().equals(Platform.IOS) &&
                            user.flowTestTags().contains("VerifyDeviceNotSupportedFlow"))
                    .map(Arguments::of)
                    .toList();
        } else {
            throw new UnsupportedPlatformException();
        }

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

    List<Arguments> bauUsers;

}
