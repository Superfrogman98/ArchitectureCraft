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
import net.minecraftforge.common.config.Property;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gcewing.architecture.ArchitectureCraft;

public class ArchitectConfiguration extends Configuration {

    public static Configuration config;
    public static List<String> acceptableMaterialsFromConfig = Arrays.asList("tile.chisel.stained_glass");
    public static Map<String, int[]> shapeRatioOverides = new HashMap<>();

    private static final String[] defaultRatioOverrride = new String[] { "Cladding|1|1" };
    public boolean extended = false;

    public ArchitectConfiguration(File file) {
        super(file);

    }

    public static void init(File configFile) {
        if (config == null) {
            config = new Configuration(configFile);
            loadConfig();
        }
    }

    @SubscribeEvent
    public void onConfigChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.modID.equalsIgnoreCase(ArchitectureCraft.MOD_ID)) loadConfig();
    }

    private static void loadConfig() {
        acceptableMaterialsFromConfig = Arrays.asList(
                config.getStringList(
                        "UnlocalizedNames",
                        "materials",
                        acceptableMaterialsFromConfig.toArray(new String[0]),
                        "Allows adding additional allowed materials for shapes"));

        String[] CraftingRatios = config.getStringList(
                "MaterialRatios",
                "materials",
                defaultRatioOverrride,
                "Change the Crafting Ratio of a Shape type format is <Shape>|<input>|<output>");

        // Loads the list of acceptable materials from the config file
        /*
         * materials { S:UnlocalizedNames < tile.chisel.stained_glass tile.chisel.glass > }
         */
        // = Arrays.asList(ArchitectureCraft.mod.config.get("materials", "UnlocalizedNames", ).getStringList());

        for (String s : CraftingRatios) {
            if (s != null) {
                System.out.println(s);

                String[] ratio = s.split("\\|");
                shapeRatioOverides.put(ratio[0], new int[] { Integer.parseInt(ratio[1]), Integer.parseInt(ratio[2]) });
            }
        }
        if (config.hasChanged()) config.save();
    }

    public String getString(String category, String key, String defaultValue) {
        return get(category, key, defaultValue).getString();
    }

    public String[] getStringList(String category, String key, String... defaultValueList) {
        String defaultValue = String.join(",", defaultValueList);
        String value = getString(category, key, defaultValue);
        return value.split(",");
    }

    @Override
    public Property get(String category, String key, String defaultValue, String comment, Property.Type type) {
        if (!hasKey(category, key)) extended = true;
        return super.get(category, key, defaultValue, comment, type);
    }
}
