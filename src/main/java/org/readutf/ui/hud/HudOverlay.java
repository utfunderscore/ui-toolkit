package org.readutf.ui.hud;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.readutf.ui.Constants;
import org.readutf.ui.Module;
import org.readutf.ui.utils.FontUtils;
import org.readutf.ui.utils.ImageCreator;
import org.readutf.ui.utils.TextHelper;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.font.FontProvider;
import team.unnamed.creative.texture.Texture;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public interface HudOverlay extends Module {

    Component getTitle();

    class TopHud implements HudOverlay {

        private final Component text;
        private final Texture texture;
        private final int barWidth;
        private final char imageChar;
        private final int height;

        private TopHud(Component text, BufferedImage bufferedImage) throws IOException {
            this.text = text;
            this.texture = Texture.texture()
                    .key(Key.key("uitoolkit", Constants.fileNameCounter.getAndIncrement() + ".png"))
                    .data(Writable.copyInputStream(new ByteArrayInputStream(ImageCreator.toPngBuffer(bufferedImage))))
                    .build();
            this.barWidth = bufferedImage.getWidth();
            this.imageChar = (char) ('\uF001' + Constants.characterCounter.getAndIncrement());
            this.height = bufferedImage.getHeight();
        }

        private TopHud(Component text, BufferedImage leftPiece, BufferedImage centerPiece) throws IOException {
            this.text = text;
            this.barWidth = (int) (TextHelper.getComponentWidth(text, true, false) + (leftPiece.getWidth()));
            this.texture = generateTexture(leftPiece, centerPiece, barWidth);
            this.imageChar = (char) ('\uF001' + Constants.characterCounter.getAndIncrement());
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
            FontUtils.appendFont(
                    resourcePack,
                    FontProvider.bitMap()
                            .file(texture.key())
                            .height(height)
                            .ascent((height / 2) + 3)
                            .characters(String.valueOf(imageChar))
                            .build());
            resourcePack.texture(texture);
        }

        @Override
        public Component getTitle() {

            int length = ((int) TextHelper.getComponentWidth(text, true, false));
            int barLength = barWidth; // Assuming a fixed length for the bar

            int offset = barLength / 2 + (length / 2);
            int leftOffset = barLength - offset;

            System.out.println("offset: " + offset);

            TextComponent title = Component.text()
                    .append(Component.translatable("space.-" + leftOffset))
                    .append(Component.text(imageChar).append(Component.translatable("space.-" + offset)))
                    .append(text)
                    .build();

            System.out.println(title.getClass());

            return title;
        }
    }

    static @NotNull HudOverlay top(Component text, @NotNull Writable writable) {
        try {
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(writable.toByteArray()));
            return new TopHud(text, image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Contract("_, _, _ -> new")
    static @NotNull HudOverlay top(Component text, BufferedImage leftPiece, BufferedImage centerPiece) {
        try {
            return new TopHud(text, leftPiece, centerPiece);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static @NotNull HudOverlay top(Component text, @NotNull Writable leftPiece, @NotNull Writable centerPiece) {
        try {
            BufferedImage left = ImageIO.read(new ByteArrayInputStream(leftPiece.toByteArray()));
            BufferedImage center = ImageIO.read(new ByteArrayInputStream(centerPiece.toByteArray()));
            return new TopHud(text, left, center);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
