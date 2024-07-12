package com.rcl.nextshiki.di.settings

interface ISettingsRepo {
    fun addValue(key: String, value: String)
    fun removeValue(key: String)
    fun getValue(key: String): String?
    fun updateValue(key: String, value: String)
}
