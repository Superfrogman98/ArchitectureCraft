// ------------------------------------------------------------------------------------------------
//
// Greg's Mod Base for 1.7 Version B - Block Utilities
//
// ------------------------------------------------------------------------------------------------

package gcewing.architecture.compat;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gcewing.architecture.common.block.BlockArchitecture;

public class BlockCompatUtils {

    public static String getNameForBlock(Block block) {
        if (block != null) return Block.blockRegistry.getNameForObject(block);
        else return "";
    }

    public static IBlockState getBlockStateFromItemStack(ItemStack stack) {
        Block block = Block.getBlockFromItem(stack.getItem());
        int meta = 0;
        if (stack.getItem().getHasSubtypes()) meta = stack.getItem().getMetadata(stack.getItemDamage());
        if (block instanceof BlockArchitecture) return ((BlockArchitecture<?>) block).getStateFromMeta(meta);
        else return new MetaBlockState(block, meta);
    }

    public static IBlockState getBlockStateFromMeta(Block block, int meta) {
        if (block instanceof BlockArchitecture) return ((BlockArchitecture<?>) block).getStateFromMeta(meta);
        else return new MetaBlockState(block, meta);
    }

    public static int getMetaFromBlockState(IBlockState state) {
        if (state instanceof MetaBlockState) return ((MetaBlockState) state).meta;
        else return ((BlockArchitecture<?>) state.getBlock()).getMetaFromState(state);
    }

    public static IBlockState getWorldBlockState(IBlockAccess world, BlockPos pos) {
        Block block = world.getBlock(pos.x, pos.y, pos.z);
        int meta = world.getBlockMetadata(pos.x, pos.y, pos.z);
        if (block instanceof BlockArchitecture) return ((BlockArchitecture<?>) block).getStateFromMeta(meta);
        else return new MetaBlockState(block, meta);
    }

    public static boolean setWorldBlockState(World world, BlockPos pos, IBlockState state, int flags) {
        Block block = state.getBlock();
        int meta = getMetaFromBlockState(state);
        return world.setBlock(pos.x, pos.y, pos.z, block, meta, flags);
    }

    public static TileEntity getWorldTileEntity(IBlockAccess world, BlockPos pos) {
        return world.getTileEntity(pos.x, pos.y, pos.z);
    }

    public static World getTileEntityWorld(TileEntity te) {
        return te.getWorldObj();
    }

    public static BlockPos getTileEntityPos(TileEntity te) {
        return new BlockPos(te.xCoord, te.yCoord, te.zCoord);
    }

    public static boolean blockCanRenderInLayer(Block block, EnumWorldBlockLayer layer) {
        if (block instanceof BlockArchitecture) return ((BlockArchitecture<?>) block).canRenderInLayer(layer);
        else return switch (layer) {
            case SOLID -> block.canRenderInPass(0);
            case TRANSLUCENT -> block.canRenderInPass(1);
            default -> false;
        };
    }

    public static IBlockState getDefaultBlockState(Block block) {
        if (block instanceof BlockArchitecture) return ((BlockArchitecture<?>) block).getDefaultState();
        else return new MetaBlockState(block, 0);
    }

    public static void playWorldAuxSFX(World world, int fxId, BlockPos pos, IBlockState state) {
        Block block = state.getBlock();
        int meta = getMetaFromBlockState(state);
        int stateId = (meta << 12) | Block.getIdFromBlock(block);
        world.playAuxSFX(fxId, pos.getX(), pos.getY(), pos.getZ(), stateId);
    }

    public static float getBlockHardness(Block block, World world, BlockPos pos) {
        return block.getBlockHardness(world, pos.x, pos.y, pos.z);
    }

    public static String getBlockHarvestTool(IBlockState state) {
        Block block = state.getBlock();
        int meta = getMetaFromBlockState(state);
        return block.getHarvestTool(meta);
    }

    public static int getBlockHarvestLevel(IBlockState state) {
        Block block = state.getBlock();
        int meta = getMetaFromBlockState(state);
        return block.getHarvestLevel(meta);
    }

    public static float getPlayerBreakSpeed(EntityPlayer player, IBlockState state, BlockPos pos) {
        Block block = state.getBlock();
        int meta = getMetaFromBlockState(state);
        return player.getBreakSpeed(block, false, meta, pos.x, pos.y, pos.z);
    }

    @SideOnly(Side.CLIENT)
    public static IIcon getSpriteForBlockState(IBlockState state) {
        if (state != null) {
            Block block = state.getBlock();
            int meta = getMetaFromBlockState(state);
            return block.getIcon(2, meta);
        } else return null;
    }

    @SideOnly(Side.CLIENT)
    public static IIcon getSpriteForBlockState(IBlockState state, int side) {
        if (state != null) {
            Block block = state.getBlock();
            int meta = getMetaFromBlockState(state);
            return block.getIcon(side, meta);
        } else return null;
    }


    public static void spawnBlockStackAsEntity(World world, BlockPos pos, ItemStack stack) {
        float var6 = 0.7F;
        double var7 = (double) (world.rand.nextFloat() * var6) + (double) (1.0F - var6) * 0.5D;
        double var9 = (double) (world.rand.nextFloat() * var6) + (double) (1.0F - var6) * 0.5D;
        double var11 = (double) (world.rand.nextFloat() * var6) + (double) (1.0F - var6) * 0.5D;
        EntityItem var13 = new EntityItem(world, pos.x + var7, pos.y + var9, pos.z + var11, stack);
        var13.delayBeforeCanPickup = 10;
        world.spawnEntityInWorld(var13);
    }

    public static ItemStack blockStackWithState(IBlockState state, int size) {
        Block block = state.getBlock();
        int meta = getMetaFromBlockState(state);
        return new ItemStack(block, size, meta);
    }

    // ------------------------------------------------------------------------------------------------

}
