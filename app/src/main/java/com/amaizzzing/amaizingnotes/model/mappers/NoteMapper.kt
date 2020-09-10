package com.amaizzzing.amaizingnotes.model.mappers

import com.amaizzzing.amaizingnotes.model.api_model.ApiNote
import com.amaizzzing.amaizingnotes.model.entities.Note
import com.amaizzzing.amaizingnotes.model.entities.NoteType
import com.amaizzzing.amaizingnotes.utils.FULL_DATE_PATTERN
import java.text.SimpleDateFormat
import java.util.*

class NoteMapper {
    companion object {
        fun apiNoteToNote(apiNote: ApiNote): Note =
            Note(
                apiNote.id,
                apiNote.typeNote,
                apiNote.date,
                SimpleDateFormat(FULL_DATE_PATTERN, Locale.ROOT).format(apiNote.date),
                apiNote.dateStart,
                apiNote.dateEnd,
                apiNote.nameNote,
                apiNote.text.trim(),
                apiNote.isDone
            )

        fun noteToApiNote(note: Note): ApiNote =
            ApiNote(
                note.id,
                note.typeNote,
                note.date,
                note.dateStart,
                note.dateEnd,
                note.nameNote,
                note.text,
                note.isDone
            )

        fun listApiNoteToListNote(listApiNote: List<ApiNote>): MutableList<Note> =
            listApiNote.map { apiNoteToNote(it) }.toMutableList()

    }

}
