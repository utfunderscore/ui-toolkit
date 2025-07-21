package org.readutf.ui;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.texture.Texture;

import java.util.concurrent.atomic.AtomicInteger;

public class Constants {

    private static AtomicInteger fileNameCounter = new AtomicInteger(0);
    private static AtomicInteger characterCounter = new AtomicInteger(0);

    public static char getNextCharacter() {
        return (char) ('\uF001' + Constants.characterCounter.getAndIncrement());
    }

    public static @NotNull Texture createTexture(Writable writable) {
        return Texture.texture()
                .key(Key.key("uitoolkit", Constants.fileNameCounter.getAndIncrement() + ".png"))
                .data(writable)
                .build();
    }

}
