package hackercup2013

import java.io._
import scala.util.Random
import leo.Leo

object BalancedSmileys extends App {
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

    def readOneBalancedSmileys( in: LineIterator ): LoadedTest = {
        in.take(1).toArray
    }

    def solveOneBalancedSmileys_lammer( in: LoadedTest ): String = {
        def matchSmileys( s: List[Char], open: Int=0 ): Boolean = {
            if( open < 0 ){
                false
            }
            else s match  {
	            case Nil => open == 0
	            case ':' :: '(' :: xs => matchSmileys(xs,open+1) || matchSmileys(xs, open)
	            case ':' :: ')' :: xs => matchSmileys(xs,open-1) || matchSmileys(xs, open)
	            case ')' :: xs => if( open > 0 ) matchSmileys(xs, open-1) else false
	            case '(' :: xs => matchSmileys( xs, open+1 )
	            case _ :: xs => matchSmileys( xs,open)
            }
        }

        if( matchSmileys( in(0).toList ) ) "YES" else "NO"
    }

    def solveOneBalancedSmileys( in: LoadedTest ): String = {
//        println( "**************************" )
        var open = 0
        var smiley = 0
        var frowny = 0
        var list = in(0).toList
        var ret = true

        while( ret && list != Nil ){
            list match{
                case Nil =>
                case ':' :: ')' :: x =>
                	smiley += 1
		            smiley = smiley.min(open)
		            list = x
                case ':' :: '(' :: x =>
                    frowny += 1
                    list = x
                case '(' :: x =>
   	                open += 1
   	                list = x
                case ')' :: x =>
	                if( open > 0 ){
	                    open -= 1
	                    smiley = smiley.min(open)
	                }
	                else if( frowny > 0 ){
	                    frowny -= 1
	                }
	                else{
	                    ret = false
	                }
	                list = x
                case c :: x =>
                    list = x
            }
        }

        if( smiley - open < 0 ) ret = false

        if(ret) "YES" else "NO"
    }

    def test() = {
	    val r = new Random(1)

	    def randomTestCase(n:Int ): String = {
	        def randomChar = r.nextInt(4) match{
	        	case 0 => ':'
	        	case 1 => '('
	        	case 2 => ')'
	        	case 3 => 'a'
	        }

	        n match {
	            case 0 => ""
	            case i => randomChar + randomTestCase( i-1 )
	        }
	    }

	    for( i <- 1 to 100000 ){
	        var t = randomTestCase(20)
	        var s1 = solveOneBalancedSmileys( Array(t) )
	        var s2 = solveOneBalancedSmileys_lammer( Array(t) )
	        var s3 = if(Leo.isBalanced(0,t)) "YES" else "NO"
	        println( t + " -- " + s1 + "," + s2 + "," + s3)
	        if( !(s1 == s2) || !(s1 == s3) ){
	            throw new IllegalStateException();
	        }
	    }
    }

    test()

//    // LAUNCHS IT ALL
//    val problem = solveOneFile( process(readOneBalancedSmileys)(solveOneBalancedSmileys) )_
//    problem("BalancedSmileys.sample.in")
//    problem("balanced_smileystxt.txt.in")



}