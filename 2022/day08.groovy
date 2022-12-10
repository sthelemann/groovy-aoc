//===============================================================
// Advent of Code 2022, Day 8: Treetop Tree House
//===============================================================

class TreetopTreeHouse {

    int[][] height
    def trees = []
    def maxScenicScore = 0

    TreetopTreeHouse(input) {

        def lines = []
        input.eachLine { lines << it }
        def iMax = lines.size() - 1
        def jMax = lines[0].size() - 1

        height = new int[iMax+1][jMax+1]

        lines.eachWithIndex { line, i ->
            line.eachWithIndex { h, j ->
                height[i][j] = (h as int)
            }
        }

        checkVisibility(iMax, jMax)
        calcMaxScenicScore(iMax, jMax)
    }

    def checkVisibility(iMax, jMax) {

        for (i in 0..iMax) {
            for (j in 0..jMax) {

                if (i == 0 || i == iMax || j == 0 || j == jMax) {
                    trees << new Tree(i, j, true)
                } else {
                    def h = height[i][j]
                    def visible = true

                    // check left
                    for (k in 0..<j) {
                        if (height[i][k] >= h) {
                            visible = false
                            break
                        }
                    }

                    if (!visible) {
                        // check right
                        visible = true
                        for (k in j+1..jMax) {
                            if (height[i][k] >= h) {
                                visible = false
                                break
                            }
                        }
                    }

                    if (!visible) {
                        // check up
                        visible = true
                        for (k in 0..<i) {
                            if (height[k][j] >= h) {
                                visible = false
                                break
                            }
                        }
                    }

                    if (!visible) {
                        // check down
                        visible = true
                        for (k in i+1..iMax) {
                            if (height[k][j] >= h) {
                                visible = false
                                break
                            }
                        }
                    }

                    trees << new Tree(i, j, visible)
                }
            }
        }
    }

    def calcMaxScenicScore(iMax, jMax) {

        // consider inner trees only because border trees all have scenic score 0
        // in at least one direction, so the total scenic score is also 0

        for (i in 1..<iMax) {
            for (j in 1..<jMax) {

                def h = height[i][j]
                def totalScore = 1

                // scenic score left
                def score = 0
                for (k in j-1..0) {
                    score++
                    if (height[i][k] >= h) break
                }

                totalScore *= score

                if (totalScore != 0) {
                    // scenic score right
                    score = 0
                    for (k in j+1..jMax) {
                        score++
                        if (height[i][k] >= h) break
                    }
                    totalScore *= score
                }

                if (totalScore != 0) {
                    // scenic score up
                    score = 0
                    for (k in i-1..0) {
                        score++
                        if (height[k][j] >= h) break
                    }
                    totalScore *= score
                }

                if (totalScore != 0) {
                    // scenic score down
                    score = 0
                    for (k in i+1..iMax) {
                        score++
                        if (height[k][j] >= h) break
                    }
                    totalScore *= score
                }

                if (totalScore > maxScenicScore) {
                    maxScenicScore = totalScore
                }
            }
        }
    }

    def countVisibleTrees() {
        trees.findAll { it.visible }.size()
    }
}

class Tree extends Point {

    boolean visible = false
    int scenicScore = 0

    Tree(x, y, visible) {
        super(x, y)
        this.visible = visible
    }
}

//-----------------------------------------------
// Test

def input = '''30373
25512
65332
33549
35390'''

def tth = new TreetopTreeHouse(input)

assert tth.countVisibleTrees() == 21
assert tth.maxScenicScore == 8

println 'Test ok'

//-----------------------------------------------
// Puzzle

input = Puzzle.getInput(2022, 8)

tth = new TreetopTreeHouse(input)

Puzzle.printResults tth.countVisibleTrees(), tth.maxScenicScore
