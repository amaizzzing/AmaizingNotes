package com.amaizzzing.amaizingnotes.view.base

open class BaseViewState<T>(
    var isLoading: Boolean,
    var error: Throwable?,
    var data: T?
)