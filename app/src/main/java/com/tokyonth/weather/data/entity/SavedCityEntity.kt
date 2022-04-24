package com.tokyonth.weather.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "savedCity")
class SavedCityEntity(
    @PrimaryKey(autoGenerate = true)
    var autoId: Long,
    val locationId: String,
    var locationName: String,
    var weather: String?,
    var temp: String?,
    var img: String?,
    var isInTime: Int?
)
