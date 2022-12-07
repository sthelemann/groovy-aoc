//===============================================================
// Advent of Code 2022, Day 4: Camp Cleanup
//===============================================================

def solve(input, part) {

    int n = 0
    input.eachLine {
    
        it.find(/(\d+)-(\d+),(\d+)-(\d+)/) { match, s1, t1, s2, t2 ->

            int x1 = s1 as int
            int y1 = t1 as int
            int x2 = s2 as int
            int y2 = t2 as int
        
            if (part == 1) {
                if ((x1 <= x2 && y1 >= y2) || (x1 >= x2 && y1 <= y2)) n++
            } else {
                // if not disjoint
                if (!(x2 > y1 || y2 < x1 || x1 > y2 || y1 < x2)) n++
            }
        }

        n
    }
}

//-----------------------------------------------
// Test

def input = '''4,6-8
2-3,4-5
5-7,7-9
2-8,3-7
6-6,4-6
2-6,4-8'''

assert solve(input, 1) == 2
assert solve(input, 2) == 4

println 'Test ok'

//-----------------------------------------------
// Puzzle

input = Puzzle.getInput(2022, 4)

Puzzle.printResults solve(input, 1), solve(input, 2)
