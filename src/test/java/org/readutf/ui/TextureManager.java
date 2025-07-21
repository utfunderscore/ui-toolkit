package org.readutf.ui;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.resource.ResourcePackInfo;
import net.kyori.adventure.resource.ResourcePackRequest;
import org.readutf.ui.utils.TextureUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.unnamed.creative.BuiltResourcePack;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.serialize.minecraft.MinecraftResourcePackWriter;
import team.unnamed.creative.server.ResourcePackServer;

import java.io.IOException;
import java.net.URI;

import static net.kyori.adventure.text.Component.text;

public class TextureManager {

    private static final Logger log = LoggerFactory.getLogger(TextureManager.class);

    public static ResourcePackRequest startPackServer(Module... module) {

        ResourcePack resourcePack;
        try {
            resourcePack = new UIToolkit().add(module).build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        BuiltResourcePack builtPack = MinecraftResourcePackWriter.minecraft().build(resourcePack);

        ResourcePackServer packServer = null;
        try {
            packServer = ResourcePackServer.server()
                    .address("127.0.0.1", 7279)
                    .pack(builtPack)
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        packServer.start();

        String hash = builtPack.hash();
        String path = hash + ".zip";
        String url = "http://127.0.0.1:7279/" + path;

        log.info(url);

        return ResourcePackRequest.resourcePackRequest()
                .prompt(text("Pls download"))
                .replace(true)
                .packs(ResourcePackInfo.resourcePackInfo()
                        .uri(URI.create(url))
                        .hash(hash)
                        .build())
                .required(false)
                .build();
    }
}
