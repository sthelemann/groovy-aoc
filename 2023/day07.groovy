//===============================================================
// Advent of Code 2023, Day 7: Camel Cards
//===============================================================

import groovy.transform.ToString

class CamelCards {

    def hands = []

    def CamelCards(input, part) {

        input.eachLine { line ->
        
            def (cards, bidString) = line.tokenize()
            hands << new Hand(cards, bidString as BigInteger, part)
        }
    }

    def sortHands() {
        hands.sort {a, b -> 
            def c = a.compareType(b)
            (c == 0 ? a.compareCards(b) : c)
        }
    }

    def getTotalWinnings() {

        BigInteger sum = 0
        sortHands().eachWithIndex { hand, i ->
            sum += hand.bid * (i + 1)
        }
        sum
    }
}

@ToString
class Hand {

    static cardRank = [
        (1): [A: 13, K: 12, Q: 11, J: 10, T: 9, 
              '9': 8, '8': 7, '7': 6, '6': 5, '5': 4, '4': 3, '3': 2, '2': 1],
        (2): [A: 13, K: 12, Q: 11, T: 10, 
              '9': 9, '8': 8, '7': 7, '6': 6, '5': 5, '4': 4, '3': 3, '2': 2, J: 1]
    ]

    static typeRank = [ fiveOfAKind: 7, fourOfAKind: 6, fullHouse: 5, 
                        threeOfAKind: 4, twoPair: 3, onePair: 2, highCard: 1]

    def cards
    BigInteger bid
    def type
    def part

    def Hand(cards, bid, part) {
        this.cards = cards
        this.bid = bid
        this.part = part
        analyseCards()
    }

    def analyseCards() {

        def map = [:]

        for (c in cards) {
            def n = map[c]
            if (n == null) {
                n = 0
            }
            map[c] = n + 1
        }

        def counts = (this.part == 1 ? map.values() : map.findAll { it.key != 'J' }.values())
        def countJ = (this.part == 1 ? 0 : (map['J'] ?: 0))

        if (5 in counts) {
            this.type = 'fiveOfAKind'
        } else if (4 in counts) {  
            this.type = 'fourOfAKind'
        } else if (3 in counts && 2 in counts) {  
            this.type = 'fullHouse'
        } else if (3 in counts) {  
            this.type = 'threeOfAKind'
        } else if (2 in counts) {
            if (counts.findAll { it == 2 }.size() == 2) {
                this.type = 'twoPair'
            } else {  
                this.type = 'onePair'
            }
        } else {  
            this.type = 'highCard'
        }

        if (part == 2) {

            while (countJ > 0) {

                // with countJ > 0 wwe never have the cases this.type == 'fiveOfAKind' and this.type == 'fullHouse'

                if (this.type == 'fourOfAKind') {
                    this.type = 'fiveOfAKind'
                } else if (this.type == 'threeOfAKind') {
                    this.type = 'fourOfAKind'
                } else if (this.type == 'twoPair') {
                   this.type = 'fullHouse'
                } else if (this.type == 'onePair') {
                    this.type = 'threeOfAKind'
                } else if (this.type == 'highCard') {
                    this.type = 'onePair'
                }

                countJ--
            }
        }
    }

    int compareType(Hand other) {

        def thisRank = Hand.typeRank[this.type]
        def otherRank = Hand.typeRank[other.type]

        if (thisRank > otherRank) {
            1
        } else if (thisRank < otherRank) {
            -1
        } else {
            0
        }
    }

    int compareCards(Hand other) {

        def cardRank = Hand.cardRank[this.part]

        for (i in 0..4) {

            def thisRank = cardRank[this.cards[i]]
            def otherRank = cardRank[other.cards[i]]

            if (thisRank > otherRank) {
                return 1
            } else if (thisRank < otherRank) {
                return -1
            }
        }

        0
    }
}

def part1(input) {

    def cc = new CamelCards(input, 1)
    cc.getTotalWinnings()
}

def part2(input) {

    def cc = new CamelCards(input, 2)
    cc.getTotalWinnings()
}

//-----------------------------------------------
// Test

def input = '''\
32T3K 765
T55J5 684
KK677 28
KTJJT 220
QQQJA 483'''

assert part1(input) == 6440
assert part2(input) == 5905

println 'Test ok'

//-----------------------------------------------
// Puzzle

input = Puzzle.getInput(2023, 7)

Puzzle.printResults part1(input), part2(input)
//Puzzle.printResults part1(input)
