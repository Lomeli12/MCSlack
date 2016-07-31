package net.lomeli.mcslack.core.command

import com.google.common.collect.Lists
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraft.server.MinecraftServer
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TextComponentTranslation
import java.util.*

class CommandMCSlack : CommandBase {
    private val modCommands: ArrayList<CommandBase>
    private val commands: ArrayList<String>

    constructor() {
        modCommands = Lists.newArrayList()
        commands = Lists.newArrayList()

        modCommands.add(CommandStop())
        modCommands.add(CommandStart())

        var i = 0;
        while (i < modCommands.size) {
            commands.add(modCommands[i].commandName)
            ++i
        }
    }

    override fun getRequiredPermissionLevel(): Int = 2

    override fun execute(server: MinecraftServer?, sender: ICommandSender?, args: Array<out String>?) {
        if (sender != null) {
            if (args != null && args.size >= 1) {
                for (i in modCommands.indices) {
                    val commandBase: CommandBase = modCommands[i];
                    if (commandBase.commandName.equals(args[0], true) && commandBase.checkPermission(server, sender))
                        commandBase.execute(server, sender, args)
                }
            } else
                sender.addChatMessage(TextComponentTranslation(getCommandUsage(sender)))
        }
    }

    override fun getCommandName(): String? = "mcslack"

    override fun getCommandUsage(sender: ICommandSender?): String? = "mcslack.command.usage";

    override fun getTabCompletionOptions(server: MinecraftServer?, sender: ICommandSender?, args: Array<out String>?, pos: BlockPos?): MutableList<String>? {
        if (sender != null && args != null) {
            if (args.size == 1)
                return CommandBase.getListOfStringsMatchingLastWord(args, commands);
            else if (args.size >= 2) {
                for (i in modCommands.indices) {
                    val command: CommandBase = modCommands[i];
                    if (command.commandName.equals(args[0], true))
                        return command.getTabCompletionOptions(server, sender, args, pos)
                }
            }
        }
        return null;
    }
}