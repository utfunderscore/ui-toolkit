package org.readutf.ui.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.*;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for calculating the pixel width of text components.
 * Uses character width data loaded from a JSON file to accurately measure text dimensions.
 */
public class TextHelper {
    private static final Logger LOGGER = Logger.getLogger(TextHelper.class.getName());

    // Maps for storing character width information
    private static final HashMap<Character, Double> WIDTHS = new HashMap<>();
    private static final HashMap<Character, Double> BOLD_OFFSETS = new HashMap<>();
    private static final HashMap<Character, Double> SHADOW_OFFSETS = new HashMap<>();

    static {
        initializeWidths();
    }

    /**
     * Calculates the pixel width of a Component, considering text decorations.
     *
     * @param component    The component to measure
     * @param boldOffset   Whether to include bold offset in calculation
     * @param shadowOffset Whether to include shadow offset in calculation
     * @return The calculated width of the component in pixels
     */
    public static double getComponentWidth(Component component, boolean boldOffset, boolean shadowOffset) {
        if (component == null) {
            return 0.0D;
        }

        if (!(component instanceof TextComponent textComponent)) {
            return 0.0D;
        }

        double width = 0.0D;
        String content = textComponent.content();

        for (int index = 0; index < content.length(); index++) {
            char character = content.charAt(index);
            width += WIDTHS.getOrDefault(character, 0.0D);

            // Add bold offset if applicable
            if (boldOffset && component.hasDecoration(TextDecoration.BOLD)) {
                width += BOLD_OFFSETS.getOrDefault(character, 0.0D);
            }

            // Add shadow offset if applicable (when shadow color exists and is not transparent)
            ShadowColor shadowColor = component.shadowColor();
            if (shadowOffset && (shadowColor == null || shadowColor.alpha() > 0)) {
                width += SHADOW_OFFSETS.getOrDefault(character, 0.0D);
            }
        }

        // Recursively add width of all child components
        for (Component child : textComponent.children()) {
            width += getComponentWidth(child, boldOffset, shadowOffset);
        }

        return width;
    }

    /**
     * Data class representing width information for a single character.
     */
    private static class CharWidths {
        public double width;
        public double bold_offset;
        public double shadow_offset;
    }

    /**
     * Initializes the width maps by loading character data from the font.json resource file.
     */
    private static void initializeWidths() {
        try (InputStream stream = TextHelper.class.getResourceAsStream("/font.json")) {
            if (stream == null) {
                LOGGER.severe("Resource /font.json not found");
                throw new RuntimeException("Resource /font.json not found");
            }

            String json = new String(stream.readAllBytes());
            Gson gson = new Gson();

            // Parse root object as Map<String, Object>
            Type rootType = new TypeToken<Map<String, Object>>() {}.getType();
            Map<String, Object> root = gson.fromJson(json, rootType);

            if (root == null) {
                LOGGER.severe("Failed to parse font.json");
                return;
            }

            // Parse chars map
            Object charsObj = root.get("chars");
            if (charsObj != null) {
                Type charsType = new TypeToken<Map<String, CharWidths>>() {}.getType();
                Map<String, CharWidths> chars = gson.fromJson(gson.toJson(charsObj), charsType);

                // Add all chars to the respective maps
                for (Map.Entry<String, CharWidths> entry : chars.entrySet()) {
                    String key = entry.getKey();
                    if (key != null && !key.isEmpty()) {
                        char character = key.charAt(0);
                        CharWidths cw = entry.getValue();

                        if (cw != null) {
                            WIDTHS.put(character, cw.width);
                            BOLD_OFFSETS.put(character, cw.bold_offset);
                            SHADOW_OFFSETS.put(character, cw.shadow_offset);
                        }
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error loading font width data", e);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error initializing text width data", e);
        }
    }
}