package channel

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ChannelFactoryTest {

    @Test
    fun `creates waveform channel`() {
        val channel = ChannelFactory.createChannel("sin")

        assertTrue(channel is WaveformChannel)
    }

    @Test
    fun `creates channel with one decorator`() {
        val channel = ChannelFactory.createChannel(
            "sin vol$0.5"
        )

        assertTrue(channel is VolumeDecorator)
    }

    @Test
    fun `generated samples are produced from factory channel`() {
        val channel = ChannelFactory.createChannel(
            "square vol$0.5"
        )

        val samples = channel.generate(
            frequency = 440.0,
            durationSeconds = 0.01,
            sampleRate = 100
        )

        assertTrue(samples.isNotEmpty())
    }
}