package net.lomeli.mcslack.client

import net.lomeli.mcslack.MCSlack
import net.minecraft.client.gui.GuiScreen
import net.minecraftforge.common.config.ConfigElement
import net.minecraftforge.common.config.Configuration
import net.minecraftforge.fml.client.config.GuiConfig

class GuiSlackConfig (parent: GuiScreen) : GuiConfig(parent, ConfigElement(MCSlack.modConfig!!.config.getCategory(Configuration.CATEGORY_GENERAL)).childElements, MCSlack.MOD_ID, false, false, GuiConfig.getAbridgedConfigPath(MCSlack.modConfig!!.config.toString()))