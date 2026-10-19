package de.s42.soundcircle.config;

import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import de.s42.soundcircle.SoundCircle;
import de.s42.soundcircle.SoundKind;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.Identifier;

import java.awt.Color;
import java.util.LinkedHashMap;
import java.util.Map;

public class SoundCircleConfig {
    public static final ConfigClassHandler<SoundCircleConfig> HANDLER = ConfigClassHandler.createBuilder(SoundCircleConfig.class)
            .id(Identifier.fromNamespaceAndPath(SoundCircle.MOD_ID, "config"))
            .serializer(handler -> GsonConfigSerializerBuilder.create(handler)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve(SoundCircle.MOD_ID + ".json5"))
                    .setJson5(true)
                    .build())
            .build();

    @SerialEntry
    public boolean enabled = true;
    @SerialEntry
    public int radius = 72;
    @SerialEntry
    public int iconScale = 1;
    @SerialEntry
    public float opacity = 0.85f;
    @SerialEntry
    public boolean showRing = false;
    @SerialEntry
    public float ringOpacity = 0.2f;
    @SerialEntry
    public int maxDistance = 16;
    @SerialEntry
    public float duration = 2.5f;
    @SerialEntry
    public int maxIndicators = 10;
    @SerialEntry
    public boolean showIcons = true;
    @SerialEntry
    public boolean mobHeads = true;
    @SerialEntry
    public boolean showLabels = false;
    @SerialEntry
    public boolean showDistance = false;
    @SerialEntry
    public boolean showVertical = true;
    @SerialEntry
    public boolean showOwnSounds = false;
    @SerialEntry
    public boolean ignoreAmbient = true;
    @SerialEntry
    public boolean showPlayerNames = true;
    @SerialEntry
    public boolean pulseDanger = true;
    @SerialEntry
    public Map<String, Boolean> kindEnabled = new LinkedHashMap<>();
    @SerialEntry
    public Map<String, Color> kindColor = new LinkedHashMap<>();

    public static SoundCircleConfig get() {
        return HANDLER.instance();
    }

    public boolean isEnabled(SoundKind kind) {
        return kindEnabled.getOrDefault(kind.key(), true);
    }

    public Color color(SoundKind kind) {
        Color color = kindColor.get(kind.key());
        return color != null ? color : new Color(kind.defaultColor());
    }
}
