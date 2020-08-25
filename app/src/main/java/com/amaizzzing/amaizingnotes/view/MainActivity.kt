package com.amaizzzing.amaizingnotes.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.amaizzzing.amaizingnotes.NotesApplication
import com.amaizzzing.amaizingnotes.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        configNavController()
    }

    private fun configNavController() {
        val navView = nav_view
        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_calendar,
                R.id.navigation_notifications
            )
        )
        navView.setupWithNavController(navController)
    }

    override fun onDestroy() {
        NotesApplication.instance.getAppDataBase(this)?.close()
        NotesApplication.instance.destroyDataBase()

        super.onDestroy()
    }
}

