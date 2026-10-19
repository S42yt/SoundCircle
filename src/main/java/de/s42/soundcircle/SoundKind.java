package de.s42.soundcircle;

import net.minecraft.network.chat.Component;

public enum SoundKind {
    DANGER(0xFF5A4E, SoundIcon.Glyph.WARNING, 5f),
    HOSTILE(0xFFA26E, SoundIcon.Glyph.SKULL, 3f),
    COMBAT(0xFFD584, SoundIcon.Glyph.SWORDS, 2.5f),
    PLAYERS(0xAED6FF, SoundIcon.Glyph.PERSON, 2f),
    FOOTSTEPS(0xFFFFFF, SoundIcon.Glyph.FEET, 1.5f),
    ANIMALS(0xB8EDA8, SoundIcon.Glyph.PAW, 1f),
    BLOCKS(0xEBD6B4, SoundIcon.Glyph.CUBE, 1f),
    ENVIRONMENT(0xA4E7F2, SoundIcon.Glyph.DROP, 0.5f),
    MUSIC(0xDBBDFF, SoundIcon.Glyph.NOTE, 0.5f);

    private final int defaultColor;
    private final SoundIcon.Glyph glyph;
    private final float priority;

    SoundKind(int defaultColor, SoundIcon.Glyph glyph, float priority) {
        this.defaultColor = defaultColor;
        this.glyph = glyph;
        this.priority = priority;
    }

    public float priority() {
        return priority;
    }

    public int defaultColor() {
        return defaultColor;
    }

    public SoundIcon.Glyph glyph() {
        return glyph;
    }

    public String key() {
        return name().toLowerCase();
    }

    public Component displayName() {
        return Component.translatable("soundcircle.kind." + key());
    }

    public Component description() {
        return Component.translatable("soundcircle.kind." + key() + ".desc");
    }
}
