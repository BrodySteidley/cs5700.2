package exceptions

open class InvalidStrategyException(waveName: String) : RuntimeException("Invalid Wave Strategy \"$waveName\"")

