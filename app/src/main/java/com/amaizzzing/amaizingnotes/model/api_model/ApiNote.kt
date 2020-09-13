package com.amaizzzing.amaizingnotes.model.api_model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.amaizzzing.amaizingnotes.model.entities.NoteType
import com.amaizzzing.amaizingnotes.utils.DEFAULT_NOTE_NAME
import com.amaizzzing.amaizingnotes.utils.DEFAULT_NOTE_TEXT
import com.amaizzzing.amaizingnotes.utils.NOTE_TABLE_NAME
import java.util.*

@Entity(tableName = NOTE_TABLE_NAME)
data class ApiNote(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var typeNote: String = NoteType.NOTE.type,
    val date: Long = Calendar.getInstance().time.time,
    val dateStart: Long = 0,
    val dateEnd: Long = 0,
    val nameNote: String = DEFAULT_NOTE_NAME,
    val text: String = DEFAULT_NOTE_TEXT,
    var isDone: Boolean = false,
    var background:Int = 0
)