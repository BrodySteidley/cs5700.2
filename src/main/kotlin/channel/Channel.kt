package channel

abstract class Channel {

    abstract fun generate(frequency: Double, durationSeconds: Double, sampleRate: Int): DoubleArray
}

