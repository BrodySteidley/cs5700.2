package soundGen

import exceptions.InvalidSoundGenException

object SoundGenStrategyFactory {
    fun createStrategy(waveName: String): SoundGenStrategy {
        return when (waveName) {
            "sin" -> SinWaveStrategy()
            "saw" -> SawWaveStrategy()
            "square" -> SquareWaveStrategy()
            "whitenoise" -> WhiteNoiseStrategy()
            else -> throw InvalidSoundGenException(waveName)
        }
    }
}

