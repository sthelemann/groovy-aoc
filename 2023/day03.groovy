//===============================================================
// Advent of Code 2023, Day 3: Gear Ratios
//===============================================================

import groovy.transform.ToString

class GearRatios {

    List<Number> numbers = []
    List<Number> symbols = []

    def numberIndex = [:]
    def symbolIndex = [:]

    def GearRatios(input) {

        int x = 0

        input.eachLine { line ->

            def number = null
            int y = 0

            def numberList = this.numberIndex[x]
            if (numberList == null) {
                numberList = []
                this.numberIndex[(x)] = numberList
            }

            def symbolList = this.symbolIndex[x]
            if (symbolList == null) {
                symbolList = []
                this.symbolIndex[(x)] = symbolList
            }

            for (c in line) {

                if (c >= '0' && c <= '9') {
                    if (number == null) {
                        number = new Number(n: c, x: x, yFrom: y, yTo: y)
                    } else {
                        number.n += c
                        number.yTo = y
                    }
                } else {
                    if (c != '.') {
                        def symbol = new Symbol(s: c, x: x, y: y)
                        this.symbols << symbol
                        symbolList << symbol
                    }

                    if (number != null) {
                        this.numbers << number
                        numberList << number
                        number = null
                    }
                }

                y++
            }

            if (number != null) {
                this.numbers << number
                numberList << number
                number = null
            }

            x++
        }

        // numbers.each {
        //     if (it.x == 0) println it
        // }

        // symbolIndex.keySet().sort().each {
        //     println "$it: ${symbolIndex[it]}"
        // }

        numberIndex.keySet().sort().each {
            println "$it: ${numberIndex[it]}"
        }

        symbols.each {
            println "symbol $it"
        }

        // symbolIndex.keySet().sort().each {
        //     println "$it: ${symbolIndex[it]}"
        // }
    }

    def getPartNumbers() {

        def result = []

        numbers.each { 

            if (isNextToSymbol(it)) {
                result << (it.n as int)
            }
        }

        result
    }

    def isNextToSymbol(number) {

        int yFrom = number.yFrom - 1
        int yTo = number.yTo + 1

        def found = false
        def deltaList = [-1, 0, 1] 

        for (delta in deltaList) {

            def symbols = symbolIndex[number.x + delta]

            if (symbols != null) {
                for (symbol in symbols) {
                    if (symbol.y >= yFrom && symbol.y <= yTo) {
                        found = true
                        break
                    }
                }
            }

            if (found) break
        } 

        found
    }

    def getGearRatios() {

        def result = []

        this.symbols.each { symbol ->

            if (symbol.s == '*') {

                def neighbors = []
                def deltaList = [-1, 0, 1] 

                for (delta in deltaList) {

                    def numbers = numberIndex[symbol.x + delta]

                    if (numbers != null) {
                        for (number in numbers) {
                            if (symbol.y >= number.yFrom - 1 && symbol.y <= number.yTo + 1) {
                                neighbors << (number.n as int)
                            }
                        }
                    }
                }

                result << (neighbors.size() == 2 ? (neighbors[0] * neighbors[1] as BigInteger) : 0)
            }
        }

        result
    }
}

@ToString
class Number {
    def n
    int x
    int yFrom
    int yTo
}

@ToString
class Symbol {
    def s
    int x
    int y
}

def part1(input) {

    new GearRatios(input).getPartNumbers().sum()
}

def part2(input) {

    new GearRatios(input).getGearRatios().sum()
}

//-----------------------------------------------
// Test

def input = '''\
467..114..
...*......
..35..633.
......#...
617*......
.....+.58.
..592.....
......755.
...$.*....
.664.598..'''

def gr = new GearRatios(input)

assert part1(input) == 4361
assert part2(input) == 467835

println 'Test ok'

//-----------------------------------------------
// Puzzle

input = Puzzle.getInput(2023, 3)

Puzzle.printResults part1(input), part2(input)
