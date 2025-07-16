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
import java.util.Arrays;
import java.util.List;

public class UIToolkit {

    private final @NotNull static MinecraftResourcePackReader reader =
            MinecraftResourcePackReader.builder().lenient(true).build();

    private final List<Module> modules;

    public UIToolkit() {
        this.modules = new ArrayList<>();
    }

    public UIToolkit add(Module... module) {
        modules.addAll(Arrays.asList(module));
        return this;
    }

    public ResourcePack build() throws IOException {

        ResourcePack negativeSpaceFontPack;
        try (InputStream in = Testing.class.getClassLoader().getResourceAsStream("NegativeSpaceFont.zip")) {
            if (in == null) throw new FileNotFoundException();
            negativeSpaceFontPack = reader.readFromInputStream(in);
        }

        for (Module module : modules) {
            module.apply(negativeSpaceFontPack);
        }

        return negativeSpaceFontPack;
    }
}
