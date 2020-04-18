package com.example.medboy2000

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import androidx.lifecycle.Observer
import java.util.*
import com.google.gson.Gson
import java.io.*


class MainActivity : AppCompatActivity(), CallbackListener {

    private lateinit var selected:Button
    private lateinit var medicationViewModel: MedicationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        floating_action_button.setOnClickListener { showDialog()}

        val weekButtons = arrayOf(monButton, tueButton, wedButton, thuButton, friButton, satButton, sunButton)

        for (button: Button in weekButtons){
            button.setOnClickListener{daySelect(button)}
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val adapter = MedicationListAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        medicationViewModel = ViewModelProvider(this).get(MedicationViewModel::class.java)
        medicationViewModel.allMeds.observe(this, Observer { meds ->
            // Update the cached copy of the words in the adapter.
            meds?.let { adapter.setMeds(it) }
        })

        loadDayOfWeek()

    }

    private fun showDialog() {
        val dialogFragment = ImportMedicationDialog(this)

        dialogFragment.show(supportFragmentManager, "signature")
    }


    override fun onDataReceived(newMedication: Medication) {

        medicationViewModel.insert(newMedication)

        Toast.makeText(applicationContext,"Medication added.", Toast.LENGTH_LONG).show()

//        textView.text = newMedication.reminder.toString()


//        val gson = Gson()
//        val yourObjectJson = gson.toJson(newMedication)
//
//        //Get your FilePath and use it to create your File
//        val filename = selected.text.toString() + ".txt"
//
//
//        val filePath = this.filesDir.toString() + "/" + filename
//        val file = File(filePath)
//        //Create your FileOutputStream, yourFile is part of the constructor
//        val fileOutputStream = FileOutputStream(file)
//
//
//        try {
//
//            //Convert your JSON String to Bytes and write() it
//            fileOutputStream.write(yourObjectJson.toByteArray())
//            //Finally flush and close your FileOutputStream
//            fileOutputStream.flush()
//            fileOutputStream.close()
//
//        } catch (e: FileNotFoundException){
//            e.printStackTrace()
//        }catch (e: NumberFormatException){
//            e.printStackTrace()
//        }catch (e: IOException){
//            e.printStackTrace()
//        }catch (e: Exception){
//            e.printStackTrace()
//        }

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
//        loadMedication(filename)

    }

//    private fun loadMedication(fileName:String){
//
//        val gson = Gson()
//        var text = ""
//
//        try {
//            //Make your FilePath and File
//            val yourFilePath = applicationContext.filesDir.toString() + "/" + fileName
//            val yourFile = File(yourFilePath)
//            //Make an InputStream with your File in the constructor
//            val inputStream = FileInputStream(yourFile)
//            val stringBuilder = StringBuilder()
//
//            //Check to see if your inputStream is null
//            //If it isn't use the inputStream to make a InputStreamReader
//            //Use that to make a BufferedReader
//            //Also create an empty String
//
//            if (inputStream != null) {
//                val inputStreamReader = InputStreamReader(inputStream)
//                val bufferedReader = BufferedReader(inputStreamReader)
//                var receiveString = ""
//                //Use a while loop to append the lines from the Buffered reader
//                var value = bufferedReader.readLine()
//                while (value != null) {
//                    stringBuilder.append(value)
//                    value = bufferedReader.readLine()
//                }
//                //Close your InputStream and save stringBuilder as a String
//                inputStream.close()
//                text = stringBuilder.toString()
//            }
//            //Use Gson to recreate your Object from the text String
//            val med = gson.fromJson(text, Medication::class.java)
//            checkNotNull(text)
//            println(text)
//            textView.text = med.name + "\nTake " + med.dosage + ".\n" + med.reminder.toString()
//
//        } catch (e: FileNotFoundException) {
//            e.printStackTrace()
//            textView.text = "Nothing to take today."
//        } catch (e: IOException) {
//            e.printStackTrace()
//
//        }catch (e: IllegalStateException) {
//            e.printStackTrace()
//        }
//
//    }


}