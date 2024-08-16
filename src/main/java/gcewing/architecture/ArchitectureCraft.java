// ------------------------------------------------------
//
// ArchitectureCraft - Main
//
// ------------------------------------------------------

package gcewing.architecture;

import java.io.File;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.server.management.PlayerManager;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import gcewing.architecture.client.render.model.IArchitectureModel;
import gcewing.architecture.client.render.model.ObjJsonModel;
import gcewing.architecture.common.config.ArchitectConfiguration;
import gcewing.architecture.common.network.DataChannel;

@Mod(
        modid = ArchitectureCraft.MOD_ID,
        name = ArchitectureCraft.MOD_NAME,
        version = ArchitectureCraft.VERSION,
        acceptedMinecraftVersions = "[1.7.10]")
public class ArchitectureCraft {

    public static final String MOD_NAME = "ArchitectureCraft";
    public static final String MOD_ID = "ArchitectureCraft";
    public static final String VERSION = Tags.VERSION;
    public static final String ASSET_KEY = MOD_ID.toLowerCase();
    public static final String REGISTRY_PREFIX = MOD_ID.toLowerCase();
    public static final ArchitectureContent content = new ArchitectureContent();
    public static ArchitectureCraftClient client;

    @Mod.Instance(MOD_ID)
    public static ArchitectureCraft mod;

    public static DataChannel channel;

    public ArchitectureCraft() {
        super();
        channel = new DataChannel(MOD_ID);
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        ArchitectConfiguration.init(e.getSuggestedConfigurationFile());
        content.preInit(e);
        if (e.getSide().isClient()) {
            client = initClient();
        }
        if (client != null) client.preInit(e);

    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);
        if (client != null) client.init(e);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        content.postInit(e);
        if (client != null) client.postInit(e);
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new ArchitectureGuiHandler());
    }

    public ArchitectureCraftClient initClient() {
        return new ArchitectureCraftClient(this);
    }

    // --------------- Resources ----------------------------------------------------------

    public static ResourceLocation resourceLocation(String path) {
        if (path.contains(":")) return new ResourceLocation(path);
        else return new ResourceLocation(ASSET_KEY, path);
    }

    public ResourceLocation modelLocation(String path) {
        return resourceLocation("models/" + path);
    }

    public IArchitectureModel getModel(String name) {
        ResourceLocation loc = modelLocation(name);
        IArchitectureModel model = content.modelCache.get(loc);
        if (model == null) {
            model = ObjJsonModel.fromResource(loc);
            content.modelCache.put(loc, model);
        }
        return model;
    }

    // ------------------------- Network --------------------------------------------------

    public static void sendTileEntityUpdate(TileEntity te) {
        Packet packet = te.getDescriptionPacket();
        if (packet != null) {
            int x = te.xCoord >> 4;
            int z = te.zCoord >> 4;
            WorldServer world = (WorldServer) te.getWorldObj();
            ServerConfigurationManager cm = FMLCommonHandler.instance().getMinecraftServerInstance()
                    .getConfigurationManager();
            PlayerManager pm = world.getPlayerManager();
            for (EntityPlayerMP player : cm.playerEntityList) if (pm.isPlayerWatchingChunk(player, x, z)) {
                player.playerNetServerHandler.sendPacket(packet);
            }
        }
    }
}
