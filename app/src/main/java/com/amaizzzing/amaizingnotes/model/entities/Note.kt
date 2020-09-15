package com.amaizzzing.amaizingnotes.model.entities

data class Note(
    val id: Long,
    var typeNote: String,
    val date: Long,
    val dateFormatted : String,
    val dateStart: Long,
    val dateEnd: Long,
    val nameNote: String,
    val text: String,
    var isDone: Boolean,
    var background:Int
)