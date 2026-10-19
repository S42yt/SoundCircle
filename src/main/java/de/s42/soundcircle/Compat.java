package de.s42.soundcircle;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
//? if >=26.1 {
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
//?} else {
/*import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
*///?}

public final class Compat {
    private Compat() {
    }

    public static KeyMapping registerKey(KeyMapping mapping) {
        //? if >=26.1 {
        return KeyMappingHelper.registerKeyMapping(mapping);
        //?} else {
        /*return KeyBindingHelper.registerKeyBinding(mapping);
        *///?}
    }

    public static void overlayMessage(Minecraft client, Component message) {
        if (client.player != null) {
            //? if >=26.1 {
            client.player.sendOverlayMessage(message);
            //?} else {
            /*client.player.displayClientMessage(message, true);
            *///?}
        }
    }

    public static void openScreen(Minecraft client, Screen screen) {
        //? if >=26.2 {
        client.gui.setScreen(screen);
        //?} else {
        /*client.setScreen(screen);
        *///?}
    }
}
