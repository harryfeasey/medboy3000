package com.example.medboy2000

import android.app.TimePickerDialog
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.importmedication.*
import java.util.*

class ImportMedicationDialog (private val callbackListener: CallbackListener) : DialogFragment() {
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
            //send back data to PARENT fragment using callback
            callbackListener.onDataReceived(editText.text.toString())
            // Now dismiss the fragment
            dismiss()
        }

        closeButton.setOnClickListener{
            dismiss()
        }

        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR) + 1
        val min = calendar.get(Calendar.MINUTE)
        calendar.set(Calendar.HOUR, hour)
        calendar.set(Calendar.MINUTE, min)

        newReminderTime.text = SimpleDateFormat("hh:mm aa").format(calendar.time)


        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            calendar.set(Calendar.HOUR, hour)
            calendar.set(Calendar.MINUTE, minute)

            newReminderTime.text = SimpleDateFormat("hh:mm aa").format(calendar.time)
        }

        setReminderButton.setOnClickListener {
            TimePickerDialog(
                context, timeSetListener, calendar.get(Calendar.HOUR),
                calendar.get(Calendar.MINUTE), false).show()
        }


    }


}