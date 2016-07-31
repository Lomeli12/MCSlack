package net.lomeli.mcslack

import org.eclipse.jetty.server.Server

import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.common.config.Configuration
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.event.FMLServerStartingEvent
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent

import net.lomeli.mcslack.core.Config
import net.lomeli.mcslack.core.Logger
import net.lomeli.mcslack.core.command.CommandMCSlack
import net.lomeli.mcslack.core.handler.EventHandler
import net.lomeli.mcslack.core.helper.SlackPostHelper
import net.lomeli.mcslack.core.handler.SlackReceiveHandler
import net.lomeli.mcslack.core.helper.LangHelper

@Mod(modid = MCSlack.MOD_ID, name = MCSlack.NAME, version = MCSlack.VERSION, modLanguageAdapter = MCSlack.KOTLIN_ADAPTER, acceptableRemoteVersions = "*")
object MCSlack {
    const val NAME = "MCSlack"
    const val MOD_ID = "mcslack"
    const val VERSION = "@VERSION@"
    const val KOTLIN_ADAPTER = "net.lomeli.mcslack.KotlinAdapter"
    var server: Server? = null
    var modConfig: Config? = null;
    var logger: Logger = Logger();
    var running: Boolean = false;

    @Mod.EventHandler fun preInit(event: FMLPreInitializationEvent) {
        logger.logInfo("Loading configs")
        modConfig = Config(Configuration(event.suggestedConfigurationFile))
        modConfig?.loadConfig()
    }

    @Mod.EventHandler fun init(event: FMLInitializationEvent) {
        logger.logInfo("Registering events")
        MinecraftForge.EVENT_BUS.register(EventHandler())
    }

    @Mod.EventHandler fun serverStarting(event: FMLServerStartingEvent) {
        event.registerServerCommand(CommandMCSlack())
        try {
            server = Server(modConfig!!.port)
            server!!.handler = SlackReceiveHandler()
            server!!.start()
            running = true
            SlackPostHelper.sendBotMessage(LangHelper.translate("mcslack.server.starting", modConfig!!.botName))
            logger.logInfo("Now listening on port ${modConfig?.port}")
        } catch (e: Exception) {
            logger.logError("Could not open slack server on port ${modConfig?.port}!")
            e.printStackTrace()
        }
    }

    @Mod.EventHandler fun serverStopping(event: FMLServerStoppedEvent) {
        try {
            server!!.stop()
            SlackPostHelper.sendBotMessage(LangHelper.translate("mcslack.server.stopping", modConfig!!.botName))
            running = false
            logger.logInfo("Stopped listening on port ${modConfig?.port}")
        } catch (e: Exception) {
            logger.logError("Could not stop server!")
            e.printStackTrace()
        }
    }
}
