package de.s42.soundcircle.render;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import de.s42.soundcircle.SoundCircle;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
//? if >=26.3 {
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
//?} else {
/*import com.mojang.blaze3d.pipeline.RenderPipeline;
*///?}
//? if <26.2 {
/*import com.mojang.blaze3d.vertex.VertexFormat;
*///?}

public final class CirclePipeline {
    private static final Identifier SHADER = Identifier.fromNamespaceAndPath(SoundCircle.MOD_ID, "core/sound_circle");

    public static final RenderPipeline PIPELINE = RenderPipelines.register(RenderPipeline.builder(RenderPipelines.GUI_SNIPPET)
            .withLocation(Identifier.fromNamespaceAndPath(SoundCircle.MOD_ID, "pipeline/sound_circle"))
            .withVertexShader(SHADER)
            .withFragmentShader(SHADER)
            //? if >=26.2 {
            .withVertexBinding(0, DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP)
            //?} else {
            /*.withVertexFormat(DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS)
            *///?}
            .build());

    private CirclePipeline() {
    }

    public static void init() {
    }
}
