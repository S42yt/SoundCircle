package de.s42.soundcircle.render;

import de.s42.soundcircle.SoundCircle;
import de.s42.soundcircle.SoundIcon;
import de.s42.soundcircle.SoundKind;
import de.s42.soundcircle.Indicator;
import de.s42.soundcircle.SoundTracker;
import de.s42.soundcircle.config.SoundCircleConfig;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3x2f;
import org.joml.Matrix3x2fStack;
import org.joml.Quaternionf;
import org.joml.Vector3f;
//? if >=26.1 {
import net.minecraft.client.gui.GuiGraphicsExtractor;
//?} else {
/*import net.minecraft.client.gui.GuiGraphics;
*///?}

import java.util.List;

public final class CircleHud {
    private static final Identifier ICONS = Identifier.fromNamespaceAndPath(SoundCircle.MOD_ID, "textures/gui/icons.png");
    private static final int CELL = 11;
    private static final int SHEET_WIDTH = CELL * SoundIcon.Glyph.COUNT;
    private static final long FADE_IN = 120L;

    private final SoundTracker tracker;

    public CircleHud(SoundTracker tracker) {
        this.tracker = tracker;
    }

    //? if >=26.1 {
    public void render(GuiGraphicsExtractor graphics, DeltaTracker delta) {
    //?} else {
    /*public void render(GuiGraphics graphics, DeltaTracker delta) {
    *///?}
        Minecraft mc = Minecraft.getInstance();
        SoundCircleConfig config = SoundCircleConfig.get();
        if (!config.enabled || mc.player == null) {
            return;
        }
        long now = Util.getMillis();
        long lifetime = (long) (config.duration * 1000.0f);
        List<Indicator> indicators = tracker.tick(now, lifetime);
        if (indicators.isEmpty()) {
            return;
        }

        float partial = delta.getGameTimeDeltaPartialTick(true);
        Vec3 eye = mc.player.getEyePosition(partial);
        float yaw = mc.player.getViewYRot(partial);
        int cx = graphics.guiWidth() / 2;
        int cy = graphics.guiHeight() / 2;
        int radius = config.radius;
        int scale = Math.max(1, config.iconScale);
        Matrix3x2fStack pose = graphics.pose();

        int count = indicators.size();
        double[] angles = new double[count];
        float[] alphas = new float[count];
        float[] closeness = new float[count];
        float strongest = 0.0f;
        for (int i = 0; i < count; i++) {
            Indicator indicator = indicators.get(i);
            Vec3 offset = indicator.position().subtract(eye);
            closeness[i] = 1.0f - Mth.clamp((float) (offset.length() / config.maxDistance), 0.0f, 1.0f);
            angles[i] = Math.toRadians(Mth.wrapDegrees(Math.toDegrees(Math.atan2(-offset.x, offset.z)) - yaw));
            float fadeIn = Mth.clamp((now - indicator.born) / (float) FADE_IN, 0.0f, 1.0f);
            float life = (now - indicator.refreshed) / (float) lifetime;
            float visibility = fadeIn * (1.0f - smoothstep(0.6f, 1.0f, life));
            float alpha = visibility * (0.45f + 0.55f * closeness[i]) * config.opacity;
            if (indicator.kind == SoundKind.DANGER && config.pulseDanger) {
                alpha *= 0.8f + 0.2f * Mth.sin(now * 0.016f);
            }
            alphas[i] = alpha;
            strongest = Math.max(strongest, visibility);
        }

        pose.pushMatrix();
        pose.translate(cx, cy);
        if (config.showRing) {
            submit(graphics, new CircleShape(new Matrix3x2f(pose), argb(config.ringOpacity * strongest * config.opacity, 0xFFFFFF), CircleShape.RING, 0, 0, radius));
        }
        for (int i = 0; i < count; i++) {
            Indicator indicator = indicators.get(i);
            int spread = Math.round(9.0f + 14.0f * closeness[i] + (indicator.kind == SoundKind.DANGER ? 6.0f : 0.0f));
            int angle = (int) Math.round(Math.toDegrees(angles[i]));
            submit(graphics, new CircleShape(new Matrix3x2f(pose), argb(alphas[i], color(config, indicator.kind)), CircleShape.ARC, angle, spread, radius));
        }
        pose.popMatrix();

        if (!config.showIcons && !config.showLabels && !config.showDistance && !config.showPlayerNames) {
            return;
        }

        int box = CELL * scale;
        float iconDistance = radius - box / 2.0f - 4.0f;
        for (int i = 0; i < count; i++) {
            Indicator indicator = indicators.get(i);
            float alpha = alphas[i];
            int x = cx + Math.round((float) Math.sin(angles[i]) * iconDistance);
            int y = cy - Math.round((float) Math.cos(angles[i]) * iconDistance);
            int tint = argb(Math.min(1.0f, alpha * 1.25f), color(config, indicator.kind));
            int white = argb(Math.min(1.0f, alpha * 1.25f), 0xFFFFFF);
            int top = y - box / 2;
            int bottom = y + box / 2;

            if (config.showIcons) {
                if (indicator.icon instanceof SoundIcon.Face face) {
                    float pixel = facePixel(face, scale);
                    int width = Math.round(face.width() * pixel);
                    int height = Math.round(face.height() * pixel);
                    int x0 = x - width / 2;
                    int y0 = y - height / 2;
                    graphics.fill(x0 - 1, y0 - 1, x0 + width + 1, y0 + height + 1, argb(alpha * 0.55f, 0x000000));
                    for (SoundIcon.Layer layer : face.layers()) {
                        Identifier texture = layer.texture() != null ? layer.texture() : face.texture();
                        graphics.blit(RenderPipelines.GUI_TEXTURED, texture, x0 + Math.round(layer.x() * pixel), y0 + Math.round(layer.y() * pixel),
                                layer.u(), layer.v(), Math.round(layer.width() * pixel), Math.round(layer.height() * pixel),
                                layer.width(), layer.height(), face.textureWidth(), face.textureHeight(), white);
                    }
                    top = y0 - 1;
                    bottom = y0 + height + 1;
                } else if (indicator.icon instanceof SoundIcon.Model && indicator.entity instanceof LivingEntity living && !living.isRemoved()) {
                    int size = 8 * scale;
                    int x0 = x - size / 2;
                    int y0 = y - size / 2;
                    graphics.fill(x0 - 1, y0 - 1, x0 + size + 1, y0 + size + 1, argb(alpha * 0.55f, 0x000000));
                    renderHead(graphics, living, x0, y0, size, partial);
                    top = y0 - 1;
                    bottom = y0 + size + 1;
                } else if (indicator.icon instanceof SoundIcon.ItemIcon item) {
                    int size = 16;
                    pose.pushMatrix();
                    pose.translate(x - 8.0f * scale * 0.75f, y - 8.0f * scale * 0.75f);
                    pose.scale(scale * 0.75f, scale * 0.75f);
                    //? if >=26.1 {
                    graphics.item(new ItemStack(item.item()), 0, 0);
                    //?} else {
                    /*graphics.renderItem(new ItemStack(item.item()), 0, 0);
                    *///?}
                    pose.popMatrix();
                    top = y - Math.round(size * scale * 0.375f);
                    bottom = y + Math.round(size * scale * 0.375f);
                } else {
                    SoundIcon.Glyph glyph = indicator.icon instanceof SoundIcon.Glyph g ? g : indicator.kind.glyph();
                    int glyphTint = indicator.color >= 0 ? argb(Math.min(1.0f, alpha * 1.25f), indicator.color) : tint;
                    blitGlyph(graphics, glyph, x - box / 2, y - box / 2, box, glyphTint);
                }
                if (indicator.badge != null && !(indicator.icon instanceof SoundIcon.Glyph)) {
                    blitGlyph(graphics, indicator.badge, x + box / 2 - CELL + 3, bottom - CELL + 3, CELL, tint);
                }
            }

            double dy = indicator.position().y - eye.y;
            if (config.showVertical && Math.abs(dy) > 2.5) {
                SoundIcon.Glyph arrow = dy > 0 ? SoundIcon.Glyph.UP : SoundIcon.Glyph.DOWN;
                blitGlyph(graphics, arrow, x + box / 2 - 3, top - 5, CELL, white);
            }

            Component text = label(config, indicator, eye);
            if (text != null && alpha > 0.1f) {
                int width = mc.font.width(text);
                //? if >=26.1 {
                graphics.text(mc.font, text, x - width / 2, bottom + 2, white);
                //?} else {
                /*graphics.drawString(mc.font, text, x - width / 2, bottom + 2, white);
                *///?}
            }
        }
    }

