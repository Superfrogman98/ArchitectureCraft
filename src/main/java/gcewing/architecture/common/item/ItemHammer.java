// ------------------------------------------------------------------------------
//
// ArchitectureCraft - Hammer
//
// ------------------------------------------------------------------------------

package gcewing.architecture.common.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gcewing.architecture.common.CreativeTab;
import gcewing.architecture.common.tile.TileShape;
import gcewing.architecture.compat.BlockPos;

public class ItemHammer extends ItemArchitecture {

    public IIcon locked;

    public ItemHammer() {
        setMaxStackSize(1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister ir) {
        this.itemIcon = ir.registerIcon("architecturecraft:hammer");
        locked = ir.registerIcon("architecturecraft:hammer_locked");
    }

    @Override
    public IIcon getIcon(ItemStack stack, int pass) {

        NBTTagCompound tags = stack.getTagCompound();
        if (tags != null) {
            if (tags.getBoolean("locked")) return locked;
        }
        return itemIcon;
    }

    @Override
    public IIcon getIconIndex(ItemStack stack) {
        return getIcon(stack, 0);
    }

    @Override
    public CreativeTabs getCreativeTab() {
        return CreativeTab.AC_TAB;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side,
            float hitX, float hitY, float hitZ) {
        TileShape te = TileShape.get(world, pos);
        if (te != null) {
            te.onHammerUse(player, side, hitX, hitY, hitZ);
            return true;
        }

        return false;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (player.isSneaking()) {
            NBTTagCompound tags = stack.getTagCompound();
            if (tags == null) {
                tags = new NBTTagCompound();
                stack.setTagCompound(tags);
            }
            tags.setBoolean("locked", !tags.getBoolean("locked"));
            player.swingItem();
        }
        return stack;
    }

}
