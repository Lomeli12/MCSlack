package net.lomeli.mcslack.core

import net.lomeli.mcslack.MCSlack
import net.lomeli.mcslack.core.helper.LangHelper
import net.minecraftforge.common.config.Configuration
import net.minecraftforge.fml.client.event.ConfigChangedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

class Config(val config: Configuration) {
    var incomingHook : String = ""
    var outgoingToken : String = ""
    var port : Int = 80
    var botName : String = "MCSlack"
    var botIcon : String = "https://files.lomeli12.net/minecraft/mcslack/mcslack.png"
    var apiKey : String = ""
    var onStartup : Boolean = true
    var listCommandToken : String = ""
    var listCommandChannel : String = ""
    var listCommandName : String = "list"
    var commandWhiteList: Array<String> = arrayOf("me", "say")

    fun loadConfig() {
        val category = Configuration.CATEGORY_GENERAL;
        incomingHook = config.getString("incomingWebHook", category, "", LangHelper.translate("mcslack.config.incoming"))
        outgoingToken = config.getString("outgoingHookToken", category, "", LangHelper.translate("mcslack.config.outgoing"))
        port = config.getInt("port", category, 80, 0, 65535, LangHelper.translate("mcslack.config.port"))
        botName = config.getString("botName", category, "MCSlack", LangHelper.translate("mcslack.config.name"))
        botIcon = config.getString("botIcon", category, "https://files.lomeli12.net/minecraft/mcslack/mcslack.png", LangHelper.translate("mcslack.config.icon"))
        apiKey = config.getString("apiKey", category, "", LangHelper.translate("mcslack.config.key"))
        onStartup = config.getBoolean("startup", category, true, LangHelper.translate("mcslack.config.startup"))
        listCommandToken = config.getString("listCommandToken", category, "", LangHelper.translate("mcslack.config.list"))
        listCommandChannel = config.getString("listCommandChannel", category, "", LangHelper.translate("mcslack.config.list.channel"))
        listCommandName = config.getString("listCommandName", category, "list", LangHelper.translate("mcslack.config.list.name"))
        //commandWhiteList = config.getStringList("commandWhiteList", category, arrayOf("me", "say"), LangHelper.translate("mcslack.config.commands"))

        if (config.hasChanged())
            config.save()
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    fun onConfigChange(event: ConfigChangedEvent.OnConfigChangedEvent) {
        if (event.modID.equals(MCSlack.MOD_ID))
            MCSlack.modConfig?.loadConfig()
    }
}