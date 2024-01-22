package com.wema.noteapp.ui.interfaces

import com.wema.noteapp.db.Note

interface INoteListener {
    fun onDeleteNoteClicked(note: Note)
    fun onNoteClicked(noteId: Long)
    fun onCreateNote(title: String, body: String)
}