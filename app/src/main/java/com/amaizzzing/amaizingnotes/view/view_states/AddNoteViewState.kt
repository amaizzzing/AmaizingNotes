package com.amaizzzing.amaizingnotes.view.view_states

import com.amaizzzing.amaizingnotes.model.entities.Note

data class AddNoteViewState(
    var isLoading:Boolean,
    var error:Throwable?,
    var note:Note?
)