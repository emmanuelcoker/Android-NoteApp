package com.wema.noteapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.wema.noteapp.databinding.FragmentShowNoteBinding
import com.wema.noteapp.db.Note
import com.wema.noteapp.ui.states.NoteDetailUiState
import com.wema.noteapp.ui.viewmodels.NoteDetailViewModel
import com.wema.noteapp.ui.viewmodels.NoteDetailViewModelFactory

class ShowNoteFragment : Fragment() {

    private lateinit var binding: FragmentShowNoteBinding
    private  val arguments: ShowNoteFragmentArgs by navArgs()
    private var viewModel: NoteDetailViewModel? = null
    private  var noteId: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentShowNoteBinding.inflate(layoutInflater, container, false)

        try {
            // Create ViewModel instance with the factory
            val factory = NoteDetailViewModelFactory(requireActivity().application)
            viewModel = ViewModelProvider(this, factory)[NoteDetailViewModel::class.java]
        }catch (e: Exception) {
            Log.e(getString(R.string.notedetailprov), e.localizedMessage?.toString().toString())
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        noteObserver()
        noteId = arguments.noteId
        viewModel?.findNote(noteId!!)

        binding.ibBack.setOnClickListener {
            //go back
            findNavController().popBackStack()
        }

        binding.btnSave.setOnClickListener {
            val title = binding.etNoteTitle.text.toString()
            val body = binding.etNoteBody.text.toString()
            viewModel?.updateNote(noteId!!, title, body, false)
        }
    }

    private fun noteObserver() {
        viewModel?.noteState?.observe((viewLifecycleOwner)) { note ->
            val noteItem = Note(note.id, note.title, note.body, note.isArchived, note.created_at, note.updated_at)
            updateNoteOnUi(noteItem)
            if (note.isUpdated) {
                Toast.makeText(requireContext(), "Note updated successfully!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateNoteOnUi(note: Note) {
        Log.d("NoteId", "update ui called")
        binding.etNoteTitle.setText(note.title)
        binding.etNoteBody.setText(note.body)
    }
}