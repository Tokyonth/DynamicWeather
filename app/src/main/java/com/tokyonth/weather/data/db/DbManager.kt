package com.tokyonth.weather.data.db

import android.content.Context
import androidx.room.Room

import com.tokyonth.weather.App
import com.tokyonth.weather.Constants
import com.tokyonth.weather.data.entity.LocationEntity

import kotlinx.coroutines.runBlocking

class DbManager(context: Context) {

    companion object {

        val db: DbManager by lazy(mode = LazyThreadSafetyMode.PUBLICATION) {
            DbManager(App.context)
        }

    }

    private var db: SQLDatabase = Room.databaseBuilder(
        context,
        SQLDatabase::class.java,
        Constants.DB_SQL_NAME
    ).build()

    fun getLocationDao(): LocationDao {
        return db.locationDao()
    }

    fun dimQueryLocationByName(mCityName: String): List<LocationEntity> {
        return runBlocking {
            db.locationDao().dimQueryLocationByName(mCityName)
        }
    }

    fun queryLocationByName(mCityName: String): LocationEntity? {
        return runBlocking {
            db.locationDao().queryLocationByName(mCityName)
        }
    }

}
