import kotlin.math.max
import kotlin.math.min

class Plan(
	private val matrix: List<String>
) {
    private val width = matrix.map{ it.length }.max()
    private val height = matrix.size

    // get all ranges with numbers in it
    private fun numberRangesOf(line: String): Sequence<IntRange> {
        val digitRegex = Regex("""(\d+)""")
        val matchResults: Sequence<MatchResult> = digitRegex.findAll(line)
        return matchResults.map { it.range }
    }

    // extend a range by one on each side if possible
    private fun extendRangeByOne(range: IntRange): IntRange {
        val last = range.start + range.count()
        val newLast = min(last, width - 1)
        return IntRange(
            max(range.start - 1, 0),
            newLast
        )
    }

    // check that a string contains something different than a digit or a dot
    private fun hasSymbol(string: String): Boolean {
        // as the matches method try to match the whole string, I put in wildcards before and after
        val regex = Regex(""".*[^0-9.].*""")
        return regex.matches(string)
    }

    // check that a range in a row has a symbol near it
    private fun hasSymbolNeighbour(row: Int, range: IntRange): Boolean {
        val expanded = extendRangeByOne(range)
        var symbolFound =
            hasSymbol(matrix[row].substring(expanded))
        if (row > 0) {
            symbolFound = symbolFound || hasSymbol(matrix[row - 1].substring(expanded))
        }
        if (row < height - 1) {
            symbolFound = symbolFound || hasSymbol(matrix[row + 1].substring(expanded))
        }
        return symbolFound
    }

    // get all numbers that have a symbol near it. (part 1)
    fun filteredNumbers(): List<Int> {
        return matrix.mapIndexed { row: Int, line: String ->
            val ranges = this.numberRangesOf(line)
            ranges.filter { range ->
                hasSymbolNeighbour(row, range)
            }.map { range: IntRange ->
                matrix[row].substring(range).toInt()
            }.toList()
        }.flatten()
    }

    private fun numberOfRowAndRange(row: Int, range: IntRange):  Int {
        return matrix[row].substring(range).toInt()
    }

    // probably there is a better string method instead using regex
    private fun posOfGears(line: String): Sequence<Int> {
        val digitRegex = Regex("""(\*)""")
        val matchResults: Sequence<MatchResult> = digitRegex.findAll(line)
        return matchResults.map { it.range.start }
    }

    private fun isInOrNearRange(nr: Int, range: IntRange): Boolean {
        return (nr + 1 >= range.start)
                && (nr - 1 <= range.last)
    }

    private fun getNumbersNear(
        numberRanges: List<List<IntRange>>,
        row: Int,
        col: Int
    ): List<Int> {
        val ranges: MutableList<Int> =
            extractNumbers(numberRanges, row, col).toMutableList()
        if (row > 0) {
            ranges.addAll(
                extractNumbers(numberRanges, row - 1, col)
            )
        }
        if (row + 1 < height) {
            ranges.addAll(
                extractNumbers(numberRanges, row + 1, col)
            )
        }
        return ranges
    }

    private fun extractNumbers(
        numberRanges: List<List<IntRange>>,
        row: Int,
        col: Int
    ) = numberRanges[row].filter { range ->
        isInOrNearRange(col, range)
    }.map {
        numberOfRowAndRange(row, it)
    }

    fun gearRatios(): List<Int> {
        // prepare all number ranges
        val numberRanges = matrix.map {
            numberRangesOf(it).toList()
        }
        // iterate over all gears
        val x = matrix.mapIndexed { row: Int, line: String ->
            val gearPositions = posOfGears(line)
            val gearNumbers = gearPositions.map {
                // for each gear: get ranges near it.
                val gearNumbers = getNumbersNear(numberRanges, row, it)
                // if they are two-> get the numbers and multiply them
                if (gearNumbers.size >= 2) {
                    gearNumbers[0] * gearNumbers[1]
                } else {
                    0
                }
            }.toList()
            gearNumbers
        }
        return x.flatten()
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val plan = Plan(input)
        val numbers = plan.filteredNumbers()
        return numbers.sum()
    }

    fun part2(input: List<String>): Int {
        val plan = Plan(input)
        val numbers = plan.gearRatios()
        return numbers.sum()
    }

    // test if implementation meets criteria from the description.
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361)

    val input = readInput("Day03")
    part1(input).println()

    check(part2(testInput) == 467835)
    part2(input).println()
}