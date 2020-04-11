package com.example.medboy2000

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import com.google.gson.Gson
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import android.content.Context
import java.io.IOException


class MainActivity : AppCompatActivity(), CallbackListener {

    private lateinit var selected:Button

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

        //Get your FilePath and use it to create your File
        val filename = selected.text.toString() + ".txt"


        val filePath = this.filesDir.toString() + "/" + filename
        val file = File(filePath)
        //Create your FileOutputStream, yourFile is part of the constructor
        val fileOutputStream = FileOutputStream(file)


        try {

            //Convert your JSON String to Bytes and write() it
            fileOutputStream.write(yourObjectJson.toByteArray())
            //Finally flush and close your FileOutputStream
            fileOutputStream.flush()
            fileOutputStream.close()

        } catch (e: FileNotFoundException){
            e.printStackTrace()
        }catch (e: NumberFormatException){
            e.printStackTrace()
        }catch (e: IOException){
            e.printStackTrace()
        }catch (e: Exception){
            e.printStackTrace()
        }
        Toast.makeText(applicationContext,"Medication added.", Toast.LENGTH_LONG).show()

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

    private fun daySelect(newSelected: Button){
        selected = newSelected
        val weekButtons = arrayOf(monButton, tueButton, wedButton, thuButton, friButton, satButton, sunButton)

        //Un-select other buttons
        for (button: Button in weekButtons){

            button.isSelected = false
            button.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }


        selected.isSelected = true
        selected.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimarySelected))
        val filename = selected.text.toString() + ".txt"
        // TODO Get the Gson objects for medication stored for this day and display it.


    }


}