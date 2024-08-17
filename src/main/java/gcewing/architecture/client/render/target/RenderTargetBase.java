// ------------------------------------------------------------------------------------------------
//
// Greg's Mod Base for 1.7 Version B - Rendering target base class
//
// ------------------------------------------------------------------------------------------------

package gcewing.architecture.client.render.target;

import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IIcon;

import gcewing.architecture.client.render.ITexture;
import gcewing.architecture.client.texture.ArchitectureTexture;
import gcewing.architecture.compat.Vector3;

public abstract class RenderTargetBase implements IRenderTarget {

    // Position of block in rendering coordinates (may be different from world coordinates)
    protected final double blockX;
    protected final double blockY;
    protected final double blockZ;

    protected int verticesPerFace;
    protected int vertexCount;
    protected ITexture texture;
    protected Vector3 normal;
    protected EnumFacing face;
    protected float red = 1, green = 1, blue = 1, alpha = 1;
    protected float shade;
    protected boolean expandTrianglesToQuads;
    protected boolean textureOverride;

    public RenderTargetBase(double x, double y, double z, IIcon overrideIcon) {
        blockX = x;
        blockY = y;
        blockZ = z;
        if (overrideIcon != null) {
            texture = ArchitectureTexture.fromSprite(overrideIcon);
            textureOverride = true;
        }
    }

    // ---------------------------- IRenderTarget ----------------------------

    public boolean isRenderingBreakEffects() {
        return textureOverride;
    }

    public void beginTriangle() {
        setMode(3);
    }

    public void beginQuad() {
        setMode(4);
    }

    protected void setMode(int mode) {
        if (vertexCount != 0) throw new IllegalStateException("Changing mode in mid-face");
        verticesPerFace = mode;
    }

    public void setTexture(ITexture texture) {
        if (!textureOverride) {
            if (texture == null)
            {
                //use fallback texture rather than crashing the game
                this.texture = ArchitectureTexture.fromSprite(Blocks.planks.getBlockTextureFromSide(0));
            }
            else
            {
                this.texture = texture;
            }
        }
    }

    public void setColor(double r, double g, double b, double a) {
        red = (float) r;
        green = (float) g;
        blue = (float) b;
        alpha = (float) a;
    }

    public void setNormal(Vector3 n) {
        normal = n;
        face = n.facing();
        shade = (float) (0.6 * n.x * n.x + 0.8 * n.z * n.z + (n.y > 0 ? 1 : 0.5) * n.y * n.y);
    }

    public void addVertex(Vector3 p, double u, double v) {
        if (texture.isProjected()) addProjectedVertex(p, face);
        else addUVVertex(p, u, v);
    }

    public void addUVVertex(Vector3 p, double u, double v) {
        double iu, iv;
        if (verticesPerFace == 0) throw new IllegalStateException("No face active");
        if (vertexCount >= verticesPerFace) throw new IllegalStateException("Too many vertices in face");
        if (normal == null) throw new IllegalStateException("No normal");
        if (texture == null) throw new IllegalStateException("No texture");
        iu = texture.interpolateU(u);
        iv = texture.interpolateV(v);
        rawAddVertex(p, iu, iv);
        if (++vertexCount == 3 && expandTrianglesToQuads && verticesPerFace == 3) {
            rawAddVertex(p, iu, iv);
        }
    }

    public void endFace() {
        if (vertexCount < verticesPerFace) {
            throw new IllegalStateException("Too few vertices in face");
        }
        vertexCount = 0;
        verticesPerFace = 0;
    }

    public void finish() {
        if (vertexCount > 0) throw new IllegalStateException("Rendering ended with incomplete face");
    }

    // -----------------------------------------------------------------------------------------

    protected abstract void rawAddVertex(Vector3 p, double u, double v);

    public float r() {
        return (float) (red * texture.red());
    }

    public float g() {
        return (float) (green * texture.green());
    }

    public float b() {
        return (float) (blue * texture.blue());
    }

    public float a() {
        return alpha;
    }

    // Add vertex with texture coords projected from the given direction
    public void addProjectedVertex(Vector3 p, EnumFacing face) {
        double x = p.x - blockX;
        double y = p.y - blockY;
        double z = p.z - blockZ;

        double u, v;
        v = switch (face) {
            case DOWN -> {
                u = x;
                yield 1 - z;
            }
            case UP -> {
                u = x;
                yield z;
            }
            case NORTH -> {
                u = 1 - x;
                yield 1 - y;
            }
            case SOUTH -> {
                u = x;
                yield 1 - y;
            }
            case WEST -> {
                u = 1 - z;
                yield 1 - y;
            }
            case EAST -> {
                u = z;
                yield 1 - y;
            }
            default -> {
                u = 0;
                yield 0;
            }
        };
        addUVVertex(p, u, v);
    }

}
