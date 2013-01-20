import java.io._

object AlienLanguaje extends App {

    /**
     * GETS AN ITERATOR OF LINES FROM AN INPUT STREAM
     */
    type LineIterator = Iterator[String]
    def inputStream2LineInterator(in: InputStream): LineIterator = {
        val reader = new BufferedReader(new InputStreamReader(in))
        Iterator.continually(reader.readLine())
    }

    /**
     * DUMPS SOLUTION IN THE USUAL FORMAT
     */
    type Solutions[T] = Iterator[T]
    def dumpSolution[T](solutions: Solutions[T])(dump: (String) => Unit = println) = {
        solutions.toList.zipWithIndex.map {
            case (sol, cas) => dump("Case #" + (cas + 1) + ": " + sol)
        }
    }

    /**
     * MULTIPLEXES OUTPUT TO SEVERAL PRINT STREAMS
     */
    def printToFiles(writers: PrintStream*): (String) => Unit = {
        (s: String) => writers.map(_.println(s))
    }

    /**
     * OPENS A FILE, PROCESSES IT AND DUMPS THE SOLUTION TO A FILE AND STANDARD OUTPUT
     */
    def processFile[T](solveProblem: (LineIterator => Solutions[T]))(file: String = "-") = {
        val in = inputStream2LineInterator(if (file != "-")
            new FileInputStream(file)
        else
            System.in)

        val solutions = solveProblem(in)

        val fileOut = (if (file.endsWith(".in"))
            file.replace(".in", ".out")
        else
            file + ".out")

        val out = new PrintStream(new FileOutputStream(fileOut, true))
        dumpSolution(solutions)(printToFiles(out, System.out))
        out.close
    }

    /**
     * DOES THE REAL WORK
     */
    def alienLanguage(in: LineIterator): Solutions[Int] = {
        val Array(l, d, n) = in.next.split(" ").map(_ toInt)
        val knownToExists = in.take(d).toArray
        val testCases = in.take(n)

        for (line <- testCases) yield {
            val re = line.map(_ match {
                case '(' => '['
                case ')' => ']'
                case c => c
            })
            println("Checking:" + re)
            knownToExists.count(_.matches(re))
        }
    }

    // LAUNCHS IT ALL
    //processFile(alienLanguage)("sample.in")
    processFile(alienLanguage)("A-large-practice.in")
    processFile(alienLanguage)("A-small-practice.in")

}