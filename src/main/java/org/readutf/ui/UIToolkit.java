package org.readutf.ui;

import org.jetbrains.annotations.NotNull;
import org.readutf.ui.container.MenuOverlay;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.font.Font;
import team.unnamed.creative.font.FontProvider;
import team.unnamed.creative.serialize.minecraft.MinecraftResourcePackReader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class UIToolkit {

    private @NotNull static MinecraftResourcePackReader reader =
            MinecraftResourcePackReader.builder().lenient(true).build();

    private final List<MenuOverlay> menus;

    public UIToolkit() {
        this.menus = new ArrayList<>();
    }

    public UIToolkit menu(MenuOverlay menu) {
        menus.add(menu);
        return this;
    }

    public ResourcePack build() throws IOException {

        ResourcePack negativeSpaceFontPack;
        try (InputStream in = Testing.class.getClassLoader().getResourceAsStream("NegativeSpaceFont.zip")) {
            if (in == null) throw new FileNotFoundException();
            negativeSpaceFontPack = reader.readFromInputStream(in);
        }

        Font defaultFont = negativeSpaceFontPack.font(Font.MINECRAFT_DEFAULT);
        List<FontProvider> providers = new ArrayList<>(defaultFont.providers());
        for (MenuOverlay menu : menus) {
            providers.add(menu.generate());
            negativeSpaceFontPack.texture(menu.getTexture());
        }
        negativeSpaceFontPack.font(Font.font(Font.MINECRAFT_DEFAULT, providers));

        return negativeSpaceFontPack;
    }
}
