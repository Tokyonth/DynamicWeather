package com.tokyonth.weather.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tokyonth.weather.Constants

@Entity(tableName = Constants.DB_SAVED_LOCATION_TABLE)
class SavedLocationEntity(
    @PrimaryKey(autoGenerate = true)
    var autoId: Long,
    val locationId: String,
    var locationName: String,
    var weather: String?,
    var temp: String?,
    var img: String?,
    var isInTime: Int?
)
