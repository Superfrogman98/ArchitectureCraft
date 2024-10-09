// ------------------------------------------------------------------------------
//
// ArchitectureCraft - Sawbench Tile Entity
//
// ------------------------------------------------------------------------------

package gcewing.architecture.common.tile;

import static gcewing.architecture.common.shape.Shape.*;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import gcewing.architecture.ArchitectureCraft;
import gcewing.architecture.common.config.ArchitectConfiguration;
import gcewing.architecture.common.shape.Shape;
import gcewing.architecture.common.shape.ShapePage;
import gcewing.architecture.compat.Directions;

public class TileSawbench extends TileArchitectureInventory implements IRestrictedDroppingInventory {

    final public static int materialSlot = 0;
    final public static int resultSlot = 1;

    final public static int[] materialSideSlots = { materialSlot };
    final public static int[] resultSideSlots = { resultSlot };

    public static final boolean allowAutomation = false;

    public static final ShapePage[] pages = {
            new ShapePage(
                    "Roofing",
                    RoofTile,
                    RoofOuterCorner,
                    RoofInnerCorner,
                    RoofRidge,
                    RoofSmartRidge,
                    RoofValley,
                    RoofSmartValley,
                    RoofOverhang,
                    RoofOverhangOuterCorner,
                    RoofOverhangInnerCorner,
                    RoofOverhangGableLH,
                    RoofOverhangGableRH,
                    RoofOverhangGableEndLH,
                    RoofOverhangGableEndRH,
                    RoofOverhangRidge,
                    RoofOverhangValley,
                    BevelledOuterCorner,
                    BevelledInnerCorner),
            new ShapePage(
                    "Rounded",
                    Cylinder,
                    CylinderHalf,
                    CylinderQuarter,
                    CylinderLargeQuarter,
                    AnticylinderLargeQuarter,
                    Pillar,
                    Post,
                    Pole,
                    SphereFull,
                    SphereHalf,
                    SphereQuarter,
                    SphereEighth,
                    SphereEighthLarge,
                    SphereEighthLargeRev),
            new ShapePage(
                    "Classical",
                    PillarBase,
                    Pillar,
                    DoricCapital,
                    DoricTriglyph,
                    DoricTriglyphCorner,
                    DoricMetope,
                    IonicCapital,
                    CorinthianCapital,
                    Architrave,
                    ArchitraveCorner,
                    CorniceLH,
                    CorniceRH,
                    CorniceEndLH,
                    CorniceEndRH,
                    CorniceRidge,
                    CorniceValley,
                    CorniceBottom),
            new ShapePage("Window", WindowFrame, WindowCorner, WindowMullion),
            new ShapePage("Arches", ArchD1, ArchD2, ArchD3A, ArchD3B, ArchD3C, ArchD4A, ArchD4B, ArchD4C),
            new ShapePage(
                    "Railings",
                    BalustradePlain,
                    BalustradePlainOuterCorner,
                    BalustradePlainInnerCorner,
                    BalustradePlainWithNewel,
                    BalustradePlainEnd,
                    BanisterPlainTop,
                    BanisterPlain,
                    BanisterPlainBottom,
                    BanisterPlainEnd,
                    BanisterPlainInnerCorner,
                    BalustradeFancy,
                    BalustradeFancyCorner,
                    BalustradeFancyWithNewel,
                    BalustradeFancyNewel,
                    BanisterFancyTop,
                    BanisterFancy,
                    BanisterFancyBottom,
                    BanisterFancyEnd,
                    BanisterFancyNewelTall),
            new ShapePage(
                    "Other",
                    CladdingSheet,
                    Slab,
                    Stairs,
                    StairsOuterCorner,
                    StairsInnerCorner,
                    SlopeTileA1,
                    SlopeTileA2,
                    SlopeTileB1,
                    SlopeTileB2,
                    SlopeTileB3,
                    SlopeTileC1,
                    SlopeTileC2,
                    SlopeTileC3,
                    SlopeTileC4,
                    AngledRoofRidge),
            new ShapePage(
                    "Glow",
                    SquareSE,
                    SlabSE,
                    RoofTileSE,
                    SlopeTileA1SE,
                    SlopeTileA2SE,
                    SlopeTileB1SE,
                    SlopeTileB2SE,
                    SlopeTileB3SE,
                    SlopeTileC1SE,
                    SlopeTileC2SE,
                    SlopeTileC3SE,
                    SlopeTileC4SE),
            new ShapePage("Curves", Curve2b2A, Curve2b2B),
            new ShapePage(
                    "DoubleSlopes",
                    DoubleRoofTile,
                    DoubleSlopeAStart,
                    DoubleSlopeAEnd,
                    DoubleSlopeBStart,
                    DoubleSlopeBMiddle,
                    DoubleSlopeBEnd,
                    DoubleSlopeC1,
                    DoubleSlopeC2,
                    DoubleSlopeC3,
                    DoubleSlopeC4),
            new ShapePage("CornerSlopes", StraightCornerC1, StraightCornerC2, StraightCornerC3, StraightCornerC4) };

    public final IInventory inventory = new InventoryBasic("Items", false, 2);
    public int selectedPage = 0;
    public final int[] selectedSlots = new int[pages.length];
    public boolean pendingMaterialUsage = false; // Material for the stack in the result slot
    // has not yet been removed from the material slot

    public Shape getSelectedShape() {
        if (isSelectedPageInRange()) {
            int slot = selectedSlots[selectedPage];
            if (slot >= 0 && slot < pages[selectedPage].size()) return pages[selectedPage].get(slot);
        }
        return null;
    }

    public int getSelectedPageIndex() {
        if (isSelectedPageInRange()) {
            return selectedPage;
        }
        return -1;
    }

