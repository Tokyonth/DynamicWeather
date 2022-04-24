package com.tokyonth.weather.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tokyonth.weather.data.entity.LocationEntity

import com.tokyonth.weather.data.entity.SavedCityEntity

@Database(
    entities = [
        LocationEntity::class,
        SavedCityEntity::class
    ],
    exportSchema = false,
    version = 1
)
abstract class SQLDatabase : RoomDatabase() {

    abstract fun locationDao(): LocationDao

}