    //? if >=26.1 {
    private static void renderHead(GuiGraphicsExtractor graphics, LivingEntity entity, int x0, int y0, int size, float partial) {
    //?} else {
    /*private static void renderHead(GuiGraphics graphics, LivingEntity entity, int x0, int y0, int size, float partial) {
    *///?}
        EntityRenderState state;
        try {
            state = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(entity).createRenderState(entity, partial);
        } catch (RuntimeException e) {
            return;
        }
        state.shadowPieces.clear();
        state.outlineColor = 0;
        state.lightCoords = 15728880;
        if (state instanceof LivingEntityRenderState living) {
            living.bodyRot = 180.0f;
            living.yRot = 0.0f;
            living.xRot = 0.0f;
        }
        float head = Mth.clamp(entity.getBbWidth() * 0.85f, 0.35f, 1.4f);
        float scale = size / head;
        Vector3f translation = new Vector3f(0.0f, entity.getEyeHeight(), 0.0f);
        Quaternionf rotation = new Quaternionf().rotateZ((float) Math.PI);
        //? if >=26.1 {
        graphics.entity(state, scale, translation, rotation, new Quaternionf(), x0, y0, x0 + size, y0 + size);
        //?} else {
        /*graphics.submitEntityRenderState(state, scale, translation, rotation, new Quaternionf(), x0, y0, x0 + size, y0 + size);
        *///?}
    }

