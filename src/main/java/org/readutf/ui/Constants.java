package org.readutf.ui;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.metadata.Metadata;
import team.unnamed.creative.texture.Texture;

import java.util.concurrent.atomic.AtomicInteger;

public class Constants {

    public static AtomicInteger fileNameCounter = new AtomicInteger(0);
    private static AtomicInteger characterCounter = new AtomicInteger(0);
    private static AtomicInteger modelCounter = new AtomicInteger(0);

    public static char getNextCharacter() {
        return (char) ('\uF001' + Constants.characterCounter.getAndIncrement());
    }

    public static @NotNull Texture createTexture(Writable writable) {
        return createTexture(writable, Metadata.empty());
    }

    public static int getNextModelId() {
        return Constants.modelCounter.getAndIncrement();
    }

    public static @NotNull Texture createTexture(@NotNull Writable writable, @NotNull Metadata metadata) {
        return Texture.texture()
                .key(Key.key(UIToolkit.namespace, Constants.fileNameCounter.getAndIncrement() + ".png"))
                .data(writable)
                .meta(metadata)
                .build();
    }

    public static @NotNull Texture createItemTexture(@NotNull Writable writable, @NotNull Metadata metadata) {
        return Texture.texture()
                .key(Key.key(UIToolkit.namespace, "item/" + Constants.fileNameCounter.getAndIncrement() + ".png"))
                .meta(metadata)
                .data(writable)
                .build();
    }
}
