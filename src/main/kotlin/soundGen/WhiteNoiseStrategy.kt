package soundGen

import kotlin.random.Random

class WhiteNoiseStrategy() : SoundGenStrategy {
    override fun shape(phase: Double): Double = Random.nextDouble(-1.0, 1.0)   // ignores phase entirely
}
