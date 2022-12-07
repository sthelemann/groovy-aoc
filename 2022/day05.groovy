//===============================================================
// Advent of Code 2022, Day 5: Supply Stacks
//===============================================================

class SupplyStacks {

    def stacks = [:]

    def moves = []

    SupplyStacks(input) {

        def mode = 'stack'

        input.eachLine { line ->

            if (mode == 'stack') {

                if (line.contains('[')) {

                    int i = 1

                    while (line) {
                        def crate = line[1]
                        if (crate != ' ') {
                            def stack = stacks[i as String] ?: []
                            if (stack.isEmpty()) {
                                stacks[i as String] = stack
                            }
                            stack << crate
                        }
                        line = line.drop(4)
                        i++
                    }

                } else {
                    mode = 'moves'
                }

            } else {
                // mode == 'moves'
                if (line) {
                    line.find(/move (\d+) from (\d) to (\d)/) { match, qty, from, to ->
                        moves << [qty: qty as int, from: from, to: to]
                    }
                }
            }
        }
    }

    def move(part) {

        moves.each {
            if (part == 1) {
                stacks[it.to] = stacks[it.from].take(it.qty).reverse() + stacks[it.to]
            } else {
                stacks[it.to] = stacks[it.from].take(it.qty) + stacks[it.to]
            }
            stacks[it.from] = stacks[it.from].drop(it.qty)
        }

        this
    }

    def getTopContainers() {

        def result = ''
        for (i in 1..stacks.size()) {
            def stack = stacks[i as String]
            if (!stack.isEmpty()) {
                result += stack[0]
            }
        }

        result
    }
}

//-----------------------------------------------
// Test

def input = '''    [D]
[N] [C]
[Z] [M] [P]
 1   2   3

move 1 from 2 to 1
move 3 from 1 to 3
move 2 from 2 to 1
move 1 from 1 to 2'''

def s = new SupplyStacks(input)
assert s.move(1).getTopContainers() == 'CMZ'

s = new SupplyStacks(input)
assert s.move(2).getTopContainers() == 'MCD'

println 'Test ok'

//-----------------------------------------------
// Puzzle

input = Puzzle.getInput(2022, 5)

s = new SupplyStacks(input)
def result1 = s.move(1).getTopContainers()

s = new SupplyStacks(input)
def result2 = s.move(2).getTopContainers()

println result1
println result2

Puzzle.printResults result1, result2
