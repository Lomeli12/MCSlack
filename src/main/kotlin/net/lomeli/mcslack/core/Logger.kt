package net.lomeli.mcslack.core

import net.lomeli.mcslack.MCSlack
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager

class Logger {
    private val logger = LogManager.getLogger(MCSlack.NAME)

    fun log(logLevel: Level, message: Any) = logger.log(logLevel, "[${MCSlack.NAME}] " + message.toString())

    fun logWarning(message: Any) = log(Level.WARN, message)

    fun logInfo(message: Any) = log(Level.INFO, message)

    fun logError(message: Any) = log(Level.ERROR, message)
}