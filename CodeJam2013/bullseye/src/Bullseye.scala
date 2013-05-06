import java.io._
import java.math.BigInteger
import com.google.common.math.BigIntegerMath

object Bullseye extends App {
    
    val useParalelism = true
    val problem = solveOneFile( process(readTest)(solveTest) )_
    problem("sample.in")
    //problem("A-small-practice.in")
    //problem("A-large-practice.in")

    /**
     * READ LINES FOR A SINGLE TEST INSIDE THE FILE
     */
    def readTest( in: LineIterator ): LoadedTest = {
        in.take(1).toArray
    }

    /**
     * SOLVE A SINGLE TEST
     */
    def solveTest( in: LoadedTest ): String = {
        val Array(r,t) = in(0).split(" ").map( BigInt(_) )

        println( "Solving:" + Thread.currentThread().getName() + ":" + in.mkString(",") )
        
        // SQUARE ROOT, BASED ON GUAVA LIBRARY
        def sqrt(v:BigInt) = {
            val ret = BigIntegerMath.sqrt(v.bigInteger, java.math.RoundingMode.HALF_DOWN)
            new BigInt(ret)
        }
        
        // INVERSE OF inkSpent
        def solveAprox(r:BigInt, t:BigInt): (BigInt,BigInt) = {
            val inside = 8*t+4*r*r-4*r+1
            val v = sqrt(inside)
            (v+2*r-1)/4 -> (v-2*r+1)/4
        }
        
        def inkSpent(r:BigInt, circles:BigInt) = {
            circles*(2*circles + r*2-1 )
        }
        
        val aprox = solveAprox(r,t)
        val hints = aprox._1 ::
                    aprox._1 - 1 ::
                    aprox._2 ::
                    aprox._2 - 1 ::
                    Nil
        
        val ink = hints.map(_.toLong).map( v => v -> inkSpent(r,v) )
        val inkInRange = ink.remove{ case (v,inkSpent) => inkSpent>t }
        inkInRange.maxBy{ case (v,inkSpent) => inkSpent>t }._1.toString
    }
    
    
    
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
    type Solutions[T] = Traversable[T]
    def dumpSolution[T](solutions: Solutions[T])(dump: (String) => Unit = println) = {
        solutions.toList.zipWithIndex.map {
            case (sol, cas) => dump(Thread.currentThread().getName() + " Case #" + (cas + 1) + ": " + sol)
        }
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
        dumpSolution(solutions)(out.println)
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
        val nTests = in.next.toInt
        val testsData = for( t <- 1 to nTests ) yield readOneTest(in)
        val td = if( useParalelism ) testsData.par else testsData
        println( "hago el map")
        val ret = td.map( solveOneTest )
        println( "devuelvo el ret" )
        ret.seq
    }



}