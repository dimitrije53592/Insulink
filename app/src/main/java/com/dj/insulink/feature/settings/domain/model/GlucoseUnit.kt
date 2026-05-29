package com.dj.insulink.feature.settings.domain.model

import androidx.annotation.DrawableRes
import com.dj.insulink.R

enum class GlucoseUnit(
    val key: String,
    val label: String,
    val suffix: String,
    @DrawableRes val flagIcon: Int
) {
    MG_DL("mg_dl", "mg/dL", "mg/dL", R.drawable.ic_flag_usa),
    MMOL_L("mmol_l", "mmol/L", "mmol/L", R.drawable.ic_flag_eu);

    companion object {
        fun fromKey(key: String): GlucoseUnit =
            entries.find { it.key == key } ?: MG_DL

        fun convertMgDlToMmolL(mgDl: Double): Double = mgDl / CONVERSION_FACTOR

        fun convertMmolLToMgDl(mmolL: Double): Double = mmolL * CONVERSION_FACTOR

        private const val CONVERSION_FACTOR = 18.0182
    }
}
