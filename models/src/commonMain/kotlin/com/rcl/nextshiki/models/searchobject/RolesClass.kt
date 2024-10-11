package com.rcl.nextshiki.models.searchobject

import com.rcl.nextshiki.models.searchobject.characters.CharacterSearchModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RolesClass(
    @SerialName("roles") val roles: List<String> = emptyList(),
    @SerialName("roles_russian") val rolesRussian: List<String> = emptyList(),
    @SerialName("character") val character: CharacterSearchModel? = CharacterSearchModel(),
    @SerialName("person") val person: CharacterSearchModel? = CharacterSearchModel()
)
