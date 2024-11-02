package com.example.climatic.utils;

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration

import java.util.Locale


object LanguageUtils {

    private const val PREFS_NAME = "app_prefs"
    private const val KEY_LANGUAGE = "app_language"

    fun setAppLocale(context: Context, language: String) {
        persistLanguage(context, language)
        val config = Configuration(context.resources.configuration)
        val locale = Locale(language)
        Locale.setDefault(locale)
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }

    private fun persistLanguage(context: Context, language: String) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(KEY_LANGUAGE, language)
        editor.apply()
    }

    fun getPersistedLanguage(context: Context): String {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(KEY_LANGUAGE, Locale.getDefault().language) ?: Locale.getDefault().language
    }
}

