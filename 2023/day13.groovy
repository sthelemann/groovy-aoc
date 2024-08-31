//===============================================================
// Advent of Code 2023, Day 13: Point of Incidence
//===============================================================

class PointOfIncidence {

    def patterns = []
    def transposedPatterns = []

    PointOfIncidence(input) {

        def pattern = []

        input.eachLine { line ->

            if (line) {
                pattern << line
            } else {
                patterns << pattern
                transposedPatterns << transposePattern(pattern)
                pattern = []
            }
        }

        patterns << pattern
        transposedPatterns << transposePattern(pattern)
    }

    def transposePattern(pattern) {

        def transposedPattern = []
        def width = pattern[0].size()
    
        for (i in 0..<width) {

            def column = ''
            pattern.each { line ->
                column += line[i]
            }

            transposedPattern << column
        }

        transposedPattern
    }

    def findReflection(pattern) {

        def height = pattern.size()

        int i = 0

        for (; i < height - 1; i++) {
            if (pattern[i] == pattern[i+1]) {
                // reflection candidate found
                def k = 1
                def isPerfectReflection = true
                while (i-k >= 0 && i+1+k < height) {
                    if (pattern[i-k] != pattern[i+1+k]) {
                        isPerfectReflection = false
                        break
                    }
                    k++
                }
                if (isPerfectReflection) break
            }
        }

        if (i < height - 1) {
            // perfect reflection found
            i + 1
        } else {
            // perfect reflection not found
            0
        }
    }

    def findSmudgedReflection(pattern) {

        def height = pattern.size()

        int i = 0

        for (; i < height - 1; i++) {
            def d = diff(pattern[i], pattern[i+1])
            println "i = $i, d = $d"
            if (d <= 1) {
                // reflection candidate found
                def k = 1
                while (i-k >= 0 && i+1+k < height && d <= 1) {
                    d += diff(pattern[i-k], pattern[i+1+k])
                    if (d > 1) break
                    k++
                }
                if (d == 1) break
            }
        }

        if (i < height - 1) {
            // perfect smudged reflection found
            i + 1
        } else {
            // perfect smudged reflection not found
            0
        }

    }

    def diff(line1, line2) {

        def width = line1.size()
        int result = 0

        for (i in 0..<width) {
            if (line1[i] != line2[i]) {
                result++
            }
        }

        result
    }
}

def part1(input) {

    def poi = new PointOfIncidence(input)

    poi.patterns.collect { poi.findReflection(it) }.sum() * 100 +     // horizontal reflections
    poi.transposedPatterns.collect { poi.findReflection(it) }.sum()  // vertical reflections
}

def part2(input) {

    def poi = new PointOfIncidence(input)

    poi.patterns.collect { poi.findSmudgedReflection(it) }.sum() * 100 +     // horizontal reflections
    poi.transposedPatterns.collect { poi.findSmudgedReflection(it) }.sum()  // vertical reflections
}

//-----------------------------------------------
// Test

def input = '''\
#.##..##.
..#.##.#.
##......#
##......#
..#.##.#.
..##..##.
#.#.##.#.

#...##..#
#....#..#
..##..###
#####.##.
#####.##.
..##..###
#....#..#'''

assert part1(input) == 405
assert part2(input) == 400

println 'Test ok'

//-----------------------------------------------
// Puzzle

input = Puzzle.getInput(2023, 13)

Puzzle.printResults part1(input), part2(input)
