package com.rcl.nextshiki.base.profile.mainprofile

import androidx.compose.runtime.Stable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.doOnResume
import com.rcl.nextshiki.base.RootComponent
import com.rcl.nextshiki.base.RootComponent.TopLevelConfiguration.ProfileScreenConfiguration.ProfileHistoryScreen
import com.rcl.nextshiki.base.RootComponent.TopLevelConfiguration.ProfileScreenConfiguration.SettingsProfileScreen
import com.rcl.nextshiki.base.RootComponent.TopLevelConfiguration.SearchScreenConfiguration.SearchedElementScreen
import com.rcl.nextshiki.base.search.mainsearchscreen.SearchType
import com.rcl.nextshiki.di.ktor.KtorRepository
import com.rcl.nextshiki.di.settings.SettingsRepo
import com.rcl.nextshiki.locale.CustomLocale.getCurrentLocale
import com.rcl.nextshiki.models.currentuser.CurrUserModel
import com.rcl.nextshiki.models.searchobject.users.UserObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@Stable
class MainProfileComponent(
    private val navigator: StackNavigation<RootComponent.TopLevelConfiguration>,
    private val context: ComponentContext,
) : ComponentContext by context, KoinComponent {
    val ktorRepository: KtorRepository by inject()
    val settings: SettingsRepo by inject()
    val isAuth = MutableValue(false)
    val id = MutableValue(0)
    private val coroutine = CoroutineScope(Dispatchers.Main)
    private val baseAuthedObject = MutableValue(CurrUserModel())
    val mainAuthedObject = MutableValue(UserObject())

    init {
        lifecycle.doOnResume {
            if (!isAuth.value) {
                isAuth.update {
                    settings.getValue("id")?.toIntOrNull() != null && !settings.getValue("id")
                        .isNullOrEmpty()
                }
                if (isAuth.value) {
                    id.value = settings.getValue("id")?.toInt()!!
                    coroutine.launch {
                        val currUser = ktorRepository.getCurrentUser()
                        if (currUser == null) {
                            logout()
                        } else {
                            getUser()
                        }
                    }
                }
            }
        }
    }

    private suspend fun getUser() {
        val currUser = ktorRepository.getCurrentUser()
        if (currUser != null) {
            settings.addValue(key = "id", value = currUser.id.toString())
            baseAuthedObject.update { currUser }
            val friendList = ktorRepository.getFriendList(id.value, locale = getCurrentLocale())
            val mainObj =
                ktorRepository.getUserById(
                    id = baseAuthedObject.value.id.toString(),
                    isNickname = false,
                    locale = getCurrentLocale()
                )
                    .copy(friendsList = friendList)
            mainAuthedObject.update {
                mainObj
            }
        }
    }

    fun updateAuthState(state: Boolean) {
        if (state) {
            coroutine.launch {
                val currUser = ktorRepository.getCurrentUser()
                if (currUser != null) {
                    getUser()
                    isAuth.update { state }
                }
            }
        } else {
            isAuth.update { state }
        }
    }

    fun navigateToContent(id: String, contentType: SearchType) {
        navigator.bringToFront(
            SearchedElementScreen(
                id = id,
                contentType = contentType
            )
        )
    }

    fun navigateToSettings() {
        navigator.bringToFront(SettingsProfileScreen)
    }

    fun navigateToHistory() {
        navigator.bringToFront(ProfileHistoryScreen)
    }

    fun addToFriends(isFriends: Boolean) {
        coroutine.launch {
            ktorRepository.friends(isAdd = isFriends, id = id.value)
            mainAuthedObject.value = mainAuthedObject.value.copy(inFriends = isFriends)
        }
    }

    fun ignore(isIgnore: Boolean) {
        coroutine.launch {
            ktorRepository.ignore(isIgnore = isIgnore, id = id.value)
            mainAuthedObject.value = mainAuthedObject.value.copy(isIgnored = isIgnore)
        }
    }

    fun logout() {
        isAuth.update { false }
        mainAuthedObject.update { UserObject() }
        settings.removeValue("id")
        settings.removeValue("refCode")
        settings.removeValue("authCode")
        settings.removeValue("langCode")
        coroutine.launch {
            ktorRepository.signOut()
        }
    }
}
