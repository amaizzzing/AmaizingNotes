package com.amaizzzing.amaizingnotes.view.view_states

import com.amaizzzing.amaizingnotes.model.entities.Note

data class CalendarNoteViewState(
    var isLoading:Boolean,
    var error:Throwable?,
    var notes:List<Note>?)