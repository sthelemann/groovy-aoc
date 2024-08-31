//===============================================================
// Advent of Code 2023, Day 15: Lens Library
//===============================================================

class Lens {
    def label
    def focalLength
}

def getHash(s) {

    def currValue = 0
    s.toCharArray().each { c->
        currValue += (c as int)
        currValue *= 17
        currValue = currValue % 256
    }

    currValue
}

def performStep(step, boxes) {

    def label
    def length

    def i = step.indexOf('=')
    if (i > 0) {
        (label, length) = step.split('=')
        def hash = getHash(label)
        def lensList = boxes[hash]
        def lens = lensList.find { it.label == label} 
        if (lens) {
            lens.focalLength = (length as int)
        } else {
            lensList << new Lens(label: label, focalLength: length as int)
        }
    } else {
        label = step[0..<i]
        def hash = getHash(label)
        def lensList = boxes[hash]
        if (lensList) {
            def lensListNew = []
            lensList.each {
                if (it.label != label) {
                    lensListNew << it
                }
            }
            boxes[hash] = lensListNew
        }
    }
}

def part1(input) {

    int sum = 0
    input.split(',').each {
        sum += getHash(it)
    }

    sum
}

def part2(input) {

    def boxes = []
    for (i in 1..256) {
        boxes << []
    }

    input.split(',').each { step ->
        performStep step, boxes
    }

    int result = 0

    boxes.eachWithIndex { lensList, i ->
        lensList.eachWithIndex { lens, j ->
            result += (i + 1) * (j + 1) * lens.focalLength
        }
    }

    result
}

//-----------------------------------------------
// Test

def input = '''\
rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7'''

assert part1(input) == 1320
assert part2(input) == 145

println 'Test ok'

//-----------------------------------------------
// Puzzle

input = Puzzle.getInput(2023, 15)

Puzzle.printResults part1(input), part2(input)
