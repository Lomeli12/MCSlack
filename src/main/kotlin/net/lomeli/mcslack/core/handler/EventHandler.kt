package net.lomeli.mcslack.core.handler

import net.lomeli.mcslack.core.helper.LangHelper
import net.lomeli.mcslack.core.helper.SlackPostHelper
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraftforge.event.ServerChatEvent
import net.minecraftforge.event.entity.living.LivingDeathEvent
import net.minecraftforge.event.entity.player.AchievementEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.PlayerEvent

class EventHandler {
    @SubscribeEvent fun serverChat(event: ServerChatEvent) {
        SlackPostHelper.sendSlackMessage(event.player, event.message)
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
}