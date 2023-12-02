fun String.firstDigit() : Int {
    return this.first{ ch -> ch in '0'..'9' }.digitToInt()
}
fun String.calibrationValue(): Int {
    return (10 * this.firstDigit() + this.reversed().firstDigit())
}

fun String.findDigitWords() : Pair<String?, String?> {
    val allDigits = "zero|one|two|three|four|five|six|seven|eight|nine|0|1|2|3|4|5|6|7|8|9".split("|")
    val firstMatch = this.findAnyOf(allDigits)
    val lastMatch = this.findLastAnyOf(allDigits)
    return Pair(
        firstMatch?.second,
        lastMatch?.second
    )
}

fun digitWordToInt(word: String?): Int {
    return when (word) {
        "1", "one" -> 1
        "2", "two" -> 2
        "3", "three" -> 3
        "4", "four" -> 4
        "5", "five" -> 5
        "6", "six" -> 6
        "7", "seven" -> 7
        "8", "eight" -> 8
        "9", "nine" -> 9
        "0", "zero" -> 0
        else -> throw Exception("Bad Digit:" + word)
    }
}

fun String.calibrationValueWithWords(): Int {
    var pair = this.findDigitWords()
    return (10 * digitWordToInt(pair.first) + digitWordToInt(pair.second) )
}

fun main() {
    fun part1(input: List<String>): Int {
        return input.map{
            it.calibrationValue()
        }.sum()
    }

    fun part2(input: List<String>): Int {
        return input.map{
            it.calibrationValueWithWords()
        }.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 142)

    val testInput2 = readInput("Day01_2_test")
    check(part2(testInput2) == 281)


    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
