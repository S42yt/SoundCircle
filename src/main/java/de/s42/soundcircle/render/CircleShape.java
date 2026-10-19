package de.s42.soundcircle.render;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import org.joml.Matrix3x2f;
import org.jspecify.annotations.Nullable;
//? if >=26.3 {
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
//?} else {
/*import com.mojang.blaze3d.pipeline.RenderPipeline;
*///?}
//? if >=26.1 {
import net.minecraft.client.renderer.state.gui.GuiElementRenderState;
//?} else {
/*import net.minecraft.client.gui.render.state.GuiElementRenderState;
*///?}

public record CircleShape(Matrix3x2f pose, int half, int color, int type, int angle, int spread, int radius,
                          @Nullable ScreenRectangle bounds) implements GuiElementRenderState {
    public static final int RING = 0;
    public static final int ARC = 1;

    public CircleShape(Matrix3x2f pose, int color, int type, int angle, int spread, int radius) {
        this(pose, radius + 3, color, type, angle, spread, radius, new ScreenRectangle(-radius - 3, -radius - 3, radius * 2 + 6, radius * 2 + 6).transformMaxBounds(pose));
    }

    @Override
    public void buildVertices(VertexConsumer consumer) {
        int packedType = type | (Math.floorMod(angle, 360) << 2);
        int packedArc = (Math.min(spread, 127) << 8) | Math.min(radius, 255);
        vertex(consumer, -half, -half, packedType, packedArc);
        vertex(consumer, -half, half, packedType, packedArc);
        vertex(consumer, half, half, packedType, packedArc);
        vertex(consumer, half, -half, packedType, packedArc);
    }

    private void vertex(VertexConsumer consumer, int x, int y, int packedType, int packedArc) {
        consumer.addVertexWith2DPose(pose, x, y).setColor(color).setUv(x, y).setUv2(packedType, packedArc);
    }

    @Override
    public RenderPipeline pipeline() {
        return CirclePipeline.PIPELINE;
    }

    @Override
    public TextureSetup textureSetup() {
        return TextureSetup.noTexture();
    }

    @Override
    public @Nullable ScreenRectangle scissorArea() {
        return null;
    }
}
