package soundGen

import kotlin.math.PI

class SquareWaveStrategy() : SoundGenStrategy {
    override fun shape(phase: Double): Double = if (phase < PI) 1.0 else -1.0
}
