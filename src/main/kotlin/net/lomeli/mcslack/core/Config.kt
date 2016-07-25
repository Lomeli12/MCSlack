package net.lomeli.mcslack.core

import net.lomeli.mcslack.MCSlack
import net.minecraftforge.common.config.Configuration
import net.minecraftforge.fml.client.event.ConfigChangedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

class Config(val config: Configuration) {
    var incomingHook : String = ""
    var outgoingToken : String = ""
    var port : Int = 80
    fun loadConfig() {
        val category = "general";
        incomingHook = config.getString("incomingWebHook", category, "", "Incoming WebHook for your slack team.")
        outgoingToken = config.getString("outgoingHookToken", category, "", "Token used to get outgoing messages.")
        port = config.getInt("port", category, 80, 0, 65535, "Port MCSlack will listen on for messages.")

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