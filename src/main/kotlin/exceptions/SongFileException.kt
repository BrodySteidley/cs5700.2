package exceptions

open class SongFileException(message: String) : RuntimeException("SONG FILE-READ FAILED: ${message}")

