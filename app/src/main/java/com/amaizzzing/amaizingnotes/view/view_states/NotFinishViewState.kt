package com.amaizzzing.amaizingnotes.view.view_states

import com.amaizzzing.amaizingnotes.model.entities.Note
import com.amaizzzing.amaizingnotes.view.base.BaseViewState

data class NotFinishViewState<T>(
    var _isLoading:Boolean,
    var _error:Throwable?,
    var notes:T?
):BaseViewState<T>(
    _isLoading,
    _error,
    notes
)