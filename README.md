# ui-toolkit
This library provides the tools and explenations on how to achieve fully custom ui designs
seen on many of your favourite servers, including: 
* Custom inventory UI's
* Arbitrary HUD elements
* Images in Tablist and Scoreboard

and much more...


## Container Overlay
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

## HUD Elements

### Auto-sized hud element
This element will automatically generate a background and size itself to the
text content.

```java
// The end piece will be flipped horizonally to work on both ends
// End piece (<) Middle piece (=)
// Generated: [<=====>]
Writable endPiece = ...;
Wrtiable middlePiece = ...;
HudAutoBackground autoBackground = HudAutoBackground.autoBackground(
        endPiece,
        middlePiece
);

// Reminder to hide the bossbar if you want to use this element
player.showBossBar(BossBar.bossBar(player.setActionBar(), 0f, BossBar.Color.WHITE, BossBar.Overlay.PROGRESS));
```

### Hotbar hud element
This element is intended to be used with the provided bottom-guide.png texture.
It contains guides for the hotbar, hunger and health and xp bar.
```java
HotbarHud hotbarHud = HotbarHud.hotbarHud(
    Writable.resource(Testing.class.getClassLoader(), "bottom-guide.png")
);
```

### Utilities

## Hide Bossbar
Hiding the bossbar is needed when doing top aligned HUD elements. The following code
will replace the bossbar sprite with an empty texture for a specific colour.

```java
UIToolkit toolkit = ...;
toolkit.add(TextureUtils.hideBossBar(BossBar.Color.WHITE));
```
