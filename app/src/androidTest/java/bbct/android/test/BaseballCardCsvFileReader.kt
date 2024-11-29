package bbct.android.test

import bbct.android.data.BaseballCard
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.util.ArrayList

/**
 * This class reads baseball card data from an input stream which is formatted
 * as comma-separated values.
 */
class BaseballCardCsvFileReader(
    input: InputStream,
    hasColHeaders: Boolean
) {
    private val input: BufferedReader = BufferedReader(InputStreamReader(input))

    /**
     * Create a [BaseballCardCsvFileReader] object which reads baseball
     * card data as comma-separated values from the given [InputStream].
     * The input may contain column headers, which will be ignored.
     *
     * @param input The [InputStream] containing the comma-separated values.
     * @param hasColHeaders Whether or not the input contains column headers.
     * @throws IOException If an error occurs while reading the input.
     */
    init {
        if (hasColHeaders) {
            this.input.readLine()
        }
    }

    /**
     * Reads baseball card data from the next line of comma-separated values.
     *
     * @return A [BaseballCard] containing the data from the input stream.
     * @throws IOException If an error occurs while reading the input.
     */
    @Throws(IOException::class)
    fun getNextBaseballCard(): BaseballCard {
        val line = this.input.readLine()
        val data = line
            .split(",".toRegex())
            .dropLastWhile { it.isEmpty() }
            .toTypedArray()
        val autographed = data[0].toBoolean()
        val condition = data[1]
        val brand = data[2]
        val year = data[3].toInt()
        val number = data[4]
        val value = 10000
        val count = 1
        val playerName = data[5]
        val team = data[6]
        val playerPosition = data[7]

        return BaseballCard(
            null,
            autographed,
            condition,
            brand,
            year,
            number,
            value,
            count,
            playerName,
            team,
            playerPosition
        )
    }

    /**
     * Determine if the input stream contains more baseball card data. If the
     * input stream is ready for an input operation, then this function returns
     * `true`. The validity of the data is determined only after
     * calling [.getNextBaseballCard] or [ ][.getAllBaseballCards].
     *
     * @return `true` if the input stream is ready for an input
     * operation; `false`, otherwise.
     * @throws IOException If an error occurs while reading the input.
     */
    @Throws(IOException::class)
    fun hasNextBaseballCard(): Boolean {
        return this.input.ready()
    }

    /**
     * Reads all the baseball card data from the input stream.
     *
     * @return A list of [BaseballCard] objects containing all of the
     * baseball card data from the input stream.
     * @throws IOException If an error occurs while reading the input.
     */
    @Throws(IOException::class)
    fun getAllBaseballCards(): MutableList<BaseballCard?> {
        val cards: MutableList<BaseballCard?> = ArrayList<BaseballCard?>()
        while (this.hasNextBaseballCard()) {
            cards.add(this.getNextBaseballCard())
        }

        return cards
    }

    /**
     * Close the input stream.
     *
     * @throws IOException If an error occurs while reading the input.
     */
    @Throws(IOException::class)
    fun close() {
        this.input.close()
    }
}
