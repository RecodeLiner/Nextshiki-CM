package com.rcl.nextshiki.models.searchobject.users

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.IntArraySerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*

object ActivitySerializable: KSerializer<ActivityList> {
    override val descriptor: SerialDescriptor = IntArraySerializer().descriptor

    override fun deserialize(decoder: Decoder): ActivityList {
        val jsonElement = (decoder as JsonDecoder).decodeJsonElement()

        return if (jsonElement is JsonObject) {
            ActivityList()
        } else {
            val jsonArray = jsonElement.jsonArray
            val list = jsonArray.map { it ->
                val jsonObject = it.jsonObject
                Activity(
                    name = jsonObject["name"]!!.jsonArray.map { it.jsonPrimitive.int },
                    value = jsonObject["value"]!!.jsonPrimitive.int
                )
            }
            ActivityList(list)
        }
    }

    override fun serialize(encoder: Encoder, value: ActivityList) {
        return encoder.encodeSerializableValue(ActivityList.serializer(), value)
    }
}