//===============================================================
// Advent of Code 2022, Day 11: Monkey in the Middle
//===============================================================

class MonkeyInTheMiddle1 {

    def monkeys = []

    MonkeyInTheMiddle1(input, shell) {

        def monkey

        input.eachLine {line ->
            if (line) {
                if (line.startsWith('Monkey')) {
                    monkey = new Monkey()
                    monkeys << monkey
                    monkey.id = line.split(' ')[1][0..-2]
                } else if (line.startsWith('  Starting')) {
                    monkey.items = line.split(':')[1].split(',').collect { it as int }
                } else if (line.startsWith('  Operation')) {
                    monkey.operation = shell.evaluate('{old -> ' + line.split('=')[1].trim() + '}')
                } else if (line.startsWith('  Test')) {
                    monkey.divisor = (line.trim().split(' ')[3] as int)
                } else if (line.startsWith('    If')) {
                    monkey.throwTo << (line.trim().split(' ')[5] as int)
                }
            }
        }
    }

    def oneRound() {
        monkeys.each { monkey ->
            while (monkey.items) {
                monkey.inspections ++
                def item = monkey.operation(monkey.items.pop())
                item = (item/3 as int)
                def target = (item % monkey.divisor == 0 ? monkey.throwTo[0] : monkey.throwTo[1])
                monkeys[target].items << item
            }
        }
    }

    def doRounds(n) {

        for (i in 1..n) {
            oneRound()
        }
    }

    def solve() {
        def val = monkeys.sort {a, b -> b.inspections <=> a. inspections}.take(2).collect { it.inspections }
        val[0]*val[1]
    }
}

class MonkeyInTheMiddle2 {

    // Each monkey uses an individual prime for testing, these primes are collected in
    // instance variable primes, the position corresponds to the condition of the monkey in monkeys

    def monkeys = []
    def primes = []

    MonkeyInTheMiddle2(input, shell) {

        def monkey

        input.eachLine {line ->
            if (line) {
                if (line.startsWith('Monkey')) {
                    monkey = new Monkey()
                    monkeys << monkey
                    monkey.id = line.split(' ')[1][0..-2]
                } else if (line.startsWith('  Starting')) {
                    monkey.items = line.split(':')[1].split(',').collect { it as int }
                } else if (line.startsWith('  Operation')) {
                    monkey.operation = shell.evaluate('{old -> ' + line.split('=')[1].trim() + '}')
                } else if (line.startsWith('  Test')) {
                    monkey.divisor = (line.trim().split(' ')[3] as int)
                    primes << monkey.divisor
                } else if (line.startsWith('    If')) {
                    monkey.throwTo << (line.trim().split(' ')[5] as int)
                }
            }
        }

        // replace all items by list of remainders of primes

        monkeys.each {
            def newItems = []
            it.items.each { item ->
                def newItem = []
                primes.each { p ->
                    newItem << (item as int) % p
                }
                newItems << newItem
            }
            it.items = newItems
        }
    }

    def oneRound() {
        monkeys.eachWithIndex { monkey, i ->
            while (monkey.items) {
                monkey.inspections ++
                def newItem = []
                monkey.items.pop().eachWithIndex { item, k ->
                    newItem << monkey.operation(item) % primes[k]
                }
                def target = (newItem[i] == 0 ? monkey.throwTo[0] : monkey.throwTo[1])
                monkeys[target].items << newItem
            }
        }
    }

    def doRounds(n) {

        for (i in 1..n) {
            oneRound()
        }
    }

    def solve() {
        def val = monkeys.sort {a, b -> b.inspections <=> a. inspections}.take(2).collect { it.inspections }
        val[0]*val[1]
    }
}

class Monkey {
    def id
    List items
    Closure operation
    int divisor
    def throwTo = []
    BigInteger inspections = 0
}

//-----------------------------------------------
// Test

def input = '''Monkey 0:
  Starting items: 79, 98
  Operation: new = old * 19
  Test: divisible by 23
    If true: throw to monkey 2
    If false: throw to monkey 3

Monkey 1:
  Starting items: 54, 65, 75, 74
  Operation: new = old + 6
  Test: divisible by 19
    If true: throw to monkey 2
    If false: throw to monkey 0

Monkey 2:
  Starting items: 79, 60, 97
  Operation: new = old * old
  Test: divisible by 13
    If true: throw to monkey 1
    If false: throw to monkey 3

Monkey 3:
  Starting items: 74
  Operation: new = old + 3
  Test: divisible by 17
    If true: throw to monkey 0
    If false: throw to monkey 1'''

def m = new MonkeyInTheMiddle1(input, this)
m.doRounds(20)

assert m.solve() == 10605

m = new MonkeyInTheMiddle2(input, this)
m.doRounds(10000)

assert m.solve() == 2713310158

println 'Test ok'

//-----------------------------------------------
// Puzzle

input = Puzzle.getInput(2022, 11)

m = new MonkeyInTheMiddle1(input, this)
m.doRounds(20)
def result1 = m.solve()

m = new MonkeyInTheMiddle2(input, this)
m.doRounds(10000)
def result2 = m.solve()

Puzzle.printResults result1, result2
