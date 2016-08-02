package net.lomeli.mcslack.client

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraftforge.fml.client.IModGuiFactory

class ConfigFactory : IModGuiFactory {
    override fun runtimeGuiCategories(): MutableSet<IModGuiFactory.RuntimeOptionCategoryElement>? = null

    override fun mainConfigGuiClass(): Class<out GuiScreen>? = GuiSlackConfig::class.java

    override fun getHandlerFor(element: IModGuiFactory.RuntimeOptionCategoryElement?): IModGuiFactory.RuntimeOptionGuiHandler? = null

    override fun initialize(minecraftInstance: Minecraft?) {
    }
}