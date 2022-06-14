import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class PollsModel(val name: String, val desc: String, val answers: MutableMap<String, Int>) {
    val id: Int = desc.hashCode()

    companion object {
        const val path = "/polls"
    }
}
