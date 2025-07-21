package org.readutf.ui.utils;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.unnamed.creative.font.Font;
import team.unnamed.creative.font.FontProvider;
import team.unnamed.creative.overlay.ResourceContainer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FontUtils {

    private static final Logger log = LoggerFactory.getLogger(FontUtils.class);

    public static void appendFont(@NotNull ResourceContainer resourcePack, @NotNull FontProvider... fontProvider) {
        Font defaultFont = resourcePack.font(Font.MINECRAFT_DEFAULT);
        if(defaultFont == null) {
            log.warn("Could not find the default minecraft font? This should not happen");
            return;
        }
        List<FontProvider> providers = new ArrayList<>(defaultFont.providers());
        providers.addAll(Arrays.asList(fontProvider));
        resourcePack.font(Font.font(Font.MINECRAFT_DEFAULT, providers));
    }

}
