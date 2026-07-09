package channel

class VolumeDecorator(
    channel: Channel,
    private val level: Double
) : ChannelDecorator(channel) {

    override fun generate(frequency: Double, durationSeconds: Double, sampleRate: Int): DoubleArray {
        return channel.generate(frequency, durationSeconds, sampleRate).map {
            (it * level).coerceIn(-1.0, 1.0)
        }.toDoubleArray()
    }
}

