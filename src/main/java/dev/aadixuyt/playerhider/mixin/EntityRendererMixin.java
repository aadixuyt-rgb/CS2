package dev.aadixuyt.playerhider.mixin;

import dev.aadixuyt.playerhider.PlayerHiderClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void playerhider$hideOtherPlayers(
            Entity entity,
            float yaw,
            float tickDelta,
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light,
            CallbackInfo ci
    ) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (PlayerHiderClient.HIDE_PLAYERS
                && entity instanceof PlayerEntity
                && entity != client.player
                && !PlayerHiderClient.isRecentlyAttacked(entity)) {
            ci.cancel();
        }
    }
}
