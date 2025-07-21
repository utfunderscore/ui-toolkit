# UI-Toolkit

A comprehensive library for creating fully custom user interfaces in Minecraft.

## Table of Contents
1. [Introduction](#introduction)
2. [Features](#features)
3. [Getting Started](#getting-started)
4. [Container Overlays](#container-overlays)
5. [HUD Elements](#hud-elements)
    - [Auto-sized HUD Elements](#auto-sized-hud-elements)
    - [Hotbar HUD Elements](#hotbar-hud-elements)
6. [Utility Functions](#utility-functions)
    - [Hiding the Bossbar](#hiding-the-bossbar)
    - [Hiding the Health Bar](#hiding-the-health-bar)
7. [Examples](#examples)
8. [API Reference](#api-reference)

## Introduction

This library provides a toolkit to create custom UI elements in Minecraft, similar to those found on mccisland, Hoplite, etc.
You need a basic understanding of [creative](https://github.com/unnamed/creative) as it is used to generate the resource pack.

## Getting Started

To begin using UI-Toolkit, add it to your project dependencies:

```gradle
dependencies {
    implementation 'org.readutf.ui:ui-toolkit:{version}'
}
```

```maven
<dependency>
    <groupId>org.readutf.ui</groupId>
    <artifactId>ui-toolkit</artifactId>
    <version>{version}</version>
</dependency>
```

Then initialize the toolkit in your code:

```java
UIToolkit toolkit = UIToolkit.create();
// Add UI elements to the toolkit
// ...
// Generate resource pack when ready
toolkit.generateResourcePack();
```

## Container Overlays

Container overlays allow you to replace Minecraft's default container backgrounds (chests, inventories, etc.) with custom designs.

Overlay templates can be found in the [resources directory](src/main/resources/).

```java
// Create a menu overlay using your custom texture
MenuOverlay overlay = MenuOverlay.chest(
    Writable.resource(MyPlugin.class.getClassLoader(), "custom-ui.png")
);

// Add your overlay to the UI toolkit
UIToolkit toolkit = UIToolkit.create();
toolkit.add(overlay);

// Set any inventory title to this component to apply the custom overlay
Component title = overlay.getTitle();
inventory.setTitle(title);
```

## HUD Elements

HUD elements allow you to display custom graphics and information directly on the player's screen.

### Auto-sized HUD Elements

These elements automatically generate a background and size themselves based on the text content.

```java
// The end piece will be flipped horizontally to work on both ends
// End piece (<) Middle piece (=)
// Generated: [<=====>]
Writable endPiece = Writable.resource(MyPlugin.class.getClassLoader(), "end-piece.png");
Writable middlePiece = Writable.resource(MyPlugin.class.getClassLoader(), "middle-piece.png");

HudAutoBackground autoBackground = HudAutoBackground.autoBackground(
        endPiece,
        middlePiece
);

// Apply the auto-sized HUD element
toolkit.add(autoBackground);

// Note: Remember to hide the bossbar if you want to use this element
player.showBossBar(BossBar.bossBar(
    player.setActionBar(), 
    0f, 
    BossBar.Color.WHITE, 
    BossBar.Overlay.PROGRESS
));
```

### Hotbar HUD Elements

Hotbar HUD elements are specifically designed to customize the appearance of the player's hotbar area.

```java
// Create a hotbar HUD element using the provided template
HotbarHud hotbarHud = HotbarHud.hotbarHud(
    Writable.resource(MyPlugin.class.getClassLoader(), "bottom-guide.png")
);

// Add the hotbar HUD element to your toolkit
toolkit.add(hotbarHud);
```

## Utility Functions

UI-Toolkit includes several utility functions to help with common UI customization tasks.

### Hiding the Bossbar

Hiding the bossbar is necessary when implementing top-aligned HUD elements. The following code replaces the bossbar sprite with an empty texture for a specific color.

```java
// Create your toolkit
UIToolkit toolkit = UIToolkit.create();

// Hide the white bossbar
toolkit.add(TextureUtils.hideBossBar(BossBar.Color.WHITE));
```

### Hiding the Health Bar

This is useful when applying custom HUD elements above the hotbar area.

```java
// Create your toolkit
UIToolkit toolkit = UIToolkit.create();

// Hide the default health bar
toolkit.add(TextureUtils.hideHealthBar());
```