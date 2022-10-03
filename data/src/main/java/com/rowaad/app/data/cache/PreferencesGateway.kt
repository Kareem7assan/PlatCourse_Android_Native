package com.rowaad.app.data.cache

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.rowaad.app.data.model.UserModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/*val preferencesGateway by lazy { PreferencesGateway() }*/
const val PREFERENCES_NAME = "PREFERENCES_NAME"

class PreferencesGateway @Inject constructor(@ApplicationContext val context: Context) {

    inline fun <reified T : Any> save(key: String, value: T) {

        context
            .getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
            .edit()
            .apply { putValue(key, value) }
            .apply()


    }

    inline fun <reified T : Any> load(key: String, defaultValue: T): T? {
        return context
            .getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
            .run { getValue(key, defaultValue) }


    }

    fun clearAll() {
        context
            .getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
            .edit()
            .clear()
            .apply()
    }

    fun isSaved(key: String): Boolean {
        return context
            .getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
            .contains(key)
    }

    fun remove(key: String) {
        context
            .getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
            .edit()
            .remove(key)
            .apply()
    }
}

inline fun <reified T : Any> SharedPreferences.Editor.putValue(
    key: String,
    value: T
) {
    when (T::class) {
        Boolean::class -> putBoolean(key, value as Boolean)
        Int::class -> putInt(key, value as Int)
        Long::class -> putLong(key, value as Long)
        Float::class -> putFloat(key, value as Float)
        String::class -> putString(key, value as String)
        UserModel::class -> putString(key, value.toJsonPref())
        else -> throw UnsupportedOperationException("not supported preferences type")
    }
}

inline fun <reified T : Any?> SharedPreferences.getValue(
    key: String,
    defaultValue: T?
): T? {
    return when (T::class) {
        Boolean::class -> getBoolean(key, defaultValue as Boolean) as T
        Int::class -> getInt(key, defaultValue as Int) as T
        Long::class -> getLong(key, defaultValue as Long) as T
        Float::class -> getFloat(key, defaultValue as Float) as T
        String::class -> getString(key, defaultValue as String) as T
        UserModel::class -> getString(key, null)?.fromJsonPref() as T?
        else -> throw UnsupportedOperationException("not supported preferences type")
    }
}

 inline fun <reified R> String.fromJsonPref(): R {
    return Gson().fromJson(this, R::class.java)
}

 inline fun <reified R> R.toJsonPref(): String {
    return Gson().toJson(this, R::class.java)
}
