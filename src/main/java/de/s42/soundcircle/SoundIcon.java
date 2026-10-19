package de.s42.soundcircle;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.Nullable;

import java.util.List;

public sealed interface SoundIcon {
    record Glyph(int id) implements SoundIcon {
        public static final Glyph FEET = new Glyph(0);
        public static final Glyph PERSON = new Glyph(1);
        public static final Glyph SWORDS = new Glyph(2);
        public static final Glyph SKULL = new Glyph(3);
        public static final Glyph WARNING = new Glyph(4);
        public static final Glyph PAW = new Glyph(5);
        public static final Glyph CUBE = new Glyph(6);
        public static final Glyph DROP = new Glyph(7);
        public static final Glyph NOTE = new Glyph(8);
        public static final Glyph UP = new Glyph(9);
        public static final Glyph DOWN = new Glyph(10);
        public static final Glyph FOOD = new Glyph(11);
        public static final Glyph GRAZE = new Glyph(12);
        public static final Glyph DOOR = new Glyph(13);
        public static final Glyph CHEST = new Glyph(14);
        public static final Glyph POTION = new Glyph(15);
        public static final Glyph ARROW = new Glyph(16);
        public static final Glyph FIRE = new Glyph(17);
        public static final Glyph BOLT = new Glyph(18);
        public static final Glyph XP = new Glyph(19);
        public static final Glyph BOUNCE = new Glyph(20);
        public static final Glyph HEART = new Glyph(21);
        public static final Glyph BOOM = new Glyph(22);
        public static final int COUNT = 23;
    }

    record Model(String type) implements SoundIcon {
    }

    record ItemIcon(Item item) implements SoundIcon {
    }

    record Face(Identifier texture, int textureWidth, int textureHeight, int width, int height, List<Layer> layers,
                boolean baby) implements SoundIcon {
    }

    record Layer(@Nullable Identifier texture, int u, int v, int width, int height, int x, int y) {
    }
}
