package data.extentions

import android.content.Context
import java.text.DecimalFormat
import java.util.*

internal fun Float.format(format: String = "0.0"): String {
    Locale.setDefault(Locale.US)
    return DecimalFormat(format).format(this)
}