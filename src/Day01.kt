fun String.firstDigit() : Int {
    return this.first{ ch -> ch in '0'..'9' }.digitToInt()
}
fun String.calibrationValue(): Int {
    return (10 * this.firstDigit() + this.reversed().firstDigit())
}

fun main() {
    fun part1(input: List<String>): Int {
        return input.map{
            it.calibrationValue()
        }.sum()
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 142)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
