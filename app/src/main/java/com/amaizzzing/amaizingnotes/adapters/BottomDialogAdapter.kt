package com.amaizzzing.amaizingnotes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.amaizzzing.amaizingnotes.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.bottom_dialog_item.view.*

class BottomDialogAdapter(var items: List<Int>, val callback: Callback) :
    RecyclerView.Adapter<BottomDialogAdapter.BottomViewHolder>() {

    inner class BottomViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {
        private val imgBottomDialog = itemView.findViewById<ImageView>(R.id.img_bottom_dialog)
        fun bind(item: Int) {
            with(itemView) {
                img_bottom_dialog.setImageResource(item)
            }
            imgBottomDialog.setOnClickListener {
                callback.onItemClicked(items[adapterPosition])
            }
        }
    }

    interface Callback {
        fun onItemClicked(item: Int)
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