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
                SimpleDateFormat("dd.MM.yy HH:mm").format(apiNote.date),
                apiNote.dateStart,
                apiNote.dateEnd,
                apiNote.nameNote,
                apiNote.text.trim(),
                apiNote.isDone
            )

        fun noteToApiNote(note:Note) : ApiNote =
            ApiNote(
                note.id,
                SimpleDateFormat("dd.MM.yy HH:mm").parse(note.date).time,
                note.dateStart,
                note.dateEnd,
                note.nameNote,
                note.text,
                note.isDone
            )

        fun listApiNoteToListNote(listApiNote:List<ApiNote>):MutableList<Note> =
            listApiNote.map { apiNoteToNote(it) }.toMutableList()

    }

}
