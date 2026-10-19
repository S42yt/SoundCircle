package de.s42.soundcircle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.function.Predicate;

public final class SoundSources {
    private SoundSources() {
    }

    public static @Nullable Entity find(Identifier id, SoundSource source, Vec3 pos) {
        String[] parts = id.getPath().split("\\.");
        String type = parts.length > 1 && parts[0].equals("entity") ? parts[1] : null;
        if ("player".equals(type) || (type == null && (source == SoundSource.PLAYERS || source == SoundSource.VOICE))) {
            return nearest(pos, 3.0, entity -> entity instanceof Player);
        }
        if (type != null && !type.equals("generic")) {
            Entity typed = nearest(pos, 3.0, entity -> MobFaces.typeOf(entity).equals(type));
            if (typed != null) {
                return typed;
            }
        }
        if (source == SoundSource.HOSTILE || source == SoundSource.NEUTRAL) {
            return nearest(pos, 2.0, entity -> entity instanceof LivingEntity && !(entity instanceof Player));
        }
        return null;
    }

    public static @Nullable Player nearestPlayer(Vec3 pos, double range) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            return null;
        }
        Player best = null;
        double bestDistance = range * range;
        for (Player player : level.players()) {
            double distance = player.getBoundingBox().getCenter().distanceToSqr(pos);
            if (distance < bestDistance) {
                best = player;
                bestDistance = distance;
            }
        }
        return best;
    }

    private static @Nullable Entity nearest(Vec3 pos, double range, Predicate<Entity> filter) {
        Minecraft mc = Minecraft.getInstance();
        ClientLevel level = mc.level;
        if (level == null) {
            return null;
        }
        AABB area = new AABB(pos.x - range, pos.y - range, pos.z - range, pos.x + range, pos.y + range, pos.z + range);
        Entity best = null;
        double bestDistance = range * range;
        for (Entity entity : level.getEntities((Entity) null, area, filter.and(entity -> entity != mc.player))) {
            double distance = entity.position().distanceToSqr(pos);
            if (distance < bestDistance) {
                best = entity;
                bestDistance = distance;
            }
        }
        return best;
    }
}
