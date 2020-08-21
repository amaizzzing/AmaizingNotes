package com.amaizzzing.amaizingnotes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amaizzzing.amaizingnotes.R
import com.amaizzzing.amaizingnotes.models.entities.Note

class TodayNotesAdapter(var items:List<Note>) : RecyclerView.Adapter<TodayNotesAdapter.NotesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder =
        NotesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.today_notes_item,parent,false))

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class NotesViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        private val timeTodayNotesItem = itemView.findViewById<TextView>(R.id.time_today_notes_item)
        private val textTodayNotesItem = itemView.findViewById<TextView>(R.id.text_today_notes_item)

        fun bind(item:Note){
            timeTodayNotesItem.text = item.date.toString()
            textTodayNotesItem.text = item.text
        }
    }
}