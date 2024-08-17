// ------------------------------------------------------
//
// Greg's Mod Base for 1.7 Version B - Configuration
//
// ------------------------------------------------------

package gcewing.architecture.common.config;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraftforge.common.config.Configuration;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gcewing.architecture.ArchitectureCraft;

public class ArchitectConfiguration {

    public static Configuration config;

    public static final String CATEGORY_MATERIALS = "materials";
    public static List<String> acceptableMaterialsFromConfig = Arrays.asList(
            "tile.chisel.glass",
            "tile.chisel.stained_glass_black",
            "tile.chisel.stained_glass_red",
            "tile.chisel.stained_glass_green",
            "tile.chisel.stained_glass_brown",
            "tile.chisel.stained_glass_blue",
            "tile.chisel.stained_glass_purple",
            "tile.chisel.stained_glass_cyan",
            "tile.chisel.stained_glass_lightgray",
            "tile.chisel.stained_glass_gray",
            "tile.chisel.stained_glass_pink",
            "tile.chisel.stained_glass_lime",
            "tile.chisel.stained_glass_yellow",
            "tile.chisel.stained_glass_lightblue",
            "tile.chisel.stained_glass_magenta",
            "tile.chisel.stained_glass_orange",
            "tile.chisel.stained_glass_white");
    public static Map<String, int[]> shapeRatioOverides = new HashMap<>();

    private static final String[] defaultRatioOverrride = new String[] { "Cladding|1|1" };

    public static void loadConfig(File configFile) {
        config = new Configuration(configFile);

        config.load();
        load();

        FMLCommonHandler.instance().bus().register(new ChangeListener());
    }

    private static void load() {
        acceptableMaterialsFromConfig = Arrays.asList(
                config.getStringList(
                        "UnlocalizedNames",
                        CATEGORY_MATERIALS,
                        acceptableMaterialsFromConfig.toArray(new String[0]),
                        "Allows adding additional allowed materials for shapes"));

        String[] CraftingRatios = config.getStringList(
                "MaterialRatios",
                CATEGORY_MATERIALS,
                defaultRatioOverrride,
                "Change the Crafting Ratio of a Shape type format is <Shape>|<input>|<output>");

        for (String s : CraftingRatios) {
            if (s != null) {
                String[] ratio = s.split("\\|");
                shapeRatioOverides.put(ratio[0], new int[] { Integer.parseInt(ratio[1]), Integer.parseInt(ratio[2]) });
            }
        }
        if (config.hasChanged()) config.save();
    }

    public static void loadPostInit() {
        if (config.hasChanged()) config.save();
    }

    public static class ChangeListener {

        @SubscribeEvent
        public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.modID.equalsIgnoreCase(ArchitectureCraft.MOD_ID)) load();
        }
    }

}
