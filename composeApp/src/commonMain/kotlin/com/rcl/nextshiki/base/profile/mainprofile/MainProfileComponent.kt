package com.rcl.nextshiki.base.profile.mainprofile

import com.arkivanov.decompose.ComponentContext
import com.rcl.nextshiki.models.currentuser.CurrUserModel
import com.rcl.nextshiki.settings

class MainProfileComponent(
    val navToSettings: () -> Unit,
    context: ComponentContext,
) : ComponentContext by context, IMainProfile {
    override val isAuth: Boolean = settings.getIntOrNull("id") != null
    override val user: CurrUserModel = CurrUserModel()
    override val name: String = user.name.toString()

    fun navigateToSettings(){
        navToSettings()
    }
}