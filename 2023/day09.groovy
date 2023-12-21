//===============================================================
// Advent of Code 2022, Day 9: Mirage Maintenance
//===============================================================

class MirageMaintenance {

    def histories = []

    def MirageMaintenance(input) {

        input.eachLine { line ->
        
            def history = []
            history << line.tokenize().collect { it as BigInteger}
            histories << history
        }

    }

    def getNextValueSum(part) {
        histories.collect { history -> getNextValue(history, part) }.sum()
    }

    def getNextValue(history, part) {

        def sequence = history[0]
        def isZeroSequence = false

//        println sequence

        while (!isZeroSequence) {

            def nextSequence = []
            isZeroSequence = true

            for (i in 1..<sequence.size()) {
                def diff = sequence[i] - sequence[i-1]
                if (isZeroSequence && diff != 0) isZeroSequence = false
                nextSequence << diff
            }

//            println "-> $nextSequence"
            sequence = nextSequence
            history << sequence
        }

        if (part == 1) {

            history.last() << 0

            for (i in (history.size() - 1)..1) {
                history[i-1] << history[i-1].last() + history[i].last()
            }

            history[0].last()

        } else {

            history.last().push(0)

            for (i in (history.size() - 1)..1) {
                history[i-1].push(history[i-1].first() - history[i].first())
            }

            history[0].first()
        }
    }
}

def part1(input) {

    def mm = new MirageMaintenance(input)
    mm.getNextValueSum(1)
}

def part2(input) {

    def mm = new MirageMaintenance(input)
    mm.getNextValueSum(2)
}

//-----------------------------------------------
// Test

def input = '''\
0 3 6 9 12 15
1 3 6 10 15 21
10 13 16 21 30 45
'''

assert part1(input) == 114
assert part2(input) == 2

println 'Test ok'

//-----------------------------------------------
// Puzzle

input = Puzzle.getInput(2023, 9)

//Puzzle.printResults part1(input)
Puzzle.printResults part1(input), part2(input)
