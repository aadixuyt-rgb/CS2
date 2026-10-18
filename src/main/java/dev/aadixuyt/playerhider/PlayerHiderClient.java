package dev.aadixuyt.playerhider;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import org.lwjgl.glfw.GLFW;

import java.util.UUID;

public final class PlayerHiderClient implements ClientModInitializer {
    public static volatile boolean HIDE_PLAYERS = false;
    public static volatile UUID LAST_ATTACKED_PLAYER = null;
    public static volatile long LAST_ATTACK_TIME_MS = 0L;
    public static final long VISIBLE_WINDOW_MS = 5_000L;
    private static KeyBinding toggleKey;

    @Override
    public void onInitializeClient() {
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.playerhider.toggle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_H,
                "key.categories.misc"
        ));

        AttackEntityCallback.EVENT.register((player, world, hand, target, hitResult) -> {
            if (world.isClient && target instanceof PlayerEntity && target != player) {
                LAST_ATTACKED_PLAYER = target.getUuid();
                LAST_ATTACK_TIME_MS = System.currentTimeMillis();
            }
            return ActionResult.PASS;
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKey.wasPressed()) {
                HIDE_PLAYERS = !HIDE_PLAYERS;
                if (client.player != null) {
                    client.player.sendMessage(
                            Text.literal("Player Hider: " + (HIDE_PLAYERS ? "ON" : "OFF")),
                            true
                    );
                }
            }
        });
    }

    public static boolean isRecentlyAttacked(Entity entity) {
        return entity instanceof PlayerEntity
                && LAST_ATTACKED_PLAYER != null
                && LAST_ATTACKED_PLAYER.equals(entity.getUuid())
                && System.currentTimeMillis() - LAST_ATTACK_TIME_MS < VISIBLE_WINDOW_MS;
    }
}
