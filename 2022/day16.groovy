//===============================================================
// Advent of Code 2022, Day 16: Proboscidea Volcanium
//===============================================================

import groovy.transform.ToString

class ProboscideaVolcanium {

    def valves = [:] // Map of valve names to Valve objects (keys: 'AA' and any valve with flow rate > 0)

    def sortedValves =  [] // sorted list of valve names (only valves with flow rate > 0, used is createStringKey)

    def shortestPaths = [:]

    def maxPressures // Map of string to maximum pressure,
    // each String key represents all valid paths (list of valves) that contain exactly the valves corresponding
    // to a 1 in the key, and the value is the mamimum of the total pressure of all these paths

    // credits go to https://www.reddit.com/r/adventofcode/comments/zo21au/2022_day_16_approaches_and_pitfalls_discussion/
    // where I found enlightment after I got stuck with the second part of the puzzle.

    ProboscideaVolcanium(input) {

        input.eachLine { line ->

            line.find(/Valve (..) has flow rate=(\d+); tunnels? leads? to valves? (.+)$/) { match, name, rate, vList ->

                def flowRate = rate as int
                def nextValves = []
                vList.split(',').each {
                    nextValves << it.trim()
                }
                valves[name] = [name: name, flowRate: flowRate, nextValves: nextValves] as Valve
            }
        }

        // find shortest paths from AA to any valve with flow rate > 0 (i == 1)
        // and shortest paths between any valves with flow rate > 0 (i == 2)

        sortedValves = valves.values().findAll { it.flowRate > 0 }.collect { it.name }.sort()

        def startList= ['AA'] + sortedValves

        startList.each { start ->

            shortestPaths[start] = []

            def paths = [[start]]
            def shortestPathsFromStart = shortestPaths[start]

            while (paths) {

                def newPaths = []

                paths.each { path ->

                    valves[path.last()].nextValves.each { next ->

                        if (next !in path) {

                            if (valves[next].flowRate > 0) {

                                def distance = path.size()

                                def p = shortestPaths[start].find { it.to == next }
                                if (p) {
                                    if (distance < p.distance) {
                                        p.distance = distance // update distance of existing path
                                    }
                                } else {
                                    shortestPathsFromStart << ([from: path[0], to: next, distance: distance, flowRate: valves[next].flowRate] as Step)
                                    newPaths << path + next
                                }
                            } else {
                                newPaths << path + next
                            }
                        }
                    }
                }

                paths = newPaths
            }
        }
    }

    def createStringKey(valves) {
        def s = ''
        for (valve in sortedValves) {
            s += (valve in valves ? '1' : '0')
        }
        s
    }

    def disjoint(s1, s2) {
        boolean b = true
        for (i in 0..<s1.size()) {
            if (s1[i] == '1' && s2[i] == '1') {
                b = false
                break
            }
        }
        b
    }

    def findMaxPressures(valvesVisited, totalPressure, minutes) {

        def last
        if (!valvesVisited) {
            last = 'AA'
            maxPressures = [:]
        } else {
            last = valvesVisited.last()
        }

        shortestPaths[last].each { nextStep ->

            def newMin = minutes - (nextStep.distance + 1) // 1 additional minute for opening the valve

            if (newMin >= 1 && nextStep.to !in valvesVisited) {
                def newTotalPressure = totalPressure + nextStep.flowRate * newMin
                def newValvesVisited = valvesVisited + nextStep.to
                def s = createStringKey(newValvesVisited)
                def pressure = maxPressures[s]
                if (!pressure || newTotalPressure > pressure) {
                    maxPressures[s] = newTotalPressure
                }
                findMaxPressures(newValvesVisited, newTotalPressure, newMin)
            }
        }
    }

    def solve1() {
        findMaxPressures([], 0, 30)
        maxPressures.values().max()
    }

    def solve2() {
        findMaxPressures([], 0, 26)
        def maxPressure = 0
        // check any combination of disjoint paths
        maxPressures.each { k1, pressure1 ->
            maxPressures.each { k2, pressure2 ->
                if (disjoint(k1, k2)) {
                    def p = pressure1 + pressure2
                    if (p > maxPressure) {
                        maxPressure = p
                    }
                }
            }
        }
        maxPressure
    }
}

class Valve {
    def name
    def flowRate
    def nextValves
}

@ToString
class Step {
    def from
    def to
    def distance
    def flowRate
}

//-----------------------------------------------
// Test

def input = '''\
Valve AA has flow rate=0; tunnels lead to valves DD, II, BB
Valve BB has flow rate=13; tunnels lead to valves CC, AA
Valve CC has flow rate=2; tunnels lead to valves DD, BB
Valve DD has flow rate=20; tunnels lead to valves CC, AA, EE
Valve EE has flow rate=3; tunnels lead to valves FF, DD
Valve FF has flow rate=0; tunnels lead to valves EE, GG
Valve GG has flow rate=0; tunnels lead to valves FF, HH
Valve HH has flow rate=22; tunnel leads to valve GG
Valve II has flow rate=0; tunnels lead to valves AA, JJ
Valve JJ has flow rate=21; tunnel leads to valve II'''

def pv = new ProboscideaVolcanium(input)

assert pv.solve1() == 1651
assert pv.solve2() == 1707

println 'Test ok'

//-----------------------------------------------
// Puzzle

input = Puzzle.getInput(2022, 16)

pv = new ProboscideaVolcanium(input)

Puzzle.printResults pv.solve1(), pv.solve2()
