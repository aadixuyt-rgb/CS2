package dev.aadixuyt.playerhider.mixin;

import dev.aadixuyt.playerhider.PlayerHiderClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Predicate;

@Mixin(MinecraftClient.class)
public abstract class WorldTargetingMixin {
    @ModifyArg(
        method = "findCrosshairTarget",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/projectile/ProjectileUtil;raycast(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Ljava/util/function/Predicate;D)Lnet/minecraft/util/hit/EntityHitResult;"
        ),
        index = 4,
        require = 0
    )
    private Predicate<Entity> playerhider$skipOtherPlayers(
            Predicate<Entity> original) {
        if (!PlayerHiderClient.HIDE_PLAYERS
                || PlayerHiderClient.LOCKED_PLAYER == null) {
            return original;
        }

        return entity -> original.test(entity)
                && (!(entity instanceof PlayerEntity)
                || PlayerHiderClient.isLockedPlayer(entity));
    }
}
