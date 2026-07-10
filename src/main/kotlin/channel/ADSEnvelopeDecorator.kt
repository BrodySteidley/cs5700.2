package channel

import exceptions.EffectParseException

class ADSEnvelopeDecorator(
    channel: Channel,
    private val attackEnd: Double,
    private val decayEnd: Double,
    private val sustain: Double,
) : ChannelDecorator(channel) {

    init {
        if(attackEnd < 0.0) { throw EffectParseException("Invalid ADS envelope attack") }
        if(decayEnd <= attackEnd) { throw EffectParseException("Invalid ADS envelope decay") }
        if (sustain !in 0.0..1.0) { throw EffectParseException("Invalid ADS envelope sustain") }
    }

    override fun generate(frequency: Double, durationSeconds: Double, sampleRate: Int): DoubleArray {
        val samples = channel.generate(frequency, durationSeconds, sampleRate)


        return DoubleArray(samples.size) { index ->
            val time = index.toDouble() / sampleRate

            val envelope = when {
                time < attackEnd -> {
                    if (attackEnd == 0.0) 1.0 else time / attackEnd
                }

                time < decayEnd -> {
                    1.0 - ((time - attackEnd) / (decayEnd - attackEnd)) * (1.0 - sustain)
                }

                else -> sustain
            }

            samples[index] * envelope
        }
    }
}
