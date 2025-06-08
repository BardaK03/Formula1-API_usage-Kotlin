package com.f1pulse.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        DriverEntity::class,
        CircuitEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class F1Database : RoomDatabase() {
    abstract fun driverDao(): DriverDao
    abstract fun circuitDao(): CircuitDao

    companion object {
        const val DATABASE_NAME = "f1_database"
    }
}
