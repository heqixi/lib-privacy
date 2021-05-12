package com.seewo.student.libutils.utils

import java.util.*

/**
 * Created by linkaipeng on 2020/6/8.
 *
 */
object DateUtil {

    fun getTodayStartAndEndTime(): TimeInfoVO {
        val startCalendar = Calendar.getInstance()
        startCalendar[Calendar.HOUR_OF_DAY] = 0
        startCalendar[Calendar.MINUTE] = 0
        startCalendar[Calendar.SECOND] = 0
        startCalendar[Calendar.MILLISECOND] = 0
        val startTime = startCalendar.time.time

        val endCalendar = Calendar.getInstance()
        endCalendar[Calendar.HOUR_OF_DAY] = 23
        endCalendar[Calendar.MINUTE] = 59
        endCalendar[Calendar.SECOND] = 59
        endCalendar[Calendar.MILLISECOND] = 999
        val endTime = endCalendar.time.time

        return TimeInfoVO(startTime, endTime)
    }
}

data class TimeInfoVO(
    val startTime: Long,
    val endTime: Long
)