//===============================================================
// Advent of Code 2023, Day 1: Trebuchet?!
//===============================================================

def part1(input) {

    def list = []

    input.eachLine { line ->
        digitList = []
        for (c in line) {
            if (c >= '0' && c <= '9') digitList << c
        }
        list << digitList
    }

    list.collect {(it.first() + it.last()) as int}.sum()
}

def part2(input) {

    // words for digits may overlap, e.g. mjlrpthgvz57skzbs24fourtwoneklr (line 73 in input)
    // -> mjlrpthgvz57skzbs24421klr

    def map = [one: '1', two: '2', three: '3', four: '4', five: '5', six: '6', seven: '7', eight: '8', nine: '9']

    def list = []

    input.eachLine { line ->

        digitList = []
        def dropSize

        while (line.size() > 0) {
            dropSize = 1
            if (line[0] >= '0' && line[0] <= '9') {
                digitList << line[0]
            } else {
                line.find(/^(one|two|three|four|five|six|seven|eight|nine)/) { match, word ->
                    digitList << map[word]
                    dropSize = word.size() - 1 // word for digits overlap in at most one character
                }
            }
            line = line.drop(dropSize)
        }

        list << digitList
    }

    list.collect {(it.first() + it.last()) as int}.sum()
}

//-----------------------------------------------
// Test

def input = '''\
1abc2
pqr3stu8vwx
a1b2c3d4e5f
treb7uchet'''

assert part1(input) == 142

input = '''\
two1nine
eightwothree
abcone2threexyz
xtwone3four
4nineeightseven2
zoneight234
7pqrstsixteen'''

assert part2(input) == 281

println 'Test ok'

//-----------------------------------------------
// Puzzle

input = Puzzle.getInput(2023, 1)

Puzzle.printResults part1(input), part2(input)
