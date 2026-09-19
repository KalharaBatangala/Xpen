package com.example.xpen.util

import java.util.Calendar

object DateUtils {
    fun formatDateTime(millis: Long): String {
        val sdf = java.text.SimpleDateFormat("dd/MM/yyyy h:mm a", java.util.Locale.getDefault())
        return sdf.format(java.util.Date(millis))
    }

    fun rangeForScreen(route: String): Pair<Long, Long> = when (route) {
        "today" -> todayRange()
        "this_week" -> thisWeekRange()
        "this_month" -> thisMonthRange()
        "last_month" -> lastMonthRange()
        "last_3_months" -> last3MonthsRange()
        else -> todayRange()
    }

    fun todayRange(): Pair<Long, Long> {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0); cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0); cal.set(Calendar.MILLISECOND, 0)
        val start = cal.timeInMillis
        cal.add(Calendar.DAY_OF_MONTH, 1)
        return start to cal.timeInMillis
    }

    fun thisWeekRange(): Pair<Long, Long> {
        val cal = Calendar.getInstance()
        cal.firstDayOfWeek = Calendar.MONDAY
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        cal.set(Calendar.HOUR_OF_DAY, 0); cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0); cal.set(Calendar.MILLISECOND, 0)
        val start = cal.timeInMillis
        cal.add(Calendar.DAY_OF_MONTH, 7)
        return start to cal.timeInMillis
    }

    fun thisMonthRange(): Pair<Long, Long> {
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.set(Calendar.HOUR_OF_DAY, 0); cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0); cal.set(Calendar.MILLISECOND, 0)
        val start = cal.timeInMillis
        cal.add(Calendar.MONTH, 1)
        return start to cal.timeInMillis
    }

    fun lastMonthRange(): Pair<Long, Long> {
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.set(Calendar.HOUR_OF_DAY, 0); cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0); cal.set(Calendar.MILLISECOND, 0)
        cal.add(Calendar.MONTH, -1)
        val start = cal.timeInMillis
        cal.add(Calendar.MONTH, 1)
        return start to cal.timeInMillis
    }

    fun last3MonthsRange(): Pair<Long, Long> {
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.set(Calendar.HOUR_OF_DAY, 0); cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0); cal.set(Calendar.MILLISECOND, 0)
        cal.add(Calendar.MONTH, -2)   // go back 2 months → start of that month
        val start = cal.timeInMillis
        cal.add(Calendar.MONTH, 3)    // forward 3 months from there → end of current month
        return start to cal.timeInMillis
    }
    /** 7-day rolling correction window, independent of calendar week grouping */
    fun isWithinCorrectionWindow(dateTimeMillis: Long): Boolean {
        val sevenDaysAgo = System.currentTimeMillis() - (7L * 24 * 60 * 60 * 1000)
        return dateTimeMillis >= sevenDaysAgo
    }
}