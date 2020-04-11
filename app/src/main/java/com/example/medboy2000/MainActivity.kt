package com.example.medboy2000

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import com.google.gson.Gson



class MainActivity : AppCompatActivity(), CallbackListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        floating_action_button.setOnClickListener { showDialog()}

        val weekButtons = arrayOf(monButton, tueButton, wedButton, thuButton, friButton, satButton, sunButton)

        for (button: Button in weekButtons){
            button.setOnClickListener{daySelect(button)}
        }

        loadDayOfWeek()

    }

    private fun showDialog() {
        val dialogFragment = ImportMedicationDialog(this)

        dialogFragment.show(supportFragmentManager, "signature")
    }

    override fun onDataReceived(newMedication: Medication) {
        textView.text = newMedication.reminder.toString()


        val gson = Gson()
        val yourObjectJson = gson.toJson(newMedication)





    }

    private fun loadDayOfWeek(){

        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_WEEK)
        var pageButton = when (day){
            2 -> monButton
            3 -> tueButton
            4 -> wedButton
            5 -> thuButton
            6 -> friButton
            7 -> satButton
            else -> sunButton
        }

        daySelect(pageButton)

    }

    private fun daySelect(selected: Button){

        val weekButtons = arrayOf(monButton, tueButton, wedButton, thuButton, friButton, satButton, sunButton)

        //Un-select other buttons
        for (button: Button in weekButtons){

            button.isSelected = false
            button.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }


        selected.isSelected = true
        selected.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimarySelected))
//        val filename = selected.text.toString() + ".txt"
        // TODO Get the Gson objects for medication stored for this day and display it.


    }


}