package org.readutf.ui.hud;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.readutf.ui.Constants;
import org.readutf.ui.Module;
import org.readutf.ui.utils.DefaultFontInfo;
import org.readutf.ui.utils.FontUtils;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.font.FontProvider;
import team.unnamed.creative.texture.Texture;

public interface HudOverlay extends Module {

    Component getTitle();

    class TopHudOverlay implements HudOverlay {

        private final Texture texture;
        private final char imageChar;

        private final Component test = Component.text("test123123");

        private TopHudOverlay(Writable writable) {
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
                    .ascent(0)
                    .characters(String.valueOf(imageChar))
                    .build();
        }

        public int getLength(Component component) {
            PlainTextComponentSerializer serializer = PlainTextComponentSerializer.plainText();
            String text = serializer.serialize(component);
            int length = 0;
            for (char c : text.toCharArray()) {
                length += DefaultFontInfo.getDefaultFontInfo(c).getLength();
            }
            return length;
        }

        @Override
        public void apply(ResourcePack resourcePack) {
            FontUtils.appendFont(resourcePack, generate());
            resourcePack.texture(texture);
        }

        @Override
        public Component getTitle() {
            return Component.text(imageChar);
        }
    }

    static HudOverlay top(Writable writable) {
        return new TopHudOverlay(writable);
    }

}
