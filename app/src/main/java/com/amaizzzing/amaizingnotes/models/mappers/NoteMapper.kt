package com.amaizzzing.amaizingnotes.models.mappers

import com.amaizzzing.amaizingnotes.models.api_model.ApiNote
import com.amaizzzing.amaizingnotes.models.entities.Note
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime

class NoteMapper{
    companion object{
        fun apiNoteToNote(apiNote: ApiNote) : Note =
            Note(
                apiNote.id,
                SimpleDateFormat("dd.MM.yyyy HH:mm").format(apiNote.date),
                apiNote.dateStart,
                apiNote.dateEnd,
                apiNote.nameNote,
                apiNote.text.trim()
            )

        fun noteToApiNote(note:Note) : ApiNote =
            ApiNote(
                note.id,
                SimpleDateFormat("dd.MM.yyyy HH:mm").parse(note.date).time,
                note.dateStart,
                note.dateEnd,
                note.nameNote,
                note.text
            )

        fun listApiNoteToListNote(listApiNote:List<ApiNote>):MutableList<Note> =
            listApiNote.map { apiNoteToNote(it) }.toMutableList()

    }

}
