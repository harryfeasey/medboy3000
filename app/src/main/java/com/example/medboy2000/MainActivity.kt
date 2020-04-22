package com.example.medboy2000

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
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
import android.content.Intent
import android.provider.CalendarContract.Events
import android.provider.CalendarContract
import android.os.Build
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.AsyncTask


class MainActivity : AppCompatActivity(), CallbackListener {

    private lateinit var selected:Button
    private lateinit var medicationViewModel: MedicationViewModel
    private lateinit var daysMeds: List<Medication>
    private lateinit var adapter:MedicationListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        floating_action_button.setOnClickListener { showImportDialog()}

        val weekButtons = arrayOf(monButton, tueButton, wedButton, thuButton, friButton, satButton, sunButton)

        for (button: Button in weekButtons){
            button.setOnClickListener{daySelect(button)}
        }



        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        adapter = MedicationListAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        medicationViewModel = ViewModelProvider(this).get(MedicationViewModel::class.java)

        loadDayOfWeek()

        medicationViewModel.allMeds.observe(this, Observer { meds ->
            // Update the cached copy of the words in the adapter.
            meds?.let { adapter.setMeds(it, selected) }
        })

    }

    private fun showImportDialog() {
        val dialogFragment = ImportMedicationDialog(this, selected.text.toString())

        dialogFragment.show(supportFragmentManager, "signature")
    }

    fun showEditDialog(medication: Medication) {

        val dialogFragment = EditMedicationDialog(this, medication)
        dialogFragment.show(supportFragmentManager, "signature")
    }

    fun showDeleteDialog(medication: Medication) {

        val builder = AlertDialog.Builder(this)
        builder.setMessage(getString(R.string.areYouSureDelete))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.delete)) { _, _ ->

                // Delete selected medication from database
                medicationViewModel.delete(medication)
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                // Dismiss the dialog
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //style id
        alert.show()



    }



    override fun onInsertDataReceived(newMedication: Medication) {

        medicationViewModel.insert(newMedication)
        setAlarm(newMedication)

        Toast.makeText(applicationContext,getString(R.string.medAdded), Toast.LENGTH_LONG).show()


    }
    override fun onUpdateDataReceived(newMedication: Medication) {

        medicationViewModel.update(newMedication)

        Toast.makeText(applicationContext,getString(R.string.medUpdated), Toast.LENGTH_LONG).show()

    }

    private fun setAlarm(medication: Medication){


        val reminderCalendar = Calendar.getInstance()
        reminderCalendar.time = medication.reminder

        var RECURRENCE_RULE = "FREQ=DAILY"

        if(medication.weekly) {

            RECURRENCE_RULE = "FREQ=WEEKLY;BYDAY="+medication.day

        }


        val EVENT_BEGIN_TIME_IN_MILLIS = reminderCalendar.timeInMillis
        reminderCalendar.add(Calendar.MINUTE, 30)
        val EVENT_END_TIME_IN_MILLIS = reminderCalendar.timeInMillis

        val insertCalendarIntent = Intent(Intent.ACTION_INSERT)
            .setData(CalendarContract.Events.CONTENT_URI)
            .putExtra(CalendarContract.Events.TITLE, "Reminder: Take "+medication.dosage+" "+medication.name) // Simple title
            .putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false)
            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, EVENT_BEGIN_TIME_IN_MILLIS) // Only date part is considered when ALL_DAY is true; Same as DTSTART
            .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, EVENT_END_TIME_IN_MILLIS) // Only date part is considered when ALL_DAY is true
            .putExtra(CalendarContract.Events.DESCRIPTION, "Reminder added by MedBoy 3000 app.") // Description
            .putExtra(CalendarContract.Events.RRULE, RECURRENCE_RULE) // Recurrence rule
            .putExtra(CalendarContract.Events.ACCESS_LEVEL, CalendarContract.Events.ACCESS_PRIVATE)
            .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_FREE)
        startActivity(insertCalendarIntent)

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


        val day = when(selected.text.toString()){
            "MO" -> getString(R.string.monday)
            "TU" -> getString(R.string.tuesday)
            "WE" -> getString(R.string.wednesday)
            "TH" -> getString(R.string.thursday)
            "FR" -> getString(R.string.friday)
            "SA" -> getString(R.string.saturday)
            else -> getString(R.string.sunday)
        }
        dayLabel.text = "${getString(R.string.dayLabelText)} $day"

        selected.isSelected = true
        selected.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimarySelected))
        adapter.setMeds(selected)

    }


}