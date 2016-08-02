package net.lomeli.mcslack.core.command

import net.lomeli.mcslack.MCSlack
import net.lomeli.mcslack.core.helper.LangHelper
import net.lomeli.mcslack.core.helper.ServerHelper
import net.lomeli.mcslack.core.helper.SlackPostHelper
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraft.server.MinecraftServer
import net.minecraft.util.text.TextComponentString

class CommandStop : CommandBase {
    constructor()

    override fun getRequiredPermissionLevel(): Int = 2

    override fun execute(server: MinecraftServer?, sender: ICommandSender?, args: Array<out String>?) {
        if (MCSlack.server!!.isRunning) {
            try {
                val msg = LangHelper.translate("mcslack.command.stop", MCSlack.modConfig!!.botName, sender!!.displayName.unformattedComponentText)
                sender?.addChatMessage(TextComponentString(msg))
                SlackPostHelper.sendBotMessage(msg)
                ServerHelper.shutdownServer()
            } catch(ex: Exception) {
                MCSlack.logger.logError("Error when ${sender!!.displayName.unformattedText} ran $commandName command.")
                ex.printStackTrace()
            }
        } else
            sender?.addChatMessage(TextComponentString(LangHelper.translate("mcslack.command.stop.stopped",  MCSlack.modConfig!!.botName)))
    }

    override fun getCommandName(): String? = "stop"

    override fun getCommandUsage(sender: ICommandSender?): String? = ""
}