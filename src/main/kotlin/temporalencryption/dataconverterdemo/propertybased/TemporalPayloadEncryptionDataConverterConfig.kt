package temporalencryption.dataconverterdemo.propertybased

import io.smallrye.config.ConfigMapping
import io.smallrye.config.WithName

@ConfigMapping(prefix = "temporal-payload-encryption.data-converter-approach")
interface TemporalPayloadEncryptionDataConverterConfig {

    @get:WithName("enabled")
    val enabled: Boolean

    @get:WithName("exclude-keywords")
    val excludeKeywords: List<String>
}