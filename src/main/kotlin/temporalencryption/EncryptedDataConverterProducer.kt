package temporalencryption

import io.temporal.common.converter.CodecDataConverter
import io.temporal.common.converter.DataConverter
import io.temporal.common.converter.DefaultDataConverter
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Produces
import temporalencryption.codecdemo.EncryptionPayloadCodec
import temporalencryption.codecdemo.TemporalPayloadEncryptionCodecConfig
import temporalencryption.dataconverterdemo.JsonEncryptedPayloadConverter
import temporalencryption.dataconverterdemo.TemporalPayloadEncryptionDataConverterConfig

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

        if (dataConverterConfig.enabled) {
            defaultDataConverter =
                defaultDataConverter.withPayloadConverterOverrides(jsonEncryptedPayloadConverter)
        }

        if (codecConfig.enabled) {
            return CodecDataConverter(defaultDataConverter, listOf(encryptionPayloadCodec))
        }

        return defaultDataConverter
    }
}
