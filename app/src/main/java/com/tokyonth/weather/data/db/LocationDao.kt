package com.tokyonth.weather.data.db

import androidx.room.*
import com.tokyonth.weather.Constants
import com.tokyonth.weather.data.entity.LocationEntity

import com.tokyonth.weather.data.entity.SavedCityEntity

@Dao
interface LocationDao {

    @Query("SELECT * FROM ${Constants.DB_CITY_INFO_TABLE} WHERE locationNameZH LIKE '%' || :mCityName || '%' ")
    suspend fun dimQueryCityByName(mCityName: String): List<LocationEntity>

    @Query("SELECT * FROM ${Constants.DB_CITY_INFO_TABLE} WHERE locationNameZH =:mCityName")
    suspend fun queryCityByName(mCityName: String): LocationEntity?

    @Query("SELECT * FROM ${Constants.DB_CITY_INFO_TABLE} WHERE adCode =:mCityId")
    suspend fun queryCityById(mCityId: String): LocationEntity?

    @Query("SELECT * FROM ${Constants.DB_SAVED_CITY_TABLE}")
    suspend fun queryAllSavedCity(): List<SavedCityEntity>

    @Delete
    suspend fun deleteSavedCity(savedCityEntity: SavedCityEntity): Int

    @Update
    suspend fun updateSavedCity(savedCityEntity: SavedCityEntity): Int

    @Insert
    suspend fun insertCity(vararg city: LocationEntity): List<Long>

    @Insert
    suspend fun insertSavedCity(vararg city: SavedCityEntity): List<Long>

}
