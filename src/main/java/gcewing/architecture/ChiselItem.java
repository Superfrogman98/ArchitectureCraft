// ------------------------------------------------------------------------------
//
// ArchitectureCraft - Chisel
//
// ------------------------------------------------------------------------------

package gcewing.architecture;

import static gcewing.architecture.BaseBlockUtils.*;

import net.minecraft.block.*;
import net.minecraft.creativetab.*;
import net.minecraft.entity.player.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.*;

public class ChiselItem extends BaseItem {

    public ChiselItem() {
        setMaxStackSize(1);
    }

    @Override
    public CreativeTabs getCreativeTab() {
        return CreativeTabs.tabTools;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side,
            float hitX, float hitY, float hitZ) {
        TileEntity te = getWorldTileEntity(world, pos);
        if (te instanceof ShapeTE) {
            if (!world.isRemote) {
                ShapeTE ste = (ShapeTE) te;
                ste.onChiselUse(player, side, hitX, hitY, hitZ);
            }
            return true;
        }
        IBlockState state = getWorldBlockState(world, pos);
        Block block = state.getBlock();
        if (block == Blocks.glass || block == Blocks.glass_pane || block == Blocks.glowstone || block == Blocks.ice) {
            setWorldBlockState(world, pos, getDefaultBlockState(Blocks.air), 3);
            if (!world.isRemote) {
                dropBlockAsItem(world, pos, state);
                playWorldAuxSFX(world, 2001, pos, getDefaultBlockState(Blocks.stone)); // block breaking sound and
                                                                                       // particles
            }
            return true;
        }
        return false;
    }

    void dropBlockAsItem(World world, BlockPos pos, IBlockState state) {
        ItemStack stack = blockStackWithState(state, 1);
        spawnBlockStackAsEntity(world, pos, stack);
    }

}
