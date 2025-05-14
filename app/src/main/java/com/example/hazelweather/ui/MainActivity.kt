package com.example.hazelweather.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
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
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.hazelmobile.core.bases.activity.BaseActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    override fun onViewBindingCreated(savedInstanceState: Bundle?) {
        super.onViewBindingCreated(savedInstanceState)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.weatherNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)
// Firebase

        firebaseAnalytics = FirebaseAnalytics.getInstance(this)


        val crashButton = Button(this)
        crashButton.text = "Test Crash"
        crashButton.setOnClickListener {
            FirebaseAnalytics.getInstance(this).logEvent("button_click", null)
            throw RuntimeException("Test Crash") // Force a crash
        }

        addContentView(crashButton, ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT))
        val bundle = Bundle().apply {
            putString("use14gmail", "user@example.com")
            putString("feedback_description", "I love your app!")
        }
        firebaseAnalytics.logEvent("WAQAR", bundle)


        val abundle = Bundle().apply {
            putString("w4g_email", "hazel@mobile.com")
            putString("feedback_description", "I love your app! The design is clean, and everything works smoothly. Great job!")
        }
        firebaseAnalytics.logEvent("WAQAR", abundle)
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 60// 1 hour
        }


        // FIrebase Remote Config Rollout and ab testing

        val firebaseRemoteConfig = Firebase.remoteConfig
        firebaseRemoteConfig.setConfigSettingsAsync(configSettings)
        firebaseRemoteConfig.setDefaultsAsync(
            mapOf(
                "Greeting_Text" to "Hi"
            )
        )

        Handler(Looper.getMainLooper()).postDelayed({
            firebaseRemoteConfig.fetch(0)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val greeting = firebaseRemoteConfig.getString("Greeting_Text")
                        crashButton.text = greeting
                    }
                }
        }, 60000)

        firebaseAnalytics.setUserProperty("user_type", "new")
        Log.d("RemoteConfig", "use_new_onboarding = before yes")


//            val remoteConfig = Firebase.remoteConfig
//        Log.d("RemoteConfig", "use_new_onboarding = yes")
//            remoteConfig.fetchAndActivate()
//                .addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        val showOnboarding = remoteConfig.getBoolean("use_new_onboarding")
//                        Log.d("RemoteConfig", "use_new_onboarding = $showOnboarding")
//
//                        if (showOnboarding) {
//                            // Show onboarding screen
//
//                        } else {
//                            // Skip onboarding
//                        }
//                    } else {
//                        Log.e("RemoteConfig", "Fetch failed: ${task.exception?.message}")
//                    }
//                }

        val remoteConfig = Firebase.remoteConfig
        Log.d("RemoteConfig", "use_new_onboarding = yes")
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val showOnboarding = remoteConfig.getBoolean("custom_signal")
                    Log.d("RemoteConfig", "use_new_onboarding = $showOnboarding")

                    if (showOnboarding) {
                        // Show onboarding screen

                    } else {
                        // Skip onboarding
                    }
                } else {
                    Log.e("RemoteConfig", "Fetch failed: ${task.exception?.message}")
                }
            }

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
