package com.wema.noteapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.wema.noteapp.NoteFragmentDirections
import com.wema.noteapp.R
import com.wema.noteapp.db.Note
import com.wema.noteapp.ui.viewmodels.NoteViewModel

class NoteAdapter(
    var notes: List<Note>,
    val viewModel: NoteViewModel?
): RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {
    inner class NoteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.itemView.apply {
            val noteId = notes[position].id
            val title = notes[position].title
            val body = notes[position].body
            this.findViewById<TextView>(R.id.tvNoteTitle).text = title
            this.findViewById<TextView>(R.id.tvNoteBody).text = body

            this.findViewById<ImageView>(R.id.ivDelete).setOnClickListener {
                viewModel?.deleteNote(notes[position])
            }
            this.setOnClickListener {
                val direction = NoteFragmentDirections.actionNoteFragmentToShowNoteFragment(noteId.toLong())
                findNavController().navigate(direction)
            }
        }
    }

    override fun getItemCount(): Int {
        return notes.size
    }
}