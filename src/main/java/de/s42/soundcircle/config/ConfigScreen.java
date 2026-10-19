package de.s42.soundcircle.config;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.ColorControllerBuilder;
import dev.isxander.yacl3.api.controller.FloatSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import de.s42.soundcircle.SoundKind;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.awt.Color;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class ConfigScreen {
    private ConfigScreen() {
    }

    public static Screen create(Screen parent) {
        return YetAnotherConfigLib.create(SoundCircleConfig.HANDLER, (defaults, config, builder) -> {
            ConfigCategory.Builder general = ConfigCategory.createBuilder()
                    .name(text("category.general"))
                    .group(OptionGroup.createBuilder()
                            .name(text("group.display"))
                            .option(toggle("enabled", defaults.enabled, () -> config.enabled, v -> config.enabled = v))
                            .option(Option.<Integer>createBuilder()
                                    .name(text("option.radius"))
                                    .description(OptionDescription.of(text("option.radius.desc")))
                                    .binding(defaults.radius, () -> config.radius, v -> config.radius = v)
                                    .controller(o -> IntegerSliderControllerBuilder.create(o).range(32, 200).step(1)
                                            .formatValue(v -> Component.literal(v + " px")))
                                    .build())
                            .option(Option.<Integer>createBuilder()
                                    .name(text("option.icon_scale"))
                                    .description(OptionDescription.of(text("option.icon_scale.desc")))
                                    .binding(defaults.iconScale, () -> config.iconScale, v -> config.iconScale = v)
                                    .controller(o -> IntegerSliderControllerBuilder.create(o).range(1, 3).step(1)
                                            .formatValue(v -> Component.literal(v + "x")))
                                    .build())
                            .option(slider("opacity", defaults.opacity, () -> config.opacity, v -> config.opacity = v, 0.1f, 1.0f, 0.05f, ""))
                            .option(toggle("show_icons", defaults.showIcons, () -> config.showIcons, v -> config.showIcons = v))
                            .option(toggle("mob_heads", defaults.mobHeads, () -> config.mobHeads, v -> config.mobHeads = v))
                            .option(toggle("show_player_names", defaults.showPlayerNames, () -> config.showPlayerNames, v -> config.showPlayerNames = v))
                            .option(toggle("show_ring", defaults.showRing, () -> config.showRing, v -> config.showRing = v))
                            .option(slider("ring_opacity", defaults.ringOpacity, () -> config.ringOpacity, v -> config.ringOpacity = v, 0.0f, 1.0f, 0.05f, ""))
                            .option(toggle("show_labels", defaults.showLabels, () -> config.showLabels, v -> config.showLabels = v))
                            .option(toggle("show_distance", defaults.showDistance, () -> config.showDistance, v -> config.showDistance = v))
                            .option(toggle("show_vertical", defaults.showVertical, () -> config.showVertical, v -> config.showVertical = v))
                            .option(toggle("pulse_danger", defaults.pulseDanger, () -> config.pulseDanger, v -> config.pulseDanger = v))
                            .build())
                    .group(OptionGroup.createBuilder()
                            .name(text("group.detection"))
                            .option(Option.<Integer>createBuilder()
                                    .name(text("option.max_distance"))
                                    .description(OptionDescription.of(text("option.max_distance.desc")))
                                    .binding(defaults.maxDistance, () -> config.maxDistance, v -> config.maxDistance = v)
                                    .controller(o -> IntegerSliderControllerBuilder.create(o).range(4, 64).step(1)
                                            .formatValue(v -> Component.literal(v + " m")))
                                    .build())
                            .option(slider("duration", defaults.duration, () -> config.duration, v -> config.duration = v, 0.5f, 6.0f, 0.1f, " s"))
                            .option(Option.<Integer>createBuilder()
                                    .name(text("option.max_indicators"))
                                    .description(OptionDescription.of(text("option.max_indicators.desc")))
                                    .binding(defaults.maxIndicators, () -> config.maxIndicators, v -> config.maxIndicators = v)
                                    .controller(o -> IntegerSliderControllerBuilder.create(o).range(1, 32).step(1))
                                    .build())
                            .option(toggle("show_own_sounds", defaults.showOwnSounds, () -> config.showOwnSounds, v -> config.showOwnSounds = v))
                            .option(toggle("ignore_ambient", defaults.ignoreAmbient, () -> config.ignoreAmbient, v -> config.ignoreAmbient = v))
                            .build());

            ConfigCategory.Builder kinds = ConfigCategory.createBuilder().name(text("category.sounds"));
            for (SoundKind kind : SoundKind.values()) {
                kinds.group(OptionGroup.createBuilder()
                        .name(kind.displayName())
                        .description(OptionDescription.of(kind.description()))
                        .option(Option.<Boolean>createBuilder()
                                .name(text("option.kind_enabled"))
                                .description(OptionDescription.of(kind.description()))
                                .binding(true, () -> config.isEnabled(kind), v -> config.kindEnabled.put(kind.key(), v))
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.<Color>createBuilder()
                                .name(text("option.kind_color"))
                                .description(OptionDescription.of(kind.description()))
                                .binding(new Color(kind.defaultColor()), () -> config.color(kind), v -> config.kindColor.put(kind.key(), v))
                                .controller(ColorControllerBuilder::create)
                                .build())
                        .build());
            }

            return builder.title(text("title")).category(general.build()).category(kinds.build());
        }).generateScreen(parent);
    }

    private static Component text(String key) {
        return Component.translatable("soundcircle.config." + key);
    }

    private static Option<Boolean> toggle(String key, boolean def, Supplier<Boolean> getter, Consumer<Boolean> setter) {
        return Option.<Boolean>createBuilder()
                .name(text("option." + key))
                .description(OptionDescription.of(text("option." + key + ".desc")))
                .binding(def, getter, setter)
                .controller(TickBoxControllerBuilder::create)
                .build();
    }

    private static Option<Float> slider(String key, float def, Supplier<Float> getter, Consumer<Float> setter, float min, float max, float step, String unit) {
        return Option.<Float>createBuilder()
                .name(text("option." + key))
                .description(OptionDescription.of(text("option." + key + ".desc")))
                .binding(def, getter, setter)
                .controller(o -> FloatSliderControllerBuilder.create(o).range(min, max).step(step)
                        .formatValue(v -> Component.literal(String.format("%.2f", v) + unit)))
                .build();
    }
}
