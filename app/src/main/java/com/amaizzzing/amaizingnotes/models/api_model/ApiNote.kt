package com.amaizzzing.amaizingnotes.models.api_model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "api_note")
data class ApiNote(
    @PrimaryKey(autoGenerate = true)
    var id:Long = 0,
    val date:Long = Calendar.getInstance().time.time,
    val dateStart:Long = 0,
    val dateEnd:Long = 0,
    val nameNote:String = "Название задачи",
    val text:String = "Текст задачи",
    var isDone:Boolean = false
) {
}