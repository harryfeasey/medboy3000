package com.example.medboy2000

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.importmedication.*
import kotlin.coroutines.coroutineContext
import android.app.Activity



class MedicationListAdapter internal constructor(
    context: Context
) : RecyclerView.Adapter<MedicationListAdapter.MedicationViewHolder>() {


    private val mContext: Context = context
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var meds = emptyList<Medication>() // Cached copy of words
    private var allMeds = emptyList<Medication>()
    private var medicationViewModel = ViewModelProviders.of(context as FragmentActivity).get(MedicationViewModel::class.java);

    inner class MedicationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val medicationNameView: TextView = itemView.findViewById(R.id.nameView)
        val medicationDosageView: TextView = itemView.findViewById(R.id.dosageView)
        val medicationReminderView: TextView = itemView.findViewById(R.id.reminderView)
        val medicationDeleteButton: Button = itemView.findViewById(R.id.deleteButton)
        val medicationEditButton: Button = itemView.findViewById(R.id.editButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicationViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return MedicationViewHolder(itemView)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: MedicationViewHolder, position: Int) {

        val current = meds[position]
        holder.medicationNameView.text = current.name
        holder.medicationDosageView.text = "${mContext.getString(R.string.take)} ${current.dosage}"
        holder.medicationReminderView.text = SimpleDateFormat("hh:mm aa").format(current.reminder?.time)
        holder.medicationDeleteButton.setOnClickListener {
            if (mContext is MainActivity) {
                mContext.showDeleteDialog(current)
            }
        }
        holder.medicationEditButton.setOnClickListener{
                if (mContext is MainActivity) {
                    mContext.showEditDialog(current)
                }
            }

        }

    internal fun setMeds(meds: List<Medication>, selected:Button) {

        this.allMeds = meds
        var newDaysMeds:MutableList<Medication> = mutableListOf()

        for(medication:Medication in meds){
            if ((medication.weekly && medication.day == selected.text) || (!medication.weekly)){
                newDaysMeds.add(medication)

            }
        }

        this.meds = newDaysMeds
        notifyDataSetChanged()
    }


    internal fun setMeds(selected:Button) {

        var newDaysMeds:MutableList<Medication> = mutableListOf()

        for(medication:Medication in allMeds){
            if ((medication.weekly && medication.day == selected.text) || (!medication.weekly)){
                newDaysMeds.add(medication)

            }
        }

        this.meds = newDaysMeds
        notifyDataSetChanged()
    }

    override fun getItemCount() = meds.size
}