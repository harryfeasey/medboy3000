package com.example.medboy2000

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), CallbackListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        floating_action_button.setOnClickListener { showDialog()}
        loadDayOfWeek()
    }

    private fun showDialog() {
        val dialogFragment = ImportMedicationDialog(this)

        dialogFragment.show(supportFragmentManager, "signature")
    }

    override fun onDataReceived(data: String) {
        textView.text = data
    }

    private fun loadDayOfWeek(){
        //TODO get day of week and select button on load.
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_WEEK)
        var pageButton = when (day){
            1 -> monButton
            2 -> tueButton
            3 -> wedButton
            4 -> thuButton
            5 -> friButton
            6 -> satButton
            else -> sunButton
        }

        pageButton.isSelected = true
        pageButton.setBackgroundColor(Color.parseColor("#8E56DF"))


    }

}