package de.s42.soundcircle;

import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public final class Indicator {
    private static final double MERGE_DISTANCE_SQR = 9.0;

    public final long born;
    public final @Nullable Entity entity;
    public final @Nullable Component name;
    public SoundKind kind;
    public SoundIcon icon;
    public SoundIcon.@Nullable Glyph badge;
    public int color;
    public @Nullable Component label;
    public long refreshed;
    private Vec3 lastPosition;

    Indicator(SoundKind kind, SoundIcon icon, SoundIcon.@Nullable Glyph badge, int color, @Nullable Entity entity,
              @Nullable Component name, @Nullable Component label, Vec3 position, long now) {
        this.kind = kind;
        this.icon = icon;
        this.badge = badge;
        this.color = color;
        this.entity = entity;
        this.name = name;
        this.label = label;
        this.lastPosition = position;
        this.born = now;
        this.refreshed = now;
    }

    public Vec3 position() {
        if (entity != null && !entity.isRemoved()) {
            lastPosition = entity.getBoundingBox().getCenter();
        }
        return lastPosition;
    }

    boolean matches(Indicator other) {
        if (other.entity != null && entity == other.entity) {
            return true;
        }
        return kind == other.kind && icon.equals(other.icon) && position().distanceToSqr(other.lastPosition) < MERGE_DISTANCE_SQR;
    }

    void merge(Indicator newer) {
        if (newer.kind.priority() >= kind.priority()) {
            kind = newer.kind;
            icon = newer.icon;
            label = newer.label;
            color = newer.color;
        }
        if (newer.badge != null || !(icon instanceof SoundIcon.Glyph)) {
            badge = newer.badge;
        }
        lastPosition = newer.lastPosition;
        refreshed = newer.refreshed;
    }

    double score(Vec3 eye, long now, int maxDistance) {
        float closeness = 1.0f - Mth.clamp((float) (position().distanceTo(eye) / maxDistance), 0.0f, 1.0f);
        float age = Mth.clamp((now - refreshed) / 3000.0f, 0.0f, 1.0f);
        return kind.priority() + closeness - age;
    }
}
