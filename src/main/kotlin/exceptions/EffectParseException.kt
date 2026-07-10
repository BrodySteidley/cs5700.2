package exceptions

class EffectParseException(message : String) : SongFileException("Invalid effect string: $message")

