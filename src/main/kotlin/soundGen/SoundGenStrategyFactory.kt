package soundGen

import exceptions.InvalidStrategyException

object SoundGenStrategyFactory {
    fun createStrategy(waveName: String): SoundGenStrategy {
        return when (waveName) {
            "sin" -> SinWaveStrategy()
            "saw" -> SawWaveStrategy()
            "square" -> SquareWaveStrategy()
            "whitenoise" -> WhiteNoiseStrategy()
            else -> throw InvalidStrategyException(waveName)
        }
    }
}

