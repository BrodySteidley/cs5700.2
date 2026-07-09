package soundGen

import kotlin.math.PI

class SawWaveStrategy() : SoundGenStrategy {
    override fun shape(phase: Double): Double = phase / PI - 1.0
}

