package org.readutf.ui.container;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.readutf.ui.Constants;
import org.readutf.ui.utils.FontUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.font.FontProvider;
import team.unnamed.creative.overlay.ResourceContainer;
import team.unnamed.creative.part.ResourcePackPart;
import team.unnamed.creative.texture.Texture;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;

/**
 * Intended to be used with the corresponding container textures.
 * To use this overlay, generate a component get getTitle() and apply it to the container title.
 */
public class MenuOverlay implements ResourcePackPart {

    private static final Logger log = LoggerFactory.getLogger(MenuOverlay.class);

    private final Texture texture;
    private final int yOffset;
    private final int xOffset;
    private final char imageChar;

    private MenuOverlay(Texture texture, int yOffset, int xOffset) {
        this.texture = texture;
        this.yOffset = yOffset;
        this.xOffset = xOffset;
        this.imageChar = Constants.getNextCharacter();
    }

    @Override
    public void addTo(ResourceContainer resourcePack) {
        FontUtils.appendFont(resourcePack, FontProvider.bitMap()
                .file(texture.key())
                .height(256)
                .ascent(yOffset)
                .characters(String.valueOf(imageChar))
                .build());
        resourcePack.texture(texture);
    }

    public Component getTitle() {
        return translatable("space." + xOffset, NamedTextColor.WHITE).append(text(imageChar));
    }

    public static MenuOverlay overlay(Texture texture, int yOffset, int xOffset) {
        return new MenuOverlay(texture, yOffset, xOffset);
    }

    public static MenuOverlay chest(Writable writable) {
        Texture texture = Constants.createTexture(writable);
        return new MenuOverlay(texture, 39, -48);
    }

    public static MenuOverlay anvil(Texture texture) {
        return new MenuOverlay(texture, 39, -100);
    }

    public static MenuOverlay crafting(Texture texture) {
        return new MenuOverlay(texture, 39, -69);
    }
}
