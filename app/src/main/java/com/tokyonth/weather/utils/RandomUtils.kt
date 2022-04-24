package com.tokyonth.weather.utils

object RandomUtils {

    fun getRandom(min: Float, max: Float): Float {
        require(max >= min) {
            "max should bigger than min!!!!"
        }
        return (min + Math.random() * (max - min)).toFloat()
    }

    /**
     * 获取[min, max)内的随机数，越大的数概率越小
     * 参考http://blog.csdn.net/loomman/article/details/3861240
     *
     * @param min
     * @param max
     * @return
     */
    fun getDownRandFloat(min: Float, max: Float): Float {
        val begin = (min + max) * max / 2f
        val x = getRandom(min, begin)
        var sum = 0
        var i = 0
        while (i < max) {
            sum += (max - i).toInt()
            if (sum > x) {
                return i.toFloat()
            }
            i++
        }
        return min
    }

}
