// ------------------------------------------------------------------------------
//
// ArchitectureCraft - ShapeItem - Client
//
// ------------------------------------------------------------------------------

package gcewing.architecture.common.shape;

import static gcewing.architecture.compat.BlockCompatUtils.getWorldBlockState;
import static gcewing.architecture.compat.BlockCompatUtils.getWorldTileEntity;
import static gcewing.architecture.compat.BlockCompatUtils.setWorldBlockState;
import static gcewing.architecture.compat.Vector3.getDirectionVec;
import static gcewing.architecture.util.Utils.oppositeFacing;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import gcewing.architecture.client.gui.widget.GuiText;
import gcewing.architecture.common.item.ArchitectureItemBlock;
import gcewing.architecture.common.tile.TileShape;
import gcewing.architecture.compat.BlockPos;
import gcewing.architecture.compat.IBlockState;
import gcewing.architecture.compat.Vec3i;
import gcewing.architecture.compat.Vector3;
import gcewing.architecture.util.Utils;

public class ShapeItem extends ArchitectureItemBlock {

    static Random rand = new Random();

    public ShapeItem(Block block) {
        super(block);
    }

    /**
     * Converts the given ItemStack damage value into a metadata value to be placed in the world when this Item is
     * placed as a Block (mostly used with ItemBlocks).
     */
    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing face,
            float hitX, float hitY, float hitZ, IBlockState newState) {
        if (!setWorldBlockState(world, pos, newState, 3)) return false;
        Vec3i d = getDirectionVec(face);
        Vector3 hit = new Vector3(hitX - d.getX() - 0.5, hitY - d.getY() - 0.5, hitZ - d.getZ() - 0.5);
        TileShape te = TileShape.get(world, pos);
        if (te != null) {
            te.readFromItemStack(stack);
            if (te.shape != null) {
                BlockPos npos = te.getPos().offset(oppositeFacing(face));
                IBlockState nstate = getWorldBlockState(world, npos);
                TileEntity nte = getWorldTileEntity(world, npos);
                te.shape.orientOnPlacement(player, te, npos, nstate, nte, face, hit);
            }
        }
        return true;
    }

    @Override
    public boolean getShareTag() {
        return true;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List lines, boolean par4) {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag != null) {
            int id = tag.getInteger("Shape");
            Shape shape = Shape.forId(id);
            if (shape != null) lines.set(0, GuiText.valueOf(GuiText.class, shape.name()).getLocal());
            else lines.set(0, lines.get(0) + " (" + id + ")");
            Block baseBlock = Block.getBlockFromName(tag.getString("BaseName"));
            int baseMetadata = tag.getInteger("BaseData");
            if (baseBlock != null) lines.add(Utils.displayNameOfBlock(baseBlock, baseMetadata));
            if (shape.title.toLowerCase().contains("double"))
                lines.add(StatCollector.translateToLocal("tooltip.doubleslope"));
        }
    }

}
