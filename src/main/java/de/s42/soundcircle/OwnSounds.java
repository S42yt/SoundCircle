package de.s42.soundcircle;

import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Util;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public final class OwnSounds {
    private static final long INTERACTION_WINDOW = 600L;
    private static final long BLOCK_WINDOW = 1500L;
    private static final long ATTACK_WINDOW = 1000L;

    private final Map<BlockPos, Long> blocks = new HashMap<>();
    private final Map<Integer, Long> attacked = new HashMap<>();
    private long lastInteraction;
    private Vec3 lastTarget = Vec3.ZERO;

    public void register() {
        AttackBlockCallback.EVENT.register((player, level, hand, pos, direction) -> {
            if (level.isClientSide()) {
                markBlock(pos);
            }
            return InteractionResult.PASS;
        });
        UseBlockCallback.EVENT.register((player, level, hand, hit) -> {
            if (level.isClientSide()) {
                markBlock(hit.getBlockPos());
                markBlock(hit.getBlockPos().relative(hit.getDirection()));
            }
            return InteractionResult.PASS;
        });
        AttackEntityCallback.EVENT.register((player, level, hand, entity, hit) -> {
            if (level.isClientSide()) {
                attacked.put(entity.getId(), Util.getMillis());
            }
            return InteractionResult.PASS;
        });
    }

    public void tick(Minecraft client) {
        long now = Util.getMillis();
        if (client.hitResult != null && (client.options.keyAttack.isDown() || client.options.keyUse.isDown())) {
            lastInteraction = now;
            lastTarget = client.hitResult.getLocation();
            if (client.hitResult instanceof BlockHitResult hit && hit.getType() == HitResult.Type.BLOCK) {
                markBlock(hit.getBlockPos());
                markBlock(hit.getBlockPos().relative(hit.getDirection()));
            }
        }
        blocks.values().removeIf(time -> now - time > BLOCK_WINDOW);
        attacked.values().removeIf(time -> now - time > ATTACK_WINDOW);
    }

    public boolean isOwn(LocalPlayer player, Identifier id, Vec3 pos, SoundSource source, @Nullable Entity entity) {
        long now = Util.getMillis();
        if (player.getBoundingBox().inflate(1.25).contains(pos)) {
            return true;
        }
        Entity vehicle = player.getVehicle();
        if (entity == player || (entity != null && entity == vehicle) || (vehicle != null && vehicle.getBoundingBox().inflate(1.0).contains(pos))) {
            return true;
        }
        if (entity instanceof Projectile projectile && projectile.getOwner() == player) {
            return true;
        }
        String path = id.getPath();
        if (entity != null && (path.contains("hurt") || path.contains("death")) && now - attacked.getOrDefault(entity.getId(), 0L) < ATTACK_WINDOW) {
            return true;
        }
        if ((source == SoundSource.BLOCKS || source == SoundSource.PLAYERS) && touchedBlockNear(pos, now)) {
            return true;
        }
        if (source == SoundSource.PLAYERS || source == SoundSource.VOICE || path.startsWith("entity.player.") || path.startsWith("entity.generic.")) {
            return SoundSources.nearestPlayer(pos, 4.0) == player;
        }
        return false;
    }

    private boolean touchedBlockNear(Vec3 pos, long now) {
        BlockPos center = BlockPos.containing(pos);
        for (BlockPos near : BlockPos.betweenClosed(center.offset(-1, -1, -1), center.offset(1, 1, 1))) {
            if (now - blocks.getOrDefault(near, 0L) < BLOCK_WINDOW) {
                return true;
            }
        }
        return now - lastInteraction < INTERACTION_WINDOW && lastTarget.distanceToSqr(pos) < 4.0;
    }

    private void markBlock(BlockPos pos) {
        blocks.put(pos.immutable(), Util.getMillis());
    }
}
