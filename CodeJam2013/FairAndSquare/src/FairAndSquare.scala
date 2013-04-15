import java.io._

object FairAndSquare extends App {
    /**
     * GETS AN ITERATOR OF LINES FROM AN INPUT STREAM
     */
    type LineIterator = Iterator[String]
    def inputStream2LineInterator(in: InputStream): LineIterator = {
        val reader = new BufferedReader(new InputStreamReader(in))
        Iterator.continually(
                reader.readLine()
        )
    }

    /**
     * DUMPS SOLUTION IN THE USUAL FORMAT
     */
    type Solutions[T] = Traversable[T]
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
    def solveOneFile[T](solveProblem: (LineIterator => Solutions[T]))(file: String = "-") = {

        println( "FILE:" + file )
        val initTime = System.currentTimeMillis()

        val is = if (!(file == "-"))
            if( new File(file).exists )
                new FileInputStream(file)
            else
                getClass().getResourceAsStream(file)
        else
            System.in

        val in = inputStream2LineInterator(is)

        val solutions = solveProblem(in)

        val fileOut = (if (file.endsWith(".in"))
            file.replace(".in", ".out")
        else
            file + ".out")

        val out = new PrintStream(new FileOutputStream(fileOut), true)
        dumpSolution(solutions)(printToFiles(out, System.out))
        out.close

        if( !(file == "-") ) is.close

        println( "Time for file " + file + ":" + (System.currentTimeMillis()-initTime))
    }



    /**
     * EXTRACTS SINGLE TESTS FROM THE FILE AND SOLVE IT
     */
    type LoadedTest = Array[String]
    def process[T](readOneTest: (LineIterator)=> LoadedTest )
                  (solveOneTest: (LoadedTest)=>T)
                  (in: LineIterator): Solutions[T] = {
        val line = in.next
        val nTests = line.toInt

        val tests = for( t <- 1 to nTests ) yield readOneTest(in)

        tests.map( solveOneTest(_) )
    }

    /**
     * READ LINES FOR A SINGLE TEST INSIDE THE FILE
     */
    def readOneFairAndSquare( in: LineIterator ): LoadedTest = {
        in.take(1).toArray
    }

    /**
     * SOLVE A SINGLE TEST
     */
    def solveOneFairAndSquare( in: LoadedTest ): Int = {
        val sentence = in(0).toLowerCase().filter(_.isLetter)
        val grouped = sentence.groupBy( c => c )
        val histogram = grouped.map{ case (c,s) => (c -> s.length ) }
        val keys = histogram.keySet.toArray.sortBy( c => -histogram(c) )
        val values = for( i <- 0 to keys.size-1 ) yield (26-i)*histogram( keys(i) )
        values.fold(0)(_+_)
    }


    // LAUNCHS IT ALL
    val problem = solveOneFile( process(readOneFairAndSquare)(solveOneFairAndSquare) )_
    problem("sample.in")

}