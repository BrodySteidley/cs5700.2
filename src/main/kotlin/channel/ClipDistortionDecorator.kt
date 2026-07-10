package channel

import exceptions.EffectParseException

class ClipDistortionDecorator(
    channel: Channel,
    private val threshold: Double
) : ChannelDecorator(channel) {

    init {
        if (threshold <= 0) { throw EffectParseException("Clip threshold must be positive") }
    }

    override fun generate(frequency: Double, durationSeconds: Double, sampleRate: Int): DoubleArray {
        return channel.generate(frequency, durationSeconds, sampleRate)
            .map { sample ->
                sample.coerceIn(-threshold, threshold)
            }
            .toDoubleArray()
    }
}
