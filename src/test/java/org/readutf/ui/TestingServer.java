package org.readutf.ui;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.resource.ResourcePackRequest;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import org.readutf.ui.container.MenuOverlay;
import org.readutf.ui.hud.HotbarHud;
import org.readutf.ui.hud.HudAutoBackground;
import org.readutf.ui.utils.TextureUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.unnamed.creative.base.Writable;

import java.net.InetSocketAddress;

public class TestingServer {

    private static final Logger log = LoggerFactory.getLogger(TestingServer.class);

//    private static final MenuOverlay customUIOverlay =
//            MenuOverlay.chest(Writable.resource(TestingServer.class.getClassLoader(), "custom-ui.png"));

    private static final HudAutoBackground hudOverlay2 = HudAutoBackground.autoBackground(
            Writable.resource(TestingServer.class.getClassLoader(), "hud/end-bar.png"),
            Writable.resource(TestingServer.class.getClassLoader(), "hud/middle-bar.png"));

    private static final HotbarHud hotbarHud =
            HotbarHud.create(Writable.resource(TestingServer.class.getClassLoader(), "hud/bottom-guide.png"));

    public static void main(String[] args) {

        ResourcePackRequest packRequest = TextureManager.startPackServer(
//                customUIOverlay,
                hudOverlay2,
                hotbarHud,
                TextureUtils.hideBossBar(BossBar.Color.WHITE),
                TextureUtils.hideHealth(TextureUtils.HealthBarType.NORMAL),
                TextureUtils.hideFood(TextureUtils.FoodBarType.REGULAR));

        startServer(packRequest);
    }

    private static void startServer(ResourcePackRequest packRequest) {

        MinecraftServer server = MinecraftServer.init();
        server.start(new InetSocketAddress(25565));

        Instance instance = MinecraftServer.getInstanceManager().createInstanceContainer();
        instance.setGenerator(unit -> unit.modifier().fillHeight(0, 40, Block.STONE));

        MinecraftServer.getGlobalEventHandler().addListener(AsyncPlayerConfigurationEvent.class, e -> {
            e.setSpawningInstance(instance);
            e.getPlayer().sendResourcePacks(packRequest);
        });

        MinecraftServer.getGlobalEventHandler().addListener(PlayerSpawnEvent.class, e -> {
            Player player = e.getPlayer();
            player.teleport(new Pos(0, 41, 0));

            player.showBossBar(BossBar.bossBar(
                    hudOverlay2.getTitle(Component.text("test123123")),
                    0f,
                    BossBar.Color.WHITE,
                    BossBar.Overlay.PROGRESS
            ));
        });
    }
}
