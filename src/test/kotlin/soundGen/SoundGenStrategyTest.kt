package soundGen

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import kotlin.math.PI

class SoundGenStrategyTest {

    private class TestStrategy : SoundGenStrategy {
        override fun shape(phase: Double): Double {
            return phase
        }
    }

    @Test
    fun `generate creates correct number of samples`() {
        val strategy = TestStrategy()

        val samples = strategy.generate(
            frequency = 440.0,
            durationSeconds = 1.0,
            sampleRate = 100
        )

        assertEquals(100, samples.size)
    }

    @Test
    fun `generate calls shape with advancing phase`() {
        val strategy = TestStrategy()

        val samples = strategy.generate(
            frequency = 1.0,
            durationSeconds = 1.0,
            sampleRate = 4
        )

        // phase increment = 2π * 1 / 4 = π/2
        assertArrayEquals(
            doubleArrayOf(
                0.0,
                PI / 2,
                PI,
                3 * PI / 2
            ),
            samples,
            1e-9
        )
    }

    @Test
    fun `generate wraps phase after full cycle`() {
        val strategy = TestStrategy()

        val samples = strategy.generate(
            frequency = 1.0,
            durationSeconds = 1.5,
            sampleRate = 4
        )

        // phases:
        // 0
        // π/2
        // π
        // 3π/2
        // 2π wraps to 0
        // π/2
        assertArrayEquals(
            doubleArrayOf(
                0.0,
                PI / 2,
                PI,
                3 * PI / 2,
                0.0,
                PI / 2
            ),
            samples,
            1e-9
        )
    }

    @Test
    fun `generate returns empty array for zero duration`() {
        val strategy = TestStrategy()

        val samples = strategy.generate(
            frequency = 440.0,
            durationSeconds = 0.0,
            sampleRate = 44100
        )

        assertTrue(samples.isEmpty())
    }

    @Test
    fun `generate truncates fractional sample counts`() {
        val strategy = TestStrategy()

        val samples = strategy.generate(
            frequency = 440.0,
            durationSeconds = 0.005,
            sampleRate = 1000
        )

        // 1000 * 0.005 = 5 samples
        assertEquals(5, samples.size)
    }
}