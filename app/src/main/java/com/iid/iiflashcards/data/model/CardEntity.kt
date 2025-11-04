package com.iid.iiflashcards.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "cards")
data class CardEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val front: String,
    val frontHint: String? = null,
    val back: String,
    val backHint: String? = null,
    val reviewDate: Date? = null,
)
