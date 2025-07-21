package org.readutf.ui.utils;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.part.ResourcePackPart;
import team.unnamed.creative.texture.Texture;

public final class TextureUtils {

    private TextureUtils() {
        // Prevent instantiation of utility class
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Creates a module that hides the boss bar of a specific color.
     *
     * @param color The color of the boss bar to hide
     * @return A module that, when applied to a resource pack, replaces the boss bar textures with blank images
     * @throws IllegalArgumentException if color is null
     */
    @NotNull
    public static ResourcePackPart hideBossBar(@NotNull BossBar.Color color) {

        return resourcePack -> {
            Writable blankTexture = Writable.inputStream(() -> ImageCreator.createBlankPngAsStream(182, 5));

            String colorName = color.name().toLowerCase();
            Texture emptyProgressBar = Texture.texture(
                    Key.key("minecraft:gui/sprites/boss_bar/%s_progress.png".formatted(colorName)), blankTexture);

            Texture emptyBackground = Texture.texture(
                    Key.key("minecraft:gui/sprites/boss_bar/%s_background.png".formatted(colorName)), blankTexture);

            resourcePack.texture(emptyProgressBar);
            resourcePack.texture(emptyBackground);
        };
    }

    public enum HealthBarType {
        NORMAL("minecraft:gui/sprites/hud/heart/"),
        HARDCORE("minecraft:gui/sprites/hud/heart/hardcore_");

        private final String texturePathPrefix;

        HealthBarType(@NotNull String prefix) {
            this.texturePathPrefix = prefix;
        }

        @NotNull
        public String getTexturePathPrefix() {
            return texturePathPrefix;
        }
    }

    /**
     * Creates a module that hides the health bar of a specific type.
     *
     * @param type The type of health bar to hide
     * @return A module that, when applied to a resource pack, replaces the health bar textures with blank images
     * @throws IllegalArgumentException if type is null
     */
    @NotNull
    public static ResourcePackPart hideHealth(@NotNull HealthBarType type) {

        return resourcePack -> {
            Writable blankTexture = Writable.inputStream(() -> ImageCreator.createBlankPngAsStream(182, 5));

            String prefix = type.getTexturePathPrefix();
            String[] heartVariants = {
                "full.png", "half.png",
                "full_blinking.png", "half_blinking.png",
            };

            for (String variant : heartVariants) {
                resourcePack.texture(Texture.texture(Key.key(prefix + variant), blankTexture));
            }

            if (type == HealthBarType.HARDCORE) {
                resourcePack.texture(Texture.texture(
                        Key.key("minecraft:gui/sprites/hud/heart/container_hardcore.png"), blankTexture));
                resourcePack.texture(Texture.texture(
                        Key.key("minecraft:gui/sprites/hud/heart/container_hardcore_blinking.png"), blankTexture));
            } else {
                resourcePack.texture(
                        Texture.texture(Key.key("minecraft:gui/sprites/hud/heart/container.png"), blankTexture));
                resourcePack.texture(Texture.texture(
                        Key.key("minecraft:gui/sprites/hud/heart/container_blinking.png"), blankTexture));
            }
        };
    }

    public enum FoodBarType {
        REGULAR(""),
        HUNGER("_hunger");

        private final String textureSuffix;

        FoodBarType(@NotNull String suffix) {
            this.textureSuffix = suffix;
        }

        @NotNull
        public String getTextureSuffix() {
            return textureSuffix;
        }
    }

    /**
     * Creates a module that hides the food bar of a specific type.
     *
     * @param type The type of food bar to hide
     * @return A module that, when applied to a resource pack, replaces the food bar textures with blank images
     * @throws IllegalArgumentException if type is null
     */
    @NotNull
    public static ResourcePackPart hideFood(@NotNull FoodBarType type) {

        return resourcePack -> {
            Writable blankTexture = Writable.inputStream(() -> ImageCreator.createBlankPngAsStream(182, 5));

            String basePath = "minecraft:gui/sprites/hud/food";
            String[] foodStates = {"full", "half", "empty"};

            // Replace all food state textures with blank textures
            for (String state : foodStates) {
                String texturePath = basePath + "_" + state + type.getTextureSuffix() + ".png";
                resourcePack.texture(Texture.texture(Key.key(texturePath), blankTexture));
            }
        };
    }
}