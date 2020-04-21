package com.example.medboy2000
interface CallbackListener {
    fun onUpdateDataReceived(newMedication: Medication)
    fun onInsertDataReceived(newMedication: Medication)
}