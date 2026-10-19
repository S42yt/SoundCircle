package de.s42.soundcircle;

import com.mojang.blaze3d.platform.InputConstants;
import de.s42.soundcircle.config.ConfigScreen;
import de.s42.soundcircle.config.SoundCircleConfig;
import de.s42.soundcircle.render.CircleHud;
import de.s42.soundcircle.render.CirclePipeline;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class SoundCircle implements ClientModInitializer {
    public static final String MOD_ID = "soundcircle";

    private final OwnSounds ownSounds = new OwnSounds();
    private final SoundTracker tracker = new SoundTracker(ownSounds);

    @Override
    public void onInitializeClient() {
        SoundCircleConfig.HANDLER.load();
        CirclePipeline.init();
        ownSounds.register();

        KeyMapping.Category category = KeyMapping.Category.register(Identifier.fromNamespaceAndPath(MOD_ID, "main"));
        KeyMapping toggle = Compat.registerKey(new KeyMapping("key.soundcircle.toggle", InputConstants.UNKNOWN.getValue(), category));
        KeyMapping settings = Compat.registerKey(new KeyMapping("key.soundcircle.config", InputConstants.UNKNOWN.getValue(), category));

        HudElementRegistry.addLast(Identifier.fromNamespaceAndPath(MOD_ID, "circle"), new CircleHud(tracker)::render);
        ClientLifecycleEvents.CLIENT_STARTED.register(client -> client.getSoundManager().addListener(tracker));
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> tracker.clear());
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            ownSounds.tick(client);
            while (toggle.consumeClick()) {
                SoundCircleConfig config = SoundCircleConfig.get();
                config.enabled = !config.enabled;
                SoundCircleConfig.HANDLER.save();
                tracker.clear();
                Compat.overlayMessage(client, Component.translatable(config.enabled ? "soundcircle.toggle.on" : "soundcircle.toggle.off"));
            }
            while (settings.consumeClick()) {
                Compat.openScreen(client, ConfigScreen.create(null));
            }
        });
    }
}
