package org.readutf.ui.hud;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.readutf.ui.Constants;
import org.readutf.ui.Module;
import org.readutf.ui.utils.FontUtils;
import org.readutf.ui.utils.TextHelper;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.font.FontProvider;
import team.unnamed.creative.texture.Texture;

public interface HudOverlay extends Module {

    Component getTitle();

    class TopHudOverlay implements HudOverlay {

        private final Component text;
        private final Texture texture;
        private final char imageChar;

        private TopHudOverlay(Component text, Writable writable) {
            this.text = text;
            this.texture = Texture.texture()
                    .key(Key.key("uitoolkit", Constants.fileNameCounter.getAndIncrement() + ".png"))
                    .data(writable)
                    .build();
            this.imageChar = (char) ('\uF001' + Constants.characterCounter.getAndIncrement());
        }

        public FontProvider generate() {
            return FontProvider.bitMap()
                    .file(texture.key())
                    .height(16)
                    .ascent(11)
                    .characters(String.valueOf(imageChar))
                    .build();
        }

        @Override
        public void apply(ResourcePack resourcePack) {
            FontUtils.appendFont(resourcePack, generate());
            resourcePack.texture(texture);
        }

        @Override
        public Component getTitle() {

            int length = ((int) TextHelper.getComponentWidth(text, true, false));
            int barLength = 200; // Assuming a fixed length for the bar

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

    static HudOverlay top(Component text, Writable writable) {
        return new TopHudOverlay(text, writable);
    }
}
