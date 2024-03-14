package com.rcl.nextshiki.base.profile.mainprofile

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.lifecycle.doOnStart
import com.rcl.nextshiki.di.ktor.KtorRepository
import com.rcl.nextshiki.models.currentuser.CurrUserModel
import com.rcl.nextshiki.models.searchobject.users.UserObject
import com.russhwolf.settings.Settings
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
            isAuth.value = settings.getIntOrNull("id") != null
            if (isAuth.value) {
                id.value = settings.getIntOrNull("id")!!
                coroutine.launch {
                    baseAuthedObject.value = ktorRepository.getCurrentUser()!!
                    mainAuthedObject.value = ktorRepository.getUserById(id = baseAuthedObject.value.id!!, isNickname = false)
                }
            }
        }
    }
    fun updateAuthState(state: Boolean) {
        if (state){
            coroutine.launch {
                if (ktorRepository.getCurrentUser()!=null){
                    isAuth.value = state
                }
            }
        }
        else {
            isAuth.value = state
        }
    }
    fun navigateToSettings(){
        navToSettings()
    }
    fun navigateBack(){
        navBack()
    }
}