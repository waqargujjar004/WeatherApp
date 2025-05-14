package com.example.hazelweather.ui

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.hazelweather.R
import com.example.hazelweather.databinding.FragmentMyBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics

class MyBottomSheetFragment : BottomSheetDialogFragment() {

    private val selectedOptions = mutableSetOf<String>() // To track selected options
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMyBottomSheetBinding.inflate(inflater, container, false)

        val buttonOption1 = binding.buttonOption1
        val buttonOption2 = binding.buttonOption2
        val buttonOption3 = binding.buttonOption3
        val buttonOption4 = binding.buttonOption4
        val buttonOption5 = binding.buttonOption5
        firebaseAnalytics = FirebaseAnalytics.getInstance(requireContext())


        val userInputField = binding.userInputField  // Reference to the EditText



        // Set the button click listeners
        buttonOption1.setOnClickListener { toggleButtonState(buttonOption1, "Option 1") }
        buttonOption2.setOnClickListener { toggleButtonState(buttonOption2, "Option 2") }
        buttonOption3.setOnClickListener { toggleButtonState(buttonOption3, "Option 3") }
        buttonOption4.setOnClickListener { toggleButtonState(buttonOption4, "Option 4") }
        buttonOption5.setOnClickListener { toggleButtonState(buttonOption5, "Option 5") }

        // Handle "Done" button action
        binding.btnDone.setOnClickListener {
           for (option in selectedOptions) {
                val eventName = when (option) {
                    "Option 1" -> "option_1_selected"
                    "Option 2" -> "option_2_selected"
                    "Option 3" -> "option_3_selected"
                    "Option 4" -> "option_4_selected"
                    "Option 5" -> "option_5_selected"
                    else -> "unknown_option_selected"
                }
              val userInputText = userInputField.text.toString()
                val limitedInput = if (userInputText.length > 90) {
                    userInputText.substring(0, 90)
                } else {
                    userInputText
                }

                val bundle = Bundle().apply {
                    putString("Description", "Data $limitedInput")
                }

                firebaseAnalytics.logEvent(eventName, bundle)
                Log.d("FirebaseEvent", "Event: $eventName | Bundle: $limitedInput")
               val exception = Exception("$eventName: $userInputText")
               FirebaseCrashlytics.getInstance().recordException(exception)
            }

            Toast.makeText(requireContext(), "Selected: $selectedOptions", Toast.LENGTH_SHORT).show()
            dismiss()
        }

        return binding.root
    }

    // Method to toggle the selected state of the button
    private fun toggleButtonState(button: Button, option: String) {
        if (selectedOptions.contains(option)) {
            selectedOptions.remove(option)
            button.setBackgroundColor(Color.GRAY)
        } else {
            selectedOptions.add(option)
            button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.dark_purple))

        }
    }

}
