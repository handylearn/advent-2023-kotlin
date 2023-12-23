class AgriMap(val name: String) {

    private val ranges = mutableListOf<List<Long>>()

    fun addRange(line: String) {
        ranges.add(line.split(" ").map{it.toLong()})
    }

    fun transform(source: Long) :Long {
        for ((destStart, sourceStart, length) in ranges) {
            if (source >= sourceStart && source < sourceStart + length) {
                return (source - sourceStart) + destStart
            }
        }
        return source // default
    }
}

class AgriMapper(
    private val seeds: List<Long>
) {

    val maps = mutableMapOf<String, AgriMap>()

    private fun transform(mapper: String, value: Long) : Long{
        val map = maps[mapper]
        return map!!.transform(value)
    }

    private fun seedToLocation(seed: Long): Long {
        val soil = transform("seed-to-soil", seed)
        val fertilizer = transform("soil-to-fertilizer", soil)
        val water = transform("fertilizer-to-water", fertilizer)
        val light = transform("water-to-light", water)
        val temperature = transform("light-to-temperature", light)
        val humidity = transform("temperature-to-humidity", temperature)
        val location = transform("humidity-to-location", humidity)
        return location
    }

    fun locations(): List<Long> {
        return seeds.map{ seedToLocation(it) }
    }


    companion object Parser {
        fun create(input: List<String>): AgriMapper {
            val seedString = input[0].split(':')[1]
            val seeds = seedString.trim().split(" ").map{it.toLong()}
            val agriMapper = AgriMapper(seeds)
            var currentMap : AgriMap? = null

            for (line in input) {
                if (line.contains("map:")) {
                    val mapName = line.split(" ")[0]
                    currentMap = AgriMap(mapName)
                    agriMapper.maps[mapName] = currentMap
                }
                if (line.startsWithDigit()) {
                    currentMap!!.addRange(line)
                }
            }
            return agriMapper
        }
    }
}

fun String.startsWithDigit(): Boolean {
    return this.length > 0 && this[0].isDigit()
}

fun main() {
    fun part1(input: List<String>): Long {
        val mapper = AgriMapper.create(input)
        return mapper.locations().min()
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    val testInput = readInput("Day05_test")
    check(part1(testInput) == 35L)

    val input = readInput("Day05")
    part1(input).println()
//    check(part2(testInput) == 0)
//    part2(input).println()
}
