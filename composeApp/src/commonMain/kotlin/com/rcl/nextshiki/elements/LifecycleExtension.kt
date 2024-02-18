package com.rcl.nextshiki.elements

import com.arkivanov.essenty.lifecycle.Lifecycle

fun Lifecycle.setOutContent(link: String) {
    subscribe(
        object : Lifecycle.Callbacks {
            override fun onCreate() {
                super.onCreate()
                currLink.value = link
            }

            override fun onDestroy() {
                super.onDestroy()
                currLink.value = null
            }
        }
    )
}