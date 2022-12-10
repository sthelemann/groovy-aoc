//===============================================================
// Advent of Code 2022, Day 10: Cathode-Ray Tube
//===============================================================

class CathodeRayTube {

    // register X
    def x = 1

    // for part 1
    def cycles = [20, 60, 100, 140, 180, 220]
    def sum = 0

    // for part 2
    def newLineCycles = [40, 80, 120, 160, 200, 240]
    def line = ''
    def lines = []

    def reset() {
        x = 1
        sum = 0
    }

    def execute(input) {

        def cycle = 1

        input.eachLine {

            if (it == 'noop') {
               checkCycle(cycle)
               cycle++ 
            } else {
                // it starts with addx
                def n = (it.split(' ')[1] as int)
                checkCycle(cycle)
                cycle++ 
                checkCycle(cycle)
                x += n
                cycle++ 
            }
        }
    }

    def checkCycle(cycle) {
        if (cycle in cycles) {
            sum += cycle * x
        }
    }

    def draw(input) {

        def cycle = 1
        def position = 0
        def line = ''

        input.eachLine {

            if (it == 'noop') {
               drawPixel(position)
               (cycle, position) = incrCycle(cycle, position)
            } else {
                // it starts with addx
                def n = (it.split(' ')[1] as int)
                drawPixel(position)
                (cycle, position) = incrCycle(cycle, position)
                drawPixel(position)
                (cycle, position) = incrCycle(cycle, position)
                x += n
            }
        }

        lines.join('\n')
    }

    def incrCycle(cycle, position) {
        if (cycle in newLineCycles) {
            // flush line
            lines << line
            line = ''
            position = 0
        } else {
            position++
        }
        cycle++
        [cycle, position]
    }

    def drawPixel(position) {
        line += (Math.abs(position - x) <= 1 ? '#' : '.')
    }
}

//-----------------------------------------------
// Test

def crt = new CathodeRayTube()

def input = new File("./2022/input", "day10_test.txt").text

crt.execute(input)

assert crt.sum == 13140

crt.reset()
def image = crt.draw(input)

assert image == '''##..##..##..##..##..##..##..##..##..##..
###...###...###...###...###...###...###.
####....####....####....####....####....
#####.....#####.....#####.....#####.....
######......######......######......####
#######.......#######.......#######.....'''

println 'Test ok'

//-----------------------------------------------
// Puzzle

crt.reset()

input = Puzzle.getInput(2022, 10)

crt.execute(input)
def sum = crt.sum

println "Result 1: $crt.sum"

crt.reset()
image = crt.draw(input)

println """Result 2:
$image"""
