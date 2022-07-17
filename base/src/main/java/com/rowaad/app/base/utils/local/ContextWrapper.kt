package com.rowaad.app.base.utils.local

import android.content.Context
import android.os.Build
import android.os.LocaleList
import java.util.*

class ContextWrapper(base: Context?) : android.content.ContextWrapper(base) {

    companion object {

        fun wrap(context: Context?, newLocale: Locale): ContextWrapper {
            var context = context
            val res = context?.resources
            val configuration = res?.configuration

            when {
                Build.VERSION.SDK_INT >= 24 -> {
                    configuration?.setLocale(newLocale)

                    val localeList = LocaleList(newLocale)
                    LocaleList.setDefault(localeList)
                    configuration?.setLocales(localeList)

                    context = context?.createConfigurationContext(configuration!!)
                    configuration?.setLocales(localeList)

                    configuration?.setLocale(newLocale)

                }
                Build.VERSION.SDK_INT >= 17 -> {
                    configuration?.setLocale(newLocale)
                    context = context?.createConfigurationContext(configuration!!)

                }
                else -> {
                    configuration?.locale = newLocale
                    res?.updateConfiguration(configuration, res.displayMetrics)
                }
            }
            return ContextWrapper(context)
        }
    }
}