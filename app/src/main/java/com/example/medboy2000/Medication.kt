package com.example.medboy2000

import java.util.*
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "meds_table")
data class Medication(@PrimaryKey(autoGenerate = true) val id: Int,
                      var name: String, var dosage: String, var weekly: Boolean, var reminder: Date?, var day:String)