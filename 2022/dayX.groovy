//===============================================================
// Advent of Code 2022, Day X:
//===============================================================

def part1(input) {
}

def part2(input) {
}

//-----------------------------------------------
// Test

def testCases = [
    [id: 1],
    [id: 2],
    [id: 3]
]

testCases.each {
    println "TEST $it.id"
    assert 1 == 1
}

def input = '''
'''
assert part1(input) == 24
assert part2(input) == 42

println 'Test ok'

//-----------------------------------------------
// Puzzle

input = Puzzle.getInput(2022, X)

Puzzle.printResults part1(input), part2(input)
