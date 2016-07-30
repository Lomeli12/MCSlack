package net.lomeli.mcslack.core.handler

import net.lomeli.mcslack.core.helper.SlackPostHelper
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.event.ServerChatEvent
import net.minecraftforge.event.entity.living.LivingDeathEvent
import net.minecraftforge.event.entity.player.AchievementEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.PlayerEvent

class EventHandler {
    @SubscribeEvent fun serverChat(event: ServerChatEvent) {
        SlackPostHelper.sendSlackMessage(event.player, event.message)
    }

    @SubscribeEvent fun playerJoined(event : PlayerEvent.PlayerLoggedInEvent) {
        SlackPostHelper.sendBotMessage("${event.player.displayNameString} joined the server.")
    }

    @SubscribeEvent fun playerLeft(event: PlayerEvent.PlayerLoggedOutEvent) {
        SlackPostHelper.sendBotMessage("${event.player.displayNameString} left the server.")
    }

    @SubscribeEvent fun playerDied(event: LivingDeathEvent) {
        if (event.entityLiving is EntityPlayer) {
            val player = event.entityLiving as EntityPlayer
            SlackPostHelper.sendBotMessage("${player.displayNameString} died!")
        }
    }

    @SubscribeEvent fun achievementEarned(event: AchievementEvent) {
        SlackPostHelper.sendBotMessage("${event.entityPlayer.displayNameString} earned achievement [${event.achievement.statName.unformattedText}]")
    }
}