package com.rcl.nextshiki.screens.profile

import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.rcl.nextshiki.di.ktor.KtorRepository
import com.rcl.nextshiki.koin
import com.rcl.nextshiki.models.usermodel.Userdata
import com.rcl.nextshiki.settings
import kotlinx.coroutines.launch

class ProfileViewModel : ScreenModel {
    val isAuth = mutableStateOf(false)
    val profileObject = mutableStateOf<Userdata?>(null)
    init {
        val id = settings.getIntOrNull("id")
        if (id != null){
            isAuth.value = true
            getProfileObject(id = id)
        }
    }
    suspend fun logout(){
        koin.get<KtorRepository>().signOut()
        settings.remove("authCode")
        settings.remove("refCode")
        settings.remove("token")
        settings.remove("id")
        isAuth.value = false
        profileObject.value = null
    }
    fun getProfileObject(id: Int){
        coroutineScope.launch {
            profileObject.value = koin.get<KtorRepository>().getUser(id = id.toString(), isNickname = false)
        }
    }
    suspend fun addToFriend(){
        val friend = profileObject.value!!.inFriends!!
        koin.get<KtorRepository>().friends(
            !friend,
            profileObject.value!!.id!!
        )
        profileObject.value = profileObject.value!!.copy(inFriends = !friend)
    }
}