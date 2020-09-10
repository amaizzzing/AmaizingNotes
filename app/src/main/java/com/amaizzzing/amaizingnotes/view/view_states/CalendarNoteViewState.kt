package com.amaizzzing.amaizingnotes.view.view_states

import com.amaizzzing.amaizingnotes.model.entities.Note
import com.amaizzzing.amaizingnotes.view.base.BaseViewState

data class CalendarNoteViewState<T>(
    val _isLoading: Boolean,
    val _error: Throwable?,
    val notes: T?
) : BaseViewState<T>(
    _isLoading,
    _error,
    notes
)