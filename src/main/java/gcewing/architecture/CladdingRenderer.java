// ------------------------------------------------------------------------------
//
// ArchitectureCraft - Cladding Item Renderer
//
// ------------------------------------------------------------------------------

package gcewing.architecture;

import static gcewing.architecture.BaseBlockUtils.*;
import static gcewing.architecture.BaseUtils.*;

import net.minecraft.block.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

import gcewing.architecture.BaseModClient.*;

public class CladdingRenderer implements ICustomRenderer {

    public void renderBlock(IBlockAccess world, BlockPos pos, IBlockState state, IRenderTarget target,
            EnumWorldBlockLayer layer, Trans3 t) {}

    public void renderItemStack(ItemStack stack, IRenderTarget target, Trans3 t) {
        NBTTagCompound nbt = stack.getTagCompound();
        if (nbt != null) {
            String blockName = nbt.getString("block");
            int meta = stack.getItemDamage();
            Block block = Block.getBlockFromName(blockName);
            if (block != null) {
                IBlockState state = getBlockStateFromMeta(block, meta);
                if (state != null) {
                    IIcon sprite = getSpriteForBlockState(state);
                    if (sprite != null) {
                        ITexture texture = BaseTexture.fromSprite(sprite);
                        IModel model = ArchitectureCraft.mod.client.getModel("shape/cladding.smeg");
                        model.render(t, target, texture);
                    }
                }
            }
        }
    }

}
