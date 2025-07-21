package org.readutf.ui;

import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.part.ResourcePackPart;
import team.unnamed.creative.serialize.minecraft.MinecraftResourcePackReader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UIToolkit {

    public static final String namespace = "uitoolkit";

    private final @NotNull static MinecraftResourcePackReader reader =
            MinecraftResourcePackReader.builder().lenient(true).build();

    private final List<ResourcePackPart> modules;

    public UIToolkit() {
        this.modules = new ArrayList<>();
    }

    /**
     * Adds a module to the toolkit.
     * Each module handles its own resources and applies them to the resource pack
     * when {@link #build()} is called.
     * @param module
     */
    public UIToolkit add(ResourcePackPart... module) {
        modules.addAll(Arrays.asList(module));
        return this;
    }

    public ResourcePack build() throws IOException {

        ResourcePack negativeSpaceFontPack;
        try (InputStream in = UIToolkit.class.getClassLoader().getResourceAsStream("NegativeSpaceFont.zip")) {
            if (in == null) throw new FileNotFoundException();
            negativeSpaceFontPack = reader.readFromInputStream(in);
        }

        for (ResourcePackPart module : modules) {
            module.addTo(negativeSpaceFontPack);
        }

        return negativeSpaceFontPack;
    }
}
