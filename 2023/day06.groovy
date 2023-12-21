//===============================================================
// Advent of Code 2023, Day 6: Wait For It
//===============================================================

class Race {
    BigInteger time
    BigInteger distance
    
    def getAllDistances() {

        def result = []

        for (i in 0..this.time) {
            result << (this.time - i) * i
        }

        result
    }
}

def getRaces(input, part) {

    def result = []

    def (timeLine, distanceLine) = input.split('\n')

    if (part == 1) {
        def times = timeLine.split(':')[1].tokenize().collect { it as BigInteger }
        def distances = distanceLine.split(':')[1].tokenize().collect { it as BigInteger }

        for (i in 0..<times.size()) {
            result << new Race(time: times[i], distance: distances[i])
        }
    } else {
        def time = (timeLine.split(':')[1].replaceAll(' ', '') as BigInteger)
        def distance = (distanceLine.split(':')[1].replaceAll(' ', '') as BigInteger)
        result << new Race(time: time, distance: distance)
    }

    result
}

def part(input, n) {

    def races = getRaces(input, n)

    def result = 1

    races.each { race ->
        result *= race.getAllDistances().findAll { it > race.distance }.size()
    }

    result
}

//-----------------------------------------------
// Test

def input = '''\
Time:      7  15   30
Distance:  9  40  200'''

assert part(input, 1) == 288
assert part(input, 2) == 71503

println 'Test ok'

//-----------------------------------------------
// Puzzle

input = Puzzle.getInput(2023, 6)

Puzzle.printResults part(input, 1), part(input, 2)
