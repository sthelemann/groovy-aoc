//===============================================================
// Advent of Code 2022, Day 15: Beacon Exclusion Zone
//===============================================================

class Sensor extends Point {
    Point beacon
    int distance

    Sensor(x, y, b) {
        super(x, y)
        this.beacon = b
        this.distance = Math.abs(b.x - x) + Math.abs(b.y - y)
    }
}

class BeaconExclusionZone {

    def sensors = []
    def beacons = []

    BeaconExclusionZone(input) {
        input.eachLine { 
            it.find(/Sensor at x=(.+), y=(.+): closest beacon is at x=(.+), y=(.+)/) { match, sx, sy, bx, by ->
                def b = new Point(bx as int, by as int)
                // different sensors can see the same beacon
                if (b !in beacons) beacons << b
                def s = new Sensor(sx as int, sy as int, b)
                sensors << s
            }
        }
    }

    // find number of scanned positions in row y
    def findScannedIntervals(y) {

        def intervals = []

        sensors.each { s->
            def d = s.distance - Math.abs(s.y - y) 
            if (d >= 0) {
                intervals += [from: s.x-d, to: s.x+d]
            }
        }

        // merge intervals

        def mergedIntervals = []
        boolean first = true

        intervals.sort { it.from }.each {
            if (first) {
                mergedIntervals << it
                first = false
            } else {
                def pred = mergedIntervals.last() // predecessor interval
                if (it.from > pred.to) {
                    mergedIntervals << it
                } else {
                    if (it.to > pred.to) {
                        pred.to = it.to
                    }
                }
            }
        }

        mergedIntervals
    }

    def solve1(y) {

        def intervals = findScannedIntervals(y) // find scanned intervals in row y

        // count positions of beacons in intervals
        def count = 0
        beacons.findAll { it.y == y }.each {
            // println "beacon: $it"
            for (interval in intervals) {
                // println "interval $interval"
                if (interval.from <= it.x && it.x <= interval.to) {
                    count++
                    break
                }
            }
        }

        intervals.collect {it.to - it.from + 1}.sum() - count
    }

    def subtract(unscanned, interval) {

        def result = []

        unscanned.each {
            if (interval.from > it.to || interval.to < it.from) {
                // no intersection
                result << it
            // } else if (unscanned.from >= interval.from && unscanned.from <= interval.to) {
            //     // intersection
            //     if (interval.to < unscanned.to) {
            //         result << [from: interval.to + 1, to: unscanned.to]
            //     }
            // } else if (unscanned.to >= interval.from && unscanned.to <= interval.to) {
            //     // intersection
            //     if (unscanned.from < interval.from) {
            //         result << [from: unscanned.from, to: interval.from - 1]
            //     }
            // } else if (unscanned.from < interval.from && interval.to < unscanned.to) {
            //     // unscanned contains interval
            //     result << [from: unscanned.from, to: interval.from - 1]
            //     result << [from: interval.to + 1, to: unscanned.to]
            // }
            } else {
                if (it.from < interval.from) {
                    result << [from: it.from, to: interval.from - 1]
                }
                if (interval.to < it.to) {
                    result << [from: interval.to + 1, to: it.to]
                }
            }
        }
    
        return result
    }

    // slow with puzzle input - multithreaded solution?
    def solve2(maxCoord) {

        BigInteger result

        int i = 0

        for (int y = 0; y <= maxCoord; y++) {
            def intervals = findScannedIntervals(y)
            def unscanned = [[from: 0, to: maxCoord]]
            for (interval in intervals) {
                unscanned = subtract(unscanned, interval)
                if (unscanned.isEmpty()) break
            }
            if (unscanned.size() == 1) {
                println "${unscanned[0]}, $y"
                result = (4000000 as BigInteger) * unscanned[0].from + y
                break
            }

            // print something to show the program is still running
            if (i % 25000 == 0) println i
            i++
        }

        result
    }
}

//-----------------------------------------------
// Test

def input = '''\
Sensor at x=2, y=18: closest beacon is at x=-2, y=15
Sensor at x=9, y=16: closest beacon is at x=10, y=16
Sensor at x=13, y=2: closest beacon is at x=15, y=3
Sensor at x=12, y=14: closest beacon is at x=10, y=16
Sensor at x=10, y=20: closest beacon is at x=10, y=16
Sensor at x=14, y=17: closest beacon is at x=10, y=16
Sensor at x=8, y=7: closest beacon is at x=2, y=10
Sensor at x=2, y=0: closest beacon is at x=2, y=10
Sensor at x=0, y=11: closest beacon is at x=2, y=10
Sensor at x=20, y=14: closest beacon is at x=25, y=17
Sensor at x=17, y=20: closest beacon is at x=21, y=22
Sensor at x=16, y=7: closest beacon is at x=15, y=3
Sensor at x=14, y=3: closest beacon is at x=15, y=3
Sensor at x=20, y=1: closest beacon is at x=15, y=3'''

def bez = new BeaconExclusionZone(input)

assert bez.solve1(10) == 26
assert bez.solve2(20) == 56000011

println 'Test ok'

//-----------------------------------------------
// Puzzle

input = Puzzle.getInput(2022, 15)

bez = new BeaconExclusionZone(input)

Puzzle.printResults bez.solve1(2000000), bez.solve2(4000000)
