package com.example.medboy2000

import androidx.lifecycle.LiveData

class MedicationRepository(private val medicationDAO: MedicationDAO) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    var allMeds: LiveData<List<Medication>> = medicationDAO.getAllMeds()

    suspend fun insert(medication: Medication) {
        medicationDAO.insert(medication)
    }
    suspend fun update(medication: Medication) {
        medicationDAO.updateMedication(medication)
    }
    suspend fun delete(medication: Medication) {
        medicationDAO.deleteMedication(medication)
    }
}