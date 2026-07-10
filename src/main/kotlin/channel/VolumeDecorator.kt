package channel

import exceptions.EffectParseException


class VolumeDecorator(
    channel: Channel,
    private val level: Double
) : ChannelDecorator(channel) {

    init {
        if (level !in 0.0..1.0) { throw EffectParseException("Volume level should be between 0.0 and 1.0") }
    }

    override fun generate(frequency: Double, durationSeconds: Double, sampleRate: Int): DoubleArray {
        return channel.generate(frequency, durationSeconds, sampleRate).map {
            (it * level).coerceIn(-1.0, 1.0)
        }.toDoubleArray()
    }
}

