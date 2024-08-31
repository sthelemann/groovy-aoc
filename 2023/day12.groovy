//===============================================================
// Advent of Code 2023, Day 12: Hot Springs
//===============================================================

class HotSprings {

    def lineNr
    def springs
    def damagedGroups // List of sizes of contiguous groups of damaged springs
    def factor

    def cache = [(3): [:], (2): [:], (1) : [:], (0) : [:]] // cache with number of solutions for n < 4

    BigInteger countSolutions = 0

    HotSprings(lineNr, line, factor) {

        this.lineNr = lineNr
        this.factor = factor - 1
       
        def tokens = line.split(' ')

        springs = tokens[0]
        if (this.factor > 0) {
            // add this.factor folds
            springs += ('?' + springs).multiply(this.factor)
        }

        damagedGroups = []

        tokens[1].split(',').each {
            damagedGroups << (it as int)
        }

        countSolutions = findSolutions(factor - 1, springs, damagedGroups)

        println "$lineNr: countSolutions = $countSolutions"
    }

    def findSolutions(n, springs, damagedGroups) {

//        if (n == 4) println "-----\nlineNr = $lineNr, n = $n, springs = $springs, size = ${springs.size()}, damagedGroups = $damagedGroups"

        BigInteger totalCount

        // look up in cache 

        if (n < this.factor) {
            totalCount = cache[n][springs]
            if (totalCount != null) {
                return totalCount
            }
        }

        totalCount = 0

        def dgCount = damagedGroups.size()

        if (n > 0) {

            // minimal solution: exactly 1 operational spring between damaged groups
            def minLength = damagedGroups.sum() + (dgCount - 1)
            // between all solutions except the last one the must be at least 1 operational spring,
            // e.g for n = 4: |1|solution3|1|solution2|1|solution1|1|solution0
            // length of solutionN is minLength, so we have 
            def maxLength = springs.size() - n*(minLength + 1)

//            if (n == 4) println "minLength = $minLength, maxLength = $maxLength"

            if (minLength > maxLength) {
                return 0
            }

            for (len in minLength..maxLength) {

                // if n > 0 any solution will end with a damaged group. In springs the next spring must be operational and this one will
                // be skipped before the solutio process continues.

                // if first spring after current solution is damaged there will be no match

//                println "n = $n, len = $len"

                if (springs.size() > len && springs[len] != '#') {

                    BigInteger count = 0
                    def currentSprings = springs.take(len)
                    def ogSize = currentSprings.size() - damagedGroups.sum()

//                    println "currentSprings = $currentSprings, ogSize = $ogSize, dgCount = $dgCount"

                    if (ogSize >= dgCount - 1 /* at least 1 operational spring between two damaged groups */) {

                        def allOperationalGroups = findAllOperationalGroups(ogSize, [0] + [1].multiply(dgCount - 1))

                        allOperationalGroups.each {
                            if (check(currentSprings, it, damagedGroups)) {
                                count++
                            }
                        }
                    }

                    if (count > 0) {
                        count = count * findSolutions(
                            n - 1, 
                            springs.drop(len + 1), // also drop first spring after solution 
                            damagedGroups
                        )
                    }
                    totalCount += count
                }
            }

        } else {

            // n = 0 (last solution), with optional trailing operational group

            def ogSize = springs.size() - damagedGroups.sum()

            def allOperationalGroups = findAllOperationalGroups(ogSize, [0] + [1].multiply(dgCount - 1) + [0])

            allOperationalGroups.each {
                if (check(springs, it, damagedGroups)) {
                    totalCount++
                }
            }
        }

        if (n < this.factor) {
            cache[(n)][springs] = totalCount
        }

        totalCount  
    }

    def findAllOperationalGroups(ogSize, minOgSizes) {

        def ogCount = minOgSizes.size()

        def result = findAllSums(ogCount, ogSize - minOgSizes.sum())

        result.each {

            for (i in 0..<ogCount) {
                if (minOgSizes[i] != 0) {
                    it[i] += 1
                }
            }
        }

        result
    }

    def findAllSums(n, totalSum) {

         def result = []

        if (totalSum < 0) {
            return result
        }

        if (n > 1) {

            for (i in 0..totalSum) {
                findAllSums(n - 1, totalSum - i).each {
                    result << ([i] + it)
                }
            }
        } else {
            result = [totalSum]
        }

        result
    }

    def check(springs, operationalGroups, damagedGroups) {

        def i = 0
        def ogSize = operationalGroups.size()
        def dgSize = damagedGroups.size()
        def j = 0
    
        while (i < ogSize) {

            // check operational group

            for (k in 0..<operationalGroups[i]) {
                if (springs[j] == '#') {
                    return false
                }
                j++
            }

            // check damaged group

            if (i < dgSize /* possibly trailing operational group */) {

                for (k in 0..<damagedGroups[i]) {
                    if (springs[j] == '.') {
                        return false
                    }
                    j++
                }
            }

            i++
        }

        true
    }
}

def getResult(input, partNr) {

    def n = (partNr == 1 ? 1 : 5)

    def i = 0
    def iMin = 0 // 668

    BigInteger result = 0

    input.eachLine { line ->
        i++
        if (i >= iMin) {
            def h =  new HotSprings(i, line, n)
            result += h.countSolutions
        }
    }

    result
}

//-----------------------------------------------
// Test

def input = '''\
???.### 1,1,3
.??..??...?##. 1,1,3
?#?#?#?#?#?#?#? 1,3,1,6
????.#...#... 4,1,1
????.######..#####. 1,6,5
?###???????? 3,2,1
'''

assert getResult(input, 1) == 21
assert getResult(input, 2) == 525152

println 'Test ok'

//-----------------------------------------------
// Puzzle

input = Puzzle.getInput(2023, 12)

Puzzle.printResults getResult(input, 1)
Puzzle.printResults null, getResult(input, 2)
