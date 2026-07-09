import exceptions.SongFileException
import java.io.IOException

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Program expects at least one parameter:  a song file to play.")
        return
    }

    for (songName in args) {
        try {
            val song: Song = Parser.parse(songName)
            println("Playing song file \"${songName}\"")
            song.play()
        } catch (e: IOException) {
            println("Failed to read file \"${songName}\":  ${e.message}")
        } catch (e: SongFileException) {
            println("Failed to parse song:  ${e.message}")
        }
    }
}
