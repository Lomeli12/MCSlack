package net.lomeli.mcslack.core.handler

import com.google.common.base.Strings
import net.lomeli.mcslack.MCSlack
import net.minecraft.util.text.TextComponentString
import net.minecraftforge.fml.client.FMLClientHandler
import net.minecraftforge.fml.common.FMLCommonHandler
import org.eclipse.jetty.server.Request
import org.eclipse.jetty.server.handler.AbstractHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class SlackReceiveHandler : AbstractHandler {
    constructor() {
    }

    override fun handle(target: String?, request0: Request?, request1: HttpServletRequest?, response: HttpServletResponse?) {
        if (request1?.method.equals("POST")) {
            val token = request1?.getParameter("token")
            if (!Strings.isNullOrEmpty(MCSlack.modConfig.outgoingToken) && MCSlack.modConfig.outgoingToken.equals(token)) {
                val text = request1?.getParameter("text");
                if (Strings.isNullOrEmpty(text))
                    return
                var user = request1?.getParameter("user_name")
                if (Strings.isNullOrEmpty(user))
                    return
                val msg = TextComponentString("#${user}: ${text}")
                if (FMLCommonHandler.instance().side.isClient)
                    FMLClientHandler.instance().clientPlayerEntity.addChatComponentMessage(msg)
                else if (FMLCommonHandler.instance().minecraftServerInstance != null) {
                    FMLCommonHandler.instance().minecraftServerInstance.playerList.sendChatMsgImpl(msg, false)
                }
            }
        }
    }
}