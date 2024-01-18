package com.wema.noteapp.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wema.noteapp.db.Note
import com.wema.noteapp.db.repository.NoteRepository
import com.wema.noteapp.ui.states.NotesUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NoteViewModel(application: Application): AndroidViewModel(application) {
    private var noteRepository: NoteRepository? = null

    private var _noteState = MutableLiveData(NotesUiState(emptyList()))
    val noteState: LiveData<NotesUiState>
    get() = _noteState

    init {
        noteRepository = NoteRepository.getInstance(application)
    }

    fun getNotes() {
        Log.d("getNotes", "get notes called")
        viewModelScope.launch {
            val notes =  noteRepository?.getAllNotes()
            _noteState.value = notes?.let { NotesUiState(notes = it) }
        }
    }

    fun insertNote(title: String, body: String){
        viewModelScope.launch {
            noteRepository?.insertNote(title, body)
            _noteState.value = _noteState.value?.copy(isCreated = true)
            getNotes()
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            noteRepository?.deleteNote(note)
            _noteState.value = _noteState.value?.copy(isDeleted = true)
            getNotes()
        }
    }
}