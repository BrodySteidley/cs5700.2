package soundGen

import exceptions.InvalidStrategyException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class SoundGenStrategyFactoryTest {

    @Test
    fun `creates sine wave strategy`() {
        val strategy = SoundGenStrategyFactory.createStrategy("sin")

        assertTrue(strategy is SinWaveStrategy)
    }

    @Test
    fun `creates saw wave strategy`() {
        val strategy = SoundGenStrategyFactory.createStrategy("saw")

        assertTrue(strategy is SawWaveStrategy)
    }

    @Test
    fun `creates square wave strategy`() {
        val strategy = SoundGenStrategyFactory.createStrategy("square")

        assertTrue(strategy is SquareWaveStrategy)
    }

    @Test
    fun `creates white noise strategy`() {
        val strategy = SoundGenStrategyFactory.createStrategy("whitenoise")

        assertTrue(strategy is WhiteNoiseStrategy)
    }

    @Test
    fun `throws exception for unknown strategy`() {
        assertThrows(InvalidStrategyException::class.java) {
            SoundGenStrategyFactory.createStrategy("triangle")
        }
    }
}