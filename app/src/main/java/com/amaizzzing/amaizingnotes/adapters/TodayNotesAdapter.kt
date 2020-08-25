package com.amaizzzing.amaizingnotes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.amaizzzing.amaizingnotes.R
import com.amaizzzing.amaizingnotes.models.entities.Note
import kotlinx.android.synthetic.main.today_notes_item.view.*

class TodayNotesAdapter(var items:List<Note>,val callback: Callback) : RecyclerView.Adapter<TodayNotesAdapter.NotesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder =
        NotesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.today_notes_item,parent,false))

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class NotesViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        private val timeTodayNotesItem = itemView.findViewById<TextView>(R.id.time_today_notes_item)
        private val textTodayNotesItem = itemView.findViewById<TextView>(R.id.text_today_notes_item)
        private val nameTodayNotesItem = itemView.findViewById<TextView>(R.id.name_today_notes_item)
        private val imgMenuTodayNotesItem = itemView.findViewById<ImageView>(R.id.img_menu_today_notes_item)
        private val chkbxTodayNotesItem = itemView.chkbx_today_notes_item

        fun bind(item:Note){
            timeTodayNotesItem.text = item.date
            nameTodayNotesItem.text = item.nameNote
            textTodayNotesItem.text = item.text
            chkbxTodayNotesItem.isChecked = item.isDone

            textTodayNotesItem.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) callback.onItemClicked(items[adapterPosition])
            }
            imgMenuTodayNotesItem.setOnClickListener {
                val pop= PopupMenu(it.context,it)
                pop.inflate(R.menu.popup_menu_notes_item)

                pop.setOnMenuItemClickListener {item->

                    when(item.itemId){
                        R.id.del_popup_menu_notes_item->{
                            callback.onDeleteClicked(items[adapterPosition])
                        }
                    }
                    true
                }
                pop.show()
                true
            }
            chkbxTodayNotesItem.setOnCheckedChangeListener { buttonView, isChecked -> callback.onChcbxChecked(items[adapterPosition]) }
        }
    }
    interface Callback {
        fun onItemClicked(item: Note)
        fun onDeleteClicked(item: Note)
        fun onChcbxChecked(item:Note)
    }
}