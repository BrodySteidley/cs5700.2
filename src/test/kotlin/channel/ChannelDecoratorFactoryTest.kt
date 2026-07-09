package channel

import exceptions.EffectParseException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ChannelDecoratorFactoryTest {

    private val channel = object : Channel() {
        override fun generate(
            frequency: Double,
            durationSeconds: Double,
            sampleRate: Int
        ): DoubleArray = doubleArrayOf(1.0)
    }

    @Test
    fun `creates volume decorator`() {
        val decorator = ChannelDecoratorFactory.createChannelDecorator(
            channel,
            "vol$0.5"
        )

        assertTrue(decorator is VolumeDecorator)
    }

    @Test
    fun `creates ADS envelope decorator`() {
        val decorator = ChannelDecoratorFactory.createChannelDecorator(
            channel,
            "ads$0.1$0.3$0.5"
        )

        assertTrue(decorator is ADSEnvelopeDecorator)
    }

    @Test
    fun `creates tanh distortion decorator`() {
        val decorator = ChannelDecoratorFactory.createChannelDecorator(
            channel,
            "tanh$2.0"
        )

        assertTrue(decorator is TanhDistortionDecorator)
    }

    @Test
    fun `creates clip distortion decorator`() {
        val decorator = ChannelDecoratorFactory.createChannelDecorator(
            channel,
            "clip$0.8"
        )

        assertTrue(decorator is ClipDistortionDecorator)
    }

    @Test
    fun `throws exception for unknown effect`() {
        assertThrows(EffectParseException::class.java) {
            ChannelDecoratorFactory.createChannelDecorator(
                channel,
                "unknown$1.0"
            )
        }
    }
}