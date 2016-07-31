package net.lomeli.mcslack.core.helper

import com.google.common.base.Strings
import net.minecraft.client.resources.I18n

object LangHelper {

    fun canLocalize(key: String) : Boolean = !Strings.isNullOrEmpty(key) && I18n.hasKey(key)

    fun translate(key: String, vararg args: Any): String {
        if (canLocalize(key))
            return if (args != null && args.size > 0) I18n.format(key, *args) else I18n.format(key)
        return key
    }
}