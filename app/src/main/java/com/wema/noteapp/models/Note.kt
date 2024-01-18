package com.wema.noteapp.models

import java.time.LocalDateTime
import java.util.Date

data class Note (
    val id: Int,
    val title: String,
    val body: String,
    var isArchived: Boolean =  false,
    val created_at: String = Date().toString(),
    val updated_at: String = Date().toString(),
    )