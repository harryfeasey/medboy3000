package com.example.medboy2000

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MedicationDAO {

    @Query("SELECT * from meds_table ORDER BY datetime(reminder) ASC")
    fun getOrderedMeds(): LiveData<List<Medication>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(medication: Medication)

    @Update
    suspend fun updateMedication(medication: Medication)

    @Delete
    suspend fun deleteMedication(medication: Medication)

    @Query("DELETE FROM meds_table")
    suspend fun deleteAll()

}