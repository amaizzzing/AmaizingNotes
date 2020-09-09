package com.amaizzzing.amaizingnotes.view.view_states

import com.amaizzzing.amaizingnotes.model.entities.AllResults
import com.amaizzzing.amaizingnotes.view.base.BaseViewState

data class ResultsViewState<T>(
    var _isLoading: Boolean,
    var _error: Throwable?,
    var allResults: T?
):BaseViewState<T>(
    _isLoading,
    _error,
    allResults
)