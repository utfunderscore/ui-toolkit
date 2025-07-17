package org.readutf.ui.hud;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.jetbrains.annotations.NotNull;
import org.readutf.ui.Constants;
import org.readutf.ui.Module;
import org.readutf.ui.utils.FontUtils;
import org.readutf.ui.utils.ImageCreator;
import org.readutf.ui.utils.TextHelper;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.font.BitMapFontProvider;
import team.unnamed.creative.font.FontProvider;
import team.unnamed.creative.texture.Texture;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public interface HudOverlay extends Module {

    Component getTitle(Component component);

    class TopHud implements HudOverlay {

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

        private TopHud(BufferedImage leftPiece, BufferedImage centerPiece) throws IOException {
            this.leftChar = Constants.getNextCharacter();
            this.centerChar = Constants.getNextCharacter();
            this.rightChar = Constants.getNextCharacter();
            this.leftCharSize = leftPiece.getWidth();
            this.centerCharSize = centerPiece.getWidth();
            this.rightCharSize = leftPiece.getWidth();
            this.leftCharTexture = Texture.texture()
                    .key(Key.key("uitoolkit", Constants.fileNameCounter.getAndIncrement() + ".png"))
                    .data(Writable.copyInputStream(new ByteArrayInputStream(ImageCreator.toPngBuffer(leftPiece))))
                    .build();
            this.centerCharTexture = Texture.texture()
                    .key(Key.key("uitoolkit", Constants.fileNameCounter.getAndIncrement() + ".png"))
                    .data(Writable.copyInputStream(new ByteArrayInputStream(ImageCreator.toPngBuffer(centerPiece))))
                    .build();
            this.rightCharTexture = Texture.texture()
                    .key(Key.key("uitoolkit", Constants.fileNameCounter.getAndIncrement() + ".png"))
                    .data(Writable.copyInputStream(new ByteArrayInputStream(
                            ImageCreator.toPngBuffer(ImageCreator.flipHorizontally(leftPiece)))))
                    .build();
            this.height = leftPiece.getHeight();
        }

        private static @NotNull Texture generateTexture(
                BufferedImage leftPiece, BufferedImage centerPiece, int barWidth) throws IOException {
            BufferedImage tiledImage = ImageCreator.createTiledImage(leftPiece, centerPiece, barWidth);

            Writable writable =
                    Writable.copyInputStream(new ByteArrayInputStream(ImageCreator.toPngBuffer(tiledImage)));
            return Texture.texture()
                    .key(Key.key("uitoolkit", Constants.fileNameCounter.getAndIncrement() + ".png"))
                    .data(writable)
                    .build();
        }

        @Override
        public void apply(ResourcePack resourcePack) {
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

        @Override
        public Component getTitle(Component component) {

            int componentWidth = ((int) TextHelper.getComponentWidth(component, true, false));


            BackgroundInfo backgroundInfo = generateTextBackground(component, leftChar, leftCharSize, centerChar, centerCharSize, rightChar, rightCharSize);

            int barLength = backgroundInfo.totalSize;

            int offset = barLength / 2 + (componentWidth / 2);
            int leftOffset = barLength - offset;

            System.out.println("offset: " + offset);

            return Component.text()
                    .append(Component.translatable("space." + (leftOffset - backgroundInfo.offsets)))
                    .append(backgroundInfo.background().append(Component.translatable("space.-" + offset)))
                    .append(component)
                    .build();
        }

        private static @NotNull BackgroundInfo generateTextBackground(Component component, char leftChar, int leftCharSize, char midChar, int midCharSize,
                                                                     char rightChar, int rightCharSize) {

            int textWidth = (int) TextHelper.getComponentWidth(component, true, false);

            int withoutEnds = textWidth - leftCharSize - rightCharSize;
            if((withoutEnds + 20) < midCharSize) {
                return new BackgroundInfo(Component.text(leftChar).append(Component.translatable("space.-1")).append(Component.text(rightChar)), leftCharSize + rightCharSize, 1);
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

    }

    static @NotNull HudOverlay top(@NotNull Writable leftPiece, @NotNull Writable centerPiece) {
        try {
            BufferedImage left = ImageIO.read(new ByteArrayInputStream(leftPiece.toByteArray()));
            BufferedImage center = ImageIO.read(new ByteArrayInputStream(centerPiece.toByteArray()));
            return new TopHud(left, center);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
