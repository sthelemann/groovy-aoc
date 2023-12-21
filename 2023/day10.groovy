//===============================================================
// Advent of Code 2023, Day 10: Pipe Maze
//===============================================================

class PipeMaze {

    def maze = []
    def loop = []
    def xMax
    def yMax
    def startPoint
    def startPipe

    def PipeMaze(input) {

        int y = 0
        input.eachLine { line ->
            if (!xMax) xMax = line.size() - 1
            def x = line.indexOf('S')
            if (x >= 0) {
                startPoint = new Point(x, y)
            }
            maze << line.split('')
            y++
        }
        yMax = y - 1

        this.findLoop()
    }

    def findLoop() {

        loop = [startPoint]

        def next = findNext(loop)

        while (next != startPoint) {
            loop << next
            next = findNext(loop)
        }

        // determine pipe type for start point
        // find connection directions

        def xDir = [(-1) : 'w', (1): 'e']
        def yDir = [(-1) : 'n', (1): 's']

        def directions = []

        [loop[1], loop.last()].each { neighbor ->
            def dx = neighbor.x - startPoint.x 
            def dy = neighbor.y - startPoint.y
            directions << (dx != 0 ? xDir[dx] : yDir[dy])
        }

        if ('n' in directions && 'e' in directions) startPipe = 'L'
        else if ('n' in directions && 'w' in directions) startPipe = 'J'
        else if ('n' in directions && 's' in directions) startPipe = '|'
        else if ('w' in directions && 'e' in directions) startPipe = '-'
        else if ('s' in directions && 'w' in directions) startPipe = '7'
        else if ('s' in directions && 'e' in directions) startPipe = 'F'

        // set start pipe in maze for starting point

        maze[startPoint.y][startPoint.x] = startPipe

        // replace all pipes that are not in the loop with x

        for (y in 0..yMax) {
            for (x in 0..xMax) {
                def c = maze[y][x]
                if (c != '.') {
                    def p = new Point(x, y)
                    if (p !in loop) {
                        maze[y][x] = 'x'
                    }
                }
            }
        }
    }

    def findNext(visited) {

        def p = visited.last()
        def pipe = maze[p.y][p.x]
        // we must not go back to predecessor of p (if that predecessor exists, i.e. visited.size() > 1)
        def predecessor = (visited.size() > 1 ? visited[-2] : null)
 
        def next = null

        if (pipe in ['S', '|', 'L', 'J'] && p.y - 1 >= 0) {
            // pipes S, |, J, 7 allow to go north (S is kind of a joker)
            // neighbors are pipes with connection to south
            if (maze[p.y - 1][p.x] in ['S', '|', 'F', '7']) {
                next = new Point(p.x, p.y - 1)
                if (next == predecessor) next = null
            }
        }

        if (!next && pipe in ['S', '|', '7', 'F'] && p.y + 1 <= yMax) {
            // pipes S, |, 7, F allow to go south
            // neighbors are pipes with connection to north or S
            if (maze[p.y + 1][p.x] in ['S', '|', 'L', 'J']) {
                next = new Point(p.x, p.y + 1)
            }
            if (next == predecessor) next = null
        }

        if (!next && pipe in ['S', '-', 'J', '7'] && p.x - 1 >= 0) {
            // pipes S, -, J, 7 allow to go west
            // neighbors are pipes with connection to east
            if (maze[p.y][p.x - 1] in ['S', '-', 'L', 'F']) {
                next = new Point(p.x - 1, p.y)
            }
            // if next is the start we have closed the loop, otherwise do not go back to already visited pipe
            if (next == predecessor) next = null
        }

        if (!next && pipe in ['S', '-', 'L', 'F'] && p.x + 1 <= xMax) {
            // pipes S, -, L, F allow to go east
            // neighbors are pipes with connection to west
            if (maze[p.y][p.x + 1] in ['S', '-', 'J', '7']) {
                next = new Point(p.x + 1, p.y)
            }
            if (next == predecessor) next = null
        }

        next
    }

    def findInnerTiles() {

        def connectedPipes = ['LJ', 'L7', 'L_', 'FJ', 'F7', 'F_', '_J', '_7', '__']

        // replace 'S' by startPipe
        maze[startPoint.y][startPoint.x] = startPipe

        // step linewise through the maze, start with state 'out' (other state is 'in')
        // if you cross the path then toggle state, otherwise keep state
        // if you are on a non-path node then increase the counter n if current state is 'in'

        def n = 0

        for (y in 0..yMax) {

            def state = 'out'
            def from = null
            def to = null

            for (x in 0..xMax) {
                def c = maze[y][x]
                if (c == '.' || c == 'x') {
                    if (state == 'in') n++
                } else {
                    def p = new Point(x, y)
                    if (p in loop) {
                        if (c == '|') {
                            from = 'n'
                            to = 's'
                        } else if (from ==  null) {
                            if (c in ['L', 'J']) {
                                from = 'n' // path comes from north
                            } else if (c in ['7', 'F']) {
                                from = 's' // path comes from south
                            }
                        } else if (to == null) {
                            if (c in ['L', 'J']) {
                                to = 'n' // path goes to north
                            } else if (c in ['7', 'F']) {
                                to = 's' // path goes to south
                            }                            
                        }
                        if (from && to) {
                            if (from != to) {
                                // the path has been crossed
                                state = (state == 'out' ? 'in' : 'out')
                            }
                            from = null
                            to = null
                        }
                    }

                }
            }
        }

        n
    }

}

def getResult(input, part) {
    def pm = new PipeMaze(input)
    if (part == 1) {
        pm.loop.size().intdiv(2)
    } else {
       pm.findInnerTiles() 
    }
}

//-----------------------------------------------
// Test

def testCases = [
    [id: 1, part: 1, input: '''\
.....
.S-7.
.|.|.
.L-J.
.....''', result: 4],

    [id: 2, part: 1, input: '''\
7-F7-
.FJ|7
SJLL7
|F--J
LJ.LJ''', result: 8],

    [id: 3, part: 2, input: '''\
...........
.S-------7.
.|F-----7|.
.||.....||.
.||.....||.
.|L-7.F-J|.
.|..|.|..|.
.L--J.L--J.
...........''', result: 4],

    [id: 4, part: 2, input: '''\
.F----7F7F7F7F-7....
.|F--7||||||||FJ....
.||.FJ||||||||L7....
FJL7L7LJLJ||LJ.L-7..
L--J.L7...LJS7F-7L7.
....F-J..F7FJ|L7L7L7
....L7.F7||L7|.L7L7|
.....|FJLJ|FJ|F7|.LJ
....FJL-7.||.||||...
....L---J.LJ.LJLJ...''', result: 8],

    [id: 5, part: 2, input: '''\
FF7FSF7F7F7F7F7F---7
L|LJ||||||||||||F--J
FL-7LJLJ||||||LJL-77
F--JF--7||LJLJ7F7FJ-
L---JF-JLJ.||-FJLJJ7
|F|F-JF---7F7-L7L|7|
|FFJF7L7F-JF7|JL---7
7-L-JL7||F7|L7F-7F7|
L.L7LFJ|||||FJL7||LJ
L7JLJL-JLJLJL--JLJ.L''', result: 10]    
]

testCases.each {
    println "TEST $it.id"
    assert getResult(it.input, it.part) == it.result
}

println 'Test ok'

//-----------------------------------------------
// Puzzle

input = Puzzle.getInput(2023, 10)

Puzzle.printResults getResult(input, 1), getResult(input, 2)