    public int getSelectedShapeIndex() {
        if (isSelectedPageInRange()) {
            return selectedSlots[selectedPage];
        }
        return -1;
    }

    private boolean isSelectedPageInRange() {
        return selectedPage >= 0 && selectedPage < pages.length;
    }

    @Override
    protected IInventory getInventory() {
        return inventory;
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack stack) {
        super.setInventorySlotContents(i, stack);
        updateResultSlot();
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        if (slot == resultSlot) usePendingMaterial();
        ItemStack result = super.decrStackSize(slot, amount);
        updateResultSlot();
        return result;
    }

    public ItemStack usePendingMaterial() {
        ItemStack origMaterialStack = getStackInSlot(materialSlot);
        if (pendingMaterialUsage) {
            pendingMaterialUsage = false;
            inventory.decrStackSize(materialSlot, materialMultiple());
        }
        return origMaterialStack;
    }

    public void returnUnusedMaterial(ItemStack origMaterialStack) {
        if (!pendingMaterialUsage) {
            ItemStack materialStack = getStackInSlot(materialSlot);
            ItemStack resultStack = getStackInSlot(resultSlot);
            int m = materialMultiple();
            int n = resultMultiple();
            if (resultStack != null && resultStack.stackSize == n) {
                if (materialStack != null) materialStack.stackSize += m;
                else {
                    materialStack = origMaterialStack;
                    materialStack.stackSize = m;
                }
                inventory.setInventorySlotContents(materialSlot, materialStack);
                pendingMaterialUsage = true;
            }
        }
    }

    /**
     * Returns an array containing the indices of the slots that can be accessed by automation on the given side of this
     * block.
     */
    public int[] getAccessibleSlotsFromSide(int side) {
        if (side == 1) // UP
            return materialSideSlots;
        else return resultSideSlots;
    }

    @Override
    public void readFromNBT(NBTTagCompound tc) {
        super.readFromNBT(tc);
        selectedPage = tc.getInteger("Page");
        int[] ss = tc.getIntArray("Slots");
        if (ss != null) for (int page = 0; page < pages.length; page++) {
            int slot = page < ss.length ? ss[page] : 0;
            selectedSlots[page] = slot >= 0 && slot < pages[page].size() ? slot : 0;
        }
        pendingMaterialUsage = tc.getBoolean("PMU");
    }

    @Override
    public void writeToNBT(NBTTagCompound tc) {
        super.writeToNBT(tc);
        tc.setInteger("Page", selectedPage);
        tc.setIntArray("Slots", selectedSlots);
        tc.setBoolean("PMU", pendingMaterialUsage);
    }

    public void setSelectedShape(int page, int slot) {
        if (page >= 0 && page < pages.length) {
            selectedPage = page;
            if (slot >= 0 && slot < pages[selectedPage].size()) {
                selectedSlots[selectedPage] = slot;
                markDirty();
                updateResultSlot();
                ArchitectureCraft.sendTileEntityUpdate(this);
            }
        }
    }

    public void updateResultSlot() {
        ItemStack oldResult = getStackInSlot(resultSlot);
        if (oldResult == null || pendingMaterialUsage) {
            ItemStack resultStack = makeResultStack();
            if (!ItemStack.areItemStacksEqual(resultStack, oldResult))
                inventory.setInventorySlotContents(resultSlot, resultStack);
            pendingMaterialUsage = resultStack != null;
        }
    }

    protected ItemStack makeResultStack() {
        Shape resultShape = getSelectedShape();
        boolean shaderEmissive = getSelectedPageIndex() == 7;
        if (resultShape != null) {
            ItemStack materialStack = getStackInSlot(materialSlot);
            if (materialStack != null && materialStack.stackSize >= resultShape.materialUsed) {
                Item materialItem = materialStack.getItem();

                if (materialItem instanceof ItemBlock) {
                    Block materialBlock = Block.getBlockFromItem(materialItem);
                    if (isAcceptableMaterial(materialBlock)) {
                        return resultShape.kind.newStack(
                                resultShape,
                                materialBlock,
                                materialStack.getItemDamage(),
                                resultShape.itemsProduced,
                                shaderEmissive);
                    }
                }
            }
        }
        return null;
    }

    protected boolean isAcceptableMaterial(Block block) {
        if (block == Blocks.glass || block == Blocks.stained_glass
                || block instanceof BlockSlab
                || ArchitectConfiguration.acceptableMaterialsFromConfig.contains(block.getUnlocalizedName()))
            return true;
        return block.renderAsNormalBlock() && !block.hasTileEntity();
    }

    public int materialMultiple() {
        int factor = 1;
        ItemStack materialStack = getStackInSlot(materialSlot);
        if (materialStack != null) {
            Block materialBlock = Block.getBlockFromItem(materialStack.getItem());
            if (materialBlock instanceof BlockSlab) factor = 2;
        }
        Shape shape = getSelectedShape();
        if (shape != null) return factor * shape.materialUsed;
        return 0;
    }

    public int resultMultiple() {
        // return productMadeForShape[selectedShape];
        Shape shape = getSelectedShape();
        if (shape != null) return shape.itemsProduced;
        return 0;
    }

    /**
     * Returns true if automation can insert the given item in the given slot from the given side. Args: Slot, item,
     * side
     */
    @Override
    public boolean canInsertItem(int slot, ItemStack stack, int side) {
        return slot == materialSlot;
    }

    /**
     * Returns true if automation can extract the given item in the given slot from the given side. Args: Slot, item,
     * side
     */
    @Override
    public boolean canExtractItem(int slot, ItemStack stack, int side) {
        if (side == Directions.DOWN) return allowAutomation && slot == resultSlot;
        else return slot == materialSlot;
    }

    @Override
    public int[] getDroppingSlots() {
        return materialSideSlots;
    }

}
