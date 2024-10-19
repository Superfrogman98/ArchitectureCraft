// ------------------------------------------------------------------------------
//
// ArchitectureCraft - Hammer
//
// ------------------------------------------------------------------------------

package gcewing.architecture.common.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import gcewing.architecture.ArchitectureCraft;
import gcewing.architecture.common.CreativeTab;
import gcewing.architecture.common.tile.TileShape;
import gcewing.architecture.compat.BlockPos;

public class ItemGlowBrush extends ItemArchitecture {

    public ItemGlowBrush() {
        setMaxStackSize(1);
    }

    @Override
    public CreativeTabs getCreativeTab() {
        return CreativeTab.AC_TAB;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side,
            float hitX, float hitY, float hitZ) {
        TileShape te = TileShape.get(world, pos);
        if (te != null && te instanceof TileShape) {
            NBTTagCompound shape = new NBTTagCompound();
            te.writeToNBT(shape);
            if (!player.isSneaking()) {
                // set the block to the glow variant, and set it's meta to 15 for it to actual give off light
                world.setBlock(pos.x, pos.y, pos.z, ArchitectureCraft.content.blockShapeSE, 15, 3);
                world.getTileEntity(pos.x, pos.y, pos.z).readFromNBT(shape);
            } else {
                // set the block to the non-glowing variant, and set it's meta to 0 for it to give no light
                world.setBlock(pos.x, pos.y, pos.z, ArchitectureCraft.content.blockShape, 0, 3);
                world.getTileEntity(pos.x, pos.y, pos.z).readFromNBT(shape);
            }
            return true;
        }
        return false;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List lines, boolean par4) {
        lines.add(StatCollector.translateToLocal("tooltip.glowbrush_1"));
        lines.add(StatCollector.translateToLocal("tooltip.glowbrush_2"));
    }
}
