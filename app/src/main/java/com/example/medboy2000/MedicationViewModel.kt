package com.example.medboy2000

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MedicationViewModel (application: Application) : AndroidViewModel(application) {

    private val repository: MedicationRepository
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allMeds: LiveData<List<Medication>>

    init {
        val medsDAO =
            MedicationRoomDatabase.getDatabase(application, viewModelScope).medicationDao()
        repository = MedicationRepository(medsDAO)
        allMeds = repository.allMeds
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(medication: Medication) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(medication)
    }

    /**
     * Launching a new coroutine to update the data in a non-blocking way
     */
    fun update(medication: Medication) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(medication)
    }


    /**
     * Launching a new coroutine to delete the data in a non-blocking way
     */
    fun delete(medication: Medication) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(medication)
    }
}