package temporalencryption.dataconverterdemo.propertybased

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.protobuf.ByteString
import io.temporal.api.common.v1.Payload
import io.temporal.common.converter.EncodingKeys
import io.temporal.common.converter.PayloadConverter
import jakarta.enterprise.context.ApplicationScoped
import java.lang.reflect.Type
import java.nio.charset.StandardCharsets
import java.util.Base64
import java.util.Optional

@ApplicationScoped
class JsonEncryptedPayloadConverter(
    private val temporalPayloadEncryptionDataConverterConfig: TemporalPayloadEncryptionDataConverterConfig,
    private val objectMapper: ObjectMapper
) : PayloadConverter {

    // Using json/plain as to override default json converter
    // without having to rebuild the whole DefaultDataConverter
    override fun getEncodingType(): String = "json/plain"

    override fun toData(value: Any?): Optional<Payload> {
        requireNotNull(value)

        val jsonString: String = when (value) {
            is String, is Number, is Boolean -> {
                // Simple types: just serialize to JSON string
                objectMapper.writeValueAsString(value)
            }

            else -> {
                // Complex object: convert to map, encrypt sensitive fields, then serialize
                val map: MutableMap<String, Any?> = objectMapper.convertValue(
                    value,
                    object : TypeReference<MutableMap<String, Any?>>() {}
                )
                val encrypted = encryptSensitiveFields(map)
                objectMapper.writeValueAsString(encrypted)
            }
        }

        return Optional.of(
            Payload.newBuilder()
                // We could use metadata to store info such as encryption key version, id, etc
                .putMetadata(
                    EncodingKeys.METADATA_ENCODING_KEY,
                    ByteString.copyFrom(getEncodingType(), StandardCharsets.UTF_8)
                )
                .setData(ByteString.copyFrom(jsonString, StandardCharsets.UTF_8))
                .build()
        )
    }

    override fun <T : Any?> fromData(
        content: Payload?,
        valueType: Class<T>?,
        valueGenericType: Type?
    ): T {
        requireNotNull(content)
        requireNotNull(valueType)

        val jsonString = content.data.toString(StandardCharsets.UTF_8)
        val javaType = objectMapper.typeFactory.constructType(valueGenericType ?: valueType)

        // Handle simple types directly (String, Number, Boolean)
        if (valueType == String::class.java
            || Number::class.java.isAssignableFrom(valueType)
            || valueType == Boolean::class.java
        ) {
            return objectMapper.readValue(jsonString, javaType)
        }

        // For complex objects, decrypt fields if needed
        val map: MutableMap<String, Any?> = objectMapper.readValue(jsonString)
        val decrypted = decryptSensitiveFields(map)
        val decryptedJson = objectMapper.writeValueAsString(decrypted)
        return objectMapper.readValue(decryptedJson, javaType)
    }

    private fun encryptSensitiveFields(input: Any?): Any? {
        return when (input) {
            is Map<*, *> -> input.mapValues { (key, value) ->
                if (key is String && key in temporalPayloadEncryptionDataConverterConfig.excludeKeywords && value != null) {
                    val json = objectMapper.writeValueAsString(value)
                    encrypt(json)
                } else {
                    encryptSensitiveFields(value)
                }
            }

            is List<*> -> input.map { encryptSensitiveFields(it) }
            else -> input
        }
    }

    private fun decryptSensitiveFields(input: Any?): Any? {
        return when (input) {
            is Map<*, *> -> input.mapValues { (key, value) ->
                if (key is String && key in temporalPayloadEncryptionDataConverterConfig.excludeKeywords && value is String) {
                    val decryptedJson = decrypt(value)
                    objectMapper.readValue<Any>(decryptedJson)
                } else {
                    decryptSensitiveFields(value)
                }
            }

            is List<*> -> input.map { decryptSensitiveFields(it) }
            else -> input
        }
    }

    // Using Base64 for 'mock' encryption, we could use anything else, like AES
    private fun encrypt(json: String): String =
        Base64.getEncoder().encodeToString(json.toByteArray(StandardCharsets.UTF_8))

    private fun decrypt(base64: String): String =
        String(Base64.getDecoder().decode(base64), StandardCharsets.UTF_8)
}
