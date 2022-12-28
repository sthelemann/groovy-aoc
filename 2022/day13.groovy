//===============================================================
// Advent of Code 2022, Day 13: Distress Signal
//===============================================================

class DistressSignal {

    def pairs = []
    def packets = []

    DistressSignal(shell, input) {

        def pair = [left: null, right: null]

        input.eachLine { line ->
            if (line) {
                def packet = shell.evaluate(line)
                packets << packet
                if (pair.left == null) { 
                    pair.left = packet
                } else {
                    pair.right = packet
                }
            } else {
                pairs << pair
                pair = [left: null, right: null]
            }
        }

        pairs << pair
    }

    def compare(left, right) {

        Boolean result = null

        if (left instanceof Integer && right instanceof Integer) {

            if (left < right) {
                result = true
            } else if (left > right) {
                result = false
            }

        } else if (left instanceof List && right instanceof List) {

            def leftSize = left.size()
            def rightSize = right.size()
            def minSize = (leftSize < rightSize ? leftSize : rightSize)

            for (int i = 0; i < minSize && result == null; i++) {
                result = compare(left[i], right[i])
            }

            if (result == null) {
                if (leftSize < rightSize) {
                    result = true
                } else if (rightSize < leftSize) {
                    result = false
                }
            }

        } else if (left instanceof Integer && right instanceof List) {
            result = compare([left], right)
        } else if (left instanceof List && right instanceof Integer) {
            result = compare(left, [right])
        }

        result
    }

    def solve1() {
        def sum = 0
        pairs.eachWithIndex { pair, i ->
            def rightOrder = compare(pair.left, pair.right)
            if (rightOrder) {
                sum += i+1
            }
        }
        sum
    }

    def solve2() {
        def divider1 = [[2]]
        def divider2 = [[6]]
        packets << divider1
        packets << divider2
        packets = packets.sort {a, b -> 
            def result = compare(a, b)
            if (result == true) -1 else if (result == false) 1 else 0
        }
        (packets.indexOf(divider1) + 1) * (packets.indexOf(divider2) + 1)
    }
}

//-----------------------------------------------
// Test

def input = '''\
[1,1,3,1,1]
[1,1,5,1,1]

[[1],[2,3,4]]
[[1],4]

[9]
[[8,7,6]]

[[4,4],4,4]
[[4,4],4,4,4]

[7,7,7,7]
[7,7,7]

[]
[3]

[[[]]]
[[]]

[1,[2,[3,[4,[5,6,7]]]],8,9]
[1,[2,[3,[4,[5,6,0]]]],8,9]'''

def d = new DistressSignal(this, input)
assert d.solve1() == 13
assert d.solve2() == 140

println 'Test ok'

//-----------------------------------------------
// Puzzle

input = Puzzle.getInput(2022, 13)

d = new DistressSignal(this, input)

Puzzle.printResults d.solve1(), d.solve2()
