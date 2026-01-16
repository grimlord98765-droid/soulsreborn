package com.terrarockys.soulsreborn.block;

import com.terrarockys.soulsreborn.SoulsRebornMod;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {
    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(SoulsRebornMod.MODID);

    public static final DeferredBlock<Block> BONFIRE = BLOCKS.register("bonfire",
            () -> new BonfireBlock(BlockBehaviour.Properties.of()
                    .lightLevel(state -> 15)
                    .noOcclusion()
                    .requiresCorrectToolForDrops()
                    .strength(2.0f)));

    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
    }
}