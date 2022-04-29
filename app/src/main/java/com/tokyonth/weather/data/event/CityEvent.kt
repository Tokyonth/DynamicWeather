package com.tokyonth.weather.data.event

import com.tokyonth.weather.data.entity.SavedLocationEntity

class CityChangeEvent(val position: Int, val savedLocationEntity: SavedLocationEntity?)

class CitySelectEvent(val position: Int)
