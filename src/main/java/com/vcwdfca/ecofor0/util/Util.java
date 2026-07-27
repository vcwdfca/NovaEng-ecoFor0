package com.vcwdfca.ecofor0.util;

import hellfirepvp.modularmachinery.common.util.BlockArray;
import hellfirepvp.modularmachinery.common.util.IBlockStateDescriptor;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

import java.util.ArrayList;
import java.util.List;

public final class Util {

    public static BlockArray.BlockInformation info(Block... blocks) {
        List<IBlockStateDescriptor> list = new ArrayList<>();
        for (Block block : blocks) {
            // Optional integration blocks are null when the integration is unavailable.
            if (block != null) {
                list.add(new IBlockStateDescriptor(block));
            }
        }
        if (list.isEmpty()) {
            throw new IllegalArgumentException("At least one block is required");
        }
        return new BlockArray.BlockInformation(list);
    }

    public static BlockArray.BlockInformation info(IBlockState... blocks) {
        List<IBlockStateDescriptor> list = new ArrayList<>();
        for(IBlockState block : blocks) {
            // Same as block
            if(block != null) {
                list.add(new IBlockStateDescriptor(block));
            }
        }
        if(list.isEmpty()) {
            throw new IllegalArgumentException("At least one block is required");
        }
        return new BlockArray.BlockInformation(list);
    }
}
