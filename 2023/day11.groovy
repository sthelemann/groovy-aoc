//===============================================================
// Advent of Code 2023, Day 11: Cosmic Expansion
//===============================================================

class CosmicExpansion {

    def galaxies = [:]
	
	def expandRows = []
	def expandColumns = []
	
    def CosmicExpansion(input) {

        def lines = []
		def k = 0
		
        input.eachLine { line ->
            lines << line
            if (line.matches(/\.+/)) {
                expandRows << k
            }
			k++
        }

        def width = lines[0].size()
        def height = lines.size()

        for (i in 0..<width) {
            def isEmptyColumn = true
            for (j in 0..<height) {
                if (isEmptyColumn && lines[j][i] != '.') {
                    isEmptyColumn = false
					break
                }
            }

            if (isEmptyColumn) {
				expandColumns << i
            }
        }
		
        def id = 0
        def y = 0
        for (line in lines) {
            def x = 0
            for (c in line) {
                if (c != '.') {
                    id++
                    galaxies[(id)] = new Point(x, y)
                }
                x++
            }
            y++
        }
    }
	
	def getDistances(expandFactor) {
		
				def idMax = galaxies.size()
		
				def distances = []
		
				for (id1 in 1..<idMax) {
					def p1 = galaxies[id1]
					def distancesFromId1 = []
					for (id2 in id1+1..idMax) {
						def p2 = galaxies[id2]
						BigInteger dist =  Math.abs(p2.x - p1.x) + Math.abs(p2.y - p1.y)
						
						// add expansion
						def expandRowsBetween = expandRows.findAll { it > Math.min(p1.y, p2.y) && it < Math.max(p1.y, p2.y) }
						def expandColumnsBetween = expandColumns.findAll { it > Math.min(p1.x, p2.x) && it < Math.max(p1.x, p2.x) }
						
						dist += (expandRowsBetween.size() + expandColumnsBetween.size()) * (expandFactor - 1)
						distances << dist
					}
				}
		
				distances
			}
}

def getResult(input, expandFactor) {
    def ce = new CosmicExpansion(input)
    BigInteger sum = 0
    ce.getDistances(expandFactor).each { 
        sum += it
    }
    sum
}

def part2(input) {
}

//-----------------------------------------------
// Test

def input = '''\
...#......
.......#..
#.........
..........
......#...
.#........
.........#
..........
.......#..
#...#.....'''

def testCases = [
	[id: 1, input: input, expandFactor: 2, result: 374],
	[id: 2, input: input, expandFactor: 10, result: 1030],
	[id: 3, input: input, expandFactor: 100, result: 8410]
]

testCases.each {
	println "TEST $it.id"
	assert getResult(it.input, it.expandFactor) == it.result
}

println 'Test ok'
//-----------------------------------------------
// Puzzle

input = Puzzle.getInput(2023, 11)

Puzzle.printResults getResult(input, 2), getResult(input, 1000000)
