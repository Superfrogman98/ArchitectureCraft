// -----------------------------------------------------------------
//
// ArchitectureCraft - Base class for special shape renderers
//
// -----------------------------------------------------------------

package gcewing.architecture;

import static gcewing.architecture.BaseBlockUtils.*;

import net.minecraft.world.*;

import gcewing.architecture.BaseModClient.*;

public abstract class RenderShape {

    protected final IBlockAccess blockWorld;
    protected final BlockPos blockPos;
    protected final ShapeTE te;
    protected final ITexture[] textures;
    protected final Trans3 t;
    protected final IRenderTarget target;

    public RenderShape(ShapeTE te, ITexture[] textures, Trans3 t, IRenderTarget target) {
        this.te = te;
        this.blockWorld = getTileEntityWorld(te);
        this.blockPos = te.getPos();
        this.textures = textures;
        this.t = t;
        this.target = target;
    }

    protected abstract void render();

    protected IModel getModel(String name) {
        return ArchitectureCraft.mod.client.getModel(name);
    }

    // protected TileEntity getTileEntityInGlobalDir(EnumFacing gdir) {
    // if (blockWorld != null)
    // return blockWorld.getTileEntity(blockPos.offset(gdir));
    // else
    // return null;
    // }
    //
    // protected ShapeTE getShapeTEInGlobalDir(EnumFacing gdir) {
    // TileEntity te = getTileEntityInGlobalDir(gdir);
    // if (te instanceof ShapeTE)
    // return (ShapeTE)te;
    // else
    // return null;
    // }

}
