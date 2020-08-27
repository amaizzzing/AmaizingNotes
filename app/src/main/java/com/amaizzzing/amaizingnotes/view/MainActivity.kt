package com.amaizzzing.amaizingnotes.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.amaizzzing.amaizingnotes.NotesApplication
import com.amaizzzing.amaizingnotes.R
import com.amaizzzing.amaizingnotes.utils.MyWorker
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        configNavController()

        val constraints = Constraints.Builder()
            .setRequiresCharging(true)
            .build()

        val currentDate = Calendar.getInstance()
        val dueDate = Calendar.getInstance()// Set Execution around 05:00:00 AM
        dueDate.set(Calendar.HOUR_OF_DAY, 12)
        dueDate.set(Calendar.MINUTE, 10)
        dueDate.set(Calendar.SECOND, 0)
        if (dueDate.before(currentDate)) {
            dueDate.add(Calendar.HOUR_OF_DAY, 0)
        }
        val timeDiff = dueDate.timeInMillis - currentDate.timeInMillis
        val dailyWorkRequest = OneTimeWorkRequestBuilder<MyWorker>()
            //.setConstraints(constraints)
            .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
            .addTag("fdfdfdf") .build()
        WorkManager.getInstance().enqueue(dailyWorkRequest)
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

