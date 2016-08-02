package net.lomeli.mcslack.core.helper

import com.google.common.base.Strings
import net.minecraft.util.text.translation.I18n

object LangHelper {

    fun canLocalize(key: String) : Boolean = !Strings.isNullOrEmpty(key) && I18n.canTranslate(key)

    fun translate(key: String, vararg args: Any): String {
        if (canLocalize(key)) {
            val local = I18n.translateToLocal(key)
            return if (args != null && args.size > 0) String.format(local, *args) else local
        }
        return key
    }
}