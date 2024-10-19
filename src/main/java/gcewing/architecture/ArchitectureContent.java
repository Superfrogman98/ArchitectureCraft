package gcewing.architecture;

import static gcewing.architecture.ArchitectureCraft.ASSET_KEY;
import static gcewing.architecture.ArchitectureCraft.REGISTRY_PREFIX;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import gcewing.architecture.client.render.model.IArchitectureModel;
import gcewing.architecture.common.CreativeTab;
import gcewing.architecture.common.block.BlockSawbench;
import gcewing.architecture.common.block.BlockShape;
import gcewing.architecture.common.item.ArchitectureItemBlock;
import gcewing.architecture.common.item.ItemArchitecture;
import gcewing.architecture.common.item.ItemChisel;
import gcewing.architecture.common.item.ItemCladding;
import gcewing.architecture.common.item.ItemGlowBrush;
import gcewing.architecture.common.item.ItemHammer;
import gcewing.architecture.common.shape.ShapeItem;
import gcewing.architecture.common.tile.TileSawbench;
import gcewing.architecture.common.tile.TileShape;

public class ArchitectureContent {

    //
    // Blocks and Items
    //
    public final List<Block> registeredBlocks = new ArrayList<>();
    public final List<Item> registeredItems = new ArrayList<>();

    // TODO: Heavy read, less heavy write - look into alternative data structures for this
    protected final Map<ResourceLocation, IArchitectureModel> modelCache = new ConcurrentHashMap<>();

    public static BlockSawbench blockSawbench;
    public static BlockShape blockShape;
    public static BlockShape blockShapeSE;
    public static Item itemSawblade;
    public static Item itemLargePulley;
    public static Item itemChisel;
    public static Item itemHammer;

    public static Item itemGlowBrush;
    public static ItemCladding itemCladding;

    public void preInit(FMLPreInitializationEvent e) {
        registerBlocks();
        registerItems();
        registerTileEntities();
    }

    public void postInit(FMLPostInitializationEvent e) {
        registerRecipes();
    }

    protected void registerBlocks() {
        blockSawbench = registerBlock(new BlockSawbench(), "sawbench", ArchitectureItemBlock.class);
        blockSawbench.setHardness(2.0F);
        blockShape = registerBlock(new BlockShape(), "shape", ShapeItem.class);
        blockShapeSE = registerBlock(new BlockShape(), "shapeSE", ShapeItem.class);
    }

    protected void registerTileEntities() {
        GameRegistry.registerTileEntity(TileSawbench.class, "gcewing.sawbench");
        GameRegistry.registerTileEntity(TileShape.class, "gcewing.shape");
    }

    protected void registerItems() {
        itemSawblade = registerItem(new ItemArchitecture(), "sawblade");
        itemLargePulley = registerItem(new ItemArchitecture(), "largePulley");
        itemChisel = registerItem(new ItemChisel(), "chisel");
        itemHammer = registerItem(new ItemHammer(), "hammer");
        itemCladding = registerItem(new ItemCladding(), "cladding");
        itemGlowBrush = registerItem(new ItemGlowBrush(), "glowbrush");
    }

    protected void registerRecipes() {
        if (!Loader.isModLoaded("dreamcraft")) {
            // spotless:off
            ItemStack orangeDye = new ItemStack(Items.dye, 1, 14);
            GameRegistry.addRecipe(new ItemStack(blockSawbench, 1),
                    "I*I",
                    "/0/",
                    "/_/",
                    'I', Items.iron_ingot,
                    '*', itemSawblade,
                    '/', Items.stick,
                    '_', Blocks.wooden_pressure_plate,
                    '0', itemLargePulley
            );
            GameRegistry.addRecipe(new ItemStack(itemSawblade, 1),
                    " I ",
                    "I/I",
                    " I ",
                    'I', Items.iron_ingot,
                    '/', Items.stick);
            GameRegistry.addRecipe(new ItemStack(itemLargePulley, 1),
                    " W ",
                    "W/W",
                    " W ",
                    'W', Blocks.planks,
                    '/', Items.stick);
            GameRegistry.addRecipe(new ItemStack(itemChisel, 1),
                    "I ",
                    "ds",
                    'I', Items.iron_ingot,
                    's', Items.stick,
                    'd', orangeDye);
            GameRegistry.addRecipe(new ItemStack(itemHammer, 1),
                    "II ",
                    "dsI",
                    "ds ",
                    'I', Items.iron_ingot,
                    's', Items.stick,
                    'd', orangeDye);
            GameRegistry.addRecipe(new ItemStack(itemGlowBrush, 1),
                    "  G",
                    " W ",
                    "s  ",
                    'G', Items.glowstone_dust,
                    's', Items.stick,
                    'W', Blocks.wool);
            // spotless:on
        }
    }

    // --------------- Block registration ----------------------------------------------------------

    public <T extends Block> T registerBlock(T block, String name, Class<? extends ItemBlock> itemClass) {
        String qualName = ASSET_KEY + ":" + name;
        block.setBlockName(qualName);
        block.setBlockTextureName(REGISTRY_PREFIX + ":" + name);
        GameRegistry.registerBlock(block, itemClass, name);
        block.setCreativeTab(CreativeTab.AC_TAB);
        registeredBlocks.add(block);
        return block;
    }

    // --------------- Item registration ----------------------------------------------------------

    public <T extends Item> T registerItem(T item, String name) {
        String qualName = ASSET_KEY + ":" + name;
        item.setUnlocalizedName(qualName);
        item.setTextureName(REGISTRY_PREFIX + ":" + name);
        GameRegistry.registerItem(item, name);
        item.setCreativeTab(CreativeTab.AC_TAB);
        registeredItems.add(item);
        return item;
    }

}
