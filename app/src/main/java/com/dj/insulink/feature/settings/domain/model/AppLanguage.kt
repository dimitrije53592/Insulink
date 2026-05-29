package com.dj.insulink.feature.settings.domain.model

import androidx.annotation.DrawableRes
import com.dj.insulink.R
import java.util.Locale

enum class AppLanguage(
    val key: String,
    val displayName: String,
    val locale: Locale,
    @DrawableRes val flagIcon: Int
) {
    ENGLISH("en", "English", Locale.ENGLISH, R.drawable.ic_flag_usa),
    SERBIAN("sr-Latn", "Srpski", Locale.forLanguageTag("sr-Latn"), R.drawable.ic_flag_serbia);

    companion object {
        fun fromKey(key: String): AppLanguage =
            entries.find { it.key == key } ?: ENGLISH
    }
}
