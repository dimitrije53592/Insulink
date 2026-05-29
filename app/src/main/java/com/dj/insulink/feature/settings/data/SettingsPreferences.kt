package com.dj.insulink.feature.settings.data

import android.content.Context
import android.content.SharedPreferences
import com.dj.insulink.feature.settings.domain.model.AppLanguage
import com.dj.insulink.feature.settings.domain.model.GlucoseUnit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsPreferences @Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getLanguage(): AppLanguage =
        AppLanguage.fromKey(prefs.getString(KEY_LANGUAGE, AppLanguage.ENGLISH.key) ?: AppLanguage.ENGLISH.key)

    fun setLanguage(language: AppLanguage) {
        prefs.edit().putString(KEY_LANGUAGE, language.key).apply()
    }

    fun getGlucoseUnit(): GlucoseUnit =
        GlucoseUnit.fromKey(prefs.getString(KEY_GLUCOSE_UNIT, GlucoseUnit.MG_DL.key) ?: GlucoseUnit.MG_DL.key)

    fun setGlucoseUnit(unit: GlucoseUnit) {
        prefs.edit().putString(KEY_GLUCOSE_UNIT, unit.key).apply()
    }

    companion object {
        private const val PREFS_NAME = "insulink_settings"
        private const val KEY_LANGUAGE = "app_language"
        private const val KEY_GLUCOSE_UNIT = "glucose_unit"
    }
}
