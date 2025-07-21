package org.readutf.ui.hud;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.ShadowColor;
import org.readutf.ui.Constants;
import org.readutf.ui.utils.FontUtils;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.font.BitMapFontProvider;
import team.unnamed.creative.font.FontProvider;
import team.unnamed.creative.overlay.ResourceContainer;
import team.unnamed.creative.part.ResourcePackPart;
import team.unnamed.creative.texture.Texture;

/**
 * Intended to be used with the bottom-guide.png texture.
 * To apply this module, add the text to an actionbar.
 */
public class HotbarHud implements ResourcePackPart {

    private final char character = Constants.getNextCharacter();
    private final Texture texture;

    private HotbarHud(Writable writable) {
        this.texture = Constants.createTexture(writable);
    }

    public Component getText() {
        return Component.text(String.valueOf(character)).shadowColor(ShadowColor.none());
    }

    @Override
    public void addTo(ResourceContainer resourcePack) {
        BitMapFontProvider rightFont = FontProvider.bitMap()
                .file(texture.key())
                .height(64)
                .ascent(0)
                .characters(String.valueOf(character))
                .build();

        resourcePack.texture(texture);
        FontUtils.appendFont(resourcePack, rightFont);
    }

    public static HotbarHud create(Writable writable) {
        return new HotbarHud(writable);
    }

}
