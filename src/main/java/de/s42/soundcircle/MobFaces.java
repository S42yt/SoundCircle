package de.s42.soundcircle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class MobFaces {
    private record Spec(String paths, int textureWidth, int textureHeight, int width, int height, List<Layer> layers) {
    }

    private record Layer(@Nullable String texture, int u, int v, int width, int height, int x, int y) {
    }

    private static final Map<String, Spec> SPECS = new HashMap<>();

    static {
        face("creeper", "creeper/creeper.png", 64, 32, 8, 8, 8, 8);
        for (String zombie : new String[]{"zombie", "husk", "drowned"}) {
            face(zombie, "zombie/" + zombie + ".png", 64, 64, 8, 8, 8, 8, hat());
        }
        for (String skeleton : new String[]{"skeleton", "stray", "wither_skeleton", "bogged"}) {
            face(skeleton, "skeleton/" + skeleton + ".png", 64, 32, 8, 8, 8, 8);
        }
        face("spider", "spider/spider.png", 64, 32, 40, 12, 8, 8);
        face("cave_spider", "spider/cave_spider.png", 64, 32, 40, 12, 8, 8);
        face("enderman", "enderman/enderman.png", 64, 32, 8, 8, 8, 8, new Layer("enderman/enderman_eyes.png", 8, 8, 8, 8, 0, 0));
        for (String piglin : new String[]{"piglin", "piglin_brute", "zombified_piglin"}) {
            face(piglin, "piglin/" + piglin + ".png", 64, 64, 8, 8, 10, 8);
        }
        face("villager", "villager/villager.png", 64, 64, 8, 8, 8, 10);
        face("zombie_villager", "zombie_villager/zombie_villager.png", 64, 64, 8, 8, 8, 10);
        face("wandering_trader", "wandering_trader.png|wandering_trader/wandering_trader.png", 64, 64, 8, 8, 8, 10);
        for (String illager : new String[]{"pillager", "vindicator", "evoker", "illusioner"}) {
            face(illager, "illager/" + illager + ".png", 64, 64, 8, 8, 8, 10);
        }
        face("witch", "witch.png|witch/witch.png", 64, 128, 8, 8, 8, 10);
        face("ravager", "illager/ravager.png", 128, 128, 16, 16, 16, 20);
        face("vex", "illager/vex.png", 32, 32, 5, 5, 5, 5);
        face("blaze", "blaze.png|blaze/blaze.png", 64, 32, 8, 8, 8, 8);
        face("iron_golem", "iron_golem/iron_golem.png", 128, 128, 8, 8, 8, 10);
        face("snow_golem", "snow_golem.png|snow_golem/snow_golem.png", 64, 64, 8, 8, 8, 8);
        face("pig", "pig/temperate_pig.png|pig/pig_temperate.png", 64, 64, 8, 8, 8, 8);
        face("cow", "cow/temperate_cow.png|cow/cow_temperate.png", 64, 64, 6, 6, 8, 8);
        face("sheep", "sheep/sheep.png", 64, 32, 8, 8, 6, 6);
        face("chicken", "chicken/temperate_chicken.png|chicken/chicken_temperate.png", 64, 32, 3, 3, 4, 6);
        face("wolf", "wolf/wolf.png", 64, 32, 4, 4, 6, 6);
        face("cat", "cat/tabby.png|cat/cat_tabby.png", 64, 32, 5, 5, 5, 4);
        face("ocelot", "cat/ocelot.png", 64, 32, 5, 5, 5, 4);
        face("fox", "fox/fox.png", 48, 32, 7, 11, 8, 6);
        face("bee", "bee/bee.png", 64, 64, 10, 10, 7, 7);
        face("polar_bear", "bear/polarbear.png", 128, 64, 7, 7, 7, 7);
        face("panda", "panda/panda.png", 64, 64, 9, 15, 13, 10);
        face("axolotl", "axolotl/axolotl_lucy.png", 64, 64, 5, 6, 8, 5);
        face("turtle", "turtle/big_sea_turtle.png|turtle/turtle.png", 128, 64, 9, 6, 6, 5);
        face("dolphin", "dolphin.png|dolphin/dolphin.png", 64, 64, 6, 6, 8, 7);
        face("squid", "squid/squid.png", 64, 32, 12, 12, 12, 16);
        face("glow_squid", "squid/glow_squid.png", 64, 32, 12, 12, 12, 16);
        face("strider", "strider/strider.png", 64, 128, 16, 16, 16, 14);
        face("allay", "allay/allay.png", 32, 32, 5, 5, 5, 5);
        face("wither", "wither/wither.png", 64, 64, 8, 8, 8, 8);
        face("shulker", "shulker/shulker.png", 64, 64, 6, 58, 6, 6);
        face("phantom", "phantom.png|phantom/phantom.png", 64, 64, 5, 5, 7, 3, new Layer("phantom_eyes.png|phantom/phantom_eyes.png", 5, 5, 7, 3, 0, 0));
        face("guardian", "guardian.png|guardian/guardian.png", 64, 64, 16, 16, 12, 12, new Layer(null, 9, 1, 2, 2, 5, 5));
        face("elder_guardian", "guardian_elder.png|guardian/guardian_elder.png", 64, 64, 16, 16, 12, 12, new Layer(null, 9, 1, 2, 2, 5, 5));
        face("slime", "slime/slime.png", 64, 32, 8, 8, 8, 8,
                new Layer(null, 6, 22, 6, 6, 1, 1), new Layer(null, 34, 2, 2, 2, 1, 2),
                new Layer(null, 34, 6, 2, 2, 5, 2), new Layer(null, 33, 9, 1, 1, 4, 5));
        face("warden", "warden/warden.png", 128, 128, 10, 42, 16, 16);
        face("ghast", "ghast/ghast.png", 128, 64, 32, 32, 32, 32);
        face("player", "player/wide/steve.png", 64, 64, 8, 8, 8, 8, hat());
    }

    private final Map<String, Optional<SoundIcon.Face>> cache = new HashMap<>();

    private static Layer hat() {
        return new Layer(null, 40, 8, 8, 8, 0, 0);
    }

    private static void face(String type, String paths, int textureWidth, int textureHeight, int u, int v, int width, int height, Layer... extra) {
        List<Layer> layers = new ArrayList<>();
        layers.add(new Layer(null, u, v, width, height, 0, 0));
        layers.addAll(List.of(extra));
        SPECS.put(type, new Spec(paths, textureWidth, textureHeight, width, height, layers));
    }

    public static String typeOf(Entity entity) {
        return BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).getPath();
    }

    public SoundIcon.@Nullable Face mob(String type) {
        return cache.computeIfAbsent(type, key -> Optional.ofNullable(build(key, null, false))).orElse(null);
    }

    public SoundIcon.@Nullable Face entity(Entity entity) {
        if (entity instanceof AbstractClientPlayer player) {
            return player(player);
        }
        String type = typeOf(entity);
        if (!SPECS.containsKey(type)) {
            return null;
        }
        boolean baby = entity instanceof LivingEntity living && living.isBaby();
        Identifier texture = variantTexture(entity);
        if (texture != null && texture.getPath().contains("_baby")) {
            Identifier adult = Identifier.fromNamespaceAndPath(texture.getNamespace(), texture.getPath().replace("_baby", ""));
            texture = exists(adult) ? adult : null;
        }
        String key = type + "|" + texture + "|" + baby;
        Identifier resolved = texture;
        return cache.computeIfAbsent(key, k -> Optional.ofNullable(build(type, resolved, baby))).orElse(null);
    }

    public static SoundIcon.Face player(AbstractClientPlayer player) {
        Spec spec = SPECS.get("player");
        return new SoundIcon.Face(player.getSkin().body().texturePath(), 64, 64, 8, 8, layers(spec), false);
    }

    public void clear() {
        cache.clear();
    }

    private static SoundIcon.@Nullable Face build(String type, @Nullable Identifier texture, boolean baby) {
        Spec spec = SPECS.get(type);
        if (spec == null) {
            return null;
        }
        Identifier base = texture != null ? texture : find(spec.paths());
        if (base == null) {
            return null;
        }
        return new SoundIcon.Face(base, spec.textureWidth(), spec.textureHeight(), spec.width(), spec.height(), layers(spec), baby);
    }

    private static List<SoundIcon.Layer> layers(Spec spec) {
        List<SoundIcon.Layer> layers = new ArrayList<>();
        for (Layer layer : spec.layers()) {
            Identifier texture = layer.texture() != null ? find(layer.texture()) : null;
            if (layer.texture() == null || texture != null) {
                layers.add(new SoundIcon.Layer(texture, layer.u(), layer.v(), layer.width(), layer.height(), layer.x(), layer.y()));
            }
        }
        return layers;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static @Nullable Identifier variantTexture(Entity entity) {
        try {
            Minecraft mc = Minecraft.getInstance();
            EntityRenderer renderer = mc.getEntityRenderDispatcher().getRenderer(entity);
            if (renderer instanceof LivingEntityRenderer living) {
                return living.getTextureLocation((LivingEntityRenderState) renderer.createRenderState(entity, 1.0f));
            }
        } catch (RuntimeException ignored) {
        }
        return null;
    }

    private static @Nullable Identifier find(String paths) {
        for (String path : paths.split("\\|")) {
            Identifier id = Identifier.withDefaultNamespace("textures/entity/" + path);
            if (exists(id)) {
                return id;
            }
        }
        return null;
    }

    private static boolean exists(Identifier id) {
        return Minecraft.getInstance().getResourceManager().getResource(id).isPresent();
    }
}
