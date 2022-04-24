package com.tokyonth.weather.dynamic.data

import com.tokyonth.weather.dynamic.BaseDrawer
import com.tokyonth.weather.dynamic.drawer.*

object SkyBackground {

    val DEFAULT = intArrayOf(-0x60431c, -0x352004)
    val CLEAR_D = intArrayOf(-0xc2663e, -0xb0613b)
    val CLEAR_N = intArrayOf(-0xf4f0db, -0xdad4be)
    val OVERCAST_D = intArrayOf(-0xccbda1, -0x9e8978) //0xff748798, 0xff617688
    val OVERCAST_N = intArrayOf(-0xd9d6df, -0xdcd6c2) //0xff1b2229, 0xff262921
    val RAIN_D = intArrayOf(-0x1000000, -0xb28b72)
    val RAIN_N = intArrayOf(-0xf2f2eb, -0xdddbd1)
    val FOG_D = intArrayOf(-0x977a69, -0xbbaea5)
    val FOG_N = intArrayOf(-0xd0c3b9, -0xdbcec5)
    val SNOW_D = intArrayOf(-0xb07f60, -0xb28b72) //临时用RAIN_D凑数的
    val SNOW_N = intArrayOf(-0xe1dfd7, -0xded9d0)
    val CLOUDY_D = intArrayOf(-0x6c5b52, -0xb28b72) //临时用RAIN_D凑数的
    val CLOUDY_N = intArrayOf(-0xf8ead9, -0xdad4be) // 0xff193353 };//{ 0xff0e1623, 0xff222830 }
    val HAZE_D = intArrayOf(-0x9e9190, -0xb8b9bc) // 0xff999b95, 0xff818e90
    val HAZE_N = intArrayOf(-0xc8c9cc, -0xdadde3)
    val SAND_D = intArrayOf(-0x4a5f9a, -0x2a3f7a) //0xffa59056
    val SAND_N = intArrayOf(-0xced7e0, -0xaeb7c0)

    fun makeDrawerByType(type: DynamicDrawerType?): BaseDrawer<*> {
        return when (type) {
            DynamicDrawerType.CLEAR_D -> SunnyDrawer(true)
            DynamicDrawerType.CLEAR_N -> StarDrawer(false)
            DynamicDrawerType.RAIN_D -> RainDrawer(false)
            DynamicDrawerType.RAIN_N -> RainDrawer(true)
            DynamicDrawerType.SNOW_D -> SnowDrawer(false)
            DynamicDrawerType.SNOW_N -> SnowDrawer(true)
            DynamicDrawerType.CLOUDY_D -> CloudyDrawer(false)
            DynamicDrawerType.CLOUDY_N -> CloudyDrawer(true)
            DynamicDrawerType.OVERCAST_D -> OvercastDrawer(false)
            DynamicDrawerType.OVERCAST_N -> OvercastDrawer(true)
            DynamicDrawerType.FOG_D -> FogDrawer(false)
            DynamicDrawerType.FOG_N -> FogDrawer(true)
            DynamicDrawerType.HAZE_D -> HazeDrawer(false)
            DynamicDrawerType.HAZE_N -> HazeDrawer(true)
            DynamicDrawerType.SAND_D -> SandDrawer(false)
            DynamicDrawerType.SAND_N -> SandDrawer(true)
            DynamicDrawerType.WIND_D -> WindDrawer(false)
            DynamicDrawerType.WIND_N -> WindDrawer(true)
            DynamicDrawerType.RAIN_SNOW_D -> RainAndSnowDrawer(false)
            DynamicDrawerType.RAIN_SNOW_N -> RainAndSnowDrawer(true)
            DynamicDrawerType.DEFAULT -> CloudyDrawer(false)
            else -> CloudyDrawer(false)
        }
    }

}
