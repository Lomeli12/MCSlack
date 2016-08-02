package net.lomeli.mcslack.core.helper

import net.lomeli.mcslack.MCSlack
import net.lomeli.mcslack.core.handler.SlackReceiveHandler
import org.eclipse.jetty.server.Server

object ServerHelper {

    fun startupServer() {
        MCSlack.server = Server(MCSlack.modConfig!!.port)
        MCSlack.server!!.handler = SlackReceiveHandler()
        MCSlack.server!!.start()
        MCSlack.running = true;
    }

    fun shutdownServer() {
        MCSlack.server!!.stop()
        MCSlack.running = false;
    }
}