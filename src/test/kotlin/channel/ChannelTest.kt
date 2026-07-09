package channel

import kotlin.math.tanh
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Test
import soundGen.SoundGenStrategy

class ChannelTest {

    private class FakeChannel(
        private val samples: DoubleArray
    ) : Channel() {
        override fun generate(
            frequency: Double,
            durationSeconds: Double,
            sampleRate: Int
        ): DoubleArray = samples
    }

    @Test
    fun `WaveformChannel delegates to SoundGenStrategy`() {
        val strategy = object : SoundGenStrategy {
            override fun shape(phase: Double): Double = 0.5
        }

        val channel = WaveformChannel(strategy)

        val result = channel.generate(
            frequency = 440.0,
            durationSeconds = 1.0,
            sampleRate = 4
        )

        // shape() always returns 0.5, so every generated sample should be 0.5
        assertArrayEquals(
            doubleArrayOf(0.5, 0.5, 0.5, 0.5),
            result,
            1e-9
        )
    }

    @Test
    fun `VolumeDecorator multiplies samples by level`() {
        val channel = FakeChannel(doubleArrayOf(0.2, -0.4, 0.5))

        val decorator = VolumeDecorator(channel, 2.0)

        val result = decorator.generate(440.0, 1.0, 44100)

        assertArrayEquals(
            doubleArrayOf(0.4, -0.8, 1.0),
            result,
            1e-9
        )
    }

    @Test
    fun `VolumeDecorator clips values to valid range`() {
        val channel = FakeChannel(doubleArrayOf(0.8, -0.8))

        val decorator = VolumeDecorator(channel, 2.0)

        val result = decorator.generate(440.0, 1.0, 44100)

        assertArrayEquals(
            doubleArrayOf(1.0, -1.0),
            result,
            1e-9
        )
    }

    @Test
    fun `ClipDistortionDecorator clips samples outside threshold`() {
        val channel = FakeChannel(
            doubleArrayOf(-1.0, -0.2, 0.3, 1.0)
        )

        val decorator = ClipDistortionDecorator(channel, 0.5)

        val result = decorator.generate(440.0, 1.0, 44100)

        assertArrayEquals(
            doubleArrayOf(-0.5, -0.2, 0.3, 0.5),
            result,
            1e-9
        )
    }

    @Test
    fun `TanhDistortionDecorator applies tanh distortion`() {
        val channel = FakeChannel(doubleArrayOf(-1.0, 0.0, 1.0))

        val decorator = TanhDistortionDecorator(channel, 2.0)

        val result = decorator.generate(440.0, 1.0, 44100)

        val expected = doubleArrayOf(
            tanh(-2.0),
            tanh(0.0),
            tanh(2.0)
        )

        assertArrayEquals(expected, result, 1e-9)
    }

    @Test
    fun `ADSEnvelopeDecorator applies attack decay and sustain envelope`() {
        // Sample rate = 1 Hz, so sample times are 0, 1, 2, 3, 4 seconds.
        val channel = FakeChannel(
            doubleArrayOf(1.0, 1.0, 1.0, 1.0, 1.0)
        )

        val decorator = ADSEnvelopeDecorator(
            channel = channel,
            attackEnd = 1.0,
            decayEnd = 3.0,
            sustain = 0.5
        )

        val result = decorator.generate(
            frequency = 440.0,
            durationSeconds = 5.0,
            sampleRate = 1
        )

        val expected = doubleArrayOf(
            0.0,
            1.0,
            0.75,
            0.5,
            0.5
        )

        assertArrayEquals(expected, result, 1e-9)
    }

    @Test
    fun `ADSEnvelopeDecorator preserves silence`() {
        val channel = FakeChannel(
            doubleArrayOf(0.0, 0.0, 0.0)
        )

        val decorator = ADSEnvelopeDecorator(
            channel,
            attackEnd = 0.5,
            decayEnd = 1.0,
            sustain = 0.6
        )

        val result = decorator.generate(440.0, 3.0, 1)

        assertArrayEquals(
            doubleArrayOf(0.0, 0.0, 0.0),
            result,
            1e-9
        )
    }
}