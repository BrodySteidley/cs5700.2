package channel

import exceptions.EffectParseException

object ChannelDecoratorFactory {
    fun createChannelDecorator(channel: Channel, effectDescriptor: String): ChannelDecorator {
        val effect: List<String> = effectDescriptor.split("$");

        if (effect.size < 2)
            throw EffectParseException("no parameter")

        return when (effect[0]) {
            "vol" -> VolumeDecorator(channel, effect[1].toDouble())
            "ads" -> if (effect.size != 4) throw EffectParseException("invalid ADS parameter count") else ADSEnvelopeDecorator(channel, effect[1].toDouble(), effect[2].toDouble(), effect[3].toDouble())
            "tanh" -> TanhDistortionDecorator(channel, effect[1].toDouble())
            "clip" -> ClipDistortionDecorator(channel, effect[1].toDouble())
            else -> throw EffectParseException("Unknown effect name")
        }
    }
}
