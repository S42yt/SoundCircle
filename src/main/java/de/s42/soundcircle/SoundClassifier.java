package de.s42.soundcircle;

import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

import java.util.Set;

public final class SoundClassifier {
    private static final Set<String> DANGER = Set.of(
            "entity.creeper.primed",
            "entity.tnt.primed",
            "entity.warden.sonic_charge",
            "entity.warden.sonic_boom",
            "entity.warden.emerge",
            "entity.warden.roar",
            "entity.ghast.warn",
            "entity.ghast.shoot",
            "entity.blaze.shoot",
            "entity.wither.shoot",
            "entity.wither.spawn",
            "entity.evoker.prepare_attack",
            "entity.evoker.prepare_summon",
            "entity.breeze.charge",
            "entity.ravager.roar",
            "entity.elder_guardian.curse"
    );
    private static final String[] COMBAT = {
            "attack", "hurt", "arrow", "crossbow", "trident", "shield", "bow.", "projectile", "mace", "death"
    };
    private static final String[] ENVIRONMENT = {
            "ambient", "weather", "water", "lava", "fire", "rain", "thunder", "bubble", "portal"
    };
    private static final String[] AMBIENT_NOISE = {
            "ambient", "idle", "drip", "crackle", "chime", "bubble_column", "lava.pop", "fire.", "portal", "beacon", "conduit",
            "rain", "flowing", "loop", "mood"
    };

    private final MobFaces faces = new MobFaces();

    public static boolean isNoise(Identifier id, SoundSource source) {
        if (source == SoundSource.AMBIENT || source == SoundSource.WEATHER) {
            return true;
        }
        String path = id.getPath();
        if (path.startsWith("entity.")) {
            return false;
        }
        for (String word : AMBIENT_NOISE) {
            if (path.contains(word)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isEating(Identifier id) {
        String path = id.getPath();
        return path.contains(".eat") || path.contains("burp") || path.contains("drink");
    }

    public static SoundIcon.@Nullable Glyph action(Identifier id, SoundKind kind) {
        String path = id.getPath();
        if (path.contains("drink") || path.contains("potion") || path.contains("brewing") || path.contains("bottle")) {
            return SoundIcon.Glyph.POTION;
        }
        if (isEating(id)) {
            return kind == SoundKind.ANIMALS ? SoundIcon.Glyph.GRAZE : SoundIcon.Glyph.FOOD;
        }
        if (path.contains("explode") || path.contains("firework_rocket.blast") || path.contains("firework_rocket.large_blast")) {
            return SoundIcon.Glyph.BOOM;
        }
        if (path.contains("thunder") || path.contains("lightning")) {
            return SoundIcon.Glyph.BOLT;
        }
        if (path.contains("levelup") || path.contains("experience_orb") || path.contains("enchant")) {
            return SoundIcon.Glyph.XP;
        }
        if (path.contains("jump") || path.contains("squish") || path.contains(".hop") || path.contains("bounce")) {
            return SoundIcon.Glyph.BOUNCE;
        }
        if (path.contains("door") || path.contains("fence_gate")) {
            return SoundIcon.Glyph.DOOR;
        }
        if (path.contains("chest") || path.contains("barrel") || path.contains("shulker_box")) {
            return SoundIcon.Glyph.CHEST;
        }
        if (path.contains("arrow") || path.contains("crossbow") || path.contains("bow.shoot") || path.contains(".throw") || path.contains(".shoot")) {
            return SoundIcon.Glyph.ARROW;
        }
        if (path.contains("fire") || path.contains("lava") || path.contains("burn") || path.contains("flintandsteel") || path.contains("campfire")) {
            return SoundIcon.Glyph.FIRE;
        }
        if (path.contains("hurt") || path.contains("death")) {
            return SoundIcon.Glyph.HEART;
        }
        if (path.contains("attack") || path.contains("sweep") || path.contains("crit") || path.contains("knockback")) {
            return SoundIcon.Glyph.SWORDS;
        }
        return null;
    }

    public static SoundKind classify(Identifier id, SoundSource source) {
        String path = id.getPath();
        if (DANGER.contains(path) || path.contains("explode")) {
            return SoundKind.DANGER;
        }
        if (isEating(id)) {
            return source == SoundSource.NEUTRAL || source == SoundSource.HOSTILE ? SoundKind.ANIMALS : SoundKind.PLAYERS;
        }
        if (source == SoundSource.RECORDS || path.startsWith("music") || path.startsWith("block.note_block")) {
            return SoundKind.MUSIC;
        }
        if (path.endsWith("step") || path.contains(".step") || path.contains("footstep")) {
            return switch (source) {
                case HOSTILE -> SoundKind.HOSTILE;
                case NEUTRAL -> SoundKind.ANIMALS;
                default -> SoundKind.FOOTSTEPS;
            };
        }
        for (String word : COMBAT) {
            if (path.contains(word)) {
                return SoundKind.COMBAT;
            }
        }
        for (String word : ENVIRONMENT) {
            if (path.startsWith(word) || path.startsWith("block." + word)) {
                return SoundKind.ENVIRONMENT;
            }
        }
        return switch (source) {
            case HOSTILE -> SoundKind.HOSTILE;
            case NEUTRAL -> SoundKind.ANIMALS;
            case PLAYERS, VOICE -> SoundKind.PLAYERS;
            case BLOCKS -> SoundKind.BLOCKS;
            default -> SoundKind.ENVIRONMENT;
        };
    }

    public SoundIcon icon(Identifier id, SoundKind kind, @Nullable Entity entity, boolean heads) {
        SoundIcon.Glyph action = action(id, kind);
        if (heads && kind != SoundKind.FOOTSTEPS && kind != SoundKind.BLOCKS) {
            SoundIcon entityIcon = entityIcon(id, entity);
            if (entityIcon != null) {
                return entityIcon;
            }
        }
        return action != null ? action : kind.glyph();
    }

    private @Nullable SoundIcon entityIcon(Identifier id, @Nullable Entity entity) {
        if (entity instanceof LivingEntity) {
            SoundIcon.Face face = faces.entity(entity);
            return face != null ? face : new SoundIcon.Model(MobFaces.typeOf(entity));
        }
        if (entity != null) {
            ItemStack stack = entity instanceof ItemEntity item ? item.getItem() : entity.getPickResult();
            if (stack != null && !stack.isEmpty()) {
                return new SoundIcon.ItemIcon(stack.getItem());
            }
        }
        String[] parts = id.getPath().split("\\.");
        if (parts.length > 1 && parts[0].equals("entity") && !parts[1].equals("player")) {
            return faces.mob(parts[1]);
        }
        return null;
    }

    public void clear() {
        faces.clear();
    }
}
