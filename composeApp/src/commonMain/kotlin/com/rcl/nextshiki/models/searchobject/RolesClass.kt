package com.rcl.nextshiki.models.searchobject

import androidx.compose.runtime.Stable
import com.rcl.nextshiki.models.searchobject.characters.CharacterSearchModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Stable
@Serializable
data class RolesClass(
    @SerialName("roles") val roles: ArrayList<String> = arrayListOf(),
    @SerialName("roles_russian") val rolesRussian: ArrayList<String> = arrayListOf(),
    @SerialName("character") val character: CharacterSearchModel? = CharacterSearchModel(),
    @SerialName("person") val person: CharacterSearchModel? = CharacterSearchModel()
)
