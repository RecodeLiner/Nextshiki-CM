package com.rcl.nextshiki.models.searchobject.users

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

object ActivitySerializable : KSerializer<ActivityList> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("ActivityList") {
        element<JsonElement>("activities")
    }

    override fun deserialize(decoder: Decoder): ActivityList {
        val jsonElement = (decoder as JsonDecoder).decodeJsonElement()

        return if (jsonElement is JsonObject) {
            ActivityList()
        } else {
            val jsonArray = jsonElement.jsonArray
            val list = jsonArray.map { element ->
                val jsonObject = element.jsonObject
                Activity(
                    name = jsonObject["name"]!!.jsonArray.map { it.jsonPrimitive.int },
                    value = jsonObject["value"]!!.jsonPrimitive.int
                )
            }
            ActivityList(list)
        }
    }

    override fun serialize(encoder: Encoder, value: ActivityList) {
        val jsonEncoder = encoder as JsonEncoder
        val activities = value.list
        if (activities != null) {
            if (activities.isEmpty()) {
                jsonEncoder.encodeJsonElement(JsonObject(emptyMap()))
            } else {
                jsonEncoder.encodeSerializableValue(
                    ListSerializer(Activity.serializer()),
                    activities
                )
            }
        }
    }
}