    private static float facePixel(SoundIcon.Face face, int scale) {
        int largest = Math.max(face.width(), face.height());
        float pixel = largest > 10 ? (float) scale / (largest / 8) : scale;
        return face.baby() ? pixel * 0.75f : pixel;
    }

    //? if >=26.1 {
    private static void blitGlyph(GuiGraphicsExtractor graphics, SoundIcon.Glyph glyph, int x, int y, int size, int color) {
    //?} else {
    /*private static void blitGlyph(GuiGraphics graphics, SoundIcon.Glyph glyph, int x, int y, int size, int color) {
    *///?}
        graphics.blit(RenderPipelines.GUI_TEXTURED, ICONS, x, y, glyph.id() * CELL, 0, size, size, CELL, CELL, SHEET_WIDTH, CELL, color);
    }

    //? if >=26.1 {
    private static void submit(GuiGraphicsExtractor graphics, CircleShape shape) {
        graphics.guiRenderState.addGuiElement(shape);
    }
    //?} else {
    /*private static void submit(GuiGraphics graphics, CircleShape shape) {
        graphics.guiRenderState.submitGuiElement(shape);
    }
    *///?}

    private static Component label(SoundCircleConfig config, Indicator indicator, Vec3 eye) {
        Component text = config.showPlayerNames ? indicator.name : null;
        if (config.showLabels && indicator.label != null) {
            text = text == null ? indicator.label : Component.empty().append(text).append(" · ").append(indicator.label);
        }
        if (config.showDistance) {
            Component meters = Component.literal(Math.round(indicator.position().distanceTo(eye)) + "m");
            text = text == null ? meters : Component.empty().append(text).append(" · ").append(meters);
        }
        return text;
    }

    private static int color(SoundCircleConfig config, SoundKind kind) {
        return config.color(kind).getRGB() & 0xFFFFFF;
    }

    private static int argb(float alpha, int rgb) {
        return (Mth.clamp(Math.round(alpha * 255.0f), 0, 255) << 24) | rgb;
    }

    private static float smoothstep(float edge0, float edge1, float x) {
        float t = Mth.clamp((x - edge0) / (edge1 - edge0), 0.0f, 1.0f);
        return t * t * (3.0f - 2.0f * t);
    }
}
