package com.amaizzzing.amaizingnotes.model.di.modules

import com.amaizzzing.amaizingnotes.model.data.TodayNoteDatasource
import com.amaizzzing.amaizingnotes.model.data.TodayNoteDatasourceImpl
import dagger.Module
import dagger.Provides

@Module
class NoteDatasourceModule {
    @Provides
    fun getNoteDatasourceModule() : TodayNoteDatasource = TodayNoteDatasourceImpl()
}