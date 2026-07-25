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
        for(Block block : blocks) {
            list.add(new IBlockStateDescriptor(block));
        }
        return new BlockArray.BlockInformation(list);
    }

    public static BlockArray.BlockInformation info(IBlockState... blocks) {
        List<IBlockStateDescriptor> list = new ArrayList<>();
        for(IBlockState block : blocks) {
            list.add(new IBlockStateDescriptor(block));
        }
        return new BlockArray.BlockInformation(list);
    }
}
