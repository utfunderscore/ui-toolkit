package org.readutf.ui.hud;

import net.kyori.adventure.text.Component;
import org.readutf.ui.Constants;
import org.readutf.ui.Module;
import org.readutf.ui.utils.FontUtils;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.font.BitMapFontProvider;
import team.unnamed.creative.font.FontProvider;
import team.unnamed.creative.texture.Texture;

public class HotbarHud implements Module {

    private final char character = Constants.getNextCharacter();
    private final Texture texture;

    public HotbarHud(Writable writable) {
        this.texture = Constants.createTexture(writable);
    }

    public Component getText() {
        return Component.text(String.valueOf(character));
    }

    @Override
    public void apply(ResourcePack resourcePack) {
        BitMapFontProvider rightFont = FontProvider.bitMap()
                .file(texture.key())
                .height(25)
                .ascent(-10)
                .characters(String.valueOf(character))
                .build();

        resourcePack.texture(texture);
        FontUtils.appendFont(resourcePack, rightFont);
    }
}
