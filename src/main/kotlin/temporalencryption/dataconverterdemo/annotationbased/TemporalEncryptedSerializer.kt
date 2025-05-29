package temporalencryption.dataconverterdemo.annotationbased

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import java.util.Base64

class TemporalEncryptedSerializer : JsonSerializer<String>() {
    override fun serialize(value: String?, gen: JsonGenerator, serializers: SerializerProvider) {
        val encrypted = if (value != null) Base64.getEncoder().encodeToString(value.toByteArray()) else null
        gen.writeString(encrypted)
    }
}