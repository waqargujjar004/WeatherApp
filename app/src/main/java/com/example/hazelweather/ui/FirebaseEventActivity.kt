package com.example.hazelweather.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.hazelweather.R
import com.example.hazelweather.databinding.ActivityFirebaseEventBinding
import com.example.hazelweather.databinding.ActivityMainBinding
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.hazelmobile.core.bases.activity.BaseActivity

class FirebaseEventActivity : BaseActivity<ActivityFirebaseEventBinding>(ActivityFirebaseEventBinding::inflate) {
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    override fun onViewBindingCreated(savedInstanceState: Bundle?) {
        super.onViewBindingCreated(savedInstanceState)




        val showBottomSheetButton = findViewById<Button>(R.id.btnTriggerEvent)

        showBottomSheetButton.setOnClickListener {
            // Show the BottomSheet when the button is clicked
            showBottomSheet()
        }








// Firebase

        firebaseAnalytics = FirebaseAnalytics.getInstance(this)


        val crashButton = Button(this)
        crashButton.text = "Test Crash"
        crashButton.setOnClickListener {
            FirebaseAnalytics.getInstance(this).logEvent("button_click", null)
            throw RuntimeException("Test Crash") // Force a crash
        }

        addContentView(
            crashButton, ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )
        val bundle = Bundle().apply {
            putString("use14gmail", "user@example.com")
            putString("feedback_description", "I love your app!")
        }
        firebaseAnalytics.logEvent("WAQAR", bundle)


        val abundle = Bundle().apply {
            putString("w4g_email", "hazel@mobile.com")
            putString(
                "feedback_description",
                "I love your app! The design is clean, and everything works smoothly. Great job!"
            )
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
    // Method to show the BottomSheetFragment
    private fun showBottomSheet() {
        val bottomSheetFragment = MyBottomSheetFragment() // Your BottomSheetFragment class
        bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
    }
}


