package com.iid.iiflashcards.data.local.converter

import androidx.room.TypeConverter
import java.util.Date

class DateConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? = longToDate(value)

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? = dateToLong(date)
}

fun longToDate(value: Long?) = value?.let { Date(it) }

fun dateToLong(date: Date?) = date?.time