package com.tokyonth.weather.data.db

import android.content.Context
import androidx.room.Room

import com.tokyonth.weather.App
import com.tokyonth.weather.Constants
import com.tokyonth.weather.data.entity.LocationEntity
import kotlinx.coroutines.runBlocking

class DbManager(context: Context) {

    companion object {

        val db: DbManager by lazy {
            DbManager(App.context)
        }

    }

    private var db: SQLDatabase = Room.databaseBuilder(
        context,
        SQLDatabase::class.java,
        Constants.DB_SQL_NAME
    ).build()

    fun getCityDao(): LocationDao {
        return db.locationDao()
    }

    fun dimQueryCityByName(mCityName: String): List<LocationEntity> {
        return runBlocking {
            db.locationDao().dimQueryCityByName(mCityName)
        }
    }

    fun queryCityByName(mCityName: String): LocationEntity? {
        return runBlocking {
            db.locationDao().queryCityByName(mCityName)
        }
    }

    fun queryCityById(mCityId: String): LocationEntity? {
        return runBlocking {
            db.locationDao().queryCityById(mCityId)
        }
    }

}
