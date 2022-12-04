class Puzzle {

    static final String dir = 'C:/_sven/Informatik/Advent of Code 2022/input'
    //static final String dir = 'C:/ethele8/Projekte/Advent of Code 2022/input'

    static String getInput(day) {

        def fname = "day${(day as String).padLeft(2, '0')}.txt"
        new File(dir, fname).text
    }

    static String printResults(Object[] args) {

        int i = 0

        args.each {
            i++
            println "Result $i: $it"
        }
    }
}