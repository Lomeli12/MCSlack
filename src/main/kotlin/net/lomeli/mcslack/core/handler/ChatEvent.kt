package net.lomeli.mcslack.core.handler

import net.minecraftforge.event.ServerChatEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

class ChatEvent {
    @SubscribeEvent fun serverChat(event: ServerChatEvent) {
        SlackPostHelper.sentSlackMessage(event.player, event.message)
    }
}