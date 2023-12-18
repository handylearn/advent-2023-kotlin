/**
 * Advent of code day 2
 */
class CubicSample(
    val pieces: Map<String, Int>) {

    fun hasLessCubesOf(color: String, bag: CubicSample): Boolean {
        val number = pieces[color] ?: 0
        return number <= bag.pieces[color]!!
    }
    fun isSubSampleOf(bag: CubicSample): Boolean {
        return hasLessCubesOf("red", bag)
                && hasLessCubesOf("green", bag)
                && hasLessCubesOf("blue", bag)
    }

    fun getNumber(color: String) : Int =
        pieces[color] ?: 0

    fun power(): Int {
        return (pieces["red"] ?: 0 ) * (pieces["green"] ?: 0 ) * (pieces["blue"] ?: 0 )
    }

    companion object Factory{
        fun create(sampleLine: String): CubicSample {
            val extracted : List<String> = sampleLine.split(',')
            val pairs = extracted.map{ part ->
                val tupel = part.trim().split(" ").take(2)
                Pair<String, Int>(tupel[1], tupel[0].toInt())
            }
            return CubicSample(pairs.toMap())
        }
    }
}

class CubicGame(
    val id: Int,
    val hands: List<CubicSample>
) {
    companion object Factory{
        fun create(sampleLine: String): CubicGame {
            val x  = sampleLine.split(":")
            val gameId = x[0].substring(5).toInt()
            val samples = x[1].split(";").map{
                CubicSample.create(it)
            }
            return CubicGame(gameId, samples)
        }
    }

    fun isPossibleWith(bag: CubicSample): Boolean {
        return hands.all{it.isSubSampleOf(bag)}
    }

    fun minimalBag() : CubicSample {
        val minimalPairs : List<Pair<String, Int>> = listOf("red", "green", "blue").map{ color ->
            val minimumNeeded = hands.map{ it.getNumber(color) }.max()
            Pair(color, minimumNeeded)
        }
        return CubicSample(minimalPairs.toMap())
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val bag = CubicSample(mapOf(
            "red" to 12,
            "green" to 13,
            "blue" to 14
        ))

        return input.map{
            val game = CubicGame.create(it)
            if( game.isPossibleWith(bag)) {
                game.id
            }
            else {
                0
            }
        }.sum()
    }

    fun part2(input: List<String>): Int {
        return input.map {
            val game = CubicGame.create(it)
            val minimalBag = game.minimalBag()
            minimalBag.power()
        }.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8)
    check(part2(testInput) == 2286)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
