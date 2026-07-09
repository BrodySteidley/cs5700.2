package soundGen

import kotlin.math.sin

class SinWaveStrategy() : SoundGenStrategy {
    override fun shape(phase: Double): Double = sin(phase)
}
