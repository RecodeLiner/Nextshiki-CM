package com.rcl.nextshiki.data.serializers

import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object LocalDateSerializer : KSerializer<LocalDate> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("LocalDate", PrimitiveKind.STRING)
    private val formatter = LocalDate.Formats.ISO

    override fun deserialize(decoder: Decoder): LocalDate {
        return try {
            LocalDate.Companion.parse(decoder.decodeString(), formatter)
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("Invalid LocalDate format", e)
        }
    }

    override fun serialize(encoder: Encoder, value: LocalDate) {
        encoder.encodeString(value.format(formatter))
    }
}