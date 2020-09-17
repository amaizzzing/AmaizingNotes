package com.amaizzzing.amaizingnotes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amaizzzing.amaizingnotes.R
import com.amaizzzing.amaizingnotes.model.entities.Note
import com.amaizzzing.amaizingnotes.model.entities.NoteType
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.today_notes_item.view.*

class TodayNotesAdapter(var items: List<Note>, val callback: Callback) :
    RecyclerView.Adapter<TodayNotesAdapter.NotesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder =
        NotesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.today_notes_item, parent, false)
        )

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class NotesViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(note: Note) = with(itemView) {
            time_today_notes_item.text = note.dateFormatted
            name_today_notes_item.text = note.nameNote
            text_today_notes_item.text = note.text
            if (note.background != 0)
                cv_main_today_note_item.background =
                    cv_main_today_note_item.context.getDrawable(note.background)

            if (note.typeNote == NoteType.TASK.type) {
                chkbx_today_notes_item.visibility = View.VISIBLE
            } else {
                chkbx_today_notes_item.visibility = View.GONE
            }
            type_notes_item.text = note.typeNote
            chkbx_today_notes_item.isChecked = note.isDone

            text_today_notes_item.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) callback.onItemClicked(items[adapterPosition])
            }
            chkbx_today_notes_item.setOnCheckedChangeListener { _, isChecked ->
                callback.onChcbxChecked(items[adapterPosition], isChecked)
            }
        }
    }

    interface Callback {
        fun onItemClicked(item: Note)
        fun onChcbxChecked(item: Note, isChecked: Boolean)
    }
}