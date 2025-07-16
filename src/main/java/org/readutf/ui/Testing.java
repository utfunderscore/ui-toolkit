package org.readutf.ui;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.resource.ResourcePackInfo;
import net.kyori.adventure.resource.ResourcePackRequest;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.minestom.server.MinecraftServer;
import net.minestom.server.component.DataComponents;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.TooltipDisplay;
import org.jetbrains.annotations.NotNull;
import org.readutf.ui.container.MenuOverlay;
import org.readutf.ui.hud.HudOverlay;
import org.readutf.ui.utils.TextHelper;
import org.readutf.ui.utils.TextureUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.unnamed.creative.BuiltResourcePack;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.serialize.minecraft.MinecraftResourcePackWriter;
import team.unnamed.creative.server.ResourcePackServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Set;

import static net.kyori.adventure.text.Component.text;

public class Testing {

    private static final Logger log = LoggerFactory.getLogger(Testing.class);

    private static final MenuOverlay customUIOverlay = MenuOverlay.chest(
            Writable.resource(Testing.class.getClassLoader(), "custom-ui.png")
    );

    private static final HudOverlay hudOverlay1 = HudOverlay.top(
            Component.text("This is quite a long title"),
            Writable.resource(Testing.class.getClassLoader(), "test-bar.png")
    );

    private static final HudOverlay hudOverlay2 = HudOverlay.top(
            Component.text("Short one"),
            Writable.resource(Testing.class.getClassLoader(), "test-bar.png")
    );

    public static void main(String[] args) throws IOException {

        ResourcePack resourcePack = new UIToolkit()
                .add(customUIOverlay, hudOverlay1, hudOverlay2, TextureUtils.hideBossBar(BossBar.Color.WHITE))
                .build();

        BuiltResourcePack builtPack = MinecraftResourcePackWriter.minecraft().build(resourcePack);

        PackInfo result = startPackServer(builtPack);
        startServer(result.url(), result.hash());

        TextComponent test = text("hello").append(text("world").append(text("hello")));

        System.out.println(TextHelper.getComponentWidth(test, true, false));
    }

    private static @NotNull PackInfo startPackServer(BuiltResourcePack builtPack) throws IOException {
        ResourcePackServer packServer = ResourcePackServer.server()
                .address("127.0.0.1", 7279)
                .pack(builtPack)
                .build();

        packServer.start();

        String hash = builtPack.hash();
        String path = hash + ".zip";
        String url = "http://127.0.0.1:7279/" + path;

        log.info(url);
        return new PackInfo(hash, url);
    }

    private record PackInfo(String hash, String url) {
    }

    private static void startServer(String url, String hash) {

        MinecraftServer server = MinecraftServer.init();
        server.start(new InetSocketAddress(25565));

        Instance instance = MinecraftServer.getInstanceManager().createInstanceContainer();
        instance.setGenerator(unit -> unit.modifier().fillHeight(0, 40, Block.STONE));

        MinecraftServer.getGlobalEventHandler().addListener(AsyncPlayerConfigurationEvent.class, e -> {
            e.setSpawningInstance(instance);
        });

        MinecraftServer.getGlobalEventHandler().addListener(PlayerSpawnEvent.class, e -> {
            Player player = e.getPlayer();
            player.teleport(new Pos(0, 41, 0));

            Inventory inventory = new Inventory(
                    InventoryType.CHEST_4_ROW, customUIOverlay.getTitle());

            inventory.setItemStack(
                    8,
                    ItemStack.of(Material.STONE)
                            .with(DataComponents.TOOLTIP_DISPLAY, new TooltipDisplay(true, Set.of())));


            player.showBossBar(BossBar.bossBar(hudOverlay1.getTitle(), 0.5f, BossBar.Color.WHITE, BossBar.Overlay.PROGRESS));
            player.showBossBar(BossBar.bossBar(hudOverlay2.getTitle(), 0.5f, BossBar.Color.WHITE, BossBar.Overlay.PROGRESS));

            player.openInventory(inventory);

            player.sendResourcePacks(ResourcePackRequest.resourcePackRequest()
                    .prompt(text("Pls download"))
                    .replace(true)
                    .packs(ResourcePackInfo.resourcePackInfo()
                            .uri(URI.create(url))
                            .hash(hash)
                            .build())
                    .required(false));
        });
    }
}
