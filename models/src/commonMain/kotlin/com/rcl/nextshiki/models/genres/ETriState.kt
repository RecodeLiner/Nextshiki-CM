package com.rcl.nextshiki.models.genres

enum class ETriState {
    NONE, ON, OFF;

    fun next(): ETriState {
        return when (this) {
            NONE -> ON
            ON -> OFF
            OFF -> NONE
        }
    }
}