package com.example.studentregistrationapp

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etStudentId = findViewById<EditText>(R.id.etStudentId)
        val etFullName = findViewById<EditText>(R.id.etFullName)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etAge = findViewById<EditText>(R.id.etAge)

        val radioGroupGender = findViewById<RadioGroup>(R.id.radioGroupGender)

        val cbCricket = findViewById<CheckBox>(R.id.cbCricket)
        val cbFootball = findViewById<CheckBox>(R.id.cbFootball)
        val cbBasketball = findViewById<CheckBox>(R.id.cbBasketball)

        val spinnerCountry = findViewById<Spinner>(R.id.spinnerCountry)
        val btnDate = findViewById<Button>(R.id.btnDate)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        val btnReset = findViewById<Button>(R.id.btnReset)

        val countries = arrayOf("Select Country", "Bangladesh", "India", "USA", "UK", "Canada")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, countries)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCountry.adapter = adapter

        btnDate.setOnClickListener {
            val calendar = Calendar.getInstance()

            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(
                this,
                { _, y, m, d ->
                    btnDate.text = "$d/${m + 1}/$y"
                },
                year,
                month,
                day
            )

            datePicker.show()
        }

        btnSubmit.setOnClickListener {
            val studentId = etStudentId.text.toString().trim()
            val fullName = etFullName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val ageText = etAge.text.toString().trim()
            val country = spinnerCountry.selectedItem.toString()
            val dob = btnDate.text.toString()

            val gender = when (radioGroupGender.checkedRadioButtonId) {
                R.id.rbMale -> "Male"
                R.id.rbFemale -> "Female"
                R.id.rbOther -> "Other"
                else -> ""
            }

            val sportsList = mutableListOf<String>()
            if (cbCricket.isChecked) sportsList.add("Cricket")
            if (cbFootball.isChecked) sportsList.add("Football")
            if (cbBasketball.isChecked) sportsList.add("Basketball")

            val sports = if (sportsList.isNotEmpty()) sportsList.joinToString(", ") else "None"

            if (studentId.isEmpty() ||
                fullName.isEmpty() ||
                email.isEmpty() ||
                password.isEmpty() ||
                ageText.isEmpty() ||
                gender.isEmpty() ||
                country == "Select Country" ||
                dob == "Select Date"
            ) {
                Toast.makeText(this, "Please complete all required fields", Toast.LENGTH_SHORT).show()
            } else if (!email.contains("@")) {
                Toast.makeText(this, "Email must contain @", Toast.LENGTH_SHORT).show()
            } else if (ageText.toIntOrNull() == null || ageText.toInt() <= 0) {
                Toast.makeText(this, "Age must be greater than 0", Toast.LENGTH_SHORT).show()
            } else {
                val message = """
                    ID: $studentId
                    Name: $fullName
                    Email: $email
                    Password: $password
                    Age: $ageText
                    Gender: $gender
                    Sports: $sports
                    Country: $country
                    DOB: $dob
                """.trimIndent()

                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }
        }

        btnReset.setOnClickListener {
            etStudentId.text.clear()
            etFullName.text.clear()
            etEmail.text.clear()
            etPassword.text.clear()
            etAge.text.clear()

            radioGroupGender.clearCheck()

            cbCricket.isChecked = false
            cbFootball.isChecked = false
            cbBasketball.isChecked = false

            spinnerCountry.setSelection(0)
            btnDate.text = "Select Date"
        }
    }
}