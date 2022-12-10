//===============================================================
// Advent of Code 2022, Day 9: Rope Bridge
//===============================================================

class RopeBridge {

    def ropes = [
        [new Knot('H'), new Knot('T')],
        [new Knot('H'), new Knot('1'),  new Knot('2'), new Knot('3'), new Knot('4'), 
         new Knot('5'), new Knot('6'), new Knot('7'), new Knot('8'), new Knot('9')]
    ]

    def moves = [R: new Point(1, 0), L: new Point(-1, 0), U: new Point(0, 1), D: new Point(0, -1)]

    def move(rope, direction, visited) {
        
        // move head
        def knot = rope[0]
        knot.add(moves[direction])

        def nextKnot

        for (i in 1..rope.size()-1) {

            nextKnot = rope[i]

            def xOld = nextKnot.x
            def yOld = nextKnot.y

            // move next
            
            def xDiff = knot.x - nextKnot.x
            def yDiff = knot.y - nextKnot.y
            
            if (Math.abs(xDiff) > 1 || Math.abs(yDiff) > 1) {

                if (Math.abs(xDiff) != 0) {
                    nextKnot.x += (xDiff > 0 ? 1 : -1)
                }
                
                if (Math.abs(yDiff) != 0) {
                    nextKnot.y += (yDiff > 0 ? 1 : -1)
                }
            }

            knot = nextKnot
        }

        visited << "${rope.last().x},${rope.last().y}"
    }

    def solve(input, part) {
        
        def rope = ropes[part - 1]

        // reset knots to have them all at (0, 0)
        for (knot in rope) {
            knot.x = 0
            knot.y = 0
        }

        // points visited by tail
        Set visited = []

        input.eachLine { line ->
            def tokens = line.split(' ')
            def direction = tokens[0]
            def steps = (tokens[1] as int)

            for (i in 1..steps) {
                move rope, direction, visited
            }

            // show rope
        }

        visited.size()
    }

    def show(rope) {
        println(rope.collect {"$it.name:$it.x,$it.y" }.join(" | "))
    }
}

class Knot extends Point {

    def name

    Knot(name) {
        super(0, 0)
        this.name = name
    }

    def add(Point p) {
        this.x += p.X
        this.y += p.y
    }
}

//-----------------------------------------------
// Test

def input1 = '''R 4
U 4
L 3
D 1
R 4
D 1
L 5
R 2'''

def input2 = '''R 5
U 8
L 8
D 3
R 17
D 10
L 25
U 20'''

def testCases = [
    [id: 1, input: input1, part: 1, result: 13],
    [id: 2, input: input1, part: 2, result: 1],
    [id: 3, input: input2, part: 2, result: 36]
]

rb = new RopeBridge()

testCases.each {
    println "TEST $it.id"
    assert rb.solve(it.input, it.part) == it.result
}

println 'Test ok'

//-----------------------------------------------
// Puzzle

input = Puzzle.getInput(2022, 9)

rb = new RopeBridge()

Puzzle.printResults rb.solve(input, 1), rb.solve(input, 2)
