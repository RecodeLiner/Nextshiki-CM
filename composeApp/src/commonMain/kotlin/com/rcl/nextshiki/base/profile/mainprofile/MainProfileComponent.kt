package com.rcl.nextshiki.base.profile.mainprofile

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.doOnStart
import com.rcl.nextshiki.di.ktor.KtorRepository
import com.rcl.nextshiki.models.currentuser.CurrUserModel
import com.rcl.nextshiki.models.searchobject.users.UserObject
import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainProfileComponent(
    private val navToSettings: () -> Unit,
    private val navBack: () -> Unit,
    private val context: ComponentContext,
) : ComponentContext by context, KoinComponent {
    private val settings = Settings()
    val ktorRepository: KtorRepository by inject()
    val isAuth = MutableValue(false)
    val id = MutableValue(0)
    private val coroutine = CoroutineScope(Default)
    private val baseAuthedObject = MutableValue(CurrUserModel())
    val mainAuthedObject = MutableValue(UserObject())

    init {
        lifecycle.doOnStart {
            isAuth.update {
                settings.getIntOrNull("id") != null
            }
            if (isAuth.value) {
                id.value = settings.getIntOrNull("id")!!
                coroutine.launch {
                    val currUser = ktorRepository.getCurrentUser()
                    if (currUser == null) {
                        logout()
                    } else {
                        baseAuthedObject.value = currUser
                        mainAuthedObject.value =
                            ktorRepository.getUserById(id = baseAuthedObject.value.id.toString(), isNickname = false)
                    }
                }
            }
        }
    }

    fun updateAuthState(state: Boolean) {
        if (state) {
            coroutine.launch {
                val currUser = ktorRepository.getCurrentUser()
                Napier.i("currUser is $currUser")
                if (currUser != null) {
                    settings["id"] = currUser.id
                    mainAuthedObject.value = ktorRepository.getUserById(id = currUser.id.toString(), isNickname = false)
                    isAuth.value = state
                }
            }
        } else {
            isAuth.value = state
        }
    }

    fun navigateToSettings() {
        navToSettings()
    }

    fun navigateBack() {
        navBack()
    }

    fun logout() {
        isAuth.value = false
        settings.remove("id")
        settings.remove("refCode")
        settings.remove("authCode")
        coroutine.launch {
            ktorRepository.signOut()
        }
    }
}