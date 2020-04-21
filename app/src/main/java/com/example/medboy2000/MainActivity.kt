package com.example.medboy2000

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
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
    private lateinit var daysMeds: LiveData<List<Medication>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        floating_action_button.setOnClickListener { showImportDialog()}

        val weekButtons = arrayOf(monButton, tueButton, wedButton, thuButton, friButton, satButton, sunButton)

        for (button: Button in weekButtons){
            button.setOnClickListener{daySelect(button)}
        }



        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val adapter = MedicationListAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        medicationViewModel = ViewModelProvider(this).get(MedicationViewModel::class.java)
        daysMeds = medicationViewModel.allMeds

        loadDayOfWeek()

        medicationViewModel.allMeds.observe(this, Observer { meds ->
            // Update the cached copy of the words in the adapter.
            meds?.let { adapter.setMeds(it) }
        })


    }

    private fun showImportDialog() {
        val dialogFragment = ImportMedicationDialog(this)

        dialogFragment.show(supportFragmentManager, "signature")
    }

    fun showEditDialog(medication: Medication) {

        val dialogFragment = EditMedicationDialog(this, medication)
        dialogFragment.show(supportFragmentManager, "signature")
    }


    override fun onInsertDataReceived(newMedication: Medication) {

        medicationViewModel.insert(newMedication)

        Toast.makeText(applicationContext,"Medication added.", Toast.LENGTH_LONG).show()

    }
    override fun onUpdateDataReceived(newMedication: Medication) {

        medicationViewModel.update(newMedication)

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





    }


}