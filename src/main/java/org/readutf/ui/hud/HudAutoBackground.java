package org.readutf.ui.hud;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.ShadowColor;
import org.jetbrains.annotations.NotNull;
import org.readutf.ui.Constants;
import org.readutf.ui.utils.FontUtils;
import org.readutf.ui.utils.ImageCreator;
import org.readutf.ui.utils.TextHelper;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.font.BitMapFontProvider;
import team.unnamed.creative.font.FontProvider;
import team.unnamed.creative.overlay.ResourceContainer;
import team.unnamed.creative.part.ResourcePackPart;
import team.unnamed.creative.texture.Texture;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Automatically generates a background for text components in the HUD.
 * This module creates textures for left, center, and right characters
 * and applies them to the resource pack.
 * The background adapts to the width of the text component.
 * Intended for use in an action bar or bossbar.
 */
public class HudAutoBackground implements ResourcePackPart {

    private final Texture leftCharTexture;
    private final Texture centerCharTexture;
    private final Texture rightCharTexture;

    private final char leftChar;
    private final int leftCharSize;
    private final char centerChar;
    private final int centerCharSize;
    private final char rightChar;
    private final int rightCharSize;
    private final int height;

    private boolean hideShadow = false;

    private HudAutoBackground(BufferedImage leftPiece, BufferedImage centerPiece) throws IOException {
        this.leftChar = Constants.getNextCharacter();
        this.centerChar = Constants.getNextCharacter();
        this.rightChar = Constants.getNextCharacter();
        this.leftCharSize = leftPiece.getWidth();
        this.centerCharSize = centerPiece.getWidth();
        this.rightCharSize = leftPiece.getWidth();
        this.leftCharTexture = Constants.createTexture(
                Writable.copyInputStream(new ByteArrayInputStream(ImageCreator.toPngBuffer(leftPiece))));
        this.centerCharTexture = Constants.createTexture(
                Writable.copyInputStream(new ByteArrayInputStream(ImageCreator.toPngBuffer(centerPiece))));
        this.rightCharTexture = Constants.createTexture(Writable.copyInputStream(
                new ByteArrayInputStream(ImageCreator.toPngBuffer(ImageCreator.flipHorizontally(leftPiece)))));
        this.height = leftPiece.getHeight();
    }

    public HudAutoBackground hideShadow(boolean hideShadow) {
        this.hideShadow = hideShadow;
        return this;
    }

    @Override
    public void addTo(ResourceContainer resourcePack) {
        BitMapFontProvider centerFont = FontProvider.bitMap()
                .file(centerCharTexture.key())
                .height(height)
                .ascent((height / 2) + 3)
                .characters(String.valueOf(centerChar))
                .build();
        BitMapFontProvider leftFont = FontProvider.bitMap()
                .file(leftCharTexture.key())
                .height(height)
                .ascent((height / 2) + 3)
                .characters(String.valueOf(leftChar))
                .build();
        BitMapFontProvider rightFont = FontProvider.bitMap()
                .file(rightCharTexture.key())
                .height(height)
                .ascent((height / 2) + 3)
                .characters(String.valueOf(rightChar))
                .build();
        FontUtils.appendFont(resourcePack, centerFont, leftFont, rightFont);
        resourcePack.texture(centerCharTexture);
        resourcePack.texture(leftCharTexture);
        resourcePack.texture(rightCharTexture);
    }

    public Component getTitle(Component component) {

        int componentWidth = ((int) TextHelper.getComponentWidth(component, true, false));

        BackgroundInfo backgroundInfo = generateTextBackground(
                component, leftChar, leftCharSize, centerChar, centerCharSize, rightChar, rightCharSize);

        int barLength = backgroundInfo.totalSize;

        int offset = barLength / 2 + (componentWidth / 2);
        int leftOffset = barLength - offset;

        Component background = backgroundInfo.background();
        if(hideShadow) {
            background = background.shadowColor(ShadowColor.none());
        }
        background = background.append(Component.translatable("space.-" + offset));


        return Component.text()
                .append(Component.translatable("space." + (leftOffset - backgroundInfo.offsets)))
                .append(background)
                .append(component)
                .build();
    }

    private static @NotNull BackgroundInfo generateTextBackground(
            Component component,
            char leftChar,
            int leftCharSize,
            char midChar,
            int midCharSize,
            char rightChar,
            int rightCharSize) {

        int textWidth = (int) TextHelper.getComponentWidth(component, true, false);

        int withoutEnds = textWidth - leftCharSize - rightCharSize;
        if ((withoutEnds + 20) < midCharSize) {
            return new BackgroundInfo(
                    Component.text(leftChar)
                            .append(Component.translatable("space.-1"))
                            .append(Component.text(rightChar)),
                    leftCharSize + rightCharSize,
                    1);
        }

        TextComponent builder = Component.text(leftChar).append(Component.translatable("space.-1"));

        int numOfMids = ((withoutEnds + 10) / midCharSize) + 1;
        for (int i = 0; i < numOfMids; i++) {
            builder = builder.append(Component.text(midChar)).append(Component.translatable("space.-1"));
        }
        builder = builder.append(Component.text(rightChar));

        return new BackgroundInfo(builder, leftCharSize + (numOfMids * midCharSize) + rightCharSize, 1 + (numOfMids));
    }

    private record BackgroundInfo(Component background, int totalSize, int offsets) {}

    public static @NotNull HudAutoBackground autoBackground(
            @NotNull Writable leftPiece, @NotNull Writable centerPiece) {
        try {
            BufferedImage left = ImageIO.read(new ByteArrayInputStream(leftPiece.toByteArray()));
            BufferedImage center = ImageIO.read(new ByteArrayInputStream(centerPiece.toByteArray()));
            return new HudAutoBackground(left, center);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}