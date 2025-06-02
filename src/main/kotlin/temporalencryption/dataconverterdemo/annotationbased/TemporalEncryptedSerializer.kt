package temporalencryption.dataconverterdemo.annotationbased

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import java.util.Base64

class TemporalEncryptedSerializer : JsonSerializer<Any>() {
    override fun serialize(value: Any?, gen: JsonGenerator, serializers: SerializerProvider) {
        if (value == null) {
            gen.writeNull()
            return
        }

        val objectMapper = gen.codec as ObjectMapper
        val rawJsonBytes = objectMapper.writeValueAsBytes(value)
        val encrypted = Base64.getEncoder().encodeToString(rawJsonBytes)

        gen.writeString(encrypted)
    }
}