package com.amaizzzing.amaizingnotes.viewmodel

import com.amaizzzing.amaizingnotes.model.NoAuthException
import com.amaizzzing.amaizingnotes.model.entities.Note
import com.amaizzzing.amaizingnotes.model.entities.User
import com.amaizzzing.amaizingnotes.model.interactors.TodayNotesInteractor
import com.amaizzzing.amaizingnotes.view.base.BaseViewModel
import com.amaizzzing.amaizingnotes.view.view_states.CalendarNoteViewState
import com.amaizzzing.amaizingnotes.view.view_states.SplashViewState
import javax.inject.Inject

class SplashViewModel (val interactor: TodayNotesInteractor) :
    BaseViewModel<SplashViewState<User>>() {
    fun requestUser() {
        interactor.getCurrentUser().observeForever {
            viewStateLiveData.value = it?.let {
                SplashViewState(false,null,it)
            } ?:
                SplashViewState(false,NoAuthException(),it)

        }
    }
}