package com.amaizzzing.amaizingnotes.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.amaizzzing.amaizingnotes.NotesApplication
import com.amaizzzing.amaizingnotes.R
import com.amaizzzing.amaizingnotes.utils.MY_WORKER_TYPE
import com.amaizzzing.amaizingnotes.utils.MyWorkerFactory
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        configNavController()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            "mywork",
            ExistingPeriodicWorkPolicy.KEEP,
            MyWorkerFactory.getWorker(MY_WORKER_TYPE.PERIODIC) as PeriodicWorkRequest
        )
            .state.observeForever {
                Log.e("MyDB", "${it}")
            }
    }

    private fun configNavController() {
        val navView = nav_view
        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_calendar,
                R.id.navigation_not_finish,
                R.id.navigation_notification
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

