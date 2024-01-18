package com.wema.noteapp.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import java.util.Date

@Dao
interface NoteDao {
    @Query("SELECT * FROM note ORDER BY id DESC")
    fun getAllNotes(): List<Note>


    @Query("SELECT * FROM note WHERE id = :noteId")
    fun findNote(noteId: Long): Note

    @Query("UPDATE note SET title = :newTitle, body = :newContent, isArchived = :isArchived, updated_at = :updatedAt WHERE id = :noteId")
    fun updateNote(noteId: Long, newTitle: String, newContent: String, isArchived: Boolean, updatedAt: String = Date().toString()) : Int

    @Insert
    fun insertAll(vararg notes: Note)

    @Delete
    fun delete(note: Note)

}