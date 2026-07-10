import channel.Channel
import channel.ChannelFactory
import exceptions.HeaderParseException
import java.io.File

import exceptions.MeasureParseException
import exceptions.SongFileException

object Parser {

    private fun parseChannel(line: String, sampleRate: Int, beatsPerMeasure: Int, tempo: Int): DoubleArray {
        val settings = line.split("|")[0];
        val channel: Channel = ChannelFactory.createChannel(settings);

        val measures = line.split("|")
            .drop(1)
            .filter { it.isNotBlank() }

        val allSamples = mutableListOf<Double>()

        for ((measureIndex, measure) in measures.withIndex()) {
            val parts = measure.trim().split(Regex("\\s+"))
            if (parts.size % 2 != 0) {
                throw MeasureParseException(measureIndex, "Expected note/duration pairs")
            }

            val frequencies: List<Double> = parts.filterIndexed { index, _ -> index % 2 == 0 }.map {
                PianoNotes[it] ?: throw MeasureParseException(measureIndex, "Invalid note \"${it}\"");
            }

            var durations: List<Double>;
            try {
                durations = parts.filterIndexed { index, _ -> index % 2 == 1 }.map { it.toDouble() }
            } catch (e: Exception) {
                throw MeasureParseException(
                    measureIndex,
                    "Invalid duration"
                );
            }

            if (durations.sum() > beatsPerMeasure)
                throw MeasureParseException(
                    measureIndex,
                    "Invalid beat count, actual (${durations.sum()}) is greater than expected (${beatsPerMeasure})"
                );

            for ((frequency, duration) in frequencies.zip(durations)) {
                if (frequency == 0.0) {
                    // rest
                    val restSamples = DoubleArray((duration * 60.0 / tempo * sampleRate).toInt())
                    allSamples.addAll(restSamples.toList())
                } else {
                    val samples = channel.generate(frequency, duration * 60.0 / tempo, sampleRate)
                    allSamples.addAll(samples.toList())
                }
            }

        }


        return allSamples.toDoubleArray()
    }

    fun parse(filename: String): Song {
        val fileBuffer = File(filename).bufferedReader();

        /* first line is the header, */
        val headerString = fileBuffer.readLine() ?: throw HeaderParseException("empty file");
	val header = headerString.trim().split(Regex("\\s+"))

        if (header.size != 3) throw HeaderParseException("invalid header length");

        val sampleRate = header[0].toIntOrNull() ?: throw HeaderParseException("Invalid sample rate");
        val beatsPerMeasure = header[1].toIntOrNull() ?: throw HeaderParseException("Invalid beats per measure");
        val tempo = header[2].toIntOrNull() ?: throw HeaderParseException("Invalid tempo");

        val samples: MutableList<DoubleArray> = mutableListOf();

        /* each line after the header represents a channel */
        var line: String?;
        while (true) {
            line = fileBuffer.readLine();
            if (line == null) break;
            if (line.split('|').isEmpty() || line.none { !it.isISOControl() }) continue;

            samples.add(parseChannel(line, sampleRate, beatsPerMeasure, tempo));
        }

        if (samples.isEmpty())
            throw SongFileException("No channels");

        return Song.createFromChannels(sampleRate, samples);
    }
}

