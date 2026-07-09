import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.SourceDataLine

class Song(
    private val sampleRate: Int,
    private val samples: DoubleArray
) {
    companion object {
        private fun mix(channels: List<DoubleArray>): DoubleArray {
            val length = channels.maxOf { it.size }
            val mixed = DoubleArray(length)
            for (channel in channels)
                for (n in channel.indices)
                    mixed[n] += channel[n]
            return mixed
        }

        fun createFromChannels(sampleRate : Int, channels : List<DoubleArray>) : Song = Song(sampleRate, mix(channels))
    }

    fun play() {
        // 16-bit, mono, signed, little-endian PCM
        val format = AudioFormat(sampleRate.toFloat(), 16, 1, true, false)
        val line: SourceDataLine = AudioSystem.getSourceDataLine(format)
        line.open(format)
        line.start()

        // Convert each Double in [-1.0, 1.0] into two bytes (a 16-bit signed sample).
        val buffer = ByteArray(samples.size * 2)
        for (i in samples.indices) {
            val clamped = samples[i].coerceIn(-1.0, 1.0)
            val value = (clamped * Short.MAX_VALUE).toInt()
            buffer[i * 2] = (value and 0xFF).toByte()          // low byte
            buffer[i * 2 + 1] = (value shr 8 and 0xFF).toByte()    // high byte
        }

        line.write(buffer, 0, buffer.size)
        line.drain()   // block until every sample has finished playing
        line.stop()
        line.close()
    }
}

