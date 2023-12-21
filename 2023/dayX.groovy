//===============================================================
// Advent of Code 2023, Day X:
//===============================================================

def part1(input) {
}

def part2(input) {
}

//-----------------------------------------------
// Test

def testCases = [
    [id: 1, input: '''\
''', result: ?],
    [id: 2, input: '''\
''', result: ?] ,
    [id: 3, input: '''\
''', result: ?]
]

testCases.each {
    println "TEST $it.id"
    assert part1(it.input) == it.result
}

def input = '''\
'''
assert part1(input) == 24
//assert part2(input) == 42

println 'Test ok'

//-----------------------------------------------
// Puzzle

input = Puzzle.getInput(2023, X)

//Puzzle.printResults part1(input), part2(input)
Puzzle.printResults part1(input)
