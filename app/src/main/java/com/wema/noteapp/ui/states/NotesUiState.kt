package com.wema.noteapp.ui.states

import com.wema.noteapp.db.Note


data class NotesUiState(
    var notes: List<Note>,
    var isArchived: Boolean =  false,
    var isLoading: Boolean = false,
    var isCreated: Boolean = false,
    var isDeleted: Boolean = false
    )