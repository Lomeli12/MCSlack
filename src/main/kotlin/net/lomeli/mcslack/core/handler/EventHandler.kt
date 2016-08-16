package net.lomeli.mcslack.core.handler

import com.google.common.base.Strings
import net.lomeli.mcslack.core.helper.LangHelper
import net.lomeli.mcslack.core.helper.SlackPostHelper
import net.minecraft.command.CommandBase
import net.minecraft.command.server.CommandBroadcast
import net.minecraft.command.server.CommandEmote
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.util.text.TextComponentTranslation
import net.minecraftforge.event.CommandEvent
import net.minecraftforge.event.ServerChatEvent
import net.minecraftforge.event.entity.living.LivingDeathEvent
import net.minecraftforge.event.entity.player.AchievementEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.PlayerEvent

class EventHandler {
    @SubscribeEvent fun serverChat(event: ServerChatEvent) {
        SlackPostHelper.sendPlayerMessage(event.player, event.message)
    }

    @SubscribeEvent fun playerJoined(event: PlayerEvent.PlayerLoggedInEvent) {
        SlackPostHelper.sendBotMessage(LangHelper.translate("mcslack.player.join", event.player.displayNameString))
    }

    @SubscribeEvent fun playerLeft(event: PlayerEvent.PlayerLoggedOutEvent) {
        SlackPostHelper.sendBotMessage(LangHelper.translate("mcslack.player.left", event.player.displayNameString))
    }

    @SubscribeEvent fun playerDied(event: LivingDeathEvent) {
        if (event.entityLiving is EntityPlayer) {
            val player = event.entityLiving as EntityPlayer
            SlackPostHelper.sendBotMessage(LangHelper.translate("mcslack.player.died", player.displayNameString))
        }
    }

    @SubscribeEvent fun achievementEarned(event: AchievementEvent) {
        val achievement = event.achievement;
        if (event.entityPlayer is EntityPlayerMP) {
            val player = event.entityPlayer as EntityPlayerMP
            if (!player.statFile.hasAchievementUnlocked(achievement) && player.statFile.canUnlockAchievement(achievement))
                SlackPostHelper.sendBotMessage(LangHelper.translate("mcslack.player.achievement", player.displayNameString, event.achievement.statName.unformattedText))
        }
    }

    @SubscribeEvent fun commandEvent(event: CommandEvent) {
        if (event.command is CommandEmote || event.command is CommandBroadcast) {
            if (event.parameters != null || event.parameters.size > 0) {
                val textComponent = CommandBase.getChatComponentFromNthArg(event.sender, event.parameters, 0, true)
                var key = ""
                if (event.command is CommandEmote) key = "chat.type.emote"
                else if (event.command is CommandBroadcast) key = "chat.type.announcement"
                if (!Strings.isNullOrEmpty(key)) {
                    var msg = TextComponentTranslation(key, *arrayOf<Any>(event.sender.displayName, textComponent)).unformattedText
                    if (event.command is CommandEmote) msg = "_${msg}_"
                    SlackPostHelper.sendPlayerMessage(event.sender.displayName.unformattedText, msg)
                }
            }
        }
    }
}