package com.amaizzzing.amaizingnotes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amaizzzing.amaizingnotes.R
import com.amaizzzing.amaizingnotes.model.entities.Note

class BottomDialogAdapter(var items:List<Int>/*,val callback : Callback*/):
    RecyclerView.Adapter<BottomDialogAdapter.BottomViewHolder>() {

    inner class BottomViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        private val imgBottomDialog = itemView.findViewById<ImageView>(R.id.img_bottom_dialog)
        fun bind(item:Int){
            imgBottomDialog.setImageResource(item)
        }
    }

    interface Callback {
        fun onItemClicked(item: Note)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        BottomViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.bottom_dialog_item, parent, false)
        )

    override fun onBindViewHolder(holder: BottomViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.count()
}