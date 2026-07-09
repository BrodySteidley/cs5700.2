package soundGen

import kotlin.math.PI

interface SoundGenStrategy {
    fun generate(frequency: Double, durationSeconds: Double, sampleRate: Int): DoubleArray {
        val sampleCount = (sampleRate * durationSeconds).toInt()
        val samples = DoubleArray(sampleCount)

        val phaseIncrement = 2.0 * PI * frequency / sampleRate   // radians per sample
        var phase = 0.0
        for (n in 0 until sampleCount) {
            samples[n] = shape(phase)             // the shape decides the amplitude
            phase += phaseIncrement                 // advance by frequency's worth of phase
            if (phase >= 2.0 * PI) phase -= 2.0 * PI     // wrap to keep phase in [0, 2π)
        }
        return samples
    }

    abstract fun shape(phase: Double): Double;
}

