package com.wema.noteapp.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wema.noteapp.db.Note
import com.wema.noteapp.db.repository.NoteRepository
import com.wema.noteapp.ui.states.NoteDetailUiState
import kotlinx.coroutines.launch

class NoteDetailViewModel(application: Application): AndroidViewModel(application) {
    private var noteRepository: NoteRepository? = null

    private var _noteState = MutableLiveData<NoteDetailUiState>()
    val noteState: LiveData<NoteDetailUiState>
        get() = _noteState
    init {
        noteRepository = NoteRepository.getInstance(application)
    }

    fun findNote(noteId: Long) {
        Log.d("NoteId", "find note called")
        viewModelScope.launch {
            val note = noteRepository?.findNote(noteId)
            if (note != null) {
                _noteState.value = NoteDetailUiState(note.id, note.title, note.body, note.isArchived, note.created_at, note.updated_at)
            }
        }
    }

    fun updateNote(noteId: Long, title: String, body: String, status: Boolean){
        viewModelScope.launch {
            val note = noteRepository?.findNote(noteId)
            if (note != null) {
                noteRepository?.updateNote(note.id.toLong(), title, body, status)
            }
            _noteState.value = _noteState.value?.copy(isUpdated = true)
        }
    }
}