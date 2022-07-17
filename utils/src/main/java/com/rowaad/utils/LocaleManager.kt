package com.rowaad.utils

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import java.util.*

object LocaleManager{

    /**
     * Create new configurations and attach them with the context.
     * @param context is your current context to override.
     *
     * @return new context with new configurations.
     * */
//    fun getCustomizedContext(context: Context): Context {
//        val dataManager = AppPreferencesHelper(context)
//        return if(dataManager.getAppLanguage().isEmpty().not()){
//            val appLocale = Locale(dataManager.getAppLanguage())
//            Locale.setDefault(appLocale)
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//                updateResources(context, appLocale)
//            else
//                updateResourcesLegacy(context, appLocale)
//        } else {
//            val defaultLocale = Locale(Constants.DEFAULT_APP_LANGUAGE)
//            Locale.setDefault(defaultLocale)
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//                updateResources(context, defaultLocale)
//            else
//                updateResourcesLegacy(context,defaultLocale)
//        }
//    }
    fun getCustomizedContext(context: Context, locale: String = "ar"): Context {
        val appLocale = Locale(locale)
        Locale.setDefault(appLocale)

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            updateResources(context, appLocale)
        else
            updateResourcesLegacy(context, appLocale)

    }
    private fun updateResources(context: Context, newLocale: Locale): Context {
        val currentResources = context.resources

        val newConfigurations = Configuration(currentResources.configuration)
        newConfigurations.setLocale(newLocale)

        return context.createConfigurationContext(newConfigurations)
    }

    private fun updateResourcesLegacy(context: Context, newLocale: Locale): Context {

        val currentResources = context.resources

        val newConfigurations = currentResources.configuration
        newConfigurations.setLocale(newLocale)

        currentResources.updateConfiguration(newConfigurations, currentResources.displayMetrics)

        return context
    }
}