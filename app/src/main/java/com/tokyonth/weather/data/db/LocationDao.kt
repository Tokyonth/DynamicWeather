package com.tokyonth.weather.data.db

import androidx.room.*

import com.tokyonth.weather.Constants
import com.tokyonth.weather.data.entity.LocationEntity
import com.tokyonth.weather.data.entity.SavedLocationEntity

@Dao
interface LocationDao {

    @Query("SELECT * FROM ${Constants.DB_LOCATION_INFO_TABLE} WHERE locationNameZH LIKE '%' || :mCityName || '%' ")
    suspend fun dimQueryLocationByName(mCityName: String): List<LocationEntity>

    @Query("SELECT * FROM ${Constants.DB_LOCATION_INFO_TABLE} WHERE locationNameZH =:mCityName")
    suspend fun queryLocationByName(mCityName: String): LocationEntity?

    @Query("SELECT * FROM ${Constants.DB_LOCATION_INFO_TABLE} WHERE adCode =:mCityId")
    suspend fun queryLocationById(mCityId: String): LocationEntity?

    @Query("SELECT * FROM ${Constants.DB_SAVED_LOCATION_TABLE}")
    suspend fun queryAllSavedLocation(): List<SavedLocationEntity>

    @Delete
    suspend fun deleteSavedLocation(savedLocationEntity: SavedLocationEntity): Int

    @Update
    suspend fun updateSavedLocation(savedLocationEntity: SavedLocationEntity): Int

    @Insert
    suspend fun insertLocation(vararg city: LocationEntity): List<Long>

    @Insert
    suspend fun insertSavedLocation(vararg location: SavedLocationEntity): List<Long>

}
