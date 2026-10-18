package dev.aadixuyt.playerhider.mixin;

import dev.aadixuyt.playerhider.PlayerHiderClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Predicate;

@Mixin(World.class)
public abstract class WorldTargetingMixin {
    @ModifyArg(
            method = "getOtherEntities",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getOtherEntities(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Box;Ljava/util/function/Predicate;)Ljava/util/List;"),
            index = 2,
            require = 0
    )
    private Predicate<Entity> playerhider$filterHiddenPlayers(Predicate<Entity> original) {
        if (!PlayerHiderClient.HIDE_PLAYERS || PlayerHiderClient.LOCKED_PLAYER == null) {
            return original;
        }
        return entity -> original.test(entity)
                && (!(entity instanceof PlayerEntity) || PlayerHiderClient.isLockedPlayer(entity));
    }
}
