package com.example.medboy2000

import android.app.TimePickerDialog
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.importmedication.*
import java.io.*
import java.util.*

class ImportMedicationDialog (private val callbackListener: CallbackListener) : DialogFragment() {

    private val reminderCalendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = true
        retainInstance = true
        return inflater.inflate(R.layout.importmedication, container, false)
    }

    override fun getTheme(): Int {
        return R.style.DialogTheme
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)


        saveButton.setOnClickListener {

            val name = nameTxt.text.toString()
            val dosage = dosageTxt.text.toString()
            val weekly = weeklyButton.isChecked

            val newMedication = Medication(name, dosage, weekly, reminderCalendar.time)

            //send back data to parent fragment using callback
            callbackListener.onDataReceived(newMedication)


            // Now dismiss the fragment
            dismiss()
        }

        createTimePicker()

        closeButton.setOnClickListener{
            dismiss()
        }



    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun createTimePicker(){

        val hour = reminderCalendar.get(Calendar.HOUR) + 1
        val min = reminderCalendar.get(Calendar.MINUTE)

        reminderCalendar.set(Calendar.HOUR, hour)
        reminderCalendar.set(Calendar.MINUTE, min)

        newReminderTime.text = SimpleDateFormat("hh:mm aa").format(reminderCalendar.time)


        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            reminderCalendar.set(Calendar.HOUR, hour)
            reminderCalendar.set(Calendar.MINUTE, minute)

            newReminderTime.text = SimpleDateFormat("hh:mm aa").format(reminderCalendar.time)
        }

        setReminderButton.setOnClickListener {
            TimePickerDialog(
                context, timeSetListener, reminderCalendar.get(Calendar.HOUR),
                reminderCalendar.get(Calendar.MINUTE), false).show()
        }

    }



}