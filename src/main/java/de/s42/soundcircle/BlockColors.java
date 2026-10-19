package de.s42.soundcircle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public final class BlockColors {
    private static final Map<String, String> MATERIALS = Map.ofEntries(
            Map.entry("wood", "oak_planks"),
            Map.entry("grass", "grass_block"),
            Map.entry("wet_grass", "grass_block"),
            Map.entry("metal", "iron_block"),
            Map.entry("copper", "copper_block"),
            Map.entry("wool", "white_wool"),
            Map.entry("snow", "snow_block"),
            Map.entry("cherry_wood", "cherry_planks"),
            Map.entry("bamboo_wood", "bamboo_planks"),
            Map.entry("nether_wood", "crimson_planks"),
            Map.entry("coral_block", "brain_coral_block"),
            Map.entry("nylium", "crimson_nylium"),
            Map.entry("fungus", "crimson_fungus"),
            Map.entry("roots", "crimson_roots"),
            Map.entry("stem", "crimson_stem"),
            Map.entry("crop", "wheat"),
            Map.entry("hard_crop", "wheat")
    );

    private BlockColors() {
    }

    public static int of(Identifier id, Vec3 pos) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            return -1;
        }
        BlockPos blockPos = BlockPos.containing(pos);
        BlockState state = level.getBlockState(blockPos);
        if (!state.isAir()) {
            int color = state.getMapColor(level, blockPos).col;
            if (color != 0) {
                return color;
            }
        }
        String[] parts = id.getPath().split("\\.");
        if (parts.length < 2 || !parts[0].equals("block")) {
            return -1;
        }
        String material = MATERIALS.getOrDefault(parts[1], parts[1]);
        if (!Identifier.isValidPath(material)) {
            return -1;
        }
        Block block = BuiltInRegistries.BLOCK.getValue(Identifier.fromNamespaceAndPath(id.getNamespace(), material));
        int color = block.defaultMapColor().col;
        return block == Blocks.AIR || color == 0 ? -1 : color;
    }
}
