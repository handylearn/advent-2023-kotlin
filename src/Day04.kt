
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

    fun part2Fast(input: List<String>): Int {
        val cardStack = input.map{ Card.create(it) }
            .map{ CardExtract.create(it)}
        val countOfCards =  Array<Int>(cardStack.size){i -> 1}

        for (i in 0..<countOfCards.size) {
            val card = cardStack[i]
            if (card.winCount > 0) {
                //  increment the cards after the current card
                val factor = countOfCards[i]
                for (j in 0 ..< card.winCount) {
                    countOfCards[i + 1 + j] += factor
                }
            }
        }
        return countOfCards.sum()
    }

    fun part2Slow(input: List<String>): Int {
        // we need a function that th right additian cards after winning
        // than we just move cards from the unused to the used stack, and put newly won cards into the used stack.
        val cardStack = input.map{ Card.create(it) }
            .map{ CardExtract.create(it)}
        val unusedCards = cardStack.toMutableList()
        // val usedCards = emptyList<CardExtract>().toMutableList()
        var usedCardsCount = 0

        while (unusedCards.isNotEmpty()) {
            val card = unusedCards.removeFirst()  // get and remove first element
            val nrNewCards = card.winCount
            // usedCards.add(card)
            usedCardsCount += 1
            val newCards = cardStack.getNewCardsAfter(card, nrNewCards)
            unusedCards.addAll(newCards)
            if (usedCardsCount.mod(10000) == 0) {
                println(usedCardsCount)
            }
        }
        return usedCardsCount
    }

    // test if implementation meets criteria from the description
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)


    val input = readInput("Day04")
    part1(input).println()

    check(part2Fast(testInput) == 30)
    println("Part 2 Check done")

    part2Fast(input).println()
    // interupted after 1.520.000, need anther algorithm
}

private fun List<CardExtract>.getNewCardsAfter(card: CardExtract, nrNewCards: Int): List<CardExtract> {
    val id = card.cardId
    return this.subList(id, id + nrNewCards)
}
