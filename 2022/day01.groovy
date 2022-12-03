//===============================================================
// Advent of Code 2022, Day 1: Calorie Counting
//===============================================================

def solve(input, part) {

    def calories = []
    def sum = 0

    input.eachLine {
        if (it) {
            // non-empty line
            sum += (it as int)
        } else {
            // empty line
            calories << sum
            sum = 0
        }
    }

    // last elf
    calories << sum

    if (part == 1) {
        calories.max()
    } else {
        calories.sort {a,b -> b <=> a}[0..2].sum()
    }
}

//-----------------------------------------------
// Test

def input = '''1000
2000
3000

4000

5000
6000

7000
8000
9000

10000'''

def result = solve(input, 1)
assert result == 24000

result = solve(input, 2)
assert result == 45000

println 'Test ok'

//-----------------------------------------------
// Puzzle

input = Puzzle.getInput(1)

Puzzle.printResults solve(input, 1), solve(input, 2)
