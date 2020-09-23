package com.amaizzzing.amaizingnotes.view.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

abstract class BaseFragment<S> : Fragment(),CoroutineScope {
    override val coroutineContext: CoroutineContext by lazy {
        Dispatchers.Main + Job()
    }
    private lateinit var dataJob: Job
    private lateinit var errorJob: Job
    abstract val viewModel: BaseViewModel<S>
    abstract val layoutRes: Int
    abstract val rootView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initListeners()
        dataJob = launch {
            viewModel.getViewState().consumeEach {
                renderUI(it)
            }
        }

        /*errorJob = launch {
            model.getErrorChannel().consumeEach {
                renderError(it)
            }
        }*/

        /*viewModel.getViewState().observe(viewLifecycleOwner, Observer {
            renderUI(it)
        })*/

        return rootView
    }

    abstract fun renderUI(data: S)

    abstract fun initListeners()
}