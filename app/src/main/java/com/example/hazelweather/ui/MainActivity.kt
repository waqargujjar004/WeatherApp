package com.example.hazelweather.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.hazelweather.R
import com.example.hazelweather.databinding.ActivityMainBinding
import com.hazelmobile.core.bases.activity.BaseActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    override fun onViewBindingCreated(savedInstanceState: Bundle?) {
        super.onViewBindingCreated(savedInstanceState)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.weatherNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)




    }
//    override fun onStart() {
//        super.onStart()
//        Log.d("LifecycleCheck", "onStart called")
//        Toast.makeText(this, "onStart", Toast.LENGTH_SHORT).show()
//    }
//
//    override fun onResume() {
//        super.onResume()
//        Log.d("LifecycleCheck", "onResume called")
//        Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show()
//    }
//
//    override fun onPause() {
//        super.onPause()
//        Log.d("LifecycleCheck", "onPause called")
//        Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show()
//    }
//
//    override fun onStop() {
//        super.onStop()
//        Log.d("LifecycleCheck", "onStop called")
//        Toast.makeText(this, "onStop", Toast.LENGTH_SHORT).show()
//    }
//
//    override fun onRestart() {
//        super.onRestart()
//        Log.d("LifecycleCheck", "onRestart called")
//        Toast.makeText(this, "onRestart", Toast.LENGTH_SHORT).show()
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        Log.d("LifecycleCheck", "onDestroy called")
//        Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show()
//    }

}
