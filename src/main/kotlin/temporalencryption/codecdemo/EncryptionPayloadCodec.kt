package temporalencryption.codecdemo

import com.google.protobuf.ByteString
import io.temporal.api.common.v1.Payload
import io.temporal.payload.codec.PayloadCodec
import jakarta.enterprise.context.ApplicationScoped
import java.nio.charset.StandardCharsets
import java.util.Base64

@ApplicationScoped
class EncryptionPayloadCodec : PayloadCodec {

    override fun encode(payloads: MutableList<Payload>): MutableList<Payload> {
        return payloads.map { payload ->
            val originalData = payload.data.toByteArray()
            val encodedData = Base64.getEncoder().encodeToString(originalData)
            Payload.newBuilder()
                .putAllMetadata(payload.metadataMap)
                .setData(ByteString.copyFrom(encodedData, StandardCharsets.UTF_8))
                .build()
        }.toMutableList()
    }

    override fun decode(payloads: MutableList<Payload>): MutableList<Payload> {
        return payloads.map { payload ->
            val encodedData = payload.data.toString(StandardCharsets.UTF_8)
            val decodedBytes = Base64.getDecoder().decode(encodedData)
            Payload.newBuilder()
                .putAllMetadata(payload.metadataMap)
                .setData(ByteString.copyFrom(decodedBytes))
                .build()
        }.toMutableList()
    }
}
