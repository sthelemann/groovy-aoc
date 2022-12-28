//===============================================================
// Advent of Code 2022, Day 14: Regolith Reservoir
//===============================================================

class RegolithReservoir {

    def cave = [:]
    def xMin = Integer.MAX_VALUE
    def xMax = Integer.MIN_VALUE
    def yMax = Integer.MIN_VALUE

    RegolithReservoir(input) {

        def lines = []

        input.eachLine { line ->
        
            def points = []

            line.replace(' -> ',';').split(';').each {
                def (x, y) = it.trim().split(',').collect { it as int }
                points << new Point(x, y)
                if (x < xMin) xMin = x
                if (x > xMax) xMax = x
                if (y > yMax) yMax = y
            }

            lines << points
        }

        lines.each { points ->
            def p // previous point
            points.each {
                if (!p) {
                    setRock(it.x, it.y)
                } else {
                    if (it.x == p.x) {
                        for(i in it.y..<p.y) {
                            setRock(it.x, i)
                        }
                    } else {
                        // it.y == p.y
                        for(i in it.x..<p.x) {
                            setRock(i, it.y)
                        }
                    }
                }
                p = it
            }
        }
    }

    def setRock(x, y) {
        cave["$x.$y"] = 2 // 2 = rock
    }

    def setSand(x, y) {
        // println "sand lands at $x,$y"
        cave["$x.$y"] = 1 // 1 = sand
    }

    def isEmpty1(x, y) {
        cave["$x.$y"] == null
    }

    // part 1: dropSand returns true iff sand finds a final position
    def dropSand1() {

        def x = 500
        def y = 0
        boolean finalPosFound = false // true if sand finds final position

        while (!finalPosFound && x >= xMin && x <= xMax && y <= yMax) {

            if (y+1 > yMax || isEmpty1(x, y+1)) {
                y++
            } else if (x-1 < xMin || y+1 > yMax || isEmpty1(x-1, y+1)) {
                y++
                x--
            } else if (x+1 > xMax || y+1 > yMax || isEmpty1(x+1, y+1)) {
                x++
                y++
            } else {
                setSand(x, y)
                finalPosFound = true
            }
        }

        finalPosFound
    }

    def solve1() {

        int i = 0
        def finalPosFound = true

        while (finalPosFound) {
            finalPosFound = dropSand1()
            if (finalPosFound) i++
        }

        i
    }

    def isEmpty2(x, y) {
        // layer yMax + 2 is completely filled with rocks
        y < yMax + 2 && cave["$x.$y"] == null
    }

    // part 2: dropSand returns true iff sand finds a final position
    def dropSand2() {

        def x = 500
        def y = 0

        while (true) {

            if (isEmpty2(x, y+1)) {
                y++
            } else if (isEmpty2(x-1, y+1)) {
                y++
                x--
            } else if (isEmpty2(x+1, y+1)) {
                x++
                y++
            } else {
                setSand(x, y)
                break
            }
        }

        [x, y]
    }

    def solve2() {

        int i = 0

        def coords = [0, 0]

        while (coords != [500, 0]) {
            coords = dropSand2()
            i++
        }

        i
    }
}

//-----------------------------------------------
// Test
 
def input = '''\
498,4 -> 498,6 -> 496,6
503,4 -> 502,4 -> 502,9 -> 494,9
'''

def rr = new RegolithReservoir(input)
assert rr.solve1() == 24

rr = new RegolithReservoir(input)
assert rr.solve2() == 93

println 'Test ok'

//-----------------------------------------------
// Puzzle

input = Puzzle.getInput(2022, 14)

rr = new RegolithReservoir(input)
def result1 = rr.solve1()

rr = new RegolithReservoir(input)
def result2 = rr.solve2()

Puzzle.printResults result1, result2