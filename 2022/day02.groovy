//===============================================================
// Advent of Code 2022, Day 2: Rock Paper Scissors
//===============================================================

class RockPaperScissors {

    def scores = [
        [
            'A X': 1 + 3, 'A Y': 2 + 6, 'A Z': 3 + 0,
            'B X': 1 + 0, 'B Y': 2 + 3, 'B Z': 3 + 6,
            'C X': 1 + 6, 'C Y': 2 + 0, 'C Z': 3 + 3
        ],
        [
            'A X': 3 + 0, 'A Y': 1 + 3, 'A Z': 2 + 6,
            'B X': 1 + 0, 'B Y': 2 + 3, 'B Z': 3 + 6,
            'C X': 2 + 0, 'C Y': 3 + 3, 'C Z': 1 + 6
        ]
    ]

    def solve(input, i) {

        def sum = 0
        input.eachLine {
            sum += scores[i-1][it]
        }
        sum
    }
}

//-----------------------------------------------
// Test

def rps = new RockPaperScissors()

def input = '''A Y
B X
C Z'''

assert rps.solve(input, 1) == 15
assert rps.solve(input, 2) == 12

println 'Test ok'

//-----------------------------------------------
// Puzzle

input = Puzzle.getInput(2022, 2)

Puzzle.printResults rps.solve(input, 1), rps.solve(input, 2)
