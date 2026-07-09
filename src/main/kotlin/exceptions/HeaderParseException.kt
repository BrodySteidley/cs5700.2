package exceptions

class HeaderParseException(message: String) : SongFileException("in header: $message")

