package temporalencryption.codecdemo

import io.smallrye.config.ConfigMapping
import io.smallrye.config.WithName

@ConfigMapping(prefix = "temporal-payload-encryption.codec-approach")
interface TemporalPayloadEncryptionCodecConfig {
    @get:WithName("enabled")
    val enabled: Boolean
}