package de.s42.soundcircle;

import de.s42.soundcircle.config.SoundCircleConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEventListener;
import net.minecraft.client.sounds.WeighedSoundEvents;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Util;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class SoundTracker implements SoundEventListener {
    private static final float MIN_AUDIBILITY = 0.03f;

    private final SoundClassifier classifier = new SoundClassifier();
    private final List<Indicator> indicators = new ArrayList<>();
    private final OwnSounds ownSounds;

    public SoundTracker(OwnSounds ownSounds) {
        this.ownSounds = ownSounds;
    }

    @Override
    public void onPlaySound(SoundInstance sound, WeighedSoundEvents soundEvent, float range) {
        SoundCircleConfig config = SoundCircleConfig.get();
        LocalPlayer player = Minecraft.getInstance().player;
        if (!config.enabled || player == null || sound.isRelative() || sound.getAttenuation() == SoundInstance.Attenuation.NONE) {
            return;
        }
        SoundSource source = sound.getSource();
        if (source == SoundSource.MUSIC || source == SoundSource.UI || source == SoundSource.MASTER) {
            return;
        }
        Identifier id = sound.getIdentifier();
        if (config.ignoreAmbient && SoundClassifier.isNoise(id, source)) {
            return;
        }
        Vec3 pos = new Vec3(sound.getX(), sound.getY(), sound.getZ());
        double distance = player.getEyePosition().distanceTo(pos);
        if (distance > config.maxDistance || distance > range) {
            return;
        }
        if (sound.getVolume() * (1.0f - (float) (distance / Math.max(range, 1.0f))) < MIN_AUDIBILITY) {
            return;
        }
        Entity entity = SoundSources.find(id, source, pos);
        if (!config.showOwnSounds && ownSounds.isOwn(player, id, pos, source, entity)) {
            return;
        }
        SoundKind kind = SoundClassifier.classify(id, source);
        if (!config.isEnabled(kind)) {
            return;
        }
        long now = Util.getMillis();
        SoundIcon icon = classifier.icon(id, kind, entity, config.mobHeads);
        SoundIcon.Glyph badge = icon instanceof SoundIcon.Glyph ? null : SoundClassifier.action(id, kind);
        int color = kind == SoundKind.BLOCKS ? BlockColors.of(id, pos) : -1;
        Indicator heard = new Indicator(kind, icon, badge, color, entity, entity instanceof Player other ? other.getName() : null,
                soundEvent.getSubtitle(), pos, now);

        for (Indicator indicator : indicators) {
            if (indicator.matches(heard)) {
                indicator.merge(heard);
                return;
            }
        }
        indicators.add(heard);
        Vec3 eye = player.getEyePosition();
        while (indicators.size() > config.maxIndicators) {
            indicators.remove(indicators.stream()
                    .min(Comparator.comparingDouble(indicator -> indicator.score(eye, now, config.maxDistance)))
                    .orElseThrow());
        }
    }

    public List<Indicator> tick(long now, long lifetime) {
        indicators.removeIf(indicator -> now - indicator.refreshed > lifetime);
        return indicators;
    }

    public void clear() {
        indicators.clear();
        classifier.clear();
    }
}
