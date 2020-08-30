package com.amaizzzing.amaizingnotes.model.entities

import com.amaizzzing.amaizingnotes.utils.NOTE_TYPE_VALUE
import com.amaizzzing.amaizingnotes.utils.TASK_TYPE_VALUE

enum class NoteType(val type: String) { NOTE(NOTE_TYPE_VALUE), TASK(TASK_TYPE_VALUE) }