package com.wema.noteapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.wema.noteapp.databinding.FragmentNoteBinding
import com.wema.noteapp.db.AppDatabase
import com.wema.noteapp.db.NoteDao
import com.wema.noteapp.ui.adapters.NoteAdapter
import com.wema.noteapp.ui.viewmodels.NoteViewModel
import com.wema.noteapp.ui.viewmodels.NoteViewModelFactory

class NoteFragment : Fragment() {

    private lateinit var binding: FragmentNoteBinding
    private lateinit var noteViewModel: NoteViewModel
    private var noteDao: NoteDao? = null
    private var adapter: NoteAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNoteBinding.inflate(layoutInflater, container, false)

        try {
            // Create ViewModel instance with the factory
            val factory = NoteViewModelFactory(requireActivity().application)
            noteViewModel = ViewModelProvider(this, factory)[NoteViewModel::class.java]

            noteDao = AppDatabase.getInstance(requireContext())?.noteDao()

        } catch (e: Exception) {
            Log.e("ViewModelProvider", e.localizedMessage?.toString().toString())
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        noteObserver()
        noteViewModel?.getNotes()
        adapter = NoteAdapter(emptyList(), noteViewModel)
        binding.rvNotes.adapter = adapter
        binding.rvNotes.layoutManager = LinearLayoutManager(activity)


        binding.ibAddNote.setOnClickListener {
            CreateNoteFragment(noteViewModel).show(childFragmentManager, "CreateNoteFragment")
        }
    }

    private fun noteObserver() {
        try {
            noteViewModel?.noteState?.observe((viewLifecycleOwner)) { noteUiState ->

                if(noteUiState.notes.isEmpty()) {
                    binding.rvNotes.visibility = View.GONE
                    binding.ivNotFound.visibility = View.VISIBLE
                }else {
                    binding.rvNotes.visibility = View.VISIBLE
                    binding.ivNotFound.visibility = View.GONE
                }
                adapter?.notes  =  noteUiState.notes
                adapter?.notifyDataSetChanged()

                if (noteUiState.isCreated) {
                    Toast.makeText(requireContext(), "Note created successfully!", Toast.LENGTH_SHORT).show()
                    adapter?.notifyItemInserted(noteUiState.notes.size - 1)
                }
                if (noteUiState.isDeleted) {
                    Toast.makeText(requireContext(), "Note Deleted successfully!", Toast.LENGTH_SHORT).show()
                    adapter?.notifyDataSetChanged()
                }
            }

        } catch (e: Exception) {
            Log.e("noteObserver", e.localizedMessage?.toString().toString())
        }
    }


}