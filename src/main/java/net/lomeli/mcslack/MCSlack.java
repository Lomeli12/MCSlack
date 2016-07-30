package net.lomeli.mcslack;

import org.eclipse.jetty.server.Server;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;

import net.lomeli.mcslack.core.Config;
import net.lomeli.mcslack.core.Logger;
import net.lomeli.mcslack.core.handler.ChatEvent;
import net.lomeli.mcslack.core.handler.SlackPostHelper;
import net.lomeli.mcslack.core.handler.SlackReceiveHandler;

@Mod(modid = MCSlack.MOD_ID, name = MCSlack.NAME, version = MCSlack.VERSION, modLanguageAdapter = MCSlack.KOTLIN_ADAPTER, acceptableRemoteVersions = "*")
public class MCSlack {
    public static final String NAME = "MCSlack";
    public static final String MOD_ID = "mcslack";
    public static final String VERSION = "@VERSION@";
    public static final String KOTLIN_ADAPTER = "net.lomeli.mcslack.KotlinAdapter";

    public static Config modConfig;
    public static Logger logger;

    private Server server;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = new Logger();
        logger.logInfo("Loading configs");
        modConfig = new Config(new Configuration(event.getSuggestedConfigurationFile()));
        modConfig.loadConfig();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new ChatEvent());
    }

    @Mod.EventHandler
    public void serverStarted(FMLServerStartingEvent event) {
        try {
            server = new Server(modConfig.getPort());
            server.setHandler(new SlackReceiveHandler());
            server.start();
            SlackPostHelper.INSTANCE.sendBotMessage("MCSlack Starting");
            logger.logInfo("Now listening on port " + modConfig.getPort());
        } catch (Exception e) {
            logger.logInfo("Could not open slack server on port " + modConfig.getPort() + "!");
            e.printStackTrace();
        }
    }

    @Mod.EventHandler
    public void serverStopped(FMLServerStoppedEvent event) {
        try {
            server.stop();
            SlackPostHelper.INSTANCE.sendBotMessage("MCSlack stopping");
            logger.logInfo("Stopped listening on port ");
        } catch (Exception e) {
            logger.logError("Could not stop server!");
            e.printStackTrace();
        }
    }
}
