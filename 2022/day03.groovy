//===============================================================
// Advent of Code 2022, Day 3: Rucksack Reorganization
//===============================================================

class RucksackReorg {

    def priority = [:]

    RucksackReorg() {

        int n = ('a' as char) as int
        for (c in 'a'..'z') {
            priority[c] =  ((c as char) as int) - n + 1
        }

        n = ('A' as char) as int
        for (c in 'A'..'Z') {
            priority[c] =  ((c as char) as int) - n + 27
        }
    }

    def part1(input) {

        def sum = 0

        input.eachLine { rucksack ->
        
            def s = rucksack.size()/2
            def compartment1 = rucksack[0..<s]
            def compartment2 = rucksack[s..-1]

            for (c in compartment1) {
                if (compartment2.contains(c)) {
                    sum += priority[c]
                    break
                }
            }
        }

        sum
    }

    def part2(input) {

        def sum = 0
        def i = 0
        Set intersection = []

        input.eachLine { rucksack ->

            i++

            if (i == 1) {
                intersection = rucksack.toSet()
            } else {
                intersection = rucksack.toSet().intersect(intersection)
                if (i == 3) {
                    assert intersection.size() == 1
                    sum += priority[intersection[0]]
                    i = 0
                }
            }
        }

        sum
    }
}

//-----------------------------------------------
// Test

def r = new RucksackReorg()

def input = '''vJrwpWtwJgWrhcsFMMfFFhFp
jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL
PmmdzqPrVvPwwTWBwg
wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn
ttgJtRGJQctTZtZT
CrZsJsPPZsGzwwsLwLmpwMDw'''

assert r.part1(input) == 157

assert r.part2(input) == 70

println 'Test ok'

//-----------------------------------------------
// Puzzle

input = Puzzle.getInput(3)

Puzzle.printResults r.part1(input), r.part2(input)
