package exceptions

open class InvalidSoundGenException(waveName: String) : SongFileException("Invalid sound generation name \"$waveName\"")

