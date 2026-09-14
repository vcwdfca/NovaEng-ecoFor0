package com.vcwdfca.ecofor0.block;

import com.vcwdfca.ecofor0.Tags;
import com.vcwdfca.ecofor0.common.tile.TileSuperPatternAssembly;
import com.vcwdfca.ecofor0.ecofor0;
import github.kasuminova.mmce.common.block.appeng.BlockMEPatternProvider;
import appeng.api.implementations.items.IMemoryCard;
import hellfirepvp.modularmachinery.common.item.ItemBlockMEMachineComponent;
import hellfirepvp.modularmachinery.common.lib.ItemsMM;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import static com.vcwdfca.ecofor0.ecofor0.id;

import java.util.List;

public final class BlockSuperPatternAssembly extends BlockMEPatternProvider {

    public static final ResourceLocation BLOCK_ID = id("super_pattern_assembly");
    public static final Block INSTANCE = new BlockSuperPatternAssembly();
    public static final ItemBlock ITEM_INSTANCE = new ItemBlockMEMachineComponent(INSTANCE);
    static {
        ITEM_INSTANCE.setRegistryName(BLOCK_ID);
    }

    public BlockSuperPatternAssembly() {
        setRegistryName(BLOCK_ID);
        setTranslationKey(Tags.MOD_ID + ".super_pattern_assembly");
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileSuperPatternAssembly();
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileSuperPatternAssembly();
    }

    @Override
    public void breakBlock(World world, @NotNull BlockPos pos, @NotNull IBlockState state) {
        if (!world.isRemote) {
            ItemStack drop = new ItemStack(BlockSuperPatternAssembly.ITEM_INSTANCE);
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof TileSuperPatternAssembly) {
                drop.setTagInfo("patternProvider", ((TileSuperPatternAssembly) tile)
                        .writeProviderNBT(new NBTTagCompound()));
            }
            spawnAsEntity(world, pos, drop);
        }
        world.removeTileEntity(pos);
    }

    @Override
    public boolean onBlockActivated(@NotNull World world, @NotNull BlockPos pos, @NotNull IBlockState state, EntityPlayer player,
                                    @NotNull EnumHand hand, @NotNull EnumFacing side, float x, float y, float z) {
        ItemStack held = player.getHeldItem(hand);
        if (hand == EnumHand.MAIN_HAND && player.isSneaking() && held.getItem() instanceof IMemoryCard) {
            if (!world.isRemote) {
                NBTTagCompound data = new NBTTagCompound();
                data.setLong("Pos", pos.toLong());
                ((IMemoryCard) held.getItem()).setMemoryCardContents(held,
                        net.minecraft.block.Block.getBlockFromItem(ItemsMM.mePatternProvider).getTranslationKey(), data);
                player.sendMessage(new TextComponentTranslation("message.blockmepatternprovider.save"));
            }
            return true;
        }
        if (player.isSneaking() && player.getHeldItem(hand).isEmpty()) {
            TileEntity tile = world.getTileEntity(pos);
            if (!world.isRemote && tile instanceof TileSuperPatternAssembly) {
                TileSuperPatternAssembly assembly = (TileSuperPatternAssembly) tile;
                player.sendMessage(new TextComponentTranslation("superpatternassembly.output_status",
                        assembly.pendingItems(), assembly.pendingFluid(), assembly.pendingGas(),
                        assembly.isActive() ? "ME online" : "ME offline"));
            }
            return true;
        }
        if (hand == EnumHand.MAIN_HAND && held.getItem() instanceof appeng.items.tools.quartz.ToolQuartzCuttingKnife)
            return super.onBlockActivated(world, pos, state, player, hand, side, x, y, z);
        if (!world.isRemote) player.openGui(ecofor0.INSTANCE, 2, world, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(@NotNull ItemStack stack, World world, @NotNull List<String> lines, @NotNull ITooltipFlag flag) {
        // Keep MMCE's original tooltip verbatim. Its translation keys remain in MMCE's language file.
        super.addInformation(stack, world, lines, flag);
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("patternProvider")) {
            lines.add(I18n.format("gui.mepatternprovider.nbt_stored"));
        }
        lines.add(I18n.format("superpatternassembly.tooltip.batching"));
        lines.add(I18n.format("superpatternassembly.tooltip.output"));
        lines.add(I18n.format("superpatternassembly.tooltip.catalyst"));
        lines.add(I18n.format("superpatternassembly.tooltip.pages"));
        lines.add(I18n.format("superpatternassembly.block_source"));
    }
}
