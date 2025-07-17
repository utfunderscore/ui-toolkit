package org.readutf.ui.hud;

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

    @Override
    public void apply(ResourcePack resourcePack) {
        BitMapFontProvider rightFont = FontProvider.bitMap()
                .file(texture.key())
                .height(25)
                .ascent(25)
                .characters(String.valueOf(character))
                .build();
    }
}
