


import scala.annotation.tailrec
import scala.math.Ordering
object CardGame extends App {

    import java.io._

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
    def solveOneFile[T](solveProblem: (LineIterator => Solutions[T]))(file: String) = {

        println( "FILE:" + file )
        val initTime = System.currentTimeMillis()

        val is = if( new File(file).exists )
            new FileInputStream(file)
        else
            getClass().getResourceAsStream(file)

        val in = inputStream2LineInterator(is)
        val solutions = solveProblem(in)

        val fileOut = (if (file.endsWith(".in"))
            file.replace(".in", ".out")
        else
            file + ".out")

        val out = new PrintStream(new FileOutputStream(fileOut), true)
        dumpSolution(solutions)(printToFiles(out, System.out))
        out.close
        is.close

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

    def readOneCardGame( in: LineIterator ): LoadedTest = {
        in.take(2).toArray
    }

    def solveOneCardGame( in: LoadedTest ): Long = {
        val modulo = 1000000007L
        val Array(totalCards, handCards) = in(0) split " "  map( _ toInt )
        val cards = in(1) split " "  map( _ toLong )

        def lammer() = {
            var ret = 0L
            for( s <- cards.toSet.subsets(handCards) ){
                ret += s.toList.max
                ret %= modulo
            }
            ret
        }

        def hacker() = {
        	val sortedCards = cards.sortBy(-_)
        	val binomials = fastBinomialCoefficients(handCards-1).take(totalCards-handCards+1).reverse
        	sortedCards.zip( binomials ).foldLeft(0L)(
        	    (acc, p ) => (acc + (p._1%modulo) * (p._2%modulo).toLong)%modulo
        	)
        }

        val ret = hacker()
        println( "ret:" + ret )
        ret
    }

    def fastBinomialCoefficient(n:Int, k:Int): BigInt = {
        if( n/2 > k ) fastBinomialCoefficient(n, n-k)
        else if( n == 0) 1
        else if( k == 0) 1
        else if( k == n) 1
        else{
            assert( n/2 <= k )
            var ret = BigInt(1)
            for( i <- (k+1) to n ) ret *= i
            for( i <- 2 to (n-k) ) ret /= i
            println( n + "--" + k +":"+ret)
            ret
        }
    }


    def binomialCoefficients(k:Int):Stream[BigInt] = {
        def loop( n: Int ): Stream[BigInt] = fastBinomialCoefficient(n,k) #:: loop(n+1)
        loop(k)
    }

    def fastBinomialCoefficients(k:Int):Stream[BigInt] = {
        lazy val ret:Stream[BigInt] = 1 #:: ret.zipWithIndex.map{
            case (previous,index) => previous*(k+index+1)/(index+1)
        }
        ret
    }


    def computeInfo( totalCards: Int, handCards: Int ){
    	val cards = (1 to totalCards).toSet
    	val subsets = cards.toSet.subsets(handCards)
    	val v = new scala.collection.mutable.HashMap[Int,Int].withDefaultValue(0)
    	for( s <- subsets ){
    	    val max = s.toList.max
    	    v(max) = v(max)+1
    	}
    	val maxs = v.keys.toArray.sorted

    	println( "totalCards:" + totalCards + " -- handCards:" + handCards )
    	var bc = binomialCoefficients(handCards-1)
    	for( m <- maxs ){
    	    println( "\t" + m + "\t-- " + v(m) + " -- " + bc.head )
    	    bc = bc.tail
    	}
    }

    def randomTest(totalCards:Int, handCards:Int): LoadedTest = {
        val l = for( i <- 1 to totalCards ) yield scala.util.Random.nextInt(10).toLong
        var value = 0L
        val values = for( v <- l ) yield { value += v; value }
        Array( totalCards.toString + " " + handCards.toString, values.mkString( " " ) )
    }

    // LAUNCHS IT ALL
    val problem = solveOneFile( process(readOneCardGame)(solveOneCardGame) )_
    problem("CardGame.sample.in")
    problem("card_game.txt")
    solveOneCardGame( randomTest(10000,5000) )
//    problem("balanced_smileystxt.txt.in")

}
