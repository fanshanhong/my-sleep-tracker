/*
 * Copyright 2019, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fan.mysleeptracker

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Build
import android.text.Html
import android.text.Spanned
import androidx.core.text.HtmlCompat
import com.fan.mysleeptracker.db.SleepNight
import java.text.SimpleDateFormat

/**
 * These functions create a formatted string that can be set in a TextView.
 */

/**
 * Returns a string representing the numeric quality rating.
 */
fun convertNumericQualityToString(quality: Int): String {
    var qualityString = "ok"
    when (quality) {
        -1 -> qualityString = "--"
        0 -> qualityString = "very bad"
        1 -> qualityString = "poor"
        2 -> qualityString = "soso"
        3 -> qualityString = "pretty good"
        4 -> qualityString = "excellent"
    }
    return qualityString
}


/**
 * Take the Long milliseconds returned by the system and stored in Room,
 * and convert it to a nicely formatted string for display.
 *
 * EEEE - Display the long letter version of the weekday
 * MMM - Display the letter abbreviation of the nmotny
 * dd-yyyy - day in month and full year numerically
 * HH:mm - Hours and minutes in 24hr format
 */
@SuppressLint("SimpleDateFormat")
fun convertLongToDateString(systemTime: Long): String {
    return SimpleDateFormat("EEEE MMM-dd-yyyy' Time: 'HH:mm:ss")
            .format(systemTime).toString()
}

/**
 * Takes a list of SleepNights and converts and formats it into one string for display.
 *
 * For display in a TextView, we have to supply one string, and styles are per TextView, not
 * applicable per word. So, we build a formatted string using HTML. This is handy, but we will
 * learn a better way of displaying this data in a future lesson.
 *
 * @param   nights - List of all SleepNights in the database.
 * @param   resources - Resources object for all the resources defined for our app.
 *
 * @return  Spanned - An interface for text that has formatting attached to it.
 *           See: https://developer.android.com/reference/android/text/Spanned
 */
fun formatNights(nights: List<SleepNight>): Spanned {
    val sb = StringBuilder()
    sb.apply {
        append("title")
        nights.forEach {
            append("<br>")
            append("startTime")
            append("\t${convertLongToDateString(it.startTime)}<br>")
            if (it.endTime != it.startTime) {
                append("endTime")
                append("\t${convertLongToDateString(it.endTime)}<br>")
                append("quality")
                append("\t${convertNumericQualityToString(it.sleepQuality)}<br>")
                append("hours")
                // Hours
                append("\t ${it.endTime.minus(it.startTime) / 1000 / 60 / 60}:")
                // Minutes
                append("${it.endTime.minus(it.startTime) / 1000 / 60}:")
                // Seconds
                append("${it.endTime.minus(it.startTime) / 1000}<br><br>")
            }
        }
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        return Html.fromHtml(sb.toString(), Html.FROM_HTML_MODE_LEGACY)
    } else {
        return HtmlCompat.fromHtml(sb.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}

