package eu.nets.test.flows.data.models;

import java.nio.file.Path;

public record MpaCard(
        String title,
        String label,
        Path iconRefImgPath,
        java.awt.Color iconColor,
        String articleTitle,
        String articleDescription,
        String paragraphTitle1,
        String paragraphDescription1,
        String paragraphHyperlink1,
        String paragraphTitle2,
        String paragraphDescription2,
        String paragraphHyperlink2,
        String paragraphTitle3,
        String paragraphDescription3,
        String paragraphHyperlink3,
        String paragraphTitle4,
        String paragraphDescription4,
        String paragraphHyperlink4,
        String paragraphTitle5,
        String paragraphDescription5,
        String paragraphHyperlink5
) {}
