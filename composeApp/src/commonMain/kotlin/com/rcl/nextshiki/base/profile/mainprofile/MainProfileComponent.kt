package com.rcl.nextshiki.base.profile.mainprofile

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.doOnResume
import com.rcl.nextshiki.base.profile.ProfileComponent.ProfileConfiguration
import com.rcl.nextshiki.di.ktor.KtorRepository
import com.rcl.nextshiki.di.settings.SettingsRepo
import com.rcl.nextshiki.models.currentuser.CurrUserModel
import com.rcl.nextshiki.models.searchobject.users.UserObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainProfileComponent(
    private val navigator: StackNavigation<ProfileConfiguration>,
    private val context: ComponentContext,
) : ComponentContext by context, KoinComponent {
    val ktorRepository: KtorRepository by inject()
    val settings: SettingsRepo by inject()
    val isAuth = MutableValue(false)
    val id = MutableValue(0)
    private val coroutine = CoroutineScope(Dispatchers.IO)
    private val baseAuthedObject = MutableValue(CurrUserModel())
    val mainAuthedObject = MutableValue(UserObject())

    init {
        lifecycle.doOnResume {
            if (!isAuth.value) {
                isAuth.update {
                    settings.getValue("id")?.toIntOrNull() != null && !settings.getValue("id").isNullOrEmpty()
                }
                if (isAuth.value) {
                    id.value = settings.getValue("id")?.toInt()!!
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
    }

    fun updateAuthState(state: Boolean) {
        if (state) {
            coroutine.launch {
                val currUser = ktorRepository.getCurrentUser()
                if (currUser != null) {
                    settings.addValue(key = "id", value = currUser.id.toString())
                    mainAuthedObject.value = ktorRepository.getUserById(id = currUser.id.toString(), isNickname = false)
                    isAuth.value = state
                }
            }
        } else {
            isAuth.value = state
        }
    }

    @OptIn(ExperimentalDecomposeApi::class)
    fun navigateToSettings() {
        navigator.pushNew(ProfileConfiguration.SettingsProfileScreen)
    }

    @OptIn(ExperimentalDecomposeApi::class)
    fun navigateToHistory() {
        navigator.pushNew(ProfileConfiguration.ProfileHistoryScreen)
    }

    fun navigateBack() {
        navigator.pop()
    }

    fun addToFriends(isFriends: Boolean) {
        coroutine.launch {
            ktorRepository.friends(isAdd = !isFriends, id = id.value)
            mainAuthedObject.value = mainAuthedObject.value.copy(inFriends = !isFriends)
        }
    }

    fun ignore(isIgnore: Boolean) {
        coroutine.launch {
            ktorRepository.ignore(isIgnore = !isIgnore, id = id.value)
            mainAuthedObject.value = mainAuthedObject.value.copy(isIgnored = !isIgnore)
        }
    }

    fun logout() {
        isAuth.value = false
        settings.removeValue("id")
        settings.removeValue("refCode")
        settings.removeValue("authCode")
        coroutine.launch {
            ktorRepository.signOut()
        }
    }
}