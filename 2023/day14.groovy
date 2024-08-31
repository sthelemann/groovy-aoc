//===============================================================
// Advent of Code 2023, Day 14: Parabolic Reflector Dish
//===============================================================

def stringToDish(s) {
    dish = []
    s.eachLine { row ->
        dish << row.toCharArray()
    }
    dish
}

def dishToString(dish) {
    dish.collect { new String(it)}.join('\n')
}

def tiltNorth(dish) {

    dish.eachWithIndex { row, i ->  
        row.eachWithIndex { content, j ->
            if (content == 'O') {
                int k = i-1
                for (; k >= 0; k--) {
                    if (dish[k][j] != '.') break
                }
                if (k+1 != i) {
                    dish[k+1][j] = 'O'
                    row[j] = '.'
                }
            }
        }
    }

    dish
}

def tiltSouth(dish) {

    def height = dish.size()

    for (int i = height - 1; i >= 0; i--) {

        def row = dish[i]

        row.eachWithIndex { content, j ->
            if (content == 'O') {
                int k = i+1
                for (; k < height; k++) {
                    if (dish[k][j] != '.') break
                }
                if (k-1 != i) {
                    dish[k-1][j] = 'O'
                    row[j] = '.'
                }
            }
        }
    }

    dish
}

def tiltWest(dish) {

    dish.each { row ->
        row.eachWithIndex { content, j ->
            if (content == 'O') {
                int k = j-1
                for (; k >= 0; k--) {
                    if (row[k] != '.') break
                }
                if (k+1 != j) {
                    row[k+1] = 'O'
                    row[j] = '.'
                }
            }
        }
    }

    dish
}

def tiltEast(dish) {

    def width = dish[0].size()

    dish.each { row ->
        for (int j = width - 1; j >= 0; j--) {
            if (row[j] == 'O') {
                int k = j+1
                for (; k < width; k++) {
                    if (row[k] != '.') break
                }
                if (k-1 != j) {
                    row[k-1] = 'O'
                    row[j] = '.'
                }
            }
        }
    }

    dish
}

def tiltCycle(dish) {

    tiltEast(tiltSouth(tiltWest(tiltNorth(dish))))
}

def getTotalLoad(dish) {

    def height = dish.size()
    def result = 0

    dish.eachWithIndex { row, i ->
        row.each { content ->
            if (content == 'O') {
                result += height - i
            }
        }
    }

    result
}

def part1(input) {
    def dish = stringToDish(input)
    getTotalLoad(tiltNorth(dish))
}

def part2(input) {

    def dish = stringToDish(input)

    int i = 0
    int k = -1
    def history = []

    while (k < 0 ) {
        dish = tiltCycle(dish)
        def s = dishToString(dish)
        k = history.indexOf(s)
        if (k < 0) {
            history << s
            i++
        }
    }

    // cycle starts at index k, has length i-k
    // find m and 0 <= r < (i-k) with k + m*(i-k) + r = 1000000000
    // take element with index k + r - 1 in history and compute total load
    // (-1 because index starts at 0)

    def cycles = 1000000000 as BigInteger

    getTotalLoad(stringToDish(history[k + (cycles - k) % (i-k) - 1]))
}

//-----------------------------------------------
// Test

def input = '''\
O....#....
O.OO#....#
.....##...
OO.#O....O
.O.....O#.
O.#..O.#.#
..O..#O..O
.......O..
#....###..
#OO..#....'''

assert part1(input) == 136
assert part2(input) == 64

println 'Test ok'

//-----------------------------------------------
// Puzzle

input = Puzzle.getInput(2023, 14)

Puzzle.printResults part1(input), part2(input)
//Puzzle.printResults part1(input)
