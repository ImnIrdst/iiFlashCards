package com.iid.iiflashcards.util

import java.util.Calendar
import java.util.Date

fun getTomorrowDate(): Date {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, 1)
    return calendar.time
}

object ReviewDateHelper {
    sealed class Repetition(
        val p0: Int,
        val amount: Int,
    ) {
        object Again : Repetition(Calendar.MINUTE, 1)
        object Hard : Repetition(Calendar.MINUTE, 10)
        object Good : Repetition(Calendar.DAY_OF_YEAR, 1)
        object Easy : Repetition(Calendar.DAY_OF_YEAR, 4)
    }

    fun updateDate(date: Date, repetition: Repetition): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(repetition.p0, repetition.amount)
        return calendar.time
    }
}