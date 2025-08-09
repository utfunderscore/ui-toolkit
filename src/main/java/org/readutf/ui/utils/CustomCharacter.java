package org.readutf.ui.utils;

import org.jetbrains.annotations.NotNull;
import org.readutf.ui.Constants;
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.font.BitMapFontProvider;
import team.unnamed.creative.font.FontProvider;
import team.unnamed.creative.overlay.ResourceContainer;
import team.unnamed.creative.part.ResourcePackPart;
import team.unnamed.creative.texture.Texture;

public class CustomCharacter implements ResourcePackPart {

    private final Texture texture;
    private final int height;
    private final int ascent;
    private final char character;

    public CustomCharacter(Writable writable, int height, int ascent) {
        this.texture = Constants.createTexture(writable);
        this.height = height;
        this.ascent = ascent;
        this.character = Constants.getNextCharacter();
    }

    public char getCharacter() {
        return character;
    }

    @Override
    public void addTo(@NotNull ResourceContainer resourceContainer) {
        FontUtils.appendFont(resourceContainer, FontProvider.bitMap()
                .file(texture.key())
                .height(height)
                .ascent(ascent)
                .characters(String.valueOf(character))
                .build());
        resourceContainer.texture(texture);
    }
}
