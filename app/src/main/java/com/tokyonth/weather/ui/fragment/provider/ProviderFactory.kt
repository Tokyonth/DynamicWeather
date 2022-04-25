@file:Suppress("UNCHECKED_CAST")

package com.tokyonth.weather.ui.fragment.provider

object ProviderFactory {

    private val providerCache = HashMap<String, Any>()

    fun <T : BaseWeatherProvider<*, *>> create(clazz: Class<T>): T {
        val key = clazz.simpleName
        return if (providerCache.contains(key)) {
            providerCache[key] as T
        } else {
            clazz.newInstance().apply {
                providerCache[key] = this
            }
        }
    }

}
