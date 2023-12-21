//===============================================================
// Advent of Code 2023, Day 5: If You Give A Seed A Fertilizer
//===============================================================

import groovy.transform.ToString

class SeedToLocation {

    def seeds
    def maps

    def SeedToLocation(input, part) {

        this.seeds = []
        this.maps = [:]

        def mode = 'seeds'

        input.eachLine { line ->

            if (line) {
                if (mode == 'seeds') {
                    if (part == 1) {
                        line.drop(6).trim().split(' ').each {
                            this.seeds << (it as BigDecimal)
                        }
                    } else {
                        def list = line.drop(6).trim().split(' ').collect { it as BigDecimal }
                        for (int i = 0; i < list.size(); i += 2) {
                            seeds << new Interval(from: list[i], to: list[i] + list[i+1] - 1)
                        }
                    }
                    mode = null
                } else if (line.contains('map:')) {
                    mode = line.split(' ')[0]
                } else {
                    def mappings = this.maps[mode]
                    if (mappings == null) {
                        mappings = []
                        this.maps[mode] = mappings
                    }
                    def (destStart, sourceStart, length) = line.split(' ').collect { it as BigDecimal }
                    mappings << new Mapping(sourceStart, destStart, length)
                }
            }
        }
    }

    // for part 1
    def mapSeeds() {

        def result = []

        this.seeds.each { seed ->

            def source = seed
            def target

            ['seed-to-soil', 'soil-to-fertilizer', 'fertilizer-to-water', 'water-to-light', 
            'light-to-temperature', 'temperature-to-humidity', 'humidity-to-location'].each { mode ->

                target = null

                for (mapping in this.maps[mode]) {
                    target = mapping.mapSource(source)
                    if (target) break
                }

                if (!target) {
                    target = source
                }

                source = target
            }

            result << target
        }

        result
    }

    // for part 2
    def mapSeedIntervals() {
        
        def notMappedIntervals = this.seeds.collect {it} // copy seeds
        def mappedIntervals

        ['seed-to-soil', 'soil-to-fertilizer', 'fertilizer-to-water', 'water-to-light', 
         'light-to-temperature', 'temperature-to-humidity', 'humidity-to-location'].each { mode ->

            mappedIntervals = []

            for (mapping in this.maps[mode]) {

                def newNotMappedIntervals = []

                notMappedIntervals.each { interval ->
                    def result = mapping.mapSourceInterval(interval)
                    mappedIntervals = mappedIntervals + result.mapped
                    newNotMappedIntervals = newNotMappedIntervals + result.notMapped
                }
            
                notMappedIntervals = newNotMappedIntervals
            }

            // make all intervals input to next iteration
            notMappedIntervals = notMappedIntervals + mappedIntervals
        }

        // notMappedIntervals is now also the result!
        notMappedIntervals
    }
}

class Mapping {
    def start
    def end
    def destStart
    def destEnd

    def Mapping(sourceStart, destStart, length) {
        this.start = sourceStart
        this.end = sourceStart + length - 1
        this.destStart = destStart
        this.destEnd = destStart + length - 1
    }

    def mapSource(source) {
        if (source >= start && source <= end) {
            destStart + (source - start)
        } else {
            null
        }
    }

    def mapSourceInterval(interval) {

        def mappedIntervals = []
        def notMappedIntervals = []

        if (end < interval.from || start > interval.to) {
            // interval              |-------|
            // map interval |......|     or    |......|   (no intersection)
            notMappedIntervals << interval
        } else if (start <= interval.from && end >= interval.to) {
            // interval       |-------|
            // map interval |...........|   (interval completely inside mapping interval)
            mappedIntervals << new Interval(from: destStart + (interval.from - start), to: destStart + (interval.to - start))
        } else if (start > interval.from && end < interval.to) {
            // interval       |--------|
            // map interval      |..|      (mapping interval inside interval but left and right "remainders")
            notMappedIntervals << new Interval(from: interval.from, to: start - 1)
            mappedIntervals    << new Interval(from: destStart, to: destEnd)
            notMappedIntervals << new Interval(from: end + 1, to: interval.to)
        } else if (start <= interval.from && end < interval.to) {
            // interval       |--------|
            // map interval  |....|
            mappedIntervals    << new Interval(from: destStart + (interval.from - start), to: destEnd)
            notMappedIntervals << new Interval(from: end + 1, to: interval.to)
        } else /* if (start > interval.from && end >= interval.to) */ {
            // interval       |--------|
            // map interval          |....|
            notMappedIntervals <<  new Interval(from: interval.from, to: start - 1)
            mappedIntervals    << new Interval(from: destStart, to: destStart + (interval.to - start))
        }

        // if (start < interval.from) {

        //     if (end >= interval.from) {
        //         if (end < interval.to) {
        //             mappedIntervals    << new Interval(from: destStart + (interval.from - start), to: destEnd)
        //             notMappedIntervals << new Interval(from: end + 1, to: interval.to)
        //         } else if (end >= interval.to) {
        //             mappedIntervals << new Interval(from: destStart + (interval.from - start), to: destStart + (interval.to - start))
        //         }
        //     } else if (end < interval.from) {
        //         result << interval
        //     }

        // } else if (start >= interval.from && start <= interval.to) {

        //     if (start > interval.from) {
        //         result << new Interval(from: interval.from, to: start - 1)
        //     }

        //     if (end < interval.to) {
        //             mappedIntervals    << new Interval(from: destStart, to: destEnd)
        //             notMappedIntervals << new Interval(from: end + 1, to: interval.to)
        //     } else if (end >= interval.to) {
        //             mappedIntervals << new Interval(from: destStart, to: destStart + (interval.to - start))
        //     }
        // } else if (start > interval.to) {
        //     notMappedIntervals << interval
        // }

        [mapped: mappedIntervals, notMapped: notMappedIntervals]
    }
}

@ToString
class Interval {
    BigDecimal from
    BigDecimal to
}

def part1(input) {
    def stl = new SeedToLocation(input, 1)
    stl.mapSeeds().min()
}

def part2(input) {
    def stl = new SeedToLocation(input, 2)
    stl.mapSeedIntervals().collect { it.from }.min()
}

//-----------------------------------------------
// Test

def input = '''\
seeds: 79 14 55 13

seed-to-soil map:
50 98 2
52 50 48

soil-to-fertilizer map:
0 15 37
37 52 2
39 0 15

fertilizer-to-water map:
49 53 8
0 11 42
42 0 7
57 7 4

water-to-light map:
88 18 7
18 25 70

light-to-temperature map:
45 77 23
81 45 19
68 64 13

temperature-to-humidity map:
0 69 1
1 0 69

humidity-to-location map:
60 56 37
56 93 4'''

assert part1(input) == 35
assert part2(input) == 46

println 'Test ok'

//-----------------------------------------------
// Puzzle

input = Puzzle.getInput(2023, 5)

Puzzle.printResults part1(input), part2(input)
//Puzzle.printResults part1(input)
