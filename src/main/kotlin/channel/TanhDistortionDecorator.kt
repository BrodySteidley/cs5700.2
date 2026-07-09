package channel

class TanhDistortionDecorator(
    channel: Channel,
    private val drive: Double
) : ChannelDecorator(channel) {

    override fun generate(frequency: Double, durationSeconds: Double, sampleRate: Int): DoubleArray {
        return channel.generate(frequency, durationSeconds, sampleRate)
            .map { sample ->
                kotlin.math.tanh(sample * drive)
            }
            .toDoubleArray()
    }
}