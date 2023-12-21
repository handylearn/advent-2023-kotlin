
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

    fun countOwnWinners(): Int = ownWinners().size

    fun cardNumber(): Int {
        val x = name.split(" ")
        return x.last().toInt()
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

// stores only the essentials of the card needed for part 2,
// to use less memory
data class CardExtract(
    val cardId: Int,
    val winCount: Int
) {
    companion object Factory{
        fun create(card: Card): CardExtract {
            return CardExtract(
                card.cardNumber(),
                card.countOwnWinners()
            )
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
        val cardStack = input.map{ Card.create(it) }
            .map{ CardExtract.create(it)}
        val countOfCards =  Array<Int>(cardStack.size){ _ -> 1}

        for (i in countOfCards.indices) {
            val card = cardStack[i]
            if (card.winCount > 0) {
                //  increment the cards after the current card
                val currentCardCount = countOfCards[i]
                for (j in 0 ..< card.winCount) {
                    countOfCards[i + 1 + j] += currentCardCount
                }
            }
        }
        return countOfCards.sum()
    }

    // test if implementation meets criteria from the description
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)

    val input = readInput("Day04")
    part1(input).println()

    check(part2(testInput) == 30)
    part2(input).println()
}

