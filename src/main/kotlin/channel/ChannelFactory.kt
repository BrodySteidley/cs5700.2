package channel

import soundGen.SoundGenStrategyFactory

object ChannelFactory {

    fun createChannel(channelSettings: String): Channel {
        val waveform: String = channelSettings.split(" ")[0];
        val effects: List<String> = channelSettings.split(" ").drop(1);

        var channel: Channel = WaveformChannel(SoundGenStrategyFactory.createStrategy(waveform));

        for (effect in effects)
            channel = ChannelDecoratorFactory.createChannelDecorator(channel, effect);

        return channel;
    }

}

