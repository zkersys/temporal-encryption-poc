package temporalencryption.dataconverterdemo.annotationbased

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import java.util.Base64

class TemporalEncryptedDeserializer : JsonDeserializer<String>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): String {
        val encrypted = p.text
        return String(Base64.getDecoder().decode(encrypted))
    }
}