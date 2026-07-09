package channel

import exceptions.EffectParseException

object ChannelDecoratorFactory {
    fun createChannelDecorator(channel: Channel, effectDescriptor: String): ChannelDecorator {
        val effect: List<String> = effectDescriptor.split("$");

        return when (effect[0]) {
            "vol" -> VolumeDecorator(channel, effect[1].toDouble())
            "ads" -> ADSEnvelopeDecorator(channel, effect[1].toDouble(), effect[2].toDouble(), effect[3].toDouble())
            "tanh" -> TanhDistortionDecorator(channel, effect[1].toDouble())
            "clip" -> ClipDistortionDecorator(channel, effect[1].toDouble())
            else -> throw EffectParseException()
        }
    }
}
