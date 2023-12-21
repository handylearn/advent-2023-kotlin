
class Card(
    private val name: String,
    private val winNumbers: List<Int>,
    private val ownNumbers: List<Int>
) {

    private fun ownWinners(): Set<Int> {
        return winNumbers.intersect(ownNumbers)
    }

    fun winningPoints(): Int {
        val nrOfWinners = ownWinners().size
        if (nrOfWinners == 0) {
            return 0
        }
        var points = 1
        for (i in 1 ..< nrOfWinners) {
            points *= 2
        }
        return points
    }

    companion object Factory{
        fun create(input: String): Card {
            val x = input.split(':')
            val name = x[0]
            val y = x[1].split('|')
            val winnings = y[0].split(' ')
                .filter{it.isNotEmpty()}
                .map{it.toInt()}
            val ownNumbers = y[1].split(' ')
                .filter{it.isNotEmpty()}
                .map{it.toInt()}
            return Card(name, winnings, ownNumbers)
        }
    }
}



fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            val card = Card.create(line)
            card.winningPoints()
        }
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)


    val input = readInput("Day04")
    part1(input).println()

//    check(part2(testInput2) == 0)
//    part2(input).println()
}
