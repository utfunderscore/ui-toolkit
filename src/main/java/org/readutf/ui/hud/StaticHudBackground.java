package org.readutf.ui.hud;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
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
 * Generates a background for text components in the HUD.
 * This module creates textures for left, center, and right characters
 * and applies them to the resource pack.
 * Can work with either a pre-defined static component or dynamically
 * adapt to components provided at runtime.
 */
public class StaticHudBackground implements ResourcePackPart {

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
    
    // Fields for static component
    private final Component staticComponent;
    private final BackgroundInfo staticBackgroundInfo;
    private final Component staticTitle;
    private final boolean isStatic;

    /**
     * Creates a StaticHudBackground for dynamic components.
     */
    private StaticHudBackground(BufferedImage leftPiece, BufferedImage centerPiece) throws IOException {
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
        
        // Set static fields to null since this is the dynamic version
        this.staticComponent = null;
        this.staticBackgroundInfo = null;
        this.staticTitle = null;
        this.isStatic = false;
    }
    
    /**
     * Creates a StaticHudBackground with a static component.
     * Pre-calculates the background for the given component.
     */
    private StaticHudBackground(BufferedImage leftPiece, BufferedImage centerPiece, Component staticComponent) throws IOException {
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
        
        // Set static component and pre-calculate the background
        this.staticComponent = staticComponent;
        this.staticBackgroundInfo = generateTextBackground(
                staticComponent, leftChar, leftCharSize, centerChar, centerCharSize, rightChar, rightCharSize);
        
        // Pre-calculate the title with background
        int componentWidth = ((int) TextHelper.getComponentWidth(staticComponent, true, false));
        int barLength = staticBackgroundInfo.totalSize;
        int offset = barLength / 2 + (componentWidth / 2);
        int leftOffset = barLength - offset;
        
        this.staticTitle = Component.text()
                .append(Component.translatable("space." + (leftOffset - staticBackgroundInfo.offsets)))
                .append(staticBackgroundInfo.background().append(Component.translatable("space.-" + offset)))
                .append(staticComponent)
                .build();
        
        this.isStatic = true;
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
    
    /**
     * Checks if this background was created with a static component.
     *
     * @return true if this is a static background, false otherwise
     */
    public boolean isStatic() {
        return isStatic;
    }
    
    /**
     * Gets the static component this background was created for.
     *
     * @return the static component
     * @throws IllegalStateException if this is not a static background
     */
    public Component getStaticComponent() {
        if (!isStatic) {
            throw new IllegalStateException("This StaticHudBackground was not created with a static component");
        }
        return staticComponent;
    }

    /**
     * Dynamically generates a title with background for the given component.
     * 
     * @param component The component to create a background for
     * @return The component with background
     * @throws IllegalStateException if this is a static background
     */
    public Component getTitle(Component component) {
        if (isStatic) {
            throw new IllegalStateException("This StaticHudBackground was created with a static component. Use getStaticTitle() instead.");
        }

        int componentWidth = ((int) TextHelper.getComponentWidth(component, true, false));

        BackgroundInfo backgroundInfo = generateTextBackground(
                component, leftChar, leftCharSize, centerChar, centerCharSize, rightChar, rightCharSize);

        int barLength = backgroundInfo.totalSize;

        int offset = barLength / 2 + (componentWidth / 2);
        int leftOffset = barLength - offset;

        return Component.text()
                .append(Component.translatable("space." + (leftOffset - backgroundInfo.offsets)))
                .append(backgroundInfo.background().append(Component.translatable("space.-" + offset)))
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

    /**
     * Creates a StaticHudBackground for dynamic components.
     */
    public static @NotNull StaticHudBackground autoBackground(
            @NotNull Writable leftPiece, @NotNull Writable centerPiece) {
        try {
            BufferedImage left = ImageIO.read(new ByteArrayInputStream(leftPiece.toByteArray()));
            BufferedImage center = ImageIO.read(new ByteArrayInputStream(centerPiece.toByteArray()));
            return new StaticHudBackground(left, center);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Creates a StaticHudBackground with a static component.
     * The background will be pre-calculated for the given component.
     */
    public static @NotNull StaticHudBackground staticBackground(
            @NotNull Writable leftPiece, @NotNull Writable centerPiece, @NotNull Component staticComponent) {
        try {
            BufferedImage left = ImageIO.read(new ByteArrayInputStream(leftPiece.toByteArray()));
            BufferedImage center = ImageIO.read(new ByteArrayInputStream(centerPiece.toByteArray()));
            return new StaticHudBackground(left, center, staticComponent);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}