package exceptions

class MeasureParseException(measureIndex: Int, message: String) :
    SongFileException("in measure ${measureIndex}: ${message}")

