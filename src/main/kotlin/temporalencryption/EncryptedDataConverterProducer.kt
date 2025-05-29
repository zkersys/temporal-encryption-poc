package temporalencryption

import com.fasterxml.jackson.databind.ObjectMapper
import io.temporal.common.converter.CodecDataConverter
import io.temporal.common.converter.DataConverter
import io.temporal.common.converter.DefaultDataConverter
import io.temporal.common.converter.JacksonJsonPayloadConverter
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Produces
import temporalencryption.codecdemo.EncryptionPayloadCodec
import temporalencryption.codecdemo.TemporalPayloadEncryptionCodecConfig
import temporalencryption.dataconverterdemo.annotationbased.TemporalEncryptionModule
import temporalencryption.dataconverterdemo.propertybased.JsonEncryptedPayloadConverter
import temporalencryption.dataconverterdemo.propertybased.TemporalPayloadEncryptionDataConverterConfig

@ApplicationScoped
class EncryptedDataConverterProducer(
    private val codecConfig: TemporalPayloadEncryptionCodecConfig,
    private val dataConverterConfig: TemporalPayloadEncryptionDataConverterConfig,
    private val encryptionPayloadCodec: EncryptionPayloadCodec,
    private val jsonEncryptedPayloadConverter: JsonEncryptedPayloadConverter,
) {

    @Produces
    fun encryptedDataConverter(): DataConverter {
        var defaultDataConverter = DefaultDataConverter.newDefaultInstance()

//        Currently commented out as trying annotation based approach
//        if (dataConverterConfig.enabled) {
//            defaultDataConverter =
//                defaultDataConverter.withPayloadConverterOverrides(jsonEncryptedPayloadConverter)
//        }
        if (dataConverterConfig.enabled) {
            val objectMapper = ObjectMapper()
            objectMapper.registerModule(TemporalEncryptionModule())
            defaultDataConverter =
                defaultDataConverter.withPayloadConverterOverrides(JacksonJsonPayloadConverter(objectMapper))
        }

        if (codecConfig.enabled) {
            return CodecDataConverter(defaultDataConverter, listOf(encryptionPayloadCodec))
        }

        return defaultDataConverter
    }
}
