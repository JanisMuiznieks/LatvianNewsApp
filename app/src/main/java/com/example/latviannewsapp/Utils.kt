package com.example.latviannewsapp

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import org.ocpsoft.prettytime.PrettyTime
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Utils {

    private var colorList = arrayOf(
        ColorDrawable(Color.parseColor("#ffeead")),
        ColorDrawable(Color.parseColor("#93cfb3")),
        ColorDrawable(Color.parseColor("#fd7a7a")),
        ColorDrawable(Color.parseColor("#faca5f")),
        ColorDrawable(Color.parseColor("#1ba798")),
        ColorDrawable(Color.parseColor("#6aa9ae")),
        ColorDrawable(Color.parseColor("#ffbf27")),
        ColorDrawable(Color.parseColor("#d93947"))
    )

    fun getRandomColor(): ColorDrawable {
        val idx = Random().nextInt(colorList.size)
        return colorList[idx]


    }

    fun dateToTime(oldDate: String): String? {
        val p = PrettyTime(Locale(getCountry()))
        val isTime: String?

        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
        val date = sdf.parse(oldDate)
        isTime = p.format(date)

        return isTime
    }

    fun dateFormat(oldDate: String): String? {
        val newDate: String?
        val dateFormat = SimpleDateFormat("E, d MMM yyyy", Locale(getCountry()))

        newDate = try {
            val date: Date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH).parse(oldDate)
            dateFormat.format(date)
        } catch (e: ParseException) {
            oldDate
        }


        return newDate
    }

    fun getCountry(): String {
        val country = "lv"
        return country
    }

    fun getLanguage(): String {
        val locale: Locale = Locale.getDefault()
        val country: String = locale.language
        return country.toLowerCase()
    }


}