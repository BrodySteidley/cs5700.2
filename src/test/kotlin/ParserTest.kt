import exceptions.HeaderParseException
import exceptions.MeasureParseException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.io.path.createTempFile

class ParserTest {

    private fun createTempSongFile(content: String): File {
        val file = createTempFile(
            prefix = "test_song",
            suffix = ".txt"
        ).toFile()

        file.writeText(content)
        return file
    }

    @Test
    fun `parses valid song file`() {
        val file = createTempSongFile(
            """
            44100 4 120
            sin|A4 1|
            """.trimIndent()
        )

        val song = Parser.parse(file.absolutePath)

        assertNotNull(song)
    }

    @Test
    fun `parses multiple channels`() {
        val file = createTempSongFile(
            """
            44100 4 120
            sin|A4 1|
            square|C4 1|
            """.trimIndent()
        )

        val song = Parser.parse(file.absolutePath)

        assertNotNull(song)
    }

    @Test
    fun `parses rests`() {
        val file = createTempSongFile(
            """
            44100 4 120
            sin|- 1|
            """.trimIndent()
        )

        val song = Parser.parse(file.absolutePath)

        assertNotNull(song)
    }

    @Test
    fun `throws exception for empty file`() {
        val file = createTempSongFile("")

        assertThrows(HeaderParseException::class.java) {
            Parser.parse(file.absolutePath)
        }
    }

    @Test
    fun `throws exception for invalid header length`() {
        val file = createTempSongFile(
            """
            44100 4
            sin|A4 1|
            """.trimIndent()
        )

        assertThrows(HeaderParseException::class.java) {
            Parser.parse(file.absolutePath)
        }
    }

    @Test
    fun `throws exception for invalid sample rate`() {
        val file = createTempSongFile(
            """
            abc 4 120
            sin|A4 1|
            """.trimIndent()
        )

        assertThrows(HeaderParseException::class.java) {
            Parser.parse(file.absolutePath)
        }
    }

    @Test
    fun `throws exception for invalid beats per measure`() {
        val file = createTempSongFile(
            """
            44100 abc 120
            sin|A4 1|
            """.trimIndent()
        )

        assertThrows(HeaderParseException::class.java) {
            Parser.parse(file.absolutePath)
        }
    }

    @Test
    fun `throws exception for invalid tempo`() {
        val file = createTempSongFile(
            """
            44100 4 abc
            sin|A4 1|
            """.trimIndent()
        )

        assertThrows(HeaderParseException::class.java) {
            Parser.parse(file.absolutePath)
        }
    }

    @Test
    fun `throws exception for invalid note`() {
        val file = createTempSongFile(
            """
            44100 4 120
            sin|NOT_A_NOTE 1|
            """.trimIndent()
        )

        assertThrows(MeasureParseException::class.java) {
            Parser.parse(file.absolutePath)
        }
    }

    @Test
    fun `throws exception for invalid duration`() {
        val file = createTempSongFile(
            """
            44100 4 120
            sin|A4 abc|
            """.trimIndent()
        )

        assertThrows(MeasureParseException::class.java) {
            Parser.parse(file.absolutePath)
        }
    }

    @Test
    fun `throws exception when measure exceeds beats per measure`() {
        val file = createTempSongFile(
            """
            44100 4 120
            sin|A4 5|
            """.trimIndent()
        )

        assertThrows(MeasureParseException::class.java) {
            Parser.parse(file.absolutePath)
        }
    }

    @Test
    fun `ignores empty lines`() {
        val file = createTempSongFile(
            """
            44100 4 120

            sin|A4 1|

            """.trimIndent()
        )

        val song = Parser.parse(file.absolutePath)

        assertNotNull(song)
    }
}