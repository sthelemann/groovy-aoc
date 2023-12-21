//===============================================================
// Advent of Code 2023, Day 4: Scratchcards
//===============================================================

class Scratchcards {

    static List scList

    def winningNumbers
    def yourNumbers

    static void parse(input) {

        scList = []

        input.eachLine { line ->

            def tokens = line.split(':')[1].split(/\|/)

            def sc = new Scratchcards(
                // findAll finds all items matching Groovy truth, i.e. item != ''
                winningNumbers: tokens[0].trim().split(' ').findAll().collect {it as int},
                yourNumbers: tokens[1].trim().split(' ').findAll().collect {it as int}
            )

            scList << sc
        }
    }

    // for part 1
    static int getPoints() {

        int points = 0

        scList.each {
            def n = it.winningNumbers.intersect(it.yourNumbers).size()
//            def n =  it.yourNumbers.findAll { number -> number in it.winningNumbers }.size()
            def p = 0
            for (int k = 0; k < n; k++) {
                p = (k == 0 ?  1 : 2 * p)
            }
            points += p
        }

        points
    }

    // for part 2
    static int getCount() {

        int points = 0

        def counterList = scList.collect { 1 }

        scList.eachWithIndex { sc, i ->
            def n = sc.winningNumbers.intersect(sc.yourNumbers).size()
            // increase counter of subsequent cards
            for (int k = 1; k <= n; k++) {
                counterList[i + k] += counterList[i]
            }
        }

        counterList.sum()
    }
}

def part1(input) {
    Scratchcards.parse(input)
    Scratchcards.getPoints()
}

def part2(input) {
    Scratchcards.parse(input)
    Scratchcards.getCount()
}

//-----------------------------------------------
// Test

def input = '''\
Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11'''

assert part1(input) == 13
assert part2(input) == 30

println 'Test ok'

//-----------------------------------------------
// Puzzle

input = Puzzle.getInput(2023, 4)

Puzzle.printResults part1(input), part2(input)
