package org.readutf.ui.container;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.ApiStatus;
import org.readutf.ui.Constants;
import org.readutf.ui.Testing;
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.font.FontProvider;
import team.unnamed.creative.texture.Texture;

import java.util.concurrent.atomic.AtomicInteger;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;

public class MenuOverlay {

    private static final AtomicInteger sNextId = new AtomicInteger(1);

    private final Texture texture;
    private final int yOffset;
    private final int xOffset;
    private final char imageChar;

    private MenuOverlay(Texture texture, int yOffset, int xOffset) {
        this.texture = texture;
        this.yOffset = yOffset;
        this.xOffset = xOffset;
        this.imageChar = (char) ('\uF001' + sNextId.getAndIncrement());
    }

    public FontProvider generate() {
        return FontProvider.bitMap()
                .file(texture.key())
                .height(256)
                .ascent(yOffset)
                .characters(String.valueOf(imageChar))
                .build();
    }

    public Component getTitle() {
        return translatable("space." + xOffset, NamedTextColor.WHITE).append(text(imageChar));
    }

    public static MenuOverlay overlay(Texture texture, int yOffset, int xOffset) {
        return new MenuOverlay(texture, yOffset, xOffset);
    }

    public static MenuOverlay chest(Writable writable) {
        Texture texture = Texture.texture()
                .key(Key.key("uitoolkit", Constants.fileNameCounter.getAndIncrement() + ".png"))
                .data(writable)
                .build();
        return new MenuOverlay(texture, 39, -48);
    }

    public static MenuOverlay anvil(Texture texture) {
        return new MenuOverlay(texture, 39, -100);
    }

    public static MenuOverlay crafting(Texture texture) {
        return new MenuOverlay(texture, 39, -69);
    }

    @ApiStatus.Internal
    public Texture getTexture() {
        return texture;
    }
}
