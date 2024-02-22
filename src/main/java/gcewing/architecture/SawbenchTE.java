// ------------------------------------------------------------------------------
//
// ArchitectureCraft - Sawbench Tile Entity
//
// ------------------------------------------------------------------------------

package gcewing.architecture;

import static gcewing.architecture.Shape.*;

import java.util.Arrays;
import java.util.List;

import net.minecraft.block.*;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;

public class SawbenchTE extends BaseTileInventory implements IRestrictedDroppingInventory {

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
                    SlopeTileC4),
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
                    SlopeTileC4SE) };

    public final IInventory inventory = new InventoryBasic("Items", false, 2);
    public int selectedPage = 0;
    public final int[] selectedSlots = new int[pages.length];
    public boolean pendingMaterialUsage = false; // Material for the stack in the result slot
    // has not yet been removed from the material slot

    // Loads the list of acceptable materials from the config file
    /*
     * materials { S:UnlocalizedNames < tile.chisel.stained_glass tile.chisel.glass > }
     */
    private static final List<String> acceptableMaterialsFromConfig = Arrays.asList(
            ArchitectureCraft.mod.config
                    .get("materials", "UnlocalizedNames", new String[] { "tile.chisel.stained_glass" })
                    .getStringList());

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
        // System.out.printf("SawbenchTE.decrStackSize: %d by %d on %s\n", slot, amount, worldObj);
        if (slot == resultSlot) usePendingMaterial();
        ItemStack result = super.decrStackSize(slot, amount);
        updateResultSlot();
        return result;
    }

    public ItemStack usePendingMaterial() {
        // System.out.printf("SawbenchTE.usePendingMaterial: pmu = %s on %s\n", pendingMaterialUsage, worldObj);
        ItemStack origMaterialStack = getStackInSlot(materialSlot);
        if (pendingMaterialUsage) {
            pendingMaterialUsage = false;
            inventory.decrStackSize(materialSlot, materialMultiple());
        }
        return origMaterialStack;
    }

    public void returnUnusedMaterial(ItemStack origMaterialStack) {
        // if (!worldObj.isRemote)
        // System.out.printf("SawbenchTE.returnUnusedMaterial: before: pmu = %s, material = %s, result = %s\n",
        // pendingMaterialUsage, getStackInSlot(materialSlot), getStackInSlot(resultSlot));
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
        // if (!worldObj.isRemote)
        // System.out.printf("SawbenchTE.returnUnusedMaterial: after: pmu = %s, material = %s, result = %s\n",
        // pendingMaterialUsage, getStackInSlot(materialSlot), getStackInSlot(resultSlot));
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
                BaseMod.sendTileEntityUpdate(this);
            }
        }
    }

    void updateResultSlot() {
        // System.out.printf("SawbenchTE.updateResultSlot: pmu = %s on %s\n", pendingMaterialUsage, worldObj);
        // showMaterial();
        ItemStack oldResult = getStackInSlot(resultSlot);
        if (oldResult == null || pendingMaterialUsage) {
            ItemStack resultStack = makeResultStack();
            if (!ItemStack.areItemStacksEqual(resultStack, oldResult))
                inventory.setInventorySlotContents(resultSlot, resultStack);
            pendingMaterialUsage = resultStack != null;
            // System.out.printf("SawbenchTE.updateResultSlot: now pmu = %s on %s\n", pendingMaterialUsage, worldObj);
        }
    }

    protected void showMaterial() {
        ItemStack stack = getStackInSlot(materialSlot);
        if (stack != null) System.out.printf("SawbenchTE: Material = %s\n", stack);
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
                || acceptableMaterialsFromConfig.contains(block.getUnlocalizedName()))
            return true;
        return block.renderAsNormalBlock() && !block.hasTileEntity();
    }

    int materialMultiple() {
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

    int resultMultiple() {
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
        if (side == BaseDirections.DOWN) return allowAutomation && slot == resultSlot;
        else return slot == materialSlot;
    }

    @Override
    public int[] getDroppingSlots() {
        return materialSideSlots;
    }

}
