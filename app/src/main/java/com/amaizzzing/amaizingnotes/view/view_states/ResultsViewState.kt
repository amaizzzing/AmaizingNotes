package com.amaizzzing.amaizingnotes.view.view_states

import com.amaizzzing.amaizingnotes.model.entities.AllResults

data class ResultsViewState(
    var isLoading: Boolean,
    var error: Throwable?,
    var allResults: AllResults?
)