package com.android.pictureoftheday.view.notes

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.android.pictureoftheday.data.note.Note

abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(dataItem: Pair<Note, Boolean>)
}