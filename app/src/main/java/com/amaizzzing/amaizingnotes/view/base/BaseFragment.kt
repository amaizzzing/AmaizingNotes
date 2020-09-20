package com.amaizzzing.amaizingnotes.view.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer

abstract class BaseFragment<T, S : BaseViewState<T>> : Fragment() {

    abstract val viewModel: BaseViewModel<T, S>
    abstract val layoutRes: Int
    abstract val rootView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initListeners()
        viewModel.getViewState().observe(viewLifecycleOwner, Observer {
            renderUI(it)
        })

        return rootView
    }

    abstract fun renderUI(data: S)

    abstract fun initListeners()
}