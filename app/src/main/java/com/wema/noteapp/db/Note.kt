package com.wema.noteapp.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity()
data class Note (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val body: String,
    var isArchived: Boolean =  false,
    val created_at: String = Date().toString(),
    val updated_at: String = Date().toString(),
)