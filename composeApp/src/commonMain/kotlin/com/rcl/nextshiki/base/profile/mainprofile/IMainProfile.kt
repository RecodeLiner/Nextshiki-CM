package com.rcl.nextshiki.base.profile.mainprofile

import com.rcl.nextshiki.models.currentuser.CurrUserModel

interface IMainProfile {
    val isAuth: Boolean
    val user: CurrUserModel
    val name: String
}