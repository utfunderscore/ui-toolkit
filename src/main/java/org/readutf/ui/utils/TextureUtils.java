package org.readutf.ui.utils;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.key.Key;
import org.readutf.ui.Module;
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.texture.Texture;

public class TextureUtils {

    public static Module hideBossBar(BossBar.Color color) {
        return resourcePack -> {
            Writable blank = Writable.inputStream(() -> ImageCreator.createBlankPngAsStream(182, 5));

            Texture emptyBar = Texture.texture(Key.key("minecraft:gui/sprites/boss_bar/%s_progress.png".formatted(color.name().toLowerCase())), blank);
            Texture emptyBackground = Texture.texture(Key.key("minecraft:gui/sprites/boss_bar/%s_background.png".formatted(color.name().toLowerCase())), blank);
            resourcePack.texture(emptyBar);
            resourcePack.texture(emptyBackground);
        };
    }

}
