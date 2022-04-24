package com.tokyonth.weather.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tokyonth.weather.Constants

@Entity(tableName = Constants.DB_CITY_INFO_TABLE)
data class LocationEntity(
    @PrimaryKey(autoGenerate = true)
    var autoId: Long,
    var locationId: String,
    var locationNameEN: String,
    var locationNameZH: String,
    var countryCode: String,
    var countryNameEN: String,
    var countryNameZH: String,
    var adm1NameEN: String,
    var adm1NameZH: String,
    var adm2NameEN: String,
    var adm2NameZN: String,
    var timeZone: String,
    var latitude: String,
    var longitude: String,
    var adCode: String
)
