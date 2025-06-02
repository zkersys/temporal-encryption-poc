package temporalencryption.dataconverterdemo.annotationbased

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.BeanProperty
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.deser.ContextualDeserializer
import java.nio.charset.StandardCharsets
import java.util.Base64

class TemporalEncryptedDeserializer<T> : JsonDeserializer<T>(), ContextualDeserializer {

    private var targetType: JavaType? = null

    override fun createContextual(ctxt: DeserializationContext, property: BeanProperty?): JsonDeserializer<*> {
        val type = property?.type ?: ctxt.contextualType
        val deser = TemporalEncryptedDeserializer<T>()
        deser.targetType = type
        return deser
    }

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): T {
        val encryptedValue = p.valueAsString
        val decryptedJson = String(Base64.getDecoder().decode(encryptedValue), StandardCharsets.UTF_8)

        val objectMapper = p.codec as ObjectMapper
        return objectMapper.readValue(decryptedJson, targetType ?: ctxt.constructType(Any::class.java))
    }
}