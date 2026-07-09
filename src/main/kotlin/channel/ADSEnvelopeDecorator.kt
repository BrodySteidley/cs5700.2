package channel

class ADSEnvelopeDecorator(
    channel: Channel,
    private val attackEnd: Double,
    private val decayEnd: Double,
    private val sustain: Double,
) : ChannelDecorator(channel) {

    override fun generate(frequency: Double, durationSeconds: Double, sampleRate: Int): DoubleArray {
        val samples = channel.generate(frequency, durationSeconds, sampleRate)

        return samples.mapIndexed { index, sample ->
            val time = index.toDouble() / sampleRate
            val envelope = when {
                time < attackEnd -> {
                    time / attackEnd
                }

                time < decayEnd -> {
                    1.0 - ((time - attackEnd) / (decayEnd - attackEnd)) * (1.0 - sustain)
                }

                else -> {
                    sustain
                }
            }
            sample * envelope
        }.toDoubleArray()
    }
}
