package com.learn.local_unit_sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import java.util.*

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    // The helper that manages writing to SharedPreferences.
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    // The input field where the user enters their name.
    private lateinit var nameText: EditText

    // The date picker where the user enters their date of birth.
    private lateinit var dobPicker: DatePicker

    // The input field where the user enters their email.
    private lateinit var emailText: EditText

    // The validator for the email input field.
    private var emailValidator = EmailValidator()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Shortcuts to input fields.
        nameText = findViewById(R.id.userNameInput)
        dobPicker = findViewById(R.id.dateOfBirthInput)
        emailText = findViewById(R.id.emailInput)

        // Setup email field validator.
        emailText.addTextChangedListener(emailValidator)

        // Instantiate a SharedPreferencesHelper.
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        sharedPreferencesHelper = SharedPreferencesHelper(sharedPreferences)

        // Fill input fields from data retrieved from the SharedPreferences.
        populateUi()
    }

    /**
     * Initialize all fields from the personal info saved in the SharedPreferences.
     */
    private fun populateUi() {
        val sharedPreferenceEntry = sharedPreferencesHelper.getPersonalInfo()
        nameText.setText(sharedPreferenceEntry.name)
        val dateOfBirth = sharedPreferenceEntry.dateOfBirth
        dobPicker.init(
            dateOfBirth.get(Calendar.YEAR), dateOfBirth.get(Calendar.MONTH),
            dateOfBirth.get(Calendar.DAY_OF_MONTH), null
        )
        emailText.setText(sharedPreferenceEntry.email)
    }


    /**
     * 当"Save"按钮被点击时执行此函数
     */
    fun onSaveClick(view: View) {
        // Don't save if the fields do not validate.
        if (!emailValidator.isValid) {
            emailText.error = "Invalid email"
            Log.w(TAG, "Not saving personal information: Invalid email")
            return
        }

        // Get the text from the input fields.
        val name = nameText.text.toString()
        val dateOfBirth = Calendar.getInstance()
        dateOfBirth.set(dobPicker.year, dobPicker.month, dobPicker.dayOfMonth)
        val email = emailText.text.toString()

        // Create a Setting model class to persist.
        val sharedPreferenceEntry = SharedPreferenceEntry(name, dateOfBirth, email)

        // Persist the personal information.
        val isSuccess = sharedPreferencesHelper.savePersonalInfo(sharedPreferenceEntry)
        if (isSuccess) {
            Toast.makeText(this, "Personal information saved", Toast.LENGTH_LONG).show()
            Log.i(TAG, "Personal information saved")
        } else {
            Log.e(TAG, "Failed to write personal information to SharedPreferences")
        }
    }

    /**
     * 当"Revert"按钮被点击时，执行此函数。
     */
    fun onRevertClick(view: View) {
        populateUi()
        Toast.makeText(this, "Personal information reverted", Toast.LENGTH_LONG).show()
        Log.i(TAG, "Personal information reverted")
    }
}
