package com.amaizzzing.amaizingnotes.view.base

import com.amaizzzing.amaizingnotes.model.entities.Note

open class BaseViewState<T>(
    var isLoading:Boolean,
    var error:Throwable?,
    var data:T?
)