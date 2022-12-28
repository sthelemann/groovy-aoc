//===============================================================
// Advent of Code 2022, Day 12: Hill Climbing Algorithm
//===============================================================

class HillPoint extends Point {

    def height
    List<HillPoint> neighbors = []
    HillPoint pred // predecessor in shortestPath
    def length // length of shortest path

    HillPoint(x, y, h) {
        super(x,y)
        height =  ((h as char) as int)
    }
}

class HillClimbingAlgorithm {

    HillPoint start
    HillPoint end
    List<HillPoint> allPoints = []
    boolean debug = false

    HillClimbingAlgorithm(input) {

        int xMax
        int yMax

        def lines = []
        input.eachLine { lines << it }
        yMax = lines.size() - 1 // rows (y axis)
        xMax = lines[0].size() - 1 // columns (x axis)

        lines.eachWithIndex { line, y ->
            line.eachWithIndex { h, x ->
                def p = new HillPoint(x, y, h)
                allPoints << p
                if (h == 'S') {
                    start = p
                    start.height = ('a' as char) as int
                } else if (h == 'E') {
                    end = p
                    end.height = ('z' as char) as int
                }
                if (x > 0) {
                    def pred = allPoints.find {it.x == x-1 && it.y == y}
                    p.neighbors << pred
                    pred.neighbors << p
                }
                if (y > 0) {
                    def pred = allPoints.find {it.x == x && it.y == y-1}
                    p.neighbors << pred
                    pred.neighbors << p
                }
            }
        }
    }

    def reset() {
        allPoints.each {
            it.length = null
            it.pred = null
        }
        start.length = 0
    }

    def findShortestPath(start) {

        this.start = start

        reset()

        def points = [start]
        def visitedPoints = [start]

        while (!points.isEmpty() && end !in points) {

            def nextPoints = []

            points.each { p ->

                p.neighbors.findAll {it !in visitedPoints && it.height - p.height <= 1}.each { q ->

                    // check all neighbors of q for shortest path to q
                    def minPoint =  null
                    q.neighbors.findAll {it in visitedPoints && q.height - it.height <= 1}.each { neighbor ->
                        if (!minPoint || neighbor.length < minPoint.length) {
                            minPoint = neighbor
                        }
                    }
                    if (minPoint) {
                        q.pred = minPoint
                        q.length = minPoint.length + 1
                        visitedPoints << q
                        nextPoints << q
                    }
                }
            }

            points = nextPoints
        }

        if (debug) {
            def path = []
            def p = end
            while (p) {
                path << p
                p = p.pred
            }
            println path.reverse().collect { "$it.x,$it.y|$it.length" }.join(' - ')
            println "length: $end.length\n----------"
        }
        end.length
    }

    def findShortestPathPart1() {

        findShortestPath(start)
    }

    def findShortestPathPart2() {
        // this implementation is very slow - better with multithreading?
        println "findShortestPathPart2..."
        allPoints.findAll { it.height == (('a' as char) as int) }.collect { findShortestPath(it) }.min()
    }

    def shortestPathDebug(xStart, yStart) {
        def start = allPoints.find { it.x == xStart && it.y == yStart}
        findShortestPath(start)
        def path = []
        def p = end
        while (p) {
            path << p
            p = p.pred
        }
        println(path.reverse().collect { "$it.x,$it.y" }.join(' - '))
    }
}

//-----------------------------------------------
// Test

def input = '''\
Sabqponm
abcryxxl
accszExk
acctuvwj
abdefghi'''

def hca = new HillClimbingAlgorithm(input)

assert hca.findShortestPathPart1() == 31
assert hca.findShortestPathPart2() == 29

println 'Test ok'

//-----------------------------------------------
// Puzzle

input = Puzzle.getInput(2022, 12)

hca = new HillClimbingAlgorithm(input)

// Puzzle.printResults hca.findShortestPathPart1(), hca.findShortestPathPart2()
