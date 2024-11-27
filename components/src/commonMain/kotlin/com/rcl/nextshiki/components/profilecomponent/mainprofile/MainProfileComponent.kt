package com.rcl.nextshiki.components.profilecomponent.mainprofile

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.arkivanov.essenty.lifecycle.doOnResume
import com.rcl.nextshiki.components.RootComponent
import com.rcl.nextshiki.components.RootComponent.TopLevelConfiguration.ProfileScreenConfiguration.ProfileHistoryConfiguration
import com.rcl.nextshiki.components.RootComponent.TopLevelConfiguration.ProfileScreenConfiguration.SettingsProfileConfiguration
import com.rcl.nextshiki.components.RootComponent.TopLevelConfiguration.SearchScreenConfiguration.SearchedElementConfiguration
import com.rcl.nextshiki.di.KtorRepository
import com.rcl.nextshiki.di.RepositoryModule
import com.rcl.nextshiki.di.language.LanguageModule
import com.rcl.nextshiki.di.language.LanguageRepo
import com.rcl.nextshiki.di.settings.ISettingsRepo
import com.rcl.nextshiki.di.settings.SettingsModuleObject
import com.rcl.nextshiki.models.currentuser.CurrUserModel
import com.rcl.nextshiki.models.searchobject.SearchCardModel
import com.rcl.nextshiki.models.searchobject.SearchType
import com.rcl.nextshiki.models.searchobject.users.UserObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainProfileComponent(
    private val navigator: StackNavigation<RootComponent.TopLevelConfiguration>,
    private val context: ComponentContext,
) : ComponentContext by context {

    val vm = instanceKeeper.getOrCreate { MainProfileViewModel() }

    init {
        lifecycle.doOnResume {
            vm.onResume()
        }
    }

    class MainProfileViewModel : InstanceKeeper.Instance {
        private val coroutine = CoroutineScope(Dispatchers.Main)
        private val baseAuthedObject = MutableStateFlow(CurrUserModel())
        val language: LanguageRepo = LanguageModule.langRepo
        val settings: ISettingsRepo = SettingsModuleObject.settingsImpl
        val ktorRepository: KtorRepository = RepositoryModule.getKtorRepository()
        val isAuth = MutableStateFlow(false)
        val id = MutableStateFlow(0)
        val mainAuthedObject = MutableStateFlow(UserObject())

        private suspend fun getUser() {
            val currUser = ktorRepository.getCurrentUser()
            val locale = language.getCurrentCode()
            if (currUser != null) {
                settings.addValue(key = "id", value = currUser.id.toString())
                baseAuthedObject.update { currUser }
                val friendList = ktorRepository.getFriendList(id.value, locale = locale)
                val mainObj =
                    ktorRepository.getUserById(
                        id = baseAuthedObject.value.id.toString(),
                        isNickname = false,
                        locale = locale
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

        fun onResume() {
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

    fun navigateToContent(searchCardModel: SearchCardModel, contentType: SearchType) {
        navigator.bringToFront(
            SearchedElementConfiguration(
                cardModel = searchCardModel,
                contentType = contentType
            )
        )
    }

    fun navigateToSettings() {
        navigator.bringToFront(SettingsProfileConfiguration)
    }

    fun navigateToHistory() {
        navigator.bringToFront(ProfileHistoryConfiguration)
    }
}
