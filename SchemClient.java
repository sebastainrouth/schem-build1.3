package com.schembuilder;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public class SchemClient implements ClientModInitializer {
    public static final SchematicBuilder BUILDER = new SchematicBuilder();

    /** Right Shift by default; can be rebound in Options > Controls. */
    private static KeyMapping openGui;

    @Override
    public void onInitializeClient() {
        KeyMapping.Category category = KeyMapping.Category.register(Identifier.fromNamespaceAndPath("schembuilder", "main"));
        openGui = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.schembuilder.open", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT, category));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            BUILDER.tick(client);
            while (openGui.consumeClick()) {
                if (client.screen == null && client.player != null) {
                    client.setScreen(new SchemScreen(BUILDER));
                }
            }
        });
        SchemCommands.register(BUILDER);
    }
}
