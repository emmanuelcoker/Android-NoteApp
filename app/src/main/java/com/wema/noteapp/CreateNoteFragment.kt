package com.wema.noteapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.wema.noteapp.databinding.FragmentCreateNoteBinding
import com.wema.noteapp.ui.viewmodels.NoteViewModel


class CreateNoteFragment(val viewModel: NoteViewModel) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentCreateNoteBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCreateNoteBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCreate.setOnClickListener {
            val newTitle = binding.etTitle.text.toString()
            val newBody = binding.etBody.text.toString()
            if(newTitle.isNotEmpty()) {
                viewModel.insertNote(newTitle, newBody)
                binding.etBody.setText("")
                binding.etTitle.setText("")
                dismiss()
            }else {
                Toast.makeText(requireContext(), "Add a note Title", Toast.LENGTH_SHORT).show()
            }
        }
    }
}