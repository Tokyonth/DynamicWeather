package com.tokyonth.weather.data.db

import androidx.room.*

import com.tokyonth.weather.Constants
import com.tokyonth.weather.data.entity.LocationEntity
import com.tokyonth.weather.data.entity.SavedLocationEntity

@Dao
interface LocationDao {

    @Query("SELECT * FROM ${Constants.DB_LOCATION_INFO_TABLE} WHERE locationNameZH LIKE '%' || :mLocationName || '%' ")
    suspend fun dimQueryLocationByName(mLocationName: String): List<LocationEntity>

    @Query("SELECT * FROM ${Constants.DB_LOCATION_INFO_TABLE} WHERE locationNameZH =:mLocationName")
    suspend fun queryLocationByName(mLocationName: String): LocationEntity?

    @Query("SELECT * FROM ${Constants.DB_LOCATION_INFO_TABLE} WHERE locationId =:mLocationId")
    suspend fun queryLocationById(mLocationId: String): LocationEntity?

    @Query("SELECT * FROM ${Constants.DB_LOCATION_INFO_TABLE} WHERE adCode =:mAdCode")
    suspend fun queryLocationByAdCode(mAdCode: String): LocationEntity?

    @Query("SELECT * FROM ${Constants.DB_SAVED_LOCATION_TABLE}")
    suspend fun queryAllSavedLocation(): List<SavedLocationEntity>

    @Query("SELECT * FROM ${Constants.DB_SAVED_LOCATION_TABLE} WHERE locationId =:mLocationId")
    suspend fun querySavedLocationById(mLocationId: String): SavedLocationEntity?

    @Delete
    suspend fun deleteSavedLocation(savedLocationEntity: SavedLocationEntity): Int

    @Update
    suspend fun updateSavedLocation(savedLocationEntity: SavedLocationEntity): Int

    @Insert
    suspend fun insertLocation(vararg locations: LocationEntity): List<Long>

    @Insert
    suspend fun insertSavedLocation(vararg locations: SavedLocationEntity): List<Long>

}
