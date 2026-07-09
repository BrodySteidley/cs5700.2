package channel

import soundGen.SoundGenStrategy

class WaveformChannel(
    private val soundGenStrategy: SoundGenStrategy
) : Channel() {
    override fun generate(frequency: Double, durationSeconds: Double, sampleRate: Int): DoubleArray {
        return soundGenStrategy.generate(frequency, durationSeconds, sampleRate)
    }
}

