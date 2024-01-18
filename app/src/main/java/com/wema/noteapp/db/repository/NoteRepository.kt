package com.wema.noteapp.db.repository

import android.content.Context
import android.util.Log
import com.wema.noteapp.db.AppDatabase
import com.wema.noteapp.db.Note

class NoteRepository(context: Context) {
    private var database : AppDatabase? = null

    init {
        database = AppDatabase.getInstance(context)
    }
    companion object {
        private var Instance : NoteRepository? = null
        fun getInstance(context: Context): NoteRepository?{
            if(Instance == null){
                Instance = NoteRepository(context)
            }
            return Instance
        }
    }



    fun getAllNotes() : List<Note>?{
        return database?.noteDao()?.getAllNotes()
    }

    fun findNote(noteId: Long): Note? {
        return database?.noteDao()?.findNote(noteId)
    }

    fun updateNote(noteId: Long, noteTitle: String, noteBody: String, noteStatus: Boolean): Int? {
        return database?.noteDao()?.updateNote(noteId.toLong(), noteTitle, noteBody, noteStatus)
    }

    fun insertNote(title: String, body: String) {
        Log.d("addDummyNotes", "insert note called")
        database?.noteDao()?.insertAll(Note(title = title, body = body))
    }

    fun deleteNote(note: Note) {
        database?.noteDao()?.delete(note)
    }
}