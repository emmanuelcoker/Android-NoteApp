package com.wema.noteapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.wema.noteapp.databinding.FragmentNoteBinding
import com.wema.noteapp.db.Note
import com.wema.noteapp.themedatastore.SettingsManager
import com.wema.noteapp.themedatastore.UIModel
import com.wema.noteapp.ui.adapters.NoteAdapter
import com.wema.noteapp.ui.interfaces.INoteListener
import com.wema.noteapp.ui.interfaces.INoteObserver
import com.wema.noteapp.ui.interfaces.IShowToast
import com.wema.noteapp.ui.viewmodels.NoteViewModel
import com.wema.noteapp.ui.viewmodels.NoteViewModelFactory
import kotlinx.coroutines.launch

class NoteFragment : Fragment(), INoteListener, IShowToast, INoteObserver {

    private lateinit var binding: FragmentNoteBinding
    private lateinit var noteViewModel: NoteViewModel
    private var adapter: NoteAdapter? = null
    private lateinit var settingsManager: SettingsManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNoteBinding.inflate(layoutInflater, container, false)
        initViewModel()
        settingsManager = SettingsManager(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        noteObserver()
        toggleAppTheme()
        noteViewModel?.getNotes()
        initUi()
    }

    override fun showToast(messageResId: Int) {
        requireActivity().runOnUiThread {
            Toast.makeText(requireContext(), getString(messageResId), Toast.LENGTH_SHORT).show()
        }
    }
    private fun showCreateNoteDialog() {
        CreateNoteFragment(this).show(childFragmentManager, "CreateNoteFragment")
    }

    private fun initUi(){
        adapter = NoteAdapter(emptyList(), this)
        binding.rvNotes.adapter = adapter
        binding.rvNotes.layoutManager = LinearLayoutManager(activity)

        binding.ibAddNote.setOnClickListener {
            showCreateNoteDialog()
        }
    }
    // Create ViewModel instance with the factory
    private fun initViewModel() {
        try {

            val factory = NoteViewModelFactory(requireActivity().application)
            noteViewModel = ViewModelProvider(this, factory)[NoteViewModel::class.java]

        } catch (e: Exception) {
            Log.e("ViewModelProvider", e.localizedMessage?.toString().toString())
        }
    }

    private fun toggleAppTheme(){
        Log.d("toggleAppTheme", "toggle called")
        binding?.scToggleDark?.setOnCheckedChangeListener { _, isChecked ->
            lifecycleScope.launch {
                Log.d("toggleAppTheme", "coroutine called")
                when(isChecked){
                    true -> settingsManager.setUIMode(UIModel.LIGHT)
                    false -> settingsManager.setUIMode(UIModel.DARK)
                }
            }
        }

        settingsManager.uiModelFlow.asLiveData().observe(this){  it ->
            it?.let {
                Log.d("toggleAppTheme", "viewmodel called")
                when(it){
                    UIModel.LIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    UIModel.DARK -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
            }
        }

    }

    override fun noteObserver() {
        try {
            noteViewModel?.noteState?.observe((viewLifecycleOwner)) { noteUiState ->

                //handle toggle empty background
                toggleEmptyBackgroundVisibility(noteUiState.notes)

                adapter?.apply {
                    notes  =  noteUiState.notes

                    if (noteUiState.isCreated) {
                        showToast(R.string.note_created_message)
                        notifyItemInserted(0) //database is sorted in descending order -- item is at the 0 position
                    }
                    if (noteUiState.isDeleted) {
                        showToast(R.string.note_deleted_message)
                        notifyDataSetChanged()
                    }
                }

            }
        } catch (e: Exception) {
            Log.e("noteObserver", e.localizedMessage?.toString().toString())
        }
    }


    private fun toggleEmptyBackgroundVisibility(notes: List<Note>){
        if(notes.isEmpty()) {
            binding.rvNotes.visibility = View.GONE
            binding.ivNotFound.visibility = View.VISIBLE
        }else {
            binding.rvNotes.visibility = View.VISIBLE
            binding.ivNotFound.visibility = View.GONE
        }
    }


    override fun onDeleteNoteClicked(note: Note) {
        noteViewModel.deleteNote(note)
    }

    override fun onNoteClicked(noteId: Long) {
        val direction = NoteFragmentDirections.actionNoteFragmentToShowNoteFragment(noteId)
        findNavController().navigate(direction)
    }

    override fun onCreateNote(title: String, body: String) {
        noteViewModel.insertNote(title, body)
    }
}