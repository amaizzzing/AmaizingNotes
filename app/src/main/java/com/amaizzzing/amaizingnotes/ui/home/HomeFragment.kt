package com.amaizzzing.amaizingnotes.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amaizzzing.amaizingnotes.R
import com.amaizzzing.amaizingnotes.adapters.TodayNotesAdapter
import com.amaizzzing.amaizingnotes.models.entities.Note
import com.amaizzzing.amaizingnotes.viewmodel.TodayNotesViewModel
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {
    lateinit var todayNotesViewModel: TodayNotesViewModel
    lateinit var rvFragmentHome: RecyclerView
    lateinit var todayNotesAdapter: TodayNotesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        initViews(root)

        todayNotesViewModel = ViewModelProvider(this).get(TodayNotesViewModel::class.java)

        todayNotesViewModel.listTodayNotes.observe(viewLifecycleOwner, Observer {
            initRecyclerView(it)
        })

        return root
    }

    override fun onStart() {
        super.onStart()
        todayNotesViewModel.onStart()
    }

    override fun onStop() {
        super.onStop()
        todayNotesViewModel.onStop()
    }

    fun initViews(v:View) {
        rvFragmentHome = v.findViewById(R.id.rv_fragment_home)
    }

    fun initRecyclerView(notesList: List<Note>) {
        todayNotesAdapter = TodayNotesAdapter(notesList)
        rvFragmentHome.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        rvFragmentHome.adapter = todayNotesAdapter
    }
}
