package com.amaizzzing.amaizingnotes.model.entities

data class AllResults(
    var dayResults:ResultsNotesItem = ResultsNotesItem(1f,0),
    var day7Results:ResultsNotesItem = ResultsNotesItem(1f,0),
    var day30Results:ResultsNotesItem = ResultsNotesItem(1f,0)
)