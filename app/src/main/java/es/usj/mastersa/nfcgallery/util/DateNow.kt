package es.usj.mastersa.nfcgallery.util

import java.text.SimpleDateFormat
import java.util.*


/**
 ****Creado por: Edison Martinez,
 ****Fecha:02,Saturday,2022,
 ****Hora: 5:46 PM.
 **/
class DateNow
{
    fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }

}