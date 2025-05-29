package temporalencryption.codecdemo

import com.google.protobuf.ByteString
import io.temporal.api.common.v1.Payload
import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType.APPLICATION_JSON
import java.util.Base64

@ApplicationScoped
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Path("/codec")
class CodecServer(
    private val encryptionPayloadCodec: EncryptionPayloadCodec
) {

    @POST
    @Path("/encode")
    fun encode(request: CodecRequest): CodecResponse {
        val decodedPayloads = request.payloads.map { it.toTemporalPayload() }.toMutableList()
        val encoded = encryptionPayloadCodec.encode(decodedPayloads)
        return CodecResponse(encoded.map { it.toCodecPayload() })
    }

    @POST
    @Path("/decode")
    fun decode(request: CodecRequest): CodecResponse {
        val encodedPayloads = request.payloads.map { it.toTemporalPayload() }.toMutableList()
        val decoded = encryptionPayloadCodec.decode(encodedPayloads)
        return CodecResponse(decoded.map { it.toCodecPayload() })
    }
}


data class CodecRequest(
    val payloads: List<CodecPayload>
)

data class CodecPayload(
    val metadata: Map<String, String>,
    val data: String
)

data class CodecResponse(
    val payloads: List<CodecPayload>
)

fun CodecPayload.toTemporalPayload(): Payload =
    Payload.newBuilder()
        .putAllMetadata(metadata.mapValues { ByteString.copyFrom(Base64.getDecoder().decode(it.value)) })
        .setData(ByteString.copyFrom(Base64.getDecoder().decode(data)))
        .build()

fun Payload.toCodecPayload(): CodecPayload =
    CodecPayload(
        metadata = metadata.mapValues { Base64.getEncoder().encodeToString(it.value.toByteArray()) },
        data = Base64.getEncoder().encodeToString(data.toByteArray())
    )
