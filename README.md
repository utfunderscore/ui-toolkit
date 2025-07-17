# ui-toolkit
This library provides the tools and explenations on how to achieve fully custom ui designs
seen on many of your favourite servers, including: 
* Custom inventory UI's
* Arbitrary HUD elements
* Images in Tablist and Scoreboard

and much more...


### Container Overlay
Overlay templates can be found [here](src/main/resources/)
```java
// Create a menu overlay using your texture
MenuOverlay overlay = MenuOverlay.chest(
    Writable.resource(Testing.class.getClassLoader(), "custom-ui.png")
);

// Add your overlay to the UI and generate a resource pack
UIToolkit toolkit = ...;
toolkit.add(overlay);

// Set any inventory title to this component to apply
Component title = overlay.getTitle();
```

### Hide Bossbar
 Hiding the bossbar is needed when doing top aligned HUD elements. The following code
will replace the bossbar sprite with an empty texture for a specific colour.

```java
UIToolkit toolkit = ...;
toolkit.add(TextureUtils.hideBossBar(BossBar.Color.WHITE));
```

### HUD Elements

Hud elements work by using custom characters in the bossbar, 
and negative space fonts shifting the text into the correct positions

#### Option 1 - Fixed background
This option uses a single fixed background image for the HUD background.
```java
private static final HudOverlay hudOverlay1 = HudOverlay.top(
        Component.text("This is quite a long title"),
        Writable.resource(Testing.class.getClassLoader(), "hud/top-background.png")
);
```

#### Option 2 - Dynamic background
This option uses a ending image and a 
