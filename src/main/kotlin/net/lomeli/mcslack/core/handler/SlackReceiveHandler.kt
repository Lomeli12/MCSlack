package net.lomeli.mcslack.core.handler

import com.google.common.base.Strings
import net.lomeli.mcslack.MCSlack
import net.lomeli.mcslack.core.helper.SlackPostHelper
import net.minecraft.util.text.TextComponentString
import net.minecraft.util.text.event.ClickEvent
import net.minecraftforge.fml.client.FMLClientHandler
import net.minecraftforge.fml.common.FMLCommonHandler
import org.eclipse.jetty.server.Request
import org.eclipse.jetty.server.Response
import org.eclipse.jetty.server.handler.AbstractHandler
import java.util.regex.Pattern
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class SlackReceiveHandler : AbstractHandler {
    val attachementPattern = Pattern.compile("(<((https|http|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_!:,.;]*[-a-zA-Z0-9+&@#/%=~_])\\|([^}]*?)>)")
    val linkPattern = Pattern.compile("(<((https|http|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_!:,.;]*[-a-zA-Z0-9+&@#/%=~_])>)")
    val userPatternSimple = Pattern.compile("(<@([^}]*?)>)")
    val userPattern = Pattern.compile("(<@([^}]*?)\\|([^}]*?)>)")
    val channelPattern = Pattern.compile("(<#([^}]*?)\\|([^}]*?)>)")
    val channelPatternSimple = Pattern.compile("(<#([^}]*?)>)")
    var clickEvent: String = ""

    constructor() {
    }

    override fun handle(target: String?, request0: Request?, request1: HttpServletRequest?, response: HttpServletResponse?) {
        if (request1?.method.equals("POST")) {
            val token = request1?.getParameter("token")
            if (!Strings.isNullOrEmpty(MCSlack.modConfig!!.outgoingToken) && MCSlack.modConfig!!.outgoingToken.equals(token)) {
                var text = request1?.getParameter("text");
                if (Strings.isNullOrEmpty(text))
                    return
                var user = request1?.getParameter("user_name")
                if (Strings.isNullOrEmpty(user) || user.equals("slackbot", true) || user.equals(MCSlack.modConfig!!.botName, true))
                    return
                try {
                    text = convertAttachments(text)
                    text = convertUsers(text)
                    text = convertURLs(text)
                    text = convertChannels(text)
                } catch (ex: Exception) {
                    //ex.printStackTrace()
                }
                val msg = TextComponentString("@$user: $text")
                if (!Strings.isNullOrEmpty(clickEvent)) {
                    msg.style.clickEvent = ClickEvent(ClickEvent.Action.OPEN_URL, clickEvent)
                    clickEvent = ""
                }
                if (FMLCommonHandler.instance().side.isClient)
                    FMLClientHandler.instance().clientPlayerEntity.addChatComponentMessage(msg)
                else if (FMLCommonHandler.instance().minecraftServerInstance != null)
                    FMLCommonHandler.instance().minecraftServerInstance.playerList.sendChatMsgImpl(msg, false)
                response?.status = Response.SC_OK
            }
        }
    }

    fun convertURLs(text: String?): String? {
        var retValue = text
        val match = linkPattern.matcher(retValue)
        while (match.find()) {
            retValue = retValue?.replace(match.group(), match.group(2))
            if (!Strings.isNullOrEmpty(match.group(2)))
                clickEvent = match.group(2)
        }
        return retValue
    }

    fun convertAttachments(text: String?): String? {
        var retValue = text
        val match = attachementPattern.matcher(retValue)
        while (match.find()) {
            val part1 = match.group().split("|")[0]
            retValue = retValue?.replace(match.group(), part1.substring(1, part1.length))
            if (!Strings.isNullOrEmpty(part1.substring(1, part1.length)))
                clickEvent = part1.substring(1, part1.length)
        }
        return retValue
    }

    fun convertUsers(text: String?): String? {
        var retValue = text
        val match = userPattern.matcher(retValue)
        while (match.find()) {
            val part1 = match.group().split("|")[1]
            val name = part1.substring(0, part1.length - 1)
            retValue = retValue?.replace(match.group(), name)
        }
        if (SlackPostHelper.canSendAPIRequests()) {
            val match2 = userPatternSimple.matcher(retValue)
            while (match2.find()) {
                val name = "@${SlackPostHelper.getUserName(match2.group(2))}"
                retValue = retValue?.replace(match2.group(), name)
            }
        }
        return retValue
    }

    fun convertChannels(text: String?): String? {
        var retValue = text
        val match = channelPattern.matcher(retValue)
        while (match.find()) {
            val part1 = match.group().split("|")[1]
            val name = part1.substring(0, part1.length - 1)
            retValue = retValue?.replace(match.group(), name)
        }
        if (SlackPostHelper.canSendAPIRequests()) {
            val match2 = channelPatternSimple.matcher(retValue)
            while (match2.find()) {
                val name = "#${SlackPostHelper.getChannelName(match2.group(2))}"
                retValue = retValue?.replace(match2.group(), name)
            }
        }
        return retValue
    }
}