class Puzzle {

    static String getInput(year, day) {

        def fname = "day${(day as String).padLeft(2, '0')}.txt"
        new File("./$year/input", fname).text
    }

    static String printResults(Object[] args) {

        int i = 0

        args.each {
            i++
            println "Result $i: $it"
        }
    }
}