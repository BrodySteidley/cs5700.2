package channel

class ClipDistortionDecorator(
    channel: Channel,
    private val threshold: Double
) : ChannelDecorator(channel) {

    override fun generate(frequency: Double, durationSeconds: Double, sampleRate: Int): DoubleArray {
        return channel.generate(frequency, durationSeconds, sampleRate)
            .map { sample ->
                sample.coerceIn(-threshold, threshold)
            }
            .toDoubleArray()
    }
}
