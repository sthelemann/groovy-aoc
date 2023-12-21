//===============================================================
// Advent of Code 2023, Day 2: Cube Conundrum
//===============================================================

class Game {

    int id
    boolean isValid
    List<CubesShown> sets

    def validate(red, green, blue) {
        this.isValid = true
        for (cubesShown in sets) {
           this.isValid = (cubesShown.red <= red && cubesShown.green <= green && cubesShown.blue <= blue)
           if (!this.isValid) break
        }
    }

    def getPower() {

        int red = 0
        int green= 0
        int blue = 0

        sets.each { set ->
            if (set.red > red) red = set.red
            if (set.green > green) green = set.green
            if (set.blue > blue) blue = set.blue
        }

        red * green * blue
    }
}

class CubesShown {

    int red
    int green
    int blue
}

def getGames(input) {

    List<Game> games = []

    input.eachLine { line ->

        def (idStr, setsStr) = line.split(':')

        def id

        idStr.find(/\d+/) { match ->
            id = match as int
        }

        List<CubesShown> sets = []

        setsStr.split(';').each { shownStr ->

            shownStr.split(',').each { s ->

                int red = 0
                int green = 0
                int blue = 0

                s.trim()find(/(\d+) (red|green|blue)/) { match, n, color ->
                    if (color == 'red') {
                        red = n as int
                    } else if (color == 'green') {
                        green = n as int
                    } else if (color == 'blue') {
                        blue = n as int
                    }
                }

                sets << new CubesShown(red: red, green: green, blue: blue)
            }

        }

        def game = new Game()
        game.id = id
        game.sets = sets
        game.validate(12, 13, 14)

        games << game
    }
}

def part1(input) {

    List<Game> games = getGames(input)

    games.collect{ it.isValid ? it.id : 0}.sum()
}

def part2(input) {

    List<Game> games = getGames(input)

    games.collect{ it.getPower() }.sum()
}

//-----------------------------------------------
// Test

def input = '''\
Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
'''

assert part1(input) == 8
assert part2(input) == 2286

println 'Test ok'

//-----------------------------------------------
// Puzzle

input = Puzzle.getInput(2023, 2)

Puzzle.printResults part1(input), part2(input)
