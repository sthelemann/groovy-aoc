//===============================================================
// Advent of Code 2022, Day 6: Tuning Trouble
//===============================================================

def solve(input, part) {

    def n = (part == 1 ? 4 : 14)

    println "part = $part, n = $n"

    def testList = input.take(n).toList()

    def i = n-1
    while (testList.toSet().size() < n) {
        i++
        testList = testList.drop(1) + input[i]
    }

    i+1
}

//-----------------------------------------------
// Test

def testCases = [
    [id: 1, input: 'mjqjpqmgbljsphdztnvjfqwrcgsmlb', result1: 7, result2: 19],
    [id: 2, input: 'bvwbjplbgvbhsrlpgdmjqwftvncz', result1: 5, result2: 23],
    [id: 3, input: 'nppdvjthqldpwncqszvftbrmjlhg', result1: 6, result2: 23],
    [id: 4, input: 'nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg', result1: 10,  result2: 29],
    [id: 5, input: 'zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw', result1: 11,  result2: 26]
]

testCases.each {
    println "TEST $it.id"
    assert solve(it.input, 1) == it.result1
    assert solve(it.input, 2) == it.result2
}

println 'Test ok'

//-----------------------------------------------
// Puzzle

input = Puzzle.getInput(2022, 6)

Puzzle.printResults solve(input, 1), solve(input, 2)
