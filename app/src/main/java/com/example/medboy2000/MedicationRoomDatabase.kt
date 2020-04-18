package com.example.medboy2000

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*

// Annotates class to be a Room Database with a table (entity) of the Word class

@Database(entities = arrayOf(Medication::class), version = 1, exportSchema = false)
@TypeConverters(TimestampConverter::class)
public abstract class MedicationRoomDatabase :RoomDatabase(){

    abstract fun medicationDao():MedicationDAO

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: MedicationRoomDatabase? = null

        fun getDatabase(context: Context,
                        scope: CoroutineScope
        ): MedicationRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MedicationRoomDatabase::class.java,
                    "meds_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }


    private class WordDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.medicationDao())
                }
            }
        }

        suspend fun populateDatabase(medicationDAO: MedicationDAO) {
            // Delete all content here.
            medicationDAO.deleteAll()

            // Add sample words.
            var medication1 = Medication(0,"Iron","2",true,Calendar.getInstance().time)
            medicationDAO.insert(medication1)
            var medication2 = Medication(0,"Steroid","5",true,Calendar.getInstance().time)
            medicationDAO.insert(medication2)
            var medication3 = Medication(0,"Multi-Vitamin","3",true,Calendar.getInstance().time)
            medicationDAO.insert(medication3)
        }
    }


}
