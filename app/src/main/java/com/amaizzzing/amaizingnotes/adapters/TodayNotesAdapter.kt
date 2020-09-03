package com.amaizzzing.amaizingnotes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amaizzzing.amaizingnotes.R
import com.amaizzzing.amaizingnotes.model.entities.Note
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

    inner class NotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val timeTodayNotesItem = itemView.findViewById<TextView>(R.id.time_today_notes_item)
        private val textTodayNotesItem = itemView.findViewById<TextView>(R.id.text_today_notes_item)
        private val nameTodayNotesItem = itemView.findViewById<TextView>(R.id.name_today_notes_item)
        private val chkbxTodayNotesItem = itemView.chkbx_today_notes_item

        fun bind(item: Note) {
            with(item) {
                timeTodayNotesItem.text = date
                nameTodayNotesItem.text = nameNote
                textTodayNotesItem.text = text
                chkbxTodayNotesItem.isChecked = isDone
            }
            textTodayNotesItem.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) callback.onItemClicked(items[adapterPosition])
            }
            chkbxTodayNotesItem.setOnCheckedChangeListener { _, isChecked ->
                callback.onChcbxChecked(items[adapterPosition], isChecked)
            }
        }
    }

    interface Callback {
        fun onItemClicked(item: Note)
        fun onChcbxChecked(item: Note, isChecked: Boolean)
    }
}