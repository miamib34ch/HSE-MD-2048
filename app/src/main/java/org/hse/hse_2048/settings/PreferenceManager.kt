package org.hse.hse_2048.settings

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager {
    private val PREFERENCE_FILE = "org.hse.android.file"
    private var sharedPref: SharedPreferences? = null

    constructor(context: Context) {
        sharedPref = context.getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE)
    }

    fun saveValue(key: String?, value: String?) {
        val editor = sharedPref!!.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun saveValue(key: String?, value: Int) {
        val editor = sharedPref!!.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun saveValue(key: String?, value: Float) {
        val editor = sharedPref!!.edit()
        editor.putFloat(key, value)
        editor.apply()
    }

    fun saveValue(key: String?, value: Boolean) {
        val editor = sharedPref!!.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getValue(key: String?, defaultValue: String?): String? {
        return sharedPref!!.getString(key, defaultValue)
    }

    fun getValue(key: String?, defaultValue: Int): Int {
        return sharedPref!!.getInt(key, defaultValue)
    }

    fun getValue(key: String?, defaultValue: Float): Float {
        return sharedPref!!.getFloat(key, defaultValue)
    }

    fun getValue(key: String?, defaultValue: Boolean): Boolean {
        return sharedPref!!.getBoolean(key, defaultValue)
    }
    
    fun removeKey(key: String?) {
        val editor = sharedPref!!.edit()
        editor.remove(key)
        editor.commit()
    }
}