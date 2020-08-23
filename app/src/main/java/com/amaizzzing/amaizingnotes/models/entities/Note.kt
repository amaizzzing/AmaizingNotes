package com.amaizzzing.amaizingnotes.models.entities

data class Note(
    val id:Long,
    val date:String,
    val dateStart:Long,
    val dateEnd:Long,
    val nameNote:String,
    val text:String) {
}