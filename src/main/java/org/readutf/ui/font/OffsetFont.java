package org.readutf.ui.font;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.readutf.ui.UIToolkit;
import team.unnamed.creative.font.BitMapFontProvider;
import team.unnamed.creative.font.FontProvider;
import team.unnamed.creative.overlay.ResourceContainer;
import team.unnamed.creative.part.ResourcePackPart;

import java.util.Arrays;
import java.util.List;

public class OffsetFont implements ResourcePackPart {

    public static final int DEFAULT_HEIGHT = 7;

    private final Key key;
    private final int height;
    private final int ascent;

    public OffsetFont(int height, int ascent) {
        if(ascent < 0) {
            this.key = Key.key(UIToolkit.namespace, "offset" + ascent);
        } else {
            this.key = Key.key(UIToolkit.namespace, "offset+" + ascent);
        }
        this.height = height;
        this.ascent = ascent;
    }

    public Key getKey() {
        return key;
    }

    @Override
    public void addTo(@NotNull ResourceContainer resourceContainer) {

        List<String> chars = Arrays.asList(
                "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000",
                "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000",
                " !\"#$%&'()*+,-./",
                "0123456789:;<=>?",
                "@ABCDEFGHIJKLMNO",
                "PQRSTUVWXYZ[\\]^_",
                "`abcdefghijklmno",
                "pqrstuvwxyz{|}~\u0000",
                "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000",
                "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000£ƒ",
                "\u0000\u0000\u0000\u0000\u0000\u0000ªº\u0000\u0000¬\u0000\u0000\u0000«»",
                "░▒▓│┤╡╢╖╕╣║╗╝╜╛┐",
                "└┴┬├─┼╞╟╚╔╩╦╠═╬╧",
                "╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀",
                "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000∅∈\u0000",
                "≡±≥≤⌠⌡÷≈°∙\u0000√ⁿ²■\u0000"
                );

        BitMapFontProvider provider = FontProvider.bitMap(Key.key("minecraft", "font/ascii.png"), height, ascent, chars);
        resourceContainer.font(key, provider);
    }
}
