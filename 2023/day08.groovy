//===============================================================
// Advent of Code 2023, Day 8: Haunted Wasteland
//===============================================================

class HauntedWasteland {

    def part
    def steps
    def map = [:]

    def HauntedWasteland(input, part) {

        this.part = part

        def firstLine = true

        input.eachLine { line ->
            if (firstLine) {
                this.steps = line.split('')
                firstLine = false
            } else if (line) {

                line.find(/(...) = \((...), (...)\)/) { match, node, left, right ->
                    this.map[node] = [L: left, R: right]
                }
            }
        }
    }

    def getNumberOfSteps() {

        BigInteger result // number of steps

        if (this.part == 1) {

            def node = 'AAA' // start node
            def stepsSize = steps.size()
            def i = 0
            result = 0

            while (node != 'ZZZ') {
                node = map[node][steps[i]]
                result++
                i++
                if (i == stepsSize) i = 0
            }

        } else if (this.part == 2) {

            // Based on results from method analysis:
            // Visiting a Z node happens periodically
            // => collect period lengths and find their least common multiple

            def nodes = this.map.keySet().findAll { it[2] == 'A' }  // start nodes
            def stepsSize = steps.size()
            def periods = []

            nodes.each { node ->

                def found = false
                def i = 0
                def n = 0

                def next = node

                while (!found) {
                    n++
                    next = map[next][steps[i]]
                    if (next[2] == 'Z') {
                        periods << n
                        found = true
                    }

                    i++
                    if (i == stepsSize) i = 0
                }
            }

            result = leastCommonMultiple(periods)
        }

        result
    }

    def leastCommonMultiple(nList) {

        def primeFactors = []

        nList.each { n ->
            getPrimeFactors(n).each { p ->
                if (p !in primeFactors) {
                    primeFactors << p
                }
            }
        }

        BigInteger result = 1
        primeFactors.each { p ->
            result *= p
        }

        result
    }

    def getPrimeFactors(n) {

        def primeFactors = []
        def pMax = Math.sqrt(n) as int

        for (int p = 2; p <= pMax; p++) {
            if (n > 1 && n % p == 0) {
                n = n.intdiv(p)
                primeFactors << p
            }
        }

        if (n > 1) {
            primeFactors << n
        }

        primeFactors
    }

    def analysis() {

        // starting at any A node
        // which Z nodes are reached after how may steps

        def nodes = this.map.keySet().findAll { it[2] == 'A' }  // start nodes
        def neededCount = nodes.size()
        def stepsSize = steps.size()

        nodes.each { node ->

            def nodesVisited = []
            def i = 0
            def n = 0
            def nBefore = 0
            def next = node

            while (nodesVisited.size() < 4) {
                n++
                next = map[next][steps[i]]
                if (next[2] == 'Z') {
                    nodesVisited << [node: next, n: n, diff: n-nBefore]
                    nBefore = n
                }

                i++
                if (i == stepsSize) i = 0
            }

            println "$node -> $nodesVisited"
        }

        // Result for test case 3:
        // 11A -> [[node:11Z, n:2, diff:2], [node:11Z, n:4, diff:2], [node:11Z, n:6, diff:2], [node:11Z, n:8, diff:2]]
        // 22A -> [[node:22Z, n:3, diff:3], [node:22Z, n:6, diff:3], [node:22Z, n:9, diff:3], [node:22Z, n:12, diff:3]]

        // Result for input data:
        // DVA -> [[node:QVZ, n:11309, diff:11309], [node:QVZ, n:22618, diff:11309], [node:QVZ, n:33927, diff:11309], [node:QVZ, n:45236, diff:11309]]
        // JQA -> [[node:TQZ, n:19199, diff:19199], [node:TQZ, n:38398, diff:19199], [node:TQZ, n:57597, diff:19199], [node:TQZ, n:76796, diff:19199]]
        // PTA -> [[node:JXZ, n:12361, diff:12361], [node:JXZ, n:24722, diff:12361], [node:JXZ, n:37083, diff:12361], [node:JXZ, n:49444, diff:12361]]
        // CRA -> [[node:QTZ, n:16043, diff:16043], [node:QTZ, n:32086, diff:16043], [node:QTZ, n:48129, diff:16043], [node:QTZ, n:64172, diff:16043]]
        // AAA -> [[node:ZZZ, n:13939, diff:13939], [node:ZZZ, n:27878, diff:13939], [node:ZZZ, n:41817, diff:13939], [node:ZZZ, n:55756, diff:13939]]
        // BGA -> [[node:QGZ, n:18673, diff:18673], [node:QGZ, n:37346, diff:18673], [node:QGZ, n:56019, diff:18673], [node:QGZ, n:74692, diff:18673]]
    }
}

def getResult(input, part) {

    def hw = new HauntedWasteland(input, part)

    if (part == 2) {
        hw.analysis()
    }

    hw.getNumberOfSteps()
}

//-----------------------------------------------
// Test

def testCases = [
    [id: 1, input: '''\
RL

AAA = (BBB, CCC)
BBB = (DDD, EEE)
CCC = (ZZZ, GGG)
DDD = (DDD, DDD)
EEE = (EEE, EEE)
GGG = (GGG, GGG)
ZZZ = (ZZZ, ZZZ)''', part: 1, result: 2],

    [id: 2, input: '''\
LLR

AAA = (BBB, BBB)
BBB = (AAA, ZZZ)
ZZZ = (ZZZ, ZZZ)''', part: 1, result: 6],

    [id: 3, input: '''\
LR

11A = (11B, XXX)
11B = (XXX, 11Z)
11Z = (11B, XXX)
22A = (22B, XXX)
22B = (22C, 22C)
22C = (22Z, 22Z)
22Z = (22B, 22B)
XXX = (XXX, XXX)''', part: 2, result: 6]
]

testCases.each {
    println "TEST $it.id"
    assert getResult(it.input, it.part) == it.result
}

println 'Test ok'

//-----------------------------------------------
// Puzzle

input = Puzzle.getInput(2023, 8)

Puzzle.printResults getResult(input, 1), getResult(input, 2)
