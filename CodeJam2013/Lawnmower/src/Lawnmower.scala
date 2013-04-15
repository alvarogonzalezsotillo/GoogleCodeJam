import java.io._

object Lawnmower extends App {
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

        tests.map( solveOneTest )
    }

    /**
     * READ LINES FOR A SINGLE TEST INSIDE THE FILE
     */
    def readLawnmower( in: LineIterator ): LoadedTest = {
        val Array(n,m) = in.next.split(" ").map(_.toInt)
        in.take(n).toArray
    }

    /**
     * SOLVE A SINGLE TEST
     */
    def solveLawnmower( in: LoadedTest ): String = {
        val tablero = for( s <- in.toArray ) yield s.split(" ").map(_.toInt)
        val rows = tablero.length
        val columns = tablero(0).length
        
        def value(r: Int, c: Int) = {
            if( r < 0 ) 0
            else if( r >= rows ) 0
            else if( c < 0 ) 0
            else if( c >= columns ) 0
            else tablero(r)(c)
        }
        
        def possibleInRow( r: Int, c: Int ) = {
            val v = value(r,c)
            (0 until rows).forall( ro => v >= value(ro,c))
        }

        def possibleInColumn( r: Int, c: Int ) = {
            val v = value(r,c)
            (0 until columns).forall( co => v >= value(r,co))
        }
        
        def possible(r:Int, c:Int) = possibleInColumn(r, c) || possibleInRow(r,c);
        
        (for( r <- 0 until rows ;c <- 0 until columns ) yield possible(r,c)).forall(b=>b) match{
            case true => "YES"
            case false => "NO"
        }
    }


    // LAUNCHS IT ALL
    val problem = solveOneFile( process(readLawnmower)(solveLawnmower) )_
    problem("sample.in")
    problem("test.in")
    problem("B-small-attempt0.in")
    problem("B-large.in")

}