package com.amaizzzing.amaizingnotes.view.view_states

import com.amaizzzing.amaizingnotes.view.base.BaseViewState

data class SplashViewState<T>(
    val _isLoading: Boolean,
    val _error: Throwable?,
    val _data: T?
) : BaseViewState<T>(
    _isLoading,
    _error,
    _data
)