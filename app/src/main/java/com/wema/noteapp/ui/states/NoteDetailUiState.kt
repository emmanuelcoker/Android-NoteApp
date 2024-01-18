package com.wema.noteapp.ui.states

import java.util.Date

data class NoteDetailUiState (
    val id: Int = 0,
    val title: String,
    val body: String,
    var isArchived: Boolean =  false,
    val created_at: String = Date().toString(),
    val updated_at: String = Date().toString(),
    var isUpdated: Boolean = false,
)